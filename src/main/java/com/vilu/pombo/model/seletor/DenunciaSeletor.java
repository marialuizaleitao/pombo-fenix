package com.vilu.pombo.model.seletor;

import com.vilu.pombo.model.entity.Denuncia;
import com.vilu.pombo.model.enums.Motivo;
import com.vilu.pombo.model.enums.StatusDenuncia;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class DenunciaSeletor extends BaseSeletor implements Specification<Denuncia> {

    private String idUsuario;
    private String idPruu;
    private Motivo motivo;
    private StatusDenuncia status;
    private LocalDateTime criadoEmInicio;
    private LocalDateTime criadoEmFim;

    @Override
    public Predicate toPredicate(Root<Denuncia> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (this.getIdUsuario() != null) {
            predicates.add(cb.equal(root.get("usuario").get("id"), this.getIdUsuario()));
        }

        if (this.getIdPruu() != null) {
            predicates.add(cb.equal(root.get("pruu").get("id"), this.getIdPruu()));
        }

        if (this.getMotivo() != null) {
            predicates.add(cb.equal(root.get("motivo"), this.getMotivo()));
        }

        if (this.getStatus() != null) {
            predicates.add(cb.equal(root.get("status"), this.getStatus()));
        }

        if (this.criadoEmInicio != null && this.criadoEmFim != null) {
            filtrarPorData(root, cb, predicates, this.getCriadoEmInicio(), this.getCriadoEmFim(), "criadoEm");
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}