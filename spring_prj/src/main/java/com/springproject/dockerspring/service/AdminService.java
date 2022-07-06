package com.springproject.dockerspring.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.springproject.dockerspring.commonenum.Usage_Authority_Enum;
import com.springproject.dockerspring.entity.NormalEntity.SysPass_Join;
import com.springproject.dockerspring.entity.NormalEntity.System_User;
import com.springproject.dockerspring.entity.NormalEntity.Usage_Authority;
import com.springproject.dockerspring.service.OriginalException.DbActionException;

/**************************************************************/
/*   [AdminService]
/*   主に「管理者」に関する処理の機能を提供する。
/**************************************************************/
public interface AdminService{

  /*******************************************/
  /*   [deleteHistory(1)]
  /*   指定された履歴テーブルの情報を、
  /*   期間を定めて全削除する。
  /*******************************************/
  Boolean deleteHistory(String table_name, Date start_datetime, Date end_datetime);


  /*******************************************/
  /*   [deleteHistory(2)]
  /*   全履歴テーブルの情報を、期間を定めて
  /*   全削除する。
  /*******************************************/
  void deleteHistory(Date start_datetime, Date end_datetime);


  /*******************************************/
  /*   [sysUserList]
  /*   検索ワードとジャンル、並び順を指定して、
  /*   システム利用者のリストを出力する。
  /*******************************************/
  Iterable<SysPass_Join> sysUserList(String word, String subject, Boolean order);


  /*******************************************/
  /*   [checkSysUser]
  /*   システム利用者の団員番号を指定して、
  /*   利用者情報を個別に取得する。
  /*******************************************/
  Optional<SysPass_Join> checkSysUser(String member_id);


  /*******************************************/
  /*   [existsSysUser]
  /*   対象のシステム利用者が存在するか
  /*   確認する。
  /*******************************************/
  Boolean existsSysUser(String member_id);


  /*******************************************/
  /*   [changeSysUser]
  /*   渡された利用者情報をデータベースに登録する。
  /*   なお、新規登録と更新で共用である。
  /*******************************************/
  System_User changeSysUser(System_User entity, Boolean new_data) throws DbActionException;


  /*******************************************/
  /*   [deleteSysUser]
  /*   対象のシステム利用者を削除する。
  /*******************************************/
  void deleteSysUser(String member_id) throws DbActionException;


  /*******************************************/
  /*   [authList]
  /*   検索ワードとジャンル、並び順を指定して、
  /*   登録権限のリストを出力する。
  /*******************************************/
  Iterable<Usage_Authority> authList(String word, String subject, Boolean order);


  /*******************************************/
  /*   [checkAuth]
  /*   権限番号を指定して登録権限情報
  /*   を個別に取得する。
  /*******************************************/
  Optional<Usage_Authority> checkAuth(String auth_id);


  /*******************************************/
  /*   [existsAuth]
  /*   対象の権限番号が存在するか確認する。
  /*******************************************/
  Boolean existsAuth(String auth_id);


  /*************************************************************/
  /*   [duplicateAuth]
  /*   引数で渡されたシリアルナンバーと権限番号を用いて、
  /*   権限番号に重複がないか確認する。
  /*************************************************************/
  Boolean duplicateAuth(Integer serial_num, String auth_id);


  /*******************************************/
  /*   [changeAuth]
  /*   渡された登録権限情報をデータベースに登録する。
  /*   なお、新規登録と更新で共用である。
  /*******************************************/
  Usage_Authority changeAuth(Usage_Authority entity) throws DbActionException;


  /*******************************************/
  /*   [deleteAuth]
  /*   対象の登録権限を削除する。
  /*******************************************/
  Boolean deleteAuth(Integer serial_num) throws DbActionException;


  /*******************************************/
  /*   [judgeUserAuth]
  /*   現在ログイン中のユーザーの情報を確認し、
  /*   使用しようとしている機能の権限があるか
  /*   判定する。
  /*******************************************/
  Boolean judgeUserAuth(Usage_Authority_Enum authname);


  /*************************************************************/
  /*   [parseString]
  /*   渡されたエンティティ内のデータを文字列に変換し、
  /*   マップデータに変換して返す。
  /*************************************************************/
  Map<String, String> parseStringS(SysPass_Join entity);
  Map<String, String> parseStringA(Usage_Authority entity);


  /*************************************************************/
  /*   [mapPacking]
  /*   渡されたエンティティリストのデータを全て文字列化して
  /*   リストとして返す。
  /*************************************************************/
  List<Map<String, String>> mapPackingS(Iterable<SysPass_Join> datalist);
  List<Map<String, String>> mapPackingA(Iterable<Usage_Authority> datalist);
}