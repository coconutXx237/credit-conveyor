package ru.klimkin.deal.entity;

import javax.persistence.*;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.klimkin.deal.dto.PaymentScheduleElementDTO;
import ru.klimkin.deal.enums.CreditStatus;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "credit")
@NoArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credit_id")
    private Long creditId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "term")
    private Integer term;

    @Column(name = "monthly_payment")
    private BigDecimal monthlyPayment;

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "psk")
    private BigDecimal psk;

    @Type(type = "jsonb")
    @Column(name = "payment_schedule", columnDefinition = "jsonb")
    private List<PaymentScheduleElementDTO> paymentSchedule;

    @Column(name = "insurance_enable")
    private boolean isInsuranceEnabled;

    @Column(name = "salary_client")
    private boolean isSalaryClientEnabled;

    @Column(name = "credit_status")
    @Enumerated(EnumType.STRING)
    private CreditStatus creditStatus;
}
