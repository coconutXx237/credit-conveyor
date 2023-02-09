package ru.klimkin.deal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.klimkin.deal.dto.LoanApplicationRequestDTO;
import ru.klimkin.deal.entity.Client;
import ru.klimkin.deal.entity.Passport;
import ru.klimkin.deal.mapper.ClientMapper;
import ru.klimkin.deal.repository.ClientRepository;
import ru.klimkin.deal.service.ClientService;
import ru.klimkin.deal.util.ClientNotFoundException;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public Client createClient(LoanApplicationRequestDTO request) {
        Client client = clientMapper.toClient(request);
        client.setPassportId(Passport.builder()
                        .passportId(client.getClientId())
                        .series(request.getPassportSeries())
                        .number(request.getPassportNumber())
                .build());

        return clientRepository.save(client);
    }

    @Override
    public Client findClient(Long clientId) {
        Optional<Client> foundClient = clientRepository.findById(clientId);
        return foundClient.orElseThrow(ClientNotFoundException::new);
    }

    @Override
    public void updateClient(Client updatedClient) {
        clientRepository.save(updatedClient);
    }
}
