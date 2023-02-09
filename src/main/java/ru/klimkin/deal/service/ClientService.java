package ru.klimkin.deal.service;

import ru.klimkin.deal.dto.LoanApplicationRequestDTO;
import ru.klimkin.deal.entity.Client;

public interface ClientService {

    Client createClient(LoanApplicationRequestDTO loanApplicationRequestDTO);

    Client findClient(Long clientId);

    void updateClient(Client updatedClient);
}
