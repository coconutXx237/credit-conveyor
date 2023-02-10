package ru.klimkin.deal.service;

import ru.klimkin.deal.dto.FinishRegistrationRequestDTO;
import ru.klimkin.deal.dto.LoanApplicationRequestDTO;
import ru.klimkin.deal.dto.LoanOfferDTO;

import java.util.List;

public interface DealHandleService {

    List<LoanOfferDTO> handleApplicationStage(LoanApplicationRequestDTO request);

    void handleOfferStage(LoanOfferDTO loanOfferDTO);

    void handleCalculationStage(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId);
}
