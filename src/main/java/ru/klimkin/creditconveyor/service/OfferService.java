package ru.klimkin.creditconveyor.service;

import ru.klimkin.creditconveyor.dto.LoanApplicationRequestDTO;
import ru.klimkin.creditconveyor.dto.LoanOfferDTO;


import java.util.List;

public interface OfferService {

    List<LoanOfferDTO> getOfferList(LoanApplicationRequestDTO loanApplicationRequestDTO);

}
