/** 
 **************************************************************************************
 * @file Common_Csv_TestCase.java
 * @brief 主に[共通のCSVファイル]機能のテストにおいて、バリデーションエラーになるテストケースを
 * 読み取るクラスを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.Common.CommonCsv;

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
 * @brief 主に[共通のCSVファイル]機能のテストにおいて、バリデーションエラーになるテストケースを
 * 読み取るクラス。
 * 
 * @details 
 * - テストケースが書かれたYMLファイルを読み込んで、専用のエンティティにマッピングする。
 * - マッピングしたデータは、外部から取得して、他のテストケースクラスで使用可能。
 * 
 * @par 使用アノテーション
 * - @Component
 * 
 * @see Common_Csv_Form_Yaml
 **************************************************************************************
 */ 
@Component
public class Common_Csv_TestCase{
  
  private final Common_Csv_Form_Yaml form_yml;








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
   * @see Common_Csv_Form_Yaml
   **********************************************************************************************
   */ 
  public Common_Csv_TestCase() throws IOException{

    try(InputStream in_csv_yml = new ClassPathResource("TestCaseFile/Common/common-csv-form.yml").getInputStream()){
      Yaml yaml = new Yaml();
      this.form_yml = yaml.loadAs(in_csv_yml, Common_Csv_Form_Yaml.class);
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
  public Map<Common_TestKeys, String> getComCsvOkData(){

    Map<Common_TestKeys, String> ok_map = new HashMap<>();

    ok_map.put(Common_TestKeys.Csv_File, this.form_yml.getCsv_file().get("ok"));

    return ok_map;
  }









  /** 
   **********************************************************************************************
   * @brief 項目[CSVファイル]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getComCsvCsvFileFailData(){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getCsv_file().get("ng_no_csv_txt"));
    fail_list.add(this.form_yml.getCsv_file().get("ng_no_csv_xlsx"));
    fail_list.add(this.form_yml.getCsv_file().get("ng_overflow"));
    fail_list.add(this.form_yml.getCsv_file().get("ng_filename_overflow"));
    fail_list.add(this.form_yml.getCsv_file().get("ng_image"));
    fail_list.add(this.form_yml.getCsv_file().get("ng_audio"));
    fail_list.add(this.form_yml.getCsv_file().get("ng_pdf"));
    fail_list.add(this.form_yml.getCsv_file().get("ng_zip"));
    fail_list.add(this.form_yml.getCsv_file().get("ng_html_escape"));
    fail_list.add(this.form_yml.getCsv_file().get("ng_sql_escape"));
    fail_list.add(this.form_yml.getCsv_file().get("ng_filepath_escape"));
    fail_list.add(this.form_yml.getCsv_file().get("ng_forgery_ext"));
    fail_list.add(this.form_yml.getCsv_file().get("ng_forgery_data"));

    return fail_list;
  }
}
