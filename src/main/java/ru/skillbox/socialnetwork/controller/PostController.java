package ru.skillbox.socialnetwork.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.socialnetwork.data.dto.AddPostRequest;
import ru.skillbox.socialnetwork.data.dto.PostResponse;
import ru.skillbox.socialnetwork.service.PostService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final Logger logger = LoggerFactory.getLogger(PostController.class);

        @PostMapping("/api/v1/users/{id}/wall")
        public ResponseEntity<PostResponse> postNewPost(@PathVariable String id,
                                                        @RequestParam(name = "publish_date", required = false) Long publishDate,
                                                        @RequestBody AddPostRequest addPostRequest, Principal principal) {

            return ResponseEntity.ok(postService.addNewPost(addPostRequest, principal, publishDate));
        }

}
