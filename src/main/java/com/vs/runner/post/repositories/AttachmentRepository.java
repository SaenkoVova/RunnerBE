package com.vs.runner.post.repositories;

import com.vs.runner.post.entities.Attachment;
import com.vs.runner.post.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, String> {
    List<Attachment> findAllByPost(Post post);
}
