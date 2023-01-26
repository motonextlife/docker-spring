/** 
 **************************************************************************************
 * @file History_RepoForm_TestCase.java
 * @brief 主に[共通の履歴機能]機能のテストにおいて、リポジトリの検索のテストを行う際の検索
 * ワードのテストケースを読み取るクラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.History.RepoForm;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import com.springproject.dockerspring.CommonTestCaseMaker.History.History_TestCase_Make.History_TestKeys;










/** 
 **************************************************************************************
 * @brief 主に[共通の履歴機能]機能のテストにおいて、バリデーションエラーになるテストケースを
 * 読み取るクラス。
 * 
 * @details 
 * - テストケースが書かれたYMLファイルを読み込んで、専用のエンティティにマッピングする。
 * - マッピングしたデータは、外部から取得して、他のテストケースクラスで使用可能。
 * 
 * @par 使用アノテーション
 * - @Component
 * 
 * @see Common_Hist_RepoForm_Yaml
 **************************************************************************************
 */ 
@Component
public class History_RepoForm_TestCase{
  
  private final Common_Hist_RepoForm_Yaml repoform_yml;


  //** @name 検索種別選択時の数値の定数 */
  //** @{ */

  //! 履歴日時
  public static final Integer CHANGE_DATETIME = 1;

  //! 管理番号
  public static final Integer ID = 2;

  //! 履歴種別
  public static final Integer CHANGE_KINDS = 3;

  //! 操作ユーザー名
  public static final Integer OPERATION_USER = 4;

  //! 不正種別
  public static final Integer MISSING = null;

  //** @} */



  //** @name 検索時のテストケースの配列のインデックス */
  //** @{ */

  //! 検索開始日時
  public static final int START_DATETIME = 0;

  //! 検索終了日時
  public static final int END_DATETIME = 1;

  //! 検索ワード
  public static final int WORD = 0;

  //! 1ページ当たりの検索結果の出力限界数(履歴日時単体での検索時)
  public static final int LIMIT_SINGLE = 2;

  //! 1ページ当たりの検索結果の出力限界数(履歴日時と検索ワードを組み合わせた検索時)
  public static final int LIMIT_DOUBLE = 1;

  //! 検索結果の全件数を出力した際の結果数想定値(履歴日時単体での検索時)
  public static final int ANSWER_COUNT_SINGLE = 3;

  //! 検索結果の全件数を出力した際の結果数想定値(履歴日時と検索ワードを組み合わせた検索時)
  public static final int ANSWER_COUNT_DOUBLE = 2;

  //! オフセット値(履歴日時単体での検索時)
  public static final int OFFSET_SINGLE = 4;

  //! オフセット値(履歴日時と検索ワードを組み合わせた検索時)
  public static final int OFFSET_DOUBLE = 3;

  //** @} */








  


  /** 
   **********************************************************************************************
   * @brief YMLファイルからデータを読み取って専用エンティティへのマッピングを行う。
   * 
   * @details
   * - テストケースが書かれたYMLファイルを読み込んで、専用のエンティティにマッピングする。
   * 
   * @par 処理の大まかな流れ
   * -# テストケースのYMLファイルを読み込む。
   * -# 読み込んだファイルを、専用のエンティティにマッピングする。
   * -# マッピングしたデータは、フィールド変数に格納する。
   * 
   * @throw IOException
   * 
   * @see Common_Hist_RepoForm_Yaml
   **********************************************************************************************
   */ 
  public History_RepoForm_TestCase() throws IOException{

    try(InputStream in_repoform_yml = new ClassPathResource("TestCaseFile/History/common-hist-repoform.yml").getInputStream()){
      Yaml yaml = new Yaml();
      this.repoform_yml = yaml.loadAs(in_repoform_yml, Common_Hist_RepoForm_Yaml.class);
    }
  }









  /** 
   **********************************************************************************************
   * @brief マッピングエンティティから読み取った[検索種別]を出力する。
   * 
   * @details
   * - 引数で渡された番号に従い、対応する番号のテストケースを出力する。
   * - 「Null」が渡された際は、エラーとなるテストケースを出力する。
   * - 範囲外の数字が来たときは処理を続行できないのでエラーを投げる。
   * 
   * @param[in] testcase_num 指定するテストケースの番号
   * 
   * @return 番号に対応するデータ
   * 
   * @note 引数の番号の項目対応表
   * - 1: ChangeDatetime
   * - 2: Id
   * - 3: ChangeKinds
   * - 4: OperationUser
   * 
   * @par 処理の大まかな流れ
   * -# 番号に従い、マッピングしたエンティティから、データを取り出す。
   **********************************************************************************************
   */ 
  public String historySubjectRepoForm(Integer testcase_num){

    if(testcase_num == null){
      return this.repoform_yml.getSubject().get("ng_miss");
    }else if(testcase_num > 4 || testcase_num < 1){
      throw new IllegalArgumentException();
    }else{
      return this.repoform_yml.getSubject().get("ok_" + testcase_num);
    }
  }
  









  /** 
   **********************************************************************************************
   * @brief マッピングエンティティから読み取った[検索種別以外]のテストケースを出力する。
   * 
   * @details
   * - 引数で渡された番号と検索種別に従い、対応する番号のテストケースを出力する。
   * - 範囲外の数字が来たときは処理を続行できないのでエラーを投げる。
   * 
   * @param[in] subject_key 指定する検索種別
   * @param[in] testcase_num 指定するテストケースの番号
   * 
   * @return 番号に対応するデータ
   * 
   * @par 処理の大まかな流れ
   * -# 番号と検索種別に従い、マッピングしたエンティティから、データを取り出す。
   **********************************************************************************************
   */ 
  public String[] historyOtherRepoForm(History_TestKeys subject_key, Integer testcase_num){

    if(testcase_num > 20 || testcase_num < 1){
      throw new IllegalArgumentException();
    }else{
      switch(subject_key){
        case Id:
          return this.repoform_yml.getId().get("word_" + testcase_num);
        case Change_Datetime:
          return this.repoform_yml.getChange_datetime().get("word_" + testcase_num);
        case Change_Kinds:
          return this.repoform_yml.getChange_kinds().get("word_" + testcase_num);
        case Operation_User:
          return this.repoform_yml.getOperation_user().get("word_" + testcase_num);
        default:
          throw new IllegalArgumentException();
      }      
    }
  }
}
