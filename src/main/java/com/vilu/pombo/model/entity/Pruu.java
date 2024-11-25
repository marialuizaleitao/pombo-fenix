package com.vilu.pombo.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vilu.pombo.model.dto.PruuDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Pruu {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator
    private String id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @NotBlank
    @Size(max = 300, message = "O pruu deve ter entre 1 e 300 caracteres.")
    private String texto;

    @ManyToMany
    @JoinTable(name = "pruu_curtida", joinColumns = @JoinColumn(name = "pruu_id"), inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    private List<Usuario> usuariosQueCurtiram;

    @OneToMany(mappedBy = "pruu")
    @JsonBackReference(value = "pruu-denuncias")
    private List<Denuncia> denuncias;

    @URL(message = "A URL da foto inserida deve ser válida.")
    private String imagem;

    private int quantidadeCurtidas = 0;
    private int quantidadeDenuncias = 0;

    @CreationTimestamp
    private LocalDateTime criadoEm;

    private boolean bloqueado = false;

}