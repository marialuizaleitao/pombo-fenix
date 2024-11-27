package com.vilu.pombo.model.entity;

import com.vilu.pombo.model.dto.UsuarioDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.validator.constraints.br.CPF;

import com.vilu.pombo.model.enums.Perfil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collection;

@Entity
@Data
public class Usuario implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@UuidGenerator
	private String id;

	@NotBlank(message = "O nome não pode estar em branco.")
	@Size(max = 200, message = "O nome deve ter entre 3 e 200 caracteres.")
	private String nome;

	@NotBlank(message = "O email não pode estar em branco.")
	@Email(message = "O email informado deve ser válido.")
	@Column(unique = true)
	private String email;

	@NotBlank(message = "O CPF não pode estar em branco.")
	@CPF(message = "O CPF informado deve ser válido.")
	@Column(unique = true)
	private String cpf;

	@NotBlank(message = "A senha não deve estar em branco.")
	@Size(max = 500)
	private String senha;

	@Column(columnDefinition = "TEXT")
	private String fotoDePerfil;

	@Enumerated(EnumType.STRING)
	private Perfil perfil = Perfil.USUARIO;

	@NotNull(message = "É obrigatório informar se o usuário é administrador.")
	private boolean isAdmin;

	@ToString.Exclude
	@OneToMany(mappedBy = "usuario")
	private List<Pruu> pruus;

	@ToString.Exclude
	@OneToMany(mappedBy = "usuario")
	@JsonBackReference(value = "usuario-denuncias")
	private List<Denuncia> denuncias = new ArrayList<>();

	@CreationTimestamp
	private LocalDateTime criadoEm;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(perfil.toString()));
		return authorities;
	}

	public String getUsername() {
		return this.email;
	}

	public String getPassword() {
		return this.senha;
	}

	public boolean isAdmin() {
		return this.perfil == Perfil.ADMINISTRADOR;
	}

	public UsuarioDTO toDTO() {
		UsuarioDTO dto = new UsuarioDTO();
		dto.setId(this.id);
		dto.setNome(this.nome);
		dto.setEmail(this.email);
		dto.setCpf(this.cpf);
		dto.setFotoDePerfil(this.fotoDePerfil);
		dto.setPerfil(this.perfil);
		dto.setPruusIds(this.pruus != null ? this.pruus.stream().map(Pruu::getId).collect(Collectors.toList()) : null);
		dto.setDenunciaIds(
				this.denuncias != null ? this.denuncias.stream().map(Denuncia::getId).collect(Collectors.toList())
						: null);
		dto.setCriadoEm(this.criadoEm);
		return dto;
	}

	public static Usuario fromDTO(UsuarioDTO dto, List<Pruu> pruus, List<Denuncia> denuncias) {
		Usuario usuario = new Usuario();
		usuario.setId(dto.getId());
		usuario.setNome(dto.getNome());
		usuario.setEmail(dto.getEmail());
		usuario.setCpf(dto.getCpf());
		usuario.setFotoDePerfil(dto.getFotoDePerfil());
		usuario.setPerfil(dto.getPerfil());
		usuario.setPruus(pruus);
		usuario.setDenuncias(denuncias);
		usuario.setCriadoEm(dto.getCriadoEm());
		return usuario;
	}

}
