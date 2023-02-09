package ru.klimkin.deal.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.TypeDef;
import ru.klimkin.deal.enums.EmploymentStatus;
import ru.klimkin.deal.enums.Position;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Employment implements Serializable {

    private Client client;

    private EmploymentStatus employmentStatus;

    private BigDecimal salary;

    private Position position;

    private Integer workExperienceTotal;

    private Integer workExperienceCurrent;
}
