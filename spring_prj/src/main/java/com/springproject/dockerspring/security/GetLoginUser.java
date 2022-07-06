package com.springproject.dockerspring.security;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;





/**************************************************************/
/*   [GetLoginUser]
/*   必要なクラスに継承して、ログインユーザー情報を取得する
/*   機能を持つ。継承しなければ使えないようにする。
/**************************************************************/
public abstract class GetLoginUser{

  protected String[] getLoginUser(){

    String[] userinfo = new String[2];
    
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Optional<String> role = auth.getAuthorities().stream().map(s -> s.getAuthority()).findFirst();

    userinfo[0] = auth.getPrincipal().getClass().getSimpleName();

		if(role.isPresent()){
			userinfo[1] = role.get();
		}else{
			userinfo[1] = RoleEnum.NO_ROLE.name();
		}

    return userinfo;
  }
}