package ru.klimkin.creditconveyor.dto;

import lombok.Builder;
import lombok.Data;
import ru.klimkin.creditconveyor.util.AgeValidation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Loan application request")
@Data
@Builder
public class LoanApplicationRequestDTO {

    @Schema(description = "Requested amount of credit")
    @Min(value = 10000, message = "Credit amount must be not less than 10000")
    @NotNull(message = "Credit amount must be stated")
    private BigDecimal amount;

    @Schema(description = "Requested term of credit")
    @Min(value = 6, message = "Term must be not less than 6")
    @NotNull(message = "Term must be stated")
    private Integer term;

    @Schema(description = "Applicant`s first name")
    @Size(min = 2, max = 30, message = "First name must be between 2 and 30 characters")
    @NotEmpty(message = "First name must be filled")
    private String firstName;

    @Schema(description = "Applicant`s last name")
    @Size(min = 2, max = 30, message = "Last Name must be between 2 and 30 characters")
    @NotEmpty(message = "Last name must be filled")
    private String lastName;

    @Schema(description = "Applicant`s middle name, if there is so")
    @Size(min = 2, max = 30, message = "Middle name must be between 2 and 30 characters")
    private String middleName;

    @Schema(description = "Applicant`s contact email")
    @Email(regexp = "[\\w\\.]{2,50}@[\\w\\.]{2,20}")
    @NotEmpty(message = "Email must be stated")
    private String email;

    @Schema(description = "Applicant`s birth date")
    @AgeValidation()
    @NotNull(message = "Birth date must be stated")
    private LocalDate birthdate;

    @Schema(description = "Applicant`s passport series")
    @NotEmpty(message = "Passport series must be stated")
    @Size(min = 4, max = 4, message = "Passport series must be 4 digits")
    private String passportSeries;

    @Schema(description = "Applicant`s passport number")
    @NotEmpty(message = "Passport number must be stated")
    @Size(min = 6, max = 6, message = "Passport number must be 6 digits")
    private String passportNumber;
}
