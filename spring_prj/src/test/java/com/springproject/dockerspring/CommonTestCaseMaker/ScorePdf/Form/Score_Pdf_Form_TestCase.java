/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.ScorePdf.Form
 * 
 * @brief [楽譜データ管理機能]に関するテストケース生成処理のうち、[フォームバリデーション]
 * 関連のテストケースを生成する処理を格納したパッケージ
 * 
 * @details
 * - このパッケージには、テストケースを記述したYAMLファイルとマッピングするエンティティや、
 * 取り込んだテストケースを呼び出し元のテストクラスに提供する機能を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.ScorePdf.Form;





/** 
 **************************************************************************************
 * @file Score_Pdf_Form_TestCase.java
 * @brief 主に[楽譜データ機能]機能のテストにおいて、バリデーションエラーになるテストケースを
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

import com.springproject.dockerspring.CommonTestCaseMaker.ScorePdf.Score_Pdf_TestCase_Make.Score_Pdf_TestKeys;








/** 
 **************************************************************************************
 * @brief 主に[楽譜データ機能]機能のテストにおいて、バリデーションエラーになるテストケースを
 * 読み取るクラス。
 * 
 * @details 
 * - テストケースが書かれたYMLファイルを読み込んで、専用のエンティティにマッピングする。
 * - マッピングしたデータは、外部から取得して、他のテストケースクラスで使用可能。
 * 
 * @par 使用アノテーション
 * - @Component
 * 
 * @see Score_Pdf_Form_Yaml
 **************************************************************************************
 */ 
@Component
public class Score_Pdf_Form_TestCase{
  
  private final Score_Pdf_Form_Yaml form_yml;









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
   * @see Score_Pdf_Form_Yaml
   **********************************************************************************************
   */ 
  public Score_Pdf_Form_TestCase() throws IOException{

    try(InputStream in_form_yml = new ClassPathResource("TestCaseFile/ScorePdf/score-pdf-form.yml").getInputStream()){
      Yaml yaml = new Yaml();
      this.form_yml = yaml.loadAs(in_form_yml, Score_Pdf_Form_Yaml.class);
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
  public Map<Score_Pdf_TestKeys, String> getNormalData(){

    Map<Score_Pdf_TestKeys, String> normal_map = new HashMap<>();
    String childkey = "normal";

    normal_map.put(Score_Pdf_TestKeys.Serial_Num, this.form_yml.getSerial_num().get(childkey));
    normal_map.put(Score_Pdf_TestKeys.Score_Id, this.form_yml.getScore_id().get(childkey));
    normal_map.put(Score_Pdf_TestKeys.Score_Name, this.form_yml.getScore_name().get(childkey));
    normal_map.put(Score_Pdf_TestKeys.Pdf_Data, this.form_yml.getPdf_data().get(childkey));

    return normal_map;
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
  public Map<Score_Pdf_TestKeys, String> getOkData(){

    Map<Score_Pdf_TestKeys, String> ok_map = new HashMap<>();
    String childkey = "ok";

    ok_map.put(Score_Pdf_TestKeys.Serial_Num, this.form_yml.getSerial_num().get(childkey));
    ok_map.put(Score_Pdf_TestKeys.Score_Id, this.form_yml.getScore_id().get(childkey));
    ok_map.put(Score_Pdf_TestKeys.Score_Name, this.form_yml.getScore_name().get(childkey));
    ok_map.put(Score_Pdf_TestKeys.Pdf_Data, this.form_yml.getPdf_data().get(childkey));

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
   * @brief 項目[楽譜番号]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
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
  public List<String> getScoreIdFailedData(Boolean output_valid){

    List<String> fail_list = new ArrayList<>();

    if(!output_valid){
      fail_list.add(this.form_yml.getScore_id().get("ng_foreign"));
    }
    
    fail_list.add(this.form_yml.getScore_id().get("ng_overflow"));
    fail_list.add(this.form_yml.getScore_id().get("ng_symbol"));
    fail_list.add(this.form_yml.getScore_id().get("ng_zenkaku"));
    fail_list.add(this.form_yml.getScore_id().get("ng_hankaku_kana"));
    fail_list.add(this.form_yml.getScore_id().get("ng_empty"));
    fail_list.add(this.form_yml.getScore_id().get("ng_blank"));
    fail_list.add(this.form_yml.getScore_id().get("ng_blank_zenkaku"));
    fail_list.add(this.form_yml.getScore_id().get("ng_blank_tab"));
    fail_list.add(this.form_yml.getScore_id().get("ng_blank_newline"));
    fail_list.add(this.form_yml.getScore_id().get("ng_null"));

    return fail_list;
  }









  /** 
   **********************************************************************************************
   * @brief 項目[楽譜データ名]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getScoreNameFailedData(){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getScore_name().get("ng_overflow"));
    fail_list.add(this.form_yml.getScore_name().get("ng_ext"));
    fail_list.add(this.form_yml.getScore_name().get("ng_html_escape"));
    fail_list.add(this.form_yml.getScore_name().get("ng_sql_escape"));
    fail_list.add(this.form_yml.getScore_name().get("ng_filepath_escape"));
    fail_list.add(this.form_yml.getScore_name().get("ng_empty"));
    fail_list.add(this.form_yml.getScore_name().get("ng_blank"));
    fail_list.add(this.form_yml.getScore_name().get("ng_blank_zenkaku"));
    fail_list.add(this.form_yml.getScore_name().get("ng_blank_tab"));
    fail_list.add(this.form_yml.getScore_name().get("ng_blank_newline"));
    fail_list.add(this.form_yml.getScore_name().get("ng_null"));

    return fail_list;
  }










  /** 
   **********************************************************************************************
   * @brief 項目[楽譜データ]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getPdfDataFailedData(){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getPdf_data().get("ng_no_pdf_mid"));
    fail_list.add(this.form_yml.getPdf_data().get("ng_no_pdf_svg"));
    fail_list.add(this.form_yml.getPdf_data().get("ng_overflow"));
    fail_list.add(this.form_yml.getPdf_data().get("ng_filename_overflow"));
    fail_list.add(this.form_yml.getPdf_data().get("ng_image"));
    fail_list.add(this.form_yml.getPdf_data().get("ng_audio"));
    fail_list.add(this.form_yml.getPdf_data().get("ng_csv"));
    fail_list.add(this.form_yml.getPdf_data().get("ng_zip"));
    fail_list.add(this.form_yml.getPdf_data().get("ng_html_escape"));
    fail_list.add(this.form_yml.getPdf_data().get("ng_sql_escape"));
    fail_list.add(this.form_yml.getPdf_data().get("ng_filepath_escape"));
    fail_list.add(this.form_yml.getPdf_data().get("ng_forgery_ext"));
    fail_list.add(this.form_yml.getPdf_data().get("ng_forgery_data"));

    return fail_list;
  }
}
