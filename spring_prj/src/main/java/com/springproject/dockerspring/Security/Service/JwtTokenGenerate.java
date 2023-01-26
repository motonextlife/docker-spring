/** 
 ******************************************************************************************
 * @file JwtTokenGenerate.java
 * @brief セキュリティに関する機能において、JWTトークンの作成や解析を行うクラスを格納する
 * ファイル。
 ******************************************************************************************
 */
package com.springproject.dockerspring.Security.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.springproject.dockerspring.CommonEnum.Environment_Config;
import com.springproject.dockerspring.CommonEnum.UtilEnum.File_Path_Enum;
import com.springproject.dockerspring.Security.Entity.AccountDetails;

import lombok.RequiredArgsConstructor;









/** 
 ******************************************************************************************
 * @brief セキュリティに関する機能において、JWTトークンの作成や解析を行うクラス
 * 
 * @details 
 * - このクラスでは、JWTトークンの作成や解析のほか、トークンの作成や解析に用いる公開鍵の
 * 生成を行う。
 *
 * @par 使用アノテーション
 * - @Component
 * - @RequiredArgsConstructor(onConstructor = @__(@Autowired))
 *
 * @see AccountDetails
 ******************************************************************************************
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtTokenGenerate{

  private final Environment_Config config;

  private final FileAttribute<Set<PosixFilePermission>> perms = PosixFilePermissions
                                                                .asFileAttribute(PosixFilePermissions
                                                                                .fromString(File_Path_Enum.Permission.getPath()));









	/** 
	 ******************************************************************************************
	 * @brief JWTトークンを、引数で渡された認証情報を用いて作成する。
	 * 
	 * @details
	 * - トークンの有効期限は、環境変数の値に従うが、かなり短時間に設定されている。これによって、
	 * ログアウト後のトークンのブラックリストを作成する必要がなく。ステートレス性が確保されるが、
	 * 定期的にフロント側からリクエストを行って、トークンの再発行を行わないといけない。
	 * - 署名を行う鍵に関しては、公開鍵方式を使う。これによって、共通鍵方式よりもブルートフォース
	 * 攻撃に強くすることが出来る。
	 * 
	 * @param[in] user_details トークンに使用する認証情報
	 * 
	 * @par 大まかな処理の流れ
	 * -# トークンに以下の情報を保存する。
	 *    - ユーザー名
	 *    - トークンの有効期間開始日（現在日時）
	 *    - トークンの有効期間終了日（現在日時 + 環境変数の時間）
	 *    - 権限情報リスト
	 * -# 公開鍵と秘密鍵を用意する。
	 * -# 保存した先ほどの情報を、公開鍵アルゴリズム[ECDSA256]で署名する。
	 * -# 生成したトークンの頭文字に[Bearer]と付加して、戻り値とする。
   *
	 * @see AccountDetails
	 ******************************************************************************************
	 */
  public String makeToken(AccountDetails user_details) throws Exception{

    try{
      String username = user_details.getUsername();
      Date issued_at = new Date();
      Date not_before = new Date(issued_at.getTime());
      Date expires_at = new Date(issued_at.getTime() + config.getExpiration_time());
      String[] role_list = user_details.getAuthorities().stream()
                                                        .map(s -> s.getAuthority())
                                                        .toArray(String[]::new);

      KeyFactory keygen = KeyFactory.getInstance("EC");
      ECPublicKey pub_key = (ECPublicKey)keygen.generatePublic(new X509EncodedKeySpec(tokenKeygen(false)));
      ECPrivateKey pri_key = (ECPrivateKey)keygen.generatePrivate(new PKCS8EncodedKeySpec(tokenKeygen(true)));

      String token = JWT.create().withIssuedAt(issued_at)
                                 .withNotBefore(not_before)
                                 .withExpiresAt(expires_at)
                                 .withSubject(username)
                                 .withArrayClaim("ROLE_LIST", role_list)
                                 .sign(Algorithm.ECDSA256(pub_key, pri_key));

      return "Bearer ".concat(token);      

    }catch(Exception e){
      throw new Exception("Error location [JwtTokenGenerate:makeToken]" + "\n" + e);
    }
  }






  


	/** 
	 ******************************************************************************************
	 * @brief 引数で渡されたトークンを解析し、抽出したデータを認証情報として作成する。
	 * 
	 * @details
	 * - トークンの解析の際は、公開鍵署名[ECDSA256]で必ず解析することを指定する。これによって、
	 * 署名方式の改ざんによる不正を防ぐ。
	 * 
	 * @param[in] token 解析対象のトークン
	 * 
	 * @par 大まかな処理の流れ
	 * -# トークンの頭文字に付加されている[Bearer]を取り除く。
	 * -# 公開鍵と秘密鍵を用意する。
	 * -# 厳密に解析用の鍵を指定して、トークンの解析を行う。改ざんがあった場合はエラーを投げる。
	 * -# 解析が成功すれば、抽出したデータから、ユーザー名と権限情報リストを取得する。
	 * -# 取得した2つの情報で、認証情報用の専用エンティティを作成し、戻り値とする。
   *
	 * @see AccountDetails
	 ******************************************************************************************
	 */
  public AccountDetails decipher(String token) throws JWTVerificationException, Exception{

    try{
      token = StringUtils.removeStart(token, "Bearer ");

      KeyFactory keygen = KeyFactory.getInstance("EC");
      ECPublicKey pub_key = (ECPublicKey)keygen.generatePublic(new X509EncodedKeySpec(tokenKeygen(false)));
      ECPrivateKey pri_key = (ECPrivateKey)keygen.generatePrivate(new PKCS8EncodedKeySpec(tokenKeygen(true)));

      DecodedJWT decode = JWT.require(Algorithm.ECDSA256(pub_key, pri_key))
                             .build()
                             .verify(token);
                             
      String username = decode.getSubject();
      String comma_role = decode.getClaim("ROLE_LIST").asList(String.class)
                                                      .stream()
                                                      .collect(Collectors.joining(","));
      List<GrantedAuthority> role_list = AuthorityUtils.commaSeparatedStringToAuthorityList(comma_role);

      return new AccountDetails(username, null, role_list, true);

    } catch (JWTVerificationException e) {
      throw new JWTVerificationException("token missing");
    } catch (Exception e) {
      throw new Exception("Error location [JwtTokenGenerate:decipher]" + "\n" + e);
    }
  }











	/** 
	 ******************************************************************************************
	 * @brief トークンの解析や作成に必要な鍵のペアを作成する。
	 * 
	 * @details
	 * - 作成する鍵の方式は公開鍵方式とする。鍵の種類は[EC/SHA1PRNG/256バイト]とする。
	 * - 鍵が既にファイル内に存在する場合はそちらを用いて、ない場合は新規に作成して用いる。
	 * 
	 * @param[in] private_key 「True」で作成した秘密鍵、「False」で作成した公開鍵を取得。
	 * 
	 * @par 大まかな処理の流れ
	 * -# 鍵のペアを作成するファイルインスタンスを作成する。なお、鍵を作成するディレクトリは
	 * 簡単にはアクセスできない場所にする。少なくともクラスパス上には作らない。
	 * -# 二つのファイルが既に存在すれば、そのファイルを読み取り戻り値とする。
	 * -# 二つのファイルの片方でもない場合は、すべての鍵ファイルを削除し、新たに鍵のペアを
	 * 作成する。
	 ******************************************************************************************
	 */
  private byte[] tokenKeygen(Boolean private_key) throws Exception{

    File pub_key = new File("src/main/keygen/pub_key.key");
    File pri_key = new File("src/main/keygen/pri_key.key");
    byte[] key_byte = null;

    try{
      if(pub_key.exists() && pri_key.exists()){

        //ファイル存在時
        if(private_key){
          key_byte = Files.readAllBytes(pri_key.toPath());
        }else{
          key_byte = Files.readAllBytes(pub_key.toPath());
        }

      }else{

        //ファイル未確認時
        Files.deleteIfExists(pub_key.toPath());
        Files.deleteIfExists(pri_key.toPath());
        Files.createFile(pub_key.toPath(), this.perms);
        Files.createFile(pri_key.toPath(), this.perms);

        KeyPairGenerator keygen = KeyPairGenerator.getInstance("EC");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        keygen.initialize(256, random);

        KeyPair pair = keygen.generateKeyPair();

        byte[] pub_key_byte = pair.getPublic().getEncoded();
        byte[] pri_key_byte = pair.getPrivate().getEncoded();

        Files.write(pub_key.toPath(), pub_key_byte);
        Files.write(pri_key.toPath(), pri_key_byte);

        key_byte = private_key ? pri_key_byte : pub_key_byte;
      }

    }catch(Exception e){
      throw new Exception("Error location [JwtTokenGenerate:tokenKeygen]" + "\n" + e);
    }

    return key_byte;
  }
}