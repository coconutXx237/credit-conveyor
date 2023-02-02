package dev.klimkin.creditconveyor.service;

import dev.klimkin.creditconveyor.dto.LoanApplicationRequestDTO;
import dev.klimkin.creditconveyor.dto.LoanOfferDTO;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
@ExtendWith(MockitoExtension.class)
class OfferServiceImplTest {

    @Mock
    private CalculationService calculationService;

    @InjectMocks
    private OfferServiceImpl offerService;

    @Test
    void getLoanOffer() {

    }
}