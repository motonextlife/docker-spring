package com.springproject.dockerspring.service.BlobServiceInterface;

import com.springproject.dockerspring.entity.HistoryEntity.Score_Pdf_History;
import com.springproject.dockerspring.entity.NormalEntity.Score_Pdf;
import com.springproject.dockerspring.form.BlobImplForm.ScorePdfForm;

/**************************************************************/
/*   [ScorePdfService]
/*   主に「楽譜データ管理」に関する処理の機能を提供する。
/**************************************************************/
public interface ScorePdfService 
    extends CommonBlobService<Score_Pdf, Score_Pdf_History, ScorePdfForm>{

  /*******************************************/
  /*   共通処理に加えて実装するメソッドはない。
  /*******************************************/
}