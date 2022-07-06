package com.springproject.dockerspring.authaspect;


import com.springproject.dockerspring.commonenum.Usage_Authority_Enum;
import com.springproject.dockerspring.service.AdminService;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;





/**************************************************************/
/*   [Common_Aspect]
/*   権限判断のAOPで共通で用いる処理を定義する。
/**************************************************************/
public abstract class CommonAspect {

	@Autowired
	AdminService adminserv;





  /*******************************************/
  /*   [checkAuth]
  /*   判断対処の権限名を受け取り、ログイン中の
  /*   ユーザーにその権限があるか判別。
  /*******************************************/
	protected Object checkAuth(ProceedingJoinPoint jp, Usage_Authority_Enum authname){

		Object returnObj = null;

		try{
			if(adminserv.judgeUserAuth(authname)){
				returnObj = jp.proceed();  //次の処理続行
			}else{
				returnObj = new SubmitAuthNgEntity(authname, false);  //権限不足によるエラー送信
			}
		}catch(Throwable e){
			Logger log = LoggerFactory.getLogger(CommonAspect.class);
			log.error("System Error --- AuthName{" + authname.name() + "} Method{checkAuth}", e);

			returnObj = new SubmitAuthNgEntity(authname, true);  //例外の際もアクセスを禁止する
		}

		return returnObj;
	}

}