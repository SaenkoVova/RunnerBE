package com.vs.runner.auth.entities;

import com.vs.runner.user.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "confirm_registration_tokens")
public class ConfirmRegistrationToken {

    @Id
    private String id;

    @OneToOne
    @MapsId
    private User user;

    private String token;
}
