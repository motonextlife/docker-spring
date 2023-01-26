/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.Security.EventHandler
 * 
 * @brief セキュリティ関連の機能で、認証認可処理時のハンドラクラスを格納したパッケージ
 * 
 * @details
 * - このパッケージでは、認証処理の際に決められたイベントが発生した際に行う処理を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Security.EventHandler;






/** 
 ******************************************************************************************
 * @file AccountAuthEvents.java
 * @brief 認証処理時の成功と失敗のイベントに応じて、処理を行うイベントハンドラクラスを
 * 格納したファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.springproject.dockerspring.Entity.NormalEntity.System_User;
import com.springproject.dockerspring.Repository.NormalRepo.System_User_Repo;
import com.springproject.dockerspring.Security.Entity.AccountDetails;

import lombok.RequiredArgsConstructor;










/** 
 ******************************************************************************************
 * @brief 認証処理時の成功と失敗のイベントに応じて、処理を行うイベントハンドラクラス
 * 
 * @details 
 * - 認証の成功と失敗に応じた処理はいくつもあるが、データベースが関係する複雑な処理に関しては
 * イベントハンドラに記載する。あまり通常のハンドラに複雑な処理を書くのはよろしくないためである。
 * - リポジトリのDIは、Lombokを用いたコンストラクタインジェクションにより実現する。
 * 
 * @par 使用アノテーション
 * - @Component
 * - @RequiredArgsConstructor(onConstructor = @__(@Autowired))
 * 
 * @see System_User_Repo
 ******************************************************************************************
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountAuthEvents{

  private final System_User_Repo sys_user_repo;








	/** 
	 ******************************************************************************************
	 * @brief ログイン処理が成功した際に行う処理を定義する。
	 * 
	 * @details 
	 * - 内容としては、ログインに成功したアカウントの、ロック回数とロックの有無をリセットする。
	 * 
	 * @param[in] success 検知する認証成功イベント
	 * 
	 * @par 使用アノテーション
	 * - @EventListener
	 * 
	 * @par 大まかな処理の流れ
	 * -# 認証中のアカウントの認証コンテキストを取得し、認証情報を抜き取る。
	 * -# 認証情報内のユーザー名を利用してデータベースを検索し、団員番号を取得する。
	 * -# 団員番号を利用して、該当団員のログイン失敗回数をリセットし、ロックがあれば解除する。
	 ******************************************************************************************
	 */
  @EventListener
  public void successProcess(AuthenticationSuccessEvent success){
    AccountDetails user_details = (AccountDetails)success.getAuthentication().getPrincipal();
    System_User ent = sys_user_repo.sysSecurity(user_details.getUsername())
                                   .orElseThrow(() -> new UsernameNotFoundException("username not found"));

    String member_id = ent.getMember_id();
    sys_user_repo.updateLock(member_id, 0, false);
  }








	/** 
	 ******************************************************************************************
	 * @brief ログイン処理が失敗した際に行う処理を定義する。
	 * 
	 * @details 
	 * - 内容としては、ログインに失敗したユーザーのアカウントのログイン失敗回数をカウントしていく。
	 * - カウントが上限に達したら、アカウントをロックする。
	 * 
	 * @param[in] fail 検知する認証失敗イベント
	 * 
	 * @par 使用アノテーション
	 * - @EventListener
	 * 
	 * @par 大まかな処理の流れ
	 * -# 認証中のアカウントの認証コンテキストを取得し、認証情報を抜き取る。
	 * -# 認証情報内のユーザー名を利用してデータベースを検索し、団員番号を取得する。
	 * なお、ユーザーが存在しなければ、例外などは出さず結果をNullとする。
	 * -# 検索結果がなかったり、ログイン失敗回数がすでに上限に達している場合は、処理を続行しない。
	 * -# 団員番号を利用して、該当団員のログイン失敗回数を一回ずつ増やす。
	 * -# カウントを増やした際に上限値に達した場合は、アカウントをロックする。
	 ******************************************************************************************
	 */
  @EventListener
  public void failedProcess(AbstractAuthenticationFailureEvent fail){

    AccountDetails user_details = (AccountDetails)fail.getAuthentication().getPrincipal();
    System_User ent = sys_user_repo.sysSecurity(user_details.getUsername())
																	 .orElse(null);

    if(ent == null || ent.getFail_count() >= 10){
      return;
    }

		int count = ent.getFail_count();
    String member_id = ent.getMember_id();
    int after_count = count++;

    if(after_count >= 10){
      sys_user_repo.updateLock(member_id, after_count, true);
    }else{
      sys_user_repo.updateLock(member_id, after_count, false);
    }
  }
}