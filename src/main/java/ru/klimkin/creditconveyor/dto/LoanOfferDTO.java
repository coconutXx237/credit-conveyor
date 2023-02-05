package ru.klimkin.creditconveyor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Schema(description = "Calculated loan offer")
@Data
@Builder
public class LoanOfferDTO {

    @Schema(description = "Id of application")
    private Long applicationId;

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
