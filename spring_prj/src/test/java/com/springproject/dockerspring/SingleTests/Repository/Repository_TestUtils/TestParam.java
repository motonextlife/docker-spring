/** 
 **************************************************************************************
 * @file TestParam.java
 * @brief リポジトリのテスト全般で用いる、関数の引数が多すぎる際に使用するビルダークラスを格納した
 * ファイル
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils;

import java.util.Date;
import java.util.List;
import java.util.function.BiConsumer;

import org.assertj.core.api.SoftAssertions;

import com.springproject.dockerspring.Repository.FindAllCrudRepository;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;











/** 
 **********************************************************************************************
 * @brief リポジトリのテスト全般で用いる、関数の引数が多すぎる際に使用するビルダークラス
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
public class TestParam<T> {
  
	//! 比較用エンティティ
	private final List<T> compare;

	//! 主要な検索ワード
	private final String main_word;

	//! 補助の検索ワード
	private final String sub_word;

	//! 検索開始日時（履歴検索に使用）
	private final Date start_datetime;

	//! 検索終了日時（履歴検索に使用）
	private final Date end_datetime;

	//! 検索種別
	private final String subject;

	//! 並び順
	private final Boolean order;

	//! 出力可能な検索結果最大数
	private final Integer max;

	//! 一つのページ当たりの最大検索結果件数
	private final Integer limit;

	//! 検索を開始する位置
	private final Integer offset;

	//! 検索種別が「全検索」の場合は「True」、それ以外は「False」とする。なお「履歴日時」の場合も同様。
	private final Boolean all_judge;

	//! 想定される検索結果件数
	private final Integer answer_count;

	//! エンティティの比較を行う際に指定する比較メソッド
	private final BiConsumer<T, T> assert_method;

	//! テスト対象のリポジトリクラス
	private final FindAllCrudRepository<T, Integer> test_repo;

	//! 遅延アサーション用のインスタンス
	private final SoftAssertions softly;
}