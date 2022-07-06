package com.springproject.dockerspring.controller;

import java.util.List;
import java.util.Map;

import org.springframework.validation.BindingResult;




/**************************************************************/
/*   [ComListFunc]
/*   「採用考課管理」「点検管理」において
/*   現在登録中の情報を一覧表示する機能を持つ。
/**************************************************************/
public interface ComListFunc{


  /*************************************************************/
  /*   [selectListData]
  /*   団員番号や設備番号を元に、現在登録されている評価情報を
  /*   一覧で取得する。
  /*************************************************************/
	SubmitEntity<List<Map<String, String>>> selectListData(String list_id, String order, BindingResult bind_data);
}