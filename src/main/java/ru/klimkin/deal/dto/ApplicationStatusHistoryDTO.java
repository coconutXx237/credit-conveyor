package ru.klimkin.deal.dto;

import lombok.Builder;
import lombok.Data;
import ru.klimkin.deal.enums.ApplicationStatus;
import ru.klimkin.deal.enums.ChangeType;

import java.time.LocalDate;

@Data
@Builder
public class ApplicationStatusHistoryDTO {

    private ApplicationStatus applicationStatus;

    private LocalDate time;

    private ChangeType changeType;
}
