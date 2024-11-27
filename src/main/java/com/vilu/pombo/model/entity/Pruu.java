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
import java.util.stream.Collectors;

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

    @Column(columnDefinition = "TEXT")
    private String imagem;

    private int quantidadeCurtidas = 0;
    private int quantidadeDenuncias = 0;

    @CreationTimestamp
    private LocalDateTime criadoEm;

    private boolean bloqueado = false;

    public PruuDTO toDTO() {
        PruuDTO dto = new PruuDTO();
        dto.setId(this.id);
        dto.setUsuarioId(this.usuario != null ? this.usuario.getId() : null);
        dto.setTexto(this.texto);
        dto.setUsuariosQueCurtiramIds(this.usuariosQueCurtiram != null
                ? this.usuariosQueCurtiram.stream().map(Usuario::getId).collect(Collectors.toList())
                : null);
        dto.setImagem(this.imagem);
        dto.setQuantidadeCurtidas(this.quantidadeCurtidas);
        dto.setQuantidadeDenuncias(this.quantidadeDenuncias);
        dto.setCriadoEm(this.criadoEm);
        dto.setBloqueado(this.bloqueado);
        return dto;
    }

    public static Pruu fromDTO(PruuDTO dto, Usuario usuario, List<Usuario> usuariosQueCurtiram) {
        Pruu pruu = new Pruu();
        pruu.setId(dto.getId());
        pruu.setUsuario(usuario);
        pruu.setTexto(dto.getTexto());
        pruu.setUsuariosQueCurtiram(usuariosQueCurtiram);
        pruu.setImagem(dto.getImagem());
        pruu.setQuantidadeCurtidas(dto.getQuantidadeCurtidas());
        pruu.setQuantidadeDenuncias(dto.getQuantidadeDenuncias());
        pruu.setCriadoEm(dto.getCriadoEm());
        pruu.setBloqueado(dto.isBloqueado());
        return pruu;
    }

}