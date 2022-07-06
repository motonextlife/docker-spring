package com.springproject.dockerspring.service.ServiceInterface;

import com.springproject.dockerspring.entity.HistoryEntity.Rec_Eval_Items_History;
import com.springproject.dockerspring.entity.NormalEntity.Rec_Eval_Items;

/**************************************************************/
/*   [RecEvalItemsService]
/*   主に「採用考課シート管理」に関する処理の機能を提供する。
/**************************************************************/
public interface RecEvalItemsService extends CommonService<Rec_Eval_Items, Rec_Eval_Items_History>{

  /*******************************************/
  /*   共通処理に加えて実装するメソッドはない。
  /*******************************************/
}