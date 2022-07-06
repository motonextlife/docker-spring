package com.springproject.dockerspring.security;

import java.util.Collection;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;





/**************************************************************/
/*   [SysUserDetails]
/*   システム利用者の認証情報を保持する。
/*   各メソッドの解説は、SpringSecurityの説明書を見れば
/*   書いてあるので、あえて記載しない。
/**************************************************************/
public class SysUserDetails implements UserDetails{

	private final String username;
	private final String password;
	private final Collection<GrantedAuthority> authority;
	private final Boolean lock;

	public SysUserDetails(String username, String password, Collection<GrantedAuthority> authority, Boolean lock){
		this.username = username;
		this.password = password;
		this.authority = authority;
		this.lock = lock;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authority;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.lock;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}