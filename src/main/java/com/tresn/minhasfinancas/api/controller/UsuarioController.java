package com.tresn.minhasfinancas.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuarioController {
	
	@GetMapping("/")
	public String HelloWord() {
		return "Hello Word!!!";
	}

}
