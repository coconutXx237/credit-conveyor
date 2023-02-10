package ru.klimkin.deal.service;

import ru.klimkin.deal.dto.EmploymentDTO;
import ru.klimkin.deal.dto.FinishRegistrationRequestDTO;
import ru.klimkin.deal.dto.LoanApplicationRequestDTO;
import ru.klimkin.deal.entity.Client;
import ru.klimkin.deal.entity.Employment;
import ru.klimkin.deal.entity.Passport;

public interface ClientService {

    Client createClient(LoanApplicationRequestDTO loanApplicationRequestDTO);

    Client findClient(Long clientId);

    void updateClient(Client updatedClient);

    Employment updateEmployment(EmploymentDTO employmentDTO, Long clientId);

    Passport enrichPassport(Passport passport, FinishRegistrationRequestDTO finishRegistrationRequestDTO, Client client);
}
