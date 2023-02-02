package dev.klimkin.creditconveyor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Schema(description = "Applicant`s employment info")
@Getter
@Setter
public class EmploymentDTO {

    @Schema(description = "Applicant`s employment status")
    @JsonProperty("employmentStatus")
    @NotNull(message = "Employment status must be stated")
    private EmploymentStatus employmentStatus;

    @Schema(description = "Applicant`s INN number")
    @Size(min = 12, max = 12, message = "Employer INN must consist of 12 digits")
    @NotEmpty(message = "Employer INN must be stated")
    private String employerINN;

    @Schema(description = "Applicant`s salary")
    @NotNull(message = "Salary must be stated")
    private BigDecimal salary;

    @Schema(description = "Applicant`s employment position")
    @JsonProperty("position")
    @NotNull(message = "Position must be stated")
    private Position position;

    @Schema(description = "Applicant`s total work experience")
    @NotNull(message = "Total work experience must be stated")
    private Integer workExperienceTotal;

    @Schema(description = "Applicant`s current work experience")
    @NotNull(message = "Current work experience must be stated")
    private Integer workExperienceCurrent;
}

