package com.springproject.dockerspring.repository.NormalRepo;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springproject.dockerspring.entity.NormalEntity.System_User;

import java.util.Optional;





/**************************************************************/
/*   [SysUserRepo]
/*   テーブル「System_User」へのデータをやり取りや、
/*   エンティティ内のデータの加工を行う。
/**************************************************************/
public interface SysUserRepo extends CrudRepository<System_User, String>{


  /**************************************************************/
  /*   [existRemainMember_id]
  /*   指定された団員番号が存在するか確認
  /*   （なければ「0」一つでもあれば「1」）を返す
  /**************************************************************/
  @Query("SELECT COUNT(*) FROM System_User WHERE member_id = :member_id")
  Integer existRemainMember_id(@Param("member_id") String member_id);


  /**************************************************************/
  /*   [existRemainAuth_id]
  /*   指定された権限番号が存在するか確認
  /*   （なければ「0」一つでもあれば「1」）を返す
  /**************************************************************/
  @Query("SELECT COUNT(*) FROM System_User WHERE auth_id = :auth_id")
  Integer existRemainAuth_id(@Param("auth_id") String auth_id);


  /**************************************************************/
  /*   [updateLock]
  /*   引数で指定した団員番号で、システム用アカウント
  /*   のログイン失敗回数とロックの有無を記録する。
  /**************************************************************/
  @Modifying
  @Query("UPDATE System_User SET fail_count = :fail_count, locking = :locking WHERE member_id = :member_id")
  void updateLock(@Param("member_id") String member_id, @Param("fail_count") Integer fail_count, @Param("locking") Boolean locking);


  /**************************************************************/
  /*   [sysSecurity]
  /*   指定されたユーザーネームに完全一致するデータ
  /*   を取得する。
  /**************************************************************/
  @Query("SELECT * FROM System_User WHERE username = :username LIMIT 1")
  Optional<System_User> sysSecurity(@Param("username") String username);


  /**************************************************************/
  /*   [makeSlot]
  /*   デフォルトメソッドの「save()」に対応するため、
  /*   新たに追加する団員番号の空のカラムを作成。
  /*   （NOTNULL部分は適当な値を入れる。）
  /**************************************************************/
  @Modifying
  @Query("INSERT INTO System_User (member_id, username, password, auth_id) VALUES (:member_id, 'blank', 'blank', 1)")
  void makeSlot(@Param("member_id") String member_id);
}