package ru.klimkin.deal.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.klimkin.deal.entity.Application;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends CrudRepository<Application, Long> {

    Optional<Application> findByApplicationId(Long applicationId);
}
