package com.anan.Soulmate.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.anan.Soulmate.config.oauth.PrincipalOauth2UserService;
import com.anan.Soulmate.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity  // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨
// secured 어노테이션 활성화 , preAuthorize, postAuthorize 어노테이션 활성화
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig  extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;
	
	// 패스워드 암호화를 해주는 객체를 리턴하는 매서드
	@Bean   // Bean: 해당 메서드의 리턴되는 오브젝트를 IOC로 등록해준다
	public BCryptPasswordEncoder encodePWD() {
		return new BCryptPasswordEncoder();
	} 

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers("/user/**").authenticated()   // 인증만 되면 접근가능
			.antMatchers("/manager/**").access("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MANAGER')")
			.antMatchers("/admin/**").access("hasAuthority('ROLE_ADMIN')")
			.anyRequest().permitAll()
			.and()
			.formLogin()
			.loginPage("/loginForm")      // 인증이 필요한 페이지에 접근시 이 페이지로 이동
			.loginProcessingUrl("/login")    //1. 스프링 시큐리티가 해당 주소로 요청하는 로그인을 가로채서 대신 로그인을 해줌
			.defaultSuccessUrl("/user/diaryMain")  // 정상적으로 요청이 완료되면 이동할 페이지 주소
			.failureUrl("/loginForm")  // 로그인 실패시 이동할 페이지 주소
			.and()
			.oauth2Login()
			.loginPage("/loginForm")
			.userInfoEndpoint()
			.userService(principalOauth2UserService)
			;
	}
}
