package com.vilu.pombo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vilu.pombo.auth.AuthService;
import com.vilu.pombo.exception.PomboException;
import com.vilu.pombo.model.entity.Pruu;
import com.vilu.pombo.model.entity.Usuario;
import com.vilu.pombo.model.repository.PruuRepository;
import com.vilu.pombo.model.repository.UsuarioRepository;
import com.vilu.pombo.model.seletor.PruuSeletor;

@Service
public class PruuService {

	@Autowired
	private PruuRepository pruuRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private AuthService authService;
	@Autowired
	private ImagemService imagemService;

	public Pruu cadastrar(Pruu pruu) throws PomboException {
		Optional<Usuario> autor = usuarioRepository.findById(pruu.getUsuario().getId());
		pruu.setUsuario(autor.orElseThrow(() -> new PomboException("Usuário não encontrado.", HttpStatus.BAD_REQUEST)));
		return pruuRepository.save(pruu);
	}

	public void toggleCurtida(String idPruu) throws PomboException {
		Usuario subject = authService.getUsuarioAutenticado();

		Pruu pruu = pruuRepository.findById(idPruu).orElseThrow(
				() -> new PomboException("O pruu selecionado não foi encontrado.", HttpStatus.BAD_REQUEST));
		Usuario usuario = usuarioRepository.findById(subject.getId())
				.orElseThrow(() -> new PomboException("Usuário não encontrado.", HttpStatus.BAD_REQUEST));

		List<Usuario> usuariosQueCurtiram = pruu.getUsuariosQueCurtiram();

		if (usuariosQueCurtiram.contains(usuario)) {
			usuariosQueCurtiram.remove(usuario);
			pruu.setQuantidadeCurtidas(pruu.getQuantidadeCurtidas() - 1);
		} else {
			usuariosQueCurtiram.add(usuario);
			pruu.setQuantidadeCurtidas(pruu.getQuantidadeCurtidas() + 1);
		}

		pruu.setUsuariosQueCurtiram(usuariosQueCurtiram);
		pruuRepository.save(pruu);
	}

	// uma mensagem deve ser excluída apenas logicamente, permitido apenas para
	// usuário ADMIN ou para USUARIO que criou a postagem
	public void excluir(String idPruu, String idUsuario) throws PomboException {
		Pruu pruu = pruuRepository.findById(idPruu).orElseThrow(
				() -> new PomboException("O pruu selecionado não foi encontrado.", HttpStatus.BAD_REQUEST));
		Usuario usuario = usuarioRepository.findById(idUsuario)
				.orElseThrow(() -> new PomboException("Usuário não encontrado.", HttpStatus.NOT_FOUND));

		boolean isAdmin = usuario.isAdmin();
		boolean isOwner = pruu.getUsuario().getId().equals(usuario.getId());

		if (!isAdmin && !isOwner) {
			throw new PomboException("Você não é o Pombo dono deste Pruu, portanto não pode excluí-lo.",
					HttpStatus.FORBIDDEN);
		}

		pruu.setBloqueado(true);
		pruuRepository.save(pruu);
	}

	public List<Pruu> pesquisarTodos() {
		return pruuRepository.findAll();
	}

	public Pruu pesquisarPorId(String id) throws PomboException {
		Pruu pruu = pruuRepository.findById(id)
				.orElseThrow(() -> new PomboException("O pruu buscado não foi encontrado.", HttpStatus.BAD_REQUEST));

		if (pruu.isBloqueado()) {
			throw new PomboException("Este pruu foi capturado e não está mais disponível.", HttpStatus.BAD_REQUEST);
		}

		return pruu;
	}

	public List<Pruu> pesquisarComFiltros(PruuSeletor seletor) throws PomboException {
		List<Pruu> pruusFiltrados;
		seletor.setBloqueado(false);

		if (seletor.temPaginacao()) {
			int pageNumber = seletor.getPagina();
			int pageSize = seletor.getLimite();

			PageRequest page = PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Direction.DESC, "criadoEm"));
			pruusFiltrados = new ArrayList<Pruu>(pruuRepository.findAll(seletor, page).toList());
		} else {
			pruusFiltrados = new ArrayList<Pruu>(pruuRepository.findAll(seletor, Sort.by(Sort.Direction.DESC, "criadoEm")));
		}

		return pruusFiltrados;
	}

	public List<Pruu> pesquisarPruusCurtidosPeloUsuario(String usuarioId) throws PomboException {
		Usuario u = usuarioRepository.findById(usuarioId).orElseThrow(() -> new PomboException("Usuário não encontrado.", HttpStatus.BAD_REQUEST));

		return pruuRepository.findPruusCurtidosPorUsuario(u.getId());
	}

	public void salvarImagem(MultipartFile foto, String pruuId, String usuarioId) throws PomboException {
		Pruu pruu = pruuRepository.findById(pruuId)
				.orElseThrow(() -> new PomboException("Pruu não encontrado.", HttpStatus.NOT_FOUND));

		if (!pruu.getUsuario().getId().equals(usuarioId)) {
			throw new PomboException("Você não tem permissão para fazer esta ação.", HttpStatus.FORBIDDEN);
		}

		String imagemBase64 = imagemService.processarImagem(foto);
		pruu.setImagem(imagemBase64);
		pruuRepository.save(pruu);
	}

	public List<Usuario> pesquisarUsuariosQueCurtiram(String pruuId) throws PomboException {
		Pruu pruu = pruuRepository.findById(pruuId)
				.orElseThrow(() -> new PomboException("Pruu não encontrado.", HttpStatus.NOT_FOUND));
		return pruu.getUsuariosQueCurtiram();
	}
	
	public int contarPaginas(PruuSeletor seletor) {
	    if (seletor != null && seletor.temPaginacao()) {
	        int pageSize = seletor.getLimite();
	        PageRequest pagina = PageRequest.of(0, pageSize); // Página inicial apenas para contar

	        Page<Pruu> paginaResultado = pruuRepository.findAll(seletor, pagina);
	        return paginaResultado.getTotalPages(); // Retorna o número total de páginas
	    }

	    // Se não houver paginação, retorna 1 página se houver registros, ou 0 se não houver registros.
	    long totalRegistros = pruuRepository.count(seletor);
	    return totalRegistros > 0 ? 1 : 0;
	}

}