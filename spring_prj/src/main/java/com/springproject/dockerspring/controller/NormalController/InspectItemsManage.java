package com.springproject.dockerspring.controller.NormalController;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.springproject.dockerspring.component.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.controller.ComFunc;
import com.springproject.dockerspring.controller.CommonController;
import com.springproject.dockerspring.controller.ErrorContentsEnum;
import com.springproject.dockerspring.controller.SubmitEntity;
import com.springproject.dockerspring.entity.HistoryEntity.Inspect_Items_History;
import com.springproject.dockerspring.entity.NormalEntity.Inspect_Items;
import com.springproject.dockerspring.form.CsvImplForm.InspectItemsForm;
import com.springproject.dockerspring.form.NormalForm.ComHistGetForm;
import com.springproject.dockerspring.form.NormalForm.ComSearchForm;
import com.springproject.dockerspring.service.OriginalException.ForeignMissException;
import com.springproject.dockerspring.service.ServiceInterface.InspectItemsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;





/**************************************************************/
/*   [InspectItemsManage]
/*   「点検シート管理」に必要な処理を行う。
/**************************************************************/
@Controller
@RequestMapping("/System/InspectItemsManage")
public class InspectItemsManage extends CommonController implements ComFunc<InspectItemsForm>{

  @Autowired
  private InspectItemsService inspectitemsserv;





	//フォームクラス初期化
  @ModelAttribute
	public InspectItemsForm setUpForm_first(){
		return new InspectItemsForm();
	}

	@ModelAttribute
	public ComHistGetForm setUpForm_third(){
		return new ComHistGetForm();
	}

	@ModelAttribute
	public ComSearchForm setUpForm_fourth(){
		return new ComSearchForm();
	}





	
  /*************************************************************/
  /*   [selectHistroy]
  /*   検索ワード、ジャンルの指定を元に、該当する履歴情報を
  /*   一覧で出力する。
  /*************************************************************/
	@GetMapping("History")
	@Override
	public SubmitEntity<List<Map<String, String>>> selectHistroy(@Validated ComHistGetForm form, BindingResult bind_data) {

		setLoginUser();

		//フォームバリデーションチェック
		if(bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new ArrayList<Map<String, String>>(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			String search_id = form.getId();
			Date start = form.getStart_datetime();
			Date end = form.getEnd_datetime();

			//履歴情報を検索し、文字列化してリスト化。
			Iterable<Inspect_Items_History> hist = inspectitemsserv.selectHist(search_id, start, end);
			List<Map<String, String>> list = inspectitemsserv.mapPackingH(hist);

			return makeSuccessEntity(list);

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(InspectItemsManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{selectHistroy}", e);

			return makeExceptionEntity(new ArrayList<Map<String, String>>());
		}
	}






  /*************************************************************/
  /*   [rollbackHistroy]
  /*   指定された履歴情報を、本データにロールバックして
  /*   復元する。
  /*************************************************************/
	@PostMapping("Histroy")
	@Override
	public SubmitEntity<Object> rollbackHistroy(@RequestParam String history_id, BindingResult bind_data) {

		setLoginUser();
		Integer parse_id = intValidAndParse(history_id, bind_data);  //履歴番号は、一度取り込んだうえでバリデーション＆変換。

		//フォームバリデーションチェック
		if(parse_id == null || bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new Object(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			//ロールバック処理呼び出し
			inspectitemsserv.rollbackData(parse_id);
			return makeSuccessEntity(new Object());

		//参照制約違反による失敗に関しては、別途例外を投げる。
		}catch(ForeignMissException e){
			return makeErrorEntity(bind_data, new Object(), ErrorContentsEnum.ROLLBACK_ERROR);

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(InspectItemsManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{rollbackHistroy}", e);

			return makeExceptionEntity(new Object());
		}
	}






	/*************************************************************/
  /*   [selectData]
  /*   指定された点検シート番号の個別情報を取得する。
  /*************************************************************/
	@GetMapping("Data")
	@Override
	public SubmitEntity<Map<String, String>> selectData(@RequestParam String search_id, BindingResult bind_data) {

		setLoginUser();
		idValid(search_id, bind_data);  //点検シート番号は、一度取り込んだうえでバリデーション＆変換。

		//フォームバリデーションチェック
		if(bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			Optional<Inspect_Items> opt = inspectitemsserv.selectData(search_id);

			//検索結果の有無によって送信エンティティを分ける。
			if(opt.isPresent()){
				return makeSuccessEntity(opt.get().makeMap());
			}else{
				return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.SEARCH_EMPTY);
			}
			
		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(InspectItemsManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{selectData}", e);

			return makeExceptionEntity(new HashMap<String, String>());
		}
	}






  /*************************************************************/
  /*   [insertData]
  /*   入力されたフォームに従い、個別情報を新規追加する。
  /*************************************************************/
	@PostMapping("Data")
	@Override
	public SubmitEntity<Map<String, String>> insertData(@Validated InspectItemsForm form, BindingResult bind_data) {

		setLoginUser();

		//フォームバリデーションチェック
		if(bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			//該当点検シート番号が、まだ登録されていないことを確認する。
			String inspsheet_id = form.getInspsheet_id();

			if(!inspectitemsserv.existsData(inspsheet_id)){
				return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.UPDATE_ERROR);
			}

			//新規追加なので、シリアルナンバーは「Null」となる。
			Inspect_Items info = new Inspect_Items(form);
			info.setSerial_num(null);
			info = inspectitemsserv.changeData(info);

			//返却するデータは、保存済みのデータを使用する。（シリアルナンバー割り当て済み）
			return makeSuccessEntity(info.makeMap());

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(InspectItemsManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{insertData}", e);

			return makeExceptionEntity(new HashMap<String, String>());
		}
	}






  /*************************************************************/
  /*   [updateData]
  /*   入力されたフォームに従い、既存の個別情報を更新する。
  /*************************************************************/
	@PutMapping("Data")
	@Override
	public SubmitEntity<Map<String, String>> updateData(@Validated InspectItemsForm form, BindingResult bind_data) {

		setLoginUser();

		//フォームバリデーションチェック
		if(bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			//該当点検シート番号が重複しないことを確認する。
			String inspsheet_id = form.getInspsheet_id();
			Integer serial_num = form.getSerial_num();

			if(!inspectitemsserv.duplicateData(serial_num, inspsheet_id)){
				return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.UPDATE_ERROR);
			}

			//シリアルナンバーに変更なし。
			Inspect_Items info = new Inspect_Items(form);
			info = inspectitemsserv.changeData(info);

			return makeSuccessEntity(info.makeMap());

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(InspectItemsManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{updateData}", e);

			return makeExceptionEntity(new HashMap<String, String>());
		}
	}






  /*************************************************************/
  /*   [deleteData]
  /*   指定されたシリアルナンバーの個別データを削除する。
  /*************************************************************/
	@DeleteMapping("Data")
	@Override
	public SubmitEntity<Object> deleteData(@RequestParam String serial_num, BindingResult bind_data) {

		setLoginUser();
		Integer parse_id = intValidAndParse(serial_num, bind_data);  //シリアルナンバーは、一度取り込んだうえでバリデーション＆変換。

		//フォームバリデーションチェック
		if(parse_id == null || bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new Object(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			Boolean success = inspectitemsserv.deleteData(parse_id);  //削除実施
			
			//削除の成功可否によって送信エンティティを分ける。
			if(success){
				return makeSuccessEntity(new Object());
			}else{
				return makeErrorEntity(bind_data, new Object(), ErrorContentsEnum.DELETE_ERROR);
			}

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(InspectItemsManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{deleteData}", e);

			return makeExceptionEntity(new Object());
		}
	}







  /*************************************************************/
  /*   [selectBulkData]
  /*   現在保存中のデータを、検索ワード、ジャンルを元に
  /*   一覧で取得する。
  /*************************************************************/
	@GetMapping("BulkData")
	@Override
	public SubmitEntity<List<Map<String, String>>> selectBulkData(@Validated ComSearchForm form, BindingResult bind_data) {

		setLoginUser();

		//フォームバリデーションチェック
		if(bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new ArrayList<Map<String, String>>(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			String word = form.getWord();
			String subject = form.getSubject();
			Boolean order = form.getOrder();

			List<Inspect_Items> list = (ArrayList<Inspect_Items>)inspectitemsserv.selectAllData(word, subject, order);   //検索開始
			
			//検索結果の有無によって、送信エンティティを変更。
			if(list.size() != 0){
				return makeSuccessEntity(inspectitemsserv.mapPacking(list));
			}else{
				return makeErrorEntity(bind_data, new ArrayList<Map<String, String>>(), ErrorContentsEnum.SEARCH_EMPTY);
			}

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(InspectItemsManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{selectBulkData}", e);

			return makeExceptionEntity(new ArrayList<Map<String, String>>());
		}
	}






  /*************************************************************/
  /*   [outputBulkCsvData]
  /*   現在保存中のデータを、検索ワード、ジャンルを元に
  /*   一覧で取得し、そのデータをCSV形式にして出力する。
  /*************************************************************/
	@PostMapping("BulkData")
	@Override
	public SubmitEntity<String> outputBulkCsvData(@Validated ComSearchForm form, BindingResult bind_data) {

		setLoginUser();

		//フォームバリデーションチェック
		if(bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new String(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			String word = form.getWord();
			String subject = form.getSubject();
			Boolean order = form.getOrder();

			List<Inspect_Items> list = (ArrayList<Inspect_Items>)inspectitemsserv.selectAllData(word, subject, order);   //検索開始
			
			//検索結果の有無によって、送信エンティティを変更。
			if(list.size() != 0){
				return makeSuccessEntity(inspectitemsserv.outputCsv(list));  //CSV出力開始
			}else{
				return makeErrorEntity(bind_data, new String(), ErrorContentsEnum.SEARCH_EMPTY);
			}

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(InspectItemsManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{outputBulkCsvData}", e);

			return makeExceptionEntity(new String());
		}
	}






  /*************************************************************/
  /*   [outputPdfData]
  /*   指定されたシリアルナンバーの個別データを決められた
  /*   フォーマットのPDFに変換して出力する。
  /*************************************************************/
	@PostMapping("PdfData")
	@Override
	public SubmitEntity<String> outputPdfData(@RequestParam String serial_num, BindingResult bind_data) {

		setLoginUser();
		Integer parse_id = intValidAndParse(serial_num, bind_data);  //シリアルナンバーは、一度取り込んだうえでバリデーション＆変換。

		//フォームバリデーションチェック
		if(parse_id == null || bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new String(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			String pdf_data = inspectitemsserv.outputPdf(parse_id);  //PDF出力開始
			
			//出力結果の有無によって、送信エンティティを変更。
			if(pdf_data != null){
				return makeSuccessEntity(pdf_data);
			}else{
				return makeErrorEntity(bind_data, new String(), ErrorContentsEnum.SEARCH_EMPTY);
			}

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(InspectItemsManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{outputPdfData}", e);

			return makeExceptionEntity(new String());
		}
	}






  /*************************************************************/
  /*   [inputCsvData]
  /*   アップロードされたCSVファイルからデータを抽出し、
  /*   そのデータを個別情報としてデータベースに全て保存する。
  /*************************************************************/
	@PostMapping("CsvData")
	@Override
	public SubmitEntity<List<List<Integer>>> inputCsvData(@RequestParam MultipartFile csvfile, BindingResult bind_data) {

		try{
			List<List<Integer>> errorlist = inspectitemsserv.inputCsv(csvfile);  //CSV入力開始
			
			//エラーの有無によって、送信エンティティを変更。
			if(errorlist == null){
				return makeSuccessEntity(new ArrayList<List<Integer>>());
			}else{
				return makeErrorEntity(bind_data, errorlist, ErrorContentsEnum.VALIDATION_ERROR);
			}

		//ファイル不適合による失敗に関しては、別途例外を投げる。
		}catch(InputFileDifferException e){
			return makeErrorEntity(bind_data, new ArrayList<List<Integer>>(), ErrorContentsEnum.FILE_MISSMATCH);

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(InspectItemsManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{inputCsvData}", e);

			return makeExceptionEntity(new ArrayList<List<Integer>>());
		}
	}






  /*************************************************************/
  /*   [outputCsvTemplateData]
  /*   アップロードに必要なCSVファイルのテンプレートを
  /*   出力する。
  /*************************************************************/
	@GetMapping("CsvData")
	@Override
	public SubmitEntity<String> outputCsvTemplateData() {

		try{
			String template = inspectitemsserv.outputCsvTemplate();

			return makeSuccessEntity(template);

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(InspectItemsManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{outputCsvTemplateData}", e);

			return makeExceptionEntity(new String());
		}
	}
}