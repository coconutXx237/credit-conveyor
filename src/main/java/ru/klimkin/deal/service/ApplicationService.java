package ru.klimkin.deal.service;

import ru.klimkin.deal.dto.LoanApplicationRequestDTO;
import ru.klimkin.deal.entity.Application;
import ru.klimkin.deal.entity.StatusHistory;

import java.util.List;

public interface ApplicationService {

    Application createApplication(LoanApplicationRequestDTO request, Long clientId);

    Application findApplication(Long id);

    void updateApplication(Application updatedApplication);

    List<StatusHistory> updateStatusHistoryForOffer(Application application);

    List<StatusHistory> updateStatusHistoryForCalculation(Application application);
}