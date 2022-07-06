 package com.springproject.dockerspring.repository.NormalRepo;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springproject.dockerspring.entity.NormalEntity.Equip_Inspect;
import com.springproject.dockerspring.repository.NormalRepo.FormEnum.FormEquipInsp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;






/**************************************************************/
/*   [EquipInspRepo]
/*   テーブル「Equip_Inspect」へのデータをやり取りや、
/*   エンティティ内のデータの加工を行う。
/**************************************************************/
public interface EquipInspRepo extends CrudRepository<Equip_Inspect, Integer>{


  /***************************************************************/
  /*   [findAllJudge]
  /*   引数で渡された検索ジャンルと検索ワードに従い、検索処理に
  /*   使用するクエリメソッドを選定する。
  /*   また選定の際に、検索結果の並び順（昇順or降順）も指定する。
  /***************************************************************/
  public default Iterable<Equip_Inspect> findAllJudge(String word, String subject, Boolean order){


    /**********************************************************************/
    /* 列挙型「FormEquipInsp」を用いて、引数の検索ジャンルが列挙型内に
    /* 定義されているものか判定。
    /* 無ければ、不正な値が渡されたと判断し、空の検索結果を返す。
    /**********************************************************************/
    Optional<FormEquipInsp> name = Arrays.stream(FormEquipInsp.values())
                                         .parallel()
                                         .filter(s -> s.name().equals(subject))
                                         .findFirst();

    if(name.isEmpty()){
      return new ArrayList<Equip_Inspect>();
    }


    /**********************************************************************/
    /* 検索ジャンルを元に分岐を行い、該当する検索メソッドを実行する。
    /* なお、分岐先では並び順指定に伴ってさらに分岐を行う。
    /**********************************************************************/
    Iterable<Equip_Inspect> list = null;
    switch(name.get()){
      case All:
        list = order ? findAllOriginal() : findAllOriginalDESC();
        break;
      case Inspect_Id:
        list = order ? findAllByInspect_id(word) : findAllByInspect_idDESC(word);
        break;
      case Faci_Id:
        list = order ? findAllByFaci_id(word) : findAllByFaci_idDESC(word);
        break;
      case Inspect_Name:
        list = order ? findAllByInspect_name(word) : findAllByInspect_nameDESC(word);
        break;
      case Inspect_Date:
        list = order ? findAllByInspect_date(word) : findAllByInspect_dateDESC(word);
        break;
      case Inspsheet_Id:
        list = order ? findAllByInspsheet_id(word) : findAllByInspsheet_idDESC(word);
        break;
      case Inspect_Contents:
        list = order ? findAllByInspect_contents(word) : findAllByInspect_contentsDESC(word);
        break;
      case Inspect_Rank:
        list = order ? findAllByInspect_rank(word) : findAllByInspect_rankDESC(word);
        break;
    }
    
    return list;
  }


  /****************************************************************************/
  /*   [findList]
  /*   引数で渡ってきた設備番号から、該当する点検情報をリストとして全取得。
  /****************************************************************************/
  @Query("SELECT * FROM Equip_Inspect WHERE faci_id = :faci_id")
  Iterable<Equip_Inspect> findList(@Param("faci_id") String faci_id);


  /************************************************************/
  /*   [findByInspect_id]
  /*   指定された点検番号に該当する情報を個別で取得。
  /************************************************************/
  @Query("SELECT * FROM Equip_Inspect WHERE inspect_id = :inspect_id LIMIT 1")
  Optional<Equip_Inspect> findByInspect_id(@Param("inspect_id") String inspect_id);


  /**************************************************************/
  /*   [findAllOriginal]
  /*   [findAllOriginalDESC]
  /*   全データを点検番号順ですべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /**************************************************************/
  @Query("SELECT * FROM Equip_Inspect ORDER BY inspect_id ASC")
  Iterable<Equip_Inspect> findAllOriginal();

  @Query("SELECT * FROM Equip_Inspect ORDER BY inspect_id DESC")
  Iterable<Equip_Inspect> findAllOriginalDESC();


  /**************************************************************/
  /*   [existRemainFaci_id]
  /*   指定された設備番号が存在するか確認
  /*   （なければ「0」一つでもあれば「1」）を返す
  /**************************************************************/
  @Query("SELECT COUNT(*) FROM Equip_Inspect WHERE faci_id = :faci_id")
  Integer existRemainFaci_id(@Param("faci_id") String faci_id);


  /**************************************************************/
  /*   [existRemainInspsheet_id]
  /*   指定された点検シート番号が存在するか確認
  /*   （なければ「0」一つでもあれば「1」）を返す
  /**************************************************************/
  @Query("SELECT COUNT(*) FROM Equip_Inspect WHERE inspsheet_id = :inspsheet_id")
  Integer existRemainInspsheet_id(@Param("inspsheet_id") String inspsheet_id);


  /*************************************************************************/
  /*   [findAllByInspect_id]
  /*   [findAllByInspect_idDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「点検番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Equip_Inspect WHERE inspect_id LIKE CONCAT('%', :word, '%') ORDER BY inspect_id ASC")
  Iterable<Equip_Inspect> findAllByInspect_id(@Param("word") String word);

  @Query("SELECT * FROM Equip_Inspect WHERE inspect_id LIKE CONCAT('%', :word, '%') ORDER BY inspect_id DESC")
  Iterable<Equip_Inspect> findAllByInspect_idDESC(@Param("word") String word);
  

  /*************************************************************************/
  /*   [findAllByFaci_id]
  /*   [findAllByFaci_idDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「設備番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Equip_Inspect WHERE faci_id LIKE CONCAT('%', :word, '%') ORDER BY faci_id ASC")
  Iterable<Equip_Inspect> findAllByFaci_id(@Param("word") String word);

  @Query("SELECT * FROM Equip_Inspect WHERE faci_id LIKE CONCAT('%', :word, '%') ORDER BY faci_id DESC")
  Iterable<Equip_Inspect> findAllByFaci_idDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByInspect_name]
  /*   [findAllByInspect_nameDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「点検名五十音順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Equip_Inspect WHERE inspect_name LIKE CONCAT('%', :word, '%') ORDER BY inspect_name ASC")
  Iterable<Equip_Inspect> findAllByInspect_name(@Param("word") String word);

  @Query("SELECT * FROM Equip_Inspect WHERE inspect_name LIKE CONCAT('%', :word, '%') ORDER BY inspect_name DESC")
  Iterable<Equip_Inspect> findAllByInspect_nameDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByInspect_date]
  /*   [findAllByInspect_dateDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「点検日順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Equip_Inspect WHERE inspect_date = STR_TO_DATE(:word, '%Y-%m-%d') ORDER BY inspect_date ASC")
  Iterable<Equip_Inspect> findAllByInspect_date(@Param("word") String word);

  @Query("SELECT * FROM Equip_Inspect WHERE inspect_date = STR_TO_DATE(:word, '%Y-%m-%d') ORDER BY inspect_date DESC")
  Iterable<Equip_Inspect> findAllByInspect_dateDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByInspsheet_id]
  /*   [findAllByInspsheet_idDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「点検シート番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Equip_Inspect WHERE inspsheet_id LIKE CONCAT('%', :word, '%') ORDER BY inspsheet_id ASC")
  Iterable<Equip_Inspect> findAllByInspsheet_id(@Param("word") String word);

  @Query("SELECT * FROM Equip_Inspect WHERE inspsheet_id LIKE CONCAT('%', :word, '%') ORDER BY inspsheet_id DESC")
  Iterable<Equip_Inspect> findAllByInspsheet_idDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByInspect_contents]
  /*   [findAllByInspect_contentsDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「点検番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Equip_Inspect WHERE" 
  + " CONCAT(insp_contents_1 ,insp_contents_2 ,insp_contents_3 ,insp_contents_4 ,insp_contents_5 ,"
          + "insp_contents_6 ,insp_contents_7 ,insp_contents_8 ,insp_contents_9 ,insp_contents_10)"
  + " LIKE CONCAT('%', :word, '%') ORDER BY inspect_id ASC")
  Iterable<Equip_Inspect> findAllByInspect_contents(@Param("word") String word);

  @Query("SELECT * FROM Equip_Inspect WHERE" 
  + " CONCAT(insp_contents_1 ,insp_contents_2 ,insp_contents_3 ,insp_contents_4 ,insp_contents_5 ,"
          + "insp_contents_6 ,insp_contents_7 ,insp_contents_8 ,insp_contents_9 ,insp_contents_10)"
  + " LIKE CONCAT('%', :word, '%') ORDER BY inspect_id DESC")
  Iterable<Equip_Inspect> findAllByInspect_contentsDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByInspect_rank]
  /*   [findAllByInspect_rankDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「点検番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Equip_Inspect WHERE" 
  + " CONCAT(insp_rank_1 ,insp_rank_2 ,insp_rank_3 ,insp_rank_4 ,insp_rank_5 ,"
          + "insp_rank_6 ,insp_rank_7 ,insp_rank_8 ,insp_rank_9 ,insp_rank_10)"
  + " LIKE CONCAT('%', :word, '%') ORDER BY inspect_id ASC")
  Iterable<Equip_Inspect> findAllByInspect_rank(@Param("word") String word);

  @Query("SELECT * FROM Equip_Inspect WHERE" 
  + " CONCAT(insp_rank_1 ,insp_rank_2 ,insp_rank_3 ,insp_rank_4 ,insp_rank_5 ,"
          + "insp_rank_6 ,insp_rank_7 ,insp_rank_8 ,insp_rank_9 ,insp_rank_10)"
  + " LIKE CONCAT('%', :word, '%') ORDER BY inspect_id DESC")
  Iterable<Equip_Inspect> findAllByInspect_rankDESC(@Param("word") String word);
}