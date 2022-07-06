package com.springproject.dockerspring.authaspect.AspectClass;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.springproject.dockerspring.authaspect.ComFuncAuth;
import com.springproject.dockerspring.authaspect.CommonAspect;
import com.springproject.dockerspring.commonenum.Usage_Authority_Enum;





/**************************************************************/
/*   [ScoreAspect]
/*   「楽譜管理機能」への、権限判断を行う。
/*   なお、判断対象のメソッド名は、末尾の「Auth」を除いて
/*   同一にしてある。
/**************************************************************/
@Component
@Aspect
public class ScoreAspect extends CommonAspect implements ComFuncAuth{

	//対象コントローラーの定義
	@Pointcut("com.springproject.dockerspring.controller.NormalController.ScoreManage")
	private void commonPointcut(){}

	@Override
	@Before("execution(* commonPointcut().selectHistroy (..))")
	public Object selectHistroyAuth(ProceedingJoinPoint jp) {
		return checkAuth(jp, Usage_Authority_Enum.Musical_Score_Hist_Brows);
	}

	@Override
	@Before("execution(* commonPointcut().rollbackHistroy (..))")
	public Object rollbackHistroyAuth(ProceedingJoinPoint jp) {
		return checkAuth(jp, Usage_Authority_Enum.Musical_Score_Hist_Rollback);
	}

	@Override
	@Before("execution(* commonPointcut().selectData (..))")
	public Object selectDataAuth(ProceedingJoinPoint jp) {
		return checkAuth(jp, Usage_Authority_Enum.Musical_Score_Brows);
	}

	@Override
	@Before("execution(* commonPointcut().insertData (..))")
	public Object insertDataAuth(ProceedingJoinPoint jp) {
		return checkAuth(jp, Usage_Authority_Enum.Musical_Score_Change);
	}

	@Override
	@Before("execution(* commonPointcut().updateData (..))")
	public Object updateDataAuth(ProceedingJoinPoint jp) {
		return checkAuth(jp, Usage_Authority_Enum.Musical_Score_Change);
	}

	@Override
	@Before("execution(* commonPointcut().deleteData (..))")
	public Object deleteDataAuth(ProceedingJoinPoint jp) {
		return checkAuth(jp, Usage_Authority_Enum.Musical_Score_Delete);
	}

	@Override
	@Before("execution(* commonPointcut().selectBulkData (..))")
	public Object selectBulkDataAuth(ProceedingJoinPoint jp) {
		return checkAuth(jp, Usage_Authority_Enum.Musical_Score_Brows);
	}

	@Override
	@Before("execution(* commonPointcut().outputBulkCsvData (..))")
	public Object outputBulkCsvDataAuth(ProceedingJoinPoint jp) {
		return checkAuth(jp, Usage_Authority_Enum.Musical_Score_Brows);
	}

	@Override
	@Before("execution(* commonPointcut().outputPdfData (..))")
	public Object outputPdfDataAuth(ProceedingJoinPoint jp) {
		return checkAuth(jp, Usage_Authority_Enum.Musical_Score_Brows);
	}

	@Override
	@Before("execution(* commonPointcut().inputCsvData (..))")
	public Object inputCsvDataAuth(ProceedingJoinPoint jp) {
		return checkAuth(jp, Usage_Authority_Enum.Musical_Score_Change);
	}

	@Override
	@Before("execution(* commonPointcut().outputCsvTemplateData (..))")
	public Object outputCsvTemplateDataAuth(ProceedingJoinPoint jp) {
		return checkAuth(jp, Usage_Authority_Enum.Musical_Score_Brows);
	}
}