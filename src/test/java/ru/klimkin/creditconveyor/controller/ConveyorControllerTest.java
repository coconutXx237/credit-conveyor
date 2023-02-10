package ru.klimkin.creditconveyor.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import ru.klimkin.creditconveyor.dto.*;
import ru.klimkin.creditconveyor.enums.EmploymentStatus;
import ru.klimkin.creditconveyor.enums.Gender;
import ru.klimkin.creditconveyor.enums.MaritalStatus;
import ru.klimkin.creditconveyor.enums.Position;
import ru.klimkin.creditconveyor.service.impl.CreditServiceImpl;
import ru.klimkin.creditconveyor.service.impl.OfferServiceImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConveyorControllerTest {
    @InjectMocks
    private ConveyorController conveyorController;

    @Mock
    private OfferServiceImpl offerService;
    @Mock
    private CreditServiceImpl creditService;

    @Mock
    private BindingResult bindingResult;

    @Test
    void getOffer() {
        LoanApplicationRequestDTO loanApplicationRequestDTO = getLoanApplicationRequestDTO();
        List<LoanOfferDTO> loanOfferDTOList = getOfferDTOList();
        when(offerService.getOfferList(loanApplicationRequestDTO)).thenReturn(loanOfferDTOList);

        assertNotNull(conveyorController.getOffer(loanApplicationRequestDTO, bindingResult));
        assertEquals(loanOfferDTOList.size(), conveyorController.getOffer(loanApplicationRequestDTO, bindingResult).size());
        assertEquals(loanOfferDTOList, conveyorController.getOffer(loanApplicationRequestDTO, bindingResult));
    }

    @Test
    void getCalculation() {
        ScoringDataDTO scoringDataDTO = getScoringDataDTO();
        CreditDTO creditDTOExpected = getCreditDTO();
        when(creditService.getCredit(scoringDataDTO)).thenReturn(creditDTOExpected);

        assertNotNull(conveyorController.getCalculation(scoringDataDTO, bindingResult));
        assertNotNull(conveyorController.getCalculation(scoringDataDTO, bindingResult).getPaymentSchedule());
        assertEquals(creditDTOExpected, conveyorController.getCalculation(scoringDataDTO, bindingResult));
        assertEquals(creditDTOExpected.getPaymentSchedule(), conveyorController.getCalculation(scoringDataDTO, bindingResult).getPaymentSchedule());
        assertEquals(creditDTOExpected.getPaymentSchedule().size(), conveyorController.getCalculation(scoringDataDTO, bindingResult).getPaymentSchedule().size());
    }

    private List<LoanOfferDTO> getOfferDTOList() {
        return List.of(
                LoanOfferDTO.builder().applicationId((long) 1).requestedAmount(new BigDecimal("300000"))
                        .totalAmount(new BigDecimal("336877.92")).term(18).monthlyPayment(new BigDecimal("18715.44"))
                        .rate(new BigDecimal("15")).isInsuranceEnabled(false).isSalaryClient(false).build(),
                LoanOfferDTO.builder().applicationId((long) 1).requestedAmount(new BigDecimal("300000"))
                        .totalAmount(new BigDecimal("334443.24")).term(18).monthlyPayment(new BigDecimal("18715.44"))
                        .rate(new BigDecimal("14")).isInsuranceEnabled(false).isSalaryClient(true).build(),
                LoanOfferDTO.builder().applicationId((long) 1).requestedAmount(new BigDecimal("300000"))
                        .totalAmount(new BigDecimal("340851.42")).term(18).monthlyPayment(new BigDecimal("18936.19"))
                        .rate(new BigDecimal("10")).isInsuranceEnabled(true).isSalaryClient(false).build(),
                LoanOfferDTO.builder().applicationId((long) 1).requestedAmount(new BigDecimal("300000"))
                        .totalAmount(new BigDecimal("338347.80")).term(18).monthlyPayment(new BigDecimal("18797.10"))
                        .rate(new BigDecimal("9")).isInsuranceEnabled(true).isSalaryClient(true).build()
        );
    }

    private LoanApplicationRequestDTO getLoanApplicationRequestDTO() {
        return LoanApplicationRequestDTO.builder().amount(new BigDecimal("300000")).term(18)
                .firstName("Ivan").lastName("Ivanov").middleName("Ivanovich").email("example@mail.ru")
                .birthDate(LocalDate.of(1993, Month.DECEMBER, 3)).passportSeries("1234")
                .passportNumber("123456").build();
    }

    private ScoringDataDTO getScoringDataDTO() {
        return ScoringDataDTO.builder()
                .amount(new BigDecimal("300000"))
                .term(18)
                .firstName("Ivan")
                .lastName("Ivanov")
                .middleName("Ivanovich")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1994, Month.MARCH, 21))
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

    Integer term = 18;

    private CreditDTO getCreditDTO() {
        return CreditDTO.builder()
                .amount(new BigDecimal("300000"))
                .term(18)
                .monthlyPayment(new BigDecimal("19434.73"))
                .rate(new BigDecimal("20"))
                .psk(new BigDecimal("11.200"))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .paymentSchedule(getPaymentSchedule(new BigDecimal("300000"), term, new BigDecimal("20"),
                        new BigDecimal("19434.73")))
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