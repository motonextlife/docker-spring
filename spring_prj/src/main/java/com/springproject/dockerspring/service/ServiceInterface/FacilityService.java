package com.springproject.dockerspring.service.ServiceInterface;

import com.springproject.dockerspring.entity.HistoryEntity.Facility_History;
import com.springproject.dockerspring.entity.NormalEntity.Facility;

/**************************************************************/
/*   [FacilityService]
/*   主に「設備管理」に関する処理の機能を提供する。
/**************************************************************/
public interface FacilityService extends CommonService<Facility, Facility_History>{

  /*************************************************************/
  /*   [foreignId]
  /*   データベースカラムに参照性制約があるデータにおいて、
  /*   該当するデータの整合性を確認する。
  /*************************************************************/
  Boolean foreignId(Facility_History entity);
}