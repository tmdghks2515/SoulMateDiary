package com.anan.Soulmate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class IndexController {
	@GetMapping("/")
	public String index() {
		return "index";
	}
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	@PostMapping("/joinProc")
	public String joinProc() {
		
		return null;
	}
}
