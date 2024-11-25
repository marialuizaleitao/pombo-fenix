package com.vilu.pombo.model.dto;

import com.vilu.pombo.model.enums.Motivo;
import com.vilu.pombo.model.enums.StatusDenuncia;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DenunciaDTO {
    private String idDenuncia;
    private String idUsuario;
    private String nomeUsuario;
    private String emailUsuario;
    private String URLfotoDoUsuario;
    private Motivo motivo;
    private StatusDenuncia status;
    private LocalDateTime criadoEm;
}
