package ru.klimkin.creditconveyor.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.klimkin.creditconveyor.dto.EmploymentDTO;
import ru.klimkin.creditconveyor.dto.PaymentScheduleElement;
import ru.klimkin.creditconveyor.dto.ScoringDataDTO;
import ru.klimkin.creditconveyor.enums.Gender;
import ru.klimkin.creditconveyor.service.CalculationService;
import ru.klimkin.creditconveyor.util.ScoringResultException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class CalculationServiceImpl implements CalculationService {
    @Value("${loan.base-rate}")
    private BigDecimal BASE_RATE;

    public static final long APPLICATION_ID_TEMP = 1;

    @Override
    public BigDecimal calculateOfferRate(boolean isInsuranceEnabled, boolean isSalaryClient) {
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

        log.info("Offer rate: " + rate);
        return rate;
    }

    @Override
    public BigDecimal calculateMonthlyPayment(BigDecimal requestedAmount, Integer term, BigDecimal rate,
                                              boolean isInsuranceEnabled) {
        BigDecimal annuityRatio = calculateAnnuityRatio(term, rate);
        log.info("Annuity ratio: " + annuityRatio);
        return isInsuranceEnabled ?
                ((requestedAmount.add(calculateInsurance(requestedAmount, term)))
                        .multiply(annuityRatio)).setScale(2, RoundingMode.HALF_UP) :
                (requestedAmount.multiply(annuityRatio)).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateTotalAmount(BigDecimal monthlyPayment, Integer term) {
        return monthlyPayment.multiply(BigDecimal.valueOf((long)term));
    }

    @Override
    public BigDecimal calculatePSK(BigDecimal amount, Integer term, BigDecimal monthlyPayment) {
        BigDecimal pskNumerator = ((monthlyPayment.multiply(BigDecimal.valueOf(term)))
                .divide(amount, 5, RoundingMode.HALF_UP)).subtract(BigDecimal.valueOf(1));
        BigDecimal pskDenominator = BigDecimal.valueOf(term).divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);

        return (pskNumerator.divide(pskDenominator, 3, RoundingMode.HALF_UP)).multiply(new BigDecimal("100"));
    }

    @Override
    public BigDecimal calculateCreditRate(ScoringDataDTO scoringDataDTO) {
        log.info("-------------------- Started scoring process -------------------- ");

        BigDecimal rate = BASE_RATE;
        log.info("Base rate: " + rate);

        EmploymentDTO employmentDTO = scoringDataDTO.getEmployment();
        List<String> listOfScoringRefusals = new ArrayList<>();
        long age = calculateAge(scoringDataDTO.getBirthdate());
        log.info("Age: " + age);

        if (scoringDataDTO.getIsInsuranceEnabled()) {
            rate = rate.subtract(new BigDecimal("5"));
            log.info("---Base rate decreased by 5 due to enabled insurance");
        }
        if (scoringDataDTO.getIsSalaryClient()) {
            rate = rate.subtract(new BigDecimal("1"));
            log.info("---Base rate decreased by 1 due to enabled salary client program");
        }
        switch (employmentDTO.getEmploymentStatus()) {
            case UNEMPLOYED -> listOfScoringRefusals.add("Reason of refusal: the applicant cannot be unemployed");
            case SELF_EMPLOYED -> {
                rate = rate.add(new BigDecimal("1"));
                log.info("---Base rate increased by 1 due to employment status SELF_EMPLOYED");
            }
            case BUSINESS_OWNER -> {
                rate = rate.add(new BigDecimal("3"));
                log.info("---Base rate increased by 3 due to employment status BUSINESS_OWNER");
            }
        }
        switch (employmentDTO.getPosition()) {
            case MID_MANAGER -> {
                rate = rate.subtract(new BigDecimal("2"));
                log.info("---Base rate decreased by 2 due to position MID_MANAGER");
            }
            case TOP_MANAGER -> {
                rate = rate.subtract(new BigDecimal("4"));
                log.info("---Base rate decreased by 4 due to position TOP_MANAGER");
            }
        }
        switch (scoringDataDTO.getMaritalStatus()) {
            case MARRIED -> {
                rate = rate.subtract(new BigDecimal("3"));
                log.info("---Base rate decreased by 3 due to marital status MARRIED");
            }
            case DIVORCED -> {
                rate = rate.add(new BigDecimal("1"));
                log.info("---Base rate increased by 1 due to marital status DIVORCED");
            }
        }
        if (scoringDataDTO.getAmount().compareTo(employmentDTO.getSalary().multiply(new BigDecimal("20"))) > 0) {
            listOfScoringRefusals.add("Reason of refusal: the amount of requested loan is too big");
        }
        if (scoringDataDTO.getDependentAmount() > 1) {
            rate = rate.add(new BigDecimal("1"));
            log.info("---Base rate increased by 1 due to dependent more than one");
        }
        if (age < 20 || age > 60) {
            listOfScoringRefusals.add("Reason of refusal: the age of applicant must be between 20 and 60 years old " +
                    "inclusively");
        }
        if (scoringDataDTO.getGender().equals(Gender.FEMALE) && age >= 35 && age <= 60) {
            rate = rate.subtract(new BigDecimal("3"));
            log.info("---Base rate decreased by 3 due to client is FEMALE of relevant age (between 35 and 60");
        }
        if (scoringDataDTO.getGender().equals(Gender.MALE) && age >= 30 && age <= 55) {
            rate = rate.subtract(new BigDecimal("3"));
            log.info("---Base rate decreased by 3 due to client is MALE of relevant age (between 30 and 55");
        }
        if (scoringDataDTO.getGender().equals(Gender.NON_BINARY)) {
            rate = rate.add(new BigDecimal("3"));
            log.info("---Base rate increased by 3 due to client is NON_BINARY");
        }
        if (employmentDTO.getWorkExperienceTotal() < 12) {
            listOfScoringRefusals.add("Reason of refusal: the total working experience is too small");
        }
        if (employmentDTO.getWorkExperienceCurrent() < 3) {
            listOfScoringRefusals.add("Reason of refusal: the current working experience is too small");
        }
        if (!listOfScoringRefusals.isEmpty()) {
            log.info("Scoring process revealed the following inconsistencies: {}",
                    Arrays.deepToString(listOfScoringRefusals.toArray()));
            throw new ScoringResultException(Arrays.deepToString(listOfScoringRefusals.toArray()));
        }
        log.info("-------------------- Completed scoring process -------------------- ");
        return rate;
    }

    @Override
    public List<PaymentScheduleElement> calculatePaymentSchedule(BigDecimal amount, Integer term, BigDecimal rate,
                                                                 BigDecimal monthlyPayment) {
        List<PaymentScheduleElement> paymentSchedule = new ArrayList<>();
        LocalDate paymentDate = LocalDate.now();
        BigDecimal interestPayment = calculateInterestPayment(amount, rate, paymentDate);
        BigDecimal debtPayment = monthlyPayment.subtract(interestPayment);
        BigDecimal remainingDebt = amount.subtract(debtPayment);

        for (int i = 1; i <= term; i++) {
            if (i == 1) {
                paymentDate = paymentDate.plusMonths(1);

                paymentSchedule.add(PaymentScheduleElement.builder()
                        .number(i)
                        .date(paymentDate)
                        .totalPayment(monthlyPayment)
                        .interestPayment(interestPayment)
                        .debtPayment(debtPayment)
                        .remainingDebt(remainingDebt)
                        .build());
            } else if (i == term) {
                interestPayment = calculateInterestPayment(remainingDebt, rate, paymentDate);
                debtPayment = remainingDebt;
                remainingDebt = remainingDebt.subtract(debtPayment);
                paymentDate = paymentDate.plusMonths(1);

                paymentSchedule.add(PaymentScheduleElement.builder()
                        .number(i)
                        .date(paymentDate)
                        .totalPayment(debtPayment.add(interestPayment))
                        .interestPayment(interestPayment)
                        .debtPayment(debtPayment)
                        .remainingDebt(remainingDebt)
                        .build());
            } else {
                interestPayment = calculateInterestPayment(remainingDebt, rate, paymentDate);
                debtPayment = monthlyPayment.subtract(interestPayment);
                remainingDebt = remainingDebt.subtract(debtPayment);
                paymentDate = paymentDate.plusMonths(1);

                paymentSchedule.add(PaymentScheduleElement.builder()
                        .number(i)
                        .date(paymentDate)
                        .totalPayment(monthlyPayment)
                        .interestPayment(interestPayment)
                        .debtPayment(debtPayment)
                        .remainingDebt(remainingDebt)
                        .build());
            }
        }
        return paymentSchedule;
    }

    private BigDecimal calculateInterestPayment(BigDecimal remainingDebt, BigDecimal rate, LocalDate paymentDate) {
        long daysPerCurrentMonth = paymentDate.lengthOfMonth();
        long daysPerCurrentYear = paymentDate.lengthOfYear();

        return (remainingDebt.multiply(rate.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP))
                .multiply(BigDecimal.valueOf(daysPerCurrentMonth)))
                .divide(BigDecimal.valueOf(daysPerCurrentYear), 2, RoundingMode.HALF_UP);
    }

    private long calculateAge(LocalDate birtDate) {
        LocalDate today = LocalDate.now();
        return ChronoUnit.YEARS.between(birtDate, today);
    }

    private BigDecimal calculateMonthlyRate(BigDecimal rate) {
        return (rate.divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_UP))
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateAnnuityRatio(Integer term, BigDecimal rate) {
        BigDecimal monthlyRate = calculateMonthlyRate(rate);
        log.info("Monthly rate: " + monthlyRate);

        BigDecimal denominatorForAnnuityRatioCalc = monthlyRate.add(new BigDecimal("1")).pow(term);
        log.info("Denominator for calculation of annuity ratio : " + denominatorForAnnuityRatioCalc);

        return (monthlyRate.multiply(denominatorForAnnuityRatioCalc))
                .divide((denominatorForAnnuityRatioCalc.subtract(BigDecimal.valueOf(1))), 11, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateInsurance(BigDecimal requestedAmount, Integer term) {
        BigDecimal insuranceCost = new BigDecimal("10000")
                .add((requestedAmount.divide(new BigDecimal("1000"), 2, RoundingMode.HALF_UP))
                        .multiply(BigDecimal.valueOf(term)));
        log.info("Insurance cost: " + insuranceCost);

        return insuranceCost;
    }
}