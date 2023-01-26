/** 
 ******************************************************************************************
 * @file AccountDeniedHandler.java
 * @brief セキュリティに関する機能において、認証済みのユーザーではあるがアクセス先のURLの
 * 権限を持っておらずアクセス拒否された際に発するイベントのハンドリングを行うクラスを
 * 格納するファイル。
 ******************************************************************************************
 */
package com.springproject.dockerspring.Security.EventHandler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springproject.dockerspring.Security.Entity.CertifiResponse;







/** 
 ******************************************************************************************
 * @brief セキュリティに関する機能において、認証済みのユーザーではあるがアクセス先のURLの
 * 権限を持っておらずアクセス拒否された際に発するイベントのハンドリングを行うクラス
 * 
 * @par 使用アノテーション
 * - @Component
 * 
 * @see CertifiResponse
 ******************************************************************************************
 */
@Component
public class AccountDeniedHandler implements AccessDeniedHandler{

  private final ObjectMapper mapper = new ObjectMapper();








	/** 
	 ******************************************************************************************
	 * @brief 認証済みのユーザーではあるがアクセス先のURLの権限を持っておらずアクセス拒否された際
	 * に実行する。
	 * 
	 * @details
	 * - レスポンスボディは、基本的にエンティティをJSON形式に変換して登録する。
	 * 
	 * @param[in] request リクエストクラス
	 * @param[in] response レスポンスクラス
	 * @param[in] accessDeniedException キャッチした認可エラー例外
	 * 
	 * @par 大まかな処理の流れ
	 * -# レスポンスの情報として、以下の情報を登録する。
	 *    - ステータスコード: 403 Forbidden
	 *    - レスポンスのコンテンツタイプ: JSON
	 * -# レスポンスボディに、ステータスコードの文字列とメッセージ本文をJSON変換して登録する。
	 * 
	 * @see CertifiResponse
	 ******************************************************************************************
	 */
  @Override
  public void handle(HttpServletRequest request, 
                     HttpServletResponse response,
                     AccessDeniedException accessDeniedException) throws IOException, ServletException {

    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    String json_body = this.mapper.writeValueAsString(new CertifiResponse("Forbidden", "no access here"));
    response.getOutputStream().println(json_body);
  }
}