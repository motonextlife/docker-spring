package com.springproject.dockerspring.repository.NormalRepo;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springproject.dockerspring.entity.NormalEntity.Sound_Source;
import com.springproject.dockerspring.repository.NormalRepo.FormEnum.FormSoundSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;





/**************************************************************/
/*   [SoundSourceRepo]
/*   テーブル「Sound_Source」へのデータをやり取りや、
/*   エンティティ内のデータの加工を行う。
/**************************************************************/
public interface SoundSourceRepo extends CrudRepository<Sound_Source, Integer>{


  /***************************************************************/
  /*   [findAllJudge]
  /*   引数で渡された検索ジャンルと検索ワードに従い、検索処理に
  /*   使用するクエリメソッドを選定する。
  /*   また選定の際に、検索結果の並び順（昇順or降順）も指定する。
  /***************************************************************/
  public default Iterable<Sound_Source> findAllJudge(String word, String subject, Boolean order){


    /**********************************************************************/
    /* 列挙型「FormSoundSource」を用いて、引数の検索ジャンルが列挙型内に
    /* 定義されているものか判定。
    /* 無ければ、不正な値が渡されたと判断し、空の検索結果を返す。
    /**********************************************************************/
    Optional<FormSoundSource> name = Arrays.stream(FormSoundSource.values())
                                               .parallel()
                                               .filter(s -> s.name().equals(subject))
                                               .findFirst();

    if(name.isEmpty()){
      return new ArrayList<Sound_Source>();
    }


    /**********************************************************************/
    /* 検索ジャンルを元に分岐を行い、該当する検索メソッドを実行する。
    /* なお、分岐先では並び順指定に伴ってさらに分岐を行う。
    /**********************************************************************/
    Iterable<Sound_Source> list = null;
    switch(name.get()){
      case All:
        list = order ? findAllOriginal() : findAllOriginalDESC();
        break;
      case Sound_Id:
        list = order ? findAllBySound_Id(word) : findAllBySound_IdDESC(word);
        break;
      case Upload_Date:
        list = order ? findAllByUpload_Date(word) : findAllByUpload_DateDESC(word);
        break;
      case Song_Title:
        list = order ? findAllBySong_title(word) : findAllBySong_titleDESC(word);
        break;
      case Composer:
        list = order ? findAllByComposer(word) : findAllByComposerDESC(word);
        break;
      case Performer:
        list = order ? findAllByPerformer(word) : findAllByPerformerDESC(word);
        break;
      case Publisher:
        list = order ? findAllByPublisher(word) : findAllByPublisherDESC(word);
        break;
      case Comment:
        list = order ? findAllByComment(word) : findAllByCommentDESC(word);
        break;
    }
    
    return list;
  }


  /************************************************************/
  /*   [findBySound_Id]
  /*   指定された音源番号に該当する情報を個別で取得。
  /************************************************************/
  @Query("SELECT * FROM Sound_Source WHERE sound_id = :sound_id LIMIT 1")
  Optional<Sound_Source> findBySound_Id(@Param("sound_id") String sound_id);


  /**************************************************************/
  /*   [findAllOriginal]
  /*   [findAllOriginalDESC]
  /*   全データを音源番号順ですべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /**************************************************************/
  @Query("SELECT * FROM Sound_Source ORDER BY sound_id ASC")
  Iterable<Sound_Source> findAllOriginal();

  @Query("SELECT * FROM Sound_Source ORDER BY sound_id DESC")
  Iterable<Sound_Source> findAllOriginalDESC();


  /*************************************************************************/
  /*   [findAllBySound_Id]
  /*   [findAllBySound_IdDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「音源番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Sound_Source WHERE sound_id LIKE CONCAT('%', :word, '%') ORDER BY sound_id ASC")
  Iterable<Sound_Source> findAllBySound_Id(@Param("word") String word);

  @Query("SELECT * FROM Sound_Source WHERE sound_id LIKE CONCAT('%', :word, '%') ORDER BY sound_id DESC")
  Iterable<Sound_Source> findAllBySound_IdDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByUpload_Date]
  /*   [findAllByUpload_DateDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「登録日順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Sound_Source WHERE upload_date = STR_TO_DATE(:word, '%Y-%m-%d') ORDER BY upload_date ASC")
  Iterable<Sound_Source> findAllByUpload_Date(@Param("word") String word);

  @Query("SELECT * FROM Sound_Source WHERE upload_date = STR_TO_DATE(:word, '%Y-%m-%d') ORDER BY upload_date DESC")
  Iterable<Sound_Source> findAllByUpload_DateDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllBySong_title]
  /*   [findAllBySong_titleDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「曲名五十音順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Sound_Source WHERE song_title LIKE CONCAT('%', :word, '%') ORDER BY song_title ASC")
  Iterable<Sound_Source> findAllBySong_title(@Param("word") String word);

  @Query("SELECT * FROM Sound_Source WHERE song_title LIKE CONCAT('%', :word, '%') ORDER BY song_title DESC")
  Iterable<Sound_Source> findAllBySong_titleDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByComposer]
  /*   [findAllByComposerDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「作曲者五十音順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Sound_Source WHERE composer LIKE CONCAT('%', :word, '%') ORDER BY composer ASC")
  Iterable<Sound_Source> findAllByComposer(@Param("word") String word);

  @Query("SELECT * FROM Sound_Source WHERE composer LIKE CONCAT('%', :word, '%') ORDER BY composer DESC")
  Iterable<Sound_Source> findAllByComposerDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByPerformer]
  /*   [findAllByPerformerDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「演奏者五十音順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Sound_Source WHERE performer LIKE CONCAT('%', :word, '%') ORDER BY performer ASC")
  Iterable<Sound_Source> findAllByPerformer(@Param("word") String word);

  @Query("SELECT * FROM Sound_Source WHERE performer LIKE CONCAT('%', :word, '%') ORDER BY performer DESC")
  Iterable<Sound_Source> findAllByPerformerDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByPublisher]
  /*   [findAllByPublisherDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「出版社五十音順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Sound_Source WHERE publisher LIKE CONCAT('%', :word, '%') ORDER BY publisher ASC")
  Iterable<Sound_Source> findAllByPublisher(@Param("word") String word);

  @Query("SELECT * FROM Sound_Source WHERE publisher LIKE CONCAT('%', :word, '%') ORDER BY publisher DESC")
  Iterable<Sound_Source> findAllByPublisherDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByComment]
  /*   [findAllByCommentDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「音源番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Sound_Source WHERE other_comment LIKE CONCAT('%', :word, '%') ORDER BY sound_id ASC")
  Iterable<Sound_Source> findAllByComment(@Param("word") String word);

  @Query("SELECT * FROM Sound_Source WHERE other_comment LIKE CONCAT('%', :word, '%') ORDER BY sound_id DESC")
  Iterable<Sound_Source> findAllByCommentDESC(@Param("word") String word);
}