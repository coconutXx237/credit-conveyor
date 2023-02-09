package ru.klimkin.deal.dto;

import lombok.Builder;
import lombok.Data;
import ru.klimkin.deal.enums.EmploymentStatus;
import ru.klimkin.deal.enums.Position;

import java.math.BigDecimal;

@Data
@Builder
public class EmploymentDTO {

    private EmploymentStatus employmentStatus;

    private String employerINN;

    private BigDecimal salary;

    private Position position;

    private Integer workExperienceTotal;

    private Integer workExperienceCurrent;
}

