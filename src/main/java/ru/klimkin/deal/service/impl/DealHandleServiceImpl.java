package ru.klimkin.deal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.klimkin.deal.dto.*;
import ru.klimkin.deal.entity.Application;
import ru.klimkin.deal.entity.Client;
import ru.klimkin.deal.entity.Credit;
import ru.klimkin.deal.entity.StatusHistory;
import ru.klimkin.deal.enums.ApplicationStatus;
import ru.klimkin.deal.enums.ChangeType;
import ru.klimkin.deal.enums.CreditStatus;
import ru.klimkin.deal.service.DealHandleService;
import ru.klimkin.deal.util.FeignClientService;

import java.time.LocalDateTime;
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
    public List<LoanOfferDTO> handleApplication(LoanApplicationRequestDTO request) {
        Client client = clientService.createClient(request);
        Application application = applicationService.createApplication(request, client.getClientId());

        List<LoanOfferDTO> loanOfferDTOList = feignClientService.getOffer(request);
        loanOfferDTOList.forEach(e -> e.setApplicationId(application.getApplicationId()));

        return loanOfferDTOList;
    }

    @Override
    public void handleOffer(LoanOfferDTO loanOfferDTO) {
        Application application = applicationService.findApplication(loanOfferDTO.getApplicationId());

        List<StatusHistory> statusHistoryList = application.getStatusHistory();

        application.setApplicationStatus(ApplicationStatus.PREAPPROVAL);
        statusHistoryList.add(StatusHistory.builder()
                .status(ApplicationStatus.PREAPPROVAL)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC).build());

        application.setStatusHistory(statusHistoryList);
        application.setAppliedOffer(loanOfferDTO);
        applicationService.updateApplication(application);
    }

    @Override
    public void handleCalculation(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId) {
        Application application = applicationService.findApplication(applicationId);

        Client client = clientService.findClient(application.getClientId());
        client.getPassportId().setIssueBranch(finishRegistrationRequestDTO.getPassportIssueBranch());
        client.getPassportId().setIssueDate(finishRegistrationRequestDTO.getPassportIssueDate());
        clientService.updateClient(client);

        CreditDTO creditDTO = feignClientService.getCalculation(getScoringDataDTO(finishRegistrationRequestDTO, client,
                application));

        Credit credit = creditService.toCredit(creditDTO);
        credit.setCreditStatus(CreditStatus.CALCULATED);
        creditService.createCredit(credit);
    }

    private ScoringDataDTO getScoringDataDTO(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Client client,
                                             Application application) {
        return ScoringDataDTO.builder()
                .amount(application.getAppliedOffer().getRequestedAmount())
                .term(application.getAppliedOffer().getTerm())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .middleName(client.getMiddleName())
                .gender(finishRegistrationRequestDTO.getGender())
                .birthDate(client.getBirthDate())
                .passportSeries(client.getPassportId().getSeries())
                .passportNumber(client.getPassportId().getNumber())
                .passportIssueDate(finishRegistrationRequestDTO.getPassportIssueDate())
                .passportIssueBranch(finishRegistrationRequestDTO.getPassportIssueBranch())
                .maritalStatus(finishRegistrationRequestDTO.getMaritalStatus())
                .dependentAmount(finishRegistrationRequestDTO.getDependentAmount())
                .employment(finishRegistrationRequestDTO.getEmployment())
                .account(finishRegistrationRequestDTO.getAccount())
                .isInsuranceEnabled(application.getAppliedOffer().getIsInsuranceEnabled())
                .isSalaryClient(application.getAppliedOffer().getIsSalaryClient()).build();
    }
}