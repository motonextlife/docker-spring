package com.springproject.dockerspring.repository.NormalRepo;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springproject.dockerspring.entity.NormalEntity.Inspect_Items;
import com.springproject.dockerspring.repository.NormalRepo.FormEnum.FormInspItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;






/**************************************************************/
/*   [InspItemsRepo]
/*   テーブル「Inspect_Items」へのデータをやり取りや、
/*   エンティティ内のデータの加工を行う。
/**************************************************************/
public interface InspItemsRepo extends CrudRepository<Inspect_Items, Integer>{


  /***************************************************************/
  /*   [findAllJudge]
  /*   引数で渡された検索ジャンルと検索ワードに従い、検索処理に
  /*   使用するクエリメソッドを選定する。
  /*   また選定の際に、検索結果の並び順（昇順or降順）も指定する。
  /***************************************************************/
  public default Iterable<Inspect_Items> findAllJudge(String word, String subject, Boolean order){


    /**********************************************************************/
    /* 列挙型「FormInspItems」を用いて、引数の検索ジャンルが列挙型内に
    /* 定義されているものか判定。
    /* 無ければ、不正な値が渡されたと判断し、空の検索結果を返す。
    /**********************************************************************/
    Optional<FormInspItems> name = Arrays.stream(FormInspItems.values())
                                              .parallel()
                                              .filter(s -> s.name().equals(subject))
                                              .findFirst();

    if(name.isEmpty()){
      return new ArrayList<Inspect_Items>();
    }


    /**********************************************************************/
    /* 検索ジャンルを元に分岐を行い、該当する検索メソッドを実行する。
    /* なお、分岐先では並び順指定に伴ってさらに分岐を行う。
    /**********************************************************************/
    Iterable<Inspect_Items> list = null;
    switch(name.get()){
      case All:
        list = order ? findAllOriginal() : findAllOriginalDESC();
        break;
      case Inspsheet_Id:
        list = order ? findAllByInspsheet_id(word) : findAllByInspsheet_idDESC(word);
        break;
      case Inspsheet_Name:
        list = order ? findAllByInspsheet_name(word) : findAllByInspsheet_nameDESC(word);
        break;
      case Inspsheet_Date:
        list = order ? findAllByInspsheet_date(word) : findAllByInspsheet_dateDESC(word);
        break;
      case Inspsheet_Contents:
        list = order ? findAllByInspsheet_contents(word) : findAllByInspsheet_contentsDESC(word);
        break;
      case Inspsheet_Itemkinds:
        list = order ? findAllByInspsheet_itemkinds(word) : findAllByInspsheet_itemkindsDESC(word);
        break;
      case Inspsheet_Itemunit:
        list = order ? findAllByInspsheet_itemunit(word) : findAllByInspsheet_itemunitDESC(word);
        break;
    }
    
    return list;
  }

  
  /************************************************************/
  /*   [findByInspsheet_id]
  /*   指定された点検シート番号に該当する情報を個別で取得。
  /************************************************************/
  @Query("SELECT * FROM Inspect_Items WHERE inspsheet_id = :inspsheet_id LIMIT 1")
  Optional<Inspect_Items> findByInspsheet_id(@Param("inspsheet_id") String inspsheet_id);


  /**************************************************************/
  /*   [findAllOriginal]
  /*   [findAllOriginalDESC]
  /*   全データを点検シート番号順ですべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /**************************************************************/
  @Query("SELECT * FROM Inspect_Items ORDER BY inspsheet_id ASC")
  Iterable<Inspect_Items> findAllOriginal();

  @Query("SELECT * FROM Inspect_Items ORDER BY inspsheet_id DESC")
  Iterable<Inspect_Items> findAllOriginalDESC();


  /*************************************************************************/
  /*   [findAllByInspsheet_id]
  /*   [findAllByInspsheet_idDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「点検シート番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Inspect_Items WHERE inspsheet_id LIKE CONCAT('%', :word, '%') ORDER BY inspsheet_id ASC")
  Iterable<Inspect_Items> findAllByInspsheet_id(@Param("word") String word);

  @Query("SELECT * FROM Inspect_Items WHERE inspsheet_id LIKE CONCAT('%', :word, '%') ORDER BY inspsheet_id DESC")
  Iterable<Inspect_Items> findAllByInspsheet_idDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByInspsheet_name]
  /*   [findAllByInspsheet_nameDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「点検シート名五十音順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Inspect_Items WHERE inspsheet_name LIKE CONCAT('%', :word, '%') ORDER BY inspsheet_name ASC")
  Iterable<Inspect_Items> findAllByInspsheet_name(@Param("word") String word);

  @Query("SELECT * FROM Inspect_Items WHERE inspsheet_name LIKE CONCAT('%', :word, '%') ORDER BY inspsheet_name DESC")
  Iterable<Inspect_Items> findAllByInspsheet_nameDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByInspsheet_date]
  /*   [findAllByInspsheet_dateDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「点検シート作成日順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Inspect_Items WHERE inspsheet_date = STR_TO_DATE(:word, '%Y-%m-%d') ORDER BY inspsheet_date ASC")
  Iterable<Inspect_Items> findAllByInspsheet_date(@Param("word") String word);

  @Query("SELECT * FROM Inspect_Items WHERE inspsheet_date = STR_TO_DATE(:word, '%Y-%m-%d') ORDER BY inspsheet_date DESC")
  Iterable<Inspect_Items> findAllByInspsheet_dateDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByInspsheet_contents]
  /*   [findAllByInspsheet_contentsDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「点検シート番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Inspect_Items WHERE" 
  + " CONCAT(insp_item_contents_1 ,insp_item_contents_2 ,insp_item_contents_3 ,insp_item_contents_4 ,insp_item_contents_5 ,"
          + "insp_item_contents_6 ,insp_item_contents_7 ,insp_item_contents_8 ,insp_item_contents_9 ,insp_item_contents_10)"
  + " LIKE CONCAT('%', :word, '%') ORDER BY inspsheet_id ASC")
  Iterable<Inspect_Items> findAllByInspsheet_contents(@Param("word") String word);

  @Query("SELECT * FROM Inspect_Items WHERE" 
  + " CONCAT(insp_item_contents_1 ,insp_item_contents_2 ,insp_item_contents_3 ,insp_item_contents_4 ,insp_item_contents_5 ,"
          + "insp_item_contents_6 ,insp_item_contents_7 ,insp_item_contents_8 ,insp_item_contents_9 ,insp_item_contents_10)"
  + " LIKE CONCAT('%', :word, '%') ORDER BY inspsheet_id DESC")
  Iterable<Inspect_Items> findAllByInspsheet_contentsDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByInspsheet_itemkinds]
  /*   [findAllByInspsheet_itemkindsDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「点検シート番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Inspect_Items WHERE" 
  + " CONCAT(insp_item_kinds_1 ,insp_item_kinds_2 ,insp_item_kinds_3 ,insp_item_kinds_4 ,insp_item_kinds_5 ,"
          + "insp_item_kinds_6 ,insp_item_kinds_7 ,insp_item_kinds_8 ,insp_item_kinds_9 ,insp_item_kinds_10)"
  + " LIKE CONCAT('%', :word, '%') ORDER BY inspsheet_id ASC")
  Iterable<Inspect_Items> findAllByInspsheet_itemkinds(@Param("word") String word);

  @Query("SELECT * FROM Inspect_Items WHERE" 
  + " CONCAT(insp_item_kinds_1 ,insp_item_kinds_2 ,insp_item_kinds_3 ,insp_item_kinds_4 ,insp_item_kinds_5 ,"
          + "insp_item_kinds_6 ,insp_item_kinds_7 ,insp_item_kinds_8 ,insp_item_kinds_9 ,insp_item_kinds_10)"
  + " LIKE CONCAT('%', :word, '%') ORDER BY inspsheet_id DESC")
  Iterable<Inspect_Items> findAllByInspsheet_itemkindsDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByInspsheet_itemunit]
  /*   [findAllByInspsheet_itemunitDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「点検シート番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Inspect_Items WHERE" 
  + " CONCAT(insp_item_unit_1 ,insp_item_unit_2 ,insp_item_unit_3 ,insp_item_unit_4 ,insp_item_unit_5 ,"
          + "insp_item_unit_6 ,insp_item_unit_7 ,insp_item_unit_8 ,insp_item_unit_9 ,insp_item_unit_10)"
  + " LIKE CONCAT('%', :word, '%') ORDER BY inspsheet_id ASC")
  Iterable<Inspect_Items> findAllByInspsheet_itemunit(@Param("word") String word);

  @Query("SELECT * FROM Inspect_Items WHERE" 
  + " CONCAT(insp_item_unit_1 ,insp_item_unit_2 ,insp_item_unit_3 ,insp_item_unit_4 ,insp_item_unit_5 ,"
          + "insp_item_unit_6 ,insp_item_unit_7 ,insp_item_unit_8 ,insp_item_unit_9 ,insp_item_unit_10)"
  + " LIKE CONCAT('%', :word, '%') ORDER BY inspsheet_id DESC")
  Iterable<Inspect_Items> findAllByInspsheet_itemunitDESC(@Param("word") String word);
}