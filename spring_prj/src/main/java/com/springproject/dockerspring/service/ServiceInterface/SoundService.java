package com.springproject.dockerspring.service.ServiceInterface;

import com.springproject.dockerspring.entity.HistoryEntity.Sound_Source_History;
import com.springproject.dockerspring.entity.NormalEntity.Sound_Source;

/**************************************************************/
/*   [SoundService]
/*   主に「音源管理」に関する処理の機能を提供する。
/**************************************************************/
public interface SoundService extends CommonService<Sound_Source, Sound_Source_History>{

  /*******************************************/
  /*   共通処理に加えて実装するメソッドはない。
  /*******************************************/
}