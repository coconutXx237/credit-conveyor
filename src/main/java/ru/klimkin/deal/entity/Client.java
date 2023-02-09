package ru.klimkin.deal.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import ru.klimkin.deal.enums.Gender;
import ru.klimkin.deal.enums.MaritalStatus;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "client")
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    @Column(name = "marital_status")
    @Enumerated(EnumType.ORDINAL)
    private MaritalStatus maritalStatus;

    @Column(name = "dependent_amount")
    private Integer dependentAmount;

    @Type(type = "jsonb")
    @Column(name = "passport_id", columnDefinition = "jsonb")
    private Passport passportId;

    @Type(type = "jsonb")
    @Column(name = "employment_id", columnDefinition = "jsonb")
    private Employment employmentId;

    @Column(name = "account")
    private String account;
}
