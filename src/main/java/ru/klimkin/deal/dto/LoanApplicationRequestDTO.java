package ru.klimkin.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Loan application request")
@Data
@Builder
public class LoanApplicationRequestDTO {

    @Schema(description = "Requested amount of credit")
    private BigDecimal amount;

    @Schema(description = "Requested term of credit")
    private Integer term;

    @Schema(description = "Applicant`s first name")
    private String firstName;

    @Schema(description = "Applicant`s last name")
    private String lastName;

    @Schema(description = "Applicant`s middle name, if there is so")
    private String middleName;

    @Schema(description = "Applicant`s contact email")
    private String email;

    @Schema(description = "Applicant`s birth date")
    private LocalDate birthDate;

    @Schema(description = "Applicant`s passport series")
    private String passportSeries;

    @Schema(description = "Applicant`s passport number")
    private String passportNumber;
}