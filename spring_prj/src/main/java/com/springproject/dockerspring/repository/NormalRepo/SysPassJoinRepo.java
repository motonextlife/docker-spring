package com.springproject.dockerspring.repository.NormalRepo;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springproject.dockerspring.entity.NormalEntity.SysPass_Join;
import com.springproject.dockerspring.repository.NormalRepo.FormEnum.FormSysPassJoin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;





/**************************************************************/
/*   [SysPassJoinRepo]
/*   テーブル「Member_Info」と「System_User」の結合テーブルへの
/*   データをやり取りや、エンティティ内のデータの加工を行う。
/**************************************************************/
public interface SysPassJoinRepo extends CrudRepository<SysPass_Join, String>{


  /***************************************************************/
  /*   [findAllJudge]
  /*   引数で渡された検索ジャンルと検索ワードに従い、検索処理に
  /*   使用するクエリメソッドを選定する。
  /*   また選定の際に、検索結果の並び順（昇順or降順）も指定する。
  /***************************************************************/
  public default Iterable<SysPass_Join> findAllJudge(String word, String subject, Boolean order){


    /**********************************************************************/
    /* 列挙型「FormSysPassJoin」を用いて、引数の検索ジャンルが列挙型内に
    /* 定義されているものか判定。
    /* 無ければ、不正な値が渡されたと判断し、空の検索結果を返す。
    /**********************************************************************/
    Optional<FormSysPassJoin> name = Arrays.stream(FormSysPassJoin.values())
                                                .parallel()
                                                .filter(s -> s.name().equals(subject))
                                                .findFirst();

    if(name.isEmpty()){
      return new ArrayList<SysPass_Join>();
    }


    /**********************************************************************/
    /* 検索ジャンルを元に分岐を行い、該当する検索メソッドを実行する。
    /* なお、分岐先では並び順指定に伴ってさらに分岐を行う。
    /**********************************************************************/
    Iterable<SysPass_Join> list = null;
    switch(name.get()){
      case All:
        list = order ? findAllOriginal() : findAllOriginalDESC();
        break;
      case Member_Id:
        list = order ? findAllByMember_id(word) : findAllByMember_idDESC(word);
        break;
      case Name:
        list = order ? findAllByName(word) : findAllByNameDESC(word);
        break;
      case Username:
        list = order ? findAllByUsername(word) : findAllByUsernameDESC(word);
        break;
      case Auth_Id:
        list = order ? findAllByAuth_id(word) : findAllByAuth_idDESC(word);
        break;
      case Locking:
        list = order ? findAllByLocking() : findAllByLockingDESC();
        break;
    }
    
    return list;
  }


  /************************************************************/
  /*   [findByMember_id]
  /*   指定された団員番号に該当する情報を個別で取得。
  /************************************************************/
  @Query("SELECT a.member_id AS member_id, a.name AS name, "
  + "b.username AS username, b.auth_id AS auth_id, b.fail_count AS fail_count, b.locking AS locking "
  + "FROM Member_Info a INNER JOIN System_User b ON a.member_id = b.member_id WHERE a.member_id = :member_id LIMIT 1")
  Optional<SysPass_Join> findByMember_id(@Param("member_id") String member_id);


  /**************************************************************/
  /*   [findAllOriginal]
  /*   [findAllOriginalDESC]
  /*   全データを団員番号順ですべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /**************************************************************/
  @Query("SELECT a.member_id AS member_id, a.name AS name, "
  + "b.username AS username, b.auth_id AS auth_id, b.fail_count AS fail_count, b.locking AS locking "
  + "FROM Member_Info a INNER JOIN System_User b ON a.member_id = b.member_id ORDER BY a.member_id ASC")
  Iterable<SysPass_Join> findAllOriginal();

  @Query("SELECT a.member_id AS member_id, a.name AS name, "
  + "b.username AS username, b.auth_id AS auth_id, b.fail_count AS fail_count, b.locking AS locking "
  + "FROM Member_Info a INNER JOIN System_User b ON a.member_id = b.member_id ORDER BY a.member_id DESC")
  Iterable<SysPass_Join> findAllOriginalDESC();


  /*************************************************************************/
  /*   [findAllByMember_id]
  /*   [findAllByMember_idDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「団員番号順」である。
  /*************************************************************************/
  @Query("SELECT a.member_id AS member_id, a.name AS name, "
  + "b.username AS username, b.auth_id AS auth_id, b.fail_count AS fail_count, b.locking AS locking "
  + "FROM Member_Info a INNER JOIN System_User b ON a.member_id = b.member_id " 
  + "WHERE a.member_id LIKE CONCAT('%', :word, '%') ORDER BY a.member_id ASC")
  Iterable<SysPass_Join> findAllByMember_id(@Param("word") String word);

  @Query("SELECT a.member_id AS member_id, a.name AS name, "
  + "b.username AS username, b.auth_id AS auth_id, b.fail_count AS fail_count, b.locking AS locking "
  + "FROM Member_Info a INNER JOIN System_User b ON a.member_id = b.member_id " 
  + "WHERE a.member_id LIKE CONCAT('%', :word, '%') ORDER BY a.member_id DESC")
  Iterable<SysPass_Join> findAllByMember_idDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByName]
  /*   [findAllByNameDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「団員名五十音順」である。
  /*************************************************************************/
  @Query("SELECT a.member_id AS member_id, a.name AS name, "
  + "b.username AS username, b.auth_id AS auth_id, b.fail_count AS fail_count, b.locking AS locking "
  + "FROM Member_Info a INNER JOIN System_User b ON a.member_id = b.member_id " 
  + "WHERE a.name LIKE CONCAT('%', :word, '%') ORDER BY a.name ASC")
  Iterable<SysPass_Join> findAllByName(@Param("word") String word);

  @Query("SELECT a.member_id AS member_id, a.name AS name, "
  + "b.username AS username, b.auth_id AS auth_id, b.fail_count AS fail_count, b.locking AS locking "
  + "FROM Member_Info a INNER JOIN System_User b ON a.member_id = b.member_id " 
  + "WHERE a.name LIKE CONCAT('%', :word, '%') ORDER BY a.name DESC")
  Iterable<SysPass_Join> findAllByNameDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByUsername]
  /*   [findAllByUsernameDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「ユーザーネーム五十音順」である。
  /*************************************************************************/
  @Query("SELECT a.member_id AS member_id, a.name AS name, "
  + "b.username AS username, b.auth_id AS auth_id, b.fail_count AS fail_count, b.locking AS locking "
  + "FROM Member_Info a INNER JOIN System_User b ON a.member_id = b.member_id " 
  + "WHERE b.username LIKE CONCAT('%', :word, '%') ORDER BY b.username ASC")
  Iterable<SysPass_Join> findAllByUsername(@Param("word") String word);

  @Query("SELECT a.member_id AS member_id, a.name AS name, "
  + "b.username AS username, b.auth_id AS auth_id, b.fail_count AS fail_count, b.locking AS locking "
  + "FROM Member_Info a INNER JOIN System_User b ON a.member_id = b.member_id " 
  + "WHERE b.username LIKE CONCAT('%', :word, '%') ORDER BY b.username DESC")
  Iterable<SysPass_Join> findAllByUsernameDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByAuth_id]
  /*   [findAllByAuth_idDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「権限番号順」である。
  /*************************************************************************/
  @Query("SELECT a.member_id AS member_id, a.name AS name, "
  + "b.username AS username, b.auth_id AS auth_id, b.fail_count AS fail_count, b.locking AS locking "
  + "FROM Member_Info a INNER JOIN System_User b ON a.member_id = b.member_id " 
  + "WHERE b.auth_id LIKE CONCAT('%', :word, '%') ORDER BY b.auth_id ASC")
  Iterable<SysPass_Join> findAllByAuth_id(@Param("word") String word);

  @Query("SELECT a.member_id AS member_id, a.name AS name, "
  + "b.username AS username, b.auth_id AS auth_id, b.fail_count AS fail_count, b.locking AS locking "
  + "FROM Member_Info a INNER JOIN System_User b ON a.member_id = b.member_id " 
  + "WHERE b.auth_id LIKE CONCAT('%', :word, '%') ORDER BY b.auth_id DESC")
  Iterable<SysPass_Join> findAllByAuth_idDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByLocking]
  /*   [findAllByLockingDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「団員番号順」である。
  /*************************************************************************/
  @Query("SELECT a.member_id AS member_id, a.name AS name, "
  + "b.username AS username, b.auth_id AS auth_id, b.fail_count AS fail_count, b.locking AS locking "
  + "FROM Member_Info a INNER JOIN System_User b ON a.member_id = b.member_id " 
  + "WHERE b.locking = true ORDER BY a.member_id ASC")
  Iterable<SysPass_Join> findAllByLocking();

  @Query("SELECT a.member_id AS member_id, a.name AS name, "
  + "b.username AS username, b.auth_id AS auth_id, b.fail_count AS fail_count, b.locking AS locking "
  + "FROM Member_Info a INNER JOIN System_User b ON a.member_id = b.member_id " 
  + "WHERE b.locking = true ORDER BY a.member_id DESC")
  Iterable<SysPass_Join> findAllByLockingDESC();
}