package com.vs.runner.travel.controllers;

import com.vs.runner.travel.models.TravelResponse;
import com.vs.runner.travel.models.UpdateTravelCordsRequest;
import com.vs.runner.travel.services.UserTravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/api/v1/travel")
public class UserTravelController {
    private final UserTravelService userTravelService;

    @PostMapping("/create")
    public ResponseEntity<TravelResponse> createTravel(Authentication authentication) {
        return ResponseEntity.ok(userTravelService.createTravel(authentication.getName()));
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateTravelCords(@RequestBody UpdateTravelCordsRequest request, Authentication authentication) {
        userTravelService.updateTravelCords(request, authentication.getName());
        return ResponseEntity.ok("Updated");
    }

    @DeleteMapping("/finish")
    public ResponseEntity<TravelResponse> finishTravel(Authentication authentication) {
        return ResponseEntity.ok(userTravelService.finishTravel(authentication.getName()));
    }

    @GetMapping("/active")
    public ResponseEntity<TravelResponse> getActiveTravel(Authentication authentication) {
        return ResponseEntity.ok(userTravelService.getActiveTravel(authentication.getName()));
    }

    @GetMapping("/all")
    public ResponseEntity<List<TravelResponse>> getAllTravels(Authentication authentication) {
        return ResponseEntity.ok(userTravelService.getAllTravels(authentication.getName()));
    }
}
