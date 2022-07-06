package com.springproject.dockerspring.service.ServiceInterface;

import com.springproject.dockerspring.entity.HistoryEntity.Inspect_Items_History;
import com.springproject.dockerspring.entity.NormalEntity.Inspect_Items;

/**************************************************************/
/*   [InspectItemsService]
/*   主に「点検シート管理」に関する処理の機能を提供する。
/**************************************************************/
public interface InspectItemsService extends CommonService<Inspect_Items, Inspect_Items_History>{

  /*******************************************/
  /*   共通処理に加えて実装するメソッドはない。
  /*******************************************/
}