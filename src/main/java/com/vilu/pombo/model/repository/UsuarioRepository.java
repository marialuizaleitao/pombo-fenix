package com.vilu.pombo.model.repository;

import com.vilu.pombo.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String>, JpaSpecificationExecutor<Usuario> {
    boolean existsByEmailIgnoreCase(String email);

    Optional<Usuario> findByEmail(String email);

    boolean existsByCpf(String cpf);

    Optional<Usuario> findById(String id);
}