package ru.klimkin.deal.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import ru.klimkin.deal.dto.LoanOfferDTO;
import ru.klimkin.deal.enums.ApplicationStatus;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "application")
@NoArgsConstructor
public class Application {
    @Id
    @Column(name = "application_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "credit_id")
    private Long creditId;

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private ApplicationStatus applicationStatus;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Type(type = "jsonb")
    @Column(name = "applied_offer", columnDefinition = "jsonb")
    private LoanOfferDTO appliedOffer;

    @Column(name = "sign_date")
    private LocalDate signDate;

    @Column(name = "ses_code")
    private Integer sesCode;

    @Type(type = "jsonb")
    @Column(name = "status_history", columnDefinition = "jsonb")
    private List<StatusHistory> statusHistory;
}
