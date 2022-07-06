package com.springproject.dockerspring.repository.NormalRepo;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springproject.dockerspring.entity.NormalEntity.Equip_Inspect_Photo;





/**************************************************************/
/*   [EquipInspPhotoRepo]
/*   テーブル「Equip_Inspect_Photo」へのデータをやり取りや、
/*   エンティティ内のデータの加工を行う。
/**************************************************************/
public interface EquipInspPhotoRepo extends CrudRepository<Equip_Inspect_Photo, Integer>{


  /************************************************************/
  /*   [findByInspect_id]
  /*   指定された点検番号に該当する情報を全て取得。
  /************************************************************/
  @Query("SELECT * FROM Equip_Inspect_Photo WHERE inspect_id = :inspect_id")
  Iterable<Equip_Inspect_Photo> findByInspect_id(@Param("inspect_id") String inspect_id);
}