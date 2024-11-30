package com.vilu.pombo.model.repository;

import com.vilu.pombo.model.entity.Pruu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PruuRepository extends JpaRepository<Pruu, String>, JpaSpecificationExecutor<Pruu> {
    @Query(value = "SELECT pruu.* FROM pruu JOIN pruu_curtida ON pruu_id = pruu.id WHERE pruu_curtida.usuario_id = :usuarioId", nativeQuery = true)
    List<Pruu> findPruusCurtidosPorUsuario(@Param("usuarioId") String usuarioId);
}


