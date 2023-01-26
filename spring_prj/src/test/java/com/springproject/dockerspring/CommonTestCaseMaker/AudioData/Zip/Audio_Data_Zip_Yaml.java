/** 
 **************************************************************************************
 * @file Audio_Data_Zip_Yaml.java
 * @brief 主に[音源データ情報]機能のテストにおいて、ZIPファイルの入出力のテストの際に用いる
 * テストケースをマッピングするクラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.AudioData.Zip;

import java.util.Map;

import lombok.Data;






/** 
 **************************************************************************************
 * @brief 主に[音源データ情報]機能のテストにおいて、ZIPファイルの入出力のテストの際に用いる
 * テストケースをマッピングするクラス。
 * 
 * @details 
 * - このエンティティが対応するYMLファイルの名称は[audio-data-zip.yml]である。
 * - 定義しているフィールド変数に関しては、対応するYMLファイルのタグ名にリンクした名称である。
 * 
 * @par 使用アノテーション
 * - @Data
 **************************************************************************************
 */ 
@Data
public class Audio_Data_Zip_Yaml {

  //! 音源データファイル名
  private Map<String, String> zip_file;
}