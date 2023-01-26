/** 
 **************************************************************************************
 * @file ZipProcess_OutputZip_Test.java
 * 
 * @brief バイナリデータの圧縮ファイル作成出力機能のテストにおいて、共通で使用するメソッドを
 * 提供するインターフェースを格納したファイル。
 * 
 * @note ここと同名のパッケージが、パッケージ[SingleTests]に存在するが、こちらは依存する
 * クラスを、モックを使わずそのまま使ってテストした、結合テストである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.JoinTests.FileIO.ZipProcess_Test.TestInterface;

import java.io.IOException;

import com.springproject.dockerspring.FileIO.OriginalException.InputFileDifferException;








/** 
 **************************************************************************************
 * @brief バイナリデータの圧縮ファイル作成出力機能のテストにおいて、共通で使用するメソッドを
 * 提供するインターフェース
 * 
 * @note ここと同名のパッケージが、パッケージ[SingleTests]に存在するが、こちらは
 * 依存するクラスを、モックを使わずそのまま使ってテストした、結合テストである。
 **************************************************************************************
 */ 
public interface ZipProcess_OutputZip_Test{


  /** 
   ****************************************************************************************
   * @brief バイナリデータの圧縮ファイル作成出力処理において共通クラスをDIした集約クラス側の
   * メソッドの検査を行う。ファイルの同一性はハッシュ化して確認し、作成した一時ファイルは使用後必ず
   * 削除する。
   * 
   * @note 本来であれば、集約クラスのテストを行う前に共通クラス側を先にテストするべきだと思うが、
   * 集約クラス側の実装内容が共通クラス側のメソッドを呼び出しているだけの極めてシンプルな内容の為、
   * 集約クラス側だけのテストでも十分共通クラス側の動作保証ができると判断したので、このテストのみである。
   * 
   * @throw IOException
   * @throw InputFileDifferException
   ****************************************************************************************
   */ 
  void 圧縮ファイル出力機能の動作確認() throws IOException, InputFileDifferException;
}
