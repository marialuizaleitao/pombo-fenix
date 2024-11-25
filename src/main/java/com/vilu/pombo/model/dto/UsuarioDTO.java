package com.vilu.pombo.model.dto;

import com.vilu.pombo.model.enums.Perfil;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UsuarioDTO {
    private String id;
    private String nome;
    private String email;
    private String cpf;
    private String senha;
    private String fotoDePerfil;
    private Perfil perfil;
    private LocalDateTime criadoEm;
}