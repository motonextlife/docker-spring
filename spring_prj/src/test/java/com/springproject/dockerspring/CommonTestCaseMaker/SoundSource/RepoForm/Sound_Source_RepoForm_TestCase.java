/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.SoundSource.RepoForm
 * 
 * @brief [音源管理]に関するテストケース生成処理のうち、[データベースのフリーワード検索]
 * 関連のテストケースを生成する処理を格納したパッケージ
 * 
 * @details
 * - このパッケージには、テストケースを記述したYAMLファイルとマッピングするエンティティや、
 * 取り込んだテストケースを呼び出し元のテストクラスに提供する機能を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.SoundSource.RepoForm;





/** 
 **************************************************************************************
 * @file Sound_Source_RepoForm_TestCase.java
 * @brief 主に[音源番号]機能のテストにおいて、リポジトリの検索のテストを行う際の検索
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

import com.springproject.dockerspring.CommonTestCaseMaker.SoundSource.Sound_Source_TestCase_Make.Sound_Source_TestKeys;








/** 
 **************************************************************************************
 * @brief 主に[音源番号]機能のテストにおいて、リポジトリの検索のテストを行う際の検索
 * ワードのテストケースを読み取るクラス。
 * 
 * @details 
 * - テストケースが書かれたYMLファイルを読み込んで、専用のエンティティにマッピングする。
 * - マッピングしたデータは、外部から取得して、他のテストケースクラスで使用可能。
 * 
 * @par 使用アノテーション
 * - @Component
 * 
 * @see Sound_Source_RepoForm_Yaml
 **************************************************************************************
 */ 
@Component
public class Sound_Source_RepoForm_TestCase{
  
  private final Sound_Source_RepoForm_Yaml repoform_yml;


  //** @name 検索種別選択時の数値の定数 */
  //** @{ */

  //! 全検索
  public static final Integer ALL = 1;

  //! 音源番号
  public static final Integer SOUND_ID = 2;

  //! 登録日
  public static final Integer UPLOAD_DATE = 3;

  //! 曲名
  public static final Integer SONG_TITLE = 4;

  //! 作曲者
  public static final Integer COMPOSER = 5;

  //! 演奏者
  public static final Integer PERFORMER = 6;

  //! 出版社
  public static final Integer PUBLISHER = 7;

  //! その他コメント
  public static final Integer COMMENT = 8;

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
   * @see Sound_Source_RepoForm_Yaml
   **********************************************************************************************
   */ 
  public Sound_Source_RepoForm_TestCase() throws IOException{

    try(InputStream in_repoform_yml = new ClassPathResource("TestCaseFile/SoundSource/sound-source-repoform.yml").getInputStream()){
      Yaml yaml = new Yaml();
      this.repoform_yml = yaml.loadAs(in_repoform_yml, Sound_Source_RepoForm_Yaml.class);
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
   * - 2: SoundId
   * - 3: UploadDate
   * - 4: SongTitle
   * - 5: Composer
   * - 6: Performer
   * - 7: Publisher
   * - 8: Comment
   * 
   * @par 処理の大まかな流れ
   * -# 番号に従い、マッピングしたエンティティから、データを取り出す。
   **********************************************************************************************
   */ 
  public String subjectRepoForm(Integer testcase_num){

    if(testcase_num == null){
      return this.repoform_yml.getSubject().get("ng_miss");
    }else if(testcase_num > 10 || testcase_num < 1){
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
  public String[] otherRepoForm(Sound_Source_TestKeys subject_key, Integer testcase_num){

    if(testcase_num > 20 || testcase_num < 1){
      throw new IllegalArgumentException();
    }else{
      switch(subject_key){
        case Sound_Id:
          return this.repoform_yml.getSound_id().get("word_" + testcase_num);
        case Upload_Date:
          return this.repoform_yml.getUpload_date().get("word_" + testcase_num);
        case Song_Title:
          return this.repoform_yml.getSong_title().get("word_" + testcase_num);
        case Composer:
          return this.repoform_yml.getComposer().get("word_" + testcase_num);
        case Performer:
          return this.repoform_yml.getPerformer().get("word_" + testcase_num);
        case Publisher:
          return this.repoform_yml.getPublisher().get("word_" + testcase_num);
        case Other_Comment:
          return this.repoform_yml.getOther_comment().get("word_" + testcase_num);
        default:
          throw new IllegalArgumentException();
      }      
    }
  }
}
