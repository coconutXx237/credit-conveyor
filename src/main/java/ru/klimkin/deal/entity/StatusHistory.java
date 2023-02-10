package ru.klimkin.deal.entity;

import lombok.Builder;
import lombok.Data;
import ru.klimkin.deal.enums.ApplicationStatus;
import ru.klimkin.deal.enums.ChangeType;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class StatusHistory implements Serializable {

    private ApplicationStatus status;

    private LocalDateTime time;

    private ChangeType changeType;
}
