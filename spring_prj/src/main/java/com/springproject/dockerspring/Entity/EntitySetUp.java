/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.Entity
 * 
 * @brief このシステム上で扱うデータ全般を格納するエンティティクラスを格納するパッケージ
 * 
 * @details
 * - このパッケージでは、データベースから取得してきたデータを格納するエンティティクラスや、
 * エンティティクラスで用いるインターフェース、ユーティリティクラスを格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Entity;







/** 
 **************************************************************************************
 * @file EntitySetUp.java
 * @brief 全てのデータベース保存やデータのやり取りに使用するエンティティにおいて、共通で使用する
 * メソッドを提供するインターフェースを格納したファイルである。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.io.IOException;
import java.util.Map;





/** 
 **************************************************************************************
 * @brief 全てのデータベース保存やデータのやり取りに使用するエンティティにおいて、共通で使用する
 * メソッドを提供するインターフェースである。
 **************************************************************************************
 */ 
public interface EntitySetUp{


  /** 
   **********************************************************************************************
   * @brief エンティティ内に格納されているデータのうち、文字列型かつ空文字や空白のデータをNullに初期化する。
   * 
   * @details このメソッドは以下の理由で設けた。
   * - 空文字や空白のままデータベースに保存されると、他のテーブルとの参照制約に悪影響を及ぼすため。
   **********************************************************************************************
   */
  void stringSetNull();



  /** 
   **********************************************************************************************
   * @brief エンティティ内に格納されているデータを全て文字列型に変換し、マップリストへ格納して返却する。
   * 
   * @details
   * - 主に、フロント側の画面への出力の際に用いるデータを作成する目的として、このメソッドを使用する。
   * - 「Null」が入っているデータは、全て「空文字」に変換される。
   * 
   * @return 文字列に変換が終わり、キー名を項目の英語名としたマップリスト。
   * 
   * @throw IOException
   **********************************************************************************************
   */
  Map<String, String> makeMap() throws IOException;
}