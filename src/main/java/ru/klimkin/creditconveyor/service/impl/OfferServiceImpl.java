package ru.klimkin.creditconveyor.service.impl;

import ru.klimkin.creditconveyor.dto.LoanApplicationRequestDTO;
import ru.klimkin.creditconveyor.dto.LoanOfferDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.klimkin.creditconveyor.service.CalculationService;
import ru.klimkin.creditconveyor.service.OfferService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static ru.klimkin.creditconveyor.service.impl.CalculationServiceImpl.APPLICATION_ID_TEMP;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfferServiceImpl implements OfferService {

    private final CalculationService calculationService;

    public List<LoanOfferDTO> getOfferList(LoanApplicationRequestDTO requestDTO) {
        log.info("---------------- Drawing up loan offer for client {} {} with application id: {}",
                requestDTO.getFirstName(), requestDTO.getLastName(), APPLICATION_ID_TEMP);

        List<LoanOfferDTO> loanOfferList = new ArrayList<>();
        log.info("Creating list of 4 loan offers\n");

        log.info("*************** Calculating loan offer #1:");
        loanOfferList.add(calculateOffer(requestDTO, false, false));
        log.info("*** Loan offer #1 was added to the loan offer list\n");

        log.info("*************** Calculating loan offer #2:");
        loanOfferList.add(calculateOffer(requestDTO, false, true));
        log.info("*** Loan offer #2 was added to the loan offer list\n");

        log.info("*************** Calculating loan offer #3:");
        loanOfferList.add(calculateOffer(requestDTO, true, false));
        log.info("*** Loan offer #3 was added to the loan offer list\n");

        log.info("*************** Calculating loan offer #4:");
        loanOfferList.add(calculateOffer(requestDTO, true, true));
        log.info("*** Loan offer #4 was added to the loan offer list");

        log.info("---------------- The loan offer for client {} {} was drawn up\n",
                requestDTO.getFirstName(), requestDTO.getLastName());
        return loanOfferList;
    }

    private LoanOfferDTO calculateOffer(LoanApplicationRequestDTO requestDTO,
                                        boolean isInsuranceEnabled, boolean isSalaryClient){
        BigDecimal rate = calculationService.calculateOfferRate(isInsuranceEnabled, isSalaryClient);
        log.info("Requested term: " + requestDTO.getTerm());
        log.info("Requested loan amount : " + requestDTO.getAmount());

        BigDecimal monthlyPayment = calculationService.calculateMonthlyPayment(requestDTO.getAmount(),
                requestDTO.getTerm(), rate, isInsuranceEnabled);
        log.info("Monthly payment: " + monthlyPayment);

        BigDecimal totalAmount = calculationService.calculateTotalAmount(monthlyPayment, requestDTO.getTerm());
        log.info("Total amount of all monthly payments: " + totalAmount);

        return LoanOfferDTO
                .builder()
                .applicationId(APPLICATION_ID_TEMP)
                .requestedAmount(requestDTO.getAmount())
                .totalAmount(totalAmount)
                .term(requestDTO.getTerm())
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .build();
    }
}

