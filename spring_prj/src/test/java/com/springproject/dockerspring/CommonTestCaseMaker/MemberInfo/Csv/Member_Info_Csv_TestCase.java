/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Csv
 * 
 * @brief [団員管理]に関するテストケース生成処理のうち、[CSVファイルの入出力]
 * 関連のテストケースを生成する処理を格納したパッケージ
 * 
 * @details
 * - このパッケージには、テストケースを記述したYAMLファイルとマッピングするエンティティや、
 * 取り込んだテストケースを呼び出し元のテストクラスに提供する機能を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Csv;





/** 
 **************************************************************************************
 * @file Member_Info_Csv_TestCase.java
 * @brief 主に[団員管理]機能のテストにおいて、CSVファイルに関係するテストケースを読み込む
 * クラスを格納するファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;









/** 
 **************************************************************************************
 * @brief 主に[団員管理]機能のテストにおいて、CSVファイルに関係するテストケースを読み込む
 * クラス。
 * 
 * @details 
 * - テストケースが書かれたYMLファイルを読み込んで、専用のエンティティにマッピングする。
 * - マッピングしたデータは、外部から取得して、他のテストケースクラスで使用可能。
 * 
 * @par 使用アノテーション
 * - @Component
 * 
 * @see Member_Info_Csv_Yaml
 **************************************************************************************
 */ 
@Component
public class Member_Info_Csv_TestCase{

  private final Member_Info_Csv_Yaml csv_yml;









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
   * @see Member_Info_Csv_Yaml
   **********************************************************************************************
   */
  public Member_Info_Csv_TestCase() throws IOException{

    try(InputStream in_csv_yml = new ClassPathResource("TestCaseFile/MemberInfo/member-info-csv.yml").getInputStream()){
      Yaml yaml = new Yaml();
      this.csv_yml = yaml.loadAs(in_csv_yml, Member_Info_Csv_Yaml.class);
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
  public String getCsvNormalData(){
    return this.csv_yml.getCsv_file().get("normal");
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
  public String getCsvOkData(){
    return this.csv_yml.getCsv_file().get("ok");
  }







  /** 
   **********************************************************************************************
   * @brief 項目[設備CSVファイル]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @details
   * - フラグを指定する事で、フォーム入力時のバリデーションテストと、CSV機能単品のテストでの使い分けが可能。
   * - 二つのテストのエラーの項目には違いがあり、ZIP機能単品のテスト時にはエラーにならない項目を除外している。
   * 
   * @param[in] form_only フォームバリデーションテストの際は「True」、CSV機能単品のテストの際は「False」
   * を指定する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getCsvFailedData(Boolean form_only){

    List<String> fail_list = new ArrayList<>();
    
    fail_list.add(this.csv_yml.getCsv_file().get("ng_row_overflow"));

    if(!form_only){
      fail_list.add(this.csv_yml.getCsv_file().get("ng_header_typo"));
      fail_list.add(this.csv_yml.getCsv_file().get("ng_header_duplication"));
      fail_list.add(this.csv_yml.getCsv_file().get("ng_header_missing"));
      fail_list.add(this.csv_yml.getCsv_file().get("ng_header_order"));
      fail_list.add(this.csv_yml.getCsv_file().get("ng_overflow_error"));
    }
    
    return fail_list;
  }








  /** 
   **********************************************************************************************
   * @brief CSVファイルの中身がバリデーションエラーとなるテストケースの作成を行う。
   * 
   * @return バリデーションに合格するテストケースデータ
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出し戻り値とする。
   **********************************************************************************************
   */ 
  public String getCsvValidError(){
    return this.csv_yml.getCsv_file().get("ng_valid_error");
  }








  /** 
   **********************************************************************************************
   * @brief CSVのバリデーション機能でエラーになるケースにおいて、エラー位置の座標を示したリストを返す。
   * このリストでテストの成功可否を判別する。
   * 
   * @return 順番が保証された、エラーの座標を表したリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出し戻り値とする。
   **********************************************************************************************
   */ 
  public LinkedHashMap<Integer, List<Integer>> getCsvErrorPos(){
    return this.csv_yml.getNg_valid_error_pos();
  }








  /** 
   **********************************************************************************************
   * @brief CSVの出力の際の比較用のテストケースを出力する。
   * 
   * @param[in] tmp_file 「True」で比較用テンプレートファイルを出力し、「False」で中身が入った比較用の
   * ファイルを出力する。
   * 
   * @return 比較用ファイルのファイルパス文字列
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出し戻り値とする。
   **********************************************************************************************
   */ 
  public String getOutputCsv(Boolean tmp_file){

    if(tmp_file){
      return this.csv_yml.getOutput_csv().get("template");
    }else{
      return this.csv_yml.getOutput_csv().get("list");
    }
  }
}
