package dev.klimkin.creditconveyor.service;

import dev.klimkin.creditconveyor.dto.LoanApplicationRequestDTO;
import dev.klimkin.creditconveyor.dto.LoanOfferDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class OfferServiceImpl implements OfferService {
    @Value("${loan.base-rate}")
    private BigDecimal BASE_RATE;

    public static final long APPLICATION_ID_TEMP = 1;

    private final CalculationService calculationService;

    public List<LoanOfferDTO> getLoanOffer(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("---------------- Drawing up loan offer for client {} {} with application id: {}",
                loanApplicationRequestDTO.getFirstName(), loanApplicationRequestDTO.getLastName(), APPLICATION_ID_TEMP);

        List<LoanOfferDTO> loanOfferList = new ArrayList<>();
        log.info("Creating list of 4 loan offers\n");

        log.info("*************** Calculating loan offer #1:");
        loanOfferList.add(calculateLoanOffer(loanApplicationRequestDTO, false, false));
        log.info("*** Loan offer #1 was added to the loan offer list\n");

        log.info("*************** Calculating loan offer #2:");
        loanOfferList.add(calculateLoanOffer(loanApplicationRequestDTO, false, true));
        log.info("*** Loan offer #2 was added to the loan offer list\n");

        log.info("*************** Calculating loan offer #3:");
        loanOfferList.add(calculateLoanOffer(loanApplicationRequestDTO, true, false));
        log.info("*** Loan offer #3 was added to the loan offer list\n");

        log.info("*************** Calculating loan offer #4:");
        loanOfferList.add(calculateLoanOffer(loanApplicationRequestDTO, true, true));
        log.info("*** Loan offer #4 was added to the loan offer list");

        log.info("---------------- The loan offer for client {} {} was drawn up\n",
                loanApplicationRequestDTO.getFirstName(), loanApplicationRequestDTO.getLastName());
        return loanOfferList;
    }

    private LoanOfferDTO calculateLoanOffer(LoanApplicationRequestDTO loanApplicationRequestDTO,
                                            boolean isInsuranceEnabled, boolean isSalaryClient){
        BigDecimal rate = calculateLoanRate(isInsuranceEnabled, isSalaryClient);
        Integer term = loanApplicationRequestDTO.getTerm();
        log.info("Requested term: " + term);

        BigDecimal requestedAmount = loanApplicationRequestDTO.getAmount();
        log.info("Requested loan amount : " + requestedAmount);

        BigDecimal annuityRatio = calculationService.calculateAnnuityRatio(term, rate);
        log.info("Annuity ratio: " + annuityRatio);

        BigDecimal monthlyPayment = calculationService.calculateMonthlyPayment(requestedAmount, term, annuityRatio,
                isInsuranceEnabled);
        log.info("Monthly payment: " + monthlyPayment);

        BigDecimal totalAmount = calculationService.calculateTotalAmount(monthlyPayment, term);
        log.info("Total amount of all monthly payments: " + totalAmount);

        return LoanOfferDTO
                .builder()
                .applicationId(APPLICATION_ID_TEMP)
                .requestedAmount(loanApplicationRequestDTO.getAmount())
                .totalAmount(totalAmount)
                .term(loanApplicationRequestDTO.getTerm())
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .build();
    }

    private BigDecimal calculateLoanRate(boolean isInsuranceEnabled, boolean isSalaryClient) {
        BigDecimal rate = BASE_RATE;
        log.info("Base rate: " + rate);

        if (isInsuranceEnabled) {
            rate = rate.subtract(BigDecimal.valueOf(5));
            log.info("---Base rate decreased by 5 due to enabled insurance");
        }
        if (isSalaryClient) {
            rate = rate.subtract(BigDecimal.valueOf(1));
            log.info("---Base rate decreased by 1 due to enabled salary client program");
        }

        log.info("Loan rate: " + rate);
        return rate;
    }
}

