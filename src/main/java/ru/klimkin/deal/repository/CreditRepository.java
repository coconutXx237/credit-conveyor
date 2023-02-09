package ru.klimkin.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.klimkin.deal.entity.Credit;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Integer> {
}
