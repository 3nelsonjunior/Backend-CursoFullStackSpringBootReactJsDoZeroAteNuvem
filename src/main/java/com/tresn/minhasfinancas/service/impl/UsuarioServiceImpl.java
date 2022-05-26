package com.tresn.minhasfinancas.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tresn.minhasfinancas.exceptions.ErroAutenticacaoException;
import com.tresn.minhasfinancas.exceptions.RegraNegocioException;
import com.tresn.minhasfinancas.model.entity.Usuario;
import com.tresn.minhasfinancas.model.repository.UsuarioRepository;
import com.tresn.minhasfinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService{
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
		super();
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	@Transactional
	public Usuario autenticar(String email, String senha) {

		Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErroAutenticacaoException("Usuário não encontrado.");
		}
		
		if(!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacaoException("Senha inválida.");
		}
		
		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		
		validarEmail(usuario.getEmail());
		
		return usuarioRepository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		
		boolean existe = usuarioRepository.existsByEmail(email);
		
		if(existe) {
			throw new RegraNegocioException("Email já cadastrado.");
		}
		
	}

}
