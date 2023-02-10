package ru.klimkin.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Contains credit payment schedule")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentScheduleElementDTO {

    @Schema(description = "Payment number")
    private Integer number;

    @Schema(description = "Date of payment")
    private LocalDate date;

    @Schema(description = "Total monthly payment (interest + debt")
    private BigDecimal totalPayment;

    @Schema(description = "Monthly interest payment")
    private BigDecimal interestPayment;

    @Schema(description = "Monthly debt payment")
    private BigDecimal debtPayment;

    @Schema(description = "Monthly remaining debt")
    private BigDecimal remainingDebt;
}
