package com.vs.runner.post.services;

import com.vs.runner.exceptions.ResourceNotFoundException;
import com.vs.runner.post.controllers.PostController;
import com.vs.runner.post.entities.Attachment;
import com.vs.runner.post.entities.Post;
import com.vs.runner.post.enums.Status;
import com.vs.runner.post.models.*;
import com.vs.runner.post.repositories.AttachmentRepository;
import com.vs.runner.post.repositories.PostRepository;
import com.vs.runner.travel.entities.Travel;
import com.vs.runner.travel.models.TravelResponse;
import com.vs.runner.travel.services.UserTravelService;
import com.vs.runner.user.entities.User;
import com.vs.runner.user.repositories.UserRepository;
import com.vs.runner.util.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final UserTravelService userTravelService;

    private final FileService fileService;

    private final AttachmentRepository attachmentRepository;

    public DraftPostResponse getOrCreateDraftPost(String travelId, String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));

        Optional<Post> draftPost = postRepository.findFirstByUserAndStatus(user, Status.DRAFT);

        Travel travel = userTravelService.findCompletedTravelById(travelId);

        if (draftPost.isPresent()) {

            Post post = draftPost.get();

            List<AttachmentResponse> attachments = attachmentRepository
                    .findAllByPost(post)
                    .stream()
                    .map(attachment -> AttachmentResponse
                            .builder()
                            .id(attachment.getId())
                            .uri(attachment.getUri())
                            .build())
                    .toList();

            post.setTravel(travel);
            postRepository.save(post);

            return DraftPostResponse
                    .builder()
                    .id(post.getId())
                    .travelId(travelId)
                    .title(post.getTitle())
                    .description(post.getDescription())
                    .attachments(attachments)
                    .build();

        }

        Post post = Post.builder()
                .user(user)
                .status(Status.DRAFT)
                .travel(travel)
                .build();

        Post savedPost = postRepository.save(post);

        return DraftPostResponse
                .builder()
                .travelId(travelId)
                .id(savedPost.getId())
                .title(savedPost.getTitle())
                .description(savedPost.getDescription())
                .build();
    }

    public UpdateDraftResponse updateDraft(UpdateDraftRequest request, String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));

        Post post = postRepository
                .findFirstByUserAndStatus(user, Status.DRAFT)
                .orElseThrow(() -> new ResourceNotFoundException("Draft not found"));

        post.setTitle(request.getTitle());
        post.setDescription(request.getDescription());

        postRepository.save(post);

        return UpdateDraftResponse
                .builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .build();
    }

    public AttachmentResponse addAttachment(MultipartFile file, String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));

        Post post = postRepository
                .findFirstByUserAndStatus(user, Status.DRAFT)
                .orElseThrow(() -> new ResourceNotFoundException("Draft not found"));

        fileService.save(file);

        String url = MvcUriComponentsBuilder
                .fromMethodName(PostController.class, "getFile", file.getOriginalFilename()).build().toString();

        Attachment attachment = Attachment
                .builder()
                .post(post)
                .uri(url)
                .build();


        Attachment savedAttachment = attachmentRepository.save(attachment);


        return AttachmentResponse
                .builder()
                .id(savedAttachment.getId())
                .uri(savedAttachment.getUri())
                .build();
    }

    public List<AttachmentResponse> getPostImageAttachments(String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));

        Post post = postRepository
                .findFirstByUserAndStatus(user, Status.DRAFT)
                .orElseThrow(() -> new ResourceNotFoundException("Draft not found"));

        List<Attachment> attachments = attachmentRepository.findAllByPost(post);

        return attachments.stream().map(attachment ->
                AttachmentResponse
                        .builder()
                        .id(attachment.getId())
                        .uri(attachment.getUri())
                        .build()
        ).toList();
    }

    public void publishPost(String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));

        Post post = postRepository
                .findFirstByUserAndStatus(user, Status.DRAFT)
                .orElseThrow(() -> new ResourceNotFoundException("Draft not found"));

        post.setStatus(Status.PUBLISHED);
        post.setPosted(System.currentTimeMillis());

        postRepository.save(post);
    }

    public List<PostResponse> getAllPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);

        return posts
                .stream()
                .map(post -> PostResponse
                        .builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .description(post.getDescription())
                        .attachmentResponses(post
                                .getAttachments()
                                .stream()
                                .map(attachment -> AttachmentResponse
                                        .builder()
                                        .id(attachment.getId())
                                        .uri(attachment.getUri())
                                        .build()).toList())
                        .travel(
                                TravelResponse
                                        .builder()
                                        .id(post.getTravel().getId())
                                        .created(post.getTravel().getCreated())
                                        .status(post.getTravel().getStatus())
                                        .positions(post.getTravel().getPositions())
                                        .completed(post.getTravel().getCompleted())
                                        .build()
                        )

                        .build()).toList();
    }
}
