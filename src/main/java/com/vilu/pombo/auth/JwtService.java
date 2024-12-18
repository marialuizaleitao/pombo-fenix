package com.vilu.pombo.auth;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.vilu.pombo.exception.PomboException;
import com.vilu.pombo.model.entity.Usuario;
import com.vilu.pombo.model.repository.UsuarioRepository;

@Service
public class JwtService {
    private final JwtEncoder jwtEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String getGenerateToken(Authentication authentication) throws PomboException {
        Instant now = Instant.now();
        long dezHorasEmSegundo = 36000L;

        String perfil = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

        Object principal = authentication.getPrincipal();
        Usuario authenticatedUser;

        if (principal instanceof Jwt) {
            Jwt jwt = (Jwt) principal;
            String login = jwt.getSubject();

            authenticatedUser = usuarioRepository.findByEmail(login).orElseThrow(() -> new PomboException("Usuário não encontrado.", HttpStatus.NOT_FOUND));
        } else {
            authenticatedUser = (Usuario) principal;
        }

        JwtClaimsSet claims = JwtClaimsSet.builder().issuer("pombo").issuedAt(now).expiresAt(now.plusSeconds(dezHorasEmSegundo)).subject(authentication.getName()).claim("perfil", perfil).claim("idUsuario", authenticatedUser.getId()).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

}