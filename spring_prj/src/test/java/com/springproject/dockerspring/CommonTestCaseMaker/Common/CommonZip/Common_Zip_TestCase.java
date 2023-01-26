/** 
 **************************************************************************************
 * @file Common_Zip_TestCase.java
 * @brief 主に[共通のZIPファイル]機能のテストにおいて、バリデーションエラーになるテストケースを
 * 読み取るクラスを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.Common.CommonZip;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import com.springproject.dockerspring.CommonTestCaseMaker.Common.Common_TestCase_Make.Common_TestKeys;







/** 
 **************************************************************************************
 * @brief 主に[共通のZIPファイル]機能のテストにおいて、バリデーションエラーになるテストケースを
 * 読み取るクラス。
 * 
 * @details 
 * - テストケースが書かれたYMLファイルを読み込んで、専用のエンティティにマッピングする。
 * - マッピングしたデータは、外部から取得して、他のテストケースクラスで使用可能。
 * 
 * @par 使用アノテーション
 * - @Component
 * 
 * @see Common_Zip_Form_Yaml
 **************************************************************************************
 */ 
@Component
public class Common_Zip_TestCase{
  
  private final Common_Zip_Form_Yaml form_yml;








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
   * @see Common_Zip_Form_Yaml
   **********************************************************************************************
   */ 
  public Common_Zip_TestCase() throws IOException{

    try(InputStream in_zip_yml = new ClassPathResource("TestCaseFile/Common/common-zip-form.yml").getInputStream()){
      Yaml yaml = new Yaml();
      this.form_yml = yaml.loadAs(in_zip_yml, Common_Zip_Form_Yaml.class);
    }
  }









  /** 
   **********************************************************************************************
   * @brief バリデーションで合格するテストケースを読み込んで、マップリストを作成する。
   * 
   * @return バリデーションに合格するマップリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータは、専用の列挙型のキーと紐つけて、マップリストに格納する。
   * -# 格納が終わったマップリストを、戻り値とする。
   **********************************************************************************************
   */ 
  public Map<Common_TestKeys, String> getComZipOkData(){

    Map<Common_TestKeys, String> ok_map = new HashMap<>();

    ok_map.put(Common_TestKeys.Zip_File, this.form_yml.getZip_file().get("ok"));

    return ok_map;
  }









  /** 
   **********************************************************************************************
   * @brief 項目[ZIPファイル]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getComZipZipFileFailData(){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getZip_file().get("ng_no_wav_7z"));
    fail_list.add(this.form_yml.getZip_file().get("ng_no_wav_rar"));
    fail_list.add(this.form_yml.getZip_file().get("ng_overflow"));
    fail_list.add(this.form_yml.getZip_file().get("ng_filename_overflow"));
    fail_list.add(this.form_yml.getZip_file().get("ng_image"));
    fail_list.add(this.form_yml.getZip_file().get("ng_audio"));
    fail_list.add(this.form_yml.getZip_file().get("ng_pdf"));
    fail_list.add(this.form_yml.getZip_file().get("ng_csv"));
    fail_list.add(this.form_yml.getZip_file().get("ng_html_escape"));
    fail_list.add(this.form_yml.getZip_file().get("ng_sql_escape"));
    fail_list.add(this.form_yml.getZip_file().get("ng_filepath_escape"));
    fail_list.add(this.form_yml.getZip_file().get("ng_forgery_ext"));
    fail_list.add(this.form_yml.getZip_file().get("ng_forgery_data_zip"));

    return fail_list;
  }
}
