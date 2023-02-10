package ru.klimkin.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.math.BigDecimal;

@Schema(description = "Calculated loan offer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanOfferDTO implements Serializable {

    @Schema(description = "Id of application")
    private Long offerApplicationId;

    @Schema(description = "Requested amount of credit")
    private BigDecimal requestedAmount;

    @Schema(description = "Total amount of credit to be returned")
    private BigDecimal totalAmount;

    @Schema(description = "Requested term of credit")
    private Integer term;

    @Schema(description = "Monthly payment")
    private BigDecimal monthlyPayment;

    @Schema(description = "Calculated rate")
    private BigDecimal rate;

    @Schema(description = "If insurance is required")
    private Boolean isInsuranceEnabled;

    @Schema(description = "If salary client program is required")
    private Boolean isSalaryClient;
}