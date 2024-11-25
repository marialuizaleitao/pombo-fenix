package com.vilu.pombo.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vilu.pombo.model.dto.UsuarioDTO;
import com.vilu.pombo.model.enums.Perfil;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator
    private String id;

    @NotBlank(message = "O nome não pode estar em branco.")
    @Size(max = 200, message = "O nome deve ter entre 3 e 200 caracteres.")
    private String nome;

    @NotBlank(message = "O email não pode estar em branco.")
    @Email(message = "O email informado deve ser válido.")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "O CPF não pode estar em branco.")
    @CPF(message = "O CPF informado deve ser válido.")
    @Column(unique = true)
    private String cpf;

    @NotBlank(message = "A senha não deve estar em branco.")
    @Size(max = 500)
    private String senha;

    @URL(message = "A URL da foto de perfil deve ser válida.")
    private String fotoDePerfil;

    @Enumerated(EnumType.STRING)
    private Perfil perfil = Perfil.USUARIO;

    @NotNull(message = "É obrigatório informar se o usuário é administrador.")
    private boolean isAdmin;

    @ToString.Exclude
    @OneToMany(mappedBy = "usuario")
    @JsonBackReference(value = "usuario-pruus")
    private List<Pruu> pruus = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "usuario")
    @JsonBackReference(value = "usuario-denuncias")
    private List<Denuncia> denuncias = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime criadoEm;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(perfil.toString()));
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    public boolean isAdmin() {
        return this.perfil == Perfil.ADMINISTRADOR;
    }

}