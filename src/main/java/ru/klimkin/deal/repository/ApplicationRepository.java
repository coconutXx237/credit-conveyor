package ru.klimkin.deal.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.klimkin.deal.entity.Application;


@Repository
public interface ApplicationRepository extends CrudRepository<Application, Long> {

    // List<Application> getApplicationsByClientId(Long clientId);
}
