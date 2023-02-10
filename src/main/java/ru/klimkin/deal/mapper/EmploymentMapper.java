package ru.klimkin.deal.mapper;

import org.mapstruct.Mapper;
import ru.klimkin.deal.dto.EmploymentDTO;
import ru.klimkin.deal.entity.Employment;

@Mapper(componentModel = "spring")
public interface EmploymentMapper {

    Employment toEmployment(EmploymentDTO employmentDTO);
}