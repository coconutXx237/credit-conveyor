package ru.klimkin.deal.entity;

import javax.persistence.*;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.klimkin.deal.enums.Gender;
import ru.klimkin.deal.enums.MaritalStatus;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "client")
@NoArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
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
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "marital_status")
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    @Column(name = "dependent_amount")
    private Integer dependentAmount;

    @Type(type = "jsonb")
    @Column(name = "passport", columnDefinition = "jsonb")
    private Passport passport;

    @Type(type = "jsonb")
    @Column(name = "employment", columnDefinition = "jsonb")
    private Employment employment;

    @Column(name = "account")
    private String account;
}
