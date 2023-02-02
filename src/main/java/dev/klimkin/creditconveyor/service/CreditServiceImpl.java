package dev.klimkin.creditconveyor.service;

import dev.klimkin.creditconveyor.dto.*;
import dev.klimkin.creditconveyor.util.ScoringResultException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditServiceImpl implements CreditService {
    @Value("${loan.base-rate}")
    private BigDecimal BASE_RATE;

    private final CalculationService calculationService;

    @Override
    public CreditDTO calculateCredit(ScoringDataDTO scoringDataDTO) {
        log.info(" * * * * * * * * * * * Started credit calculation for client {} {} * * * * * * * * * * * ",
                scoringDataDTO.getFirstName(), scoringDataDTO.getLastName());

        BigDecimal amount = scoringDataDTO.getAmount();
        log.info("Requested amount: " + amount);

        BigDecimal rate = calculateFinalRate(scoringDataDTO);
        log.info("Final rate: " + rate);

        Integer term = scoringDataDTO.getTerm();
        log.info("Requested term: " + term);

        BigDecimal annuityRatio = calculationService.calculateAnnuityRatio(term, rate);
        log.info("Annuity ratio: " + annuityRatio);

        BigDecimal monthlyPayment = calculationService
                .calculateMonthlyPayment(amount, term, annuityRatio, scoringDataDTO.getIsInsuranceEnabled());
        log.info("Monthly payment: " + monthlyPayment);

        BigDecimal psk = calculatePSK(term, amount, monthlyPayment);
        log.info("PSK: " + psk);

        log.info(" * * * * * * * * * * * Completed credit calculation for client {} {} * * * * * * * * * * * \n",
                scoringDataDTO.getFirstName(), scoringDataDTO.getLastName());
        return CreditDTO
                .builder()
                .amount(amount)
                .term(term)
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .psk(psk)
                .isInsuranceEnabled(scoringDataDTO.getIsInsuranceEnabled())
                .isSalaryClient(scoringDataDTO.getIsSalaryClient())
                .paymentSchedule(calculatePaymentSchedule(term, monthlyPayment, amount, rate))
                .build();
    }

    private BigDecimal calculateFinalRate(ScoringDataDTO scoringDataDTO) {
        log.info("-------------------- Started scoring process -------------------- ");

        BigDecimal rate = BASE_RATE;
        log.info("Base rate: " + rate);

        EmploymentDTO employmentDTO = scoringDataDTO.getEmployment();
        List<String> listOfScoringRefusals = new ArrayList<>();
        long age = calculateAge(scoringDataDTO.getBirthdate());
        log.info("Age: " + age);

        if (scoringDataDTO.getIsInsuranceEnabled()) {
            rate = rate.subtract(BigDecimal.valueOf(5));
            log.info("---Base rate decreased by 5 due to enabled insurance");
        }
        if (scoringDataDTO.getIsSalaryClient()) {
            rate = rate.subtract(BigDecimal.valueOf(1));
            log.info("---Base rate decreased by 1 due to enabled salary client program");
        }
        switch (employmentDTO.getEmploymentStatus()) {
            case UNEMPLOYED -> listOfScoringRefusals.add("Reason of refusal: the applicant cannot be unemployed");
            case SELF_EMPLOYED -> {
                rate = rate.add(BigDecimal.valueOf(1));
                log.info("---Base rate increased by 1 due to employment status SELF_EMPLOYED");
            }
            case BUSINESS_OWNER -> {
                rate = rate.add(BigDecimal.valueOf(3));
                log.info("---Base rate increased by 3 due to employment status BUSINESS_OWNER");
            }
        }
        switch (employmentDTO.getPosition()){
            case MID_MANAGER -> {
                rate = rate.subtract(BigDecimal.valueOf(2));
                log.info("---Base rate decreased by 2 due to position MID_MANAGER");
            }
            case TOP_MANAGER -> {
                rate = rate.subtract(BigDecimal.valueOf(4));
                log.info("---Base rate decreased by 2 due to position TOP_MANAGER");
            }
        }
        switch (scoringDataDTO.getMaritalStatus()){
            case MARRIED -> {
                rate = rate.subtract(BigDecimal.valueOf(3));
                log.info("---Base rate decreased by 3 due to marital status MARRIED");
            }
            case DIVORCED -> {
                rate = rate.add(BigDecimal.valueOf(1));
                log.info("---Base rate increased by 1 due to marital status DIVORCED");
            }
        }
        if (scoringDataDTO.getAmount().compareTo(employmentDTO.getSalary().multiply(BigDecimal.valueOf(20))) > 0){
            listOfScoringRefusals.add("Reason of refusal: the amount of requested loan is too big");
        }
        if (scoringDataDTO.getDependentAmount() > 1) {
            rate = rate.add(BigDecimal.valueOf(1));
            log.info("---Base rate increased by 1 due to dependent more than one");
        }
        if (age < 20 || age > 60) {
            listOfScoringRefusals.add("Reason of refusal: the age of applicant must be between 20 and 60 years old " +
                    "inclusively");
        }
        if (scoringDataDTO.getGender().equals(Gender.FEMALE) && age >= 35 && age <= 60){
            rate = rate.subtract(BigDecimal.valueOf(3));
            log.info("---Base rate decreased by 3 due to client is FEMALE of relevant age (between 35 and 60");
        }
        if (scoringDataDTO.getGender().equals(Gender.MALE) && age >= 30 && age <= 55){
            rate = rate.subtract(BigDecimal.valueOf(3));
            log.info("---Base rate decreased by 3 due to client is MALE of relevant age (between 30 and 55");
        }
        if (scoringDataDTO.getGender().equals(Gender.NON_BINARY)){
            rate = rate.add(BigDecimal.valueOf(3));
            log.info("---Base rate increased by 3 due to client is NON_BINARY");
        }
        if (employmentDTO.getWorkExperienceTotal() < 12){
            listOfScoringRefusals.add("Reason of refusal: the total working experience is too small");
        }
        if (employmentDTO.getWorkExperienceCurrent() < 3){
            listOfScoringRefusals.add("Reason of refusal: the current working experience is too small");
        }
        if (!listOfScoringRefusals.isEmpty()){
            log.info("Scoring process revealed the following inconsistencies: {}",
                    Arrays.deepToString(listOfScoringRefusals.toArray()));
            throw new ScoringResultException(Arrays.deepToString(listOfScoringRefusals.toArray()));
        }
        log.info("-------------------- Completed scoring process -------------------- ");
        return rate;
    }

    private long calculateAge(LocalDate birtDate) {
        LocalDate today = LocalDate.now();
        return ChronoUnit.YEARS.between(birtDate, today);
    }

    private List<PaymentScheduleElement> calculatePaymentSchedule(Integer term, BigDecimal monthlyPayment,
                                                                  BigDecimal amount, BigDecimal rate) {
        List<PaymentScheduleElement> paymentSchedule = new ArrayList<>();
        LocalDate paymentDate = LocalDate.now();
        BigDecimal interestPayment = calculateInterestPayment(amount, rate, paymentDate);
        BigDecimal debtPayment = monthlyPayment.subtract(interestPayment);
        BigDecimal remainingDebt = amount.subtract(debtPayment);

        for (int i = 1; i <= term; i++){
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

        long daysPerCurrentYear= paymentDate.lengthOfYear();

        return (remainingDebt.multiply(rate.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP))
                .multiply(BigDecimal.valueOf(daysPerCurrentMonth)))
                .divide(BigDecimal.valueOf(daysPerCurrentYear), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculatePSK(Integer term, BigDecimal amount, BigDecimal monthlyPayment) {
        BigDecimal pskNumerator = ((monthlyPayment.multiply(BigDecimal.valueOf(term)))
                .divide(amount, 5, RoundingMode.HALF_UP)).subtract(BigDecimal.valueOf(1));
        BigDecimal pskDenominator = BigDecimal.valueOf(term).divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);

        return (pskNumerator.divide(pskDenominator, 3, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(100));
    }
}