package com.vs.runner.travel.entities;

import com.vs.runner.post.entities.Post;
import com.vs.runner.travel.enums.Status;
import com.vs.runner.travel.models.Position;
import com.vs.runner.user.entities.User;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "travels")
public class Travel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @org.hibernate.annotations.Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<Position> positions;

    @OneToOne(mappedBy = "travel")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Temporal(TemporalType.TIMESTAMP)
    private Long created;

    @Temporal(TemporalType.TIMESTAMP)
    private Long completed;
}
