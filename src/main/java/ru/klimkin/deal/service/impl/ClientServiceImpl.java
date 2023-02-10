package ru.klimkin.deal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.klimkin.deal.dto.EmploymentDTO;
import ru.klimkin.deal.dto.FinishRegistrationRequestDTO;
import ru.klimkin.deal.dto.LoanApplicationRequestDTO;
import ru.klimkin.deal.entity.Client;
import ru.klimkin.deal.entity.Employment;
import ru.klimkin.deal.entity.Passport;
import ru.klimkin.deal.mapper.ClientMapper;
import ru.klimkin.deal.mapper.EmploymentMapper;
import ru.klimkin.deal.repository.ClientRepository;
import ru.klimkin.deal.service.ClientService;
import ru.klimkin.deal.util.ClientNotFoundException;


@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final EmploymentMapper employmentMapper;

    public Client createClient(LoanApplicationRequestDTO request) {
        Client client = clientMapper.toClient(request);
        client.setPassport(Passport.builder()
                        .series(request.getPassportSeries())
                        .number(request.getPassportNumber()).build());
        log.info("Set passportSeries to {}, set passportNumber to {} for client {} {}",
                request.getPassportSeries(), request.getPassportNumber(), request.getFirstName(), request.getLastName());
        return clientRepository.save(client);
    }

    @Override
    public Client findClient(Long clientId) {
        return clientRepository.findById(clientId).orElseThrow(ClientNotFoundException::new);
    }

    @Override
    public void updateClient(Client updatedClient) {
        clientRepository.save(updatedClient);
    }

    @Override
    public Employment updateEmployment(EmploymentDTO employmentDTO, Long clientId) {
        Employment employment = employmentMapper.toEmployment(employmentDTO);
        log.info("Mapped EmploymentDTO to Employment entity object for clientId={}", clientId);
        employment.setEmploymentId(clientId);
        log.info("Set employmentId as {} for clientId={}", clientId, clientId);
        return employment;
    }

    @Override
    public Passport enrichPassport(Passport passport, FinishRegistrationRequestDTO finishRegistrationRequestDTO, Client client) {
        passport.setIssueBranch(finishRegistrationRequestDTO.getPassportIssueBranch());
        log.info("Passport issue branch set to {} for clientId={}", finishRegistrationRequestDTO.getPassportIssueBranch(),
                client.getClientId());
        passport.setIssueDate(finishRegistrationRequestDTO.getPassportIssueDate());
        log.info("Passport issue date set to {} for clientId={}", finishRegistrationRequestDTO.getPassportIssueDate(),
                client.getClientId());
        passport.setPassportId(client.getClientId());
        log.info("Passport ID set to {} for clientId={}", client.getClientId(), client.getClientId());
        return passport;
    }
}