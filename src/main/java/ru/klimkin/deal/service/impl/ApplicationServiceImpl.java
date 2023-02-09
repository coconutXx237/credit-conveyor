package ru.klimkin.deal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.klimkin.deal.dto.LoanApplicationRequestDTO;
import ru.klimkin.deal.entity.Application;
import ru.klimkin.deal.repository.ApplicationRepository;
import ru.klimkin.deal.service.ApplicationService;
import ru.klimkin.deal.util.ApplicationNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
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
    public Application findApplication(Long id) {
        System.out.println("--------------------Long id : " + id);
        System.out.println("--------------------foundApplication: " + applicationRepository.findById(id));
        Optional<Application> foundApplication = applicationRepository.findById(id);
        return foundApplication.orElseThrow(ApplicationNotFoundException::new);
    }

    @Override
    public void updateApplication(Application updatedApplication) {
        applicationRepository.save(updatedApplication);
    }
}
