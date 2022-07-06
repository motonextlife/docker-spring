package com.springproject.dockerspring.repository.HistoryRepo;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springproject.dockerspring.entity.HistoryEntity.Inspect_Items_History;

import java.util.Date;





/**************************************************************/
/*   [InspItemsHistRepo]
/*   テーブル「Inspect_Items_History」へのデータベース
/*   アクセスを行う。
/**************************************************************/
public interface InspItemsHistRepo extends CrudRepository<Inspect_Items_History, Integer>{


  /******************************************************************************************/
  /*   [findAllByDateBetween]
  /*   格納済みの履歴情報に対して、対象の点検シート番号と期間を指定して、データの検索を行う。
  /******************************************************************************************/
  @Query(
    "SELECT * FROM Inspect_Items_History " + 
    "WHERE inspsheet_id = :inspsheet_id AND change_datetime BETWEEN :start_datetime AND :end_datetime LIMIT 500"
  )
  Iterable<Inspect_Items_History> findAllByDateBetween(
    @Param("inspsheet_id") String inspsheet_id, 
    @Param("start_datetime") Date start_datetime, 
    @Param("end_datetime") Date end_datetime
  );


  /******************************************************************************************/
  /*   [deleteByDateBetween]
  /*   格納済みの履歴情報に対して、対象の期間を指定して、該当する情報全てを削除する。
  /******************************************************************************************/
  @Modifying
  @Query(
    "DELETE FROM Inspect_Items_History " + 
    "WHERE change_datetime BETWEEN :start_datetime AND :end_datetime"
  )
  void deleteByDateBetween(
    @Param("start_datetime") Date start_datetime, 
    @Param("end_datetime") Date end_datetime
  );
}