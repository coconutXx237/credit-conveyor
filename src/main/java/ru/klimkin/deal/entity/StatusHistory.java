package ru.klimkin.deal.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.TypeDef;
import ru.klimkin.deal.enums.ApplicationStatus;
import ru.klimkin.deal.enums.ChangeType;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class StatusHistory implements Serializable {

    private ApplicationStatus status;

    private LocalDateTime time;

    private ChangeType changeType;
}
