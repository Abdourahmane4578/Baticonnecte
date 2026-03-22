package com.baticonnecte.baticonnecte.config.seed;

import com.baticonnecte.baticonnecte.entity.UserEntity;
import com.baticonnecte.baticonnecte.enumeration.StatusEnum;
import com.baticonnecte.baticonnecte.enumeration.TypeUserEnum;
import com.baticonnecte.baticonnecte.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    CommandLineRunner initAdmin(){
        return args -> {
            if (!userRepository.existsByEmail("admin@gmail.com")){
                UserEntity userEntity = UserEntity.builder()
                        .nomComplet("Admin")
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("admin123@"))
                        .role(TypeUserEnum.ADMIN)
                        .statut(StatusEnum.ACTIF)
                        .build();

                userRepository.save(userEntity);

                System.out.println("✅ ADMIN créé avec succès");
            }
        };
    }
}
