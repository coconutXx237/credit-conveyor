package ru.klimkin.deal.service;

import ru.klimkin.deal.dto.LoanApplicationRequestDTO;
import ru.klimkin.deal.entity.Application;

import java.util.Optional;

public interface ApplicationService {

    Application createApplication(LoanApplicationRequestDTO request, Long clientId);

    Application findApplication(Long id);

    void updateApplication(Application updatedApplication);
}
