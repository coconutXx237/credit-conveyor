package dev.klimkin.creditconveyor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "DTO to be enriched with results of calculations")
@Getter
@Setter
@Builder
public class CreditDTO {

    @Schema(description = "Requested amount of credit")
    private BigDecimal amount;

    @Schema(description = "Requested term of credit")
    private Integer term;

    @Schema(description = "Calculated monthly payment")
    private BigDecimal monthlyPayment;

    @Schema(description = "Calculated final rate")
    private BigDecimal rate;

    @Schema(description = "Calculated psk")
    private BigDecimal psk;

    @Schema(description = "If insurance is required")
    private Boolean isInsuranceEnabled;

    @Schema(description = "If salary client program is required")
    private Boolean isSalaryClient;

    @Schema(description = "Calculated payment schedule")
    private List<PaymentScheduleElement> paymentSchedule;
}
