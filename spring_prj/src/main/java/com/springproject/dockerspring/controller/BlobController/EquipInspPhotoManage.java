package com.springproject.dockerspring.controller.BlobController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.springproject.dockerspring.component.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.controller.ComBlobFunc;
import com.springproject.dockerspring.controller.CommonController;
import com.springproject.dockerspring.controller.ErrorContentsEnum;
import com.springproject.dockerspring.controller.SubmitEntity;
import com.springproject.dockerspring.entity.HistoryEntity.Equip_Inspect_Photo_History;
import com.springproject.dockerspring.entity.NormalEntity.Equip_Inspect_Photo;
import com.springproject.dockerspring.form.BlobImplForm.EquipInspPhotoForm;
import com.springproject.dockerspring.form.NormalForm.ComHistGetForm;
import com.springproject.dockerspring.form.NormalForm.ComSearchForm;
import com.springproject.dockerspring.service.BlobServiceInterface.EquipInspPhotoService;
import com.springproject.dockerspring.service.OriginalException.ForeignMissException;
import com.springproject.dockerspring.service.OriginalException.OverCountException;
import com.springproject.dockerspring.service.ServiceInterface.EquipInspectService;

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
/*   [EquipInspPhotoManage]
/*   「点検データ管理」に必要な処理を行う。
/**************************************************************/
@RestController
@RequestMapping("/System/EquipInspPhotoManage")
public class EquipInspPhotoManage extends CommonController implements ComBlobFunc<EquipInspPhotoForm>{

  @Autowired
  private EquipInspPhotoService photoserv;

	@Autowired
  private EquipInspectService equipinspserv;




	//フォームクラス初期化
  @ModelAttribute
	public EquipInspPhotoForm setUpForm_first(){
		return new EquipInspPhotoForm();
	}

	@ModelAttribute
	public ComHistGetForm setUpForm_second(){
		return new ComHistGetForm();
	}

	@ModelAttribute
	public ComSearchForm setUpForm_third(){
		return new ComSearchForm();
	}





  /*************************************************************/
  /*   [selectBlobHistroy]
  /*   検索ワード、ジャンルの指定を元に、該当する履歴情報を
  /*   一覧で出力する。但し、バイナリデータはこの処理では
  /*   出力しない。
  /*************************************************************/
	@GetMapping("BlobHistory")
	@Override
	public SubmitEntity<List<Map<String, String>>> selectBlobHistroy(@Validated ComHistGetForm form, BindingResult bind_data) {

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
			Iterable<Equip_Inspect_Photo_History> hist = photoserv.selectHist(search_id, start, end);
			List<Map<String, String>> list = photoserv.mapPackingH(hist);

			return makeSuccessEntity(list);

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(EquipInspPhotoManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{selectBlobHistroy}", e);

			return makeExceptionEntity(new ArrayList<Map<String, String>>());
		}
	}






  /*************************************************************/
  /*   [getHistroyBlob]
  /*   指定された履歴番号のバイナリデータを出力する。
  /*************************************************************/
	@PostMapping("BlobHistory")
	@Override
	public SubmitEntity<Map<String, String>> getHistroyBlob(@RequestParam String history_id, BindingResult bind_data) {

		setLoginUser();
		Integer parse_id = intValidAndParse(history_id, bind_data);  //履歴番号は、一度取り込んだうえでバリデーション＆変換。

		//フォームバリデーションチェック
		if(parse_id == null || bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			Optional<Equip_Inspect_Photo_History> opt =  photoserv.selectHistBlobData(parse_id);

			//検索結果の有無によって送信エンティティを分ける。
			if(opt.isPresent()){
				return makeSuccessEntity(opt.get().makeMap());
			}else{
				return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.SEARCH_EMPTY);
			}
		
		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(EquipInspPhotoManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{getHistroyBlob}", e);

			return makeExceptionEntity(new HashMap<String, String>());
		}
	}






  /*************************************************************/
  /*   [rollbackBlobHistroy]
  /*   指定された履歴情報を、本データにロールバックして
  /*   復元する。
  /*************************************************************/
	@PutMapping("BlobHistory")
	@Override
	public SubmitEntity<Object> rollbackBlobHistroy(@RequestParam String history_id, BindingResult bind_data) {

		setLoginUser();
		Integer parse_id = intValidAndParse(history_id, bind_data);  //履歴番号は、一度取り込んだうえでバリデーション＆変換。

		//フォームバリデーションチェック
		if(parse_id == null || bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new Object(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			//ロールバック処理呼び出し
			photoserv.rollbackData(parse_id);
			return makeSuccessEntity(new Object());

		//参照制約違反による失敗に関しては、別途例外を投げる。
		}catch(ForeignMissException e){
			return makeErrorEntity(bind_data, new Object(), ErrorContentsEnum.ROLLBACK_ERROR);

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(EquipInspPhotoManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{rollbackBlobHistroy}", e);

			return makeExceptionEntity(new Object());
		}
	}






  /*************************************************************/
  /*   [selectBlobData]
  /*   指定された点検データ番号の情報をすべて取得する。
  /*************************************************************/
	@GetMapping("BlobData")
	@Override
	public SubmitEntity<List<Map<String, String>>> selectBlobData(@RequestParam String search_id, BindingResult bind_data) {

		setLoginUser();
		idValid(search_id, bind_data);  //団員番号は、一度取り込んだうえでバリデーション＆変換。

		//フォームバリデーションチェック
		if(bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new ArrayList<Map<String, String>>(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			List<Equip_Inspect_Photo> list = (ArrayList<Equip_Inspect_Photo>)photoserv.selectData(search_id);

			//検索結果の有無によって送信エンティティを分ける。
			if(list.size() != 0){
				return makeSuccessEntity(photoserv.mapPacking(list));
			}else{
				return makeErrorEntity(bind_data, new ArrayList<Map<String, String>>(), ErrorContentsEnum.SEARCH_EMPTY);
			}
			
		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(EquipInspPhotoManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{selectBlobData}", e);

			return makeExceptionEntity(new ArrayList<Map<String, String>>());
		}
	}





  /*************************************************************/
  /*   [insertBlobData]
  /*   入力されたフォームに従い、個別情報を新規追加する。
  /*************************************************************/
	@PostMapping("BlobData")
	@Override
	public SubmitEntity<List<Map<String, String>>> insertBlobData(@Validated EquipInspPhotoForm form, BindingResult bind_data) {

		setLoginUser();

		//フォームバリデーションチェック
		if(bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new ArrayList<Map<String, String>>(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			String inspect_id = form.getInspect_id();

			//既に該当点検番号が存在するか、又は、参照元のテーブルに点検番号が存在するか。
			if(!photoserv.existsData(inspect_id) && !equipinspserv.existsData(inspect_id)){
				return makeErrorEntity(bind_data, new ArrayList<Map<String, String>>(), ErrorContentsEnum.UPDATE_ERROR);
			}
			
			//新規追加なので、シリアルナンバーは「Null」となる。
			List<Equip_Inspect_Photo> list = photoserv.blobPacking(form);
			list.stream().forEach(s -> s.setSerial_num(null));
			list = (List<Equip_Inspect_Photo>)photoserv.changeData(list);

			//該当点検番号のデータが、規定数以内に収まっているか判別。
			if(!photoserv.checkMaxLimitData(inspect_id, list.size())){
				return makeErrorEntity(bind_data, new ArrayList<Map<String, String>>(), ErrorContentsEnum.BLOB_OVER_COUNT);
			}

			//返却するデータは、保存済みのデータを使用する。（シリアルナンバー割り当て済み）
			return makeSuccessEntity(photoserv.mapPacking(list));

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(EquipInspPhotoManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{insertBlobData}", e);

			return makeExceptionEntity(new ArrayList<Map<String, String>>());
		}
	}





  /*************************************************************/
  /*   [updateBlobData]
  /*   入力されたフォームに従い、既存の個別情報を更新する。
  /*************************************************************/
	@PutMapping("BlobData")
	@Override
	public SubmitEntity<Map<String, String>> updateBlobData(@Validated EquipInspPhotoForm form, BindingResult bind_data) {

		setLoginUser();

		//フォームバリデーションチェック
		if(bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			String inspect_id = form.getInspect_id();
			Integer serial_num = form.getSerial_num();

			//入力された点検番号とシリアルナンバーのペアが存在するか判定。
			//番号の改ざんにより、データ数のオーバフローを防ぐため。
			if(!photoserv.existsPair(serial_num, inspect_id)){
				return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.UPDATE_ERROR);
			}

			//シリアルナンバーに変更なし。保存するの最初の一つだけ。
			Equip_Inspect_Photo entity = new Equip_Inspect_Photo(form, 1);
			List<Equip_Inspect_Photo> list = new ArrayList<>(Arrays.asList(entity));
			list = (List<Equip_Inspect_Photo>)photoserv.changeData(list);

			//返却するデータは、保存済みのデータを使用する。（シリアルナンバー割り当て済み）
			//返却するデータは、最初の一つだけ。
			return makeSuccessEntity(list.get(0).makeMap());

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(EquipInspPhotoManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{updateBlobData}", e);

			return makeExceptionEntity(new HashMap<String, String>());
		}
	}






  /*************************************************************/
  /*   [deleteBlobData]
  /*   指定されたシリアルナンバーの個別データを削除する。
  /*************************************************************/
	@DeleteMapping("BlobData")
	@Override
	public SubmitEntity<Object> deleteBlobData(@RequestParam String serial_num, BindingResult bind_data) {

		setLoginUser();
		Integer parse_id = intValidAndParse(serial_num, bind_data);  //シリアルナンバーは、一度取り込んだうえでバリデーション＆変換。

		//フォームバリデーションチェック
		if(parse_id == null || bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new Object(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			photoserv.deleteData(parse_id);  //削除実施
			
			return makeSuccessEntity(new Object());

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(EquipInspPhotoManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{deleteBlobData}", e);

			return makeExceptionEntity(new Object());
		}
	}






  /*************************************************************/
  /*   [outputZipData]
  /*   指定された点検番号のバイナリデータをZIPファイルに入れ、
  /*   圧縮したものを出力する。
  /*************************************************************/
	@GetMapping("ZipData")
	@Override
	public SubmitEntity<String> outputZipData(@RequestParam String search_id, BindingResult bind_data) {

		setLoginUser();
		idValid(search_id, bind_data);  //点検番号は、一度取り込んだうえでバリデーション＆変換。

		//フォームバリデーションチェック
		if(bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new String(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			List<Equip_Inspect_Photo> list = (ArrayList<Equip_Inspect_Photo>)photoserv.selectData(search_id);
			
			//検索結果の有無によって、送信エンティティを変更。
			if(list.size() != 0){
				return makeSuccessEntity(photoserv.outputZip(list));  //ZIP出力開始
			}else{
				return makeErrorEntity(bind_data, new String(), ErrorContentsEnum.SEARCH_EMPTY);
			}

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(EquipInspPhotoManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{outputZipData}", e);

			return makeExceptionEntity(new String());
		}
	}






  /*************************************************************/
  /*   [inputZipData]
  /*   アップロードされたZIPファイルからデータを抽出し、
  /*   そのデータを個別情報としてデータベースに全て保存する。
  /*************************************************************/
	@PostMapping("ZipData")
	@Override
	public SubmitEntity<Object> inputZipData(@RequestParam String search_id, @RequestParam MultipartFile zipfile, BindingResult bind_data) {

		setLoginUser();
		idValid(search_id, bind_data);  //点検番号は、一度取り込んだうえでバリデーション＆変換。

		//フォームバリデーションチェック
		if(bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new Object(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			photoserv.inputZip(search_id, zipfile);  //ZIP入力開始
			
			return makeSuccessEntity(new Object());

		//ファイル数超過による失敗に関しては、別途例外を投げる。
		}catch(OverCountException e){
			return makeErrorEntity(bind_data, new Object(), ErrorContentsEnum.BLOB_OVER_COUNT);
	
		//ファイル不適合による失敗に関しては、別途例外を投げる。
		}catch(InputFileDifferException e){
			return makeErrorEntity(bind_data, new Object(), ErrorContentsEnum.FILE_MISSMATCH);

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(EquipInspPhotoManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{inputZipData}", e);

			return makeExceptionEntity(new Object());
		}
	}
}