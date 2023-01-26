/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.History.FailDatabase
 * 
 * @brief [共通の履歴管理機能]に関するテストケース生成処理のうち、[出力失敗ケースのデータベース]
 * 関連のテストケースを生成する処理を格納したパッケージ
 * 
 * @details
 * - このパッケージには、テストケースを記述したYAMLファイルとマッピングするエンティティや、
 * 取り込んだテストケースをデータベースにセットアップしたりする機能を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.History.FailDatabase;





/** 
 **************************************************************************************
 * @file History_Fail_Database_TestCase.java
 * @brief 主に[共通の履歴機能]機能のテストにおいて、データベースに格納するフォームバリデーション
 * でエラーになるテストケースを読み取るクラスを格納したファイル。
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

import com.springproject.dockerspring.CommonTestCaseMaker.History.Form.Common_Hist_Form_Yml;
import com.springproject.dockerspring.CommonTestCaseMaker.History.History_TestCase_Make.History_TestKeys;








/** 
 **************************************************************************************
 * @brief 主に[共通の履歴機能]機能のテストにおいて、データベースに格納するフォームバリデーション
 * でエラーになるテストケースを読み取るクラス。
 * 
 * @details 
 * - テストケースが書かれたYMLファイルを読み込んで、専用のエンティティにマッピングする。
 * - マッピングしたデータは、外部から取得して、他のテストケースクラスで使用可能。
 * - 現段階ではデータベース等へのデータの格納は行わない。実際の格納は、このクラスからデータを
 * 取得した他のテストケースクラスが行う。
 * 
 * @par 使用アノテーション
 * - @Component
 * 
 * @see Common_Hist_Form_Yml
 **************************************************************************************
 */ 
@Component
public class History_Fail_Database_TestCase{
  
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
  public History_Fail_Database_TestCase() throws IOException{

    try(InputStream in_form_yml = new ClassPathResource("TestCaseFile/History/common-hist-form.yml").getInputStream()){
      Yaml yaml = new Yaml();
      this.form_yml = yaml.loadAs(in_form_yml, Common_Hist_Form_Yml.class);
    }
  }








  /** 
   **********************************************************************************************
   * @brief 履歴用のテーブルで出力時にエラーとなるデータのリストを返す。
   * 
   * @details
   * - テストケースに登録されている全てのエラーケースをリスト化したものを作成する。
   * - ただし、データベースに格納できないようなエラーケース（例、列挙型や日付型）は対象外。
   * これは、データベースに格納する前に制約に違反し保存できない為、テーブル内にテストケースを用意できないから。
   * 
   * @return エラーケースのマップが順番に格納されたリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、エラーテストケースのみを取り出す。
   * -# 取り出したデータは、順番にリストに格納する。
   * -# 各項目の格納が終わったリストを全て結合し、一つのリストにしたものを戻り値とする。
   **********************************************************************************************
   */ 
  public List<Map<History_TestKeys, String>> getHistoryDatabaseFailData(){
    return makeOperationUserFailedData();
  }









  /** 
   **********************************************************************************************
   * @brief エラーケースのデータを作成するうえで基礎となる、合格ケースのリストを用意する。
   * 
   * @details
   * - ここで作成するリストを、項目一つづつ上書きすることで、一つの項目のみがエラーであるマップリストを
   * 作成することが可能である。
   * - 一つの項目のみがエラーとなるようにしているので、テストエラー時の原因を把握しやすくなる。
   * - 合格ケースが複数ある項目があるため、その項目が全てテストケースを満たすように作成する。
   * - ただし、データベースに格納できないようなエラーケース（例、列挙型や日付型）は対象外。
   * これは、データベースに格納する前に制約に違反し保存できない為、テーブル内にテストケースを用意できないから。
   * 
   * @return 正常データのマップが順番に格納されたリスト
   * 
   * @par 処理の大まかな流れ
   * -# あらかじめ定義した配列内の文字を連結して、データを取得する際のキー名を組む。
   * -# マッピングしたエンティティから、エラーテストケースのみを取り出す。
   * -# 取り出したデータは、順番にリストに格納する。
   * -# 格納が終わったリストを戻り値とする。
   **********************************************************************************************
   */ 
  private List<Map<History_TestKeys, String>> makeBaseData(){

    List<Map<History_TestKeys, String>> base_list = new ArrayList<>();
    String childkey = "ok";
    
    String[] kinds = new String[]{"_insert", "_update", "_delete", "_rollback"};
    String[] users = new String[]{"_min", "_max"};

    for(String kind: kinds){
      Map<History_TestKeys, String> ok_case_map = new HashMap<>();
      ok_case_map.put(History_TestKeys.History_Id, this.form_yml.getHistory_id().get(childkey));
      ok_case_map.put(History_TestKeys.Change_Datetime, this.form_yml.getChange_datetime().get(childkey));
      ok_case_map.put(History_TestKeys.Change_Kinds, this.form_yml.getChange_kinds().get(childkey + kind));
      ok_case_map.put(History_TestKeys.Operation_User, this.form_yml.getOperation_user().get(childkey + users[0]));

      base_list.add(ok_case_map);
    }

    for(String user: users){
      Map<History_TestKeys, String> ok_case_map = new HashMap<>();
      ok_case_map.put(History_TestKeys.History_Id, this.form_yml.getHistory_id().get(childkey));
      ok_case_map.put(History_TestKeys.Change_Datetime, this.form_yml.getChange_datetime().get(childkey));
      ok_case_map.put(History_TestKeys.Change_Kinds, this.form_yml.getChange_kinds().get(childkey + kinds[0]));
      ok_case_map.put(History_TestKeys.Operation_User, this.form_yml.getOperation_user().get(childkey + user));

      base_list.add(ok_case_map);
    }

    return base_list;
  }









  /** 
   **********************************************************************************************
   * @brief 項目[操作ユーザー名]でエラーになるテストケースを格納したマップリストを作成する。
   * 
   * @details
   * - マッピングしたエンティティから該当項目のエラーケースを取り出し、ベースとなる合格ケースのリストを
   * 上書きすることで作成する。
   * - 一つの項目のみがエラーとなるようにしているので、テストエラー時の原因を把握しやすくなる。
   * - ただし、データベースに格納できないようなエラーケース（例、列挙型や日付型）は対象外。
   * これは、データベースに格納する前に制約に違反し保存できない為、テーブル内にテストケースを用意できないから。
   * 
   * @return エラーケースのマップが順番に格納されたリスト
   * 
   * @par 処理の大まかな流れ
   * -# あらかじめ定義した配列内の文字を連結して、データを取得する際のキー名を組む。
   * -# マッピングしたエンティティから、エラーテストケースのみを取り出す。
   * -# あらかじめ作成した合格ケースのリストを取得し、そのリストの該当項目をエラーケースで上書きする。
   * -# 上書きしたデータは、順番に新規のリストに格納する。
   * -# 格納が終わったリストを戻り値とする。
   **********************************************************************************************
   */ 
  private List<Map<History_TestKeys, String>> makeOperationUserFailedData(){

    List<Map<History_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_less_than", 
                         "ng_overflow", 
                         "ng_symbol", 
                         "ng_zenkaku", 
                         "ng_hankaku_kana", 
                         "ng_empty", 
                         "ng_blank", 
                         "ng_blank_zenkaku", 
                         "ng_blank_tab", 
                         "ng_blank_newline"};

    for(String key: key_name){
      List<Map<History_TestKeys, String>> base_data = makeBaseData();

      for(Map<History_TestKeys, String> base_map: base_data){
        base_map.put(History_TestKeys.Operation_User, this.form_yml.getOperation_user().get(key));
        error_list.add(base_map);
      }
    }

    return error_list;
  }
}
