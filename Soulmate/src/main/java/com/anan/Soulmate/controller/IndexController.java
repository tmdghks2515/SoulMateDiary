package com.anan.Soulmate.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.anan.Soulmate.config.auth.PrincipalDetails;
import com.anan.Soulmate.dto.ResponseDTO;
import com.anan.Soulmate.model.User;
import com.anan.Soulmate.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class IndexController {
	
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final UserRepository userRepository;
	
	@GetMapping("/")
	public String index(@AuthenticationPrincipal PrincipalDetails principalDetails) {
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
	public String joinProc(User user, HttpServletResponse res) {
		User userEntity = userRepository.findByUsername(user.getUsername());
		System.out.println("userEntity : "+userEntity);
		if(userEntity != null) {
			JSONObject json = new JSONObject(new ResponseDTO<String>(201,"아이디가 중복됩니다"));
			try {
				res.getWriter().write(json.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		user.setRole("ROLE_USER");
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userRepository.save(user);
		JSONObject json = new JSONObject(new ResponseDTO<String>(200,"회원가입 성공"));
		try {
			res.getWriter().write(json.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
