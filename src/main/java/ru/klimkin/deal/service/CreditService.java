package ru.klimkin.deal.service;

import ru.klimkin.deal.dto.CreditDTO;
import ru.klimkin.deal.dto.FinishRegistrationRequestDTO;
import ru.klimkin.deal.entity.Application;
import ru.klimkin.deal.entity.Client;
import ru.klimkin.deal.entity.Credit;

public interface CreditService {

    Credit toCredit(CreditDTO creditDTO);

    void createCredit(Credit credit);

    CreditDTO getCalculation(FinishRegistrationRequestDTO finishRegistrationRequestDTO,
                                    Application application, Client client);
}
