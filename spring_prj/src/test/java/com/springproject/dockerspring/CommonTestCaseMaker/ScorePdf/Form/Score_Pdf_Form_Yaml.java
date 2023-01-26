/** 
 **************************************************************************************
 * @file Score_Pdf_Form_Yaml.java
 * @brief 主に[楽譜データ情報]機能のテストにおいて、フォームバリデーションテストの際に用いる
 * テストケースをマッピングするクラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.ScorePdf.Form;

import java.util.Map;

import lombok.Data;






/** 
 **************************************************************************************
 * @brief 主に[楽譜データ情報]機能のテストにおいて、フォームバリデーションテストの際に用いる
 * テストケースをマッピングするクラス。
 * 
 * @details 
 * - このエンティティが対応するYMLファイルの名称は[score-pdf-form.yml]である。
 * - 定義しているフィールド変数に関しては、対応するYMLファイルのタグ名にリンクした名称である。
 * - YMLファイルの中には、様々なデータ型のデータが存在するが、読取後に対応する型に変換するため、
 * 一度文字列型でマッピングするものとする。
 * 
 * @par 使用アノテーション
 * - @Data
 **************************************************************************************
 */ 
@Data
public class Score_Pdf_Form_Yaml {

  //! シリアルナンバー
  private Map<String, String> serial_num;

  //! 楽譜番号
  private Map<String, String> score_id;

  //! 楽譜データ名
  private Map<String, String> score_name;

  //! 楽譜データファイル
  private Map<String, String> pdf_data;
}