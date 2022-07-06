package com.springproject.dockerspring.controller;

import java.util.List;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.springproject.dockerspring.form.NormalForm.ComHistGetForm;



/**************************************************************/
/*   [ComBlobFunc]
/*   バイナリデータ系処理があるコントローラーへの機能を提供する。
/**************************************************************/
public interface ComBlobFunc<F>{

  /*************************************************************/
  /*   [selectBlobHistroy]
  /*   検索ワード、ジャンルの指定を元に、該当する履歴情報を
  /*   一覧で出力する。但し、バイナリデータはこの処理では
  /*   出力しない。
  /*************************************************************/
	SubmitEntity<List<Map<String, String>>> selectBlobHistroy(ComHistGetForm form, BindingResult bind_data);


  /*************************************************************/
  /*   [getHistroyBlob]
  /*   指定された履歴番号のバイナリデータを出力する。
  /*************************************************************/
	SubmitEntity<Map<String, String>> getHistroyBlob(String history_id, BindingResult bind_data);


  /*************************************************************/
  /*   [rollbackBlobHistroy]
  /*   指定された履歴情報を、本データにロールバックして
  /*   復元する。
  /*************************************************************/
	SubmitEntity<Object> rollbackBlobHistroy(String history_id, BindingResult bind_data);


  /*************************************************************/
  /*   [selectBlobData]
  /*   指定された管理番号の情報をすべて取得する。
  /*************************************************************/
	SubmitEntity<List<Map<String, String>>> selectBlobData(String search_id, BindingResult bind_data);


  /*************************************************************/
  /*   [insertBlobData]
  /*   入力されたフォームに従い、個別情報を新規追加する。
  /*************************************************************/
	SubmitEntity<List<Map<String, String>>> insertBlobData(F form, BindingResult bind_data);


  /*************************************************************/
  /*   [updateBlobData]
  /*   入力されたフォームに従い、既存の個別情報を更新する。
  /*************************************************************/
	SubmitEntity<Map<String, String>> updateBlobData(F form, BindingResult bind_data);


  /*************************************************************/
  /*   [deleteBlobData]
  /*   指定されたシリアルナンバーの個別データを削除する。
  /*************************************************************/
	SubmitEntity<Object> deleteBlobData(String serial_num, BindingResult bind_data);


  /*************************************************************/
  /*   [outputZipData]
  /*   指定された管理番号のバイナリデータをZIPファイルに入れ、
  /*   圧縮したものを出力する。
  /*************************************************************/
	SubmitEntity<String> outputZipData(String search_id, BindingResult bind_data);
	
	
  /*************************************************************/
  /*   [inputZipData]
  /*   アップロードされたZIPファイルからデータを抽出し、
  /*   そのデータを個別情報としてデータベースに全て保存する。
  /*************************************************************/
	SubmitEntity<Object> inputZipData(String search_id, MultipartFile zipfile, BindingResult bind_data);
}