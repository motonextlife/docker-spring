/** 
 **************************************************************************************
 * @file Audio_Data_Database_Yaml.java
 * @brief 主に[音源データ情報]機能のテストにおいて、データベースが関連したテストの際に用いる、
 * データベースセットアップ用のデータのYMLファイルをマッピングするエンティティを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.AudioData.Database;

import java.util.Map;

import lombok.Data;






/** 
 **************************************************************************************
 * @brief 主に[音源データ情報]機能のテストにおいて、データベースが関連したテストの際に用いる、
 * データベースセットアップ用のデータのYMLファイルをマッピングするエンティティを格納したファイル。
 * 
 * @details 
 * - このエンティティが対応するYMLファイルの名称は[audio-data-database.yml]である。
 * - 定義しているフィールド変数に関しては、対応するYMLファイルのタグ名にリンクした名称である。
 * 
 * @par 使用アノテーション
 * - @Data
 **************************************************************************************
 */ 
@Data
public class Audio_Data_Database_Yaml {

  //! 音源番号
  private Map<String, String> sound_id;

  //! 音源データ名
  private Map<String, String> sound_name;

  //! 音源データハッシュ値
  private Map<String, String> audio_hash;

  //! 音源データファイル名
  private Map<String, String> audio_data;
}