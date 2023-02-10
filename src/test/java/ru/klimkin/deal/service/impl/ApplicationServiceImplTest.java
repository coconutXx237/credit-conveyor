package ru.klimkin.deal.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.klimkin.deal.dto.LoanApplicationRequestDTO;
import ru.klimkin.deal.entity.Application;
import ru.klimkin.deal.entity.StatusHistory;
import ru.klimkin.deal.enums.ApplicationStatus;
import ru.klimkin.deal.enums.ChangeType;
import ru.klimkin.deal.repository.ApplicationRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceImplTest {
    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @Mock
    private ApplicationRepository applicationRepository;

    @Test
    void createApplication() {
        Application newApplication = new Application();
        when(applicationRepository.save(newApplication)).thenReturn(newApplication);
        assertEquals(applicationRepository.save(newApplication), newApplication);

        newApplication.setClientId(6L);
        newApplication.setCreationDate(LocalDate.now());
        newApplication.setStatusHistory(new ArrayList<>());

        LoanApplicationRequestDTO request = LoanApplicationRequestDTO.builder().build();
        Application createdApplication = applicationService.createApplication(request, 6L);
        assertEquals(createdApplication, newApplication);
    }

    @Test
    void findApplication() {
        Long applicationId = 6L;
        Optional<Application> newApplication = Optional.of(new Application());
        when(applicationRepository.findByApplicationId(applicationId)).thenReturn(newApplication);
        assertEquals(applicationRepository.findByApplicationId(applicationId), newApplication);

        assertEquals(applicationService.findApplication(applicationId), new Application());
    }

    @Test
    void updateApplication() {
        Application updatedApplication = new Application();
        when(applicationRepository.save(updatedApplication)).thenReturn(updatedApplication);
        assertEquals(applicationRepository.save(updatedApplication), updatedApplication);
    }

    @Test
    void updateStatusHistoryForOffer() {
        Application application = new Application();
        application.setStatusHistory(new ArrayList<>());

        List<StatusHistory> statusHistoryList = application.getStatusHistory();
        statusHistoryList.add(StatusHistory.builder()
                .status(ApplicationStatus.PREAPPROVAL)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC).build());

        assertEquals(applicationService.updateStatusHistoryForOffer(application), statusHistoryList);
    }

    @Test
    void updateStatusHistoryForCalculation() {
        Application application = new Application();
        application.setStatusHistory(new ArrayList<>());

        List<StatusHistory> statusHistoryList = application.getStatusHistory();
        statusHistoryList.add(StatusHistory.builder()
                .status(ApplicationStatus.APPROVED)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC).build());

        assertEquals(applicationService.updateStatusHistoryForCalculation(application), statusHistoryList);
    }
}