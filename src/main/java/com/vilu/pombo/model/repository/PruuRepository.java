package com.vilu.pombo.model.repository;

import com.vilu.pombo.model.entity.Pruu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PruuRepository extends JpaRepository<Pruu, String>, JpaSpecificationExecutor<Pruu> {
}
