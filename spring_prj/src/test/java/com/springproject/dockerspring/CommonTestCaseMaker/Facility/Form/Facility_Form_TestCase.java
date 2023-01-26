/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.Facility.Form
 * 
 * @brief [設備管理]に関するテストケース生成処理のうち、[フォームバリデーション]
 * 関連のテストケースを生成する処理を格納したパッケージ
 * 
 * @details
 * - このパッケージには、テストケースを記述したYAMLファイルとマッピングするエンティティや、
 * 取り込んだテストケースを呼び出し元のテストクラスに提供する機能を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.Facility.Form;





/** 
 **************************************************************************************
 * @file Facility_Form_TestCase.java
 * @brief 主に[設備管理]機能のテストにおいて、バリデーションエラーになるテストケースを
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

import com.springproject.dockerspring.CommonTestCaseMaker.Facility.Facility_TestCase_Make.Facility_TestKeys;







/** 
 **************************************************************************************
 * @brief 主に[設備管理]機能のテストにおいて、バリデーションエラーになるテストケースを
 * 読み取るクラス。
 * 
 * @details 
 * - テストケースが書かれたYMLファイルを読み込んで、専用のエンティティにマッピングする。
 * - マッピングしたデータは、外部から取得して、他のテストケースクラスで使用可能。
 * 
 * @par 使用アノテーション
 * - @Component
 * 
 * @see Facility_Form_Yaml
 **************************************************************************************
 */ 
@Component
public class Facility_Form_TestCase{
  
  private final Facility_Form_Yaml form_yml;









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
   * @see Facility_Form_Yaml
   **********************************************************************************************
   */ 
  public Facility_Form_TestCase() throws IOException{

    try(InputStream in_form_yml = new ClassPathResource("TestCaseFile/Facility/facility-form.yml").getInputStream()){
      Yaml yaml = new Yaml();
      this.form_yml = yaml.loadAs(in_form_yml, Facility_Form_Yaml.class);
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
  public Map<Facility_TestKeys, String> getNormalData(){

    Map<Facility_TestKeys, String> normal_map = new HashMap<>();
    String childkey = "normal";

    normal_map.put(Facility_TestKeys.Serial_Num, this.form_yml.getSerial_num().get(childkey));
    normal_map.put(Facility_TestKeys.Faci_Id, this.form_yml.getFaci_id().get(childkey));
    normal_map.put(Facility_TestKeys.Faci_Name, this.form_yml.getFaci_name().get(childkey));
    normal_map.put(Facility_TestKeys.Buy_Date, this.form_yml.getBuy_date().get(childkey));
    normal_map.put(Facility_TestKeys.Producer, this.form_yml.getProducer().get(childkey));
    normal_map.put(Facility_TestKeys.Storage_Loc, this.form_yml.getStorage_loc().get(childkey));
    normal_map.put(Facility_TestKeys.Disp_Date, this.form_yml.getDisp_date().get(childkey));
    normal_map.put(Facility_TestKeys.Other_Comment, this.form_yml.getOther_comment().get(childkey));

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
  public Map<Facility_TestKeys, String> getOkData(Boolean new_tolerance){

    Map<Facility_TestKeys, String> ok_map = new HashMap<>();
    String childkey = "ok";

    if(new_tolerance){
      ok_map.put(Facility_TestKeys.Serial_Num, null);
      ok_map.put(Facility_TestKeys.Faci_Id, this.form_yml.getFaci_id().get(childkey));
    }else{
      ok_map.put(Facility_TestKeys.Serial_Num, this.form_yml.getSerial_num().get(childkey));
      ok_map.put(Facility_TestKeys.Faci_Id, this.form_yml.getFaci_id().get("ng_unique"));
    }
    
    ok_map.put(Facility_TestKeys.Faci_Name, this.form_yml.getFaci_name().get(childkey));
    ok_map.put(Facility_TestKeys.Buy_Date, this.form_yml.getBuy_date().get(childkey));
    ok_map.put(Facility_TestKeys.Producer, this.form_yml.getProducer().get(childkey));
    ok_map.put(Facility_TestKeys.Storage_Loc, this.form_yml.getStorage_loc().get(childkey));
    ok_map.put(Facility_TestKeys.Disp_Date, this.form_yml.getDisp_date().get(childkey));
    ok_map.put(Facility_TestKeys.Other_Comment, this.form_yml.getOther_comment().get(childkey));

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
   * @brief 項目[設備番号]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
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
  public List<String> getFaciIdFailedData(Boolean unique_tolerance){

    List<String> fail_list = new ArrayList<>();

    if(!unique_tolerance){
      fail_list.add(this.form_yml.getFaci_id().get("ng_unique"));
    }
    
    fail_list.add(this.form_yml.getFaci_id().get("ng_overflow"));
    fail_list.add(this.form_yml.getFaci_id().get("ng_symbol"));
    fail_list.add(this.form_yml.getFaci_id().get("ng_zenkaku"));
    fail_list.add(this.form_yml.getFaci_id().get("ng_hankaku_kana"));
    fail_list.add(this.form_yml.getFaci_id().get("ng_empty"));
    fail_list.add(this.form_yml.getFaci_id().get("ng_blank"));
    fail_list.add(this.form_yml.getFaci_id().get("ng_blank_zenkaku"));
    fail_list.add(this.form_yml.getFaci_id().get("ng_blank_tab"));
    fail_list.add(this.form_yml.getFaci_id().get("ng_blank_newline"));
    fail_list.add(this.form_yml.getFaci_id().get("ng_null"));

    return fail_list;
  }










  /** 
   **********************************************************************************************
   * @brief 項目[設備名]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getFaciNameFailedData(){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getFaci_name().get("ng_overflow"));
    fail_list.add(this.form_yml.getFaci_name().get("ng_symbol"));
    fail_list.add(this.form_yml.getFaci_name().get("ng_alpha"));
    fail_list.add(this.form_yml.getFaci_name().get("ng_digit"));
    fail_list.add(this.form_yml.getFaci_name().get("ng_hankaku_kana"));
    fail_list.add(this.form_yml.getFaci_name().get("ng_empty"));
    fail_list.add(this.form_yml.getFaci_name().get("ng_blank"));
    fail_list.add(this.form_yml.getFaci_name().get("ng_blank_tab"));
    fail_list.add(this.form_yml.getFaci_name().get("ng_blank_newline"));
    fail_list.add(this.form_yml.getFaci_name().get("ng_null"));

    return fail_list;
  }









  /** 
   **********************************************************************************************
   * @brief 項目[購入日]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
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
  public List<String> getBuyDateFailedData(Boolean output_valid){

    List<String> fail_list = new ArrayList<>();

    if(!output_valid){
      fail_list.add(this.form_yml.getBuy_date().get("ng_no_date"));
      fail_list.add(this.form_yml.getBuy_date().get("ng_no_format"));
    }

    fail_list.add(this.form_yml.getBuy_date().get("ng_null"));

    return fail_list;
  }










  /** 
   **********************************************************************************************
   * @brief 項目[製作者]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getProducerFailedData(){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getProducer().get("ng_overflow"));
    fail_list.add(this.form_yml.getProducer().get("ng_symbol"));
    fail_list.add(this.form_yml.getProducer().get("ng_alpha"));
    fail_list.add(this.form_yml.getProducer().get("ng_digit"));
    fail_list.add(this.form_yml.getProducer().get("ng_hankaku_kana"));
    fail_list.add(this.form_yml.getProducer().get("ng_empty"));
    fail_list.add(this.form_yml.getProducer().get("ng_blank"));
    fail_list.add(this.form_yml.getProducer().get("ng_blank_tab"));
    fail_list.add(this.form_yml.getProducer().get("ng_blank_newline"));
    fail_list.add(this.form_yml.getProducer().get("ng_null"));

    return fail_list;
  }









  /** 
   **********************************************************************************************
   * @brief 項目[保管場所]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getStolageLocFailedData(){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getStorage_loc().get("ng_overflow"));
    fail_list.add(this.form_yml.getStorage_loc().get("ng_symbol"));
    fail_list.add(this.form_yml.getStorage_loc().get("ng_alpha"));
    fail_list.add(this.form_yml.getStorage_loc().get("ng_digit"));
    fail_list.add(this.form_yml.getStorage_loc().get("ng_hankaku_kana"));

    return fail_list;
  }










  /** 
   **********************************************************************************************
   * @brief 項目[廃棄日]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
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
  public List<String> getDispDateFailedData(Boolean output_valid){

    List<String> fail_list = new ArrayList<>();

    if(!output_valid){
      fail_list.add(this.form_yml.getDisp_date().get("ng_no_date"));
      fail_list.add(this.form_yml.getDisp_date().get("ng_no_format"));
    }

    fail_list.add(this.form_yml.getDisp_date().get("ng_before_buy_date"));

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
