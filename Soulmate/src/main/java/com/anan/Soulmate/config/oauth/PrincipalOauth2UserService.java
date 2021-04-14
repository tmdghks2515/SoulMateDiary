package com.anan.Soulmate.config.oauth;

import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.anan.Soulmate.config.auth.PrincipalDetails;
import com.anan.Soulmate.config.oauth.provider.FacebookUserInfo;
import com.anan.Soulmate.config.oauth.provider.GoogleUserInfo;
import com.anan.Soulmate.config.oauth.provider.KakaoUserInfo;
import com.anan.Soulmate.config.oauth.provider.NaverUserInfo;
import com.anan.Soulmate.config.oauth.provider.OAuth2UserInfo;
import com.anan.Soulmate.model.User;
import com.anan.Soulmate.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
	
	final UserRepository userRepository;

	final BCryptPasswordEncoder passwordEncoder;

	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		// 페이스북 로그인 버튼 클릭 -> 페이스북 로그인 창 -> 로그인완료 -> code를 리턴(OAuth-Client 라이브러리) ->
		// AccessToken 요청
		// 여기 까지가 userReqest 정보 -> loadUser함수 호출 -> 회원프로필을 받는다
		OAuth2User oauth2User = super.loadUser(userRequest);
		// attributes 구조 확인
		System.out.println("attributes : " + oauth2User.getAttributes());

		// 회원가입을 강제로 진행
		OAuth2UserInfo oauth2UserInfo = null;
		if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			System.out.println("구글 로그인 요청");
			oauth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
		}else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
			System.out.println("페이스북 로그인 요청");
			oauth2UserInfo = new FacebookUserInfo(oauth2User.getAttributes());
		}else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
			System.out.println("네이버 로그인 요청");
			oauth2UserInfo = new NaverUserInfo((Map<String, Object>) oauth2User.getAttributes().get("response"));
		}else if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
			System.out.println("카카오 로그인 요청");
			oauth2UserInfo = new KakaoUserInfo(oauth2User.getAttributes());
		} else {
			System.out.println("구글,페이스북,네이버,카카오 만 지원합니다");
		}
		String username = oauth2UserInfo.getProvider() + "_" + oauth2UserInfo.getProviderId();
		String password = passwordEncoder.encode("소울mate");

		User user = userRepository.findByUsername(username);
		if (user == null) {
			System.out.println("연동하기 .... on!");
			user = User.builder().username(username)
					.password(password)
					.email(oauth2UserInfo.getEmail())
					.role("ROLE_USER")
					.name(oauth2UserInfo.getName())
					.build();
			userRepository.save(user);
		}
		return new PrincipalDetails(user, oauth2User.getAttributes());
	}
}