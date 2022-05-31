package com.tresn.minhasfinancas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
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
	
	@SpyBean
	UsuarioServiceImpl usuarioServiceImpl;
	
		
	@Test
	public void deveSalvarUmUsuario() {
		// cenario
		Mockito.doNothing().when(usuarioServiceImpl).validarEmail(Mockito.anyString());
		
		Usuario usuario = Usuario.builder().id(1l).nome("nome").email("email@email.com").senha("senha").build();
		
		Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		// ação ou execução
		Usuario usuarioSalvo = usuarioServiceImpl.salvarUsuario(new Usuario());
		
		
		// verificação
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
		
	}
	
	@Test(expected = RegraNegocioException.class)
	public void naoDeveSalvarUsuarioComEmailJaCadastrado() {
		// cenario
		String email = "email@email.com";
				
		Usuario usuario = Usuario.builder().email("email@email.com").build();
		
		Mockito.doThrow(RegraNegocioException.class).when(usuarioServiceImpl).validarEmail(email);
		
		// ação ou execução
		usuarioServiceImpl.salvarUsuario(usuario);
		
		
		// verificação
		Mockito.verify(usuarioRepository, Mockito.never()).save(usuario);
	}
	
	@Test(expected = Test.None.class)
	public void deveAutenticarUmUsuarioComSucesso() {
		
		// cenário
		String email = "email@email.com";
		String senha = "senha1234";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1L).build();
		
		Mockito.when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		// ação ou execução
		Usuario result = usuarioServiceImpl.autenticar(email, senha);
		
		// verificação
		Assertions.assertThat(result).isNotNull();
	}
	
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComEmailInformado() {
		
		// cenário
		Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		// ação ou execução
		Throwable exception = Assertions.catchThrowable ( () -> usuarioServiceImpl.autenticar("email@email.com", "senha"));
		
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
		Throwable exception = Assertions.catchThrowable ( () -> usuarioServiceImpl.autenticar("email@email.com", "senha-errada"));
		
		// verificação
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacaoException.class).hasMessage("Senha inválida.");
		
	}
	
	@Test(expected = Test.None.class)
	public void deveValidarEmail() {
		
		// cenário
		Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		// ação ou execução
		usuarioServiceImpl.validarEmail("3nelsonjunior@gmail.com");
		
		// verificação
		//não se aplica, pois esta verificando se vai lançar exeção
	}
	
	@Test(expected = RegraNegocioException.class)
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		
		// cenário
		Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		
		// ação ou execução
		usuarioServiceImpl.validarEmail("3nelsonjunior@gmail.com");
		
		// verificação
		//não se aplica, pois esta verificando se vai lançar exeção
	}
}
