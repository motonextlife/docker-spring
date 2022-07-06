package com.springproject.dockerspring.service.BlobServiceInterface;

import com.springproject.dockerspring.entity.HistoryEntity.Rec_Eval_Record_History;
import com.springproject.dockerspring.entity.NormalEntity.Rec_Eval_Record;
import com.springproject.dockerspring.form.BlobImplForm.RecEvalRecordForm;

/**************************************************************/
/*   [RecEvalRecordService]
/*   主に「採用考課記録データ管理」に関する処理の機能を提供する。
/**************************************************************/
public interface RecEvalRecordService 
    extends CommonBlobService<Rec_Eval_Record, Rec_Eval_Record_History, RecEvalRecordForm>{

  /*******************************************/
  /*   共通処理に加えて実装するメソッドはない。
  /*******************************************/
}