package com.springproject.dockerspring.repository.NormalRepo;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springproject.dockerspring.entity.NormalEntity.Rec_Eval_Record;





/**************************************************************/
/*   [RecEvalRecordRepo]
/*   テーブル「Rec_Eval_Record」へのデータをやり取りや、
/*   エンティティ内のデータの加工を行う。
/**************************************************************/
public interface RecEvalRecordRepo extends CrudRepository<Rec_Eval_Record, Integer>{


  /************************************************************/
  /*   [findByEval_id]
  /*   指定された採用考課番号に該当する情報を全て取得。
  /************************************************************/
  @Query("SELECT * FROM Rec_Eval_Record WHERE eval_id = :eval_id")
  Iterable<Rec_Eval_Record> findByEval_id(@Param("eval_id") String eval_id);
}