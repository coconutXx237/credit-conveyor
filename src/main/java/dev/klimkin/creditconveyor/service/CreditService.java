package dev.klimkin.creditconveyor.service;

import dev.klimkin.creditconveyor.dto.CreditDTO;
import dev.klimkin.creditconveyor.dto.ScoringDataDTO;

public interface CreditService {

    CreditDTO calculateCredit(ScoringDataDTO scoringDataDTO);
}
