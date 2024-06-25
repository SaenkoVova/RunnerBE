package com.vs.runner.travel.repositories;

import com.vs.runner.travel.entities.Travel;
import com.vs.runner.travel.enums.Status;
import com.vs.runner.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserTravelRepository extends JpaRepository<Travel, String> {
    Optional<Travel> findFirstByIdAndStatus(String id, Status status);

    Optional<Travel> findFirstByUserAndStatus(User user, Status status);

    List<Travel> findAllByUserAndStatus(User user, Status status);

}
