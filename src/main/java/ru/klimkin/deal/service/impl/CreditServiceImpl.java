package ru.klimkin.deal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.klimkin.deal.dto.CreditDTO;
import ru.klimkin.deal.dto.FinishRegistrationRequestDTO;
import ru.klimkin.deal.dto.ScoringDataDTO;
import ru.klimkin.deal.entity.Application;
import ru.klimkin.deal.entity.Client;
import ru.klimkin.deal.entity.Credit;
import ru.klimkin.deal.mapper.CreditMapper;
import ru.klimkin.deal.repository.CreditRepository;
import ru.klimkin.deal.service.CreditService;
import ru.klimkin.deal.util.FeignClientService;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditServiceImpl implements CreditService {

    private final CreditRepository creditRepository;
    private final CreditMapper creditMapper;
    private final FeignClientService feignClientService;

    @Override
    public Credit toCredit(CreditDTO creditDTO) {
        return creditMapper.toCredit(creditDTO);
    }

    @Override
    public void createCredit(Credit credit) {
        creditRepository.save(credit);
    }

    @Override
    public CreditDTO getCalculation(FinishRegistrationRequestDTO finishRegistrationRequestDTO,
                                     Application application, Client client) {
        ScoringDataDTO scoringDataDTO = getScoringDataDTO(finishRegistrationRequestDTO, client, application);
        log.info("Sending created ScoringDataDTO for clientId={} to MS credit-conveyor for scoring and calculation",
                client.getClientId());
        return feignClientService.getCalculation(scoringDataDTO);
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
                .passportSeries(client.getPassport().getSeries())
                .passportNumber(client.getPassport().getNumber())
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