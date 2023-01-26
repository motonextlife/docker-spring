/** 
 **************************************************************************************
 * @file History_Form_TestCase.java
 * @brief 主に[共通の履歴機能]機能のテストにおいて、バリデーションエラーになるテストケースを
 * 読み取るクラスを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.History.Form;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import com.springproject.dockerspring.CommonTestCaseMaker.History.History_TestCase_Make.History_TestKeys;










/** 
 **************************************************************************************
 * @brief 主に[共通の履歴機能]機能のテストにおいて、バリデーションエラーになるテストケースを
 * 読み取るクラス。
 * 
 * @details 
 * - テストケースが書かれたYMLファイルを読み込んで、専用のエンティティにマッピングする。
 * - マッピングしたデータは、外部から取得して、他のテストケースクラスで使用可能。
 * 
 * @par 使用アノテーション
 * - @Component
 * 
 * @see Common_Hist_Form_Yml
 **************************************************************************************
 */ 
@Component
public class History_Form_TestCase{
  
  private final Common_Hist_Form_Yml form_yml;








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
   * @see Common_Hist_Form_Yml
   **********************************************************************************************
   */ 
  public History_Form_TestCase() throws IOException{

    try(InputStream in_form_yml = new ClassPathResource("TestCaseFile/History/common-hist-form.yml").getInputStream()){
      Yaml yaml = new Yaml();
      this.form_yml = yaml.loadAs(in_form_yml, Common_Hist_Form_Yml.class);
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
   * @return 特に問題ないテストケースが格納されたリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータは、専用の列挙型のキーと紐つけて、マップリストに格納する。
   * -# 格納が終わったマップリストを、戻り値とする。
   **********************************************************************************************
   */ 
  public Map<History_TestKeys, String> getHistoryNormalData(){

    Map<History_TestKeys, String> normal_map = new HashMap<>();
    String childkey = "normal";

    normal_map.put(History_TestKeys.History_Id, this.form_yml.getHistory_id().get(childkey));
    normal_map.put(History_TestKeys.Change_Datetime, this.form_yml.getChange_datetime().get(childkey));
    normal_map.put(History_TestKeys.Change_Kinds, this.form_yml.getChange_kinds().get(childkey));
    normal_map.put(History_TestKeys.Operation_User, this.form_yml.getOperation_user().get(childkey));

    return normal_map;
  }









  /** 
   **********************************************************************************************
   * @brief バリデーションで合格するテストケースを読み込んで、マップリストを作成する。
   * 
   * @details
   * - 合格するケースが複数ある項目もあるため、全てのパターンを満たせるように作成する。
   * 
   * @return バリデーションに合格するマップが順番に格納されたリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータは、専用の列挙型のキーと紐つけて、マップリストに格納する。
   * -# 格納が終わったマップリストを、戻り値とする。
   **********************************************************************************************
   */ 
  public List<Map<History_TestKeys, String>> getHistoryOkData(){

    List<Map<History_TestKeys, String>> ok_list = new ArrayList<>();
    String childkey = "ok";
    
    String[] kinds = new String[]{"_insert", "_update", "_delete", "_rollback"};
    String[] users = new String[]{"_min", "_max"};

    for(String kind: kinds){
      Map<History_TestKeys, String> ok_map = new HashMap<>();
      ok_map.put(History_TestKeys.History_Id, this.form_yml.getHistory_id().get(childkey));
      ok_map.put(History_TestKeys.Change_Datetime, this.form_yml.getChange_datetime().get(childkey));
      ok_map.put(History_TestKeys.Change_Kinds, this.form_yml.getChange_kinds().get(childkey + kind));
      ok_map.put(History_TestKeys.Operation_User, this.form_yml.getOperation_user().get(childkey + users[0]));

      ok_list.add(ok_map);
    }

    for(String user: users){
      Map<History_TestKeys, String> ok_map = new HashMap<>();
      ok_map.put(History_TestKeys.History_Id, this.form_yml.getHistory_id().get(childkey));
      ok_map.put(History_TestKeys.Change_Datetime, this.form_yml.getChange_datetime().get(childkey));
      ok_map.put(History_TestKeys.Change_Kinds, this.form_yml.getChange_kinds().get(childkey + kinds[0]));
      ok_map.put(History_TestKeys.Operation_User, this.form_yml.getOperation_user().get(childkey + user));

      ok_list.add(ok_map);
    }

    return ok_list;
  }










  /** 
   **********************************************************************************************
   * @brief 項目[履歴番号]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
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
  public List<String> getHistoryIdFailedData(Boolean output_valid){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getHistory_id().get("ng_overflow"));
    fail_list.add(this.form_yml.getHistory_id().get("ng_null"));
    fail_list.add(this.form_yml.getHistory_id().get("ng_negative"));

    if(!output_valid){
      fail_list.add(this.form_yml.getHistory_id().get("ng_double"));
      fail_list.add(this.form_yml.getHistory_id().get("ng_no_digit"));
    }

    return fail_list;
  }








  /** 
   **********************************************************************************************
   * @brief 項目[履歴日時]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getChangeDatetimeFailedData(){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getChange_datetime().get("ng_null"));

    return fail_list;
  }









  /** 
   **********************************************************************************************
   * @brief 項目[履歴種別]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getChangeKindsFailedData(){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getChange_kinds().get("ng_empty"));
    fail_list.add(this.form_yml.getChange_kinds().get("ng_blank"));
    fail_list.add(this.form_yml.getChange_kinds().get("ng_blank_zenkaku"));
    fail_list.add(this.form_yml.getChange_kinds().get("ng_blank_tab"));
    fail_list.add(this.form_yml.getChange_kinds().get("ng_blank_newline"));
    fail_list.add(this.form_yml.getChange_kinds().get("ng_null"));

    return fail_list;
  }









  /** 
   **********************************************************************************************
   * @brief 項目[操作ユーザー名]でバリデーションエラーになるテストケースを読み取って、リストを作成する。
   * 
   * @return バリデーションでエラーになるテストケースのリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、データを取り出す。
   * -# 取り出したデータをリストに格納して、そのリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getOperationUserFailedData(){

    List<String> fail_list = new ArrayList<>();

    fail_list.add(this.form_yml.getOperation_user().get("ng_less_than"));
    fail_list.add(this.form_yml.getOperation_user().get("ng_overflow"));
    fail_list.add(this.form_yml.getOperation_user().get("ng_symbol"));
    fail_list.add(this.form_yml.getOperation_user().get("ng_zenkaku"));
    fail_list.add(this.form_yml.getOperation_user().get("ng_hankaku_kana"));
    fail_list.add(this.form_yml.getOperation_user().get("ng_empty"));
    fail_list.add(this.form_yml.getOperation_user().get("ng_blank"));
    fail_list.add(this.form_yml.getOperation_user().get("ng_blank_zenkaku"));
    fail_list.add(this.form_yml.getOperation_user().get("ng_blank_tab"));
    fail_list.add(this.form_yml.getOperation_user().get("ng_blank_newline"));
    fail_list.add(this.form_yml.getOperation_user().get("ng_null"));

    return fail_list;
  }
}
