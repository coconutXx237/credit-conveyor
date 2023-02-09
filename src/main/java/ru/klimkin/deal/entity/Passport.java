package ru.klimkin.deal.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Passport implements Serializable {

    private Long passportId;

    private String series;

    private String number;

    private String issueBranch;

    private LocalDate issueDate;
}
