package com.tresn.minhasfinancas.api.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tresn.minhasfinancas.api.dto.UsuarioDto;
import com.tresn.minhasfinancas.exceptions.RegraNegocioException;
import com.tresn.minhasfinancas.model.entity.Usuario;
import com.tresn.minhasfinancas.service.UsuarioService;

@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@PostMapping()
	public ResponseEntity salvar(@RequestBody UsuarioDto dto) {

		Usuario usuario = Usuario.builder()
									.nome(dto.getNome())
									.email(dto.getEmail())
									.senha(dto.getSenha())
									.dataCadastro(LocalDate.now())
									.build();
		try {
			Usuario usuarioSalvo = usuarioService.salvarUsuario(usuario);
			
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
			
		} catch (RegraNegocioException e) {
			return  ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}

}
