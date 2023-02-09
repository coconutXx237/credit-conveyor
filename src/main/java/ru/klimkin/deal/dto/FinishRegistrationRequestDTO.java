package ru.klimkin.deal.dto;

import lombok.Builder;
import lombok.Data;
import ru.klimkin.deal.enums.Gender;
import ru.klimkin.deal.enums.MaritalStatus;

import java.time.LocalDate;

@Data
@Builder
public class FinishRegistrationRequestDTO {

    private Gender gender;

    private MaritalStatus maritalStatus;

    private Integer dependentAmount;

    private LocalDate passportIssueDate;

    private String passportIssueBranch;

    private EmploymentDTO employment;

    private String account;
}
