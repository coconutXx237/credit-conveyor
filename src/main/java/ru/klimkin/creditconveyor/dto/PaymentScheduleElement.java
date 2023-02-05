package ru.klimkin.creditconveyor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Contains credit payment schedule")
@Data
@Builder
public class PaymentScheduleElement {

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
