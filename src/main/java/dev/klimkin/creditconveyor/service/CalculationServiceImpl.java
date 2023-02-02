package dev.klimkin.creditconveyor.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
public class CalculationServiceImpl implements CalculationService{
    @Override
    public BigDecimal calculateMonthlyRate(BigDecimal rate) {
        return (rate.divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_UP))
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateAnnuityRatio(Integer term, BigDecimal rate) {
        BigDecimal monthlyRate = calculateMonthlyRate(rate);
        log.info("Monthly rate: " + monthlyRate);

        BigDecimal denominatorForAnnuityRatioCalc = monthlyRate.add(BigDecimal.valueOf(1)).pow(term);
        log.info("Denominator for calculation of annuity ratio : " + denominatorForAnnuityRatioCalc);

        return (monthlyRate.multiply(denominatorForAnnuityRatioCalc))
                .divide((denominatorForAnnuityRatioCalc.subtract(BigDecimal.valueOf(1))), 11, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateInsurance(BigDecimal requestedAmount, Integer term) {
        BigDecimal insuranceCost = BigDecimal.valueOf(10000)
                .add((requestedAmount.divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP))
                        .multiply(BigDecimal.valueOf(term)));
        log.info("Insurance cost: " + insuranceCost);

        return insuranceCost;
    }

    @Override
    public BigDecimal calculateMonthlyPayment(BigDecimal requestedAmount, Integer term, BigDecimal annuityRatio,
                                              boolean isInsuranceEnabled) {
        return isInsuranceEnabled ?
                ((requestedAmount.add(calculateInsurance(requestedAmount, term)))
                        .multiply(annuityRatio)).setScale(2, RoundingMode.HALF_UP) :
                (requestedAmount.multiply(annuityRatio)).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateTotalAmount(BigDecimal monthlyPayment, Integer term) {
        return monthlyPayment.multiply(BigDecimal.valueOf((long)term));
    }
}
