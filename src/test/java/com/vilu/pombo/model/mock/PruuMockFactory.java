package com.vilu.pombo.model.mock;

import com.vilu.pombo.model.entity.Pruu;
import com.vilu.pombo.model.entity.Usuario;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Collections;

public class PruuMockFactory {

    public static Pruu criarPruuMock() {
        Pruu pruuMock = Mockito.mock(Pruu.class);

        Mockito.when(pruuMock.getId()).thenReturn("pruu-uuid-12345");
        Mockito.when(pruuMock.getTexto()).thenReturn("Eu sou bicampeão mundial de Fórmula 1.");
        Mockito.when(pruuMock.getQuantidadeCurtidas()).thenReturn(10);
        Mockito.when(pruuMock.getQuantidadeDenuncias()).thenReturn(2);
        Mockito.when(pruuMock.isBloqueado()).thenReturn(false);
        Mockito.when(pruuMock.getCriadoEm()).thenReturn(LocalDateTime.now());

        Usuario usuarioMock = UsuarioMockFactory.criarUsuarioMock();
        Mockito.when(pruuMock.getUsuario()).thenReturn(usuarioMock);

        Mockito.when(pruuMock.getUsuariosQueCurtiram()).thenReturn(Collections.emptyList());

        Mockito.when(pruuMock.getDenuncias()).thenReturn(Collections.emptyList());

        return pruuMock;
    }

}
