/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.UsageAuthority.Form
 * 
 * @brief [権限管理]に関するテストケース生成処理のうち、[フォームバリデーション]
 * 関連のテストケースを生成する処理を格納したパッケージ
 * 
 * @details
 * - このパッケージには、テストケースを記述したYAMLファイルとマッピングするエンティティや、
 * 取り込んだテストケースを呼び出し元のテストクラスに提供する機能を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.UsageAuthority.Form;





/** 
 **************************************************************************************
 * @file Usage_Authority_Form_TestCase.java
 * @brief 主に[権限管理]機能のテストにおいて、バリデーションエラーになるテストケースを
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

import com.springproject.dockerspring.CommonTestCaseMaker.UsageAuthority.Usage_Authority_TestCase_Make.Usage_Authority_TestKeys;







/** 
 **************************************************************************************
 * @brief 主に[権限管理]機能のテストにおいて、バリデーションエラーになるテストケースを
 * 読み取るクラス。
 * 
 * @details 
 * - テストケースが書かれたYMLファイルを読み込んで、専用のエンティティにマッピングする。
 * - マッピングしたデータは、外部から取得して、他のテストケースクラスで使用可能。
 * 
 * @par 使用アノテーション
 * - @Component
 * 
 * @see Usage_Authority_Form_Yaml
 **************************************************************************************
 */ 
@Component
public class Usage_Authority_Form_TestCase{
  
  private final Usage_Authority_Form_Yaml form_yml;









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
   * @see Usage_Authority_Form_Yaml
   **********************************************************************************************
   */ 
  public Usage_Authority_Form_TestCase() throws IOException{

    try(InputStream in_form_yml = new ClassPathResource("TestCaseFile/UsegeAuthority/usage-authority-form.yml").getInputStream()){
      Yaml yaml = new Yaml();
      this.form_yml = yaml.loadAs(in_form_yml, Usage_Authority_Form_Yaml.class);
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
  public Map<Usage_Authority_TestKeys, String> getNormalData(){

    Map<Usage_Authority_TestKeys, String> normal_map = new HashMap<>();
    String childkey = "normal";

    normal_map.put(Usage_Authority_TestKeys.Serial_Num, this.form_yml.getSerial_num().get(childkey));
    normal_map.put(Usage_Authority_TestKeys.Auth_Id, this.form_yml.getAuth_id().get(childkey));
    normal_map.put(Usage_Authority_TestKeys.Auth_Name, this.form_yml.getAuth_name().get(childkey));
	  normal_map.put(Usage_Authority_TestKeys.Admin, this.form_yml.getAuth_ok().get("auth_ok_admin").get("admin"));
	  normal_map.put(Usage_Authority_TestKeys.Member_Info, this.form_yml.getAuth_ok().get("auth_ok_admin").get("member_info"));
	  normal_map.put(Usage_Authority_TestKeys.Facility, this.form_yml.getAuth_ok().get("auth_ok_admin").get("facility"));
	  normal_map.put(Usage_Authority_TestKeys.Musical_Score, this.form_yml.getAuth_ok().get("auth_ok_admin").get("musical_score"));
	  normal_map.put(Usage_Authority_TestKeys.Sound_Source, this.form_yml.getAuth_ok().get("auth_ok_admin").get("sound_source"));
    
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
  public List<Map<Usage_Authority_TestKeys, String>> getOkData(Boolean new_tolerance){

    List<Map<Usage_Authority_TestKeys, String>> ok_list = new ArrayList<>();
    String childkey = "ok";

    String auth_id = null;
    String serial_num = null;

    if(new_tolerance){
      auth_id = this.form_yml.getAuth_id().get(childkey);
    }else{
      serial_num = this.form_yml.getSerial_num().get(childkey);
      auth_id = this.form_yml.getAuth_id().get("ng_unique");
    }

    String auth_name = this.form_yml.getAuth_name().get(childkey);

    Map<Usage_Authority_TestKeys, String> only_admin = new HashMap<>();
    only_admin.put(Usage_Authority_TestKeys.Serial_Num, serial_num);
    only_admin.put(Usage_Authority_TestKeys.Auth_Id, auth_id);
    only_admin.put(Usage_Authority_TestKeys.Auth_Name, auth_name);
		only_admin.put(Usage_Authority_TestKeys.Admin, this.form_yml.getAuth_ok().get("auth_ok_admin").get("admin"));
		only_admin.put(Usage_Authority_TestKeys.Member_Info, this.form_yml.getAuth_ok().get("auth_ok_admin").get("member_info"));
		only_admin.put(Usage_Authority_TestKeys.Facility, this.form_yml.getAuth_ok().get("auth_ok_admin").get("facility"));
		only_admin.put(Usage_Authority_TestKeys.Musical_Score, this.form_yml.getAuth_ok().get("auth_ok_admin").get("musical_score"));
		only_admin.put(Usage_Authority_TestKeys.Sound_Source, this.form_yml.getAuth_ok().get("auth_ok_admin").get("sound_source"));
    ok_list.add(only_admin);

    String[] key_name = {"auth_ok_1", "auth_ok_2", "auth_ok_3", "auth_ok_4", "auth_ok_5"};

    for(String key: key_name){
      Map<Usage_Authority_TestKeys, String> not_admin = new HashMap<>();
      not_admin.put(Usage_Authority_TestKeys.Serial_Num, serial_num);
      not_admin.put(Usage_Authority_TestKeys.Auth_Id, auth_id);
      not_admin.put(Usage_Authority_TestKeys.Auth_Name, auth_name);
      not_admin.put(Usage_Authority_TestKeys.Admin, this.form_yml.getAuth_ok().get(key).get("admin"));
      not_admin.put(Usage_Authority_TestKeys.Member_Info, this.form_yml.getAuth_ok().get(key).get("other"));
      not_admin.put(Usage_Authority_TestKeys.Facility, this.form_yml.getAuth_ok().get(key).get("other"));
      not_admin.put(Usage_Authority_TestKeys.Musical_Score, this.form_yml.getAuth_ok().get(key).get("other"));
      not_admin.put(Usage_Authority_TestKeys.Sound_Source, this.form_yml.getAuth_ok().get(key).get("other"));
      ok_list.add(not_admin);
    }

    return ok_list;
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
   * @brief 項目[権限番号]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
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
  public List<String> getAuthIdFailedData(Boolean unique_tolerance){

    List<String> fail_list = new ArrayList<>();

    if(!unique_tolerance){
      fail_list.add(this.form_yml.getAuth_id().get("ng_unique"));
    }
    
    fail_list.add(this.form_yml.getAuth_id().get("ng_overflow"));
    fail_list.add(this.form_yml.getAuth_id().get("ng_symbol"));
    fail_list.add(this.form_yml.getAuth_id().get("ng_zenkaku"));
    fail_list.add(this.form_yml.getAuth_id().get("ng_hankaku_kana"));
    fail_list.add(this.form_yml.getAuth_id().get("ng_empty"));
    fail_list.add(this.form_yml.getAuth_id().get("ng_blank"));
    fail_list.add(this.form_yml.getAuth_id().get("ng_blank_zenkaku"));
    fail_list.add(this.form_yml.getAuth_id().get("ng_blank_tab"));
    fail_list.add(this.form_yml.getAuth_id().get("ng_blank_newline"));
    fail_list.add(this.form_yml.getAuth_id().get("ng_null"));

    return fail_list;
  }










  /** 
   **********************************************************************************************
   * @brief 項目[権限名]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getAuthNameFailedData(){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getAuth_name().get("ng_overflow"));
    fail_list.add(this.form_yml.getAuth_name().get("ng_symbol"));
    fail_list.add(this.form_yml.getAuth_name().get("ng_alpha"));
    fail_list.add(this.form_yml.getAuth_name().get("ng_digit"));
    fail_list.add(this.form_yml.getAuth_name().get("ng_hankaku_kana"));
    fail_list.add(this.form_yml.getAuth_name().get("ng_empty"));
    fail_list.add(this.form_yml.getAuth_name().get("ng_blank"));
    fail_list.add(this.form_yml.getAuth_name().get("ng_blank_tab"));
    fail_list.add(this.form_yml.getAuth_name().get("ng_blank_newline"));
    fail_list.add(this.form_yml.getAuth_name().get("ng_null"));

    return fail_list;
  }










  /** 
   **********************************************************************************************
   * @brief 項目[権限内容]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<Map<Usage_Authority_TestKeys, String>> getAuthKindFailedData(){

    List<Map<Usage_Authority_TestKeys, String>> fail_list = new ArrayList<>();

    String[] key_name = {"admin_1", "admin_2", "admin_3", "admin_4", "admin_5", 
                         "miss", "null", "empty", "blank", "blank_zenkaku", "blank_tab", "blank_newline"};

    for(String key: key_name){
      String admin = this.form_yml.getAuth_ng().get("auth_ng_" + key).get("admin");

      Map<Usage_Authority_TestKeys, String> member_info = new HashMap<>();
      member_info.put(Usage_Authority_TestKeys.Admin, admin);
      member_info.put(Usage_Authority_TestKeys.Member_Info, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("target"));
      member_info.put(Usage_Authority_TestKeys.Facility, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("other"));
      member_info.put(Usage_Authority_TestKeys.Musical_Score, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("other"));
      member_info.put(Usage_Authority_TestKeys.Sound_Source, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("other"));
      fail_list.add(member_info);

      Map<Usage_Authority_TestKeys, String> facility = new HashMap<>();
      facility.put(Usage_Authority_TestKeys.Admin, admin);
      facility.put(Usage_Authority_TestKeys.Member_Info, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("other"));
      facility.put(Usage_Authority_TestKeys.Facility, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("target"));
      facility.put(Usage_Authority_TestKeys.Musical_Score, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("other"));
      facility.put(Usage_Authority_TestKeys.Sound_Source, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("other"));
      fail_list.add(facility);

      Map<Usage_Authority_TestKeys, String> musical_score = new HashMap<>();
      musical_score.put(Usage_Authority_TestKeys.Admin, admin);
      musical_score.put(Usage_Authority_TestKeys.Member_Info, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("other"));
      musical_score.put(Usage_Authority_TestKeys.Facility, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("other"));
      musical_score.put(Usage_Authority_TestKeys.Musical_Score, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("target"));
      musical_score.put(Usage_Authority_TestKeys.Sound_Source, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("other"));
      fail_list.add(musical_score);

      Map<Usage_Authority_TestKeys, String> sound_source = new HashMap<>();
      sound_source.put(Usage_Authority_TestKeys.Admin, admin);
      sound_source.put(Usage_Authority_TestKeys.Member_Info, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("other"));
      sound_source.put(Usage_Authority_TestKeys.Facility, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("other"));
      sound_source.put(Usage_Authority_TestKeys.Musical_Score, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("other"));
      sound_source.put(Usage_Authority_TestKeys.Sound_Source, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("target"));
      fail_list.add(sound_source);
    }

    return fail_list;
  }
}
