package com.springproject.dockerspring.authaspect;

import org.aspectj.lang.ProceedingJoinPoint;



/**************************************************************/
/*   [ComBlobAuth]
/*   バイナリデータ系処理があるコントローラーへの権限判断を行う。
/*   なお、判断対象のメソッド名は、末尾の「Auth」を除いて
/*   同一にしてある。
/**************************************************************/
public interface ComBlobAuth{

	Object selectBlobHistroyAuth(ProceedingJoinPoint jp);
	Object getHistroyBlobAuth(ProceedingJoinPoint jp);
	Object rollbackBlobHistroyAuth(ProceedingJoinPoint jp);
	Object selectBlobDataAuth(ProceedingJoinPoint jp);
	Object insertBlobDataAuth(ProceedingJoinPoint jp);
	Object updateBlobDataAuth(ProceedingJoinPoint jp);
	Object deleteBlobDataAuth(ProceedingJoinPoint jp);
	Object outputZipDataAuth(ProceedingJoinPoint jp);
	Object inputZipDataAuth(ProceedingJoinPoint jp);
}