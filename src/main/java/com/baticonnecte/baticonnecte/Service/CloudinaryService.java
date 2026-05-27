package com.baticonnecte.baticonnecte.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary){
        this.cloudinary = cloudinary;
    }

    public String uploadImage(MultipartFile file, UUID userId) {

        try {

            if (file == null || file.isEmpty()) {
                throw new RuntimeException("Le fichier est requis");
            }

            String contentType = file.getContentType();

            if (contentType == null || !contentType.startsWith("image/")) {
               throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Seules les images sont autorisées");
            }

            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "baticonnecte/users",
                            "public_id", userId.toString(),
                            "overwrite", true,
                            "resource_type", "image"
                    )
            );

            return uploadResult.get("secure_url").toString();

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload de l'image");
        }
    }
}