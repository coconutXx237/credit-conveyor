package ru.klimkin.deal.entity;

import lombok.Builder;
import lombok.Data;
import ru.klimkin.deal.enums.EmploymentStatus;
import ru.klimkin.deal.enums.Position;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
public class Employment implements Serializable {

    private Long employmentId;

    private EmploymentStatus employmentStatus;

    private BigDecimal salary;

    private Position position;

    private Integer workExperienceTotal;

    private Integer workExperienceCurrent;
}
