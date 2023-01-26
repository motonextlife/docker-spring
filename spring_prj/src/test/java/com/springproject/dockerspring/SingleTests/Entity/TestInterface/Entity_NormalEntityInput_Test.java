/** 
 **************************************************************************************
 * @file Entity_NormalEntityInput_Test.java
 * 
 * @brief テスト対象履歴用エンティティに対応する通常エンティティを入力した際のテストにおいて、
 * 共通で使用するメソッドを提供するインターフェースを格納するファイル。
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、こちらは依存する
 * クラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Entity.TestInterface;

import java.io.IOException;
import java.text.ParseException;






/** 
 **************************************************************************************
 * @brief テスト対象履歴用エンティティに対応する通常エンティティを入力した際のテストにおいて、
 * 共通で使用するメソッドを提供するインターフェースを格納するファイル。
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、
 * こちらは依存するクラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
public interface Entity_NormalEntityInput_Test {


  /** 
   ****************************************************************************************
   * @brief 入力した通常エンティティからデータを取り出して履歴用エンティティに格納したデータが、
   * あらかじめ用意しておいたテストケースと同一であることを確認する。
   * 
   * @throw ParseException
   * @throw IOException
   ****************************************************************************************
   */ 
  void 入力した通常エンティティデータの同一性確認() throws ParseException, IOException;


  /** 
   ****************************************************************************************
   * @brief 入力したデータに初期化対象文字が含まれていた場合、入力後に取り出した該当データが
   * Nullに初期化されていることを確認する。
   * 
   * @details 初期化対象文字は以下の物である。
   * - 空文字("")
   * - 半角空白(" ")
   * - 全角空白("　")
   * - タブ空白("\t")
   * - 改行空白("\n")
   ****************************************************************************************
   */ 
  void 通常エンティティの初期化対象文字のNull初期化確認(String init_str);


  /** 
   ****************************************************************************************
   * @brief 入力したデータに[Null]が含まれていた場合でも、処理中に[NullPointerException]が発生して
   * 処理が止まらないことを確認する。
   ****************************************************************************************
   */ 
  void 通常エンティティのNull入力時のNullPointerException未発生確認();


  /** 
   ****************************************************************************************
   * @brief 入力の際に履歴種別を判定する列挙型を同時に保存するが、保存後のデータに列挙型に対応する
   * 文字列が間違いなく入っていることを確認する。
   ****************************************************************************************
   */ 
  void 登録履歴種別の同一性確認();
}
