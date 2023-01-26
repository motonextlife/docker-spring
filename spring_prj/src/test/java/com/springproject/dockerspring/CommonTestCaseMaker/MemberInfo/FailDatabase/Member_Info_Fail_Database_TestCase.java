/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.FailDatabase
 * 
 * @brief [団員管理]に関するテストケース生成処理のうち、[出力失敗ケースのデータベース]
 * 関連のテストケースを生成する処理を格納したパッケージ
 * 
 * @details
 * - このパッケージには、テストケースを記述したYAMLファイルとマッピングするエンティティや、
 * 取り込んだテストケースをデータベースにセットアップしたりする機能を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.FailDatabase;





/** 
 **************************************************************************************
 * @file Member_Info_Fail_Database_TestCase.java
 * @brief 主に[団員管理]機能のテストにおいて、データベースとの通信テストの際に、あらかじめ
 * データベース内にエラーとなる初期値データを格納してセットアップするクラスを格納したファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import com.ninja_squad.dbsetup.operation.Insert;
import com.ninja_squad.dbsetup.operation.Operation;
import com.ninja_squad.dbsetup.operation.SqlOperation;
import com.ninja_squad.dbsetup.operation.Truncate;
import com.springproject.dockerspring.CommonTestCaseMaker.Configure.ConfigureFactory;
import com.springproject.dockerspring.CommonTestCaseMaker.History.History_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.History.History_TestCase_Make.History_TestKeys;
import com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Form.Member_Info_Form_Yaml;
import com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Member_Info_TestCase_Make.Member_Info_TestKeys;








/** 
 **************************************************************************************
 * @brief 主に[団員管理]機能のテストにおいて、データベースとの通信テストの際に、あらかじめ
 * データベース内にエラーとなる初期値データを格納してセットアップするクラス。
 * 
 * @details 
 * - このクラスでは、データベースの初期化のほか、初期化時に使用したデータをテスト時の比較用として
 * モックのエンティティに格納して返却が可能。
 * - 必要に応じてファイルサーバーと連携してデータベースとファイルサーバーのどちらも初期化すること
 * が可能である。
 * - なお、このクラスは通常データ用と履歴データ用で共用で用いる。
 * 
 * @par 使用アノテーション
 * - @Component
 * - @Import({ConfigureFactory.class, History_TestCase_Make.class})
 * 
 * @see Member_Info_Form_Yaml
 * @see ConfigureFactory
 * @see History_TestCase_Make
 **************************************************************************************
 */ 
@Component
@Import({ConfigureFactory.class, History_TestCase_Make.class})
public class Member_Info_Fail_Database_TestCase{
  
  private final Member_Info_Form_Yaml form_yml;
  private final History_TestCase_Make hist_testcase_make;
  private final ConfigureFactory config_factory;


  private final SqlOperation FOREIGN_KEY_CHECK_0 = SqlOperation.of("SET FOREIGN_KEY_CHECKS = 0");
  private final SqlOperation FOREIGN_KEY_CHECK_1 = SqlOperation.of("SET FOREIGN_KEY_CHECKS = 1");
  private final String NORMAL_TABLE_NAME = "Member_Info";
  private final String HISTORY_TABLE_NAME = "Member_Info_History";
  private final Truncate NORMAL_TABLE = Truncate.table(NORMAL_TABLE_NAME);
  private final Truncate HISTORY_TABLE = Truncate.table(HISTORY_TABLE_NAME);










  /** 
   **********************************************************************************************
   * @brief 使用するテストケースクラスのDIのほか、YMLファイルからデータを読み取って専用エンティティへの
   * マッピングを行う。
   * 
   * @details
   * - データベースやファイルサーバーのセットアップに必要なクラスを、インジェクションする。
   * - データベースに格納するテストケースが記述されたYMLファイルを読み込んで、専用のエンティティに
   * マッピングする。
   * 
   * @param[in] config_factory テスト設定ファイルの総合クラス
   * @param[in] hist_testcase_make 履歴情報のデータベース関連のエラーテストケースクラス
   * 
   * @par 処理の大まかな流れ
   * -# 引数で渡されたクラスを、コンストラクタインジェクションする。
   * -# 指定されたYMLファイルのテストケースを読み込む。
   * -# 読み込んだYMLファイルを専用のエンティティにマッピングする。
   * -# インジェクションしたクラスを、フィールド変数に格納する。
   * 
   * @see Member_Info_Form_Yaml
   * @see ConfigureFactory
   * @see History_TestCase_Make
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  @Autowired
  public Member_Info_Fail_Database_TestCase(ConfigureFactory config_factory, 
                                            History_TestCase_Make hist_testcase_make) throws IOException{

    try(InputStream in_form_yml = new ClassPathResource("TestCaseFile/MemberInfo/member-info-form.yml").getInputStream()){
      Yaml yaml = new Yaml();
      this.form_yml = yaml.loadAs(in_form_yml, Member_Info_Form_Yaml.class);
    }

    this.config_factory = config_factory;
    this.hist_testcase_make = hist_testcase_make;
  }











  /** 
   **********************************************************************************************
   * @brief リポジトリの検査時に用いるデータベースに、エラーテストケースのデータを保存する。
   * 
   * @details
   * - ただし、データベースに格納できないようなエラーケース（例、列挙型や日付型）は対象外。
   * - なおセットアップ後は、データ検索に必要なシリアルナンバーと管理番号を返す。
   * 
   * @return データ検索に必要なシリアルナンバーと管理番号のマップリスト
   * 
   * @par 処理の大まかな流れ
   * -# 保存前に保存対象のテーブルを初期化するクエリを組み立てる。
   * -# 組み上げたクエリを実行する。
   * -# クエリ組み上げの過程で保存しておいた、データ検索に必要なシリアルナンバーと管理番号を戻り値とする。
   * 
   * @throw IOException
   * @throw ParseException
   **********************************************************************************************
   */ 
  public Map<Integer, String> failDatabaseSetup() throws IOException, ParseException{

    SimpleDateFormat parse_date = new SimpleDateFormat("yyyy-MM-dd");
    List<Operation> operation_list = new ArrayList<>();

    operation_list.add(FOREIGN_KEY_CHECK_0);
    operation_list.add(NORMAL_TABLE);
    operation_list.add(FOREIGN_KEY_CHECK_1);

    List<Map<Member_Info_TestKeys, String>> fail_list = getDatabaseFail();
    Map<Integer, String> search_map = new HashMap<>();


    int i = 1;
    for(Map<Member_Info_TestKeys, String> fail_map: fail_list){
      String filename = fail_map.get(Member_Info_TestKeys.Face_Photo);
      byte[] photo_data = null;

      try(InputStream in = new ClassPathResource("TestCaseFile/MemberInfo/BlobTestCaseData/form/" + filename).getInputStream();
          BufferedInputStream buf = new BufferedInputStream(in);){
  
        photo_data = IOUtils.toByteArray(buf);
      }

      for(int j = 0; j < 2; j++){
        Operation operation = Insert.into(NORMAL_TABLE_NAME)
                                    .columns("serial_num",
                                            "member_id",
                                            "name",
                                            "name_pronu",
                                            "sex",
                                            "birthday",
                                            "face_photo",
                                            "join_date",
                                            "ret_date",
                                            "email_1",
                                            "email_2",
                                            "tel_1",
                                            "tel_2",
                                            "addr_postcode",
                                            "addr",
                                            "position",
                                            "position_arri_date",
                                            "job",
                                            "assign_dept",
                                            "assign_date",
                                            "inst_charge",
                                            "other_comment")
                                    .values(i,
                                            fail_map.get(Member_Info_TestKeys.Member_Id), 
                                            fail_map.get(Member_Info_TestKeys.Name), 
                                            fail_map.get(Member_Info_TestKeys.Name_Pronu), 
                                            fail_map.get(Member_Info_TestKeys.Sex), 
                                            parse_date.parse(fail_map.get(Member_Info_TestKeys.Birthday)), 
                                            photo_data, 
                                            parse_date.parse(fail_map.get(Member_Info_TestKeys.Join_Date)), 
                                            parse_date.parse(fail_map.get(Member_Info_TestKeys.Ret_Date)), 
                                            j % 2 == 0 ? makeBaseData().get(0).get(Member_Info_TestKeys.Email) : fail_map.get(Member_Info_TestKeys.Email), 
                                            j % 2 != 0 ? makeBaseData().get(0).get(Member_Info_TestKeys.Email) : fail_map.get(Member_Info_TestKeys.Email), 
                                            j % 2 == 0 ? makeBaseData().get(0).get(Member_Info_TestKeys.Tel) : fail_map.get(Member_Info_TestKeys.Tel), 
                                            j % 2 != 0 ? makeBaseData().get(0).get(Member_Info_TestKeys.Tel) : fail_map.get(Member_Info_TestKeys.Tel), 
                                            fail_map.get(Member_Info_TestKeys.Addr_Postcode), 
                                            fail_map.get(Member_Info_TestKeys.Addr), 
                                            fail_map.get(Member_Info_TestKeys.Position), 
                                            parse_date.parse(fail_map.get(Member_Info_TestKeys.Position_Arri_Date)), 
                                            fail_map.get(Member_Info_TestKeys.Job), 
                                            fail_map.get(Member_Info_TestKeys.Assign_Dept), 
                                            parse_date.parse(fail_map.get(Member_Info_TestKeys.Assign_Date)), 
                                            fail_map.get(Member_Info_TestKeys.Inst_Charge), 
                                            fail_map.get(Member_Info_TestKeys.Other_Comment))
                                    .build();

        operation_list.add(operation);
        search_map.put(i++, fail_map.get(Member_Info_TestKeys.Member_Id));
      }
    }

    this.config_factory.databaseExec(operation_list);

    return search_map;
  }










  /** 
   **********************************************************************************************
   * @brief リポジトリの検査時に使用したデータベース内のデータを全削除してリセットする。
   * 
   * @par 処理の大まかな流れ
   * -# 対象のデータベースのテーブルを初期化するクエリを組み立てる。
   * -# 組み上げたクエリを実行する。
   **********************************************************************************************
   */ 
  public void failDatabaseReset(){

    List<Operation> operation_list = new ArrayList<>();

    operation_list.add(FOREIGN_KEY_CHECK_0);
    operation_list.add(NORMAL_TABLE);
    operation_list.add(FOREIGN_KEY_CHECK_1);

    this.config_factory.databaseExec(operation_list);
  }










  /** 
   **********************************************************************************************
   * @brief 履歴用のリポジトリの検査時に用いるデータベースに、エラーテストケースのデータを保存する。
   * 
   * @details
   * - セットアップ後は、データ検索に必要な履歴番号を返す。
   * - ただし、データベースに格納できないようなエラーケース（例、列挙型や日付型）は対象外。
   * 
   * @return データ検索に必要な履歴番号をリストに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 保存前に保存対象のテーブルを初期化するクエリを組み立てる。
   * -# 組み上げたクエリを実行する。
   * -# クエリ組み上げの過程で保存しておいた、データ検索に必要な履歴番号のリストを戻り値とする。
   * 
   * @throw IOException
   * @throw ParseException
   **********************************************************************************************
   */ 
  public List<Integer> historyFailDatabaseSetup() throws IOException, ParseException{

    SimpleDateFormat parse_date = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat parse_datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    List<Operation> operation_list = new ArrayList<>();

    operation_list.add(FOREIGN_KEY_CHECK_0);
    operation_list.add(HISTORY_TABLE);
    operation_list.add(FOREIGN_KEY_CHECK_1);

    List<Map<String, String>> fail_list = getHistoryDatabaseFail();
    List<Integer> search_list = new ArrayList<>();


    int i = 1;
    for(Map<String, String> fail_map: fail_list){
      String filename = fail_map.get("face_photo");
      byte[] photo_data = null;

      try(InputStream in = new ClassPathResource("TestCaseFile/MemberInfo/BlobTestCaseData/form/" + filename).getInputStream();
          BufferedInputStream buf = new BufferedInputStream(in);){
  
        photo_data = IOUtils.toByteArray(buf);
      }
      
      for(int j = 0; j < 2; j++){
        Operation operation = Insert.into(HISTORY_TABLE_NAME)
                              .columns("history_id",
                                      "change_datetime",
                                      "change_kinds",
                                      "operation_user",
                                      "serial_num",
                                      "member_id",
                                      "name",
                                      "name_pronu",
                                      "sex",
                                      "birthday",
                                      "face_photo",
                                      "join_date",
                                      "ret_date",
                                      "email_1",
                                      "email_2",
                                      "tel_1",
                                      "tel_2",
                                      "addr_postcode",
                                      "addr",
                                      "position",
                                      "position_arri_date",
                                      "job",
                                      "assign_dept",
                                      "assign_date",
                                      "inst_charge",
                                      "other_comment")
                              .values(i,
                                      parse_datetime.parse(fail_map.get("change_datetime")), 
                                      fail_map.get("change_kinds"), 
                                      fail_map.get("operation_user"), 
                                      i, 
                                      fail_map.get("member_id"), 
                                      fail_map.get("name"), 
                                      fail_map.get("name_pronu"), 
                                      fail_map.get("sex"), 
                                      parse_date.parse(fail_map.get("birthday")), 
                                      photo_data, 
                                      parse_date.parse(fail_map.get("join_date")), 
                                      parse_date.parse(fail_map.get("ret_date")), 
                                      j % 2 == 0 ? makeBaseData().get(0).get(Member_Info_TestKeys.Email) : fail_map.get("email"), 
                                      j % 2 != 0 ? makeBaseData().get(0).get(Member_Info_TestKeys.Email) : fail_map.get("email"), 
                                      j % 2 == 0 ? makeBaseData().get(0).get(Member_Info_TestKeys.Tel) : fail_map.get("tel"), 
                                      j % 2 != 0 ? makeBaseData().get(0).get(Member_Info_TestKeys.Tel) : fail_map.get("tel"), 
                                      fail_map.get("addr_postcode"), 
                                      fail_map.get("addr"), 
                                      fail_map.get("position"), 
                                      parse_date.parse(fail_map.get("position_arri_date")), 
                                      fail_map.get("job"), 
                                      fail_map.get("assign_dept"), 
                                      parse_date.parse(fail_map.get("assign_date")), 
                                      fail_map.get("inst_charge"), 
                                      fail_map.get("other_comment"))
                              .build();

        operation_list.add(operation);
        search_list.add(i++);
      }
    }

    this.config_factory.databaseExec(operation_list);

    return search_list;
  }









  /** 
   **********************************************************************************************
   * @brief 履歴用のリポジトリの検査時に使用したデータベース内のデータを全削除してリセットする。
   * 
   * @par 処理の大まかな流れ
   * -# 対象のデータベースのテーブルを初期化するクエリを組み立てる。
   * -# 組み上げたクエリを実行する。
   **********************************************************************************************
   */ 
  public void historyFailDatabaseReset(){

    List<Operation> operation_list = new ArrayList<>();

    operation_list.add(FOREIGN_KEY_CHECK_0);
    operation_list.add(HISTORY_TABLE);
    operation_list.add(FOREIGN_KEY_CHECK_1);

    this.config_factory.databaseExec(operation_list);
  }










  /** 
   **********************************************************************************************
   * @brief 通常テーブルで用いるエラーテストケースを全て格納したリストを作成する。
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
   * -# 団員番号が一意制約に違反しないように、「団員番号がエラーのケース」以外のエラーケースに関しては、
   * 確実に一意のエラーが起こらない団員番号に上書きする。
   * -# 取り出したデータは、順番にリストに格納する。
   * -# 各項目の格納が終わったリストを全て結合し、一つのリストにしたものを戻り値とする。
   **********************************************************************************************
   */ 
  private List<Map<Member_Info_TestKeys, String>> getDatabaseFail(){

    List<Map<Member_Info_TestKeys, String>> error_list = new ArrayList<>();

    error_list.addAll(makeNameFailedData());
    error_list.addAll(makeNamePronuFailedData());
    error_list.addAll(makeFacePhotoFailedData());
    error_list.addAll(makeJoinDateFailedData());
    error_list.addAll(makeRetDateFailedData());
    error_list.addAll(makeEmailFailedData());
    error_list.addAll(makeTelFailedData());
    error_list.addAll(makeAddrPostcodeFailedData());
    error_list.addAll(makeAddrFailedData());
    error_list.addAll(makePositionFailedData());
    error_list.addAll(makePositionArriDateFailedData());
    error_list.addAll(makeJobFailedData());
    error_list.addAll(makeAssignDeptFailedData());
    error_list.addAll(makeAssignDateFailedData());
    error_list.addAll(makeInstChargeFailedData());
    error_list.addAll(makeOtherCommentFailedData());

    int i = 1;
    for(Map<Member_Info_TestKeys, String> id_override: error_list){
      String member_id = id_override.get(Member_Info_TestKeys.Member_Id);
      member_id = member_id.replaceFirst("....$", "") + "_" + i;

      id_override.put(Member_Info_TestKeys.Member_Id, member_id);
      i++;
    }

    error_list.addAll(makeMemberIdFailedData());

    return error_list;
  }









  /** 
   **********************************************************************************************
   * @brief 履歴テーブルで用いるエラーテストケースを全て格納したリストを作成する。
   * 
   * @details
   * - テストケースに登録されている全てのエラーケースをリスト化したものを作成する。
   * - ただし、データベースに格納できないようなエラーケース（例、列挙型や日付型）は対象外。
   * これは、データベースに格納する前に制約に違反し保存できない為、テーブル内にテストケースを用意できないから。
   * 
   * @return エラーケースのマップが順番に格納されたリスト
   * 
   * @note 
   * - 履歴内の履歴情報データがエラーになるケースでは、通常データはエラーにならないような適当な値を格納。
   * - 履歴内の通常データがエラーになるケースでは、履歴付随情報はエラーにならないような適当な値を格納。
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、エラーテストケースのみを取り出す。
   * -# 取り出したデータは、順番にリストに格納する。
   * -# 各項目の格納が終わったリストを全て結合し、一つのリストにしたものを戻り値とする。
   **********************************************************************************************
   */ 
  private List<Map<String, String>> getHistoryDatabaseFail(){
    List<Map<String, String>> fail_list = new ArrayList<>();

    List<Map<Member_Info_TestKeys, String>> normal_table = getDatabaseFail();
    List<Map<History_TestKeys, String>> hist_table = this.hist_testcase_make.getHistoryDatabaseFailData();


    for(Map<Member_Info_TestKeys, String> normal_map: normal_table){

      Map<String, String> strkey_map = new HashMap<>();
      strkey_map.put("change_datetime", "1950-01-01 12:30:30");
      strkey_map.put("change_kinds", "update");
      strkey_map.put("operation_user", "testuser");
      strkey_map.put("member_id", normal_map.get(Member_Info_TestKeys.Member_Id));
      strkey_map.put("name", normal_map.get(Member_Info_TestKeys.Name));
      strkey_map.put("name_pronu", normal_map.get(Member_Info_TestKeys.Name_Pronu));
      strkey_map.put("sex", normal_map.get(Member_Info_TestKeys.Sex));
      strkey_map.put("birthday", normal_map.get(Member_Info_TestKeys.Birthday));
      strkey_map.put("face_photo", normal_map.get(Member_Info_TestKeys.Face_Photo));
      strkey_map.put("join_date", normal_map.get(Member_Info_TestKeys.Join_Date));
      strkey_map.put("ret_date", normal_map.get(Member_Info_TestKeys.Ret_Date));
      strkey_map.put("email", normal_map.get(Member_Info_TestKeys.Email));
      strkey_map.put("tel", normal_map.get(Member_Info_TestKeys.Tel));
      strkey_map.put("addr_postcode", normal_map.get(Member_Info_TestKeys.Addr_Postcode));
      strkey_map.put("addr", normal_map.get(Member_Info_TestKeys.Addr));
      strkey_map.put("position", normal_map.get(Member_Info_TestKeys.Position));
      strkey_map.put("position_arri_date", normal_map.get(Member_Info_TestKeys.Position_Arri_Date));
      strkey_map.put("job", normal_map.get(Member_Info_TestKeys.Job));
      strkey_map.put("assign_dept", normal_map.get(Member_Info_TestKeys.Assign_Dept));
      strkey_map.put("assign_date", normal_map.get(Member_Info_TestKeys.Assign_Date));
      strkey_map.put("inst_charge", normal_map.get(Member_Info_TestKeys.Inst_Charge));
      strkey_map.put("other_comment", normal_map.get(Member_Info_TestKeys.Other_Comment));

      fail_list.add(strkey_map);
    }


    for(Map<History_TestKeys, String> hist_map: hist_table){

      Map<String, String> strkey_map = new HashMap<>();
      strkey_map.put("change_datetime", hist_map.get(History_TestKeys.Change_Datetime));
      strkey_map.put("change_kinds", hist_map.get(History_TestKeys.Change_Kinds));
      strkey_map.put("operation_user", hist_map.get(History_TestKeys.Operation_User));
      strkey_map.put("member_id", makeBaseData().get(0).get(Member_Info_TestKeys.Member_Id));
      strkey_map.put("name", makeBaseData().get(0).get(Member_Info_TestKeys.Name));
      strkey_map.put("name_pronu", makeBaseData().get(0).get(Member_Info_TestKeys.Name_Pronu));
      strkey_map.put("sex", makeBaseData().get(0).get(Member_Info_TestKeys.Sex));
      strkey_map.put("birthday", makeBaseData().get(0).get(Member_Info_TestKeys.Birthday));
      strkey_map.put("face_photo", makeBaseData().get(0).get(Member_Info_TestKeys.Face_Photo));
      strkey_map.put("join_date", makeBaseData().get(0).get(Member_Info_TestKeys.Join_Date));
      strkey_map.put("ret_date", makeBaseData().get(0).get(Member_Info_TestKeys.Ret_Date));
      strkey_map.put("email", makeBaseData().get(0).get(Member_Info_TestKeys.Email));
      strkey_map.put("tel", makeBaseData().get(0).get(Member_Info_TestKeys.Tel));
      strkey_map.put("addr_postcode", makeBaseData().get(0).get(Member_Info_TestKeys.Addr_Postcode));
      strkey_map.put("addr", makeBaseData().get(0).get(Member_Info_TestKeys.Addr));
      strkey_map.put("position", makeBaseData().get(0).get(Member_Info_TestKeys.Position));
      strkey_map.put("position_arri_date", makeBaseData().get(0).get(Member_Info_TestKeys.Position_Arri_Date));
      strkey_map.put("job", makeBaseData().get(0).get(Member_Info_TestKeys.Job));
      strkey_map.put("assign_dept", makeBaseData().get(0).get(Member_Info_TestKeys.Assign_Dept));
      strkey_map.put("assign_date", makeBaseData().get(0).get(Member_Info_TestKeys.Assign_Date));
      strkey_map.put("inst_charge", makeBaseData().get(0).get(Member_Info_TestKeys.Inst_Charge));
      strkey_map.put("other_comment", makeBaseData().get(0).get(Member_Info_TestKeys.Other_Comment));

      fail_list.add(strkey_map);
    }

    return fail_list;
  }









  /** 
   **********************************************************************************************
   * @brief エラーケースのデータを作成するうえで基礎となる、合格ケースのリストを用意する。
   * 
   * @details
   * - ここで作成するリストを、項目一つづつ上書きすることで、一つの項目のみがエラーであるマップリストを
   * 作成することが可能である。
   * - 一つの項目のみがエラーとなるようにしているので、テストエラー時の原因を把握しやすくなる。
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
  public List<Map<Member_Info_TestKeys, String>> makeBaseData(){

    List<Map<Member_Info_TestKeys, String>> base_list = new ArrayList<>();
    String childkey = "ok";
    String[] key_name = {"_male", "_female"};
    
    for(String sex: key_name){
      Map<Member_Info_TestKeys, String> base_map = new HashMap<>();

      base_map.put(Member_Info_TestKeys.Member_Id, this.form_yml.getMember_id().get(childkey));
      base_map.put(Member_Info_TestKeys.Name, this.form_yml.getName().get(childkey));
      base_map.put(Member_Info_TestKeys.Name_Pronu, this.form_yml.getName_pronu().get(childkey));
      base_map.put(Member_Info_TestKeys.Sex, this.form_yml.getSex().get(childkey + sex));
      base_map.put(Member_Info_TestKeys.Birthday, this.form_yml.getBirthday().get(childkey));
      base_map.put(Member_Info_TestKeys.Face_Photo, this.form_yml.getFace_photo().get(childkey));
      base_map.put(Member_Info_TestKeys.Join_Date, this.form_yml.getJoin_date().get(childkey));
      base_map.put(Member_Info_TestKeys.Ret_Date, this.form_yml.getRet_date().get(childkey));
      base_map.put(Member_Info_TestKeys.Email, this.form_yml.getEmail().get(childkey));
      base_map.put(Member_Info_TestKeys.Tel, this.form_yml.getTel().get(childkey));
      base_map.put(Member_Info_TestKeys.Addr_Postcode, this.form_yml.getAddr_postcode().get(childkey));
      base_map.put(Member_Info_TestKeys.Addr, this.form_yml.getAddr().get(childkey));
      base_map.put(Member_Info_TestKeys.Position, this.form_yml.getPosition().get(childkey));
      base_map.put(Member_Info_TestKeys.Position_Arri_Date, this.form_yml.getPosition_arri_date().get(childkey));
      base_map.put(Member_Info_TestKeys.Job, this.form_yml.getJob().get(childkey));
      base_map.put(Member_Info_TestKeys.Assign_Dept, this.form_yml.getAssign_dept().get(childkey));
      base_map.put(Member_Info_TestKeys.Assign_Date, this.form_yml.getAssign_date().get(childkey));
      base_map.put(Member_Info_TestKeys.Inst_Charge, this.form_yml.getInst_charge().get(childkey));
      base_map.put(Member_Info_TestKeys.Other_Comment, this.form_yml.getOther_comment().get(childkey));

      base_list.add(base_map);
    }

    return base_list;
  }











  /** 
   **********************************************************************************************
   * @brief 項目[団員番号]でエラーになるテストケースを格納したマップリストを作成する。
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
  private List<Map<Member_Info_TestKeys, String>> makeMemberIdFailedData(){

    List<Map<Member_Info_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_overflow", 
                         "ng_symbol", 
                         "ng_zenkaku", 
                         "ng_hankaku_kana", 
                         "ng_empty", 
                         "ng_blank", 
                         "ng_blank_tab", 
                         "ng_blank_newline"};

    for(String key: key_name){
      List<Map<Member_Info_TestKeys, String>> base_data = makeBaseData();

      for(Map<Member_Info_TestKeys, String> base_map: base_data){
        base_map.put(Member_Info_TestKeys.Member_Id, this.form_yml.getMember_id().get(key));
        error_list.add(base_map);
      }
    }

    return error_list;
  }









  /** 
   **********************************************************************************************
   * @brief 項目[名前]でエラーになるテストケースを格納したマップリストを作成する。
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
  private List<Map<Member_Info_TestKeys, String>> makeNameFailedData(){

    List<Map<Member_Info_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_overflow", 
                         "ng_symbol", 
                         "ng_alpha", 
                         "ng_digit", 
                         "ng_hankaku_kana", 
                         "ng_empty", 
                         "ng_blank", 
                         "ng_blank_zenkaku", 
                         "ng_blank_tab", 
                         "ng_blank_newline"};

    for(String key: key_name){
      List<Map<Member_Info_TestKeys, String>> base_data = makeBaseData();

      for(Map<Member_Info_TestKeys, String> base_map: base_data){
        base_map.put(Member_Info_TestKeys.Name, this.form_yml.getName().get(key));
        error_list.add(base_map);
      }
    }

    return error_list;
  }








  /** 
   **********************************************************************************************
   * @brief 項目[ふりがな]でエラーになるテストケースを格納したマップリストを作成する。
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
  private List<Map<Member_Info_TestKeys, String>> makeNamePronuFailedData(){

    List<Map<Member_Info_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_overflow", 
                         "ng_symbol", 
                         "ng_alpha", 
                         "ng_digit", 
                         "ng_hankaku_kana", 
                         "ng_empty", 
                         "ng_blank", 
                         "ng_blank_tab", 
                         "ng_blank_newline"};

    for(String key: key_name){
      List<Map<Member_Info_TestKeys, String>> base_data = makeBaseData();

      for(Map<Member_Info_TestKeys, String> base_map: base_data){
        base_map.put(Member_Info_TestKeys.Name_Pronu, this.form_yml.getName_pronu().get(key));
        error_list.add(base_map);
      }
    }

    return error_list;
  }









  /** 
   **********************************************************************************************
   * @brief 項目[顔写真]でエラーになるテストケースを格納したマップリストを作成する。
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
  private List<Map<Member_Info_TestKeys, String>> makeFacePhotoFailedData(){

    List<Map<Member_Info_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_no_png_jpg", 
                         "ng_no_png_gif", 
                         "ng_overflow", 
                         "ng_pdf", 
                         "ng_audio", 
                         "ng_csv", 
                         "ng_zip"};

    for(String key: key_name){
      List<Map<Member_Info_TestKeys, String>> base_data = makeBaseData();

      for(Map<Member_Info_TestKeys, String> base_map: base_data){
        base_map.put(Member_Info_TestKeys.Face_Photo, this.form_yml.getFace_photo().get(key));
        error_list.add(base_map);
      }
    }

    return error_list;
  }








  /** 
   **********************************************************************************************
   * @brief 項目[入団日]でエラーになるテストケースを格納したマップリストを作成する。
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
  private List<Map<Member_Info_TestKeys, String>> makeJoinDateFailedData(){

    List<Map<Member_Info_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_before_birthday"};

    for(String key: key_name){
      List<Map<Member_Info_TestKeys, String>> base_data = makeBaseData();

      for(Map<Member_Info_TestKeys, String> base_map: base_data){
        base_map.put(Member_Info_TestKeys.Join_Date, this.form_yml.getJoin_date().get(key));
        error_list.add(base_map);
      }
    }

    return error_list;
  }








  /** 
   **********************************************************************************************
   * @brief 項目[退団日]でエラーになるテストケースを格納したマップリストを作成する。
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
  private List<Map<Member_Info_TestKeys, String>> makeRetDateFailedData(){

    List<Map<Member_Info_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_before_birthday"};

    for(String key: key_name){
      List<Map<Member_Info_TestKeys, String>> base_data = makeBaseData();

      for(Map<Member_Info_TestKeys, String> base_map: base_data){
        base_map.put(Member_Info_TestKeys.Ret_Date, this.form_yml.getRet_date().get(key));
        error_list.add(base_map);
      }
    }

    return error_list;
  }








  /** 
   **********************************************************************************************
   * @brief 項目[メールアドレス]でエラーになるテストケースを格納したマップリストを作成する。
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
  private List<Map<Member_Info_TestKeys, String>> makeEmailFailedData(){

    List<Map<Member_Info_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_overflow", 
                         "ng_zenkaku", 
                         "ng_hankaku_kana"};

    for(String key: key_name){
      List<Map<Member_Info_TestKeys, String>> base_data = makeBaseData();

      for(Map<Member_Info_TestKeys, String> base_map: base_data){
        base_map.put(Member_Info_TestKeys.Email, this.form_yml.getEmail().get(key));
        error_list.add(base_map);
      }
    }

    return error_list;
  }
  






  /** 
   **********************************************************************************************
   * @brief 項目[電話番号]でエラーになるテストケースを格納したマップリストを作成する。
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
  private List<Map<Member_Info_TestKeys, String>> makeTelFailedData(){

    List<Map<Member_Info_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_overflow", 
                         "ng_zenkaku", 
                         "ng_alpha", 
                         "ng_hankaku_kana"};

    for(String key: key_name){
      List<Map<Member_Info_TestKeys, String>> base_data = makeBaseData();

      for(Map<Member_Info_TestKeys, String> base_map: base_data){
        base_map.put(Member_Info_TestKeys.Tel, this.form_yml.getTel().get(key));
        error_list.add(base_map);
      }
    }

    return error_list;
  }







  /** 
   **********************************************************************************************
   * @brief 項目[現住所郵便番号]でエラーになるテストケースを格納したマップリストを作成する。
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
  private List<Map<Member_Info_TestKeys, String>> makeAddrPostcodeFailedData(){

    List<Map<Member_Info_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_overflow", 
                         "ng_zenkaku", 
                         "ng_alpha", 
                         "ng_hankaku_kana"};

    for(String key: key_name){
      List<Map<Member_Info_TestKeys, String>> base_data = makeBaseData();

      for(Map<Member_Info_TestKeys, String> base_map: base_data){
        base_map.put(Member_Info_TestKeys.Addr_Postcode, this.form_yml.getAddr_postcode().get(key));
        error_list.add(base_map);
      }
    }

    return error_list;
  }









  /** 
   **********************************************************************************************
   * @brief 項目[現住所]でエラーになるテストケースを格納したマップリストを作成する。
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
  private List<Map<Member_Info_TestKeys, String>> makeAddrFailedData(){

    List<Map<Member_Info_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_overflow", 
                         "ng_symbol", 
                         "ng_alpha", 
                         "ng_digit", 
                         "ng_hankaku_kana"};

    for(String key: key_name){
      List<Map<Member_Info_TestKeys, String>> base_data = makeBaseData();

      for(Map<Member_Info_TestKeys, String> base_map: base_data){
        base_map.put(Member_Info_TestKeys.Addr, this.form_yml.getAddr().get(key));
        error_list.add(base_map);
      }
    }

    return error_list;
  }








  /** 
   **********************************************************************************************
   * @brief 項目[役職名]でエラーになるテストケースを格納したマップリストを作成する。
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
  private List<Map<Member_Info_TestKeys, String>> makePositionFailedData(){

    List<Map<Member_Info_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_overflow", 
                         "ng_symbol", 
                         "ng_alpha", 
                         "ng_digit", 
                         "ng_hankaku_kana"};

    for(String key: key_name){
      List<Map<Member_Info_TestKeys, String>> base_data = makeBaseData();

      for(Map<Member_Info_TestKeys, String> base_map: base_data){
        base_map.put(Member_Info_TestKeys.Position, this.form_yml.getPosition().get(key));
        error_list.add(base_map);
      }
    }

    return error_list;
  }




  



  /** 
   **********************************************************************************************
   * @brief 項目[現役職着任日]でエラーになるテストケースを格納したマップリストを作成する。
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
  private List<Map<Member_Info_TestKeys, String>> makePositionArriDateFailedData(){

    List<Map<Member_Info_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_before_joindate"};

    for(String key: key_name){
      List<Map<Member_Info_TestKeys, String>> base_data = makeBaseData();

      for(Map<Member_Info_TestKeys, String> base_map: base_data){
        base_map.put(Member_Info_TestKeys.Position_Arri_Date, this.form_yml.getPosition_arri_date().get(key));
        error_list.add(base_map);
      }
    }

    return error_list;
  }







  /** 
   **********************************************************************************************
   * @brief 項目[職種名]でエラーになるテストケースを格納したマップリストを作成する。
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
  private List<Map<Member_Info_TestKeys, String>> makeJobFailedData(){

    List<Map<Member_Info_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_overflow", 
                         "ng_symbol", 
                         "ng_alpha", 
                         "ng_digit", 
                         "ng_hankaku_kana"};

    for(String key: key_name){
      List<Map<Member_Info_TestKeys, String>> base_data = makeBaseData();

      for(Map<Member_Info_TestKeys, String> base_map: base_data){
        base_map.put(Member_Info_TestKeys.Job, this.form_yml.getJob().get(key));
        error_list.add(base_map);
      }
    }

    return error_list;
  }







  /** 
   **********************************************************************************************
   * @brief 項目[配属部署]でエラーになるテストケースを格納したマップリストを作成する。
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
  private List<Map<Member_Info_TestKeys, String>> makeAssignDeptFailedData(){

    List<Map<Member_Info_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_overflow", 
                         "ng_symbol", 
                         "ng_alpha", 
                         "ng_digit", 
                         "ng_hankaku_kana"};

    for(String key: key_name){
      List<Map<Member_Info_TestKeys, String>> base_data = makeBaseData();

      for(Map<Member_Info_TestKeys, String> base_map: base_data){
        base_map.put(Member_Info_TestKeys.Assign_Dept, this.form_yml.getAssign_dept().get(key));
        error_list.add(base_map);
      }
    }

    return error_list;
  }







  /** 
   **********************************************************************************************
   * @brief 項目[配属日]でエラーになるテストケースを格納したマップリストを作成する。
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
  private List<Map<Member_Info_TestKeys, String>> makeAssignDateFailedData(){

    List<Map<Member_Info_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_before_joindate"};

    for(String key: key_name){
      List<Map<Member_Info_TestKeys, String>> base_data = makeBaseData();

      for(Map<Member_Info_TestKeys, String> base_map: base_data){
        base_map.put(Member_Info_TestKeys.Assign_Date, this.form_yml.getAssign_date().get(key));
        error_list.add(base_map);
      }
    }

    return error_list;
  }







  /** 
   **********************************************************************************************
   * @brief 項目[担当楽器]でエラーになるテストケースを格納したマップリストを作成する。
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
  private List<Map<Member_Info_TestKeys, String>> makeInstChargeFailedData(){

    List<Map<Member_Info_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_overflow", 
                         "ng_symbol", 
                         "ng_alpha", 
                         "ng_digit", 
                         "ng_hankaku_kana"};

    for(String key: key_name){
      List<Map<Member_Info_TestKeys, String>> base_data = makeBaseData();

      for(Map<Member_Info_TestKeys, String> base_map: base_data){
        base_map.put(Member_Info_TestKeys.Inst_Charge, this.form_yml.getInst_charge().get(key));
        error_list.add(base_map);
      }
    }

    return error_list;
  }







  /** 
   **********************************************************************************************
   * @brief 項目[その他コメント]でエラーになるテストケースを格納したマップリストを作成する。
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
  private List<Map<Member_Info_TestKeys, String>> makeOtherCommentFailedData(){

    List<Map<Member_Info_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_overflow", 
                         "ng_symbol", 
                         "ng_alpha", 
                         "ng_digit", 
                         "ng_hankaku_kana"};

    for(String key: key_name){
      List<Map<Member_Info_TestKeys, String>> base_data = makeBaseData();

      for(Map<Member_Info_TestKeys, String> base_map: base_data){
        base_map.put(Member_Info_TestKeys.Other_Comment, this.form_yml.getOther_comment().get(key));
        error_list.add(base_map);
      }
    }

    return error_list;
  }
}
