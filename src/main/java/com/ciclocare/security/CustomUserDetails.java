package com.ciclocare.security;

import com.ciclocare.entity.Usuario;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private String email;
    private String senha;
    private Boolean ativo;
    private List<GrantedAuthority> authorities;

    @Builder
    public CustomUserDetails(Usuario usuario) {
        this.email = usuario.getEmail();
        this.senha = usuario.getSenha();
        this.ativo = usuario.getAtivo();
        this.authorities = List.of();
    }

    public static CustomUserDetails from(Usuario usuario) {
        return CustomUserDetails.builder()
                .usuario(usuario)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return ativo;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return ativo;
    }
}
