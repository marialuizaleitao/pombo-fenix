package com.vilu.pombo.service;

import com.vilu.pombo.auth.AuthService;
import com.vilu.pombo.exception.PomboException;
import com.vilu.pombo.model.entity.Pruu;
import com.vilu.pombo.model.entity.Usuario;

import com.vilu.pombo.model.repository.UsuarioRepository;
import com.vilu.pombo.model.seletor.UsuarioSeletor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ImagemService imagemService;

    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private AuthService authService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails) usuarioRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado " + username));
    }

    public void cadastrar(Usuario usuario) throws PomboException {
        if (usuarioRepository.existsByEmailIgnoreCase(usuario.getEmail())) {
            throw new PomboException("O e-mail informado já está cadastrado. Por favor, utilize um e-mail diferente.", HttpStatus.BAD_REQUEST);
        }

        if (usuarioRepository.existsByCpf(usuario.getCpf())) {
            throw new PomboException("O CPF informado já está registrado. Por favor, verifique os dados ou utilize outro CPF.", HttpStatus.BAD_REQUEST);
        }

        usuarioRepository.save(usuario);
    }

    public void cadastrarAdmin(Usuario usuario) throws PomboException {
        if (usuarioRepository.existsByEmailIgnoreCase(usuario.getEmail())) {
            throw new PomboException("O e-mail informado já está cadastrado. Por favor, utilize um e-mail diferente.", HttpStatus.BAD_REQUEST);
        }

        if (usuarioRepository.existsByCpf(usuario.getCpf())) {
            throw new PomboException("O CPF informado já está registrado. Por favor, verifique os dados ou utilize outro CPF.", HttpStatus.BAD_REQUEST);
        }

        usuarioRepository.save(usuario);
    }

    public Usuario atualizar(Usuario usuarioASerAtualizado) throws PomboException {
        Usuario usuarioExistente = usuarioRepository.findById(usuarioASerAtualizado.getId()).orElseThrow(() -> new PomboException("Usuário não encontrado.", HttpStatus.NOT_FOUND));

        usuarioExistente.setNome(usuarioASerAtualizado.getNome());
        usuarioExistente.setEmail(usuarioASerAtualizado.getEmail());

        if (usuarioASerAtualizado.getSenha() != null && !usuarioASerAtualizado.getSenha().isEmpty()) {
            usuarioExistente.setSenha(encoder.encode(usuarioASerAtualizado.getSenha()));
        }

        return usuarioRepository.save(usuarioExistente);
    }

    public void excluir(String id) throws PomboException {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new PomboException("Usuário não encontrado.", HttpStatus.NOT_FOUND));

        if (usuario.getPruus().isEmpty() && usuario.getDenuncias().isEmpty()) {
            usuarioRepository.deleteById(id);
        } else {
            throw new PomboException("Usuários com pruus postados ou denúncias criadas não podem ser deletados.", HttpStatus.BAD_REQUEST);
        }
    }

    public List<Usuario> pesquisarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario pesquisarPorId(String id) throws PomboException {
        return usuarioRepository.findById(id).orElseThrow(() -> new PomboException("Usuário não encontrado.", HttpStatus.NOT_FOUND));
    }

    public List<Usuario> pesquisarComFiltros(UsuarioSeletor seletor) {
        if (seletor.temPaginacao()) {
            int pageNumber = seletor.getPagina();
            int pageSize = seletor.getLimite();

            PageRequest page = PageRequest.of(pageNumber - 1, pageSize);
            return usuarioRepository.findAll(seletor, page).toList();
        }

        return usuarioRepository.findAll(seletor);
    }

    public void salvarFotoDePerfil(MultipartFile foto, String usuarioId) throws PomboException {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new PomboException("Usuário não encontrado.", HttpStatus.NOT_FOUND));
        String imagemBase64 = imagemService.processarImagem(foto);
        usuario.setFotoDePerfil(imagemBase64);
        System.out.println(imagemBase64);
        usuarioRepository.save(usuario);
    }

}