/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.Security.Service
 * 
 * @brief セキュリティ関連の機能で、ハンドラメソッドに処理内容を提供するクラスを格納したパッケージ
 * 
 * @details
 * - このパッケージでは、認証処理時にデータベースからアカウント情報を取り出したり、リクエストからの
 * トークンを解析して認可処理を行う処理を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Security.Service;





/** 
 ******************************************************************************************
 * @file AccountDetailsService.java
 * @brief セキュリティに関する機能において、データベースからアカウント情報を取得するクラスを
 * 格納したファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springproject.dockerspring.Entity.NormalEntity.System_User;
import com.springproject.dockerspring.Entity.NormalEntity.Usage_Authority;
import com.springproject.dockerspring.Repository.NormalRepo.System_User_Repo;
import com.springproject.dockerspring.Repository.NormalRepo.Usage_Authority_Repo;
import com.springproject.dockerspring.Security.Role_Enum;
import com.springproject.dockerspring.Security.Entity.AccountDetails;

import lombok.RequiredArgsConstructor;








/** 
 ******************************************************************************************
 * @brief セキュリティに関する機能において、データベースからアカウント情報を取得するクラス
 * 
 * @details 
 * - ログイン画面から渡されたユーザー名を用いて検索を行い、データベースから取得した値を
 * 専用エンティティに格納して返却する。
 * - トークン生成クラスのDIは、Lombokを用いたコンストラクタインジェクションにより実現する。
 *
 * @par 使用アノテーション
 * - @Service
 * - @RequiredArgsConstructor(onConstructor = @__(@Autowired))
 * 
 * @see AccountDetails
 * @see System_User_Repo
 * @see Usage_Authority_Repo
 ******************************************************************************************
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountDetailsService implements UserDetailsService{

	private final System_User_Repo sys_user_repo;
	private final Usage_Authority_Repo usage_auth_repo;







	
	/** 
	 ******************************************************************************************
	 * @brief データベースからアカウント情報を取得する。
	 * 
	 * @details
	 * - ログイン画面から渡されたユーザー名を用いて検索を行い、データベースから取得した値を
	 * 専用エンティティに格納して返却する。
	 * - トランザクションを適用し、少しでも例外が出た場合はロールバックを行い処理を終了する。
	 * - この処理は読み取り専用として行う。
	 * 
	 * @param[in] input_name 入力されたユーザー名
	 * 
	 * @par 使用アノテーション
	 * - @Transactional(readOnly=true, rollbackFor = Exception.class)
	 * 
	 * @par 大まかな処理の流れ
	 * -# リポジトリを使用して、システムユーザーテーブルから、ユーザー名で検索してアカウント
	 * 情報を取得する。検索結果がない場合は、エラーとする。
	 * -# 先ほど取得したアカウント情報の権限番号を利用して、権限情報テーブルから対応する権限情報を
	 * 取得する。検索結果がない場合は、エラーとする。
	 * -# 先ほど取得した権限情報をもとに、権限情報列挙型を参照して、カンマ区切りの権限情報リストを
	 * 作成する。
	 * -# 作成したカンマ区切り権限情報を使って、認証コンテキストに登録する権限リストを作成する。
	 * -# これまで用意した、ユーザー名、パスワード、権限リスト、ロック有無を専用エンティティに
	 * 格納して戻り値とする。
	 * 
	 * @note ロック有無に関しては、データベースから取得した真偽値を反転させて登録する。
	 * 理由としては、SpringSecurityのロック管理は「True」でロック無しという扱いだが、データベース
	 * 上では「False」をロック無しという扱いにしている為である。

	 * @see AccountDetails
	 ******************************************************************************************
	 */
	@Override
	@Transactional(readOnly=true, rollbackFor = Exception.class)
	public UserDetails loadUserByUsername(String input_name) {

		System_User sys_user = this.sys_user_repo.sysSecurity(input_name)
																					 	 .orElseThrow(() -> new UsernameNotFoundException("username not found"));

    Usage_Authority auth_list = this.usage_auth_repo.findByAuth_id(sys_user.getAuth_id())
																					 				  .orElseThrow(() -> new UsernameNotFoundException("authority not found"));

		List<String> comma_role = new ArrayList<>(){
			{
				add(Role_Enum.getAdminRole(auth_list.getAdmin()));
				add(Role_Enum.getMemberInfoRole(auth_list.getMember_info()));
				add(Role_Enum.getFacilityRole(auth_list.getFacility()));
				add(Role_Enum.getMusicalScoreRole(auth_list.getMusical_score()));
				add(Role_Enum.getSoundSourceRole(auth_list.getSound_source()));
			}
		};

    List<GrantedAuthority> auth = AuthorityUtils
																  .commaSeparatedStringToAuthorityList(comma_role.stream()
																																								 .collect(Collectors.joining(",")));

		String username = sys_user.getUsername();
		String password = sys_user.getPassword();
		Boolean lock = !(sys_user.getLocking());

		return new AccountDetails(username, password, auth, lock);
	}
}