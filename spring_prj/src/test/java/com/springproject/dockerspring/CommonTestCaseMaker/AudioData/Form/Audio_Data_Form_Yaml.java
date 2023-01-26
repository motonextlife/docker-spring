/** 
 **************************************************************************************
 * @file Audio_Data_Form_Yaml.java
 * @brief 主に[音源データ情報]機能のテストにおいて、フォームバリデーションテストの際に用いる
 * テストケースをマッピングするクラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.AudioData.Form;

import java.util.Map;

import lombok.Data;






/** 
 **************************************************************************************
 * @brief 主に[音源データ情報]機能のテストにおいて、フォームバリデーションテストの際に用いる
 * テストケースをマッピングするクラス。
 * 
 * @details 
 * - このエンティティが対応するYMLファイルの名称は[audio-data-form.yml]である。
 * - 定義しているフィールド変数に関しては、対応するYMLファイルのタグ名にリンクした名称である。
 * - YMLファイルの中には、様々なデータ型のデータが存在するが、読取後に対応する型に変換するため、
 * 一度文字列型でマッピングするものとする。
 * 
 * @par 使用アノテーション
 * - @Data
 **************************************************************************************
 */ 
@Data
public class Audio_Data_Form_Yaml {

  //! シリアルナンバー
  private Map<String, String> serial_num;

  //! 音源番号
  private Map<String, String> sound_id;

  //! 音源データ名
  private Map<String, String> sound_name;

  //! 音源データファイル
  private Map<String, String> audio_data;
}