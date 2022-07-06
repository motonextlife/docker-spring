package com.springproject.dockerspring.authaspect.AspectClass;



import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.springproject.dockerspring.authaspect.CommonAspect;
import com.springproject.dockerspring.commonenum.Usage_Authority_Enum;




/**************************************************************/
/*   [AdminAspect]
/*   「管理者機能」への、権限判断を行う。
/**************************************************************/
@Component
@Aspect
public class AdminAspect extends CommonAspect{

	/*************************************************************/
  /*   [adminAuthCheck]
  /*   管理者関連のメソッドすべてに適用される。
  /*************************************************************/
	@Before("execution(* com.springproject.dockerspring.controller.AdminManage.* (..))")
	public Object adminAuthCheck(ProceedingJoinPoint jp) throws Throwable{
		return checkAuth(jp, Usage_Authority_Enum.Admin);
	}
}