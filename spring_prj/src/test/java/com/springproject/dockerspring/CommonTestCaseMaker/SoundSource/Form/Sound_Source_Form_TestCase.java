/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.SoundSource.Form
 * 
 * @brief [音源管理]に関するテストケース生成処理のうち、[フォームバリデーション]
 * 関連のテストケースを生成する処理を格納したパッケージ
 * 
 * @details
 * - このパッケージには、テストケースを記述したYAMLファイルとマッピングするエンティティや、
 * 取り込んだテストケースを呼び出し元のテストクラスに提供する機能を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.SoundSource.Form;





/** 
 **************************************************************************************
 * @file Sound_Source_Form_TestCase.java
 * @brief 主に[音源番号]機能のテストにおいて、バリデーションエラーになるテストケースを
 * 読み取るクラスを格納するファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import com.springproject.dockerspring.CommonTestCaseMaker.SoundSource.Sound_Source_TestCase_Make.Sound_Source_TestKeys;








/** 
 **************************************************************************************
 * @brief 主に[音源番号]機能のテストにおいて、バリデーションエラーになるテストケースを
 * 読み取るクラス。
 * 
 * @details 
 * - テストケースが書かれたYMLファイルを読み込んで、専用のエンティティにマッピングする。
 * - マッピングしたデータは、外部から取得して、他のテストケースクラスで使用可能。
 * 
 * @par 使用アノテーション
 * - @Component
 * 
 * @see Sound_Source_Form_Yaml
 **************************************************************************************
 */ 
@Component
public class Sound_Source_Form_TestCase{
  
  private final Sound_Source_Form_Yaml form_yml;










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
   * @see Sound_Source_Form_Yaml
   **********************************************************************************************
   */ 
  public Sound_Source_Form_TestCase() throws IOException{

    try(InputStream in_form_yml = new ClassPathResource("TestCaseFile/SoundSource/sound-source-form.yml").getInputStream()){
      Yaml yaml = new Yaml();
      this.form_yml = yaml.loadAs(in_form_yml, Sound_Source_Form_Yaml.class);
    }
  }



  






  /** 
   **********************************************************************************************
   * @brief バリデーションにおいて特に問題ないテストケースを読み込んで、マップリストを作成する。
   * 
   * @details
   * - このテストケースに関しては、バリデーション以外の機能でのテストに用いる。つまり、適当な値がほしい際に
   * 用いることが可能。
   * 
   * @return 特に問題ないテストケースが格納されたマップリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータは、専用の列挙型のキーと紐つけて、マップリストに格納する。
   * -# 格納が終わったマップリストを、戻り値とする。
   **********************************************************************************************
   */ 
  public Map<Sound_Source_TestKeys, String> getNormalData(){

    Map<Sound_Source_TestKeys, String> normal_map = new HashMap<>();
    String childkey = "normal";

    normal_map.put(Sound_Source_TestKeys.Serial_Num, this.form_yml.getSerial_num().get(childkey));
    normal_map.put(Sound_Source_TestKeys.Sound_Id, this.form_yml.getSound_id().get(childkey));
    normal_map.put(Sound_Source_TestKeys.Upload_Date, this.form_yml.getUpload_date().get(childkey));
    normal_map.put(Sound_Source_TestKeys.Song_Title, this.form_yml.getSong_title().get(childkey));
    normal_map.put(Sound_Source_TestKeys.Composer, this.form_yml.getComposer().get(childkey));
    normal_map.put(Sound_Source_TestKeys.Performer, this.form_yml.getPerformer().get(childkey));
    normal_map.put(Sound_Source_TestKeys.Publisher, this.form_yml.getPublisher().get(childkey));
    normal_map.put(Sound_Source_TestKeys.Other_Comment, this.form_yml.getOther_comment().get(childkey));

    return normal_map;
  }









  /** 
   **********************************************************************************************
   * @brief バリデーションで合格するテストケースを読み込んで、マップリストを作成する。
   * 
   * @details
   * - シリアルナンバーのバリデーションは、新規追加と更新の時で挙動が違うため、フラグを指定する事により
   * その二つのパターンに対応できるテストケースを取得可能。内容としては、「Null」を許容するかしないか。
   * 
   * @param[in] new_tolerance 新規追加時のバリデーションテスト時は「True」、更新時のテスト時は「False」
   * を指定。
   * 
   * @return バリデーションに合格するマップリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータは、専用の列挙型のキーと紐つけて、マップリストに格納する。
   * -# 格納が終わったマップリストを、戻り値とする。
   **********************************************************************************************
   */ 
  public Map<Sound_Source_TestKeys, String> getOkData(Boolean new_tolerance){

    Map<Sound_Source_TestKeys, String> ok_map = new HashMap<>();
    String childkey = "ok";

    if(new_tolerance){
      ok_map.put(Sound_Source_TestKeys.Serial_Num, null);
      ok_map.put(Sound_Source_TestKeys.Sound_Id, this.form_yml.getSound_id().get(childkey));
    }else{
      ok_map.put(Sound_Source_TestKeys.Serial_Num, this.form_yml.getSerial_num().get(childkey));
      ok_map.put(Sound_Source_TestKeys.Sound_Id, this.form_yml.getSound_id().get("ng_unique"));
    }
    
    ok_map.put(Sound_Source_TestKeys.Upload_Date, this.form_yml.getUpload_date().get(childkey));
    ok_map.put(Sound_Source_TestKeys.Song_Title, this.form_yml.getSong_title().get(childkey));
    ok_map.put(Sound_Source_TestKeys.Composer, this.form_yml.getComposer().get(childkey));
    ok_map.put(Sound_Source_TestKeys.Performer, this.form_yml.getPerformer().get(childkey));
    ok_map.put(Sound_Source_TestKeys.Publisher, this.form_yml.getPublisher().get(childkey));
    ok_map.put(Sound_Source_TestKeys.Other_Comment, this.form_yml.getOther_comment().get(childkey));

    return ok_map;
  }









  /** 
   **********************************************************************************************
   * @brief 項目[シリアルナンバー]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @details
   * - フラグを指定する事で、フォーム入力時のバリデーションテストと、データ出力時のバリデーションテストの
   * 使い分けが可能。
   * - 入力時と出力時のエラーの項目には違いがあり、出力時に関してはデータベースに保存できないテストケースを
   * 除外している。
   * - シリアルナンバーのバリデーションは、新規追加と更新の時で挙動が違うため、フラグを指定する事により
   * その二つのパターンに対応できるテストケースを取得可能。内容としては、「Null」を許容するかしないか。
   * 
   * @param[in] null_tolelance 新規追加のバリデーション時は「True」、更新時の場合は「False」を指定。
   * @param[in] output_valid 入力時のバリデーションテスト時は「False」、出力時のバリデーションテスト時は
   * 「True」を指定して使用する。
   * 
   * @return バリデーションでエラーになるデータのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getSerialNumFailedData(Boolean null_tolelance, Boolean output_valid){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getSerial_num().get("ng_overflow"));
    fail_list.add(this.form_yml.getSerial_num().get("ng_negative"));

    if(!output_valid){
      fail_list.add(this.form_yml.getSerial_num().get("ng_double"));
      fail_list.add(this.form_yml.getSerial_num().get("ng_no_digit"));
    }

    if(!null_tolelance){
      fail_list.add(null);
    }
    
    return fail_list;
  }










  /** 
   **********************************************************************************************
   * @brief 項目[音源番号]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @param[in] unique_tolerance 重複検査をする際は「False」、しない際は「True」となる。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getSoundIdFailedData(Boolean unique_tolerance){

    List<String> fail_list = new ArrayList<>();

    if(!unique_tolerance){
      fail_list.add(this.form_yml.getSound_id().get("ng_unique"));
    }

    fail_list.add(this.form_yml.getSound_id().get("ng_overflow"));
    fail_list.add(this.form_yml.getSound_id().get("ng_symbol"));
    fail_list.add(this.form_yml.getSound_id().get("ng_zenkaku"));
    fail_list.add(this.form_yml.getSound_id().get("ng_hankaku_kana"));
    fail_list.add(this.form_yml.getSound_id().get("ng_empty"));
    fail_list.add(this.form_yml.getSound_id().get("ng_blank"));
    fail_list.add(this.form_yml.getSound_id().get("ng_blank_zenkaku"));
    fail_list.add(this.form_yml.getSound_id().get("ng_blank_tab"));
    fail_list.add(this.form_yml.getSound_id().get("ng_blank_newline"));
    fail_list.add(this.form_yml.getSound_id().get("ng_null"));

    return fail_list;
  }









  /** 
   **********************************************************************************************
   * @brief 項目[登録日]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @details
   * - フラグを指定する事で、フォーム入力時のバリデーションテストと、データ出力時のバリデーションテストの
   * 使い分けが可能。
   * - 入力時と出力時のエラーの項目には違いがあり、出力時に関してはデータベースに保存できないテストケースを
   * 除外している。
   * 
   * @param[in] output_valid 入力時のバリデーションテスト時は「False」、出力時のバリデーションテスト時は
   * 「True」を指定して使用する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getUploadDateFailedData(Boolean output_valid){

    List<String> fail_list = new ArrayList<>();

    if(!output_valid){
      fail_list.add(this.form_yml.getUpload_date().get("ng_no_date"));
      fail_list.add(this.form_yml.getUpload_date().get("ng_no_format"));
    }

    fail_list.add(this.form_yml.getUpload_date().get("ng_null"));

    return fail_list;
  }









  /** 
   **********************************************************************************************
   * @brief 項目[曲名]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getSongTitleFailedData(){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getSong_title().get("ng_overflow"));
    fail_list.add(this.form_yml.getSong_title().get("ng_symbol"));
    fail_list.add(this.form_yml.getSong_title().get("ng_alpha"));
    fail_list.add(this.form_yml.getSong_title().get("ng_digit"));
    fail_list.add(this.form_yml.getSong_title().get("ng_hankaku_kana"));
    fail_list.add(this.form_yml.getSong_title().get("ng_empty"));
    fail_list.add(this.form_yml.getSong_title().get("ng_blank"));
    fail_list.add(this.form_yml.getSong_title().get("ng_blank_tab"));
    fail_list.add(this.form_yml.getSong_title().get("ng_blank_newline"));
    fail_list.add(this.form_yml.getSong_title().get("ng_null"));

    return fail_list;
  }










  /** 
   **********************************************************************************************
   * @brief 項目[作曲者]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getComposerFailedData(){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getComposer().get("ng_overflow"));
    fail_list.add(this.form_yml.getComposer().get("ng_symbol"));
    fail_list.add(this.form_yml.getComposer().get("ng_alpha"));
    fail_list.add(this.form_yml.getComposer().get("ng_digit"));
    fail_list.add(this.form_yml.getComposer().get("ng_hankaku_kana"));
    fail_list.add(this.form_yml.getComposer().get("ng_empty"));
    fail_list.add(this.form_yml.getComposer().get("ng_blank"));
    fail_list.add(this.form_yml.getComposer().get("ng_blank_tab"));
    fail_list.add(this.form_yml.getComposer().get("ng_blank_newline"));
    fail_list.add(this.form_yml.getComposer().get("ng_null"));

    return fail_list;
  }









  /** 
   **********************************************************************************************
   * @brief 項目[演奏者]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getPerformerFailedData(){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getPerformer().get("ng_overflow"));
    fail_list.add(this.form_yml.getPerformer().get("ng_symbol"));
    fail_list.add(this.form_yml.getPerformer().get("ng_alpha"));
    fail_list.add(this.form_yml.getPerformer().get("ng_digit"));
    fail_list.add(this.form_yml.getPerformer().get("ng_hankaku_kana"));
    fail_list.add(this.form_yml.getPerformer().get("ng_empty"));
    fail_list.add(this.form_yml.getPerformer().get("ng_blank"));
    fail_list.add(this.form_yml.getPerformer().get("ng_blank_tab"));
    fail_list.add(this.form_yml.getPerformer().get("ng_blank_newline"));
    fail_list.add(this.form_yml.getPerformer().get("ng_null"));

    return fail_list;
  }









  /** 
   **********************************************************************************************
   * @brief 項目[出版社]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getPublisherFailedData(){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getPublisher().get("ng_overflow"));
    fail_list.add(this.form_yml.getPublisher().get("ng_symbol"));
    fail_list.add(this.form_yml.getPublisher().get("ng_alpha"));
    fail_list.add(this.form_yml.getPublisher().get("ng_digit"));
    fail_list.add(this.form_yml.getPublisher().get("ng_hankaku_kana"));

    return fail_list;
  }









  /** 
   **********************************************************************************************
   * @brief 項目[その他コメント]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getOtherCommentFailedData(){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getOther_comment().get("ng_overflow"));
    fail_list.add(this.form_yml.getOther_comment().get("ng_symbol"));
    fail_list.add(this.form_yml.getOther_comment().get("ng_alpha"));
    fail_list.add(this.form_yml.getOther_comment().get("ng_digit"));
    fail_list.add(this.form_yml.getOther_comment().get("ng_hankaku_kana"));

    return fail_list;
  }
}
