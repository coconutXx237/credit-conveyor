package ru.klimkin.creditconveyor.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.klimkin.creditconveyor.dto.LoanApplicationRequestDTO;
import ru.klimkin.creditconveyor.dto.LoanOfferDTO;
import ru.klimkin.creditconveyor.service.CalculationService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class OfferServiceImplTest {

    @Mock
    private CalculationService calculationService;

    @InjectMocks
    private OfferServiceImpl offerService;

    @Test
    void getOfferList() {
        BigDecimal amount = new BigDecimal("300000");
        Integer term = 18;

        BigDecimal rateInsFalseSalFalse = new BigDecimal("15");
        BigDecimal rateInsFalseSalTrue = new BigDecimal("14");
        BigDecimal rateInsTrueSalFalse = new BigDecimal("10");
        BigDecimal rateInsTrueSalTrue = new BigDecimal("9");
        when(calculationService.calculateOfferRate(false, false)).thenReturn(rateInsFalseSalFalse);
        when(calculationService.calculateOfferRate(false, true)).thenReturn(rateInsFalseSalTrue);
        when(calculationService.calculateOfferRate(true, false)).thenReturn(rateInsTrueSalFalse);
        when(calculationService.calculateOfferRate(true, true)).thenReturn(rateInsTrueSalTrue);

        BigDecimal monthPayInsFalseSalFalse = new BigDecimal("18715.44");
        BigDecimal monthPayInsFalseSalTrue = new BigDecimal("18580.18");
        BigDecimal monthPayInsTrueSalFalse = new BigDecimal("18936.19");
        BigDecimal monthPayInsTrueSalTrue = new BigDecimal("18797.10");
        when(calculationService.calculateMonthlyPayment(amount,term, rateInsFalseSalFalse, false)).thenReturn(monthPayInsFalseSalFalse);
        when(calculationService.calculateMonthlyPayment(amount,term, rateInsFalseSalTrue, false)).thenReturn(monthPayInsFalseSalTrue);
        when(calculationService.calculateMonthlyPayment(amount,term, rateInsTrueSalFalse, true)).thenReturn(monthPayInsTrueSalFalse);
        when(calculationService.calculateMonthlyPayment(amount,term, rateInsTrueSalTrue, true)).thenReturn(monthPayInsTrueSalTrue);

        BigDecimal totalAmountInsFalseSalFalse = new BigDecimal("336877.92");
        BigDecimal totalAmountInsFalseSalTrue = new BigDecimal("334443.24");
        BigDecimal totalAmountInsTrueSalFalse = new BigDecimal("340851.42");
        BigDecimal totalAmountInsTrueSalTrue = new BigDecimal("338347.80");
        when(calculationService.calculateTotalAmount(monthPayInsFalseSalFalse, term)).thenReturn(totalAmountInsFalseSalFalse);
        when(calculationService.calculateTotalAmount(monthPayInsFalseSalTrue, term)).thenReturn(totalAmountInsFalseSalTrue);
        when(calculationService.calculateTotalAmount(monthPayInsTrueSalFalse, term)).thenReturn(totalAmountInsTrueSalFalse);
        when(calculationService.calculateTotalAmount(monthPayInsTrueSalTrue, term)).thenReturn(totalAmountInsTrueSalTrue);

        LoanApplicationRequestDTO requestDTO = getRequestDTO();
        List<LoanOfferDTO> offerList = offerService.getOfferList(requestDTO);

        verify(calculationService, times(4)).calculateOfferRate(anyBoolean(), anyBoolean());
        verify(calculationService, times(4)).calculateMonthlyPayment(any(), any(), any(), anyBoolean());
        verify(calculationService, times(4)).calculateTotalAmount(any(), any());

        assertNotNull(offerService.getOfferList(requestDTO));

        assertEquals(4, offerList.size());

        assertEquals(1, offerList.get(0).getApplicationId());
        assertEquals(1, offerList.get(1).getApplicationId());
        assertEquals(1, offerList.get(2).getApplicationId());
        assertEquals(1, offerList.get(3).getApplicationId());

        assertEquals(requestDTO.getAmount(), offerList.get(0).getRequestedAmount());
        assertEquals(requestDTO.getAmount(), offerList.get(1).getRequestedAmount());
        assertEquals(requestDTO.getAmount(), offerList.get(2).getRequestedAmount());
        assertEquals(requestDTO.getAmount(), offerList.get(3).getRequestedAmount());

        assertEquals(requestDTO.getTerm(), offerList.get(0).getTerm());
        assertEquals(requestDTO.getTerm(), offerList.get(1).getTerm());
        assertEquals(requestDTO.getTerm(), offerList.get(2).getTerm());
        assertEquals(requestDTO.getTerm(), offerList.get(3).getTerm());

        assertEquals(rateInsFalseSalFalse, offerList.get(0).getRate());
        assertEquals(rateInsFalseSalTrue, offerList.get(1).getRate());
        assertEquals(rateInsTrueSalFalse, offerList.get(2).getRate());
        assertEquals(rateInsTrueSalTrue, offerList.get(3).getRate());

        assertEquals(monthPayInsFalseSalFalse, offerList.get(0).getMonthlyPayment());
        assertEquals(monthPayInsFalseSalTrue, offerList.get(1).getMonthlyPayment());
        assertEquals(monthPayInsTrueSalFalse, offerList.get(2).getMonthlyPayment());
        assertEquals(monthPayInsTrueSalTrue, offerList.get(3).getMonthlyPayment());

        assertEquals(totalAmountInsFalseSalFalse, offerList.get(0).getTotalAmount());
        assertEquals(totalAmountInsFalseSalTrue, offerList.get(1).getTotalAmount());
        assertEquals(totalAmountInsTrueSalFalse, offerList.get(2).getTotalAmount());
        assertEquals(totalAmountInsTrueSalTrue, offerList.get(3).getTotalAmount());
    }

    private LoanApplicationRequestDTO getRequestDTO() {
        return LoanApplicationRequestDTO.builder()
                .amount(new BigDecimal("300000"))
                .term(18)
                .firstName("Ivan")
                .lastName("Ivanov")
                .middleName("Ivanovich")
                .email("example@mail.ru")
                .birthDate(LocalDate.of(1993, Month.DECEMBER, 3))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();
    }
}