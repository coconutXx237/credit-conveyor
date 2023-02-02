package dev.klimkin.creditconveyor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Applicant`s full data for scoring performance")
@Getter
@Setter
public class ScoringDataDTO {

    @Schema(description = "Requested amount of credit")
    @Min(value = 10000, message = "Credit amount must be not less than 10000")
    @NotNull(message = "Credit amount must be stated")
    private BigDecimal amount;

    @Schema(description = "Requested term of credit (in months")
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

    @Schema(description = "Applicant`s middle name (if there is so)")
    @Size(min = 2, max = 30, message = "Middle name must be between 2 and 30 characters")
    private String middleName;

    @Schema(description = "Applicant`s gender")
    @JsonProperty("gender")
    @NotNull(message = "Gender must be stated")
    private Gender gender;

    @Schema(description = "Applicant`s date of birth")
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

    @Schema(description = "Applicant`s passport issue date")
    @NotNull(message = "Passport issue date date must be stated")
    private LocalDate passportIssueDate;

    @Schema(description = "Applicant`s passport issue branch")
    @NotEmpty(message = "Passport issue branch must be stated")
    private String passportIssueBranch;

    @Schema(description = "Applicant`s marital status")
    @JsonProperty("maritalStatus")
    @NotNull(message = "Marital status must be stated")
    private MaritalStatus maritalStatus;

    @Schema(description = "Applicant`s dependent amount")
    @NotNull(message = "Dependent amount must be stated")
    private Integer dependentAmount;

    @Schema(description = "Applicant`s employment data")
    @NotNull(message = "Employment data must be stated")
    private EmploymentDTO employment;

    @Schema(description = "Applicant`s account number")
    @NotEmpty(message = "Account number status must be stated")
    private String account;

    @Schema(description = "If insurance is required")
    @NotNull(message = "Insurance status must be stated")
    private Boolean isInsuranceEnabled;

    @Schema(description = "If salary client program is required")
    @NotNull(message = "Salary client status must be stated")
    private Boolean isSalaryClient;
}
