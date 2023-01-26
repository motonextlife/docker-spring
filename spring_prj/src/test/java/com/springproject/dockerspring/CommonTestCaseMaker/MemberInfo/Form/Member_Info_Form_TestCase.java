/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Form
 * 
 * @brief [団員管理]に関するテストケース生成処理のうち、[フォームバリデーション]
 * 関連のテストケースを生成する処理を格納したパッケージ
 * 
 * @details
 * - このパッケージには、テストケースを記述したYAMLファイルとマッピングするエンティティや、
 * 取り込んだテストケースを呼び出し元のテストクラスに提供する機能を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Form;





/** 
 **************************************************************************************
 * @file Member_Info_Form_TestCase.java
 * @brief 主に[団員管理]機能のテストにおいて、バリデーションエラーになるテストケースを
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

import com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Member_Info_TestCase_Make.Member_Info_TestKeys;









/** 
 **************************************************************************************
 * @brief 主に[団員管理]機能のテストにおいて、バリデーションエラーになるテストケースを
 * 読み取るクラス。
 * 
 * @details 
 * - テストケースが書かれたYMLファイルを読み込んで、専用のエンティティにマッピングする。
 * - マッピングしたデータは、外部から取得して、他のテストケースクラスで使用可能。
 * 
 * @par 使用アノテーション
 * - @Component
 * 
 * @see Member_Info_Form_Yaml
 **************************************************************************************
 */ 
@Component
public class Member_Info_Form_TestCase{
  
  private final Member_Info_Form_Yaml form_yml;










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
   * @see Member_Info_Form_Yaml
   **********************************************************************************************
   */ 
  public Member_Info_Form_TestCase() throws IOException{

    try(InputStream in_form_yml = new ClassPathResource("TestCaseFile/MemberInfo/member-info-form.yml").getInputStream()){
      Yaml yaml = new Yaml();
      this.form_yml = yaml.loadAs(in_form_yml, Member_Info_Form_Yaml.class);
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
  public Map<Member_Info_TestKeys, String> getNormalData(){

    Map<Member_Info_TestKeys, String> normal_map = new HashMap<>();
    String childkey = "normal";

    normal_map.put(Member_Info_TestKeys.Serial_Num, this.form_yml.getSerial_num().get(childkey));
    normal_map.put(Member_Info_TestKeys.Member_Id, this.form_yml.getMember_id().get(childkey));
    normal_map.put(Member_Info_TestKeys.Name, this.form_yml.getName().get(childkey));
    normal_map.put(Member_Info_TestKeys.Name_Pronu, this.form_yml.getName_pronu().get(childkey));
    normal_map.put(Member_Info_TestKeys.Sex, this.form_yml.getSex().get(childkey));
    normal_map.put(Member_Info_TestKeys.Birthday, this.form_yml.getBirthday().get(childkey));
    normal_map.put(Member_Info_TestKeys.Face_Photo, this.form_yml.getFace_photo().get(childkey));
    normal_map.put(Member_Info_TestKeys.Join_Date, this.form_yml.getJoin_date().get(childkey));
    normal_map.put(Member_Info_TestKeys.Ret_Date, this.form_yml.getRet_date().get(childkey));
    normal_map.put(Member_Info_TestKeys.Email, this.form_yml.getEmail().get(childkey));
    normal_map.put(Member_Info_TestKeys.Tel, this.form_yml.getTel().get(childkey));
    normal_map.put(Member_Info_TestKeys.Addr_Postcode, this.form_yml.getAddr_postcode().get(childkey));
    normal_map.put(Member_Info_TestKeys.Addr, this.form_yml.getAddr().get(childkey));
    normal_map.put(Member_Info_TestKeys.Position, this.form_yml.getPosition().get(childkey));
    normal_map.put(Member_Info_TestKeys.Position_Arri_Date, this.form_yml.getPosition_arri_date().get(childkey));
    normal_map.put(Member_Info_TestKeys.Job, this.form_yml.getJob().get(childkey));
    normal_map.put(Member_Info_TestKeys.Assign_Dept, this.form_yml.getAssign_dept().get(childkey));
    normal_map.put(Member_Info_TestKeys.Assign_Date, this.form_yml.getAssign_date().get(childkey));
    normal_map.put(Member_Info_TestKeys.Inst_Charge, this.form_yml.getInst_charge().get(childkey));
    normal_map.put(Member_Info_TestKeys.Other_Comment, this.form_yml.getOther_comment().get(childkey));

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
  public List<Map<Member_Info_TestKeys, String>> getOkData(Boolean new_tolerance){

    List<Map<Member_Info_TestKeys, String>> ok_list = new ArrayList<>();
    String childkey = "ok";
    String[] key_name = {"_male", "_female"};
    
    for(String sex: key_name){
      Map<Member_Info_TestKeys, String> ok_map = new HashMap<>();
  
      if(new_tolerance){
        ok_map.put(Member_Info_TestKeys.Serial_Num, null);
        ok_map.put(Member_Info_TestKeys.Member_Id, this.form_yml.getMember_id().get(childkey));
      }else{
        ok_map.put(Member_Info_TestKeys.Serial_Num, this.form_yml.getSerial_num().get(childkey));
        ok_map.put(Member_Info_TestKeys.Member_Id, this.form_yml.getMember_id().get("ng_unique"));
      }
      
      ok_map.put(Member_Info_TestKeys.Name, this.form_yml.getName().get(childkey));
      ok_map.put(Member_Info_TestKeys.Name_Pronu, this.form_yml.getName_pronu().get(childkey));
      ok_map.put(Member_Info_TestKeys.Sex, this.form_yml.getSex().get(childkey + sex));
      ok_map.put(Member_Info_TestKeys.Birthday, this.form_yml.getBirthday().get(childkey));
      ok_map.put(Member_Info_TestKeys.Face_Photo, this.form_yml.getFace_photo().get(childkey));
      ok_map.put(Member_Info_TestKeys.Join_Date, this.form_yml.getJoin_date().get(childkey));
      ok_map.put(Member_Info_TestKeys.Ret_Date, this.form_yml.getRet_date().get(childkey));
      ok_map.put(Member_Info_TestKeys.Email, this.form_yml.getEmail().get(childkey));
      ok_map.put(Member_Info_TestKeys.Tel, this.form_yml.getTel().get(childkey));
      ok_map.put(Member_Info_TestKeys.Addr_Postcode, this.form_yml.getAddr_postcode().get(childkey));
      ok_map.put(Member_Info_TestKeys.Addr, this.form_yml.getAddr().get(childkey));
      ok_map.put(Member_Info_TestKeys.Position, this.form_yml.getPosition().get(childkey));
      ok_map.put(Member_Info_TestKeys.Position_Arri_Date, this.form_yml.getPosition_arri_date().get(childkey));
      ok_map.put(Member_Info_TestKeys.Job, this.form_yml.getJob().get(childkey));
      ok_map.put(Member_Info_TestKeys.Assign_Dept, this.form_yml.getAssign_dept().get(childkey));
      ok_map.put(Member_Info_TestKeys.Assign_Date, this.form_yml.getAssign_date().get(childkey));
      ok_map.put(Member_Info_TestKeys.Inst_Charge, this.form_yml.getInst_charge().get(childkey));
      ok_map.put(Member_Info_TestKeys.Other_Comment, this.form_yml.getOther_comment().get(childkey));

      ok_list.add(ok_map);
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
   * @brief 項目[団員番号]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
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
  public List<String> getMemberIdFailedData(Boolean unique_tolerance){

    List<String> fail_list = new ArrayList<>();

    if(!unique_tolerance){
      fail_list.add(this.form_yml.getMember_id().get("ng_unique"));
    }

    fail_list.add(this.form_yml.getMember_id().get("ng_foreign"));
    fail_list.add(this.form_yml.getMember_id().get("ng_overflow"));
    fail_list.add(this.form_yml.getMember_id().get("ng_symbol"));
    fail_list.add(this.form_yml.getMember_id().get("ng_zenkaku"));
    fail_list.add(this.form_yml.getMember_id().get("ng_hankaku_kana"));
    fail_list.add(this.form_yml.getMember_id().get("ng_empty"));
    fail_list.add(this.form_yml.getMember_id().get("ng_blank"));
    fail_list.add(this.form_yml.getMember_id().get("ng_blank_zenkaku"));
    fail_list.add(this.form_yml.getMember_id().get("ng_blank_tab"));
    fail_list.add(this.form_yml.getMember_id().get("ng_blank_newline"));
    fail_list.add(this.form_yml.getMember_id().get("ng_null"));

    return fail_list;
  }









  /** 
   **********************************************************************************************
   * @brief 項目[名前]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getNameFailedData(){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getName().get("ng_overflow"));
    fail_list.add(this.form_yml.getName().get("ng_symbol"));
    fail_list.add(this.form_yml.getName().get("ng_alpha"));
    fail_list.add(this.form_yml.getName().get("ng_digit"));
    fail_list.add(this.form_yml.getName().get("ng_hankaku_kana"));
    fail_list.add(this.form_yml.getName().get("ng_empty"));
    fail_list.add(this.form_yml.getName().get("ng_blank"));
    fail_list.add(this.form_yml.getName().get("ng_blank_tab"));
    fail_list.add(this.form_yml.getName().get("ng_blank_newline"));
    fail_list.add(this.form_yml.getName().get("ng_null"));

    return fail_list;
  }









  /** 
   **********************************************************************************************
   * @brief 項目[ふりがな]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getNamePronuFailedData(){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getName_pronu().get("ng_overflow"));
    fail_list.add(this.form_yml.getName_pronu().get("ng_symbol"));
    fail_list.add(this.form_yml.getName_pronu().get("ng_alpha"));
    fail_list.add(this.form_yml.getName_pronu().get("ng_digit"));
    fail_list.add(this.form_yml.getName_pronu().get("ng_hankaku_kana"));
    fail_list.add(this.form_yml.getName_pronu().get("ng_empty"));
    fail_list.add(this.form_yml.getName_pronu().get("ng_blank"));
    fail_list.add(this.form_yml.getName_pronu().get("ng_blank_tab"));
    fail_list.add(this.form_yml.getName_pronu().get("ng_blank_newline"));
    fail_list.add(this.form_yml.getName_pronu().get("ng_null"));

    return fail_list;
  }







  


  /** 
   **********************************************************************************************
   * @brief 項目[性別]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getSexFailedData(){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getSex().get("ng"));
    fail_list.add(this.form_yml.getSex().get("ng_empty"));
    fail_list.add(this.form_yml.getSex().get("ng_blank"));
    fail_list.add(this.form_yml.getSex().get("ng_blank_tab"));
    fail_list.add(this.form_yml.getSex().get("ng_blank_newline"));
    fail_list.add(this.form_yml.getSex().get("ng_null"));

    return fail_list;
  }







  /** 
   **********************************************************************************************
   * @brief 項目[誕生日]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
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
  public List<String> getBirthdayFailedData(Boolean output_valid){

    List<String> fail_list = new ArrayList<>();

    if(!output_valid){
      fail_list.add(this.form_yml.getBirthday().get("ng_no_date"));
      fail_list.add(this.form_yml.getBirthday().get("ng_no_format"));
    }

    fail_list.add(this.form_yml.getBirthday().get("ng_null"));

    return fail_list;
  }








  /** 
   **********************************************************************************************
   * @brief 項目[顔写真]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
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
  public List<String> getFacePhotoFailedData(Boolean output_valid){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getFace_photo().get("ng_no_png_jpg"));
    fail_list.add(this.form_yml.getFace_photo().get("ng_no_png_gif"));
    fail_list.add(this.form_yml.getFace_photo().get("ng_overflow"));
    fail_list.add(this.form_yml.getFace_photo().get("ng_pdf"));
    fail_list.add(this.form_yml.getFace_photo().get("ng_audio"));
    fail_list.add(this.form_yml.getFace_photo().get("ng_csv"));
    fail_list.add(this.form_yml.getFace_photo().get("ng_zip"));

    if(!output_valid){
      fail_list.add(this.form_yml.getFace_photo().get("ng_filename_overflow"));
      fail_list.add(this.form_yml.getFace_photo().get("ng_html_escape"));
      fail_list.add(this.form_yml.getFace_photo().get("ng_sql_escape"));
      fail_list.add(this.form_yml.getFace_photo().get("ng_filepath_escape"));
      fail_list.add(this.form_yml.getFace_photo().get("ng_forgery_ext"));
      fail_list.add(this.form_yml.getFace_photo().get("ng_forgery_data"));
    }

    return fail_list;
  }








  /** 
   **********************************************************************************************
   * @brief 項目[入団日]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
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
  public List<String> getJoinDateFailedData(Boolean output_valid){

    List<String> fail_list = new ArrayList<>();

    if(!output_valid){
      fail_list.add(this.form_yml.getJoin_date().get("ng_no_date"));
      fail_list.add(this.form_yml.getJoin_date().get("ng_no_format"));
    }

    fail_list.add(this.form_yml.getJoin_date().get("ng_null"));
    fail_list.add(this.form_yml.getJoin_date().get("ng_before_birthday"));

    return fail_list;
  }








  /** 
   **********************************************************************************************
   * @brief 項目[退団日]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
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
  public List<String> getRetDateFailedData(Boolean output_valid){

    List<String> fail_list = new ArrayList<>();

    if(!output_valid){
      fail_list.add(this.form_yml.getRet_date().get("ng_no_date"));
      fail_list.add(this.form_yml.getRet_date().get("ng_no_format"));
    }


    fail_list.add(this.form_yml.getRet_date().get("ng_before_joindate"));

    return fail_list;
  }








  /** 
   **********************************************************************************************
   * @brief 項目[メールアドレス]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getEmailFailedData(){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getEmail().get("ng_overflow"));
    fail_list.add(this.form_yml.getEmail().get("ng_zenkaku"));
    fail_list.add(this.form_yml.getEmail().get("ng_hankaku_kana"));

    return fail_list;
  }










  /** 
   **********************************************************************************************
   * @brief 項目[電話番号]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getTelFailedData(){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getTel().get("ng_overflow"));
    fail_list.add(this.form_yml.getTel().get("ng_zenkaku"));
    fail_list.add(this.form_yml.getTel().get("ng_alpha"));
    fail_list.add(this.form_yml.getTel().get("ng_hankaku_kana"));

    return fail_list;
  }










  /** 
   **********************************************************************************************
   * @brief 項目[現住所郵便番号]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getAddrPostcodeFailedData(){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getAddr_postcode().get("ng_overflow"));
    fail_list.add(this.form_yml.getAddr_postcode().get("ng_zenkaku"));
    fail_list.add(this.form_yml.getAddr_postcode().get("ng_alpha"));
    fail_list.add(this.form_yml.getAddr_postcode().get("ng_hankaku_kana"));

    return fail_list;
  }












  /** 
   **********************************************************************************************
   * @brief 項目[現住所]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getAddrFailedData(){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getAddr().get("ng_overflow"));
    fail_list.add(this.form_yml.getAddr().get("ng_symbol"));
    fail_list.add(this.form_yml.getAddr().get("ng_alpha"));
    fail_list.add(this.form_yml.getAddr().get("ng_digit"));
    fail_list.add(this.form_yml.getAddr().get("ng_hankaku_kana"));

    return fail_list;
  }










  /** 
   **********************************************************************************************
   * @brief 項目[役職名]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getPositionFailedData(){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getPosition().get("ng_overflow"));
    fail_list.add(this.form_yml.getPosition().get("ng_symbol"));
    fail_list.add(this.form_yml.getPosition().get("ng_alpha"));
    fail_list.add(this.form_yml.getPosition().get("ng_digit"));
    fail_list.add(this.form_yml.getPosition().get("ng_hankaku_kana"));

    return fail_list;
  }







  /** 
   **********************************************************************************************
   * @brief 項目[現役職着任日]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
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
  public List<String> getPositionArriDateFailedData(Boolean output_valid){

    List<String> fail_list = new ArrayList<>();

    if(!output_valid){
      fail_list.add(this.form_yml.getPosition_arri_date().get("ng_no_date"));
      fail_list.add(this.form_yml.getPosition_arri_date().get("ng_no_format"));
    }

    fail_list.add(this.form_yml.getPosition_arri_date().get("ng_before_joindate"));

    return fail_list;
  }









  /** 
   **********************************************************************************************
   * @brief 項目[職種名]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getJobFailedData(){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getJob().get("ng_overflow"));
    fail_list.add(this.form_yml.getJob().get("ng_symbol"));
    fail_list.add(this.form_yml.getJob().get("ng_alpha"));
    fail_list.add(this.form_yml.getJob().get("ng_digit"));
    fail_list.add(this.form_yml.getJob().get("ng_hankaku_kana"));

    return fail_list;
  }








  /** 
   **********************************************************************************************
   * @brief 項目[配属部署]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getAssignDeptFailedData(){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getAssign_dept().get("ng_overflow"));
    fail_list.add(this.form_yml.getAssign_dept().get("ng_symbol"));
    fail_list.add(this.form_yml.getAssign_dept().get("ng_alpha"));
    fail_list.add(this.form_yml.getAssign_dept().get("ng_digit"));
    fail_list.add(this.form_yml.getAssign_dept().get("ng_hankaku_kana"));

    return fail_list;
  }









  /** 
   **********************************************************************************************
   * @brief 項目[配属日]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
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
  public List<String> getAssignDateFailedData(Boolean output_valid){

    List<String> fail_list = new ArrayList<>();

    if(!output_valid){
      fail_list.add(this.form_yml.getAssign_date().get("ng_no_date"));
      fail_list.add(this.form_yml.getAssign_date().get("ng_no_format"));
    }

    fail_list.add(this.form_yml.getAssign_date().get("ng_before_joindate"));

    return fail_list;
  }









  /** 
   **********************************************************************************************
   * @brief 項目[担当楽器]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getInstChargeFailedData(){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getInst_charge().get("ng_overflow"));
    fail_list.add(this.form_yml.getInst_charge().get("ng_symbol"));
    fail_list.add(this.form_yml.getInst_charge().get("ng_alpha"));
    fail_list.add(this.form_yml.getInst_charge().get("ng_digit"));
    fail_list.add(this.form_yml.getInst_charge().get("ng_hankaku_kana"));

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
