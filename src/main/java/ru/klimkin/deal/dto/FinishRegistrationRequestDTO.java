package ru.klimkin.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.klimkin.deal.enums.Gender;
import ru.klimkin.deal.enums.MaritalStatus;

import java.time.LocalDate;

@Schema(description = "Client`s personal data to finalize the registration process")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinishRegistrationRequestDTO {

    @Schema(description = "Applicant`s gender")
    private Gender gender;

    @Schema(description = "Applicant`s marital status")
    private MaritalStatus maritalStatus;

    @Schema(description = "Applicant`s dependent amount")
    private Integer dependentAmount;

    @Schema(description = "Applicant`s passport issue date")
    private LocalDate passportIssueDate;

    @Schema(description = "Applicant`s passport issue branch")
    private String passportIssueBranch;

    @Schema(description = "Applicant`s employment info")
    private EmploymentDTO employment;

    @Schema(description = "Applicant`s bank account number")
    private String account;
}