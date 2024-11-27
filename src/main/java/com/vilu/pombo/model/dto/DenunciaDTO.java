package com.vilu.pombo.model.dto;

import com.vilu.pombo.model.enums.Motivo;
import com.vilu.pombo.model.enums.StatusDenuncia;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DenunciaDTO {

    private String id;
    private String pruuId;
    private String usuarioId;
    private Motivo motivo;
    private StatusDenuncia status;
    private LocalDateTime criadoEm;
}