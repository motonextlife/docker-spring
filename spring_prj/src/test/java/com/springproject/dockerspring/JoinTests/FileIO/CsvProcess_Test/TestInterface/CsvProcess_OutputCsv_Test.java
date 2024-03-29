/** 
 **************************************************************************************
 * @file CsvProcess_OutputCsv_Test.java
 * 
 * @brief CSVファイルのデータベース保存データ出力機能のテストにおいて、共通で使用するメソッドを
 * 提供するインターフェースを格納したファイル。
 * 
 * @note ここと同名のパッケージが、パッケージ[SingleTests]に存在するが、こちらは依存する
 * クラスを、モックを使わずそのまま使ってテストした、結合テストである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.JoinTests.FileIO.CsvProcess_Test.TestInterface;

import java.io.IOException;
import java.text.ParseException;








/** 
 **************************************************************************************
 * @brief CSVファイルのデータベース保存データ出力機能のテストにおいて、共通で使用するメソッドを
 * 提供するインターフェース
 * 
 * @note ここと同名のパッケージが、パッケージ[SingleTests]に存在するが、こちらは
 * 依存するクラスを、モックを使わずそのまま使ってテストした、結合テストである。
 **************************************************************************************
 */ 
public interface CsvProcess_OutputCsv_Test{


  /** 
   ****************************************************************************************
   * @brief CSVファイルデータベース保存データ出力処理において共通の部分を提供する共通クラスの
   * メソッドの検査を行う。ファイルの同一性はハッシュ化して確認し、作成した一時ファイルは使用後必ず
   * 削除する。
   * 
   * @throw IOException
   ****************************************************************************************
   */ 
  void 共通クラス側のCSVデータベース保存データ出力機能の動作確認() throws IOException, ParseException;


  /** 
   ****************************************************************************************
   * @brief CSVファイルデータベース保存データ出力処理において共通クラスをDIした集約クラス側の
   * メソッドの検査を行う。ファイルの同一性はハッシュ化して確認し、作成した一時ファイルは使用後必ず
   * 削除する。
   * 
   * @throw IOException
   ****************************************************************************************
   */ 
  void 集約クラス側のCSVデータベース保存データ出力機能の動作確認() throws IOException, ParseException;
}
