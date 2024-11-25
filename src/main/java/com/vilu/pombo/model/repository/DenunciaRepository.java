package com.vilu.pombo.model.repository;

import com.vilu.pombo.model.entity.Denuncia;
import com.vilu.pombo.model.seletor.DenunciaSeletor;
import org.springframework.data.domain.ManagedTypes;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, String>, JpaSpecificationExecutor<Denuncia> {
}