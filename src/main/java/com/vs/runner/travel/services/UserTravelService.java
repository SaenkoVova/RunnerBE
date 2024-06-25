package com.vs.runner.travel.services;

import com.vs.runner.exceptions.ResourceNotFoundException;
import com.vs.runner.travel.entities.Travel;
import com.vs.runner.travel.enums.Status;
import com.vs.runner.travel.models.Position;
import com.vs.runner.travel.models.TravelResponse;
import com.vs.runner.travel.models.UpdateTravelCordsRequest;
import com.vs.runner.travel.repositories.UserTravelRepository;
import com.vs.runner.user.entities.User;
import com.vs.runner.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserTravelService {
    private final UserTravelRepository userTravelRepository;

    private final UserRepository userRepository;

    public Travel findActiveTravel(String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));

        return userTravelRepository
                .findFirstByUserAndStatus(user, Status.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Travel with ACTIVE status for user with id" + user.getId() + " not found"));
    }

    public Travel findCompletedTravelById(String id) {
        return userTravelRepository.findFirstByIdAndStatus(id, Status.COMPLETED).orElseThrow(() -> new ResourceNotFoundException("Travel with id " + id + " and status COMPLETED not found"));
    }

    public TravelResponse createTravel(String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));


        Travel travel = userTravelRepository.save(
                Travel.builder()
                        .user(user)
                        .created(System.currentTimeMillis())
                        .status(Status.ACTIVE)
                        .build()
        );

        return mapToTravelResponse(travel);
    }

    public TravelResponse getActiveTravel(String email) {
        Travel travel = findActiveTravel(email);
        return mapToTravelResponse(travel);
    }

    public TravelResponse finishTravel(String email) {
        Travel travel = findActiveTravel(email);

        travel.setStatus(Status.COMPLETED);
        travel.setCompleted(System.currentTimeMillis());

        Travel finishedTravel = userTravelRepository.save(travel);

        return mapToTravelResponse(finishedTravel);
    }

    public TravelResponse mapToTravelResponse(Travel travel) {
        return TravelResponse
                .builder()
                .id(travel.getId())
                .created(travel.getCreated())
                .completed(travel.getCompleted())
                .positions(travel.getPositions())
                .status(travel.getStatus())
                .build();
    }

    public List<TravelResponse> getAllTravels(String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));

        return userTravelRepository.findAllByUserAndStatus(user, Status.COMPLETED).stream().map(this::mapToTravelResponse).toList();
    }

    public void updateTravelCords(UpdateTravelCordsRequest request, String email) {

        Travel travel = findActiveTravel(email);

        if (travel.getPositions() != null) {
            travel.getPositions().add(request.getPosition());
        } else {
            List<Position> positions = new ArrayList<>();
            positions.add(request.getPosition());
            travel.setPositions(positions);
        }

        userTravelRepository.save(travel);
    }
}
