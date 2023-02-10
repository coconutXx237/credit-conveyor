package ru.klimkin.deal.mapper;

import org.mapstruct.Mapper;
import ru.klimkin.deal.dto.CreditDTO;
import ru.klimkin.deal.entity.Credit;

@Mapper(componentModel = "spring")
public interface CreditMapper {

    Credit toCredit(CreditDTO creditDTO);
}