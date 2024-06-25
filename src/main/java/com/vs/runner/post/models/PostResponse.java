package com.vs.runner.post.models;

import com.vs.runner.travel.models.Position;
import com.vs.runner.travel.models.TravelResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private String id;
    private String title;
    private String description;
    private List<AttachmentResponse> attachmentResponses;
    private TravelResponse travel;
}
