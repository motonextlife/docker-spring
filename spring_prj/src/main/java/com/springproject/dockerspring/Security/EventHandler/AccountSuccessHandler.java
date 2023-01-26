/** 
 ******************************************************************************************
 * @file AccountSuccessHandler.java
 * @brief セキュリティに関する機能において、ログインに成功した際のハンドリングを行うクラスを
 * 格納したファイル。
 ******************************************************************************************
 */
package com.springproject.dockerspring.Security.EventHandler;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springproject.dockerspring.Security.Entity.CertifiResponse;
import com.springproject.dockerspring.Security.Entity.AccountDetails;
import com.springproject.dockerspring.Security.Service.JwtTokenGenerate;

import lombok.RequiredArgsConstructor;








/** 
 ******************************************************************************************
 * @brief セキュリティに関する機能において、ログインに成功した際のハンドリングを行うクラス
 * 
 * @details 
 * - ログイン成功時には、次回認証のためのJWTトークンを生成する必要がある。トークンの生成をして
 * レスポンスのヘッダーに情報を格納する。
 * - トークン生成クラスのDIは、Lombokを用いたコンストラクタインジェクションにより実現する。
 *
 * @par 使用アノテーション
 * - @Component
 * - @RequiredArgsConstructor(onConstructor = @__(@Autowired))
 *
 * @see CertifiResponse
 * @see AccountDetails
 * @see JwtTokenGenerate
 ******************************************************************************************
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountSuccessHandler implements AuthenticationSuccessHandler{

  private final JwtTokenGenerate jwt_token;
  private final ObjectMapper mapper = new ObjectMapper();



  





	/** 
	 ******************************************************************************************
	 * @brief ログインに成功した際の後処理を定義する。
	 * 
	 * @details
	 * - 次回アクセス時の情報として、JWTトークンを作成しヘッダーに登録する。これによって、セッションを
	 * 作成する必要がなく、ステートレスな認証が可能になる。
	 * - レスポンスボディは、基本的にエンティティをJSON形式に変換して登録する。
	 * 
	 * @param[in] request リクエストクラス
	 * @param[in] response レスポンスクラス
	 * @param[in] accessDeniedException キャッチした認可エラー例外
	 * 
	 * @par 大まかな処理の流れ
	 * -# 認証コンテキストから、現在認証中のアカウントの認証情報を取得する。
	 * -# フロント側の画面制御の為に必要な情報を、ヘッダーに登録する。内容は以下のようになる。
	 *    - 画面のアクセス制御をする、権限情報（リストをカンマでつないで登録）
	 *    - 画面に表示するユーザー名
	 * -# 取得した認証情報を用いて、JWTトークンを作成し、ヘッダーに登録する。
	 * -# レスポンスの情報として、以下の情報を登録する。
	 *    - ステータスコード: 200 OK
	 *    - レスポンスのコンテンツタイプ: JSON
	 * -# レスポンスボディに、ステータスコードの文字列とメッセージ本文をJSON変換して登録する。
	 * 
	 * @see CertifiResponse
	 * @see AccountDetails
	 ******************************************************************************************
	 */
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, 
                                      HttpServletResponse response, 
                                      Authentication authentication) throws IOException, ServletException {

    AccountDetails user_details = (AccountDetails)authentication.getPrincipal();

    String username = user_details.getUsername();
    String role_list = user_details.getAuthorities().stream()
                                                    .map(s -> s.getAuthority())
                                                    .collect(Collectors.joining(","));
    response.setHeader("X_USERNAME", username);
    response.setHeader("X_ROLE_LIST", role_list);

    try {
      response.setHeader("X_AUTH_TOKEN", jwt_token.makeToken(user_details));
    } catch (Exception e) {
      throw new IOException("Error location [AccountSuccessHandler:onAuthenticationSuccess]" + "\n" + e);
    }

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    String json_body = this.mapper.writeValueAsString(new CertifiResponse("OK", "login success"));
    response.getOutputStream().println(json_body);
  }
}