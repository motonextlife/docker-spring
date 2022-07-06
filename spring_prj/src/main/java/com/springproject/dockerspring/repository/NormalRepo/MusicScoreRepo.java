package com.springproject.dockerspring.repository.NormalRepo;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springproject.dockerspring.entity.NormalEntity.Musical_Score;
import com.springproject.dockerspring.repository.NormalRepo.FormEnum.FormMusicScore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;





/**************************************************************/
/*   [MusicScoreRepo]
/*   テーブル「Musical_Score」へのデータをやり取りや、
/*   エンティティ内のデータの加工を行う。
/**************************************************************/
public interface MusicScoreRepo extends CrudRepository<Musical_Score, Integer>{


  /***************************************************************/
  /*   [findAllJudge]
  /*   引数で渡された検索ジャンルと検索ワードに従い、検索処理に
  /*   使用するクエリメソッドを選定する。
  /*   また選定の際に、検索結果の並び順（昇順or降順）も指定する。
  /***************************************************************/
  public default Iterable<Musical_Score> findAllJudge(String word, String subject, Boolean order){


    /**********************************************************************/
    /* 列挙型「FormMusicScore」を用いて、引数の検索ジャンルが列挙型内に
    /* 定義されているものか判定。
    /* 無ければ、不正な値が渡されたと判断し、空の検索結果を返す。
    /**********************************************************************/
    Optional<FormMusicScore> name = Arrays.stream(FormMusicScore.values())
                                               .parallel()
                                               .filter(s -> s.name().equals(subject))
                                               .findFirst();

    if(name.isEmpty()){
      return new ArrayList<Musical_Score>();
    }


    /**********************************************************************/
    /* 検索ジャンルを元に分岐を行い、該当する検索メソッドを実行する。
    /* なお、分岐先では並び順指定に伴ってさらに分岐を行う。
    /**********************************************************************/
    Iterable<Musical_Score> list = null;
    switch(name.get()){
      case All:
        list = order ? findAllOriginal() : findAllOriginalDESC();
        break;
      case Score_Id:
        list = order ? findAllByScore_id(word) : findAllByScore_idDESC(word);
        break;
      case Buy_Date:
        list = order ? findAllByBuy_date(word) : findAllByBuy_dateDESC(word);
        break;
      case Song_Title:
        list = order ? findAllBySong_title(word) : findAllBySong_titleDESC(word);
        break;
      case Composer:
        list = order ? findAllByComposer(word) : findAllByComposerDESC(word);
        break;
      case Arranger:
        list = order ? findAllByArranger(word) : findAllByArrangerDESC(word);
        break;
      case Publisher:
        list = order ? findAllByPublisher(word) : findAllByPublisherDESC(word);
        break;
      case Strage_Loc:
        list = order ? findAllByStrage_loc(word) : findAllByStrage_locDESC(word);
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
  /*   [findByScore_Id]
  /*   指定された楽譜番号に該当する情報を個別で取得。
  /************************************************************/
  @Query("SELECT * FROM Musical_Score WHERE score_id = :score_id LIMIT 1")
  Optional<Musical_Score> findByScore_Id(@Param("score_id") String score_id);


  /**************************************************************/
  /*   [findAllOriginal]
  /*   [findAllOriginalDESC]
  /*   全データを楽譜番号順ですべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /**************************************************************/
  @Query("SELECT * FROM Musical_Score ORDER BY score_id ASC")
  Iterable<Musical_Score> findAllOriginal();

  @Query("SELECT * FROM Musical_Score ORDER BY score_id DESC")
  Iterable<Musical_Score> findAllOriginalDESC();


  /*************************************************************************/
  /*   [findAllByScore_id]
  /*   [findAllByScore_idDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「楽譜番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Musical_Score WHERE score_id LIKE CONCAT('%', :word, '%') ORDER BY score_id ASC")
  Iterable<Musical_Score> findAllByScore_id(@Param("word") String word);

  @Query("SELECT * FROM Musical_Score WHERE score_id LIKE CONCAT('%', :word, '%') ORDER BY score_id DESC")
  Iterable<Musical_Score> findAllByScore_idDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByBuy_date]
  /*   [findAllByBuy_dateDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「購入日順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Musical_Score WHERE buy_date = STR_TO_DATE(:word, '%Y-%m-%d') ORDER BY buy_date ASC")
  Iterable<Musical_Score> findAllByBuy_date(@Param("word") String word);

  @Query("SELECT * FROM Musical_Score WHERE buy_date = STR_TO_DATE(:word, '%Y-%m-%d') ORDER BY buy_date DESC")
  Iterable<Musical_Score> findAllByBuy_dateDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllBySong_title]
  /*   [findAllBySong_titleDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「曲名五十音順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Musical_Score WHERE song_title LIKE CONCAT('%', :word, '%') ORDER BY song_title ASC")
  Iterable<Musical_Score> findAllBySong_title(@Param("word") String word);

  @Query("SELECT * FROM Musical_Score WHERE song_title LIKE CONCAT('%', :word, '%') ORDER BY song_title DESC")
  Iterable<Musical_Score> findAllBySong_titleDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByComposer]
  /*   [findAllByComposerDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「作曲者五十音順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Musical_Score WHERE composer LIKE CONCAT('%', :word, '%') ORDER BY composer ASC")
  Iterable<Musical_Score> findAllByComposer(@Param("word") String word);

  @Query("SELECT * FROM Musical_Score WHERE composer LIKE CONCAT('%', :word, '%') ORDER BY composer DESC")
  Iterable<Musical_Score> findAllByComposerDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByArranger]
  /*   [findAllByArrangerDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「編曲者五十音順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Musical_Score WHERE arranger LIKE CONCAT('%', :word, '%') ORDER BY arranger ASC")
  Iterable<Musical_Score> findAllByArranger(@Param("word") String word);

  @Query("SELECT * FROM Musical_Score WHERE arranger LIKE CONCAT('%', :word, '%') ORDER BY arranger DESC")
  Iterable<Musical_Score> findAllByArrangerDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByPublisher]
  /*   [findAllByPublisherDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「出版社五十音順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Musical_Score WHERE publisher LIKE CONCAT('%', :word, '%') ORDER BY publisher ASC")
  Iterable<Musical_Score> findAllByPublisher(@Param("word") String word);

  @Query("SELECT * FROM Musical_Score WHERE publisher LIKE CONCAT('%', :word, '%') ORDER BY publisher DESC")
  Iterable<Musical_Score> findAllByPublisherDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByStrage_loc]
  /*   [findAllByStrage_locDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「保管場所五十音順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Musical_Score WHERE strage_loc LIKE CONCAT('%', :word, '%') ORDER BY strage_loc ASC")
  Iterable<Musical_Score> findAllByStrage_loc(@Param("word") String word);

  @Query("SELECT * FROM Musical_Score WHERE strage_loc LIKE CONCAT('%', :word, '%') ORDER BY strage_loc DESC")
  Iterable<Musical_Score> findAllByStrage_locDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByDisp_date]
  /*   [findAllByDisp_dateDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「廃棄日順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Musical_Score WHERE disp_date = STR_TO_DATE(:word, '%Y-%m-%d') ORDER BY disp_date ASC")
  Iterable<Musical_Score> findAllByDisp_date(@Param("word") String word);

  @Query("SELECT * FROM Musical_Score WHERE disp_date = STR_TO_DATE(:word, '%Y-%m-%d') ORDER BY disp_date DESC")
  Iterable<Musical_Score> findAllByDisp_dateDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByComment]
  /*   [findAllByCommentDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「楽譜番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Musical_Score WHERE other_comment LIKE CONCAT('%', :word, '%') ORDER BY score_id ASC")
  Iterable<Musical_Score> findAllByComment(@Param("word") String word);

  @Query("SELECT * FROM Musical_Score WHERE other_comment LIKE CONCAT('%', :word, '%') ORDER BY score_id DESC")
  Iterable<Musical_Score> findAllByCommentDESC(@Param("word") String word);
}