package ru.klimkin.creditconveyor.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.klimkin.creditconveyor.dto.EmploymentDTO;
import ru.klimkin.creditconveyor.dto.PaymentScheduleElement;
import ru.klimkin.creditconveyor.dto.ScoringDataDTO;
import ru.klimkin.creditconveyor.enums.EmploymentStatus;
import ru.klimkin.creditconveyor.enums.Gender;
import ru.klimkin.creditconveyor.enums.MaritalStatus;
import ru.klimkin.creditconveyor.enums.Position;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class CalculationServiceImplTest {
    @Autowired
    private CalculationServiceImpl calculationService;

    private final BigDecimal amount = new BigDecimal("300000");
    private final Integer term = 18;
    private final BigDecimal psk = new BigDecimal("11.100");
    private final BigDecimal monthlyPaymentOffer = new BigDecimal("18715.44");
    private final BigDecimal monthlyPaymentCredit = new BigDecimal("19434.73");
    private final BigDecimal offerRate = new BigDecimal("15");
    private final BigDecimal creditRate = new BigDecimal("20");
    private final BigDecimal totalAmountOffer = monthlyPaymentOffer.multiply(BigDecimal.valueOf(term));

    private final ScoringDataDTO scoringDataDTO = getScoringDataDTO();

    @Test
    void shouldCalculateRateOffer(){
        assertNotNull(calculationService.calculateOfferRate(false, false));
        assertEquals(new BigDecimal("15"), calculationService.calculateOfferRate(false, false));
    }
    @Test
    void shouldCalculateMonthlyPaymentForOffer(){
        assertNotNull(calculationService.calculateMonthlyPayment(amount, term, offerRate, false));
        assertEquals(monthlyPaymentOffer, calculationService.calculateMonthlyPayment(amount, term, offerRate, false));
    }
    @Test
    void shouldCalculateMonthlyPaymentForCredit(){
        assertNotNull(calculationService.calculateMonthlyPayment(amount, term, creditRate, false));
        assertEquals(monthlyPaymentCredit, calculationService.calculateMonthlyPayment(amount, term, creditRate, false));
    }

    @Test
    void shouldCalculatePskCredit(){
        assertNotNull(calculationService.calculatePSK(amount, term, monthlyPaymentCredit));
        assertEquals(psk, calculationService.calculatePSK(amount, term, monthlyPaymentCredit));
    }

    @Test
    void shouldCalculateTotalAmountOffer(){
        assertNotNull(calculationService.calculateTotalAmount(monthlyPaymentOffer, term));
        assertEquals(totalAmountOffer, calculationService.calculateTotalAmount(monthlyPaymentOffer, term));
    }

    @Test
    void shouldCalculatePaymentScheduleCredit() {
        assertNotNull( calculationService.calculatePaymentSchedule(amount, term, creditRate, monthlyPaymentCredit));
        assertEquals(getPaymentSchedule(amount, term, creditRate, monthlyPaymentCredit),
                calculationService.calculatePaymentSchedule(amount, term, creditRate, monthlyPaymentCredit));
    }

    @Test
    void shouldCalculateCreditRate() {
        assertNotNull(calculationService.calculateCreditRate(scoringDataDTO));
        assertEquals(creditRate, calculationService.calculateCreditRate(scoringDataDTO));
    }

    private ScoringDataDTO getScoringDataDTO() {
        return ScoringDataDTO.builder()
                .amount(new BigDecimal("300000"))
                .term(18)
                .firstName("Ivan")
                .lastName("Ivanov")
                .middleName("Ivanovich")
                .gender(Gender.MALE)
                .birthdate(LocalDate.of(1994, Month.MARCH, 21))
                .passportSeries("1234")
                .passportNumber("123123")
                .passportIssueDate(LocalDate.of(2004, Month.JUNE, 16))
                .passportIssueBranch("TP # 11 OUFMS MSC")
                .maritalStatus(MaritalStatus.DIVORCED)
                .dependentAmount(3)
                .employment(getEmploymentDTO())
                .account("111222333")
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();
    }

    private EmploymentDTO getEmploymentDTO() {
        return EmploymentDTO.builder()
                .employmentStatus(EmploymentStatus.BUSINESS_OWNER)
                .employerINN("123456789012")
                .salary(new BigDecimal("100000"))
                .position(Position.OWNER)
                .workExperienceTotal(15)
                .workExperienceCurrent(4)
                .build();
    }

    private BigDecimal calculateInterestPayment(BigDecimal remainingDebt, BigDecimal rate, LocalDate paymentDate) {
        long daysPerCurrentMonth = paymentDate.lengthOfMonth();
        long daysPerCurrentYear = paymentDate.lengthOfYear();

        return (remainingDebt.multiply(rate.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP))
                .multiply(BigDecimal.valueOf(daysPerCurrentMonth)))
                .divide(BigDecimal.valueOf(daysPerCurrentYear), 2, RoundingMode.HALF_UP);
    }

    private List<PaymentScheduleElement> getPaymentSchedule(BigDecimal amount, Integer term, BigDecimal rate,
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
}