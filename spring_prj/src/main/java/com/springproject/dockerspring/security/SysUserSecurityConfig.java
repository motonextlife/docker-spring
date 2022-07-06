package com.springproject.dockerspring.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;






/**************************************************************/
/*   [SysUserSecurityConfig]
/*   通常のシステム利用者権限の認証に関する設定を定義する。
/**************************************************************/
@Configuration
@EnableWebSecurity
@Order(1)
public class SysUserSecurityConfig{

	@Bean
	public BCryptPasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}





	/**************************************************************/
	/*   [webSecurityCustomizer]
	/*   SpringSecurityの認証対象外とするファイルを定義。
	/*   ここでは、静的リソースは対象外とする。
	/**************************************************************/
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() throws Exception {
			return web -> web.ignoring().mvcMatchers("/images", "/css", "/javascript");
	}






	/**************************************************************/
	/*   [securityFilterChain]
	/*   認証範囲とログイン＆ログアウトのURLを定義する。
	/**************************************************************/
	@Bean
	protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(auth -> {
			auth.mvcMatchers("/System").authenticated();
		});

		http.formLogin(form -> {
			form.loginProcessingUrl("/SystemLogin")
					.failureUrl("/SystemLogin/LoginFail")
					.defaultSuccessUrl("/SystemLogin/LoginSuccess")
					.usernameParameter("username")
					.passwordParameter("password")
					.permitAll();
		});

		http.logout(logout -> {
			logout.logoutUrl("/SystemLogin/Logout")
						.logoutSuccessUrl("/SystemLogin/LogoutSuccess")
						.permitAll();
		});

		return http.build();
	}






	/**************************************************************/
	/*   [userDetailsService]
	/*   データベースの利用者情報を用いた認証情報を定義する。
	/**************************************************************/
	@Bean
	protected UserDetailsService userDetailsService() throws Exception {	
		return new SysUserDetailsService();
	}
}