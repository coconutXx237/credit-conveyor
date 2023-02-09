package ru.klimkin.deal.service;

import ru.klimkin.deal.dto.FinishRegistrationRequestDTO;
import ru.klimkin.deal.dto.LoanApplicationRequestDTO;
import ru.klimkin.deal.dto.LoanOfferDTO;

import java.util.List;

public interface DealHandleService {

    List<LoanOfferDTO> handleApplication(LoanApplicationRequestDTO request);

    void handleOffer(LoanOfferDTO loanOfferDTO);

    void handleCalculation(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId);
}
