package ru.klimkin.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name="Main controller", description="provides functionality via FeignClient to send data for pre-scoring, " +
        "to handle the chosen offer and to send data for scoring")
@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
public class DealController {

    private final DealHandleServiceImpl dealHandleService;

    @Operation(
            summary = "Get loan offer",
            description = "Sends data for pre-scoring to MS credit-conveyor"
    )
    @PostMapping("/application")
    public List<LoanOfferDTO> getOffer(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
        return dealHandleService.handleApplicationStage(loanApplicationRequestDTO);
    }

    @Operation(
            summary = "Handle the chosen offer",
            description = "Handles the chosen offer within inner services business logic"
    )
    @PutMapping("/offer")
    public void chooseOffer(@RequestBody LoanOfferDTO loanOfferDTO) {
        dealHandleService.handleOfferStage(loanOfferDTO);
    }

    @Operation(
            summary = "Get scored and calculated credit",
            description = "Finishes the registration process, sends the drawn up Scoring data to MS credit-conveyor for " +
                    "scoring and calculation, then handles the approved credit within inner services business logic"
    )
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