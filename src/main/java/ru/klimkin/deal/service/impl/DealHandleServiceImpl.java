package ru.klimkin.deal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.klimkin.deal.dto.*;
import ru.klimkin.deal.entity.Application;
import ru.klimkin.deal.entity.Client;
import ru.klimkin.deal.entity.Credit;
import ru.klimkin.deal.entity.Passport;
import ru.klimkin.deal.enums.ApplicationStatus;
import ru.klimkin.deal.enums.CreditStatus;
import ru.klimkin.deal.service.DealHandleService;
import ru.klimkin.deal.util.FeignClientService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DealHandleServiceImpl implements DealHandleService {

    private final ClientServiceImpl clientService;
    private final ApplicationServiceImpl applicationService;
    private final CreditServiceImpl creditService;
    private final FeignClientService feignClientService;

    @Override
    public List<LoanOfferDTO> handleApplicationStage(LoanApplicationRequestDTO request) {
        log.info("Processing LoanApplicationRequest for client {} {}", request.getFirstName(), request.getLastName());
        Client client = clientService.createClient(request);
        log.info("Created client {} {} with clientId={}", request.getFirstName(), request.getLastName(),
                client.getClientId());
        Application application = applicationService.createApplication(request, client.getClientId());
        log.info("Created application for client {} {} with applicationId={}", request.getFirstName(), request.getLastName(),
                application.getApplicationId());
        log.info("Sending LoanApplicationRequest for client {} {} with clientId={} to MS credit-conveyor for pre-scoring",
                request.getFirstName(), request.getLastName(), client.getClientId());
        List<LoanOfferDTO> loanOfferDTOList = feignClientService.getOffer(request);
        log.info("Received list of 4 loan offers for client {} {} with applicationId={} from MS credit-conveyor after pre-scoring",
                request.getFirstName(), request.getLastName(), client.getClientId());
        loanOfferDTOList.forEach(e -> e.setOfferApplicationId(application.getApplicationId()));
        log.info("Added clientId={} to each offer in the LoanOfferList for client {} {}",
                client.getClientId(), request.getFirstName(), request.getLastName());

        log.info("Passing the processed LoanOfferList for clientId={} back to API\n", client.getClientId());
        return loanOfferDTOList;
    }

    @Override
    public void handleOfferStage(LoanOfferDTO loanOfferDTO) {
        log.info("Processing LoanOffer with applicationId={}", loanOfferDTO.getOfferApplicationId());
        Application application = applicationService.findApplication(loanOfferDTO.getOfferApplicationId());
        log.info("Took out application with ID={} from DataBase", loanOfferDTO.getOfferApplicationId());
        application.setApplicationStatus(ApplicationStatus.PREAPPROVAL);
        log.info("Set applicationStatus with ID={} to {}", loanOfferDTO.getOfferApplicationId(), ApplicationStatus.PREAPPROVAL);
        application.setStatusHistory(applicationService.updateStatusHistoryForOffer(application));
        log.info("Updated offerStage StatusHistory for applicationId={}", application.getApplicationId());
        application.setAppliedOffer(loanOfferDTO);
        log.info("LoanOffer with applicationId={} was set as applied", loanOfferDTO.getOfferApplicationId());
        applicationService.updateApplication(application);
        log.info("Updated the application with applicationId={} and saved to DataBase\n", loanOfferDTO.getOfferApplicationId());
    }

    @Override
    public void handleCalculationStage(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId) {
        log.info("Processing the scoring and calculation stage for applicationId={}", applicationId);
        Application application = applicationService.findApplication(applicationId);
        log.info("Took out application with ID={} from DataBase", applicationId);
        Client client = clientService.findClient(application.getClientId());
        log.info("Took out client with ID={} from DataBase", application.getClientId());
        Passport passport = client.getPassport();
        log.info("Took out passport for clientId={} from client entity", application.getClientId());

        client.setEmployment(clientService.updateEmployment(finishRegistrationRequestDTO.getEmployment(), client.getClientId()));
        client.setPassport(clientService.enrichPassport(passport, finishRegistrationRequestDTO, client));
        client.setMaritalStatus(finishRegistrationRequestDTO.getMaritalStatus());
        log.info("Set maritalStatus to {} for clientId={}", finishRegistrationRequestDTO.getMaritalStatus(), client.getClientId());
        client.setGender(finishRegistrationRequestDTO.getGender());
        log.info("Set gender to {} for clientId={}", finishRegistrationRequestDTO.getGender(), client.getClientId());
        client.setDependentAmount(finishRegistrationRequestDTO.getDependentAmount());
        log.info("Set dependentAmount to {} for clientId={}", finishRegistrationRequestDTO.getDependentAmount(), client.getClientId());
        clientService.updateClient(client);
        log.info("Updated client with Id={} was saved into DataBase", client.getClientId());

        CreditDTO creditDTO = creditService.getCalculation(finishRegistrationRequestDTO, application, client);
        log.info("Received the scored and calculated CreditDTO from MS credit-conveyor for clientId={}", client.getClientId());

        Credit credit = creditService.toCredit(creditDTO);
        log.info("Mapped CreditDTO to Credit entity object for clientId={}", client.getClientId());

        credit.setCreditStatus(CreditStatus.CALCULATED);
        log.info("Set credit status to {} for clientId={}", CreditStatus.CALCULATED, client.getClientId());
        creditService.createCredit(credit);
        log.info("Saved updated credit entity with ID={} into DataBase for clientId={}", credit.getCreditId(), client.getClientId());

        application.setCreditId(credit.getCreditId());
        log.info("Set creditId={} in application with ID={}", credit.getCreditId(), application.getApplicationId());

        application.setApplicationStatus(ApplicationStatus.APPROVED);
        log.info("Set applicationStatus to {} for application with ID={}", ApplicationStatus.APPROVED, application.getApplicationId());
        application.setStatusHistory(applicationService.updateStatusHistoryForCalculation(application));
        log.info("Updated calculationStage StatusHistory for applicationId={}", application.getApplicationId());

        applicationService.updateApplication(application);
        log.info("Saved updated application with ID={} into DataBase", application.getApplicationId());
    }
}
