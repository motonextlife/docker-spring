/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.Security
 * 
 * @brief このシステムを管理するセキュリティ関連の機能全般を格納するパッケージ。
 * 
 * @details
 * - このパッケージでは、SpringSecurityを使用する際の設定ファイルや、認証や認可処理を行う
 * ハンドラクラスを格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Security;





/** 
 ******************************************************************************************
 * @file AccountSecurityConfig.java
 * @brief このシステム上のSpringSecurityのセキュリティ設定を全般的に設定するクラスを格納する
 * ファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.beans.factory.annotation.Autowired;

import com.springproject.dockerspring.Security.EventHandler.AccountDeniedHandler;
import com.springproject.dockerspring.Security.EventHandler.AccountEntryPoint;
import com.springproject.dockerspring.Security.EventHandler.AccountFailureHandler;
import com.springproject.dockerspring.Security.EventHandler.AccountLogoutHandler;
import com.springproject.dockerspring.Security.EventHandler.AccountSuccessHandler;
import com.springproject.dockerspring.Security.EventHandler.AccountTokenFilter;
import com.springproject.dockerspring.Security.Service.AccountDetailsService;

import lombok.RequiredArgsConstructor;










/** 
 ******************************************************************************************
 * @brief このシステム上のSpringSecurityのセキュリティ設定を全般的に設定するクラス
 * 
 * @details 
 * - 基本的にセキュリティに関する設定は、このクラスに記載する。
 * - 詳しい仕様を把握した場合は、SpringSecurityのドキュメントを参照のこと。
 * - なお、このクラスに実装しているメソッドはすべて、Beanとして登録するようになっている。
 * 
 * @par 使用アノテーション
 * - @Configuration
 * - @EnableWebSecurity
 * - @EnableWebMvc
 * 
 * @see AccountSuccessHandler
 * @see AccountFailureHandler
 * @see AccountEntryPoint
 * @see AccountDeniedHandler
 * @see AccountTokenFilter
 * @see AccountLogoutHandler
 * @see AccountDetailsService
 ******************************************************************************************
 */
@Configuration
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountSecurityConfig{

	private final AccountSuccessHandler success_handler;
	private final AccountFailureHandler failure_handler;
	private final AccountEntryPoint entry_point;
	private final AccountDeniedHandler denied_handler;
	private final AccountTokenFilter token_filter;
	private final AccountLogoutHandler logout_handler;
	private final AccountDetailsService details_service;






	/** 
	 ******************************************************************************************
	 * @brief 認証の際に利用するハッシュ関数を登録する。
	 * 
	 * @details 
	 * - ハッシュ関数としては、[BCrypt]を採用する。
	 * 
	 * @return ハッシュ関数のエンコーダ
	 ******************************************************************************************
	 */
  @Bean
  public BCryptPasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }






	/** 
	 ******************************************************************************************
	 * @brief 認証の際に発生するイベントを検知して、処理を行えるようにする。
	 * 
	 * @param[in] pub アプリケーションのイベント受付設定クラス
	 * 
	 * @return イベント受付の設定クラス
	 ******************************************************************************************
	 */
  @Bean
  public AuthenticationEventPublisher authenticationEventPublisher(ApplicationEventPublisher pub){
    return new DefaultAuthenticationEventPublisher(pub);
  }







	/** 
	 ******************************************************************************************
	 * @brief 認証時の設定を一元的に設定する。
	 * 
	 * @details 設定内容としては、以下のようになる。
	 * -# セキュリティを適用するURL
	 * 		- [/System]配下のパスをすべて適用する。
	 * -# ログイン時の設定
	 * 		- ログイン時のURL: [/login]
	 * 		- ログイン成功時に呼び出すハンドラクラス: AccountSuccessHandler
	 * 		- ログイン失敗時に呼び出すハンドラクラス: AccountFailureHandler
	 * 		- ログイン時のユーザー名のパラメータ名: username
	 * 		- ログイン時のパスワードのパラメータ名: password
	 * 		- なお、ログイン時に使用するURLはセキュリティの対象外とする
	 * -# セッションのマネジメント設定
	 * 		- このシステムのSpringsSecurityは、セッションを一切生成しない設定とする
	 * -# 認証や認可失敗時のハンドラ設定
	 * 		- 認証されていないユーザーが直接、認証が必要なURLにアクセスしたとき: AccountEntryPoint
	 * 		- 認証されているが、必要な権限を持っていないユーザーがアクセスしたとき: AccountDeniedHandler
	 * -# 認証処理前に行うフィルター
	 * 		- JWTトークンの検証クラス: AccountTokenFilter
	 * 		- なおこのフィルターは、[UsernamePasswordAuthenticationFilter]の実行前に必ず行う。
	 * -# CSRFトークン生成の設定
	 * 		- CSRF対策は無効とする。JWTトークンで要件を満たせるからである。
	 * -# ログアウト時の設定
	 * 		- ログアウト時のURL: [/Logout]
	 * 		- 生成されたセッションはすべて無効にし削除する。
	 * 		- ログアウト成功時に呼び出すハンドラ: AccountLogoutHandler
	 * 
	 * @param[in] http HTTP通信時のセキュリティ設定のクラス
	 * 
	 * @return セキュリティの設定を組み立てた設定クラス
	 * 
	 * @see AccountSuccessHandler
	 * @see AccountFailureHandler
	 * @see AccountEntryPoint
	 * @see AccountDeniedHandler
	 * @see AccountTokenFilter
	 * @see AccountLogoutHandler
	 ******************************************************************************************
	 */
	@Bean
	protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(auth -> {
			auth.mvcMatchers("/System/**").authenticated();
		});

		http.formLogin(form -> {
			form.loginProcessingUrl("/Login")
					.successHandler(this.success_handler)
					.failureHandler(this.failure_handler)
					.usernameParameter("username")
					.passwordParameter("password")
					.permitAll();
		});

		http.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.exceptionHandling(excep -> {
      excep.authenticationEntryPoint(this.entry_point)
           .accessDeniedHandler(this.denied_handler);
    });

		http.addFilterBefore(this.token_filter, UsernamePasswordAuthenticationFilter.class);

    http.csrf(csrf -> {
      csrf.disable();
    });

		http.logout(logout -> {
			logout.logoutUrl("/Logout")
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID")
						.logoutSuccessHandler(this.logout_handler)
						.permitAll();
		});

		return http.build();
	}


	




	/** 
	 ******************************************************************************************
	 * @brief データベースから認証情報を取得し、専用のエンティティに格納してBeanに登録する。
	 * 
	 * @details 
	 * - 認証に関しては、インメモリ認証は一切用いない。
	 * - データベースからの認証情報の取得に関しては、独自に実装したサービスクラスを実装して
	 * 取得する。
	 * 
	 * @return 認証情報を格納した専用エンティティ
	 * 
	 * @see AccountDetailsService
	 ******************************************************************************************
	 */
	@Bean
	protected UserDetailsService userDetailsService() throws Exception {	
		return this.details_service;
	}
}