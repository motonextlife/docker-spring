package com.springproject.dockerspring.authaspect;


import org.aspectj.lang.ProceedingJoinPoint;



/**************************************************************/
/*   [ComFuncAuth]
/*   全てのコントローラーへの権限判断を行う。
/*   なお、判断対象のメソッド名は、末尾の「Auth」を除いて
/*   同一にしてある。
/**************************************************************/
public interface ComFuncAuth{

	Object selectHistroyAuth(ProceedingJoinPoint jp);
	Object rollbackHistroyAuth(ProceedingJoinPoint jp);
	Object selectDataAuth(ProceedingJoinPoint jp);
	Object insertDataAuth(ProceedingJoinPoint jp);
	Object updateDataAuth(ProceedingJoinPoint jp);
	Object deleteDataAuth(ProceedingJoinPoint jp);
	Object selectBulkDataAuth(ProceedingJoinPoint jp);
	Object outputBulkCsvDataAuth(ProceedingJoinPoint jp);
	Object outputPdfDataAuth(ProceedingJoinPoint jp);
	Object inputCsvDataAuth(ProceedingJoinPoint jp);
	Object outputCsvTemplateDataAuth(ProceedingJoinPoint jp);
}