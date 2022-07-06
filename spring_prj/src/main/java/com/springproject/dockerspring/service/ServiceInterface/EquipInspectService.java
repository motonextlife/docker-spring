package com.springproject.dockerspring.service.ServiceInterface;

import com.springproject.dockerspring.entity.HistoryEntity.Equip_Inspect_History;
import com.springproject.dockerspring.entity.NormalEntity.Equip_Inspect;
import com.springproject.dockerspring.service.ListOrderEnum;
import com.springproject.dockerspring.service.OriginalException.ForeignMissException;

/**************************************************************/
/*   [EquipInspectService]
/*   主に「点検管理」に関する処理の機能を提供する。
/**************************************************************/
public interface EquipInspectService extends CommonService<Equip_Inspect, Equip_Inspect_History>{

  /*************************************************************/
  /*   [selectList]
  /*   入力された管理番号に対応する情報をリストで取得する。
  /*************************************************************/
  Iterable<Equip_Inspect> selectList(String id, ListOrderEnum order);

  /*************************************************************/
  /*   [foreignId]
  /*   データベースカラムに参照性制約があるデータにおいて、
  /*   該当するデータの整合性を確認する。
  /*************************************************************/
  void foreignId(Equip_Inspect_History entity) throws ForeignMissException;
}