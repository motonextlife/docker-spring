package com.springproject.dockerspring.repository.NormalRepo;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springproject.dockerspring.entity.NormalEntity.Audio_Data;





/**************************************************************/
/*   [AudioDataRepo]
/*   テーブル「Audio_Data」へのデータをやり取りや、
/*   エンティティ内のデータの加工を行う。
/**************************************************************/
public interface AudioDataRepo extends CrudRepository<Audio_Data, Integer>{


  /************************************************************/
  /*   [findBySound_id]
  /*   指定された採用考課番号に該当する情報を全て取得。
  /************************************************************/
  @Query("SELECT * FROM Audio_Data WHERE sound_id = :sound_id")
  Iterable<Audio_Data> findBySound_id(@Param("sound_id") String sound_id);
}