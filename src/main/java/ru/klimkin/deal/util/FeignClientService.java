package ru.klimkin.deal.util;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.klimkin.deal.dto.CreditDTO;
import ru.klimkin.deal.dto.LoanApplicationRequestDTO;
import ru.klimkin.deal.dto.LoanOfferDTO;
import ru.klimkin.deal.dto.ScoringDataDTO;

import java.util.List;

@FeignClient(name = "conveyor", url = "http://localhost:8080/conveyor")
public interface FeignClientService {

    @PostMapping("/offers")
    List<LoanOfferDTO> getOffer(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO);

    @PostMapping("/calculation")
    CreditDTO getCalculation(@RequestBody ScoringDataDTO scoringDataDTO);
}
