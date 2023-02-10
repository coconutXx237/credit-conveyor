package ru.klimkin.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.klimkin.deal.enums.Gender;
import ru.klimkin.deal.enums.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Applicant`s full data for scoring performance")
@Data
@Builder
public class ScoringDataDTO {

    @Schema(description = "Requested amount of credit")
    private BigDecimal amount;

    @Schema(description = "Requested term of credit (in months")
    private Integer term;

    @Schema(description = "Applicant`s first name")
    private String firstName;

    @Schema(description = "Applicant`s last name")
    private String lastName;

    @Schema(description = "Applicant`s middle name (if there is so)")
    private String middleName;

    @Schema(description = "Applicant`s gender")
    private Gender gender;

    @Schema(description = "Applicant`s date of birth")
    private LocalDate birthDate;

    @Schema(description = "Applicant`s passport series")
    private String passportSeries;

    @Schema(description = "Applicant`s passport number")
    private String passportNumber;

    @Schema(description = "Applicant`s passport issue date")
    private LocalDate passportIssueDate;

    @Schema(description = "Applicant`s passport issue branch")
    private String passportIssueBranch;

    @Schema(description = "Applicant`s marital status")
    private MaritalStatus maritalStatus;

    @Schema(description = "Applicant`s dependent amount")
    private Integer dependentAmount;

    @Schema(description = "Applicant`s employment data")
    private EmploymentDTO employment;

    @Schema(description = "Applicant`s account number")
    private String account;

    @Schema(description = "If insurance is required")
    private Boolean isInsuranceEnabled;

    @Schema(description = "If salary client program is required")
    private Boolean isSalaryClient;
}