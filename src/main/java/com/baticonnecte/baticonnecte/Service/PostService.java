package com.baticonnecte.baticonnecte.Service;

import com.baticonnecte.baticonnecte.dto.*;
import com.baticonnecte.baticonnecte.entity.PostEntity;
import com.baticonnecte.baticonnecte.entity.UserEntity;
import com.baticonnecte.baticonnecte.repository.PostRepository;
import com.baticonnecte.baticonnecte.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    public PostService(PostRepository postRepository, UserRepository userRepository, CloudinaryService cloudinaryService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.cloudinaryService = cloudinaryService;
    }

    public PostDto createPost(CreatePostDto dto, MultipartFile file) {
        UserEntity user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        String imageUrl = cloudinaryService.uploadImage(file, dto.userId());
        PostEntity post = PostEntity.builder()
                .titre(dto.titre())
                .description(dto.description())
                .imageUrl(imageUrl)
                .user(user)
                .build();

        return mapToDto(postRepository.save(post));
    }

    public PostResponseDto getPostById(UUID id) {
       PostEntity post = postRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profil introuvable pour cet utilisateur"));
       UserEntity user = post.getUser();

       return  new PostResponseDto(
               post.getId(),
               post.getTitre(),
               post.getDescription(),
               post.getImageUrl(),

               user.getId(),
               user.getNomComplet(),

               post.getCreatedAt(),
               post.getUpdatedAt()
       );
    }

    public PageResponseDto<PostResponseDto> getAllPosts(String filter, Pageable pageable) {

        Page<PostEntity> posts;

        if (filter == null || filter.isBlank()) {
            posts = postRepository.findAll(pageable);
        } else {
            posts = postRepository.findByTitreContainingIgnoreCase(filter, pageable);
        }

        List<PostResponseDto> data = posts.map(post -> {

            UserEntity user = post.getUser();

            return new PostResponseDto(
                    post.getId(),
                    post.getTitre(),
                    post.getDescription(),
                    post.getImageUrl(),
                    user.getId(),
                    user.getNomComplet(),
                    post.getCreatedAt(),
                    post.getUpdatedAt()
            );

        }).getContent();


        return PageResponseDto.<PostResponseDto>builder()
                .data(data)
                .page(pageable.getPageNumber() + 1)
                .limit(pageable.getPageSize())
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .build();
    }

    public PostResponseDto update(UUID id, UUID userId, UpdatePostDto dto, MultipartFile file){
        PostEntity post = postRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post introuvable !"));
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable !"));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vous n'avez pas le droit de modifier ce pos");
        }

        if (dto.titre() != null && !dto.titre().isBlank()){
            post.setTitre(dto.titre());
        }

        if (dto.description() != null && !dto.description().isBlank()) {
            post.setDescription(dto.description());
        }

        if (file != null && !file.isEmpty()) {

            String imageUrl = cloudinaryService.uploadImage(
                    file,
                    user.getId()
            );

            post.setImageUrl(imageUrl);
        }

        PostEntity postUpdateData = postRepository.save(post);

        return new PostResponseDto(
                postUpdateData.getId(),
                postUpdateData.getTitre(),
                postUpdateData.getDescription(),
                postUpdateData.getImageUrl(),

                user.getId(),
                user.getNomComplet(),

                postUpdateData.getCreatedAt(),
                postUpdateData.getUpdatedAt()
        );
    }

     public String deletePost(UUID id) {
       if (postRepository.existsById(id)){
           new ResponseStatusException(HttpStatus.NOT_FOUND, "Post introuvable !");
       }

       postRepository.deleteById(id);

       return "Post supprimé avec succès";
    }

    private PostDto mapToDto(PostEntity p) {
        return new PostDto(
                p.getId(),
                p.getTitre(),
                p.getDescription(),
                p.getImageUrl(),
                p.getUser() != null ? p.getUser().getId() : null
        );
    }
}