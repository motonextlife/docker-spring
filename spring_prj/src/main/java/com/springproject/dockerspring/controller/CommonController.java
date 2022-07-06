package com.springproject.dockerspring.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.springproject.dockerspring.form.RegexEnum;
import com.springproject.dockerspring.security.GetLoginUser;
import com.springproject.dockerspring.service.ListOrderEnum;






public abstract class CommonController extends GetLoginUser{

	protected String common_role;  //ログインユーザーロール名
	protected String common_username;  //ログインユーザー名





	protected void setLoginUser(){

		String[] userinfo = getLoginUser();
		this.common_username = userinfo[0];
		this.common_role = userinfo[1];
	}






	protected Integer intValidAndParse(String str_int, BindingResult bind_data){

		try{
			if(!str_int.matches(RegexEnum.DIGIT.getRegex()) || str_int.isEmpty() || str_int.isBlank()){
				bind_data.addError(new FieldError("history_id", "history_id", "input miss"));
				bind_data.addError(new FieldError("serial_num", "serial_num", "input miss"));
			}

			return Integer.parseInt(str_int);

		}catch(NumberFormatException e){
			return null;
		}
	}






	protected void idValid(String str_id, BindingResult bind_data){

		if(!str_id.matches(RegexEnum.ALNUM_NOTNULL.getRegex())){
			bind_data.addError(new FieldError("search_id", "search_id", "input miss"));
			bind_data.addError(new FieldError("list_id", "list_id", "input miss"));
		}
	}






	protected ListOrderEnum orderValidAndParse(String order, BindingResult bind_data){

    /**********************************************************************/
    /* 列挙型「ListOrderEnum」を用いて、引数の検索ジャンルが列挙型内に
    /* 定義されているものか判定。
    /* 無ければ、不正な値が渡されたと判断し、「null」を返す。
    /**********************************************************************/
    Optional<ListOrderEnum> name = Arrays.stream(ListOrderEnum.values())
                                         .parallel()
                                         .filter(s -> s.name().equals(order))
                                         .findFirst();

		if(name.isEmpty()){
			bind_data.addError(new FieldError("order", "order", "input miss"));
			return null;
		}

		switch(name.get()){
			case UPLOAD_DATE_ASC:
				return ListOrderEnum.UPLOAD_DATE_ASC;
			case UPLOAD_DATE_DESC:
				return ListOrderEnum.UPLOAD_DATE_DESC;
			case ID_ASC:
				return ListOrderEnum.ID_ASC;
			case ID_DESC:
				return ListOrderEnum.ID_DESC;
			case SHEET_ASC:
				return ListOrderEnum.SHEET_ASC;
			case SHEET_DESC:
				return ListOrderEnum.SHEET_DESC;	
			default:
				return null;
		}
	}






	protected <T> SubmitEntity<T> makeErrorEntity(BindingResult bind_data, T datatype, ErrorContentsEnum errortype){

		List<String> errorlist = bind_data.getFieldErrors()
																			.stream()
																			.parallel()
																			.map(s -> s.getField())
																			.collect(Collectors.toList());

		return new SubmitEntity<T>(this.common_username, 
															 this.common_role, 
															 false, 
															 true, 
															 errortype, 
															 errorlist, 
															 datatype);
	}






	protected <T> SubmitEntity<T> makeExceptionEntity(T datatype){

		return new SubmitEntity<T>(this.common_username, 
															 this.common_role, 
															 true, 
															 false, 
															 null, 
															 null, 
															 null);
	}






	protected <T> SubmitEntity<T> makeSuccessEntity(T submit_data){

		return new SubmitEntity<T>(this.common_username, 
															 this.common_role, 
															 false, 
															 false, 
															 null, 
															 null, 
															 submit_data);
	}
}