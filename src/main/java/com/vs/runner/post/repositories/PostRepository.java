package com.vs.runner.post.repositories;

import com.vs.runner.post.entities.Post;
import com.vs.runner.post.enums.Status;
import com.vs.runner.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, String> {
    Optional<Post> findFirstByUserAndStatus(User user, Status status);
}
