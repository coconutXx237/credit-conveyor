package ru.klimkin.deal.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.klimkin.deal.entity.Credit;

@Repository
public interface CreditRepository extends CrudRepository<Credit, Long> {
}
