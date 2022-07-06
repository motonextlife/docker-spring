package com.springproject.dockerspring.repository.NormalRepo;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springproject.dockerspring.entity.NormalEntity.Rec_Eval_Items;
import com.springproject.dockerspring.repository.NormalRepo.FormEnum.FormRecEvalItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;





/**************************************************************/
/*   [RecEvalItemsRepo]
/*   テーブル「Rec_Eval_Items」へのデータをやり取りや、
/*   エンティティ内のデータの加工を行う。
/**************************************************************/
public interface RecEvalItemsRepo extends CrudRepository<Rec_Eval_Items, Integer>{


  /***************************************************************/
  /*   [findAllJudge]
  /*   引数で渡された検索ジャンルと検索ワードに従い、検索処理に
  /*   使用するクエリメソッドを選定する。
  /*   また選定の際に、検索結果の並び順（昇順or降順）も指定する。
  /***************************************************************/
  public default Iterable<Rec_Eval_Items> findAllJudge(String word, String subject, Boolean order){


    /**********************************************************************/
    /* 列挙型「FormRecEvalItems」を用いて、引数の検索ジャンルが列挙型内に
    /* 定義されているものか判定。
    /* 無ければ、不正な値が渡されたと判断し、空の検索結果を返す。
    /**********************************************************************/
    Optional<FormRecEvalItems> name = Arrays.stream(FormRecEvalItems.values())
                                                 .parallel()
                                                 .filter(s -> s.name().equals(subject))
                                                 .findFirst();

    if(name.isEmpty()){
      return new ArrayList<Rec_Eval_Items>();
    }


    /**********************************************************************/
    /* 検索ジャンルを元に分岐を行い、該当する検索メソッドを実行する。
    /* なお、分岐先では並び順指定に伴ってさらに分岐を行う。
    /**********************************************************************/
    Iterable<Rec_Eval_Items> list = null;
    switch(name.get()){
      case All:
        list = order ? findAllOriginal() : findAllOriginalDESC();
        break;
      case Evalsheet_Id:
        list = order ? findAllByEvalsheet_id(word) : findAllByEvalsheet_idDESC(word);
        break;
      case Evalsheet_Name:
        list = order ? findAllByEvalsheet_name(word) : findAllByEvalsheet_nameDESC(word);
        break;
      case Evalsheet_Date:
        list = order ? findAllByEvalsheet_date(word) : findAllByEvalsheet_dateDESC(word);
        break;
      case Evalsheet_Kinds:
        list = order ? findAllByEvalsheet_kinds(word) : findAllByEvalsheet_kindsDESC(word);
        break;
      case Evalsheet_Contents:
        list = order ? findAllByEvalsheet_contents(word) : findAllByEvalsheet_contentsDESC(word);
        break;
      case Evalsheet_Itemkinds:
        list = order ? findAllByEvalsheet_itemkinds(word) : findAllByEvalsheet_itemkindsDESC(word);
        break;
    }
    
    return list;
  }


  /************************************************************/
  /*   [findByEvalsheet_id]
  /*   指定された採用考課シート番号に該当する情報を個別で取得。
  /************************************************************/
  @Query("SELECT * FROM Rec_Eval_Items WHERE evalsheet_id = :evalsheet_id LIMIT 1")
  Optional<Rec_Eval_Items> findByEvalsheet_id(@Param("evalsheet_id") String evalsheet_id);


  /**************************************************************/
  /*   [findAllOriginal]
  /*   [findAllOriginalDESC]
  /*   全データを採用考課シート順ですべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /**************************************************************/
  @Query("SELECT * FROM Rec_Eval_Items ORDER BY evalsheet_id ASC")
  Iterable<Rec_Eval_Items> findAllOriginal();

  @Query("SELECT * FROM Rec_Eval_Items ORDER BY evalsheet_id DESC")
  Iterable<Rec_Eval_Items> findAllOriginalDESC();


  /*************************************************************************/
  /*   [findAllByEvalsheet_id]
  /*   [findAllByEvalsheet_idDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「採用考課シート番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Rec_Eval_Items WHERE evalsheet_id LIKE CONCAT('%', :word, '%') ORDER BY evalsheet_id ASC")
  Iterable<Rec_Eval_Items> findAllByEvalsheet_id(@Param("word") String word);

  @Query("SELECT * FROM Rec_Eval_Items WHERE evalsheet_id LIKE CONCAT('%', :word, '%') ORDER BY evalsheet_id DESC")
  Iterable<Rec_Eval_Items> findAllByEvalsheet_idDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByEvalsheet_name]
  /*   [findAllByEvalsheet_nameDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「シート名五十音順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Rec_Eval_Items WHERE evalsheet_name LIKE CONCAT('%', :word, '%') ORDER BY evalsheet_name ASC")
  Iterable<Rec_Eval_Items> findAllByEvalsheet_name(@Param("word") String word);

  @Query("SELECT * FROM Rec_Eval_Items WHERE evalsheet_name LIKE CONCAT('%', :word, '%') ORDER BY evalsheet_name DESC")
  Iterable<Rec_Eval_Items> findAllByEvalsheet_nameDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByEvalsheet_date]
  /*   [findAllByEvalsheet_dateDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「シート作成日順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Rec_Eval_Items WHERE evalsheet_date = STR_TO_DATE(:word, '%Y-%m-%d') ORDER BY evalsheet_date ASC")
  Iterable<Rec_Eval_Items> findAllByEvalsheet_date(@Param("word") String word);

  @Query("SELECT * FROM Rec_Eval_Items WHERE evalsheet_date = STR_TO_DATE(:word, '%Y-%m-%d') ORDER BY evalsheet_date DESC")
  Iterable<Rec_Eval_Items> findAllByEvalsheet_dateDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByEvalsheet_kinds]
  /*   [findAllByEvalsheet_kindsDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「採用考課シート番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Rec_Eval_Items WHERE evalsheet_kinds = :word ORDER BY evalsheet_id ASC")
  Iterable<Rec_Eval_Items> findAllByEvalsheet_kinds(@Param("word") String word);

  @Query("SELECT * FROM Rec_Eval_Items WHERE evalsheet_kinds = :word ORDER BY evalsheet_id DESC")
  Iterable<Rec_Eval_Items> findAllByEvalsheet_kindsDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByEvalsheet_contents]
  /*   [findAllByEvalsheet_contentsDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「採用考課シート番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Rec_Eval_Items WHERE" 
  + " CONCAT(eval_item_contents_1 ,eval_item_contents_2 ,eval_item_contents_3 ,eval_item_contents_4 ,eval_item_contents_5 ,"
          + "eval_item_contents_6 ,eval_item_contents_7 ,eval_item_contents_8 ,eval_item_contents_9 ,eval_item_contents_10)"
  + " LIKE CONCAT('%', :word, '%') ORDER BY evalsheet_id ASC")
  Iterable<Rec_Eval_Items> findAllByEvalsheet_contents(@Param("word") String word);

  @Query("SELECT * FROM Rec_Eval_Items WHERE" 
  + " CONCAT(eval_item_contents_1 ,eval_item_contents_2 ,eval_item_contents_3 ,eval_item_contents_4 ,eval_item_contents_5 ,"
          + "eval_item_contents_6 ,eval_item_contents_7 ,eval_item_contents_8 ,eval_item_contents_9 ,eval_item_contents_10)"
  + " LIKE CONCAT('%', :word, '%') ORDER BY evalsheet_id DESC")
  Iterable<Rec_Eval_Items> findAllByEvalsheet_contentsDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByEvalsheet_itemkinds]
  /*   [findAllByEvalsheet_itemkindsDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「採用考課シート番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Rec_Eval_Items WHERE" 
  + " CONCAT(eval_item_kinds_1 ,eval_item_kinds_2 ,eval_item_kinds_3 ,eval_item_kinds_4 ,eval_item_kinds_5 ,"
          + "eval_item_kinds_6 ,eval_item_kinds_7 ,eval_item_kinds_8 ,eval_item_kinds_9 ,eval_item_kinds_10)"
  + " LIKE CONCAT('%', :word, '%') ORDER BY evalsheet_id ASC")
  Iterable<Rec_Eval_Items> findAllByEvalsheet_itemkinds(@Param("word") String word);

  @Query("SELECT * FROM Rec_Eval_Items WHERE" 
  + " CONCAT(eval_item_kinds_1 ,eval_item_kinds_2 ,eval_item_kinds_3 ,eval_item_kinds_4 ,eval_item_kinds_5 ,"
          + "eval_item_kinds_6 ,eval_item_kinds_7 ,eval_item_kinds_8 ,eval_item_kinds_9 ,eval_item_kinds_10)"
  + " LIKE CONCAT('%', :word, '%') ORDER BY evalsheet_id DESC")
  Iterable<Rec_Eval_Items> findAllByEvalsheet_itemkindsDESC(@Param("word") String word);
}