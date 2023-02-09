package ru.klimkin.deal.service;

import ru.klimkin.deal.dto.CreditDTO;
import ru.klimkin.deal.entity.Credit;

public interface CreditService {

    Credit toCredit(CreditDTO creditDTO);

    void createCredit(Credit credit);
}
