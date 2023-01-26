/** 
 **************************************************************************************
 * @file CompareParam.java
 * @brief リポジトリのテストにおいて、検索結果の比較用に用いるリストを作成する際に、引数として
 * 渡す値を組み立てるビルダークラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;












/** 
 **********************************************************************************************
 * @brief リポジトリのテストにおいて、検索結果の比較用に用いるリストを作成する際に、引数として
 * 渡す値を組み立てるビルダークラス
 * 
 * @details
 * - ビルダーパターンを採用し、柔軟に値をセットできることで、共通に使用できるようにしている。
 * - 一つの処理の引数に対して、全ての値を使用するわけではない。状況によってはNullのままもある。
 * - ビルダー経由のインスタンス生成のみ許可し、コンストラクタの引数からの生成は禁止する。
 * 
 * @par 使用アノテーション
 * - @Builder
 * - @Getter
 * - @AllArgsConstructor(access = AccessLevel.PRIVATE)
 **********************************************************************************************
 */
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CompareParam<T> {

  //! 元となる比較用エンティティ
	private final List<T> origin_compare;

  //! 必要なエンティティのみ残すフィルターの関数型インターフェース。なお、検索種別が「全検索」の際はNullとなる。
	private final Predicate<T> filter;

  //! ソートの第一条件のコンパレーター
	private final Comparator<T> sort_first;

  //! ソートの第二条件のコンパレーター（ない場合はNull）
	private final Comparator<T> sort_second;

  //! ソートの第三条件のコンパレーター（ない場合はNull）
	private final Comparator<T> sort_third;

  //! 最初からスキップする数量。つまりオフセット値。
	private final Integer skip;

  //! 生成するリストの長さ。つまり1ページ当たりの検索結果最大数。
	private final Integer limit;
}