package com.springproject.dockerspring.authaspect;

import com.springproject.dockerspring.commonenum.Usage_Authority_Enum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;





/**************************************************************/
/*   [SubmitAuthNgEntity]
/*   使用権限がなく、処理を拒否された時に送信するエンティティ。
/**************************************************************/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubmitAuthNgEntity {

	//拒否されないために必要な権限名。
	private Usage_Authority_Enum auth_name;

	//例外やエラーの有無。
	private Boolean error;
}