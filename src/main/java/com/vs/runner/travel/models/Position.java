package com.vs.runner.travel.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Position {
    private double latitude;

    private double longitude;

    private double altitude;

    private double speed;

    private long timestamp;
}
