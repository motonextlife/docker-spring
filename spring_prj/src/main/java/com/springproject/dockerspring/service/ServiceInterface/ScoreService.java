package com.springproject.dockerspring.service.ServiceInterface;

import com.springproject.dockerspring.entity.HistoryEntity.Musical_Score_History;
import com.springproject.dockerspring.entity.NormalEntity.Musical_Score;

/**************************************************************/
/*   [ScoreService]
/*   主に「楽譜管理」に関する処理の機能を提供する。
/**************************************************************/
public interface ScoreService extends CommonService<Musical_Score, Musical_Score_History>{

  /*******************************************/
  /*   共通処理に加えて実装するメソッドはない。
  /*******************************************/
}