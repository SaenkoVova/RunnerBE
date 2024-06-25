package com.vs.runner.post.models;

import com.vs.runner.post.entities.Attachment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DraftPostResponse {
    private String id;
    private String travelId;
    private String title;
    private String description;
    private List<AttachmentResponse> attachments;
}
