/** 
 **************************************************************************************
 * @file CsvProcess_ExtractCsv_Test.java
 * 
 * @brief CSVファイルの入力データ抽出機能のテストにおいて、共通で使用するメソッドを提供する
 * インターフェースを格納するファイル。
 * 
 * @note ここと同名のパッケージが、パッケージ[SingleTests]に存在するが、こちらは依存する
 * クラスを、モックを使わずそのまま使ってテストした、結合テストである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.JoinTests.FileIO.CsvProcess_Test.TestInterface;

import com.springproject.dockerspring.FileIO.OriginalException.InputFileDifferException;

import java.io.IOException;








/** 
 **************************************************************************************
 * @brief CSVファイルの入力データ抽出機能のテストにおいて、共通で使用するメソッドを提供する
 * インターフェース
 * 
 * @note ここと同名のパッケージが、パッケージ[SingleTests]に存在するが、こちらは
 * 依存するクラスを、モックを使わずそのまま使ってテストした、結合テストである。
 **************************************************************************************
 */ 
public interface CsvProcess_ExtractCsv_Test{


  /** 
   ****************************************************************************************
   * @brief CSVファイル入力データ抽出処理において共通の部分を提供する共通クラスのメソッドの検査を
   * 行う。このテストは、抽出が成功するパターンのテストである。
   * 
   * @throw IOException
   * @throw InputFileDifferException
   ****************************************************************************************
   */ 
  void 共通クラス側のCSV入力データ抽出機能確認の抽出成功パターン() throws IOException, InputFileDifferException;


  /** 
   ****************************************************************************************
   * @brief CSVファイルテンプレート出力処理において共通クラスをDIした集約クラス側のメソッドの検査を
   * 行う。このテストは、抽出が成功するパターンのテストである。
   * 
   * @throw IOException
   * @throw InputFileDifferException
   ****************************************************************************************
   */ 
  void 集約クラス側のCSV入力データ抽出機能確認の抽出成功パターン() throws IOException, InputFileDifferException;


  /** 
   ****************************************************************************************
   * @brief CSVファイル入力データ抽出処理において共通の部分を提供する共通クラスのメソッドの検査を
   * 行う。このテストは、抽出が失敗するパターンのテストである。
   * 
   * @throw IOException
   * @throw InputFileDifferException
   ****************************************************************************************
   */ 
  void 共通クラス側のCSV入力データ抽出機能確認の抽出失敗パターン() throws IOException, InputFileDifferException;


  /** 
   ****************************************************************************************
   * @brief CSVファイルテンプレート出力処理において共通クラスをDIした集約クラス側のメソッドの検査を
   * 行う。このテストは、抽出が失敗するパターンのテストである。
   * 
   * @throw IOException
   * @throw InputFileDifferException
   ****************************************************************************************
   */ 
  void 集約クラス側のCSV入力データ抽出機能確認の抽出失敗パターン() throws IOException, InputFileDifferException;
}
