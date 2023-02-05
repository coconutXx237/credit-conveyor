package ru.klimkin.creditconveyor.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.klimkin.creditconveyor.dto.CreditDTO;
import ru.klimkin.creditconveyor.dto.PaymentScheduleElement;
import ru.klimkin.creditconveyor.dto.ScoringDataDTO;
import ru.klimkin.creditconveyor.service.CalculationService;
import ru.klimkin.creditconveyor.service.CreditService;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditServiceImpl implements CreditService {

    private final CalculationService calculationService;

    @Override
    public CreditDTO getCredit(ScoringDataDTO scoringDataDTO) {
        return calculateCredit(scoringDataDTO);
    }

    private CreditDTO calculateCredit(ScoringDataDTO scoringDataDTO) {
        log.info(" * * * * * * * * * * * Started credit calculation for client {} {} * * * * * * * * * * * ",
                scoringDataDTO.getFirstName(), scoringDataDTO.getLastName());
        log.info("Requested amount: " + scoringDataDTO.getAmount());
        log.info("Requested term: " + scoringDataDTO.getTerm());

        BigDecimal rate = calculationService.calculateCreditRate(scoringDataDTO);
        log.info("Final rate: " + rate);

        BigDecimal monthlyPayment = calculationService.calculateMonthlyPayment(scoringDataDTO.getAmount(),
                scoringDataDTO.getTerm(), rate, scoringDataDTO.getIsInsuranceEnabled());
        log.info("Monthly payment: " + monthlyPayment);

        BigDecimal psk = calculationService.calculatePSK(scoringDataDTO.getAmount(), scoringDataDTO.getTerm(), monthlyPayment);
        log.info("PSK: " + psk);

        List<PaymentScheduleElement> paymentSchedule = calculationService.calculatePaymentSchedule(scoringDataDTO.getAmount(),
                scoringDataDTO.getTerm(), rate, monthlyPayment);

        log.info(" * * * * * * * * * * * Completed credit calculation for client {} {} * * * * * * * * * * * \n",
                scoringDataDTO.getFirstName(), scoringDataDTO.getLastName());

        return CreditDTO
                .builder()
                .amount(scoringDataDTO.getAmount())
                .term(scoringDataDTO.getTerm())
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .psk(psk)
                .isInsuranceEnabled(scoringDataDTO.getIsInsuranceEnabled())
                .isSalaryClient(scoringDataDTO.getIsSalaryClient())
                .paymentSchedule(paymentSchedule)
                .build();
    }
}