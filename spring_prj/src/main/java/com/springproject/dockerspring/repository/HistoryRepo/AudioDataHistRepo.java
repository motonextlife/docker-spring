package com.springproject.dockerspring.repository.HistoryRepo;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springproject.dockerspring.entity.HistoryEntity.Audio_Data_History;

import java.util.Date;





/**************************************************************/
/*   [AudioDataHistRepo]
/*   テーブル「Audio_Data_History」へのデータベース
/*   アクセスを行う。
/**************************************************************/
public interface AudioDataHistRepo extends CrudRepository<Audio_Data_History, Integer>{


  /******************************************************************************************/
  /*   [findAllByDateBetween]
  /*   格納済みの履歴情報に対して、対象の音源番号と期間を指定して、データの検索を行う。
  /******************************************************************************************/
  @Query(
    "SELECT history_id, change_datetime, change_kinds, operation_user, serial_num, sound_id, sound_name " + 
    "FROM Audio_Data_History " + 
    "WHERE sound_id = :sound_id AND change_datetime BETWEEN :start_datetime AND :end_datetime LIMIT 500"
  )
  Iterable<Audio_Data_History> findAllByDateBetween(
    @Param("sound_id") String evalsheet_id, 
    @Param("start_datetime") Date start_datetime, 
    @Param("end_datetime") Date end_datetime
  );


  /******************************************************************************************/
  /*   [deleteByDateBetween]
  /*   格納済みの履歴情報に対して、対象の期間を指定して、該当する情報全てを削除する。
  /******************************************************************************************/
  @Modifying
  @Query(
    "DELETE FROM Audio_Data_History " + 
    "WHERE change_datetime BETWEEN :start_datetime AND :end_datetime"
  )
  void deleteByDateBetween(
    @Param("start_datetime") Date start_datetime, 
    @Param("end_datetime") Date end_datetime
  );
}