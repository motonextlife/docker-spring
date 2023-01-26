/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils
 * 
 * @brief リポジトリのテストで共通で用いるユーティリティクラスを格納したパッケージ
 * 
 * @details
 * - このパッケージは、リポジトリのテストコードを集約し簡素化する目的で設けたユーティリティクラスを
 * 格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils;





/** 
 **************************************************************************************
 * @file Common_Repository_TestUtils.java
 * @brief 主に[全ての機能]のリポジトリのテストで用いる、テスト全般に用いることができるユーティリティ
 * クラスを格納したファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import com.springproject.dockerspring.Repository.FindAllCrudRepository;
import com.springproject.dockerspring.Repository.FindAllParam;
import com.springproject.dockerspring.Repository.FindAllResult;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.assertj.core.api.SoftAssertions;












/** 
 **************************************************************************************
 * @brief 主に[全ての機能]のリポジトリのテストで用いる、テスト全般に用いることができる
 * ユーティリティクラス 
 * 
 * @details 
 * - 必要性がないため、インスタンス化を禁止する。
 * - 引数が多い関数に関しては、ビルダーエンティティを作成し、その中に格納して渡す。
 **************************************************************************************
 */ 
public class Common_Repository_TestUtils{

  private Common_Repository_TestUtils(){
    throw new AssertionError();
  }











  /** 
   **********************************************************************************************
   * @brief 通常データのリポジトリの検索結果取得メソッドの単体での実行を行い、返された結果の判定を行う。
   * 
   * @details
   * - 遅延アサーションのインスタンスは、このメソッドを呼び出したクラス側の物を使用する。また、アサーションの
   * 最終実行も呼び出し側のクラスで行う。
   * - 検索種別に「全検索」を指定した場合は、検索ワードがNullだった場合の判定を実施しない。
   * - エンティティの比較メソッドに関しては、こちらのクラス側ではジェネリクスで指定できないので、テストクラス側で
   * 関数型インターフェースを作成し、その中で作成して実行する。
	 * - リポジトリ内の各検索メソッドの名称は異なるので、関数型インターフェースに格納して処理を実行する。
   *
   * @param[in] find_all_method 検査対象の検索メソッドを実行する関数型インターフェース
   * @param[in] test_param テスト検索に必要な引数を格納したビルダーエンティティ
   * 
   * @see TestParam
   * 
   * @par 大まかな処理の流れ
   * -# 指定された検索ワードや検索種別を用いて、テスト対象メソッドを実行し、検索結果を取得する。
   * -# 引数で渡された比較用エンティティのリストと、検索結果のエンティティリストを比較し、想定通りの
   * 結果が返ってきていることを確認する。
   * -# 検索種別が「全検索」の時以外は、検索ワードにNullを渡した際の結果が、0件であることを確認する。
   * -# 本来Nullが許されない、テスト対象メソッドの引数（オフセット値や検索結果最大値）にNullを渡した際に、
   * 想定される例外が返ってくることを確認する。
   * 
   * @throw ExecutionException
   * @throw InterruptedException
   **********************************************************************************************
   */
	public static <T> void findAllSearchCheck_Normal(Function<TestParam<T>, CompletableFuture<List<T>>> find_all_method, 
																									 TestParam<T> test_param) 
																									 throws InterruptedException, ExecutionException{

    List<T> compare = test_param.getCompare();
    BiConsumer<T, T> assert_method = test_param.getAssert_method();
    SoftAssertions softly = test_param.getSoftly();

    CompletableFuture<List<T>> result = find_all_method.apply(test_param);

		
    //結果の同一判定
    List<T> result_list = result.get();
    for(int i = 0; i < compare.size(); i++){
      assert_method.accept(compare.get(i), result_list.get(i));
    }


    //検索ワードが無しの場合の結果判定(全検索時は判定対象外)
		if(!test_param.getAll_judge()){
			CompletableFuture<List<T>> empty_result_main = find_all_method.apply(test_param.toBuilder()
																																										 .main_word(null)
																																										 .build());
			softly.assertThat(empty_result_main.get()).isEmpty();

			//日付検索などでサブワードが指定されていれば、サブワードが無しの場合の結果も判定
			if(test_param.getSub_word() != null){
				CompletableFuture<List<T>> empty_result_sub = find_all_method.apply(test_param.toBuilder()
																																											.sub_word(null)
																																											.build());
				softly.assertThat(empty_result_sub.get()).isEmpty();
			}
		}


    //不正値が渡された時の挙動確認
    CompletableFuture<List<T>> error_result_1 = find_all_method.apply(test_param.toBuilder()
																																								.limit(null)
																																								.build());
    softly.assertThatThrownBy(() -> error_result_1.get())
          .isInstanceOf(ExecutionException.class);


    CompletableFuture<List<T>> error_result_2 = find_all_method.apply(test_param.toBuilder()
																																								.offset(null)
																																								.build());
    softly.assertThatThrownBy(() -> error_result_2.get())
          .isInstanceOf(ExecutionException.class);
	}












  /** 
   **********************************************************************************************
   * @brief 履歴データのリポジトリの検索結果取得メソッドの単体での実行を行い、返された結果の判定を行う。
   * 
   * @details
   * - 遅延アサーションのインスタンスは、このメソッドを呼び出したクラス側の物を使用する。また、アサーションの
   * 最終実行も呼び出し側のクラスで行う。
   * - 検索種別に「全検索」を指定した場合は、検索ワードがNullだった場合の判定を実施しない。
   * - エンティティの比較メソッドに関しては、こちらのクラス側ではジェネリクスで指定できないので、テストクラス側で
   * 関数型インターフェースを作成し、その中で作成して実行する。
	 * - リポジトリ内の各検索メソッドの名称は異なるので、関数型インターフェースに格納して処理を実行する。
   *
   * @param[in] find_all_method 検査対象の検索メソッドを実行する関数型インターフェース
   * @param[in] test_param テスト検索に必要な引数を格納したビルダーエンティティ
   * 
   * @see TestParam
   * 
   * @par 大まかな処理の流れ
   * -# 指定された検索ワードや検索種別を用いて、テスト対象メソッドを実行し、検索結果を取得する。
   * -# 引数で渡された比較用エンティティのリストと、検索結果のエンティティリストを比較し、想定通りの
   * 結果が返ってきていることを確認する。
   * -# 検索種別が「全検索」の時以外は、検索ワードにNullを渡した際の結果が、0件であることを確認する。
   * -# 本来Nullが許されない、テスト対象メソッドの引数（オフセット値や検索結果最大値）にNullを渡した際に、
   * 想定される例外が返ってくることを確認する。
   * 
   * @throw ExecutionException
   * @throw InterruptedException
   **********************************************************************************************
   */
	public static <T> void findAllSearchCheck_History(Function<TestParam<T>, CompletableFuture<List<T>>> find_all_method, 
																									  TestParam<T> test_param) 
																									  throws InterruptedException, ExecutionException{

    List<T> compare = test_param.getCompare();
    BiConsumer<T, T> assert_method = test_param.getAssert_method();
    SoftAssertions softly = test_param.getSoftly();

    CompletableFuture<List<T>> result = find_all_method.apply(test_param);

		
    //結果の同一判定
    List<T> result_list = result.get();
    for(int i = 0; i < compare.size(); i++){
      assert_method.accept(compare.get(i), result_list.get(i));
    }


    //検索ワードが無しの場合の結果判定(全検索時は、検索ワードを指定した判定対象外)
		CompletableFuture<List<T>> empty_result_start = find_all_method.apply(test_param.toBuilder()
																																									 	.start_datetime(null)
																																									 	.build());
		softly.assertThat(empty_result_start.get()).isEmpty();

		CompletableFuture<List<T>> empty_result_end = find_all_method.apply(test_param.toBuilder()
																																									.end_datetime(null)
																																									.build());
		softly.assertThat(empty_result_end.get()).isEmpty();

		if(!test_param.getAll_judge()){
			CompletableFuture<List<T>> empty_result_word = find_all_method.apply(test_param.toBuilder()
																																										 .main_word(null)
																																										 .build());
			softly.assertThat(empty_result_word.get()).isEmpty();
		}


    //不正値が渡された時の挙動確認
    CompletableFuture<List<T>> error_result_1 = find_all_method.apply(test_param.toBuilder()
																																								.limit(null)
																																								.build());
    softly.assertThatThrownBy(() -> error_result_1.get())
          .isInstanceOf(ExecutionException.class);


    CompletableFuture<List<T>> error_result_2 = find_all_method.apply(test_param.toBuilder()
																																								.offset(null)
																																								.build());
    softly.assertThatThrownBy(() -> error_result_2.get())
          .isInstanceOf(ExecutionException.class);
	}




  


  






  /** 
   **********************************************************************************************
   * @brief 通常データのリポジトリの検索メソッドの分岐と実行メソッドを実行し、返された結果の判定を行う。
   * 
   * @details
   * - 遅延アサーションのインスタンスは、このメソッドを呼び出したクラス側の物を使用する。また、アサーションの
   * 最終実行も呼び出し側のクラスで行う。
   * - 検索種別に「全検索」を指定した場合は、検索ワードがNullだった場合の判定を実施しない。
   * - エンティティの比較メソッドに関しては、こちらのクラス側ではジェネリクスで指定できないので、テストクラス側で
   * 関数型インターフェースを作成し、その中で作成して実行する。
   *
   * @param[in] test_param テスト検索に必要な引数を格納したビルダーエンティティ
   * 
   * @see TestParam
   * @see FindAllParam
   * 
   * @par 大まかな処理の流れ
   * -# 指定された検索ワードや検索種別を用いて、テスト対象メソッドを実行し、検索結果を取得する。
   * -# 引数で渡された比較用エンティティのリストと、検索結果のエンティティリストを比較し、想定通りの
   * 結果が返ってきていることを確認する。
   * -# 検索結果件数が、想定通りの値であることを確認する。
   * -# 検索種別が「全検索」の時以外は、検索ワードにNullを渡した際の結果が、0件であることを確認する。
   * -# 本来Nullが許されない、テスト対象メソッドの引数（オフセット値や検索結果最大値）にNullを渡した際に、
   * 想定される例外が返ってくることを確認する。
   * 
   * @throw ExecutionException
   * @throw InterruptedException
   * @throw ParseException
   **********************************************************************************************
   */
  public static <T> void findAllBranchCheck_Normal(TestParam<T> test_param) 
      throws ExecutionException, InterruptedException, ParseException{

    List<T> compare = test_param.getCompare();
    String main_word = test_param.getMain_word();
    String sub_word = test_param.getSub_word();
    String subject = test_param.getSubject();
    Boolean order = test_param.getOrder();
    Integer max = test_param.getMax();
    Integer limit = test_param.getLimit();
    Integer offset = test_param.getOffset();
    BiConsumer<T, T> assert_method = test_param.getAssert_method();
    FindAllCrudRepository<T, Integer> test_repo = test_param.getTest_repo();
    SoftAssertions softly = test_param.getSoftly();


    //結果の同一判定
    FindAllParam param = FindAllParam.builder().main_word(main_word)
                                               .sub_word(sub_word)
                                               .subject(subject)
                                               .order(order)
                                               .max(max)
                                               .limit(limit)
                                               .offset(offset)
                                               .build();

    FindAllResult<T> result = test_repo.findAllBranch(param);


    List<T> result_list = result.getResult_list();
    for(int i = 0; i < compare.size(); i++){
      assert_method.accept(compare.get(i), result_list.get(i));
    }

    softly.assertThat(result.getResult_count()).isEqualTo(test_param.getAnswer_count());


    //検索ワードが無しの場合の結果判定(全検索時は判定対象外)
    if(!test_param.getAll_judge()){
      param = FindAllParam.builder().subject(subject)
                                    .order(order)
                                    .max(max)
                                    .limit(limit)
                                    .offset(offset)
                                    .build();

      FindAllResult<T> empty_result = test_repo.findAllBranch(param);
      softly.assertThat(empty_result.getResult_list()).isEmpty();
      softly.assertThat(empty_result.getResult_count()).isEqualTo(0);
    }


    //不正値が渡された時の挙動確認
    FindAllParam error_param_1 = FindAllParam.builder().order(order).max(max).limit(limit).offset(offset)
                                                       .build();
    softly.assertThatThrownBy(() -> test_repo.findAllBranch(error_param_1))
          .isInstanceOf(IllegalArgumentException.class);


    FindAllParam error_param_2 = FindAllParam.builder().subject(subject).max(max).limit(limit).offset(offset)
                                                       .build();
    softly.assertThatThrownBy(() -> test_repo.findAllBranch(error_param_2))
          .isInstanceOf(IllegalArgumentException.class);


    FindAllParam error_param_3 = FindAllParam.builder().subject(subject).order(order).limit(limit).offset(offset)
                                                       .build();
    softly.assertThatThrownBy(() -> test_repo.findAllBranch(error_param_3))
          .isInstanceOf(IllegalArgumentException.class);


    FindAllParam error_param_4 = FindAllParam.builder().order(order).max(max).offset(offset)
                                                       .build();
    softly.assertThatThrownBy(() -> test_repo.findAllBranch(error_param_4))
          .isInstanceOf(IllegalArgumentException.class);


    FindAllParam error_param_5 = FindAllParam.builder().order(order).max(max).limit(limit)
                                                       .build();
    softly.assertThatThrownBy(() -> test_repo.findAllBranch(error_param_5))
          .isInstanceOf(IllegalArgumentException.class);
  }












  /** 
   **********************************************************************************************
   * @brief 履歴データのリポジトリの検索メソッドの分岐と実行メソッドを実行し、返された結果の判定を行う。
   * 
   * @details
   * - 遅延アサーションのインスタンスは、このメソッドを呼び出したクラス側の物を使用する。また、アサーションの
   * 最終実行も呼び出し側のクラスで行う。
   * - 検索種別に「履歴日時」を指定した場合は、検索ワードがNullだった場合の判定を実施しない。ただし、開始日時や
   * 終了日時がNullだった場合の判定は実施するので注意する。
   * - エンティティの比較メソッドに関しては、こちらのクラス側ではジェネリクスで指定できないので、テストクラス側で
   * 関数型インターフェースを作成し、その中で作成して実行する。
   *
   * @param[in] test_param テスト検索に必要な引数を格納したビルダーエンティティ
   * 
   * @see TestParam
   * 
   * @par 大まかな処理の流れ
   * -# 指定された検索ワードや検索種別を用いて、テスト対象メソッドを実行し、検索結果を取得する。
   * -# 引数で渡された比較用エンティティのリストと、検索結果のエンティティリストを比較し、想定通りの
   * 結果が返ってきていることを確認する。
   * -# 検索結果件数が、想定通りの値であることを確認する。
   * -# 検索種別が「履歴日時」の時以外は、検索ワードにNullを渡した際の結果が、0件であることを確認する。
   * -# 検索開始日時や検索終了日時に、Nullを渡した際の結果が、0件であることを確認する。
   * -# 本来Nullが許されない、テスト対象メソッドの引数（オフセット値や検索結果最大値）にNullを渡した際に、
   * 想定される例外が返ってくることを確認する。
   * 
   * @throw ExecutionException
   * @throw InterruptedException
   * @throw ParseException
   **********************************************************************************************
   */
  public static <T> void findAllBranchCheck_History(TestParam<T> test_param) 
      throws ExecutionException, InterruptedException, ParseException{

    List<T> compare = test_param.getCompare();
    String word = test_param.getMain_word();
    Date start_datetime = test_param.getStart_datetime();
    Date end_datetime = test_param.getEnd_datetime();
    String subject = test_param.getSubject();
    Boolean order = test_param.getOrder();
    Integer max = test_param.getMax();
    Integer limit = test_param.getLimit();
    Integer offset = test_param.getOffset();
    BiConsumer<T, T> assert_method = test_param.getAssert_method();
    FindAllCrudRepository<T, Integer> test_repo = test_param.getTest_repo();
    SoftAssertions softly = test_param.getSoftly();


    //結果の同一判定
    FindAllParam param = FindAllParam.builder().main_word(word)
                                               .start_datetime(start_datetime)
                                               .end_datetime(end_datetime)
                                               .subject(subject)
                                               .order(order)
                                               .max(max)
                                               .limit(limit)
                                               .offset(offset)
                                               .build();

    FindAllResult<T> result = test_repo.findAllBranch(param);


    List<T> result_list = result.getResult_list();
    for(int i = 0; i < compare.size(); i++){
      assert_method.accept(compare.get(i), result_list.get(i));
    }

    softly.assertThat(result.getResult_count()).isEqualTo(test_param.getAnswer_count());


    //検索ワードが無しの場合の結果判定(検索種別が履歴日時の場合、検索ワードがNullの時に関しては判定対象外)
    if(!test_param.getAll_judge()){
      param = FindAllParam.builder().start_datetime(start_datetime)
                                    .end_datetime(end_datetime)
                                    .subject(subject)
                                    .order(order)
                                    .max(max)
                                    .limit(limit)
                                    .offset(offset)
                                    .build();

      FindAllResult<T> empty_result_1 = test_repo.findAllBranch(param);
      softly.assertThat(empty_result_1.getResult_list()).isEmpty();
      softly.assertThat(empty_result_1.getResult_count()).isEqualTo(0);
    }

    param = FindAllParam.builder().main_word(word)
                                  .subject(subject)
                                  .order(order)
                                  .max(max)
                                  .limit(limit)
                                  .offset(offset)
                                  .build();

    FindAllResult<T> empty_result_2 = test_repo.findAllBranch(param.toBuilder().end_datetime(end_datetime).build());
    FindAllResult<T> empty_result_3 = test_repo.findAllBranch(param.toBuilder().start_datetime(start_datetime).build());
    softly.assertThat(empty_result_2.getResult_list()).isEmpty();
    softly.assertThat(empty_result_3.getResult_list()).isEmpty();
    softly.assertThat(empty_result_2.getResult_count()).isEqualTo(0);
    softly.assertThat(empty_result_3.getResult_count()).isEqualTo(0);



    //不正値が渡された時の挙動確認
    FindAllParam error_param_1 = FindAllParam.builder().start_datetime(start_datetime).end_datetime(end_datetime)
                                                       .order(order).max(max).limit(limit).offset(offset)
                                                       .build();
    softly.assertThatThrownBy(() -> test_repo.findAllBranch(error_param_1))
          .isInstanceOf(IllegalArgumentException.class);


    FindAllParam error_param_2 = FindAllParam.builder().start_datetime(start_datetime).end_datetime(end_datetime)
                                                       .subject(subject).max(max).limit(limit).offset(offset)
                                                       .build();
    softly.assertThatThrownBy(() -> test_repo.findAllBranch(error_param_2))
          .isInstanceOf(IllegalArgumentException.class);


    FindAllParam error_param_3 = FindAllParam.builder().start_datetime(start_datetime).end_datetime(end_datetime)
                                                       .subject(subject).order(order).limit(limit).offset(offset)
                                                       .build();
    softly.assertThatThrownBy(() -> test_repo.findAllBranch(error_param_3))
          .isInstanceOf(IllegalArgumentException.class);


    FindAllParam error_param_4 = FindAllParam.builder().start_datetime(start_datetime).end_datetime(end_datetime)
                                                       .subject(subject).order(order).max(max).offset(offset)
                                                       .build();
    softly.assertThatThrownBy(() -> test_repo.findAllBranch(error_param_4))
          .isInstanceOf(IllegalArgumentException.class);


    FindAllParam error_param_5 = FindAllParam.builder().start_datetime(start_datetime).end_datetime(end_datetime)
                                                       .subject(subject).order(order).max(max).limit(limit)
                                                       .build();
    softly.assertThatThrownBy(() -> test_repo.findAllBranch(error_param_5))
          .isInstanceOf(IllegalArgumentException.class);
  }












  /** 
   **********************************************************************************************
   * @brief リポジトリの検索メソッドの比較用エンティティリスト作成のためのストリームを作成する。
   * 
   * @details
   * - 元となるエンティティのリストを渡し、引数で指定したフィルターやソート条件を元に、新しくリストを組み立てる。
   *
   * @param[in] param テスト検索に必要な引数を格納したビルダーエンティティ
   * 
   * @par 大まかな処理の流れ
   * -# 指定されたソート第一条件を用いて、コンパレーターを作成する。その際に、反転が指定されていれば、
	 * 反転を指定して作成する。
	 * -# ソート第二条件が指定されていれば、同じように反転の有無を反映しながら、コンパレータに追加する。
	 * -# ソート第三条件が指定されていれば、同じように反転の有無を反映しながら、コンパレータに追加する。
	 * -# 引数で指定されたスキップ数とリミット数を使い、ストリームを作成して、加工された比較用エンティティ
	 * リストを作成する。その際にフィルターが指定されていれば、それを反映する。
   **********************************************************************************************
   */
	public static <T> List<T> compareListMake(CompareParam<T> param){

    List<T> origin_compare = param.getOrigin_compare();
    Comparator<T> sort_first = param.getSort_first();
    Comparator<T> sort_second = param.getSort_second();
    Comparator<T> sort_third = param.getSort_third();
    
		Comparator<T> comparator = sort_first;

		if(sort_second != null){
			comparator = comparator.thenComparing(sort_second);
		}

		if(sort_second != null && sort_third != null){
			comparator = comparator.thenComparing(sort_third);
		}

		if(param.getFilter() != null){
			return origin_compare.stream()
													 .filter(param.getFilter())
													 .sorted(comparator)
													 .skip(param.getSkip())
													 .limit(param.getLimit())
													 .collect(Collectors.toList());
		}else{
			return origin_compare.stream()
													 .sorted(comparator)
													 .skip(param.getSkip())
													 .limit(param.getLimit())
													 .collect(Collectors.toList());
		}
	}
}
