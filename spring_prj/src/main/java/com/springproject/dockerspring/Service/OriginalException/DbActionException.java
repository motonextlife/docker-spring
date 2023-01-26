/** 
 **************************************************************************************
 * @file DbActionException.java
 * @brief データベース上に、SpringDataJDBCの機能を用いてデータの保存を行う時にエラーが発生
 * した際の例外を格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Service.OriginalException;







/** 
 **************************************************************************************
 * @brief データベース上に、SpringDataJDBCの機能を用いてデータの保存を行う時にエラーが発生
 * した際の例外
 * 
 * @details 
 * - この例外を発する状況としては、SpringDataJDBCの[save]メソッドを使用した場合である。
 * このメソッドでのSQLエラーは全て[DbActionExecutionException]という例外にまとめて投げられる。
 * ただこの例外は任意のメッセージを登録できない為、代わりの例外としてこのクラスの物を用いる。
 * - この例外は、サービスクラス以上の上流のクラスで用いる。
 **************************************************************************************
 */ 
public class DbActionException extends Exception{

  private static final long serialVersionUID = 1L;

  public DbActionException(String msg){
    super(msg);
  }
}