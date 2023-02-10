package ru.klimkin.deal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.klimkin.deal.dto.FinishRegistrationRequestDTO;
import ru.klimkin.deal.dto.LoanApplicationRequestDTO;
import ru.klimkin.deal.dto.LoanOfferDTO;
import ru.klimkin.deal.service.impl.DealHandleServiceImpl;
import ru.klimkin.deal.util.ApplicationErrorResponse;
import ru.klimkin.deal.util.ApplicationNotFoundException;
import ru.klimkin.deal.util.ClientErrorResponse;

import java.util.List;

@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
public class DealController {

    private final DealHandleServiceImpl dealHandleService;

    @PostMapping("/application")
    public List<LoanOfferDTO> getOffer(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
        return dealHandleService.handleApplicationStage(loanApplicationRequestDTO);
    }

    @PutMapping("/offer")
    public void chooseOffer(@RequestBody LoanOfferDTO loanOfferDTO) {
        dealHandleService.handleOfferStage(loanOfferDTO);
    }

    @PutMapping("/calculate/{applicationId}")
    public void getCalculation(@RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO,
                               @PathVariable("applicationId") Long applicationId) {
        dealHandleService.handleCalculationStage(finishRegistrationRequestDTO, applicationId);
    }



    @ExceptionHandler
    private ResponseEntity<ApplicationErrorResponse> handleException(ApplicationNotFoundException e) {
        ApplicationErrorResponse response = new ApplicationErrorResponse(
                "Application with such ID was not found!",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ClientErrorResponse> handleException(ClassCastException e) {
        ClientErrorResponse response = new ClientErrorResponse(
                "Client with such ID was not found!",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
