/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.SystemUser.RepoForm
 * 
 * @brief [システムユーザー管理]に関するテストケース生成処理のうち、[データベースのフリーワード検索]
 * 関連のテストケースを生成する処理を格納したパッケージ
 * 
 * @details
 * - このパッケージには、テストケースを記述したYAMLファイルとマッピングするエンティティや、
 * 取り込んだテストケースを呼び出し元のテストクラスに提供する機能を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.SystemUser.RepoForm;





/** 
 **************************************************************************************
 * @file System_User_RepoForm_TestCase.java
 * @brief 主に[ログインユーザー管理]機能のテストにおいて、リポジトリの検索のテストを行う際の検索
 * ワードのテストケースを読み取るクラスを格納したファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.io.IOException;
import java.io.InputStream;

import com.springproject.dockerspring.CommonTestCaseMaker.SystemUser.System_User_TestCase_Make.System_User_TestKeys;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;







/** 
 **************************************************************************************
 * @brief 主に[システムユーザー管理]機能のテストにおいて、バリデーションエラーになるテストケースを
 * 読み取るクラス。
 * 
 * @details 
 * - テストケースが書かれたYMLファイルを読み込んで、専用のエンティティにマッピングする。
 * - マッピングしたデータは、外部から取得して、他のテストケースクラスで使用可能。
 * 
 * @par 使用アノテーション
 * - @Component
 * 
 * @see System_User_RepoForm_Yaml
 **************************************************************************************
 */ 
@Component
public class System_User_RepoForm_TestCase{

  private final System_User_RepoForm_Yaml repoform_yml;



  //** @name 検索種別選択時の数値の定数 */
  //** @{ */

  //! 全検索
  public static final Integer ALL = 1;

  //! 団員番号
  public static final Integer MEMBER_ID = 2;

  //! ユーザー名
  public static final Integer USERNAME = 3;

  //! 権限番号
  public static final Integer AUTH_ID = 4;

  //! ロック有無
  public static final Integer LOCKING = 5;

  //! 不正種別
  public static final Integer MISSING = null;

  //! 全検索時の想定される検索結果数
  public static final Integer ALL_ANSWER = 1;

  //! 全検索時に設定する検索限界値
  public static final Integer ALL_MAX = 2;

  //! 全検索時に設定するオフセット値
  public static final Integer ALL_OFFSET = 3;

  //! 検索結果数にリミットを設けた場合の想定される検索結果数
  public static final Integer LIMIT_ANSWER = 4;

  //! 検索結果数にリミットを設けた場合に設定する検索限界値
  public static final Integer LIMIT_MAX = 5;

  //! 検索結果数にリミットを設けた場合に設定するオフセット値
  public static final Integer LIMIT_OFFSET = 6;

  //** @} */







  //** @name 検索時のテストケースの配列のインデックス */
  //** @{ */

  //! 検索ワード
  public static final int WORD = 0;

  //! 1ページ当たりの検索結果の出力限界数
  public static final int LIMIT = 1;

  //! 検索結果の全件数を出力した際の結果数想定値
  public static final int ANSWER_COUNT = 2;

  //! オフセット値
  public static final int OFFSET = 3;

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
   * @see System_User_RepoForm_Yaml
   **********************************************************************************************
   */ 
  public System_User_RepoForm_TestCase() throws IOException{

    try(InputStream in_repoform_yml = new ClassPathResource("TestCaseFile/SystemUser/system-user-repoform.yml").getInputStream()){
      Yaml yaml = new Yaml();
      this.repoform_yml = yaml.loadAs(in_repoform_yml, System_User_RepoForm_Yaml.class);
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
   * - 1: All
   * - 2: MemberId
   * - 3: Username
   * - 4: AuthId
   * - 5: Locking
   * 
   * @par 処理の大まかな流れ
   * -# 番号に従い、マッピングしたエンティティから、データを取り出す。
   **********************************************************************************************
   */ 
  public String subjectRepoForm(Integer testcase_num){

    if(testcase_num == null){
      return this.repoform_yml.getSubject().get("ng_miss");
    }else if(testcase_num > 5 || testcase_num < 1){
      throw new IllegalArgumentException();
    }else{
      return this.repoform_yml.getSubject().get("ok_" + testcase_num);
    }
  }



  





  /** 
   **********************************************************************************************
   * @brief マッピングエンティティから読み取った[全検索時]を出力する。
   * 
   * @details
   * - 引数で渡された番号に従い、対応する番号のテストケースを出力する。
   * - 範囲外の数字が来たときは処理を続行できないのでエラーを投げる。
   * 
   * @param[in] testcase_num 指定するテストケースの番号
   * 
   * @return 番号に対応するデータ
   * 
   * @note 引数の番号の項目対応表
   * - 1: all_answer
   * - 2: all_max
   * - 3: all_offset
   * - 4: limit_answer
   * - 5: limit_max
   * - 6: limit_offset
   * 
   * @par 処理の大まかな流れ
   * -# 番号に従い、マッピングしたエンティティから、データを取り出す。
   **********************************************************************************************
   */ 
  public Integer allRepoForm(Integer testcase_num){

    String[] key_name = {"all_answer", "all_max", "all_offset", "limit_answer", "limit_max", "limit_offset"};

    if(testcase_num > 6 || testcase_num < 1){
      throw new IllegalArgumentException();
    }else{
      return this.repoform_yml.getAll().get(key_name[testcase_num - 1]);
    }
  }









  /** 
   **********************************************************************************************
   * @brief マッピングエンティティから読み取った[管理番号存在確認]のテストケースを出力する。
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
  public String[] existRemainRepoForm(System_User_TestKeys subject_key, Integer testcase_num){
    
    if(testcase_num > 10 || testcase_num < 1){
      throw new IllegalArgumentException();
    }else{
      switch(subject_key){
        case Member_Id:
          return this.repoform_yml.getExist_remain_member().get("word_" + testcase_num);
        case Auth_Id:
          return this.repoform_yml.getExist_remain_auth().get("word_" + testcase_num);
        default:
          throw new IllegalArgumentException();
      }      
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
  public String[] otherRepoForm(System_User_TestKeys subject_key, Integer testcase_num){

    if(testcase_num > 20 || testcase_num < 1){
      throw new IllegalArgumentException();
    }else{
      switch(subject_key){
        case Member_Id:
          return this.repoform_yml.getMember_id().get("word_" + testcase_num);
        case Username:
          return this.repoform_yml.getUsername().get("word_" + testcase_num);
        case Auth_Id:
          return this.repoform_yml.getAuth_id().get("word_" + testcase_num);
        case Locking:
          return this.repoform_yml.getLocking().get("word_" + testcase_num);
        default:
          throw new IllegalArgumentException();
      }      
    }
  }
}
