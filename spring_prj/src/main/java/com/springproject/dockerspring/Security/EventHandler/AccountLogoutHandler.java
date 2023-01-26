/** 
 ******************************************************************************************
 * @file AccountLogoutHandler.java
 * @brief セキュリティに関する機能において、ログアウト処理が終わった後の際のハンドリングを
 * 行うクラスを格納したファイル。
 ******************************************************************************************
 */
package com.springproject.dockerspring.Security.EventHandler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springproject.dockerspring.Security.Entity.CertifiResponse;







/** 
 ******************************************************************************************
 * @brief セキュリティに関する機能において、ログアウト処理が終わった後の際のハンドリングを
 * 行うクラス
 * 
 * @par 使用アノテーション
 * - @Component
 * 
 * @see CertifiResponse
 ******************************************************************************************
 */
@Component
public class AccountLogoutHandler implements LogoutSuccessHandler{
  
  private final ObjectMapper mapper = new ObjectMapper();








	/** 
	 ******************************************************************************************
	 * @brief ログアウト処理が終わった後の際に実行する。
	 * 
	 * @details
	 * - レスポンスボディは、基本的にエンティティをJSON形式に変換して登録する。
	 * 
	 * @param[in] request リクエストクラス
	 * @param[in] response レスポンスクラス
	 * @param[in] authentication キャッチした認証処理情報
	 * 
	 * @par 大まかな処理の流れ
	 * -# レスポンスの情報として、以下の情報を登録する。
	 *    - ステータスコード: 200 OK
	 *    - レスポンスのコンテンツタイプ: JSON
	 * -# レスポンスボディに、ステータスコードの文字列とメッセージ本文をJSON変換して登録する。
	 * 
	 * @see CertifiResponse
	 ******************************************************************************************
	 */
  @Override
  public void onLogoutSuccess(HttpServletRequest request, 
                              HttpServletResponse response, 
                              Authentication authentication) throws IOException, ServletException {

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    String json_body = this.mapper.writeValueAsString(new CertifiResponse("OK", "logout success"));
    response.getOutputStream().println(json_body);
  }
}