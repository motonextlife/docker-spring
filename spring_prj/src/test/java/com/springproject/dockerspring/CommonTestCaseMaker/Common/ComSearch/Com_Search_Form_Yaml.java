/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.Common.ComSearch
 * 
 * @brief [共通のフォームバリデーション]に関するテストケース生成処理のうち、[共通の通常データ検索]
 * 関連のテストケースを生成する処理を格納したパッケージ
 * 
 * @details
 * - このパッケージには、テストケースを記述したYAMLファイルとマッピングするエンティティや、
 * 取り込んだテストケースを呼び出し元のテストクラスに提供する機能を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.Common.ComSearch;





/** 
 **************************************************************************************
 * @file Com_Search_Form_Yaml.java
 * @brief 主に[共通の通常情報検索]機能のテストにおいて、フォームバリデーションテストの際に用いる
 * テストケースをマッピングするクラスを格納したファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.util.Map;

import lombok.Data;







/** 
 **************************************************************************************
 * @brief 主に[共通の通常情報検索]機能のテストにおいて、フォームバリデーションテストの際に用いる
 * テストケースをマッピングするクラス。
 * 
 * @details 
 * - このエンティティが対応するYMLファイルの名称は[com-search-form.yml]である。
 * - 定義しているフィールド変数に関しては、対応するYMLファイルのタグ名にリンクした名称である。
 * - YMLファイルの中には、様々なデータ型のデータが存在するが、読取後に対応する型に変換するため、
 * 一度文字列型でマッピングするものとする。
 * 
 * @par 使用アノテーション
 * - @Data
 **************************************************************************************
 */ 
@Data
public class Com_Search_Form_Yaml {

  //! 検索ワード
  private Map<String, String> word;

  //! 検索種別
  private Map<String, String> subject;
}