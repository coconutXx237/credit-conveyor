package ru.klimkin.creditconveyor.controller;

import ru.klimkin.creditconveyor.dto.CreditDTO;
import ru.klimkin.creditconveyor.dto.LoanApplicationRequestDTO;
import ru.klimkin.creditconveyor.dto.LoanOfferDTO;

import ru.klimkin.creditconveyor.dto.ScoringDataDTO;
import ru.klimkin.creditconveyor.service.impl.CreditServiceImpl;
import ru.klimkin.creditconveyor.service.impl.OfferServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.klimkin.creditconveyor.util.*;

import java.util.List;

@Tag(name="Main controller", description="provides functionality for pre-scoring/scoring and credit calculation")
@RestController
@RequestMapping("/conveyor")
@RequiredArgsConstructor
public class ConveyorController {

    private final OfferServiceImpl offerService;
    private final CreditServiceImpl creditService;

    @Operation(
            summary = "Get loan offer",
            description = "performs pre-scoring and return preliminary loan offer calculation"
    )
    @PostMapping("/offers")
    public List<LoanOfferDTO> getOffer(@RequestBody @Valid LoanApplicationRequestDTO loanApplicationRequestDTO,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }
            throw new LoanApplicationRequestErrorException(errorMsg.toString());
        }
        return offerService.getOfferList(loanApplicationRequestDTO);
    }

    @Operation(
            summary = "Credit calculation",
            description = "performs scoring and return final credit calculation"
    )
    @PostMapping("/calculation")
    public CreditDTO getCalculation(@RequestBody @Valid ScoringDataDTO scoringDataDTO,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }
            throw new ScoringDataErrorException(errorMsg.toString());
        }
        return creditService.getCredit(scoringDataDTO);
    }

    @ExceptionHandler
    private ResponseEntity<LoanApplicationRequestErrorResponse> handleException(LoanApplicationRequestErrorException e) {
        LoanApplicationRequestErrorResponse response = new LoanApplicationRequestErrorResponse(
                "Check that the form is filled out correctly: " + e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ScoringDataErrorResponse> handleException(ScoringDataErrorException e) {
        ScoringDataErrorResponse response = new ScoringDataErrorResponse(
                "Check that the form is filled out correctly: " + e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ScoringDataErrorResponse> handleException(IllegalArgumentException e) {
        ScoringDataErrorResponse response = new ScoringDataErrorResponse(
                "Check that the form is filled out correctly: " + e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ScoringDataErrorResponse> handleException(ScoringResultException e) {
        ScoringDataErrorResponse response = new ScoringDataErrorResponse(
                "The application was declined due to: " + e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}