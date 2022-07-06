package com.springproject.dockerspring.repository.HistoryRepo;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springproject.dockerspring.entity.HistoryEntity.Score_Pdf_History;

import java.util.Date;





/**************************************************************/
/*   [ScorePdfHistRepo]
/*   テーブル「Score_Pdf_History」へのデータベース
/*   アクセスを行う。
/**************************************************************/
public interface ScorePdfHistRepo extends CrudRepository<Score_Pdf_History, Integer>{


  /******************************************************************************************/
  /*   [findAllByDateBetween]
  /*   格納済みの履歴情報に対して、対象の楽譜番号と期間を指定して、データの検索を行う。
  /******************************************************************************************/
  @Query(
    "SELECT history_id, change_datetime, change_kinds, operation_user, serial_num, score_id, score_name " + 
    "FROM Score_Pdf_History " + 
    "WHERE score_id = :score_id AND change_datetime BETWEEN :start_datetime AND :end_datetime LIMIT 500"
  )
  Iterable<Score_Pdf_History> findAllByDateBetween(
    @Param("score_id") String evalsheet_id, 
    @Param("start_datetime") Date start_datetime, 
    @Param("end_datetime") Date end_datetime
  );


  /******************************************************************************************/
  /*   [deleteByDateBetween]
  /*   格納済みの履歴情報に対して、対象の期間を指定して、該当する情報全てを削除する。
  /******************************************************************************************/
  @Modifying
  @Query(
    "DELETE FROM Score_Pdf_History " + 
    "WHERE change_datetime BETWEEN :start_datetime AND :end_datetime"
  )
  void deleteByDateBetween(
    @Param("start_datetime") Date start_datetime, 
    @Param("end_datetime") Date end_datetime
  );
}