package com.vilu.pombo.model.entity;

import com.vilu.pombo.model.dto.DenunciaDTO;
import com.vilu.pombo.model.enums.Motivo;
import com.vilu.pombo.model.enums.StatusDenuncia;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Data
public class Denuncia {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator
    private String id;

    @ManyToOne
    @JoinColumn(name = "pruu_id")
    private Pruu pruu;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Nos ajude a entender o motivo da sua denúncia.")
    private Motivo motivo;

    @Enumerated(EnumType.STRING)
    @NotNull
    private StatusDenuncia status = StatusDenuncia.PENDENTE;

    @CreationTimestamp
    private LocalDateTime criadoEm;

    public static DenunciaDTO toDTO(Denuncia denuncia) {
        return new DenunciaDTO(
                denuncia.getId(),
                denuncia.getUsuario().getNome(),
                denuncia.getPruu().getId(),
                denuncia.getPruu().getTexto(),
                denuncia.getPruu().getImagem(),
                denuncia.getUsuario().getId(),
                denuncia.getUsuario().getNome(),
                denuncia.getMotivo(),
                denuncia.getStatus(),
                denuncia.getCriadoEm()
        );
    }

}