package com.vilu.pombo.model.mock;

import com.vilu.pombo.model.entity.Denuncia;
import com.vilu.pombo.model.entity.Pruu;
import com.vilu.pombo.model.entity.Usuario;
import com.vilu.pombo.model.enums.Motivo;
import com.vilu.pombo.model.enums.StatusDenuncia;
import org.mockito.Mockito;

import java.time.LocalDateTime;

public class DenunciaMockFactory {

    public static Denuncia criarDenunciaMock() {
        Denuncia denunciaMock = Mockito.mock(Denuncia.class);

        Mockito.when(denunciaMock.getId()).thenReturn("denuncia-uuid-12345");
        Mockito.when(denunciaMock.getMotivo()).thenReturn(Motivo.CONTEUDO_INAPROPRIADO);
        Mockito.when(denunciaMock.getStatus()).thenReturn(StatusDenuncia.PENDENTE);
        Mockito.when(denunciaMock.getCriadoEm()).thenReturn(LocalDateTime.now());

        Pruu pruuMock = PruuMockFactory.criarPruuMock();
        Mockito.when(denunciaMock.getPruu()).thenReturn(pruuMock);

        Usuario usuarioMock = UsuarioMockFactory.criarUsuarioMock();
        Mockito.when(denunciaMock.getUsuario()).thenReturn(usuarioMock);

        return denunciaMock;
    }

    public static Denuncia criarDenunciaComStatusMock(StatusDenuncia status) {
        Denuncia denunciaMock = criarDenunciaMock();

        Mockito.when(denunciaMock.getStatus()).thenReturn(status);

        return denunciaMock;
    }

    public static Denuncia criarDenunciaComMotivoMock(Motivo motivo) {
        Denuncia denunciaMock = criarDenunciaMock();

        Mockito.when(denunciaMock.getMotivo()).thenReturn(motivo);

        return denunciaMock;
    }
}
