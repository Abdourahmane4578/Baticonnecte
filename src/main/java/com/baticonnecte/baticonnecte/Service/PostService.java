package com.baticonnecte.baticonnecte.Service;

import com.baticonnecte.baticonnecte.dto.CreatePostDto;
import com.baticonnecte.baticonnecte.dto.PostDto;
import com.baticonnecte.baticonnecte.entity.PostEntity;
import com.baticonnecte.baticonnecte.entity.UserEntity;
import com.baticonnecte.baticonnecte.repository.PostRepository;
import com.baticonnecte.baticonnecte.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // CREATE
    public PostDto createPost(CreatePostDto dto) {
        UserEntity user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        PostEntity post = PostEntity.builder()
                .titre(dto.titre())
                .description(dto.description())
                .image(dto.image())
                .user(user)
                .build();

        return mapToDto(postRepository.save(post));
    }

    // READ ALL
    public List<PostDto> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // READ ONE
    public PostDto getPostById(UUID id) {
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post non trouvé"));
        return mapToDto(post);
    }

    // UPDATE
    public PostDto updatePost(UUID id, CreatePostDto dto) {
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post non trouvé"));

        post.setTitre(dto.titre());
        post.setDescription(dto.description());
        post.setImage(dto.image());

        return mapToDto(postRepository.save(post));
    }

    // DELETE
    public void deletePost(UUID id) {
        postRepository.deleteById(id);
    }

    //  MAPPING CENTRALISÉ
    private PostDto mapToDto(PostEntity p) {
        return new PostDto(
                p.getId(),
                p.getTitre(),
                p.getDescription(),
                p.getImage(),
                p.getUser() != null ? p.getUser().getId() : null
        );
    }
}