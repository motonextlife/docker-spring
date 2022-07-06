package com.springproject.dockerspring.service.OriginalException;



/***************************************************************************/
/*   [DbActionException]
/*   データベースへの更新処理においてエラーが発生した際に投げる例外。
/***************************************************************************/
public class DbActionException extends Exception{

  private static final long serialVersionUID = 1L;

  public DbActionException(String msg){
    super(msg);
  }
}