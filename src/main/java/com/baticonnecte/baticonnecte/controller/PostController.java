package com.baticonnecte.baticonnecte.controller;

import com.baticonnecte.baticonnecte.dto.CreatePostDto;
import com.baticonnecte.baticonnecte.dto.PostDto;
import com.baticonnecte.baticonnecte.Service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin("*") // pour Angular plus tard
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody CreatePostDto dto) {
        PostDto created = postService.createPost(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable UUID id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable UUID id,
                                              @RequestBody CreatePostDto dto) {
        return ResponseEntity.ok(postService.updatePost(id, dto));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}