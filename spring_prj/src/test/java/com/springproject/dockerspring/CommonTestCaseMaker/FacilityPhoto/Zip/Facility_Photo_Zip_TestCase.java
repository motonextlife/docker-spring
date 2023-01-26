/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.FacilityPhoto.Zip
 * 
 * @brief [設備写真データ管理機能]に関するテストケース生成処理のうち、[ZIP圧縮ファイルの入出力]
 * 関連のテストケースを生成する処理を格納したパッケージ
 * 
 * @details
 * - このパッケージには、テストケースを記述したYAMLファイルとマッピングするエンティティや、
 * 取り込んだテストケースを呼び出し元のテストクラスに提供する機能を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.FacilityPhoto.Zip;





/** 
 **************************************************************************************
 * @file Facility_Photo_Zip_TestCase.java
 * @brief 主に[設備写真データ機能]機能のテストにおいて、ZIPの入出力の機能において使用するテスト
 * ケースを読み取るクラスを格納したファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;









/** 
 **************************************************************************************
 * @brief 主に[設備写真データ機能]機能のテストにおいて、ZIPの入出力の機能において使用するテスト
 * ケースを読み取るクラス。
 * 
 * @details 
 * - テストケースが書かれたYMLファイルを読み込んで、専用のエンティティにマッピングする。
 * - マッピングしたデータは、外部から取得して、他のテストケースクラスで使用可能。
 * 
 * @par 使用アノテーション
 * - @Component
 * 
 * @see Facility_Photo_Zip_Yaml
 **************************************************************************************
 */ 
@Component
public class Facility_Photo_Zip_TestCase{
  
  private final Facility_Photo_Zip_Yaml zip_yml;










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
   * @see Facility_Photo_Zip_Yaml
   **********************************************************************************************
   */ 
  public Facility_Photo_Zip_TestCase() throws IOException{

    try(InputStream in_zip_yml = new ClassPathResource("TestCaseFile/Facilityphoto/facility-photo-zip.yml").getInputStream()){
      Yaml yaml = new Yaml();
      this.zip_yml = yaml.loadAs(in_zip_yml, Facility_Photo_Zip_Yaml.class);
    }
  }











  /** 
   **********************************************************************************************
   * @brief バリデーションにおいて特に問題ないテストケースを読み込んで返却する。
   * 
   * @details
   * - このテストケースに関しては、バリデーション以外の機能でのテストに用いる。つまり、適当な値がほしい際に
   * 用いることが可能。
   * 
   * @return 特に問題ないテストケースデータ
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出し戻り値とする。
   **********************************************************************************************
   */ 
  public String getZipNormalData(){
    return this.zip_yml.getZip_file().get("normal");
  }








  /** 
   **********************************************************************************************
   * @brief バリデーションで合格するテストケースを読み込んで返却する。
   * 
   * @return バリデーションに合格するテストケースデータ
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出し戻り値とする。
   **********************************************************************************************
   */ 
  public String getZipOkData(){
    return this.zip_yml.getZip_file().get("ok");
  }







  /** 
   **********************************************************************************************
   * @brief 項目[設備写真ZIPファイル]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @details
   * - フラグを指定する事で、フォーム入力時のバリデーションテストと、ZIP機能単品のテストでの使い分けが可能。
   * - 二つのテストのエラーの項目には違いがあり、ZIP機能単品のテスト時にはエラーにならない項目を除外している。
   * 
   * @param[in] append_form フォームバリデーションテストの際は「True」、ZIP機能単品のテストの際は「False」
   * を指定する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getZipFailedData(Boolean append_form){

    List<String> fail_list = new ArrayList<>();
    
    fail_list.add(this.zip_yml.getZip_file().get("ng_count"));
    fail_list.add(this.zip_yml.getZip_file().get("ng_directory"));
    fail_list.add(this.zip_yml.getZip_file().get("ng_encrypt"));
    fail_list.add(this.zip_yml.getZip_file().get("ng_overflow"));

    if(append_form){
      fail_list.add(this.zip_yml.getZip_file().get("ng_no_png_jpg"));
      fail_list.add(this.zip_yml.getZip_file().get("ng_no_png_gif"));
      fail_list.add(this.zip_yml.getZip_file().get("ng_filename_overflow"));
      fail_list.add(this.zip_yml.getZip_file().get("ng_audio"));
      fail_list.add(this.zip_yml.getZip_file().get("ng_pdf"));
      fail_list.add(this.zip_yml.getZip_file().get("ng_csv"));
      fail_list.add(this.zip_yml.getZip_file().get("ng_zip"));
      fail_list.add(this.zip_yml.getZip_file().get("ng_html_escape"));
      fail_list.add(this.zip_yml.getZip_file().get("ng_sql_escape"));
      fail_list.add(this.zip_yml.getZip_file().get("ng_filepath_escape"));
      fail_list.add(this.zip_yml.getZip_file().get("ng_forgery_ext"));
      fail_list.add(this.zip_yml.getZip_file().get("ng_forgery_data"));
    }
    
    return fail_list;
  }
}
