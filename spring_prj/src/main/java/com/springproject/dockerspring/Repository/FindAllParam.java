/** 
 **************************************************************************************
 * @file FindAllParam.java
 * @brief リポジトリの検索分岐メソッドに渡す引数を格納するエンティティを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Repository;

import java.util.Date;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;








/** 
 **************************************************************************************
 * @brief リポジトリの検索分岐メソッドに渡す引数を格納するエンティティ
 * 
 * @details 
 * - このクラスを設けた理由としては、リポジトリの分岐メソッドには多くの引数が必要となるが、要件上
 * 分割ができない為、ビルダーのエンティティに格納してまとめて格納して渡せるようにする。
 * - ビルダーパターンを採用し、柔軟に値をセットできることで、共通に使用できるようにしている。
 * - 一つの処理の引数に対して、全ての値を使用するわけではない。状況によってはNullのままもある。
 * - ビルダー経由のインスタンス生成のみ許可し、コンストラクタの引数からの生成は禁止する。
 * 
 * @par 使用アノテーション
 * - @Builder
 * - @Getter
 * - @AllArgsConstructor(access = AccessLevel.PRIVATE)
 **************************************************************************************
 */ 
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FindAllParam {

	//! 主要な検索ワード
	private final String main_word;

	//! 補助の検索ワード
	private final String sub_word;

	//! 検索開始日
	private final Date start_datetime;

	//! 検索終了日
	private final Date end_datetime;

	//! 検索種別
	private final String subject;

	//! 並び順
	private final Boolean order;

	//! 検索件数最大値
	private final Integer max;

	//! 一ページ当たりの検索件数
	private final Integer limit;

	//! オフセット値
	private final Integer offset;
}