package com.springproject.dockerspring.controller.NormalController;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.springproject.dockerspring.component.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.controller.ComFunc;
import com.springproject.dockerspring.controller.ComListFunc;
import com.springproject.dockerspring.controller.CommonController;
import com.springproject.dockerspring.controller.ErrorContentsEnum;
import com.springproject.dockerspring.controller.SubmitEntity;
import com.springproject.dockerspring.entity.HistoryEntity.Rec_Eval_History;
import com.springproject.dockerspring.entity.NormalEntity.Rec_Eval;
import com.springproject.dockerspring.form.CsvImplForm.RecEvalForm;
import com.springproject.dockerspring.form.NormalForm.ComHistGetForm;
import com.springproject.dockerspring.form.NormalForm.ComSearchForm;
import com.springproject.dockerspring.service.ListOrderEnum;
import com.springproject.dockerspring.service.OriginalException.ForeignMissException;
import com.springproject.dockerspring.service.ServiceInterface.RecEvalService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;





/**************************************************************/
/*   [RecEvalManage]
/*   「採用考課管理」に必要な処理を行う。
/**************************************************************/
@RestController
@RequestMapping("/System/RecEvalManage")
public class RecEvalManage extends CommonController implements ComFunc<RecEvalForm>, ComListFunc{

	@Autowired
  private RecEvalService recevalserv;




	//フォームクラス初期化
	@ModelAttribute
	public RecEvalForm setUpForm_second(){
		return new RecEvalForm();
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
			Iterable<Rec_Eval_History> hist = recevalserv.selectHist(search_id, start, end);
			List<Map<String, String>> list = recevalserv.mapPackingH(hist);

			return makeSuccessEntity(list);

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(RecEvalManage.class);
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
			recevalserv.rollbackData(parse_id);
			return makeSuccessEntity(new Object());

		//参照制約違反による失敗に関しては、別途例外を投げる。
		}catch(ForeignMissException e){
			return makeErrorEntity(bind_data, new Object(), ErrorContentsEnum.ROLLBACK_ERROR);

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(RecEvalManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{rollbackHistroy}", e);

			return makeExceptionEntity(new Object());
		}
	}






	/*************************************************************/
  /*   [selectData]
  /*   指定された採用考課番号の個別情報を取得する。
  /*************************************************************/
	@GetMapping("Data")
	@Override
	public SubmitEntity<Map<String, String>> selectData(@RequestParam String search_id, BindingResult bind_data) {

		setLoginUser();
		idValid(search_id, bind_data);  //採用考課番号は、一度取り込んだうえでバリデーション＆変換。

		//フォームバリデーションチェック
		if(bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			Optional<Rec_Eval> opt = recevalserv.selectData(search_id);

			//検索結果の有無によって送信エンティティを分ける。
			if(opt.isPresent()){
				return makeSuccessEntity(opt.get().makeMap());
			}else{
				return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.SEARCH_EMPTY);
			}
			
		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(RecEvalManage.class);
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
	public SubmitEntity<Map<String, String>> insertData(@Validated RecEvalForm form, BindingResult bind_data) {

		setLoginUser();

		//フォームバリデーションチェック
		if(bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			//該当採用考課番号が、まだ登録されていないことを確認する。
			String eval_id = form.getEval_id();

			if(!recevalserv.existsData(eval_id)){
				return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.UPDATE_ERROR);
			}

			//新規追加なので、シリアルナンバーは「Null」となる。
			Rec_Eval info = new Rec_Eval(form);
			info.setSerial_num(null);
			info = recevalserv.changeData(info);

			//返却するデータは、保存済みのデータを使用する。（シリアルナンバー割り当て済み）
			return makeSuccessEntity(info.makeMap());

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(RecEvalManage.class);
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
	public SubmitEntity<Map<String, String>> updateData(@Validated RecEvalForm form, BindingResult bind_data) {

		setLoginUser();

		//フォームバリデーションチェック
		if(bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			//該当採用考課番号が重複しないことを確認する。
			String eval_id = form.getEval_id();
			Integer serial_num = form.getSerial_num();

			if(!recevalserv.duplicateData(serial_num, eval_id)){
				return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.UPDATE_ERROR);
			}

			//シリアルナンバーに変更なし。
			Rec_Eval info = new Rec_Eval(form);
			info = recevalserv.changeData(info);

			return makeSuccessEntity(info.makeMap());

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(RecEvalManage.class);
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
			Boolean success = recevalserv.deleteData(parse_id);  //削除実施
			
			//削除の成功可否によって送信エンティティを分ける。
			if(success){
				return makeSuccessEntity(new Object());
			}else{
				return makeErrorEntity(bind_data, new Object(), ErrorContentsEnum.DELETE_ERROR);
			}

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(RecEvalManage.class);
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

			List<Rec_Eval> list = (ArrayList<Rec_Eval>)recevalserv.selectAllData(word, subject, order);   //検索開始
			
			//検索結果の有無によって、送信エンティティを変更。
			if(list.size() != 0){
				return makeSuccessEntity(recevalserv.mapPacking(list));
			}else{
				return makeErrorEntity(bind_data, new ArrayList<Map<String, String>>(), ErrorContentsEnum.SEARCH_EMPTY);
			}

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(RecEvalManage.class);
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

			List<Rec_Eval> list = (ArrayList<Rec_Eval>)recevalserv.selectAllData(word, subject, order);   //検索開始
			
			//検索結果の有無によって、送信エンティティを変更。
			if(list.size() != 0){
				return makeSuccessEntity(recevalserv.outputCsv(list));  //CSV出力開始
			}else{
				return makeErrorEntity(bind_data, new String(), ErrorContentsEnum.SEARCH_EMPTY);
			}

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(RecEvalManage.class);
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
			String pdf_data = recevalserv.outputPdf(parse_id);  //PDF出力開始
			
			//出力結果の有無によって、送信エンティティを変更。
			if(pdf_data != null){
				return makeSuccessEntity(pdf_data);
			}else{
				return makeErrorEntity(bind_data, new String(), ErrorContentsEnum.SEARCH_EMPTY);
			}

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(RecEvalManage.class);
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
			List<List<Integer>> errorlist = recevalserv.inputCsv(csvfile);  //CSV入力開始
			
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
			Logger log = LoggerFactory.getLogger(RecEvalManage.class);
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
			String template = recevalserv.outputCsvTemplate();

			return makeSuccessEntity(template);

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(RecEvalManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{outputCsvTemplateData}", e);

			return makeExceptionEntity(new String());
		}
	}






  /*************************************************************/
  /*   [selectListData]
  /*   団員番号を元に、現在登録されている評価情報を
  /*   一覧で取得する。
  /*************************************************************/
	@GetMapping("List")
	@Override
	public SubmitEntity<List<Map<String, String>>> selectListData(String list_id, String order, BindingResult bind_data) {

		setLoginUser();
		idValid(list_id, bind_data);  //採用考課番号は、一度取り込んだうえでバリデーション＆変換。
		ListOrderEnum orderenum = orderValidAndParse(order, bind_data);  //並び順文字列は、一度取り込んだうえでバリデーション＆変換。

		//フォームバリデーションチェック
		if(orderenum == null || bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new ArrayList<Map<String, String>>(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			List<Rec_Eval> list = (ArrayList<Rec_Eval>)recevalserv.selectList(list_id, orderenum);

			//検索結果の有無によって送信エンティティを分ける。
			if(list.size() != 0){
				return makeSuccessEntity(recevalserv.mapPacking(list));
			}else{
				return makeErrorEntity(bind_data, new ArrayList<Map<String, String>>(), ErrorContentsEnum.SEARCH_EMPTY);
			}
			
		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(RecEvalManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{selectListData}", e);

			return makeExceptionEntity(new ArrayList<Map<String, String>>());
		}
	}
}