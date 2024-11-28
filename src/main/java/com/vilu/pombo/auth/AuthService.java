package com.vilu.pombo.auth;

import com.vilu.pombo.exception.PomboException;
import com.vilu.pombo.model.entity.Usuario;
import com.vilu.pombo.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final JwtService jwtService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public AuthService(JwtService jwtService, UsuarioRepository usuarioRepository) {
        this.jwtService = jwtService;
    }

    public String authenticate(Authentication authentication) throws PomboException {
        return jwtService.getGenerateToken(authentication);
    }

    public Usuario getUsuarioAutenticado() throws PomboException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioAutenticado = null;

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            Jwt jwt = (Jwt) principal;
            String login = jwt.getClaim("sub");

            usuarioAutenticado = usuarioRepository.findByEmail(login).orElseThrow(() -> new PomboException("Usuário não encontrado", HttpStatus.BAD_REQUEST));
        }

        if (usuarioAutenticado == null) {
            throw new PomboException("Usuário não encontrado", HttpStatus.BAD_REQUEST);
        }

        return usuarioAutenticado;
    }
}