/** 
 ******************************************************************************************
 * @file AccountEntryPoint.java
 * @brief セキュリティに関する機能において、認証されていないユーザーが、直接認証が必要な
 * URLにアクセスした場合に拒否された際のハンドリングを行うクラスを格納したファイル。
 ******************************************************************************************
 */
package com.springproject.dockerspring.Security.EventHandler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springproject.dockerspring.Security.Entity.CertifiResponse;








/** 
 ******************************************************************************************
 * @brief セキュリティに関する機能において、認証されていないユーザーが、直接認証が必要な
 * URLにアクセスした場合に拒否された際のハンドリングを行うクラス
 * 
 * @par 使用アノテーション
 * - @Component
 * 
 * @see CertifiResponse
 ******************************************************************************************
 */
@Component
public class AccountEntryPoint implements AuthenticationEntryPoint{

  private final ObjectMapper mapper = new ObjectMapper();








	/** 
	 ******************************************************************************************
	 * @brief 認証されていないユーザーが、直接認証が必要なURLにアクセスした場合に拒否された際
	 * に実行する。
	 * 
	 * @details
	 * - レスポンスボディは、基本的にエンティティをJSON形式に変換して登録する。
	 * 
	 * @param[in] request リクエストクラス
	 * @param[in] response レスポンスクラス
	 * @param[in] authException キャッチした認証エラー例外
	 * 
	 * @par 大まかな処理の流れ
	 * -# レスポンスの情報として、以下の情報を登録する。
	 *    - ステータスコード: 401 Unauthorized
	 *    - レスポンスのコンテンツタイプ: JSON
	 * -# レスポンスボディに、ステータスコードの文字列とメッセージ本文をJSON変換して登録する。
	 * 
	 * @see CertifiResponse
	 ******************************************************************************************
	 */
  @Override
  public void commence(HttpServletRequest request, 
                       HttpServletResponse response, 
                       AuthenticationException authException) throws IOException, ServletException {

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    String json_body = this.mapper.writeValueAsString(new CertifiResponse("Unauthorized", "uncertified"));
    response.getOutputStream().println(json_body);
  }
}