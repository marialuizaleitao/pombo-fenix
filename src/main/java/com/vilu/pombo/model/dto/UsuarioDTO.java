package com.vilu.pombo.model.dto;

import com.vilu.pombo.model.enums.Perfil;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UsuarioDTO {

    private String id;
    private String nome;
    private String email;
    private String cpf;
    private String fotoDePerfil;
    private Perfil perfil;
    private List<String> pruusIds;
    private List<String> denunciaIds;
    private LocalDateTime criadoEm;
}