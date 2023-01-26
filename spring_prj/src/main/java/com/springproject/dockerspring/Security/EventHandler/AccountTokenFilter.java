/** 
 ******************************************************************************************
 * @file AccountTokenFilter.java
 * @brief セキュリティに関する機能において、ユーザーから渡ってきたJWTトークンを検証する
 * クラスを格納したファイル。
 ******************************************************************************************
 */
package com.springproject.dockerspring.Security.EventHandler;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springproject.dockerspring.Security.Entity.CertifiResponse;
import com.springproject.dockerspring.Security.Entity.AccountDetails;
import com.springproject.dockerspring.Security.Service.JwtTokenGenerate;

import lombok.RequiredArgsConstructor;









/** 
 ******************************************************************************************
 * @brief セキュリティに関する機能において、ユーザーから渡ってきたJWTトークンを検証する
 * クラス
 * 
 * @details 
 * - このフィルターは、[UsernamePasswordAuthenticationFilter]の前に実行される。
 * - トークン生成クラスのDIは、Lombokを用いたコンストラクタインジェクションにより実現する。
 * - トークンの状態により、そのままログイン処理に移行するか、認証コンテキストを操作して
 * アクセスの許可や拒否を行う。
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
public class AccountTokenFilter extends GenericFilterBean{

  private final JwtTokenGenerate jwt_token;
  private final ObjectMapper mapper = new ObjectMapper();







	/** 
	 ******************************************************************************************
	 * @brief フィルターとして行う処理を定義する。
	 * 
	 * @details
	 * - エラーだった場合のレスポンスボディは、基本的にエンティティをJSON形式に変換して登録する。
	 * 
	 * @param[in] request リクエストクラス
	 * @param[in] response レスポンスクラス
	 * @param[in] chain フィルターチェーン
	 * 
	 * @par 大まかな処理の流れ
	 * -# 受け付けたリクエストの中のヘッダー情報からトークンを取り出す。この際に、トークンが
	 * 存在しなければ初回のアクセスと判断し、通常のログイン処理へ引き継ぐ。
	 * -# トークンを解析し、改ざんがなく正当な情報であることを確認する。もしエラーだった
	 * 場合は、エラーレスポンスとして以下の情報を登録する。
	 *    - ステータスコード: 401 Unauthorized
	 *    - レスポンスのコンテンツタイプ: JSON
	 * -# エラーレスポンスボディに、ステータスコードの文字列とメッセージ本文をJSON変換して登録する。
	 * -# 認証に成功した場合はフロント側の画面制御の為に必要な情報を、ヘッダーに登録する。
   * 内容は以下のようになる。
	 *    - 画面のアクセス制御をする、権限情報（リストをカンマでつないで登録）
	 *    - 画面に表示するユーザー名
	 * -# JWTトークンの値を利用して、認証コンテキストを作成し登録することで、認証を終える。
	 * -# フィルター処理を終え、次の処理へ引き継ぐ。
	 * 
	 * @see CertifiResponse
	 * @see AccountDetails
	 ******************************************************************************************
	 */
  @Override
  public void doFilter(ServletRequest request, 
                       ServletResponse response, 
                       FilterChain chain) throws IOException, ServletException {

    String token = extractToken((HttpServletRequest)request);

    if(token == null){
      chain.doFilter(request, response);
      return;
    }

    HttpServletResponse http_response = (HttpServletResponse)response;
    
    try{
      UserDetails user_details = verifyToken(token);

      String username = user_details.getUsername();
      String role_list = user_details.getAuthorities().stream()
                                                      .map(s -> s.getAuthority())
                                                      .collect(Collectors.joining(","));
      http_response.setHeader("X_USERNAME", username);
      http_response.setHeader("X_ROLE_LIST", role_list);

    }catch(JWTVerificationException | UsernameNotFoundException e){

      SecurityContextHolder.clearContext();
      
      http_response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      http_response.setContentType(MediaType.APPLICATION_JSON_VALUE);

      String json_body = mapper.writeValueAsString(new CertifiResponse("Unauthorized", "token missing"));
      http_response.getOutputStream().println(json_body);

    }catch (Exception e) {
      throw new IOException("Error location [AccountTokenFilter:doFilter]" + "\n" + e);
    }
    
    chain.doFilter(request, response);
  }








	/** 
	 ******************************************************************************************
	 * @brief ヘッダーからJWTトークンを抽出する。同時にトークンが存在するかを判別する。
	 * 
	 * @details
	 * - データが存在しない場合は、初回アクセスとみなされる為、Nullを返却する。
	 * - また、データがあったとしても頭文字が[Bearer]で始まらないとJWTトークンとは判別できないため
	 * トークンはないものとみなされる。
	 * 
	 * @param[in] request リクエストクラス
	 * 
	 * @par 大まかな処理の流れ
	 * -# 該当ヘッダーから値を取り出す。
	 * -# 値が存在しない、または存在しても頭文字が[Bearer]で始まらない場合は、トークンがないものと
	 * みなし、Nullを返却する。
	 * -# トークンがあれば、抽出した値を返却する。
	 ******************************************************************************************
	 */
  private String extractToken(HttpServletRequest request){

    String token = request.getHeader("X_AUTH_TOKEN");

    if(token == null || token.startsWith("Bearer ")){
      return null;
    }

    return token;
  }







  


	/** 
	 ******************************************************************************************
	 * @brief 抽出したトークンを検証し、検証が成功すれば認証コンテキストにトークンの情報を登録して
	 * 認証処理を完了する。
	 * 
	 * @details
	 * - JWTトークンが改ざんされていないかを確認する。
	 * - 改ざんがされていない場合は、JWTトークンの値を全面的に信用するとして、そのまま認証コンテキストに
   * 値の登録を行う。
	 * 
	 * @param[in] token 抽出したトークン
	 * 
	 * @par 大まかな処理の流れ
	 * -# トークンを検証し、改ざんがされていないかを確認する。
	 * -# 改ざんがされていた場合は、例外を出す。
	 * -# 検証に成功すれば、トークンから取り出した値を認証コンテキストに登録する。
	 * -# 認証コンテキストに登録した際の認証情報を返却する。
	 ******************************************************************************************
	 */
  private UserDetails verifyToken(String token) throws JWTVerificationException, Exception{

    try{
      AccountDetails user_details = jwt_token.decipher(token);

      UsernamePasswordAuthenticationToken auth_token = 
        new UsernamePasswordAuthenticationToken(user_details, null, user_details.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(auth_token);

      return user_details;

    } catch (JWTVerificationException e) {
      throw new JWTVerificationException("token missing");
    } catch (Exception e) {
      throw new Exception("Error location [AccountTokenFilter:verifyToken]" + "\n" + e);
    }
  }
}