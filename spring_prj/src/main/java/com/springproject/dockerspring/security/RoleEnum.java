package com.springproject.dockerspring.security;


/**************************************************************/
/*   [RoleEnum]
/*   利用者の利用権限を定義する列挙型である。
/**************************************************************/
public enum RoleEnum{
  ROLE_CREATOR,
  ROLE_ADMIN_AND_USER,
  NO_ROLE;
}