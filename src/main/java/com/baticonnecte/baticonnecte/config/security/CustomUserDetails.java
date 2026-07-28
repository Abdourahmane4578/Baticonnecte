package com.baticonnecte.baticonnecte.config.security;

import com.baticonnecte.baticonnecte.entity.UserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

@Getter
public class CustomUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final String email;
    private final String password;
    private final String nomComplet;
    private final Collection<? extends GrantedAuthority> authorities;


    public CustomUserDetails(
            UserEntity user,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.nomComplet = user.getNomComplet();
        this.authorities = authorities;
    }


    @Override
    public String getUsername() {
        return email;
    }


    @Override
    public String getPassword() {
        return password;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    @Override
    public boolean isAccountNonLocked() {
        return true;
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    public boolean isEnabled() {
        return true;
    }
}