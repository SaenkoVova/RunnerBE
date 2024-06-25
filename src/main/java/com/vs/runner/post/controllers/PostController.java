package com.vs.runner.post.controllers;

import com.vs.runner.post.models.*;
import com.vs.runner.post.services.PostService;
import com.vs.runner.util.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {
    private final PostService postService;

    private final FileService fileService;

    @PostMapping("/get_or_create_draft")
    public ResponseEntity<DraftPostResponse> getOrCreateDraftPost(@RequestParam String travelId, Authentication authentication) {
        return ResponseEntity.ok(postService.getOrCreateDraftPost(travelId, authentication.getName()));
    }

    @PutMapping("")
    public ResponseEntity<UpdateDraftResponse> updateDraft(@RequestBody UpdateDraftRequest request, Authentication authentication) {
        return ResponseEntity.ok(postService.updateDraft(request, authentication.getName()));
    }

    @PutMapping("/add_attachment")
    public ResponseEntity<AttachmentResponse> addAttachments(@RequestParam("file") MultipartFile file, Authentication authentication) {
        return ResponseEntity.ok(postService.addAttachment(file, authentication.getName()));
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws IOException {
        Resource file = fileService.load(filename);

        return ResponseEntity
                .ok()
                .header("Content-Type", Files.probeContentType(file.getFile().toPath()))
                .body(file);
    }

    @GetMapping("/post_image_attachments")
    public ResponseEntity<List<AttachmentResponse>> getPostImageAttachments(Authentication authentication) {
        return ResponseEntity.ok(postService.getPostImageAttachments(authentication.getName()));
    }

    @PutMapping("/publish")
    public ResponseEntity<String> publishPost(Authentication authentication) {
        postService.publishPost(authentication.getName());
        return ResponseEntity.ok("Post published");
    }
}
