/** 
 ******************************************************************************************
 * @file GetLoginUser.java
 * @brief システムの処理中に、認証情報を取得するクラスを格納するファイル。
 ******************************************************************************************
 */
package com.springproject.dockerspring.Security.Service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.springproject.dockerspring.Security.Role_Enum;
import com.springproject.dockerspring.Security.Entity.AccountDetails;








/** 
 ******************************************************************************************
 * @brief システムの処理中に、認証情報を取得するクラス
 * 
 * @details 
 * - 使用用途としては、履歴情報の作成時に使うログインユーザー情報を取得する際に用いる。
 * - また、現在のユーザーに管理者権限が付与されていることを判別して行う処理にも用いる。
 *
 * @par 
 * - @Component
 *
 * @see AccountDetails
 ******************************************************************************************
 */
@Component
public class GetLoginUser{







	/** 
	 ******************************************************************************************
	 * @brief 現在ログイン中のユーザー名を取得する。
	 * 
	 * @par 大まかな処理の流れ
	 * -# 認証コンテキストを取り出し、その中のユーザー名を取得して返却する。
	 * 
	 * @see AccountDetails
	 ******************************************************************************************
	 */
  public String getLoginUser(){
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    AccountDetails user_datails = (AccountDetails)auth.getPrincipal();

    return user_datails.getUsername();
  }








	/** 
	 ******************************************************************************************
	 * @brief 現在ログイン中のユーザーが、管理者権限を持っているか確認する。
	 * 
	 * @par 大まかな処理の流れ
	 * -# 認証コンテキストを取り出し、その中の権限情報のリストを取得する。
	 * -# 取得したリストの中に、管理者権限の列挙型名が含まれているか確認し、真偽値を返す。
	 * 
	 * @see AccountDetails
	 ******************************************************************************************
	 */
  public Boolean existAdminRole(){
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    AccountDetails user_datails = (AccountDetails)auth.getPrincipal();
   
    return user_datails.getAuthorities()
                       .parallelStream()
                       .map(s -> s.getAuthority())
                       .toList()
                       .contains(Role_Enum.ROLE_ADMIN_TRUE.name());
  }
}