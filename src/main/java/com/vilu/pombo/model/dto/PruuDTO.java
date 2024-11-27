package com.vilu.pombo.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PruuDTO {

    private String id;
    private String texto;
    private String imagem;
    private String usuarioId;
    private List<String> usuariosQueCurtiramIds;
    private int quantidadeCurtidas;
    private int quantidadeDenuncias;
    private LocalDateTime criadoEm;
    private boolean bloqueado;
}
