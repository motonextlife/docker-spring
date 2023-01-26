/** 
 ******************************************************************************************
 * @file CertifiResponse.java
 * @brief アカウントの認証や、権限の認可の際にレスポンスとして返すエンティティのクラスを
 * 格納するファイル。
 ******************************************************************************************
 */
package com.springproject.dockerspring.Security.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;





/** 
 ******************************************************************************************
 * @brief アカウントの認証や、権限の認可の際にレスポンスとして返すエンティティのクラス
 * 
 * @details 
 * - このエンティティは、主にフロント側へのレスポンスとして、JSON形式に変換して使用する。
 * - レスポンスとして返却する内容は、主にステータスコードと、処理の結果を表すメッセージとなる。
 * 
 * @par 使用アノテーション
 * - @Data
 * - @NoArgsConstructor
 * - @AllArgsConstructor
 ******************************************************************************************
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertifiResponse{

  //! ステータスコード
  private String code;

  //! ステータスメッセージ
  private String message;
}