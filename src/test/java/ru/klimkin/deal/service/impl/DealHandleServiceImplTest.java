package ru.klimkin.deal.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.klimkin.deal.dto.LoanApplicationRequestDTO;
import ru.klimkin.deal.dto.LoanOfferDTO;
import ru.klimkin.deal.entity.Application;
import ru.klimkin.deal.entity.Client;
import ru.klimkin.deal.util.FeignClientService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealHandleServiceImplTest {
    @InjectMocks
    private DealHandleServiceImpl dealHandleService;
    @Mock
    private ClientServiceImpl clientService;
    @Mock
    private ApplicationServiceImpl applicationService;
    @Mock
    private CreditServiceImpl creditService;
    @Mock
    private FeignClientService feignClientService;

    @Test
    void handleApplicationStage() {
        LoanApplicationRequestDTO loanApplicationRequestDTO = getRequestDTO();
        List<LoanOfferDTO> testLoanOfferDTOList = getLoanOfferDTOList();

        when(clientService.createClient(loanApplicationRequestDTO)).thenReturn(new Client());
        when(applicationService.createApplication(loanApplicationRequestDTO,  null)).thenReturn(new Application());
        when(dealHandleService.handleApplicationStage(loanApplicationRequestDTO)).thenReturn(testLoanOfferDTOList);

        Client client = clientService.createClient(loanApplicationRequestDTO);
        Application application = applicationService.createApplication(loanApplicationRequestDTO, client.getClientId());
        List<LoanOfferDTO> loanOfferDTOList = feignClientService.getOffer(loanApplicationRequestDTO);
        loanOfferDTOList.forEach(e -> e.setOfferApplicationId(application.getApplicationId()));

        assertEquals(dealHandleService.handleApplicationStage(loanApplicationRequestDTO), testLoanOfferDTOList);
    }

    @Test
    void handleOfferStage() {
    }

    @Test
    void handleCalculationStage() {
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

    private List<LoanOfferDTO> getLoanOfferDTOList() {
        return List.of(
                new LoanOfferDTO(1L, new BigDecimal("300000"), new BigDecimal("336877.92"),
                        18, new BigDecimal("18715.44"), new BigDecimal("15"), false, false),
                new LoanOfferDTO(1L, new BigDecimal("300000"), new BigDecimal("334443.24"),
                        18, new BigDecimal("18580.18"), new BigDecimal("14"), false, true),
                new LoanOfferDTO(1L, new BigDecimal("300000"), new BigDecimal("340851.42"),
                        18, new BigDecimal("18936.19"), new BigDecimal("10"), true, false),
                new LoanOfferDTO(1L, new BigDecimal("300000"), new BigDecimal("338347.80"),
                        18, new BigDecimal("18797.10"), new BigDecimal("9"), true, true));
    }
}
