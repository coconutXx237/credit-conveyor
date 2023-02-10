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
        Client client = clientService.createClient(request);
        Application application = applicationService.createApplication(request, client.getClientId());

        List<LoanOfferDTO> loanOfferDTOList = feignClientService.getOffer(request);
        loanOfferDTOList.forEach(e -> e.setApplicationId(application.getApplicationId()));

        return loanOfferDTOList;
    }

    @Override
    public void handleOfferStage(LoanOfferDTO loanOfferDTO) {
        Application application = applicationService.findApplication(loanOfferDTO.getApplicationId());
        application.setApplicationStatus(ApplicationStatus.PREAPPROVAL);
        application.setStatusHistory(applicationService.updateStatusHistoryForOffer(application));
        application.setAppliedOffer(loanOfferDTO);
        applicationService.updateApplication(application);
    }

    @Override
    public void handleCalculationStage(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId) {
        Application application = applicationService.findApplication(applicationId);
        Client client = clientService.findClient(application.getClientId());
        Passport passport = client.getPassport();

        client.setEmployment(clientService.updateEmployment(finishRegistrationRequestDTO.getEmployment(), client.getClientId()));
        client.setPassport(clientService.enrichPassport(passport, finishRegistrationRequestDTO, client));
        client.setMaritalStatus(finishRegistrationRequestDTO.getMaritalStatus());
        client.setGender(finishRegistrationRequestDTO.getGender());
        client.setDependentAmount(finishRegistrationRequestDTO.getDependentAmount());
        clientService.updateClient(client);

        CreditDTO creditDTO = creditService.getCalculation(finishRegistrationRequestDTO, application, client);

        Credit credit = creditService.toCredit(creditDTO);
        credit.setCreditStatus(CreditStatus.CALCULATED);
        creditService.createCredit(credit);

        application.setCreditId(credit.getCreditId());
        application.setApplicationStatus(ApplicationStatus.APPROVED);
        application.setStatusHistory(applicationService.updateStatusHistoryForCalculation(application));
        applicationService.updateApplication(application);
    }
}