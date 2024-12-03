package com.vilu.pombo.model.seletor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.vilu.pombo.model.entity.Pruu;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;

@Data
public class PruuSeletor extends BaseSeletor implements Specification<Pruu> {

	private String idUsuario;
	private String texto;
	private LocalDateTime criadoEmInicio;
	private LocalDateTime criadoEmFim;
	private Boolean bloqueado;
	
	@Override
	public Predicate toPredicate(Root<Pruu> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		List<Predicate> predicates = new ArrayList<>();

		if (this.getBloqueado() != null) {
			predicates.add(cb.equal(root.get("bloqueado"), this.getBloqueado()));
		}
		
		if (this.getTexto() != null && !this.getTexto().trim().isEmpty()) {
			predicates.add(cb.like(root.get("texto"), "%" + this.getTexto() + "%"));
		}

		if (this.getIdUsuario() != null) {
			predicates.add(cb.equal(root.get("usuario").get("id"), this.getIdUsuario()));
		}

		if (this.criadoEmInicio != null && this.criadoEmFim != null) {
			filtrarPorData(root, cb, predicates, this.getCriadoEmInicio(), this.getCriadoEmFim(), "criadoEm");
		}

		return cb.and(predicates.toArray(new Predicate[0]));
	}
}