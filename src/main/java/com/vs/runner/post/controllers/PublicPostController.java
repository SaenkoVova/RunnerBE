package com.vs.runner.post.controllers;

import com.vs.runner.post.models.PostResponse;
import com.vs.runner.post.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/api/v1/public/post")
public class PublicPostController {

    private final PostService postService;

    @GetMapping("/all")
    public ResponseEntity<List<PostResponse>> getAllPosts(@PageableDefault()Pageable pageable) {
        return ResponseEntity.ok(postService.getAllPosts(pageable));
    }
}
