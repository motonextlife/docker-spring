package com.springproject.dockerspring.service.BlobServiceInterface;

import com.springproject.dockerspring.entity.HistoryEntity.Audio_Data_History;
import com.springproject.dockerspring.entity.NormalEntity.Audio_Data;
import com.springproject.dockerspring.form.BlobImplForm.AudioDataForm;

/**************************************************************/
/*   [AudioDataService]
/*   主に「音源データ管理」に関する処理の機能を提供する。
/**************************************************************/
public interface AudioDataService 
    extends CommonBlobService<Audio_Data, Audio_Data_History, AudioDataForm>{

  /*******************************************/
  /*   共通処理に加えて実装するメソッドはない。
  /*******************************************/
}