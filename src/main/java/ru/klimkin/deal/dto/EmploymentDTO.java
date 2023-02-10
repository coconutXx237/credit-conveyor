package ru.klimkin.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.klimkin.deal.enums.EmploymentStatus;
import ru.klimkin.deal.enums.Position;

import java.math.BigDecimal;

@Schema(description = "Applicant`s employment info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmploymentDTO {

    @Schema(description = "Applicant`s employment status")
    private EmploymentStatus employmentStatus;

    @Schema(description = "Applicant`s INN number")
    private String employerINN;

    @Schema(description = "Applicant`s salary")
    private BigDecimal salary;

    @Schema(description = "Applicant`s employment position")
    private Position position;

    @Schema(description = "Applicant`s total work experience")
    private Integer workExperienceTotal;

    @Schema(description = "Applicant`s current work experience")
    private Integer workExperienceCurrent;
}