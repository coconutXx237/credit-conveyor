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
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    @Override
    public Application createApplication(LoanApplicationRequestDTO request, Long clientId) {
        Application newApplication = new Application();
        newApplication.setClientId(clientId);
        log.info("Added clientId={} into new application for client {} {}", clientId, request.getFirstName(), request.getLastName());
        newApplication.setCreationDate(LocalDate.now());
        log.info("Added creationDate={} into new application for client {} {}", LocalDate.now(), request.getFirstName(), request.getLastName());
        newApplication.setStatusHistory(new ArrayList<>());
        log.info("Added empty statusHistory into new application for client {} {}", request.getFirstName(), request.getLastName());


        return applicationRepository.save(newApplication);
    }

    @Override
    public Application findApplication(Long applicationId) {
        return applicationRepository.findByApplicationId(applicationId).orElseThrow(ApplicationNotFoundException::new);
    }

    @Override
    public void updateApplication(Application updatedApplication) {
        applicationRepository.save(updatedApplication);
    }

    @Override
    public List<StatusHistory> updateStatusHistoryForOffer(Application application) {
        log.info("Updating offerStage StatusHistory for applicationId={}", application.getApplicationId());
        List<StatusHistory> statusHistoryList = application.getStatusHistory();
        statusHistoryList.add(StatusHistory.builder()
                .status(ApplicationStatus.PREAPPROVAL)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC).build());
        log.info("ApplicationStatus set to {}, time set to {}, ChangeType set to {} for applicationId={}",
                ApplicationStatus.PREAPPROVAL, LocalDateTime.now(), ChangeType.AUTOMATIC, application.getApplicationId());
        return statusHistoryList;
    }

    @Override
    public List<StatusHistory> updateStatusHistoryForCalculation(Application application) {
        log.info("Updating calculationStage StatusHistory for applicationId={}", application.getApplicationId());
        List<StatusHistory> statusHistoryList = application.getStatusHistory();
        statusHistoryList.add(StatusHistory.builder()
                .status(ApplicationStatus.APPROVED)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC).build());
        log.info("ApplicationStatus set to {}, time set to {}, ChangeType set to {} for applicationId={}",
                ApplicationStatus.APPROVED, LocalDateTime.now(), ChangeType.AUTOMATIC, application.getApplicationId());
        return statusHistoryList;
    }

}