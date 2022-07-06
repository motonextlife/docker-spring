package com.springproject.dockerspring.repository.NormalRepo;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springproject.dockerspring.entity.NormalEntity.Facility;
import com.springproject.dockerspring.repository.NormalRepo.FormEnum.FormFaci;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;






/**************************************************************/
/*   [FaciRepo]
/*   テーブル「Facility」へのデータをやり取りや、
/*   エンティティ内のデータの加工を行う。
/**************************************************************/
public interface FaciRepo extends CrudRepository<Facility, Integer>{


  /***************************************************************/
  /*   [findAllJudge]
  /*   引数で渡された検索ジャンルと検索ワードに従い、検索処理に
  /*   使用するクエリメソッドを選定する。
  /*   また選定の際に、検索結果の並び順（昇順or降順）も指定する。
  /***************************************************************/
  public default Iterable<Facility> findAllJudge(String word, String subject, Boolean order){


    /**********************************************************************/
    /* 列挙型「FormFaci」を用いて、引数の検索ジャンルが列挙型内に
    /* 定義されているものか判定。
    /* 無ければ、不正な値が渡されたと判断し、空の検索結果を返す。
    /**********************************************************************/
    Optional<FormFaci> name = Arrays.stream(FormFaci.values())
                                         .parallel()
                                         .filter(s -> s.name().equals(subject))
                                         .findFirst();

    if(name.isEmpty()){
      return new ArrayList<Facility>();
    }


    /**********************************************************************/
    /* 検索ジャンルを元に分岐を行い、該当する検索メソッドを実行する。
    /* なお、分岐先では並び順指定に伴ってさらに分岐を行う。
    /**********************************************************************/
    Iterable<Facility> list = null;
    switch(name.get()){
      case All:
        list = order ? findAllOriginal() : findAllOriginalDESC();
        break;
      case Faci_Id:
        list = order ? findAllByFaci_id(word) : findAllByFaci_idDESC(word);
        break;
      case Faci_Name:
        list = order ? findAllByFaci_name(word) : findAllByFaci_nameDESC(word);
        break;
      case Chief_Admin:
        list = order ? findAllByChief_admin(word) : findAllByChief_adminDESC(word);
        break;
      case Resp_Person:
        list = order ? findAllByResp_person(word) : findAllByResp_personDESC(word);
        break;
      case Buy_Date:
        list = order ? findAllByBuy_date(word) : findAllByBuy_dateDESC(word);
        break;
      case Storage_Loc:
        list = order ? findAllByStorage_loc(word) : findAllByStorage_locDESC(word);
        break;
      case Buy_Price:
        list = order ? findAllByBuy_price(word) : findAllByBuy_priceDESC(word);
        break;
      case Disp_Date:
        list = order ? findAllByDisp_date(word) : findAllByDisp_dateDESC(word);
        break;
      case Comment:
        list = order ? findAllByComment(word) : findAllByCommentDESC(word);
        break;
    }
    
    return list;
  }


  /************************************************************/
  /*   [findByFaci_Id]
  /*   指定された設備番号に該当する情報を個別で取得。
  /************************************************************/
  @Query("SELECT * FROM Facility WHERE faci_id = :faci_id LIMIT 1")
  Optional<Facility> findByFaci_Id(@Param("faci_id") String faci_id);


  /**************************************************************/
  /*   [findAllOriginal]
  /*   [findAllOriginalDESC]
  /*   全データを設備番号順ですべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /**************************************************************/
  @Query("SELECT * FROM Facility ORDER BY faci_id ASC")
  Iterable<Facility> findAllOriginal();

  @Query("SELECT * FROM Facility ORDER BY faci_id DESC")
  Iterable<Facility> findAllOriginalDESC();


  /**************************************************************/
  /*   [existRemainMember_id]
  /*   指定された団員番号が存在するか確認
  /*   （なければ「0」一つでもあれば「1」）を返す
  /**************************************************************/
  @Query("SELECT COUNT(*) FROM Facility WHERE CONCAT(chief_admin, resp_person) = :member_id")
  Integer existRemainMember_id(@Param("member_id") String member_id);


  /*************************************************************************/
  /*   [findAllByFaci_id]
  /*   [findAllByFaci_idDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「設備番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Facility WHERE faci_id LIKE CONCAT('%', :word, '%') ORDER BY faci_id ASC")
  Iterable<Facility> findAllByFaci_id(@Param("word") String word);

  @Query("SELECT * FROM Facility WHERE faci_id LIKE CONCAT('%', :word, '%') ORDER BY faci_id DESC")
  Iterable<Facility> findAllByFaci_idDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByFaci_name]
  /*   [findAllByFaci_nameDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「設備名五十音順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Facility WHERE faci_name LIKE CONCAT('%', :word, '%') ORDER BY faci_name ASC")
  Iterable<Facility> findAllByFaci_name(@Param("word") String word);

  @Query("SELECT * FROM Facility WHERE faci_name LIKE CONCAT('%', :word, '%') ORDER BY faci_name DESC")
  Iterable<Facility> findAllByFaci_nameDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByChief_admin]
  /*   [findAllByChief_adminDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「管理責任者団員番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Facility WHERE chief_admin LIKE CONCAT('%', :word, '%') ORDER BY chief_admin ASC")
  Iterable<Facility> findAllByChief_admin(@Param("word") String word);

  @Query("SELECT * FROM Facility WHERE chief_admin LIKE CONCAT('%', :word, '%') ORDER BY chief_admin DESC")
  Iterable<Facility> findAllByChief_adminDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByResp_person]
  /*   [findAllByResp_personDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「管理担当者団員番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Facility WHERE resp_person LIKE CONCAT('%', :word, '%') ORDER BY resp_person ASC")
  Iterable<Facility> findAllByResp_person(@Param("word") String word);

  @Query("SELECT * FROM Facility WHERE resp_person LIKE CONCAT('%', :word, '%') ORDER BY resp_person DESC")
  Iterable<Facility> findAllByResp_personDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByBuy_date]
  /*   [findAllByBuy_dateDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「購入日順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Facility WHERE buy_date = STR_TO_DATE(:word, '%Y-%m-%d') ORDER BY buy_date ASC")
  Iterable<Facility> findAllByBuy_date(@Param("word") String word);

  @Query("SELECT * FROM Facility WHERE buy_date = STR_TO_DATE(:word, '%Y-%m-%d') ORDER BY buy_date DESC")
  Iterable<Facility> findAllByBuy_dateDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByStorage_loc]
  /*   [findAllByStorage_locDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「保管場所五十音順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Facility WHERE storage_loc LIKE CONCAT('%', :word, '%') ORDER BY storage_loc ASC")
  Iterable<Facility> findAllByStorage_loc(@Param("word") String word);

  @Query("SELECT * FROM Facility WHERE storage_loc LIKE CONCAT('%', :word, '%') ORDER BY storage_loc DESC")
  Iterable<Facility> findAllByStorage_locDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByBuy_price]
  /*   [findAllByBuy_priceDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「購入金額順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Facility WHERE buy_price = CAST(:word AS SIGNED) ORDER BY buy_price ASC")
  Iterable<Facility> findAllByBuy_price(@Param("word") String word);

  @Query("SELECT * FROM Facility WHERE buy_price = CAST(:word AS SIGNED) ORDER BY buy_price DESC")
  Iterable<Facility> findAllByBuy_priceDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByDisp_date]
  /*   [findAllByDisp_dateDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「廃棄日順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Facility WHERE disp_date = STR_TO_DATE(:word, '%Y-%m-%d') ORDER BY disp_date ASC")
  Iterable<Facility> findAllByDisp_date(@Param("word") String word);

  @Query("SELECT * FROM Facility WHERE disp_date = STR_TO_DATE(:word, '%Y-%m-%d') ORDER BY disp_date DESC")
  Iterable<Facility> findAllByDisp_dateDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByComment]
  /*   [findAllByCommentDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「設備番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Facility WHERE other_comment LIKE CONCAT('%', :word, '%') ORDER BY faci_id ASC")
  Iterable<Facility> findAllByComment(@Param("word") String word);

  @Query("SELECT * FROM Facility WHERE other_comment LIKE CONCAT('%', :word, '%') ORDER BY faci_id DESC")
  Iterable<Facility> findAllByCommentDESC(@Param("word") String word);
}