package com.springproject.dockerspring.repository.NormalRepo;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springproject.dockerspring.entity.NormalEntity.Rec_Eval;
import com.springproject.dockerspring.repository.NormalRepo.FormEnum.FormRecEval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;






/**************************************************************/
/*   [RecEvalRepo]
/*   テーブル「Rec_Eval」へのデータをやり取りや、
/*   エンティティ内のデータの加工を行う。
/**************************************************************/
public interface RecEvalRepo extends CrudRepository<Rec_Eval, Integer>{


  /***************************************************************/
  /*   [findAllJudge]
  /*   引数で渡された検索ジャンルと検索ワードに従い、検索処理に
  /*   使用するクエリメソッドを選定する。
  /*   また選定の際に、検索結果の並び順（昇順or降順）も指定する。
  /***************************************************************/
  public default Iterable<Rec_Eval> findAllJudge(String word, String subject, Boolean order){


    /**********************************************************************/
    /* 列挙型「FormRecEval」を用いて、引数の検索ジャンルが列挙型内に
    /* 定義されているものか判定。
    /* 無ければ、不正な値が渡されたと判断し、空の検索結果を返す。
    /**********************************************************************/
    Optional<FormRecEval> name = Arrays.stream(FormRecEval.values())
                                            .parallel()
                                            .filter(s -> s.name().equals(subject))
                                            .findFirst();

    if(name.isEmpty()){
      return new ArrayList<Rec_Eval>();
    }


    /**********************************************************************/
    /* 検索ジャンルを元に分岐を行い、該当する検索メソッドを実行する。
    /* なお、分岐先では並び順指定に伴ってさらに分岐を行う。
    /**********************************************************************/
    Iterable<Rec_Eval> list = null;
    switch(name.get()){
      case All:
        list = order ? findAllOriginal() : findAllOriginalDESC();
        break;
      case Eval_Id:
        list = order ? findAllByEval_id(word) : findAllByEval_idDESC(word);
        break;
      case Member_Id:
        list = order ? findAllByMember_id(word) : findAllByMember_idDESC(word);
        break;
      case Eval_Name:
        list = order ? findAllByEval_name(word) : findAllByEval_nameDESC(word);
        break;  
      case Eval_Date:
        list = order ? findAllByEval_date(word) : findAllByEval_dateDESC(word);
        break;
      case Evalsheet_Id:
        list = order ? findAllByEvalsheet_id(word) : findAllByEvalsheet_idDESC(word);
        break;
      case Eval_Contents:
        list = order ? findAllByEval_contents(word) : findAllByEval_contentsDESC(word);
        break;
      case Eval_Rank:
        list = order ? findAllByEval_rank(word) : findAllByEval_rankDESC(word);
        break;
    }
    
    return list;
  }


  /****************************************************************************/
  /*   [findList]
  /*   引数で渡ってきた団員番号から、該当する点検情報をリストとして全取得。
  /****************************************************************************/
  @Query("SELECT * FROM Rec_Eval WHERE member_id = :member_id")
  Iterable<Rec_Eval> findList(@Param("member_id") String member_id);


  /************************************************************/
  /*   [findByEval_id]
  /*   指定された採用考課番号に該当する情報を個別で取得。
  /************************************************************/
  @Query("SELECT * FROM Rec_Eval WHERE eval_id = :eval_id LIMIT 1")
  Optional<Rec_Eval> findByEval_id(@Param("eval_id") String eval_id);


  /**************************************************************/
  /*   [findAllOriginal]
  /*   [findAllOriginalDESC]
  /*   全データを採用考課番号順ですべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /**************************************************************/
  @Query("SELECT * FROM Rec_Eval ORDER BY eval_id ASC")
  Iterable<Rec_Eval> findAllOriginal();

  @Query("SELECT * FROM Rec_Eval ORDER BY eval_id DESC")
  Iterable<Rec_Eval> findAllOriginalDESC();


  /**************************************************************/
  /*   [existRemainMember_id]
  /*   指定された団員番号が存在するか確認
  /*   （なければ「0」一つでもあれば「1」）を返す
  /**************************************************************/
  @Query("SELECT COUNT(*) FROM Rec_Eval WHERE member_id = :member_id")
  Integer existRemainMember_id(@Param("member_id") String member_id);


  /**************************************************************/
  /*   [existRemainEvalsheet_id]
  /*   指定された採用考課シート番号が存在するか確認
  /*   （なければ「0」一つでもあれば「1」）を返す
  /**************************************************************/
  @Query("SELECT COUNT(*) FROM Rec_Eval WHERE evalsheet_id = :evalsheet_id")
  Integer existRemainEvalsheet_id(@Param("evalsheet_id") String evalsheet_id);


  /*************************************************************************/
  /*   [findAllByEval_id]
  /*   [findAllByEval_idDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「採用考課番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Rec_Eval WHERE eval_id LIKE CONCAT('%', :word, '%') ORDER BY eval_id ASC")
  Iterable<Rec_Eval> findAllByEval_id(@Param("word") String word);

  @Query("SELECT * FROM Rec_Eval WHERE eval_id LIKE CONCAT('%', :word, '%') ORDER BY eval_id DESC")
  Iterable<Rec_Eval> findAllByEval_idDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByMember_id]
  /*   [findAllByMember_idDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「団員番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Rec_Eval WHERE member_id LIKE CONCAT('%', :word, '%') ORDER BY member_id ASC")
  Iterable<Rec_Eval> findAllByMember_id(@Param("word") String word);

  @Query("SELECT * FROM Rec_Eval WHERE member_id LIKE CONCAT('%', :word, '%') ORDER BY member_id DESC")
  Iterable<Rec_Eval> findAllByMember_idDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByEval_name]
  /*   [findAllByEval_nameDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「採用考課名五十音順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Rec_Eval WHERE eval_name LIKE CONCAT('%', :word, '%') ORDER BY eval_name ASC")
  Iterable<Rec_Eval> findAllByEval_name(@Param("word") String word);

  @Query("SELECT * FROM Rec_Eval WHERE eval_name LIKE CONCAT('%', :word, '%') ORDER BY eval_name DESC")
  Iterable<Rec_Eval> findAllByEval_nameDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByEval_date]
  /*   [findAllByEval_dateDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「採用考課日順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Rec_Eval WHERE eval_date = STR_TO_DATE(:word, '%Y-%m-%d') ORDER BY eval_date ASC")
  Iterable<Rec_Eval> findAllByEval_date(@Param("word") String word);

  @Query("SELECT * FROM Rec_Eval WHERE eval_date = STR_TO_DATE(:word, '%Y-%m-%d') ORDER BY eval_date DESC")
  Iterable<Rec_Eval> findAllByEval_dateDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByEvalsheet_id]
  /*   [findAllByEvalsheet_idDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「採用考課シート番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Rec_Eval WHERE evalsheet_id LIKE CONCAT('%', :word, '%') ORDER BY evalsheet_id ASC")
  Iterable<Rec_Eval> findAllByEvalsheet_id(@Param("word") String word);

  @Query("SELECT * FROM Rec_Eval WHERE evalsheet_id LIKE CONCAT('%', :word, '%') ORDER BY evalsheet_id DESC")
  Iterable<Rec_Eval> findAllByEvalsheet_idDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByEval_contents]
  /*   [findAllByEval_contentsDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「採用考課番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Rec_Eval WHERE" 
  + " CONCAT(eval_contents_1 ,eval_contents_2 ,eval_contents_3 ,eval_contents_4 ,eval_contents_5 ,"
          + "eval_contents_6 ,eval_contents_7 ,eval_contents_8 ,eval_contents_9 ,eval_contents_10)"
  + " LIKE CONCAT('%', :word, '%') ORDER BY eval_id ASC")
  Iterable<Rec_Eval> findAllByEval_contents(@Param("word") String word);

  @Query("SELECT * FROM Rec_Eval WHERE" 
  + " CONCAT(eval_contents_1 ,eval_contents_2 ,eval_contents_3 ,eval_contents_4 ,eval_contents_5 ,"
          + "eval_contents_6 ,eval_contents_7 ,eval_contents_8 ,eval_contents_9 ,eval_contents_10)"
  + " LIKE CONCAT('%', :word, '%') ORDER BY eval_id DESC")
  Iterable<Rec_Eval> findAllByEval_contentsDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByEval_rank]
  /*   [findAllByEval_rankDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「採用考課番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Rec_Eval WHERE" 
  + " CONCAT(eval_rank_1 ,eval_rank_2 ,eval_rank_3 ,eval_rank_4 ,eval_rank_5 ,"
          + "eval_rank_6 ,eval_rank_7 ,eval_rank_8 ,eval_rank_9 ,eval_rank_10)"
  + " LIKE CONCAT('%', :word, '%') ORDER BY eval_id ASC")
  Iterable<Rec_Eval> findAllByEval_rank(@Param("word") String word);

  @Query("SELECT * FROM Rec_Eval WHERE" 
  + " CONCAT(eval_rank_1 ,eval_rank_2 ,eval_rank_3 ,eval_rank_4 ,eval_rank_5 ,"
          + "eval_rank_6 ,eval_rank_7 ,eval_rank_8 ,eval_rank_9 ,eval_rank_10)"
  + " LIKE CONCAT('%', :word, '%') ORDER BY eval_id DESC")
  Iterable<Rec_Eval> findAllByEval_rankDESC(@Param("word") String word);
}