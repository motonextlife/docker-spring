/** 
 **************************************************************************************
 * @file Sound_Source_Database_Yaml.java
 * @brief 主に[音源管理]機能のテストにおいて、データベースが関連したテストの際に用いる、
 * データベースセットアップ用のデータのYMLファイルをマッピングするエンティティを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.SoundSource.Database;

import java.util.Date;
import java.util.Map;

import lombok.Data;






/** 
 **************************************************************************************
 * @brief 主に[音源管理]機能のテストにおいて、データベースが関連したテストの際に用いる、
 * データベースセットアップ用のデータのYMLファイルをマッピングするエンティティを格納したファイル。
 * 
 * @details 
 * - このエンティティが対応するYMLファイルの名称は[sound-source-database.yml]である。
 * - 定義しているフィールド変数に関しては、対応するYMLファイルのタグ名にリンクした名称である。
 * 
 * @par 使用アノテーション
 * - @Data
 **************************************************************************************
 */ 
@Data
public class Sound_Source_Database_Yaml {

  //! 楽譜番号
  private Map<String, String> sound_id;

  //! 登録日
  private Map<String, Date> upload_date;

  //! 曲名
  private Map<String, String> song_title;

  //! 作曲者
  private Map<String, String> composer;

  //! 演奏者
  private Map<String, String> performer;

  //! 出版社
  private Map<String, String> publisher;

  //! その他コメント
  private Map<String, String> other_comment;
}