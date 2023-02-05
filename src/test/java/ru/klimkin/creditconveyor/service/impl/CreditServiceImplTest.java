package ru.klimkin.creditconveyor.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.klimkin.creditconveyor.dto.CreditDTO;
import ru.klimkin.creditconveyor.dto.EmploymentDTO;
import ru.klimkin.creditconveyor.dto.ScoringDataDTO;
import ru.klimkin.creditconveyor.enums.EmploymentStatus;
import ru.klimkin.creditconveyor.enums.Gender;
import ru.klimkin.creditconveyor.enums.MaritalStatus;
import ru.klimkin.creditconveyor.enums.Position;
import ru.klimkin.creditconveyor.service.CalculationService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditServiceImplTest {

    @Mock
    private CalculationService calculationService;

    @InjectMocks
    private CreditServiceImpl creditService;

    @Test
    void getCredit() {
        BigDecimal amount = new BigDecimal("300000");
        Integer term = 18;
        BigDecimal rate = new BigDecimal("20");
        boolean IsInsuranceEnabled = false;
        BigDecimal monthlyPayment = new BigDecimal("19434.73");
        BigDecimal psk = new BigDecimal("11.100");

        when(calculationService.calculateCreditRate(getScoringDataDTO())).thenReturn(rate);
        when((calculationService.calculateMonthlyPayment(amount, term, rate, IsInsuranceEnabled))).thenReturn(monthlyPayment);
        when(calculationService.calculatePSK(amount, term, monthlyPayment)).thenReturn(psk);

        ScoringDataDTO scoringDataDTO = getScoringDataDTO();
        CreditDTO creditDTO = creditService.getCredit(scoringDataDTO);

        assertNotNull(creditService.getCredit(scoringDataDTO));

        assertEquals(scoringDataDTO.getAmount(), creditDTO.getAmount());
        assertEquals(scoringDataDTO.getTerm(), creditDTO.getTerm());
        assertEquals(scoringDataDTO.getIsInsuranceEnabled(), creditDTO.getIsInsuranceEnabled());
        assertEquals(scoringDataDTO.getIsSalaryClient(), creditDTO.getIsSalaryClient());

        assertEquals(rate, creditDTO.getRate());
        assertEquals(monthlyPayment, creditDTO.getMonthlyPayment());
        assertEquals(psk, creditDTO.getPsk());
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

}