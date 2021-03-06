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
/*   ??????????????????????????????????????????????????????
/**************************************************************/
@RestController
@RequestMapping("/System/RecEvalManage")
public class RecEvalManage extends CommonController implements ComFunc<RecEvalForm>, ComListFunc{

	@Autowired
  private RecEvalService recevalserv;




	//??????????????????????????????
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
  /*   ??????????????????????????????????????????????????????????????????????????????
  /*   ????????????????????????
  /*************************************************************/
	@GetMapping("History")
	@Override
	public SubmitEntity<List<Map<String, String>>> selectHistroy(@Validated ComHistGetForm form, BindingResult bind_data) {

		setLoginUser();

		//?????????????????????????????????????????????
		if(bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new ArrayList<Map<String, String>>(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			String search_id = form.getId();
			Date start = form.getStart_datetime();
			Date end = form.getEnd_datetime();

			//????????????????????????????????????????????????????????????
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
  /*   ????????????????????????????????????????????????????????????????????????
  /*   ???????????????
  /*************************************************************/
	@PostMapping("Histroy")
	@Override
	public SubmitEntity<Object> rollbackHistroy(@RequestParam String history_id, BindingResult bind_data) {

		setLoginUser();
		Integer parse_id = intValidAndParse(history_id, bind_data);  //?????????????????????????????????????????????????????????????????????????????????

		//?????????????????????????????????????????????
		if(parse_id == null || bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new Object(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			//????????????????????????????????????
			recevalserv.rollbackData(parse_id);
			return makeSuccessEntity(new Object());

		//??????????????????????????????????????????????????????????????????????????????
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
  /*   ??????????????????????????????????????????????????????????????????
  /*************************************************************/
	@GetMapping("Data")
	@Override
	public SubmitEntity<Map<String, String>> selectData(@RequestParam String search_id, BindingResult bind_data) {

		setLoginUser();
		idValid(search_id, bind_data);  //???????????????????????????????????????????????????????????????????????????????????????

		//?????????????????????????????????????????????
		if(bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			Optional<Rec_Eval> opt = recevalserv.selectData(search_id);

			//????????????????????????????????????????????????????????????????????????
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
  /*   ???????????????????????????????????????????????????????????????????????????
  /*************************************************************/
	@PostMapping("Data")
	@Override
	public SubmitEntity<Map<String, String>> insertData(@Validated RecEvalForm form, BindingResult bind_data) {

		setLoginUser();

		//?????????????????????????????????????????????
		if(bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			//????????????????????????????????????????????????????????????????????????????????????
			String eval_id = form.getEval_id();

			if(!recevalserv.existsData(eval_id)){
				return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.UPDATE_ERROR);
			}

			//??????????????????????????????????????????????????????Null???????????????
			Rec_Eval info = new Rec_Eval(form);
			info.setSerial_num(null);
			info = recevalserv.changeData(info);

			//?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
			return makeSuccessEntity(info.makeMap());

		}catch(Exception e){
			Logger log = LoggerFactory.getLogger(RecEvalManage.class);
			log.error("System Error --- Username{" + super.common_username + "} Method{insertData}", e);

			return makeExceptionEntity(new HashMap<String, String>());
		}
	}






  /*************************************************************/
  /*   [updateData]
  /*   ??????????????????????????????????????????????????????????????????????????????
  /*************************************************************/
	@PutMapping("Data")
	@Override
	public SubmitEntity<Map<String, String>> updateData(@Validated RecEvalForm form, BindingResult bind_data) {

		setLoginUser();

		//?????????????????????????????????????????????
		if(bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			//??????????????????????????????????????????????????????????????????
			String eval_id = form.getEval_id();
			Integer serial_num = form.getSerial_num();

			if(!recevalserv.duplicateData(serial_num, eval_id)){
				return makeErrorEntity(bind_data, new HashMap<String, String>(), ErrorContentsEnum.UPDATE_ERROR);
			}

			//??????????????????????????????????????????
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
  /*   ???????????????????????????????????????????????????????????????????????????
  /*************************************************************/
	@DeleteMapping("Data")
	@Override
	public SubmitEntity<Object> deleteData(@RequestParam String serial_num, BindingResult bind_data) {

		setLoginUser();
		Integer parse_id = intValidAndParse(serial_num, bind_data);  //?????????????????????????????????????????????????????????????????????????????????????????????

		//?????????????????????????????????????????????
		if(parse_id == null || bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new Object(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			Boolean success = recevalserv.deleteData(parse_id);  //????????????
			
			//????????????????????????????????????????????????????????????????????????
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
  /*   ????????????????????????????????????????????????????????????????????????
  /*   ????????????????????????
  /*************************************************************/
	@GetMapping("BulkData")
	@Override
	public SubmitEntity<List<Map<String, String>>> selectBulkData(@Validated ComSearchForm form, BindingResult bind_data) {

		setLoginUser();

		//?????????????????????????????????????????????
		if(bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new ArrayList<Map<String, String>>(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			String word = form.getWord();
			String subject = form.getSubject();
			Boolean order = form.getOrder();

			List<Rec_Eval> list = (ArrayList<Rec_Eval>)recevalserv.selectAllData(word, subject, order);   //????????????
			
			//????????????????????????????????????????????????????????????????????????
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
  /*   ????????????????????????????????????????????????????????????????????????
  /*   ???????????????????????????????????????CSV??????????????????????????????
  /*************************************************************/
	@PostMapping("BulkData")
	@Override
	public SubmitEntity<String> outputBulkCsvData(@Validated ComSearchForm form, BindingResult bind_data) {

		setLoginUser();

		//?????????????????????????????????????????????
		if(bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new String(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			String word = form.getWord();
			String subject = form.getSubject();
			Boolean order = form.getOrder();

			List<Rec_Eval> list = (ArrayList<Rec_Eval>)recevalserv.selectAllData(word, subject, order);   //????????????
			
			//????????????????????????????????????????????????????????????????????????
			if(list.size() != 0){
				return makeSuccessEntity(recevalserv.outputCsv(list));  //CSV????????????
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
  /*   ???????????????????????????????????????????????????????????????????????????
  /*   ?????????????????????PDF??????????????????????????????
  /*************************************************************/
	@PostMapping("PdfData")
	@Override
	public SubmitEntity<String> outputPdfData(@RequestParam String serial_num, BindingResult bind_data) {

		setLoginUser();
		Integer parse_id = intValidAndParse(serial_num, bind_data);  //?????????????????????????????????????????????????????????????????????????????????????????????

		//?????????????????????????????????????????????
		if(parse_id == null || bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new String(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			String pdf_data = recevalserv.outputPdf(parse_id);  //PDF????????????
			
			//????????????????????????????????????????????????????????????????????????
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
  /*   ???????????????????????????CSV??????????????????????????????????????????
  /*   ?????????????????????????????????????????????????????????????????????????????????
  /*************************************************************/
	@PostMapping("CsvData")
	@Override
	public SubmitEntity<List<List<Integer>>> inputCsvData(@RequestParam MultipartFile csvfile, BindingResult bind_data) {

		try{
			List<List<Integer>> errorlist = recevalserv.inputCsv(csvfile);  //CSV????????????
			
			//?????????????????????????????????????????????????????????????????????
			if(errorlist == null){
				return makeSuccessEntity(new ArrayList<List<Integer>>());
			}else{
				return makeErrorEntity(bind_data, errorlist, ErrorContentsEnum.VALIDATION_ERROR);
			}

		//?????????????????????????????????????????????????????????????????????????????????
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
  /*   ??????????????????????????????CSV????????????????????????????????????
  /*   ???????????????
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
  /*   ??????????????????????????????????????????????????????????????????
  /*   ????????????????????????
  /*************************************************************/
	@GetMapping("List")
	@Override
	public SubmitEntity<List<Map<String, String>>> selectListData(String list_id, String order, BindingResult bind_data) {

		setLoginUser();
		idValid(list_id, bind_data);  //???????????????????????????????????????????????????????????????????????????????????????
		ListOrderEnum orderenum = orderValidAndParse(order, bind_data);  //???????????????????????????????????????????????????????????????????????????????????????

		//?????????????????????????????????????????????
		if(orderenum == null || bind_data.hasErrors()){
			return makeErrorEntity(bind_data, new ArrayList<Map<String, String>>(), ErrorContentsEnum.VALIDATION_ERROR);
		}

		try{
			List<Rec_Eval> list = (ArrayList<Rec_Eval>)recevalserv.selectList(list_id, orderenum);

			//????????????????????????????????????????????????????????????????????????
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