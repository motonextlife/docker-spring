package com.springproject.dockerspring.authaspect.AspectClass;



import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.springproject.dockerspring.authaspect.ComBlobAuth;
import com.springproject.dockerspring.authaspect.CommonAspect;
import com.springproject.dockerspring.commonenum.Usage_Authority_Enum;




/**************************************************************/
/*   [ScorePdfAspect]
/*   「楽譜データ管理機能」への、権限判断を行う。
/*   なお、判断対象のメソッド名は、末尾の「Auth」を除いて
/*   同一にしてある。
/**************************************************************/
@Component
@Aspect
public class ScorePdfAspect extends CommonAspect implements ComBlobAuth{

	//対象コントローラーの定義
	@Pointcut("com.springproject.dockerspring.controller.BlobController.ScorePdfManage")
	private void commonPointcut(){}
	
	@Override
	@Before("execution(* commonPointcut().selectBlobHistroy (..))")
	public Object selectBlobHistroyAuth(ProceedingJoinPoint jp) {
		return checkAuth(jp, Usage_Authority_Enum.Musical_Score_Hist_Brows);
	}

	@Override
	@Before("execution(* commonPointcut().getHistroyBlob (..))")
	public Object getHistroyBlobAuth(ProceedingJoinPoint jp) {
		return checkAuth(jp, Usage_Authority_Enum.Musical_Score_Hist_Brows);
	}

	@Override
	@Before("execution(* commonPointcut().rollbackBlobHistroy (..))")
	public Object rollbackBlobHistroyAuth(ProceedingJoinPoint jp) {
		return checkAuth(jp, Usage_Authority_Enum.Musical_Score_Hist_Rollback);
	}

	@Override
	@Before("execution(* commonPointcut().selectBlobData (..))")
	public Object selectBlobDataAuth(ProceedingJoinPoint jp) {
		return checkAuth(jp, Usage_Authority_Enum.Musical_Score_Brows);
	}

	@Override
	@Before("execution(* commonPointcut().insertBlobData (..))")
	public Object insertBlobDataAuth(ProceedingJoinPoint jp) {
		return checkAuth(jp, Usage_Authority_Enum.Musical_Score_Change);
	}

	@Override
	@Before("execution(* commonPointcut().updateBlobData (..))")
	public Object updateBlobDataAuth(ProceedingJoinPoint jp) {
		return checkAuth(jp, Usage_Authority_Enum.Musical_Score_Change);
	}

	@Override
	@Before("execution(* commonPointcut().deleteBlobData (..))")
	public Object deleteBlobDataAuth(ProceedingJoinPoint jp) {
		return checkAuth(jp, Usage_Authority_Enum.Musical_Score_Delete);
	}

	@Override
	@Before("execution(* commonPointcut().outputZipData (..))")
	public Object outputZipDataAuth(ProceedingJoinPoint jp) {
		return checkAuth(jp, Usage_Authority_Enum.Musical_Score_Brows);
	}

	@Override
	@Before("execution(* commonPointcut().inputZipData (..))")
	public Object inputZipDataAuth(ProceedingJoinPoint jp) {
		return checkAuth(jp, Usage_Authority_Enum.Musical_Score_Change);
	}
}