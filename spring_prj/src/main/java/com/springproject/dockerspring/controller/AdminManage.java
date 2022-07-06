package com.springproject.dockerspring.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.springproject.dockerspring.entity.NormalEntity.SysPass_Join;
import com.springproject.dockerspring.entity.NormalEntity.System_User;
import com.springproject.dockerspring.entity.NormalEntity.Usage_Authority;
import com.springproject.dockerspring.form.NormalForm.ComSearchForm;
import com.springproject.dockerspring.form.NormalForm.DelIndiviHistForm;
import com.springproject.dockerspring.form.NormalForm.SysUserForm;
import com.springproject.dockerspring.form.NormalForm.UsageAuthForm;
import com.springproject.dockerspring.service.AdminService;

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





/**************************************************************/
/*   [AdminManage]
/*   「管理者」に必要な処理を行う。
/**************************************************************/
@RestController
@RequestMapping("/System/AdminManage")
public class AdminManage extends CommonController{

	@Autowired
	private AdminService adminserv;





	//フォームクラス初期化
  @ModelAttribute
	public SysUserForm setUpForm_1(){
		return new SysUserForm();
	}

	@ModelAttribute
	public UsageAuthForm setUpForm_2(){
		return new UsageAuthForm();
	}

	@ModelAttribute
	public ComSearchForm setUpForm_3(){
		return new ComSearchForm();
	}

	@ModelAttribute
	public DelIndiviHistForm setUpForm_4(){
		return new DelIndiviHistForm();
	}






  /*************************************************************/
  /*   [deleteHistroy]
  /*   期間とテーブルの指定を行い、履歴情報の一括削除を行う。
  /*************************************************************/
	@DeleteMapping("DeleteHistory")
	public SubmitEntity<Object> deleteHistroy(@Validated DelIndiviHistForm form, BindingResult bind_data) {

		setLoginUser();

		//フォームバリデーションチェック
		if(bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new Object(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			String table = form.getTable_name();
			Date start = form.getStart_datetime();
			Date end = form.getEnd_datetime();

			//全削除が、テーブル指定削除か判別。
			if(table.equals("All")){
				adminserv.deleteHistory(start, end);
			}else{
				adminserv.deleteHistory(table, start, end);
			}

			return makeSuccessEntity(new Object());

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(AdminManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{selectHistroy}", e);

			return makeExceptionEntity(new Object());
		}
	}






  /*************************************************************/
  /*   [sysUserList]
  /*   検索ワードを元に、現在登録中のシステム利用者を
  /*   検索してリストを出力する。
  /*************************************************************/
	@GetMapping("SysUserList")
	public SubmitEntity<List<Map<String, String>>> sysUserList(@Validated ComSearchForm form, BindingResult bind_data) {

		setLoginUser();

		//フォームバリデーションチェック
		if(bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new ArrayList<Map<String, String>>(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			String word = form.getWord();
			String subject = form.getSubject();
			Boolean order = form.getOrder();

			List<SysPass_Join> list = (ArrayList<SysPass_Join>)adminserv.sysUserList(word, subject, order);

			//検索結果の有無によって送信エンティティを分ける。
			if(list.size() != 0){
				return makeSuccessEntity(adminserv.mapPackingS(list));
			}else{
				return makeErrorEntity(bind_data, new ArrayList<Map<String, String>>(), ErrorContentsEnum.SEARCH_EMPTY);
			}
			
		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(AdminManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{sysUserList}", e);

			return makeExceptionEntity(new ArrayList<Map<String, String>>());
		}
	}






	/*************************************************************/
  /*   [checkSysUser]
  /*   指定された団員番号の利用者情報を取得する。
  /*************************************************************/
	@GetMapping("SysUser")
	public SubmitEntity<Map<String, String>> checkSysUser(@RequestParam String member_id, BindingResult bind_data) {

		setLoginUser();
		idValid(member_id, bind_data);  //団員番号は、一度取り込んだうえでバリデーション＆変換。

		//フォームバリデーションチェック
		if(bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			Optional<SysPass_Join> opt = adminserv.checkSysUser(member_id);

			//検索結果の有無によって送信エンティティを分ける。
			if(opt.isPresent()){
				return makeSuccessEntity(opt.get().makeMap());
			}else{
				return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.SEARCH_EMPTY);
			}
			
		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(AdminManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{checkSysUser}", e);

			return makeExceptionEntity(new HashMap<String, String>());
		}
	}






  /*************************************************************/
  /*   [insertSysUser]
  /*   入力されたフォームに従い、システム利用者を新規追加する。
  /*************************************************************/
	@PostMapping("SysUser")
	public SubmitEntity<Map<String, String>> insertSysUser(@Validated SysUserForm form, BindingResult bind_data) {

		setLoginUser();

		//フォームバリデーションチェック
		if(bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			String member_id = form.getMember_id();
			
			//該当団員番号がまだ登録されていないか判別。
			if(!adminserv.existsSysUser(member_id)){
				return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.UPDATE_ERROR);
			}

			//新規追加扱いとする。
			System_User info = new System_User(form);
			info = adminserv.changeSysUser(info, true);

			//返却するデータは、保存済みのデータを使用する。（シリアルナンバー割り当て済み）
			return makeSuccessEntity(info.makeMap());

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(AdminManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{insertSysUser}", e);

			return makeExceptionEntity(new HashMap<String, String>());
		}
	}






  /*************************************************************/
  /*   [updateSysUser]
  /*   入力されたフォームに従い、既存システム利用者を更新する。
  /*************************************************************/
	@PutMapping("SysUser")
	public SubmitEntity<Map<String, String>> updateSysUser(@Validated SysUserForm form, BindingResult bind_data) {

		setLoginUser();

		//フォームバリデーションチェック
		if(bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			String member_id = form.getMember_id();
			
			//該当団員番号が登録されているか判別。
			if(adminserv.existsSysUser(member_id)){
				return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.UPDATE_ERROR);
			}

			//更新扱いとする。
			System_User info = new System_User(form);
			info = adminserv.changeSysUser(info, false);

			//返却するデータは、保存済みのデータを使用する。（シリアルナンバー割り当て済み）
			return makeSuccessEntity(info.makeMap());

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(AdminManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{updateSysUser}", e);

			return makeExceptionEntity(new HashMap<String, String>());
		}
	}






  /*************************************************************/
  /*   [deleteSysUser]
  /*   指定された団員番号のシステム利用者を削除する。
  /*************************************************************/
	@DeleteMapping("SysUser")
	public SubmitEntity<Object> deleteSysUser(@RequestParam String member_id, BindingResult bind_data) {

		setLoginUser();
		idValid(member_id, bind_data);  //団員番号は、一度取り込んだうえでバリデーション＆変換。

		//フォームバリデーションチェック
		if(bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new Object(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			adminserv.deleteSysUser(member_id);  //削除実施

			return makeSuccessEntity(new Object());

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(AdminManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{deleteData}", e);

			return makeExceptionEntity(new Object());
		}
	}







  /*************************************************************/
  /*   [authList]
  /*   検索ワードを元に、現在登録中の権限情報を
  /*   検索してリストを出力する。
  /*************************************************************/
	@GetMapping("authList")
	public SubmitEntity<List<Map<String, String>>> authList(@Validated ComSearchForm form, BindingResult bind_data) {

		setLoginUser();

		//フォームバリデーションチェック
		if(bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new ArrayList<Map<String, String>>(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			String word = form.getWord();
			String subject = form.getSubject();
			Boolean order = form.getOrder();

			List<Usage_Authority> list = (ArrayList<Usage_Authority>)adminserv.authList(word, subject, order);

			//検索結果の有無によって送信エンティティを分ける。
			if(list.size() != 0){
				return makeSuccessEntity(adminserv.mapPackingA(list));
			}else{
				return makeErrorEntity(bind_data, new ArrayList<Map<String, String>>(), ErrorContentsEnum.SEARCH_EMPTY);
			}
			
		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(AdminManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{authList}", e);

			return makeExceptionEntity(new ArrayList<Map<String, String>>());
		}
	}






	/*************************************************************/
  /*   [checkAuth]
  /*   指定された権限番号の利用者情報を取得する。
  /*************************************************************/
	@GetMapping("Auth")
	public SubmitEntity<Map<String, String>> checkAuth(@RequestParam String auth_id, BindingResult bind_data) {

		setLoginUser();
		idValid(auth_id, bind_data);  //団員番号は、一度取り込んだうえでバリデーション＆変換。

		//フォームバリデーションチェック
		if(bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			Optional<Usage_Authority> opt = adminserv.checkAuth(auth_id);

			//検索結果の有無によって送信エンティティを分ける。
			if(opt.isPresent()){
				return makeSuccessEntity(opt.get().makeMap());
			}else{
				return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.SEARCH_EMPTY);
			}
			
		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(AdminManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{checkAuth}", e);

			return makeExceptionEntity(new HashMap<String, String>());
		}
	}






  /*************************************************************/
  /*   [insertAuth]
  /*   入力されたフォームに従い、権限情報を新規追加する。
  /*************************************************************/
	@PostMapping("Auth")
	public SubmitEntity<Map<String, String>> insertAuth(@Validated UsageAuthForm form, BindingResult bind_data) {

		setLoginUser();

		//フォームバリデーションチェック
		if(bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			String auth_id = form.getAuth_id();
			
			//該当権限番号がまだ登録されていないか判別。
			if(!adminserv.existsAuth(auth_id)){
				return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.UPDATE_ERROR);
			}

			//新規追加扱いとする。
			Usage_Authority info = new Usage_Authority(form);
			info.setSerial_num(null);
			info = adminserv.changeAuth(info);

			//返却するデータは、保存済みのデータを使用する。（シリアルナンバー割り当て済み）
			return makeSuccessEntity(info.makeMap());

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(AdminManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{insertAuth}", e);

			return makeExceptionEntity(new HashMap<String, String>());
		}
	}






  /*************************************************************/
  /*   [updateAuth]
  /*   入力されたフォームに従い、既存権限情報を更新する。
  /*************************************************************/
	@PutMapping("Auth")
	public SubmitEntity<Map<String, String>> updateAuth(@Validated UsageAuthForm form, BindingResult bind_data) {

		setLoginUser();

		//フォームバリデーションチェック
		if(bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			String auth_id = form.getAuth_id();
			Integer serial_num = form.getSerial_num();
			
			//該当権限番号に重複が無いか確認。
			if(adminserv.duplicateAuth(serial_num, auth_id)){
				return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.UPDATE_ERROR);
			}

			//更新扱いとする。
			Usage_Authority info = new Usage_Authority(form);
			info = adminserv.changeAuth(info);

			//返却するデータは、保存済みのデータを使用する。（シリアルナンバー割り当て済み）
			return makeSuccessEntity(info.makeMap());

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(AdminManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{updateAuth}", e);

			return makeExceptionEntity(new HashMap<String, String>());
		}
	}






  /*************************************************************/
  /*   [deleteAuth]
  /*   指定された権限番号の情報を削除する。
  /*************************************************************/
	@DeleteMapping("Auth")
	public SubmitEntity<Object> deleteAuth(@RequestParam String serial_num, BindingResult bind_data) {

		setLoginUser();
		Integer parse_id = intValidAndParse(serial_num, bind_data);  //シリアルナンバーは、一度取り込んだうえでバリデーション＆変換。

		//フォームバリデーションチェック
		if(parse_id == null || bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new Object(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			Boolean success = adminserv.deleteAuth(parse_id);  //削除実施

			//削除の成功可否によって送信エンティティを分ける。
			if(success){
				return makeSuccessEntity(new Object());
			}else{
				return makeErrorEntity(bind_data, new Object(), ErrorContentsEnum.DELETE_ERROR);
			}

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(AdminManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{deleteAuth}", e);

			return makeExceptionEntity(new Object());
		}
	}
}
