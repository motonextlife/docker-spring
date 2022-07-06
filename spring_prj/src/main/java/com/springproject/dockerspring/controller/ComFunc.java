package com.springproject.dockerspring.controller;

import java.util.List;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.springproject.dockerspring.form.NormalForm.ComHistGetForm;
import com.springproject.dockerspring.form.NormalForm.ComSearchForm;



/**************************************************************/
/*   [ComFunc]
/*   全てのコントローラに共通で必要な機能を提供する。
/**************************************************************/
public interface ComFunc<F>{

  /*************************************************************/
  /*   [selectHistroy]
  /*   検索ワード、ジャンルの指定を元に、該当する履歴情報を
  /*   一覧で出力する。
  /*************************************************************/
	SubmitEntity<List<Map<String, String>>> selectHistroy(ComHistGetForm form, BindingResult bind_data);


  /*************************************************************/
  /*   [rollbackHistroy]
  /*   指定された履歴情報を、本データにロールバックして
  /*   復元する。
  /*************************************************************/
	SubmitEntity<Object> rollbackHistroy(String history_id, BindingResult bind_data);


  /*************************************************************/
  /*   [selectData]
  /*   指定された管理番号の個別情報を取得する。
  /*************************************************************/
	SubmitEntity<Map<String, String>> selectData(String search_id, BindingResult bind_data);


  /*************************************************************/
  /*   [insertData]
  /*   入力されたフォームに従い、個別情報を新規追加する。
  /*************************************************************/
	SubmitEntity<Map<String, String>> insertData(F form, BindingResult bind_data);


  /*************************************************************/
  /*   [updateData]
  /*   入力されたフォームに従い、既存の個別情報を更新する。
  /*************************************************************/
	SubmitEntity<Map<String, String>> updateData(F form, BindingResult bind_data);


  /*************************************************************/
  /*   [deleteData]
  /*   指定されたシリアルナンバーの個別データを削除する。
  /*************************************************************/
	SubmitEntity<Object> deleteData(String serial_num, BindingResult bind_data);


  /*************************************************************/
  /*   [selectBulkData]
  /*   現在保存中のデータを、検索ワード、ジャンルを元に
  /*   一覧で取得する。
  /*************************************************************/
	SubmitEntity<List<Map<String, String>>> selectBulkData(ComSearchForm form, BindingResult bind_data);


  /*************************************************************/
  /*   [outputBulkCsvData]
  /*   現在保存中のデータを、検索ワード、ジャンルを元に
  /*   一覧で取得し、そのデータをCSV形式にして出力する。
  /*************************************************************/
	SubmitEntity<String> outputBulkCsvData(ComSearchForm form, BindingResult bind_data);


  /*************************************************************/
  /*   [outputPdfData]
  /*   指定されたシリアルナンバーの個別データを決められた
  /*   フォーマットのPDFに変換して出力する。
  /*************************************************************/
	SubmitEntity<String> outputPdfData(String serial_num, BindingResult bind_data);


  /*************************************************************/
  /*   [inputCsvData]
  /*   アップロードされたCSVファイルからデータを抽出し、
  /*   そのデータを個別情報としてデータベースに全て保存する。
  /*************************************************************/
	SubmitEntity<List<List<Integer>>> inputCsvData(MultipartFile csvfile, BindingResult bind_data);


  /*************************************************************/
  /*   [outputCsvTemplateData]
  /*   アップロードに必要なCSVファイルのテンプレートを
  /*   出力する。
  /*************************************************************/
	SubmitEntity<String> outputCsvTemplateData();
}