package com.springproject.dockerspring.authaspect;


import org.aspectj.lang.ProceedingJoinPoint;



/**************************************************************/
/*   [ComListAuth]
/*   「採用考課管理」「点検管理」において現在登録中の情報を
/*   一覧表示する機能への権限判断を行う。
/*   なお、判断対象のメソッド名は、末尾の「Auth」を除いて
/*   同一にしてある。
/**************************************************************/
public interface ComListAuth{

	Object selectListDataAuth(ProceedingJoinPoint jp);
}