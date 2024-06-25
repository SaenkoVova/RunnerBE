package com.vs.runner.travel.models;

import com.vs.runner.travel.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TravelResponse {
    private String id;
    private Long created;
    private Long completed;
    private Status status;
    private List<Position> positions;
}
