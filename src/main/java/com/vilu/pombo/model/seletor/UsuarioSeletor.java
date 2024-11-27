package com.vilu.pombo.model.seletor;

import com.vilu.pombo.model.entity.Usuario;
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
public class UsuarioSeletor extends BaseSeletor implements Specification<Usuario> {

    private String nome;
    private String email;
    private String cpf;
    private LocalDateTime criadoEmInicio;
    private LocalDateTime criadoEmFim;

    @Override
    public Predicate toPredicate(Root<Usuario> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (this.getNome() != null && !this.getNome().trim().isEmpty()) {
            predicates.add(cb.like(root.get("nome"), "%" + this.getNome() + "%"));
        }

        if (this.getEmail() != null && !this.getEmail().trim().isEmpty()) {
            predicates.add(cb.like(root.get("email"), "%" + this.getEmail() + "%"));
        }

        if (this.getCpf() != null && !this.getCpf().trim().isEmpty()) {
            predicates.add(cb.like(root.get("cpf"), "%" + this.getCpf() + "%"));
        }

        if(this.criadoEmInicio != null && this.criadoEmFim != null) {
            filtrarPorData(root, cb, predicates, this.getCriadoEmInicio(), this.getCriadoEmFim(), "criadoEm");
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}