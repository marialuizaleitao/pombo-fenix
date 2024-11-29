package com.vilu.pombo.model.dto;

import com.vilu.pombo.model.enums.Motivo;
import com.vilu.pombo.model.enums.StatusDenuncia;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DenunciaDTO {

    private String id;
    private String nomeDenunciante;
    private String pruuId;
    private String textoPruu;
    private String imagemPruu;
    private String usuarioId;
    private String nomeUsuario;
    private Motivo motivo;
    private StatusDenuncia status;
    private LocalDateTime criadoEm;
}