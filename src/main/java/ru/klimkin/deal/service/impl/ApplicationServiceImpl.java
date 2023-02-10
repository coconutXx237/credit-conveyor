package ru.klimkin.deal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.klimkin.deal.dto.LoanApplicationRequestDTO;
import ru.klimkin.deal.entity.Application;
import ru.klimkin.deal.entity.StatusHistory;
import ru.klimkin.deal.enums.ApplicationStatus;
import ru.klimkin.deal.enums.ChangeType;
import ru.klimkin.deal.repository.ApplicationRepository;
import ru.klimkin.deal.service.ApplicationService;
import ru.klimkin.deal.util.ApplicationNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {

private final ApplicationRepository applicationRepository;

    @Override
    public Application createApplication(LoanApplicationRequestDTO request, Long clientId) {
        Application newApplication = new Application();
        newApplication.setClientId(clientId);
        newApplication.setCreationDate(LocalDate.now());
        newApplication.setStatusHistory(new ArrayList<>());

        return applicationRepository.save(newApplication);
    }

    @Override
    public Application findApplication(Long applicationId) {
        Optional<Application> foundApplication = applicationRepository.findByApplicationId(applicationId);
        return foundApplication.orElseThrow(ApplicationNotFoundException::new);
    }

    @Override
    public void updateApplication(Application updatedApplication) {
        applicationRepository.save(updatedApplication);
    }

    @Override
    public List<StatusHistory> updateStatusHistoryForOffer(Application application) {
        List<StatusHistory> statusHistoryList = application.getStatusHistory();
        statusHistoryList.add(StatusHistory.builder()
                .status(ApplicationStatus.PREAPPROVAL)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC).build());

        return statusHistoryList;
    }

    @Override
    public List<StatusHistory> updateStatusHistoryForCalculation(Application application) {
        List<StatusHistory> statusHistoryList = application.getStatusHistory();
        statusHistoryList.add(StatusHistory.builder()
                .status(ApplicationStatus.APPROVED)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC).build());

        return statusHistoryList;
    }

}