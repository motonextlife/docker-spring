package com.springproject.dockerspring.service;


/**************************************************************/
/*   [ListOrderEnum]
/*   「採用考課情報」や「点検情報」において、一覧情報を
/*   取得するときの並び順を指定する。
/**************************************************************/
public enum ListOrderEnum{
  UPLOAD_DATE_ASC,
  UPLOAD_DATE_DESC,
  ID_ASC,
  ID_DESC,
  SHEET_ASC,
  SHEET_DESC;
}