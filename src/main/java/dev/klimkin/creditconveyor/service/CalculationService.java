package dev.klimkin.creditconveyor.service;

import java.math.BigDecimal;

public interface CalculationService {
    BigDecimal calculateMonthlyRate(BigDecimal rate);
    BigDecimal calculateAnnuityRatio(Integer term, BigDecimal rate);
    BigDecimal calculateInsurance(BigDecimal requestedAmount, Integer term);
    BigDecimal calculateMonthlyPayment(BigDecimal requestedAmount, Integer term, BigDecimal annuityRatio,
                                       boolean isInsuranceEnabled);
    BigDecimal calculateTotalAmount(BigDecimal monthlyPayment, Integer term);
}