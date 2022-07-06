package com.springproject.dockerspring.security;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springproject.dockerspring.entity.NormalEntity.System_User;
import com.springproject.dockerspring.repository.NormalRepo.SysUserRepo;





/**************************************************************/
/*   [SysUserDetailsService]
/*   システム利用者の認証に用いるサービスクラスを定義する。
/**************************************************************/
@Service
public class SysUserDetailsService implements UserDetailsService{

	@Autowired
	SysUserRepo sysuser_repo;





	/**************************************************************/
	/*   [loadUserByUsername]
	/*   SpringSecurityでデフォルトで使用するメソッドである。
	/**************************************************************/
	@Override
	@Transactional(readOnly=true, rollbackFor = Exception.class)
	public UserDetails loadUserByUsername(String inputname) {

		Optional<System_User> opt = sysuser_repo.sysSecurity(inputname);

		//ユーザーが見当たらない場合は、空のDetailsを返し、強制的にログイン拒否する。
		if(opt.isEmpty()){
			Collection<GrantedAuthority> norole = AuthorityUtils.createAuthorityList(RoleEnum.NO_ROLE.name());
			return new SysUserDetails(null, null, norole, true);
		}

		//通常ユーザー用のロールを付加。
		Collection<GrantedAuthority> auth = AuthorityUtils.createAuthorityList(RoleEnum.ROLE_ADMIN_AND_USER.name());

		System_User sysuser = opt.get();
		String username = sysuser.getUsername();
		String password = sysuser.getPassword();
		Boolean lock = !(sysuser.getLocking());

		if(lock){
			Integer fail_count = sysuser.getFail_count();

			//認証後のロックカウントが難しいため、認証前にロックカウントを増やし、
			//ログイン成功後にリセットする。
			fail_count++;

			String member_id = sysuser.getMember_id();

			//ロックカウントが10回以上に達していれば、アカウントをロックする。
			if(fail_count >= 10){
				sysuser_repo.updateLock(member_id, fail_count, true);
				lock = false;
			}else{
				sysuser_repo.updateLock(member_id, fail_count, false);
			}
		}

		return new SysUserDetails(username, password, auth, lock);
	}
}