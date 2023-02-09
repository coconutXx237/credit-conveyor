package ru.klimkin.deal.mapper;

import org.mapstruct.Mapper;
import ru.klimkin.deal.dto.LoanApplicationRequestDTO;
import ru.klimkin.deal.entity.Client;


@Mapper(componentModel = "spring")
public interface ClientMapper {

    Client toClient(LoanApplicationRequestDTO loanApplicationRequestDTO);
}

/*
    private String firstName;

    private String lastName;

    private String middleName;


    private LocalDate birthdate;

    private String passportSeries;

    private String passportNumber;*/
