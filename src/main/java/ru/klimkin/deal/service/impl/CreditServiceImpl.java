package ru.klimkin.deal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.klimkin.deal.dto.CreditDTO;
import ru.klimkin.deal.entity.Credit;
import ru.klimkin.deal.mapper.CreditMapper;
import ru.klimkin.deal.repository.CreditRepository;
import ru.klimkin.deal.service.CreditService;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditServiceImpl implements CreditService {

    private final CreditRepository creditRepository;
    private final CreditMapper creditMapper;

    @Override
    public Credit toCredit(CreditDTO creditDTO) {
        return creditMapper.toCredit(creditDTO);
    }

    @Override
    public void createCredit(Credit credit) {
        creditRepository.save(credit);
    }
}
