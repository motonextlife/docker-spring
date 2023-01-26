/** 
 **************************************************************************************
 * @file FindAllResult.java
 * @brief リポジトリの検索メソッドの検索結果と結果件数を格納し返却するエンティティクラスを
 * 格納したファイルである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Repository;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;






/** 
 **************************************************************************************
 * @brief リポジトリの検索メソッドの検索結果と結果件数を格納し返却するエンティティクラス。
 * 
 * @details 
 * - このクラスを設けた理由としては、リポジトリの検索メソッドは同時に二つの違う型のデータを戻り値と
 * しなくてはならないが、通常では同時に返せないので戻り値の返却の為だけにデータを格納して返すため。
 * 
 * @par 使用アノテーション
 * - @Data
 * - @AllArgsConstructor
 **************************************************************************************
 */ 
@Data
@AllArgsConstructor
public class FindAllResult<T> {

	//! 検索結果リスト
	private List<T> result_list;

	//! 全体の検索結果件数
	private Integer result_count;
}