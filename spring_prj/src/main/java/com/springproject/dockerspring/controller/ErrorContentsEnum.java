package com.springproject.dockerspring.controller;



/**************************************************************/
/*   [ErrorContentsEnum]
/*   フロント側へ返却するエラーの種別を定義する。
/**************************************************************/
public enum ErrorContentsEnum{
  VALIDATION_ERROR,
  ROLLBACK_ERROR,
  UPDATE_ERROR,
  DELETE_ERROR,
  SEARCH_EMPTY,
  FILE_MISSMATCH,
  BLOB_OVER_COUNT;
}