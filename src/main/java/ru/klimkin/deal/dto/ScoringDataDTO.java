package ru.klimkin.deal.dto;

import lombok.Builder;
import lombok.Data;
import ru.klimkin.deal.enums.Gender;
import ru.klimkin.deal.enums.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@Builder
public class ScoringDataDTO {

    private BigDecimal amount;

    private Integer term;

    private String firstName;

    private String lastName;

    private String middleName;

    private Gender gender;

    private LocalDate birthDate;

    private String passportSeries;

    private String passportNumber;

    private LocalDate passportIssueDate;

    private String passportIssueBranch;

    private MaritalStatus maritalStatus;

    private Integer dependentAmount;

    private EmploymentDTO employment;

    private String account;

    private Boolean isInsuranceEnabled;

    private Boolean isSalaryClient;
}