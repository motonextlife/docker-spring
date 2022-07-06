package com.springproject.dockerspring.service.BlobServiceInterface;

import com.springproject.dockerspring.entity.HistoryEntity.Equip_Inspect_Photo_History;
import com.springproject.dockerspring.entity.NormalEntity.Equip_Inspect_Photo;
import com.springproject.dockerspring.form.BlobImplForm.EquipInspPhotoForm;

/**************************************************************/
/*   [EquipInspPhotoService]
/*   主に「点検記録データ管理」に関する処理の機能を提供する。
/**************************************************************/
public interface EquipInspPhotoService 
    extends CommonBlobService<Equip_Inspect_Photo, Equip_Inspect_Photo_History, EquipInspPhotoForm>{

  /*******************************************/
  /*   共通処理に加えて実装するメソッドはない。
  /*******************************************/
}