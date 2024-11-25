package com.vilu.pombo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PruuDTO {
    private String id;
    private String texto;
    private String imagem;
    private String usuarioId;
    private String usuarioNome;
    private String usuarioFotoDePerfil;
    private int quantidadeCurtidas;
    private int quantidadeDenuncias;
    private LocalDateTime criadoEm;
    private boolean bloqueado;
}
