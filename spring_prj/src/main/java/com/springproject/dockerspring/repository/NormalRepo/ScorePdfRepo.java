package com.springproject.dockerspring.repository.NormalRepo;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springproject.dockerspring.entity.NormalEntity.Score_Pdf;





/**************************************************************/
/*   [ScorePdfRepo]
/*   テーブル「Score_Pdf」へのデータをやり取りや、
/*   エンティティ内のデータの加工を行う。
/**************************************************************/
public interface ScorePdfRepo extends CrudRepository<Score_Pdf, Integer>{

  
  /************************************************************/
  /*   [findByScore_id]
  /*   指定された採用考課番号に該当する情報を全て取得。
  /************************************************************/
  @Query("SELECT * FROM Score_Pdf WHERE score_id = :score_id")
  Iterable<Score_Pdf> findByScore_id(@Param("score_id") String score_id);
}