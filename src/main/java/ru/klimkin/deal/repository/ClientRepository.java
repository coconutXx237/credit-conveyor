package ru.klimkin.deal.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.klimkin.deal.entity.Client;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {
}
