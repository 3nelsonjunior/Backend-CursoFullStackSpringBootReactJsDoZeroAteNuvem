package com.tresn.minhasfinancas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.tresn.minhasfinancas.exceptions.ErroAutenticacaoException;
import com.tresn.minhasfinancas.exceptions.RegraNegocioException;
import com.tresn.minhasfinancas.model.entity.Usuario;
import com.tresn.minhasfinancas.model.repository.UsuarioRepository;
import com.tresn.minhasfinancas.service.impl.UsuarioServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	@MockBean
	UsuarioRepository usuarioRepository;
	
	UsuarioService usuarioService;
	
	@Before
	public void setUp() {
		usuarioService = new UsuarioServiceImpl(usuarioRepository);
	}
	
	@Test(expected = Test.None.class)
	public void deveAutenticarUmUsuarioComSucesso() {
		
		// cenário
		String email = "email@email.com";
		String senha = "senha1234";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1L).build();
		
		Mockito.when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		// ação ou execução
		Usuario result = usuarioService.autenticar(email, senha);
		
		// verificação
		Assertions.assertThat(result).isNotNull();
	}
	
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComEmailInformado() {
		
		// cenário
		Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		// ação ou execução
		Throwable exception = Assertions.catchThrowable ( () -> usuarioService.autenticar("email@email.com", "senha"));
		
		// verificação
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacaoException.class).hasMessage("Usuário não encontrado.");
	}
	
	@Test
	public void deveLacarErroQuandoSenhaNaoBater() {
		
		// cenário
		String senha = "senha1234";
		
		Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).id(1L).build();
		
		Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		// ação ou execução
		Throwable exception = Assertions.catchThrowable ( () -> usuarioService.autenticar("email@email.com", "senha-errada"));
		
		// verificação
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacaoException.class).hasMessage("Senha inválida.");
		
	}
	
	@Test(expected = Test.None.class)
	public void deveValidarEmail() {
		
		// cenário
		Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		// ação ou execução
		usuarioService.validarEmail("3nelsonjunior@gmail.com");
		
		// verificação
		//não se aplica, pois esta verificando se vai lançar exeção
	}
	
	@Test(expected = RegraNegocioException.class)
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		
		// cenário
		Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		
		// ação ou execução
		usuarioService.validarEmail("3nelsonjunior@gmail.com");
		
		// verificação
		//não se aplica, pois esta verificando se vai lançar exeção
	}
}
