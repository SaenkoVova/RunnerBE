package com.vs.runner.user.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_infos")
public class UserInfo {
    @Id
    private String id;

    @OneToOne
    @MapsId
    private User user;

    @Temporal(TemporalType.DATE)
    private Date birth;

    private String familyName;

    private String givenName;

    private String picture;

    private String currentTravelId;
}
