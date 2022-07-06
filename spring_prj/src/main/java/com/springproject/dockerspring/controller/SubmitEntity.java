package com.springproject.dockerspring.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



/**************************************************************/
/*   [SubmitEntity]
/*   フロント側へのデータ送信の際の入れ物として機能する。
/*   このクラスは、JSON形式に変換される前提で作成する。
/**************************************************************/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubmitEntity<T>{

	//操作を実施したログイン中のユーザー名
	private String username;

	//操作を実施したログイン中のユーザーが持っているロール
	private String role;

	//予期せぬ例外の有無
	private Boolean unexpected_exception;

	//想定内のエラーの有無
	private Boolean error_flag;

	//エラーの種別
	private ErrorContentsEnum error_contents;

	//バリデーションエラーの内容
	private List<String> error_data;

	//送信するデータ本体
	private T submit_data;
}