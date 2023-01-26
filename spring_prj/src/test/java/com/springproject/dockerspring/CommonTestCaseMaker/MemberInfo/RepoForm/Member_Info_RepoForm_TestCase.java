/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.RepoForm
 * 
 * @brief [団員管理]に関するテストケース生成処理のうち、[データベースのフリーワード検索]
 * 関連のテストケースを生成する処理を格納したパッケージ
 * 
 * @details
 * - このパッケージには、テストケースを記述したYAMLファイルとマッピングするエンティティや、
 * 取り込んだテストケースを呼び出し元のテストクラスに提供する機能を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.RepoForm;




/** 
 **************************************************************************************
 * @file Musical_Score_RepoForm_TestCase.java
 * @brief 主に[団員番号]機能のテストにおいて、リポジトリの検索のテストを行う際の検索
 * ワードのテストケースを読み取るクラスを格納したファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Member_Info_TestCase_Make.Member_Info_TestKeys;










/** 
 **************************************************************************************
 * @brief 主に[団員番号]機能のテストにおいて、リポジトリの検索のテストを行う際の検索
 * ワードのテストケースを読み取るクラス。
 * 
 * @details 
 * - テストケースが書かれたYMLファイルを読み込んで、専用のエンティティにマッピングする。
 * - マッピングしたデータは、外部から取得して、他のテストケースクラスで使用可能。
 * 
 * @par 使用アノテーション
 * - @Component
 * 
 * @see Member_Info_RepoForm_Yaml
 **************************************************************************************
 */ 
@Component
public class Member_Info_RepoForm_TestCase{
  
  private final Member_Info_RepoForm_Yaml repoform_yml;



  //** @name 検索種別選択時の数値の定数 */
  //** @{ */

  //! 全検索
  public static final Integer ALL = 1;

  //! 団員番号
  public static final Integer MEMBER_ID = 2;

  //! 名前
  public static final Integer NAME = 3;

  //! ふりがな
  public static final Integer NAME_PRONU = 4;

  //! 性別
  public static final Integer SEX = 5;

  //! 誕生日
  public static final Integer BIRTHDAY = 6;

  //! 入団日
  public static final Integer JOIN_DATE = 7;

  //! 退団日
  public static final Integer RET_DATE = 8;

  //! メールアドレス
  public static final Integer EMAIL = 9;

  //! 電話番号
  public static final Integer TEL = 10;

  //! 現住所郵便番号
  public static final Integer POSTCODE = 11;

  //! 現住所
  public static final Integer ADDR = 12;

  //! 役職名
  public static final Integer POSITION = 13;

  //! 現役職着任日
  public static final Integer ARRI_DATE = 14;

  //! 職種名
  public static final Integer JOB = 15;

  //! 配属部署
  public static final Integer ASSIGN_DEPT = 16;

  //! 配属日
  public static final Integer ASSIGN_DATE = 17;

  //! 担当楽器
  public static final Integer INST_CHARGE = 18;

  //! その他コメント
  public static final Integer COMMENT = 19;

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

  //! 検索開始日
  public static final int START_DATE = 0;

  //! 検索終了日
  public static final int END_DATE = 1;

  //! 1ページ当たりの検索結果の出力限界数(検索ワードが通常の文字列の場合)
  public static final int LIMIT_WORD = 1;

  //! 1ページ当たりの検索結果の出力限界数(検索ワードが日付の期間の場合)
  public static final int LIMIT_DATE = 2;

  //! 検索結果の全件数を出力した際の結果数想定値(検索ワードが通常の文字列の場合)
  public static final int ANSWER_COUNT_WORD = 2;

  //! 検索結果の全件数を出力した際の結果数想定値(検索ワードが日付の期間の場合)
  public static final int ANSWER_COUNT_DATE = 3;

  //! オフセット値(検索ワードが通常の文字列の場合)
  public static final int OFFSET_WORD = 3;

  //! オフセット値(検索ワードが日付の期間の場合)
  public static final int OFFSET_DATE = 4;

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
   * @see Member_Info_RepoForm_Yaml
   **********************************************************************************************
   */ 
  public Member_Info_RepoForm_TestCase() throws IOException{

    try(InputStream in_repoform_yml = new ClassPathResource("TestCaseFile/MemberInfo/member-info-repoform.yml").getInputStream()){
      Yaml yaml = new Yaml();
      this.repoform_yml = yaml.loadAs(in_repoform_yml, Member_Info_RepoForm_Yaml.class);
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
   * - 2: Memberid
   * - 3: Name
   * - 4: NamePronu
   * - 5: Sex
   * - 6: Birthday
   * - 7: JoinDate
   * - 8: RetDate
   * - 9: Email
   * - 10: Tel
   * - 11: Postcode
   * - 12: Addr
   * - 13: Position
   * - 14: ArriDate
   * - 15: Job
   * - 16: AssignDept
   * - 17: AssignDate
   * - 18: InstCharge
   * - 19: Comment
   * 
   * @par 処理の大まかな流れ
   * -# 番号に従い、マッピングしたエンティティから、データを取り出す。
   **********************************************************************************************
   */ 
  public String subjectRepoForm(Integer testcase_num){

    if(testcase_num == null){
      return this.repoform_yml.getSubject().get("ng_miss");
    }else if(testcase_num > 19 || testcase_num < 1){
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
  public String[] otherRepoForm(Member_Info_TestKeys subject_key, Integer testcase_num){

    if(testcase_num > 20 || testcase_num < 1){
      throw new IllegalArgumentException();
    }else{
      switch(subject_key){
        case Member_Id:
          return this.repoform_yml.getMember_id().get("word_" + testcase_num);
        case Name:
          return this.repoform_yml.getName().get("word_" + testcase_num);
        case Name_Pronu:
          return this.repoform_yml.getName_pronu().get("word_" + testcase_num);
        case Sex:
          return this.repoform_yml.getSex().get("word_" + testcase_num);
        case Birthday:
          return this.repoform_yml.getBirthday().get("word_" + testcase_num);
        case Join_Date:
          return this.repoform_yml.getJoin_date().get("word_" + testcase_num);
        case Ret_Date:
          return this.repoform_yml.getRet_date().get("word_" + testcase_num);
        case Email:
          return this.repoform_yml.getEmail().get("word_" + testcase_num);
        case Tel:
          return this.repoform_yml.getTel().get("word_" + testcase_num);
        case Addr_Postcode:
          return this.repoform_yml.getAddr_postcode().get("word_" + testcase_num);
        case Addr:
          return this.repoform_yml.getAddr().get("word_" + testcase_num);
        case Position:
          return this.repoform_yml.getPosition().get("word_" + testcase_num);
        case Position_Arri_Date:
          return this.repoform_yml.getPosition_arri_date().get("word_" + testcase_num);
        case Job:
          return this.repoform_yml.getJob().get("word_" + testcase_num);
        case Assign_Dept:
          return this.repoform_yml.getAssign_dept().get("word_" + testcase_num);
        case Assign_Date:
          return this.repoform_yml.getAssign_date().get("word_" + testcase_num);
        case Inst_Charge:
          return this.repoform_yml.getInst_charge().get("word_" + testcase_num);
        case Other_Comment:
          return this.repoform_yml.getOther_comment().get("word_" + testcase_num);
        default:
          throw new IllegalArgumentException();
      }      
    }
  }
}
