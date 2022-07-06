package com.springproject.dockerspring.service.ServiceInterface;

import com.springproject.dockerspring.entity.HistoryEntity.Member_Info_History;
import com.springproject.dockerspring.entity.NormalEntity.Member_Info;

/**************************************************************/
/*   [MemberInfoService]
/*   主に「団員管理」に関する処理の機能を提供する。
/**************************************************************/
public interface MemberInfoService extends CommonService<Member_Info, Member_Info_History>{

  /*******************************************/
  /*   共通処理に加えて実装するメソッドはない。
  /*******************************************/
}