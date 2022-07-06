package com.springproject.dockerspring.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;





/**************************************************************/
/*   [CreaterSecurityConfig]
/*   制作者権限の認証に関する設定を定義する。
/**************************************************************/
@Configuration
@EnableWebSecurity
@Order(2)
public class CreaterSecurityConfig{

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
			form.loginProcessingUrl("/CreaterLogin")
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
	/*   [userDetailsManager]
	/*   インメモリで用いる認証情報を定義する。
	/**************************************************************/
	@Bean
	public InMemoryUserDetailsManager userDetailsManager() throws Exception {

		//パスワードはこのままだとバレバレなので、本番稼働環境ではハッシュ値をそのまま記載する。
		UserDetails creater = User.withUsername("test")
															.password(
																	PasswordEncoderFactories.createDelegatingPasswordEncoder()
																													.encode("password")
															)
															.roles(RoleEnum.ROLE_CREATOR.name())
															.build();

		return new InMemoryUserDetailsManager(creater);
	}
}