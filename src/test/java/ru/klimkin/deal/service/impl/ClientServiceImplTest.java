package ru.klimkin.deal.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.klimkin.deal.dto.LoanApplicationRequestDTO;
import ru.klimkin.deal.entity.Application;
import ru.klimkin.deal.entity.Client;
import ru.klimkin.deal.entity.Passport;
import ru.klimkin.deal.mapper.ClientMapper;
import ru.klimkin.deal.mapper.EmploymentMapper;
import ru.klimkin.deal.repository.ClientRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {
    @InjectMocks
    private ClientServiceImpl clientService;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private EmploymentMapper employmentMapper;

    @Test
    void createClient() {
        Client mockClient = new Client();
        when(clientMapper.toClient(LoanApplicationRequestDTO.builder().build())).thenReturn(new Client());
        Passport passport = Passport.builder().build();
        mockClient.setPassport(passport);
        when(clientRepository.save(mockClient)).thenReturn(mockClient);
        LoanApplicationRequestDTO request = LoanApplicationRequestDTO.builder().build();
        Client client = clientMapper.toClient(request);
        client.setPassport(Passport.builder()
                .series(request.getPassportSeries())
                .number(request.getPassportNumber()).build());

        assertEquals(clientRepository.save(client), mockClient);
        assertEquals(clientService.createClient(request), mockClient);
        assertEquals(clientMapper.toClient(request), client);
    }

    @Test
    void findClient() {
        Long clientId = 6L;
        Optional<Client> client = Optional.of(new Client());
        when(clientRepository.findById(clientId)).thenReturn(client);
        assertEquals(clientRepository.findById(clientId), client);

        assertEquals(clientService.findClient(clientId), new Client());
    }

    @Test
    void updateEmployment() {
    }

    @Test
    void enrichPassport() {
    }
}