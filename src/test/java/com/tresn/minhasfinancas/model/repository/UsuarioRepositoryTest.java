package com.tresn.minhasfinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.tresn.minhasfinancas.model.entity.Usuario;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveVerificarExistenciaDeUmEmail() {
		
		// cenário
		Usuario usuario = criarUsuario();
		
		entityManager.persist(usuario);
		
		
		// ação ou execução
		boolean result = usuarioRepository.existsByEmail("3nelsonjunior@gmail.com");
		
		// verificação
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
		
		// cenário
		// banco vazio
		
		
		// ação ou execução
		boolean result = usuarioRepository.existsByEmail("3nelsonjunior@gmail.com");
		
		// verificação
		Assertions.assertThat(result).isFalse();
	}
	
	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		
		// cenário
		Usuario usuario = criarUsuario();
		
		// ação ou execução
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		
		// verificação
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
	}
	
	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		
		// cenário
		Usuario usuario = criarUsuario();
		
		entityManager.persist(usuario);
		
		// ação ou execução
		Optional<Usuario> result = usuarioRepository.findByEmail(usuario.getEmail());
		
		// verificação
		Assertions.assertThat(result.isPresent()).isTrue();
	}
	
	private static Usuario criarUsuario() {
		return Usuario.builder()
				.nome("Nelson")
				.email("3nelsonjunior@gmail.com")
				.senha("senha")
				.build();
	}
	
}
