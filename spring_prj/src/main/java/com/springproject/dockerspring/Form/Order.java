/** 
 **************************************************************************************
 * @file Order.java
 * @brief フォームバリデーションの実行順番の定義を行うためのインターフェースを複数格納する
 * クラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form;

import javax.validation.GroupSequence;








/** 
 **************************************************************************************
 * @brief フォームバリデーションの実行順番の定義を行うためのインターフェースを複数格納する
 * クラス
 * 
 * @details 
 * - このクラスの内部には、内部インターフェースとして5つの順番定義の為のインターフェースが存在する。
 * - 順番定義のインターフェースをアノテーションに登録し、実行順番を定義するシーケンス用の
 * インターフェースが存在する。
 * - フォームバリデーションの実行順番を厳密に決める理由としては、バリデーションの内容によっては
 * 前の順番のバリデーションが終わってない状態で次のバリデーションに行くと、不正な値を渡してしまい
 * エラーになる恐れがあるためである。
 **************************************************************************************
 */ 
public class Order {

  //! 1番目に実行
  public interface Order_1{}

  //! 2番目に実行
  public interface Order_2{}

  //! 3番目に実行
  public interface Order_3{}

  //! 4番目に実行
  public interface Order_4{}

  //! 5番目に実行
  public interface Order_5{}


  /** 
   **********************************************************************************************
   * @brief 実行順番を定義する。順番としては、インターフェースに付加されている番号の順番になる。
	 * 
	 * @par 使用アノテーション
	 * - @GroupSequence({Order_1.class, Order_2.class, Order_3.class, Order_4.class, Order_5.class})
   **********************************************************************************************
   */
  @GroupSequence({Order_1.class, Order_2.class, Order_3.class, Order_4.class, Order_5.class})
  public interface Sequence{}
}
