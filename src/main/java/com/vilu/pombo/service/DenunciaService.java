package com.vilu.pombo.service;

import com.vilu.pombo.exception.PomboException;
import com.vilu.pombo.model.dto.DenunciaDTO;
import com.vilu.pombo.model.entity.Denuncia;
import com.vilu.pombo.model.entity.Pruu;
import com.vilu.pombo.model.entity.Usuario;
import com.vilu.pombo.model.enums.Motivo;
import com.vilu.pombo.model.enums.StatusDenuncia;
import com.vilu.pombo.model.repository.DenunciaRepository;
import com.vilu.pombo.model.repository.PruuRepository;
import com.vilu.pombo.model.repository.UsuarioRepository;
import com.vilu.pombo.model.seletor.DenunciaSeletor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Service
public class DenunciaService {

    @Autowired
    private DenunciaRepository denunciaRepository;
    @Autowired
    private PruuRepository pruuRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Denuncia cadastrar(Denuncia denuncia) throws PomboException {
        if (denuncia.getMotivo() == null || !EnumSet.allOf(Motivo.class).contains(denuncia.getMotivo())) {
            throw new PomboException("Motivo de denúncia inválido.", HttpStatus.BAD_REQUEST);
        }

        Pruu pruuDenunciado = pruuRepository.findById(denuncia.getPruu().getId()).orElseThrow(() -> new PomboException("Pruu não encontrado.", HttpStatus.BAD_REQUEST));
        Usuario autorDaDenuncia = usuarioRepository.findById(denuncia.getUsuario().getId()).orElseThrow(() -> new PomboException("Usuário não encontrado.", HttpStatus.BAD_REQUEST));

        denuncia.setPruu(pruuDenunciado);
        denuncia.setUsuario(autorDaDenuncia);

        return denunciaRepository.save(denuncia);
    }

    public void atualizar(String idDenuncia, StatusDenuncia novoStatus) throws PomboException {
        Denuncia denuncia = denunciaRepository.findById(idDenuncia).orElseThrow(() -> new PomboException("Denúncia não encontrada.", HttpStatus.BAD_REQUEST));
        Pruu pruu = denuncia.getPruu();

        atualizarStatusPruu(pruu, denuncia.getStatus(), novoStatus);

        denuncia.setStatus(novoStatus);
        denunciaRepository.save(denuncia);
    }

    public void excluir(String idDenuncia, String idUsuario) throws PomboException {
        Denuncia denuncia = denunciaRepository.findById(idDenuncia).orElseThrow(() -> new PomboException("A denúncia buscada não foi encontrada.", HttpStatus.BAD_REQUEST));

        if (denuncia.getUsuario().getId().equals(idUsuario)) {
            denunciaRepository.deleteById(idDenuncia);
        } else {
            throw new PomboException("Não é possível excluir denúncias que não foram feitas por você.", HttpStatus.BAD_REQUEST);
        }
    }

    public List<DenunciaDTO> pesquisarTodas() {
        List<Denuncia> denuncias =  denunciaRepository.findAll();
        return toDenunciaDTO(denuncias);
    }

    public DenunciaDTO pesquisarPorId(String idDenuncia) throws PomboException {
        Denuncia denuncia = denunciaRepository.findById(idDenuncia).orElseThrow(() -> new PomboException("A denúncia buscada não foi encontrada.", HttpStatus.BAD_REQUEST));
        return Denuncia.toDTO(denuncia);
    }

    public List<DenunciaDTO> pesquisarComFiltros(DenunciaSeletor seletor) {
        List<Denuncia> denuncias;

        if (seletor.temPaginacao()) {
            int pageNumber = seletor.getPagina();
            int pageSize = seletor.getLimite();

            PageRequest page = PageRequest.of(pageNumber - 1, pageSize);
            denuncias = denunciaRepository.findAll(seletor, page).toList();
        }

        denuncias = denunciaRepository.findAll(seletor);

        return toDenunciaDTO(denuncias);
    }

    void atualizarStatusPruu(Pruu pruu, StatusDenuncia statusAtual, StatusDenuncia novoStatus) {
        if (novoStatus == StatusDenuncia.ACEITA) {
            pruu.setBloqueado(true);
        } else if (statusAtual == StatusDenuncia.ACEITA && (novoStatus == StatusDenuncia.REJEITADA || novoStatus == StatusDenuncia.PENDENTE)) {
            pruu.setBloqueado(false);
        }

        pruuRepository.save(pruu);
    }

    public List<DenunciaDTO> toDenunciaDTO(List<Denuncia> denuncias) {
        List<DenunciaDTO> denunciasDTO = new ArrayList<>();

        for (Denuncia d : denuncias) {
            DenunciaDTO dto = Denuncia.toDTO(d);
            denunciasDTO.add(dto);

        }
        return denunciasDTO;
    }

}
