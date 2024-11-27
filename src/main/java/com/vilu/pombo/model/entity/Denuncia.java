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

    public DenunciaDTO toDTO() {
        DenunciaDTO dto = new DenunciaDTO();
        dto.setId(this.id);
        dto.setPruuId(this.pruu != null ? this.pruu.getId() : null);
        dto.setUsuarioId(this.usuario != null ? this.usuario.getId() : null);
        dto.setMotivo(this.motivo);
        dto.setStatus(this.status);
        dto.setCriadoEm(this.criadoEm);
        return dto;
    }

    public static Denuncia fromDTO(DenunciaDTO dto, Pruu pruu, Usuario usuario) {
        Denuncia denuncia = new Denuncia();
        denuncia.setId(dto.getId());
        denuncia.setPruu(pruu);
        denuncia.setUsuario(usuario);
        denuncia.setMotivo(dto.getMotivo());
        denuncia.setStatus(dto.getStatus());
        denuncia.setCriadoEm(dto.getCriadoEm());
        return denuncia;
    }
}