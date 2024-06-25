package com.vs.runner.post.entities;

import com.vs.runner.post.enums.Status;
import com.vs.runner.travel.entities.Travel;
import com.vs.runner.user.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @MapsId
    private Travel travel;

    private String title;

    private String description;

    @OneToMany(mappedBy = "post")
    private List<Attachment> attachments;

    @Temporal(TemporalType.TIMESTAMP)
    private Long posted;

    @Enumerated(EnumType.STRING)
    private Status status;
}
