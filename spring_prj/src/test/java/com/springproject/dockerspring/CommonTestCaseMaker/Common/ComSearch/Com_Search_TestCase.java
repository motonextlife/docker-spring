/** 
 **************************************************************************************
 * @file Com_Search_TestCase.java
 * @brief 主に[音源データ機能]機能のテストにおいて、バリデーションエラーになるテストケースを
 * 読み取るクラスを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.Common.ComSearch;

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
 * @brief 主に[音源データ機能]機能のテストにおいて、バリデーションエラーになるテストケースを
 * 読み取るクラス。
 * 
 * @details 
 * - テストケースが書かれたYMLファイルを読み込んで、専用のエンティティにマッピングする。
 * - マッピングしたデータは、外部から取得して、他のテストケースクラスで使用可能。
 * 
 * @par 使用アノテーション
 * - @Component
 * 
 * @see Com_Search_Form_Yaml
 **************************************************************************************
 */ 
@Component
public class Com_Search_TestCase{
  
  private final Com_Search_Form_Yaml form_yml;








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
   * @see Com_Search_Form_Yaml
   **********************************************************************************************
   */ 
  public Com_Search_TestCase() throws IOException{

    try(InputStream in_search_yml = new ClassPathResource("TestCaseFile/Common/com-search-form.yml").getInputStream()){
      Yaml yaml = new Yaml();
      this.form_yml = yaml.loadAs(in_search_yml, Com_Search_Form_Yaml.class);
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
   * 
   * @note [検索種別]に関しては合格ケースはこのクラスで読み込んだファイル内には存在しない為、除外する。
   **********************************************************************************************
   */ 
  public Map<Common_TestKeys, String> getComSearchOkData(){

    String childkey = "ok";
    Map<Common_TestKeys, String> ok_map = new HashMap<>();

    ok_map.put(Common_TestKeys.Word, this.form_yml.getWord().get(childkey));

    return ok_map;
  }









  /** 
   **********************************************************************************************
   * @brief 項目[検索ワード]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getComSearchWordFailData(){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getWord().get("ng_overflow"));

    for(int i = 1; i <= 12; i++){
      fail_list.add(this.form_yml.getWord().get("ng_dangerous_" + i));
    }
    
    return fail_list;
  }






  


  /** 
   **********************************************************************************************
   * @brief 項目[検索種別]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getComSearchSubjectFailData(){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getSubject().get("ng_overflow"));
    fail_list.add(this.form_yml.getSubject().get("ng_symbol"));
    fail_list.add(this.form_yml.getSubject().get("ng_digit"));
    fail_list.add(this.form_yml.getSubject().get("ng_zenkaku"));
    fail_list.add(this.form_yml.getSubject().get("ng_hankaku_kana"));
    fail_list.add(this.form_yml.getSubject().get("ng_empty"));
    fail_list.add(this.form_yml.getSubject().get("ng_blank"));
    fail_list.add(this.form_yml.getSubject().get("ng_blank_zenkaku"));
    fail_list.add(this.form_yml.getSubject().get("ng_blank_tab"));
    fail_list.add(this.form_yml.getSubject().get("ng_blank_newline"));
    fail_list.add(this.form_yml.getSubject().get("ng_null"));

    return fail_list;
  }
}
