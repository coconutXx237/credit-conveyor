package ru.klimkin.creditconveyor.service;

import ru.klimkin.creditconveyor.dto.CreditDTO;
import ru.klimkin.creditconveyor.dto.ScoringDataDTO;

public interface CreditService {

    CreditDTO getCredit(ScoringDataDTO scoringDataDTO);
}
