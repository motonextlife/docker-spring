package com.springproject.dockerspring.service.ServiceInterface;

import com.springproject.dockerspring.entity.HistoryEntity.Rec_Eval_History;
import com.springproject.dockerspring.entity.NormalEntity.Rec_Eval;
import com.springproject.dockerspring.service.ListOrderEnum;
import com.springproject.dockerspring.service.OriginalException.ForeignMissException;

/**************************************************************/
/*   [RecEvalService]
/*   主に「採用考課管理」に関する処理の機能を提供する。
/**************************************************************/
public interface RecEvalService extends CommonService<Rec_Eval, Rec_Eval_History>{

  /*************************************************************/
  /*   [selectList]
  /*   入力された管理番号に対応する情報をリストで取得する。
  /*************************************************************/
  Iterable<Rec_Eval> selectList(String id, ListOrderEnum order);

  /*************************************************************/
  /*   [foreignId]
  /*   データベースカラムに参照性制約があるデータにおいて、
  /*   該当するデータの整合性を確認する。
  /*************************************************************/
  void foreignId(Rec_Eval_History entity) throws ForeignMissException;
}