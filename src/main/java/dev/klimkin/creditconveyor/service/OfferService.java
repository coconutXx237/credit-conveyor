package dev.klimkin.creditconveyor.service;

import dev.klimkin.creditconveyor.dto.LoanApplicationRequestDTO;
import dev.klimkin.creditconveyor.dto.LoanOfferDTO;


import java.util.List;

public interface OfferService {

    List<LoanOfferDTO> getLoanOffer(LoanApplicationRequestDTO loanApplicationRequestDTO);

}
