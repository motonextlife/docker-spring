package com.springproject.dockerspring.repository.HistoryRepo;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springproject.dockerspring.entity.HistoryEntity.Equip_Inspect_Photo_History;

import java.util.Date;





/**************************************************************/
/*   [EquipInspPhotoHistRepo]
/*   テーブル「Equip_Inspect_Photo_History」へのデータベース
/*   アクセスを行う。
/**************************************************************/
public interface EquipInspPhotoHistRepo extends CrudRepository<Equip_Inspect_Photo_History, Integer>{


  /******************************************************************************************/
  /*   [findAllByDateBetween]
  /*   格納済みの履歴情報に対して、対象の点検番号と期間を指定して、データの検索を行う。
  /******************************************************************************************/
  @Query(
    "SELECT history_id, change_datetime, change_kinds, operation_user, serial_num, inspect_id, photo_name " + 
    "FROM Equip_Inspect_Photo_History " + 
    "WHERE inspect_id = :inspect_id AND change_datetime BETWEEN :start_datetime AND :end_datetime LIMIT 500"
  )
  Iterable<Equip_Inspect_Photo_History> findAllByDateBetween(
    @Param("inspect_id") String evalsheet_id, 
    @Param("start_datetime") Date start_datetime, 
    @Param("end_datetime") Date end_datetime
  );


  /******************************************************************************************/
  /*   [deleteByDateBetween]
  /*   格納済みの履歴情報に対して、対象の期間を指定して、該当する情報全てを削除する。
  /******************************************************************************************/
  @Modifying
  @Query(
    "DELETE FROM Equip_Inspect_Photo_History " + 
    "WHERE change_datetime BETWEEN :start_datetime AND :end_datetime"
  )
  void deleteByDateBetween(
    @Param("start_datetime") Date start_datetime, 
    @Param("end_datetime") Date end_datetime
  );
}