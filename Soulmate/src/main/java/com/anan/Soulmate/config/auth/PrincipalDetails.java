package com.anan.Soulmate.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.anan.Soulmate.model.Soulmate;
import com.anan.Soulmate.model.User;
import com.anan.Soulmate.repository.SoulmateRepository;

import lombok.RequiredArgsConstructor;

public class PrincipalDetails implements UserDetails, OAuth2User{

	private User user;
	private Soulmate soulmate;
	private Map<String, Object> attributes;
	
	public PrincipalDetails(User user) {
		this.user = user;
		if(this.user == null)
			this.user = new User();
	}
	
	public PrincipalDetails(User user, Map<String, Object> attributes) {
		this.user = user;
		if(this.user == null)
			this.user = new User();
		this.attributes = attributes;
	}

	public User getUser() {
		return user;
	}
	public Soulmate getSoulmate() {
		return soulmate;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
		return null;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
