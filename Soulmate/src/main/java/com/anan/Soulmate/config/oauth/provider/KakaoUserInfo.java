package com.anan.Soulmate.config.oauth.provider;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {
	
	private Map<String, Object> attributes;
	
	public KakaoUserInfo(Map<String, Object> attributes) {
		super();
		this.attributes = attributes;
	}

	@Override
	public String getProviderId() {
		return String.valueOf(attributes.get("id")) ;
	}

	@Override
	public String getProvider() {
		return "kakao";
	}

	@Override
	public String getEmail() {
		return (String) ((Map<String, Object>) attributes.get("kakao_account")).get("email");
	}

	@Override
	public String getName() {
		return  (String) ((Map<String, Object>) attributes.get("properties")).get("nickname");
	}

}
