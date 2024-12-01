package com.vilu.pombo.model.mock;

import com.vilu.pombo.model.entity.Usuario;
import com.vilu.pombo.model.enums.Perfil;
import org.mockito.Mockito;

import java.time.LocalDateTime;

public class UsuarioMockFactory {

    public static Usuario criarUsuarioMock() {
        Usuario usuarioMock = Mockito.mock(Usuario.class);

        Mockito.when(usuarioMock.getId()).thenReturn("12345-uuid");
        Mockito.when(usuarioMock.getNome()).thenReturn("Fernando Alonso");
        Mockito.when(usuarioMock.getEmail()).thenReturn("fernando@alonso.com");
        Mockito.when(usuarioMock.getCpf()).thenReturn("158.534.520-29");
        Mockito.when(usuarioMock.getSenha()).thenReturn("senha");
        Mockito.when(usuarioMock.isAdmin()).thenReturn(false);

        return usuarioMock;
    }

    public static Usuario criarUsuarioAdminMock() {
        Usuario usuarioMock = criarUsuarioMock();
        Mockito.when(usuarioMock.getPerfil()).thenReturn(Perfil.ADMINISTRADOR);
        Mockito.when(usuarioMock.isAdmin()).thenReturn(true);
        return usuarioMock;
    }

}
