/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Database
 * 
 * @brief [団員管理]に関するテストケース生成処理のうち、[データベース]
 * 関連のテストケースを生成する処理を格納したパッケージ
 * 
 * @details
 * - このパッケージには、テストケースを記述したYAMLファイルとマッピングするエンティティや、
 * 取り込んだテストケースをデータベースにセットアップしたりする機能を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Database;





/** 
 **************************************************************************************
 * @file Member_Info_Database_TestCase.java
 * @brief 主に[楽譜管理]機能のテストにおいて、データベースとの通信テストの際に、あらかじめ
 * データベース内に初期値データを格納してセットアップするクラスを格納したファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.springproject.dockerspring.Entity.HistoryEntity.Member_Info_History;
import com.springproject.dockerspring.Entity.NormalEntity.Member_Info;








/** 
 **************************************************************************************
 * @brief 主に[楽譜管理]機能のテストにおいて、データベースとの通信テストの際に、あらかじめ
 * データベース内に初期値データを格納してセットアップするクラス。
 * 
 * @details 
 * - このクラスでは、データベースの初期化のほか、初期化時に使用したデータをテスト時の比較用として
 * モックのエンティティに格納して返却が可能。
 * - 必要に応じてファイルサーバーと連携してデータベースとファイルサーバーのどちらも初期化すること
 * が可能である。
 * - データベースへの保存に失敗するテストケースを、モックのエンティティに格納して返却が可能。
 * - なお、このクラスは通常データ用と履歴データ用で共用で用いる。
 * 
 * @par 使用アノテーション
 * - @Component
 * - @Import({ConfigureFactory.class, History_TestCase_Make.class})
 * 
 * @see Member_Info_Database_Yaml
 * @see ConfigureFactory
 * @see History_TestCase_Make
 **************************************************************************************
 */ 
@Component
@Import({ConfigureFactory.class, History_TestCase_Make.class})
public class Member_Info_Database_TestCase{
  
  private final Member_Info_Database_Yaml database_yml;
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
   * @param[in] hist_testcase_make 履歴情報のデータベース関連のテストケースクラス
   * 
   * @par 処理の大まかな流れ
   * -# 引数で渡されたクラスを、コンストラクタインジェクションする。
   * -# 指定されたYMLファイルのテストケースを読み込む。
   * -# 読み込んだYMLファイルを専用のエンティティにマッピングする。
   * -# インジェクションしたクラスを、フィールド変数に格納する。
   * 
   * @see Member_Info_Database_Yaml
   * @see ConfigureFactory
   * @see History_TestCase_Make
   **********************************************************************************************
   */ 
  @Autowired
  public Member_Info_Database_TestCase(ConfigureFactory config_factory, 
                                       History_TestCase_Make hist_testcase_make) throws IOException{

    try(InputStream in_database_yml = new ClassPathResource("TestCaseFile/MemberInfo/member-info-database.yml").getInputStream()){
      Yaml yaml = new Yaml();
      this.database_yml = yaml.loadAs(in_database_yml, Member_Info_Database_Yaml.class);
    }

    this.config_factory = config_factory;
    this.hist_testcase_make = hist_testcase_make;
  }











  /** @name 通常データのテスト時のセットアップ */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief リポジトリの検査時に用いるデータベースに、テストケースのデータを保存する。
   * 
   * @par 処理の大まかな流れ
   * -# 保存前に保存対象のテーブルを初期化するクエリを組み立てる。
   * -# 組み上げたクエリを実行する。
   **********************************************************************************************
   */ 
  public void databaseSetup() throws IOException{

    List<Operation> operation_list = new ArrayList<>();

    operation_list.add(FOREIGN_KEY_CHECK_0);
    operation_list.add(NORMAL_TABLE);
    operation_list.add(FOREIGN_KEY_CHECK_1);

    for(int i = 1; i <= 20; i++){
      String filename = this.database_yml.getFace_photo().get("case_" + i);
      byte[] photo_data = null;

      try(InputStream in = new ClassPathResource("TestCaseFile/MemberInfo/BlobTestCaseData/setup/" + filename).getInputStream();
          BufferedInputStream buf = new BufferedInputStream(in);){
  
        photo_data = IOUtils.toByteArray(buf);
      }

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
                                          this.database_yml.getMember_id().get("case_" + i), 
                                          this.database_yml.getName().get("case_" + i), 
                                          this.database_yml.getName_pronu().get("case_" + i), 
                                          this.database_yml.getSex().get("case_" + i), 
                                          this.database_yml.getBirthday().get("case_" + i), 
                                          photo_data, 
                                          this.database_yml.getJoin_date().get("case_" + i), 
                                          this.database_yml.getRet_date().get("case_" + i), 
                                          this.database_yml.getEmail_1().get("case_" + i), 
                                          this.database_yml.getEmail_2().get("case_" + i), 
                                          this.database_yml.getTel_1().get("case_" + i), 
                                          this.database_yml.getTel_2().get("case_" + i), 
                                          this.database_yml.getAddr_postcode().get("case_" + i), 
                                          this.database_yml.getAddr().get("case_" + i), 
                                          this.database_yml.getPosition().get("case_" + i), 
                                          this.database_yml.getPosition_arri_date().get("case_" + i), 
                                          this.database_yml.getJob().get("case_" + i), 
                                          this.database_yml.getAssign_dept().get("case_" + i), 
                                          this.database_yml.getAssign_date().get("case_" + i), 
                                          this.database_yml.getInst_charge().get("case_" + i), 
                                          this.database_yml.getOther_comment().get("case_" + i))
                                  .build();

      operation_list.add(operation);
    }

    this.config_factory.databaseExec(operation_list);
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
  public void databaseReset(){

    List<Operation> operation_list = new ArrayList<>();

    operation_list.add(FOREIGN_KEY_CHECK_0);
    operation_list.add(NORMAL_TABLE);
    operation_list.add(FOREIGN_KEY_CHECK_1);

    this.config_factory.databaseExec(operation_list);
  }









  /** 
   **********************************************************************************************
   * @brief データベースの初期化データとして使ったデータを用いて、テストの際に比較用として用いるモックの
   * エンティティを作成する。
   * 
   * @details
   * - データベースの初期化データと内容が同じなので、検索メソッドテストの際の結果の比較などに幅広く用いる
   * 事が可能である。
   * - 範囲外の数字が来たときは処理を続行できないのでエラーを投げる。
   * 
   * @param[in] testcase_num 指定するテストケースの番号
   * 
   * @return データが格納されているモックエンティティ
   * 
   * @par 処理の大まかな流れ
   * -# 指定した番号のテストケースデータを、マッピングエンティティから取り出す。
   * -# 取り出したデータを、作成したモックオブジェクトに格納する。
   * -# 格納が終わったモックを、戻り値とする。
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  public Member_Info compareEntityMake(Integer testcase_num) throws IOException{

    if(testcase_num > 20 || testcase_num < 1){
      throw new IllegalArgumentException();
    }else{
      Member_Info mock_entity = mock(Member_Info.class);

      String filename = this.database_yml.getFace_photo().get("case_" + testcase_num);
      byte[] photo_data = null;
      
      try(InputStream in = new ClassPathResource("TestCaseFile/MemberInfo/BlobTestCaseData/setup/" + filename).getInputStream();
          BufferedInputStream buf = new BufferedInputStream(in);){
        
        photo_data = IOUtils.toByteArray(buf);
      }

      when(mock_entity.getSerial_num()).thenReturn(testcase_num);
      when(mock_entity.getMember_id()).thenReturn(this.database_yml.getMember_id().get("case_" + testcase_num));
      when(mock_entity.getName()).thenReturn(this.database_yml.getName().get("case_" + testcase_num));
      when(mock_entity.getName_pronu()).thenReturn(this.database_yml.getName_pronu().get("case_" + testcase_num));
      when(mock_entity.getSex()).thenReturn(this.database_yml.getSex().get("case_" + testcase_num));
      when(mock_entity.getBirthday()).thenReturn(this.database_yml.getBirthday().get("case_" + testcase_num));
      when(mock_entity.getFace_photo()).thenReturn(photo_data);
      when(mock_entity.getJoin_date()).thenReturn(this.database_yml.getJoin_date().get("case_" + testcase_num));
      when(mock_entity.getRet_date()).thenReturn(this.database_yml.getRet_date().get("case_" + testcase_num));
      when(mock_entity.getEmail_1()).thenReturn(this.database_yml.getEmail_1().get("case_" + testcase_num));
      when(mock_entity.getEmail_2()).thenReturn(this.database_yml.getEmail_2().get("case_" + testcase_num));
      when(mock_entity.getTel_1()).thenReturn(this.database_yml.getTel_1().get("case_" + testcase_num));
      when(mock_entity.getTel_2()).thenReturn(this.database_yml.getTel_2().get("case_" + testcase_num));
      when(mock_entity.getAddr_postcode()).thenReturn(this.database_yml.getAddr_postcode().get("case_" + testcase_num));
      when(mock_entity.getAddr()).thenReturn(this.database_yml.getAddr().get("case_" + testcase_num));
      when(mock_entity.getPosition()).thenReturn(this.database_yml.getPosition().get("case_" + testcase_num));
      when(mock_entity.getPosition_arri_date()).thenReturn(this.database_yml.getPosition_arri_date().get("case_" + testcase_num));
      when(mock_entity.getJob()).thenReturn(this.database_yml.getJob().get("case_" + testcase_num));
      when(mock_entity.getAssign_dept()).thenReturn(this.database_yml.getAssign_dept().get("case_" + testcase_num));
      when(mock_entity.getAssign_date()).thenReturn(this.database_yml.getAssign_date().get("case_" + testcase_num));
      when(mock_entity.getInst_charge()).thenReturn(this.database_yml.getInst_charge().get("case_" + testcase_num));
      when(mock_entity.getOther_comment()).thenReturn(this.database_yml.getOther_comment().get("case_" + testcase_num));

      return mock_entity;
    }
  }











  /** 
   **********************************************************************************************
   * @brief リポジトリでの更新処理の際に使用する、更新後のデータのモックエンティティを作成する。
   * 
   * @details
   * - 更新後のデータの内容としては、明らかに既存のテストケースの値とは被らないような適当な値を用いている。
   * - 範囲外の数字が来たときは処理を続行できないのでエラーを投げる。
   * 
   * @param[in] testcase_num 指定するテストケースの番号
   * 
   * @return データが格納されているモックエンティティ
   * 
   * @par 処理の大まかな流れ
   * -# 適当な被らない値を、作成したモックオブジェクトに格納する。
   * -# 格納が終わったモックを、戻り値とする。
   * 
   * @throw ParseException
   **********************************************************************************************
   */ 
  public Member_Info UpdateAfterEntityMake(Integer testcase_num) throws ParseException{

    SimpleDateFormat parse_date = new SimpleDateFormat("yyyy-MM-dd");

    if(testcase_num > 20 || testcase_num < 1){
      throw new IllegalArgumentException();
    }else{
      Member_Info mock_entity = mock(Member_Info.class);

      when(mock_entity.getSerial_num()).thenReturn(testcase_num);
      when(mock_entity.getMember_id()).thenReturn("id" + testcase_num);
      when(mock_entity.getName()).thenReturn("Blank");
      when(mock_entity.getName_pronu()).thenReturn("Blank");
      when(mock_entity.getSex()).thenReturn("male");
      when(mock_entity.getBirthday()).thenReturn(parse_date.parse("1996-2-19"));
      when(mock_entity.getFace_photo()).thenReturn(null);
      when(mock_entity.getJoin_date()).thenReturn(parse_date.parse("1996-2-19"));
      when(mock_entity.getRet_date()).thenReturn(parse_date.parse("1996-2-19"));
      when(mock_entity.getEmail_1()).thenReturn("Blank");
      when(mock_entity.getEmail_2()).thenReturn("Blank");
      when(mock_entity.getTel_1()).thenReturn("Blank");
      when(mock_entity.getTel_2()).thenReturn("Blank");
      when(mock_entity.getAddr_postcode()).thenReturn("Blank");
      when(mock_entity.getAddr()).thenReturn("Blank");
      when(mock_entity.getPosition()).thenReturn("Blank");
      when(mock_entity.getPosition_arri_date()).thenReturn(parse_date.parse("1996-2-19"));
      when(mock_entity.getJob()).thenReturn("Blank");
      when(mock_entity.getAssign_dept()).thenReturn("Blank");
      when(mock_entity.getAssign_date()).thenReturn(parse_date.parse("1996-2-19"));
      when(mock_entity.getInst_charge()).thenReturn("Blank");
      when(mock_entity.getOther_comment()).thenReturn("Blank");

      return mock_entity;
    }
  }





  





  /** 
   **********************************************************************************************
   * @brief リポジトリでの更新＆追加処理の際に使用する、カラムに許容される文字数ギリギリの文字数を格納した
   * モックエンティティを作成する。
   * 
   * @return データが格納されているモックエンティティ
   * 
   * @par 処理の大まかな流れ
   * -# 指定した番号のテストケースデータを、マッピングエンティティから取り出す。
   * -# 取り出したデータを、作成したモックオブジェクトに格納する。
   * -# 格納が終わったモックを、戻り値とする。
   **********************************************************************************************
   */ 
  public Member_Info justInTimeEntityMake(){

    Member_Info mock_entity = mock(Member_Info.class);

    when(mock_entity.getSerial_num()).thenReturn(1);
    when(mock_entity.getMember_id()).thenReturn(this.database_yml.getMember_id().get("ok_length"));
    when(mock_entity.getName()).thenReturn(this.database_yml.getName().get("ok_length"));
    when(mock_entity.getName_pronu()).thenReturn(this.database_yml.getName_pronu().get("ok_length"));
    when(mock_entity.getSex()).thenReturn(this.database_yml.getSex().get("case_1"));
    when(mock_entity.getBirthday()).thenReturn(this.database_yml.getBirthday().get("case_1"));
    when(mock_entity.getFace_photo()).thenReturn(null);
    when(mock_entity.getJoin_date()).thenReturn(this.database_yml.getJoin_date().get("case_1"));
    when(mock_entity.getRet_date()).thenReturn(this.database_yml.getRet_date().get("case_1"));
    when(mock_entity.getEmail_1()).thenReturn(this.database_yml.getEmail_1().get("ok_length"));
    when(mock_entity.getEmail_2()).thenReturn(this.database_yml.getEmail_2().get("ok_length"));
    when(mock_entity.getTel_1()).thenReturn(this.database_yml.getTel_1().get("ok_length"));
    when(mock_entity.getTel_2()).thenReturn(this.database_yml.getTel_2().get("ok_length"));
    when(mock_entity.getAddr_postcode()).thenReturn(this.database_yml.getAddr_postcode().get("ok_length"));
    when(mock_entity.getAddr()).thenReturn(this.database_yml.getAddr().get("ok_length"));
    when(mock_entity.getPosition()).thenReturn(this.database_yml.getPosition().get("ok_length"));
    when(mock_entity.getPosition_arri_date()).thenReturn(this.database_yml.getPosition_arri_date().get("case_1"));
    when(mock_entity.getJob()).thenReturn(this.database_yml.getJob().get("ok_length"));
    when(mock_entity.getAssign_dept()).thenReturn(this.database_yml.getAssign_dept().get("ok_length"));
    when(mock_entity.getAssign_date()).thenReturn(this.database_yml.getAssign_date().get("case_1"));
    when(mock_entity.getInst_charge()).thenReturn(this.database_yml.getInst_charge().get("ok_length"));
    when(mock_entity.getOther_comment()).thenReturn(this.database_yml.getOther_comment().get("ok_length"));

    return mock_entity;
  }










  /** 
   **********************************************************************************************
   * @brief リポジトリでの更新＆追加処理の際に使用する、カラムに許容されるNullの値を入れた場合の
   * モックエンティティを作成する。
   * 
   * @return データが格納されているモックエンティティ
   * 
   * @par 処理の大まかな流れ
   * -# 指定した番号のテストケースデータを、マッピングエンティティから取り出す。
   * -# 取り出したデータを、作成したモックオブジェクトに格納する。
   * -# 格納が終わったモックを、戻り値とする。
   **********************************************************************************************
   */ 
  public Member_Info okNullEntityMake(){

    Member_Info mock_entity = mock(Member_Info.class);

    when(mock_entity.getSerial_num()).thenReturn(1);
    when(mock_entity.getMember_id()).thenReturn(this.database_yml.getMember_id().get("ok_null_inspect"));
    when(mock_entity.getName()).thenReturn(this.database_yml.getName().get("case_1"));
    when(mock_entity.getName_pronu()).thenReturn(this.database_yml.getName_pronu().get("case_1"));
    when(mock_entity.getSex()).thenReturn(this.database_yml.getSex().get("case_1"));
    when(mock_entity.getBirthday()).thenReturn(this.database_yml.getBirthday().get("case_1"));
    when(mock_entity.getFace_photo()).thenReturn(null);
    when(mock_entity.getJoin_date()).thenReturn(this.database_yml.getJoin_date().get("case_1"));
    when(mock_entity.getRet_date()).thenReturn(this.database_yml.getRet_date().get("ok_null"));
    when(mock_entity.getEmail_1()).thenReturn(this.database_yml.getEmail_1().get("ok_null"));
    when(mock_entity.getEmail_2()).thenReturn(this.database_yml.getEmail_2().get("ok_null"));
    when(mock_entity.getTel_1()).thenReturn(this.database_yml.getTel_1().get("ok_null"));
    when(mock_entity.getTel_2()).thenReturn(this.database_yml.getTel_2().get("ok_null"));
    when(mock_entity.getAddr_postcode()).thenReturn(this.database_yml.getAddr_postcode().get("ok_null"));
    when(mock_entity.getAddr()).thenReturn(this.database_yml.getAddr().get("ok_null"));
    when(mock_entity.getPosition()).thenReturn(this.database_yml.getPosition().get("ok_null"));
    when(mock_entity.getPosition_arri_date()).thenReturn(this.database_yml.getPosition_arri_date().get("ok_null"));
    when(mock_entity.getJob()).thenReturn(this.database_yml.getJob().get("ok_null"));
    when(mock_entity.getAssign_dept()).thenReturn(this.database_yml.getAssign_dept().get("ok_null"));
    when(mock_entity.getAssign_date()).thenReturn(this.database_yml.getAssign_date().get("ok_null"));
    when(mock_entity.getInst_charge()).thenReturn(this.database_yml.getInst_charge().get("ok_null"));
    when(mock_entity.getOther_comment()).thenReturn(this.database_yml.getOther_comment().get("ok_null"));

    return mock_entity;
  }










  /** 
   **********************************************************************************************
   * @brief リポジトリでの更新＆追加処理の際に使用する、保存に失敗するテストケースのモックエンティティを作成。
   * 
   * @details
   * - ベースとなる成功ケースのモックエンティティを上書きすることで、実現する。
   * 
   * @return データが格納されているモックエンティティのリスト
   * 
   * @par 処理の大まかな流れ
   * -# ベースとなる成功ケースのモックエンティティを取得。
   * -# 取り出したデータを、作成したモックオブジェクトに上書きする。
   * -# 格納が終わったモックを、リストに格納する。
   * -# 格納が終わったリストを戻り値とする。
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  public List<Member_Info> failedEntityMake() throws IOException{

    List<Member_Info> fail_mock_list = new ArrayList<>();
    int i = 1;

    String[] member_id = {"ng_overflow", "ng_null"};
    for(String key_name: member_id){
      Member_Info mock_entity = compareEntityMake(1);

      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getMember_id()).thenReturn(this.database_yml.getMember_id().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] name = {"ng_overflow", "ng_null"};
    for(String key_name: name){
      Member_Info mock_entity = compareEntityMake(1);

      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getName()).thenReturn(this.database_yml.getName().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] name_pronu = {"ng_overflow", "ng_null"};
    for(String key_name: name_pronu){
      Member_Info mock_entity = compareEntityMake(1);

      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getName_pronu()).thenReturn(this.database_yml.getName_pronu().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] sex = {"ng_enum", "ng_null"};
    for(String key_name: sex){
      Member_Info mock_entity = compareEntityMake(1);

      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getSex()).thenReturn(this.database_yml.getSex().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] birthday = {"ng_null"};
    for(String key_name: birthday){
      Member_Info mock_entity = compareEntityMake(1);

      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getBirthday()).thenReturn(this.database_yml.getBirthday().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] join_date = {"ng_null"};
    for(String key_name: join_date){
      Member_Info mock_entity = compareEntityMake(1);

      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getJoin_date()).thenReturn(this.database_yml.getJoin_date().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] email_1 = {"ng_overflow"};
    for(String key_name: email_1){
      Member_Info mock_entity = compareEntityMake(1);

      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getEmail_1()).thenReturn(this.database_yml.getEmail_1().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] email_2 = {"ng_overflow"};
    for(String key_name: email_2){
      Member_Info mock_entity = compareEntityMake(1);

      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getEmail_2()).thenReturn(this.database_yml.getEmail_2().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] tel_1 = {"ng_overflow"};
    for(String key_name: tel_1){
      Member_Info mock_entity = compareEntityMake(1);

      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getTel_1()).thenReturn(this.database_yml.getTel_1().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] tel_2 = {"ng_overflow"};
    for(String key_name: tel_2){
      Member_Info mock_entity = compareEntityMake(1);

      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getTel_2()).thenReturn(this.database_yml.getTel_2().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] addr_postcode = {"ng_overflow"};
    for(String key_name: addr_postcode){
      Member_Info mock_entity = compareEntityMake(1);

      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getAddr_postcode()).thenReturn(this.database_yml.getAddr_postcode().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] addr = {"ng_overflow"};
    for(String key_name: addr){
      Member_Info mock_entity = compareEntityMake(1);

      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getAddr()).thenReturn(this.database_yml.getAddr().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] position = {"ng_overflow"};
    for(String key_name: position){
      Member_Info mock_entity = compareEntityMake(1);

      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getPosition()).thenReturn(this.database_yml.getPosition().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] job = {"ng_overflow"};
    for(String key_name: job){
      Member_Info mock_entity = compareEntityMake(1);

      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getJob()).thenReturn(this.database_yml.getJob().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] assign_dept = {"ng_overflow"};
    for(String key_name: assign_dept){
      Member_Info mock_entity = compareEntityMake(1);

      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getAssign_dept()).thenReturn(this.database_yml.getAssign_dept().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] inst_charge = {"ng_overflow"};
    for(String key_name: inst_charge){
      Member_Info mock_entity = compareEntityMake(1);

      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getInst_charge()).thenReturn(this.database_yml.getInst_charge().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] comment = {"ng_overflow"};
    for(String key_name: comment){
      Member_Info mock_entity = compareEntityMake(1);

      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getOther_comment()).thenReturn(this.database_yml.getOther_comment().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    return fail_mock_list;
  }









  /** 
   **********************************************************************************************
   * @brief リポジトリでの更新＆追加処理の際に使用する、一意制約違反により失敗するテストケースを作成。
   * 
   * @details
   * - ベースとなる成功ケースのモックエンティティを上書きすることで、実現する。
   * 
   * @return データが格納されているモックエンティティのリスト
   * 
   * @par 処理の大まかな流れ
   * -# ベースとなる成功ケースのモックエンティティを取得。
   * -# 取り出したデータを、作成したモックオブジェクトに上書きする。
   * -# 格納が終わったモックを、リストに格納する。
   * -# 格納が終わったリストを戻り値とする。
   **********************************************************************************************
   */ 
  public Member_Info UniqueMissEntityMake(){

    Member_Info mock_entity = mock(Member_Info.class);

    when(mock_entity.getSerial_num()).thenReturn(1);
    when(mock_entity.getMember_id()).thenReturn(this.database_yml.getMember_id().get("ng_unique"));
    when(mock_entity.getName()).thenReturn(this.database_yml.getName().get("case_1"));
    when(mock_entity.getName_pronu()).thenReturn(this.database_yml.getName_pronu().get("case_1"));
    when(mock_entity.getSex()).thenReturn(this.database_yml.getSex().get("case_1"));
    when(mock_entity.getBirthday()).thenReturn(this.database_yml.getBirthday().get("case_1"));
    when(mock_entity.getFace_photo()).thenReturn(null);
    when(mock_entity.getJoin_date()).thenReturn(this.database_yml.getJoin_date().get("case_1"));
    when(mock_entity.getRet_date()).thenReturn(this.database_yml.getRet_date().get("case_1"));
    when(mock_entity.getEmail_1()).thenReturn(this.database_yml.getEmail_1().get("case_1"));
    when(mock_entity.getEmail_2()).thenReturn(this.database_yml.getEmail_2().get("case_1"));
    when(mock_entity.getTel_1()).thenReturn(this.database_yml.getTel_1().get("case_1"));
    when(mock_entity.getTel_2()).thenReturn(this.database_yml.getTel_2().get("case_1"));
    when(mock_entity.getAddr_postcode()).thenReturn(this.database_yml.getAddr_postcode().get("case_1"));
    when(mock_entity.getAddr()).thenReturn(this.database_yml.getAddr().get("case_1"));
    when(mock_entity.getPosition()).thenReturn(this.database_yml.getPosition().get("case_1"));
    when(mock_entity.getPosition_arri_date()).thenReturn(this.database_yml.getPosition_arri_date().get("case_1"));
    when(mock_entity.getJob()).thenReturn(this.database_yml.getJob().get("case_1"));
    when(mock_entity.getAssign_dept()).thenReturn(this.database_yml.getAssign_dept().get("case_1"));
    when(mock_entity.getAssign_date()).thenReturn(this.database_yml.getAssign_date().get("case_1"));
    when(mock_entity.getInst_charge()).thenReturn(this.database_yml.getInst_charge().get("case_1"));
    when(mock_entity.getOther_comment()).thenReturn(this.database_yml.getOther_comment().get("case_1"));

    return mock_entity;
  }

  /** @} */














  /** @name 履歴データのテスト時のセットアップ */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief 履歴用のリポジトリの検査時に用いるデータベースに、テストケースのデータを保存する。
   * 
   * @par 処理の大まかな流れ
   * -# 保存前に保存対象のテーブルを初期化するクエリを組み立てる。
   * -# 組み上げたクエリを実行する。
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  public void historyDatabaseSetup() throws IOException{

    List<Operation> operation_list = new ArrayList<>();

    operation_list.add(FOREIGN_KEY_CHECK_0);
    operation_list.add(HISTORY_TABLE);
    operation_list.add(FOREIGN_KEY_CHECK_1);

    for(int i = 1; i <= 20; i++){
      String filename = this.database_yml.getFace_photo().get("case_" + i);
      byte[] photo_data = null;

      try(InputStream in = new ClassPathResource("TestCaseFile/MemberInfo/BlobTestCaseData/setup/" + filename).getInputStream();
          BufferedInputStream buf = new BufferedInputStream(in);){
  
        photo_data = IOUtils.toByteArray(buf);
      }
      
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
                                          this.hist_testcase_make.getCompareChange_datetime(i), 
                                          this.hist_testcase_make.getCompareChange_kinds(i), 
                                          this.hist_testcase_make.getCompareOperation_user(i), 
                                          i, 
                                          this.hist_testcase_make.getCompareId(i), 
                                          this.database_yml.getName().get("case_" + i), 
                                          this.database_yml.getName_pronu().get("case_" + i), 
                                          this.database_yml.getSex().get("case_" + i), 
                                          this.database_yml.getBirthday().get("case_" + i), 
                                          photo_data, 
                                          this.database_yml.getJoin_date().get("case_" + i), 
                                          this.database_yml.getRet_date().get("case_" + i), 
                                          this.database_yml.getEmail_1().get("case_" + i), 
                                          this.database_yml.getEmail_2().get("case_" + i), 
                                          this.database_yml.getTel_1().get("case_" + i), 
                                          this.database_yml.getTel_2().get("case_" + i), 
                                          this.database_yml.getAddr_postcode().get("case_" + i), 
                                          this.database_yml.getAddr().get("case_" + i), 
                                          this.database_yml.getPosition().get("case_" + i), 
                                          this.database_yml.getPosition_arri_date().get("case_" + i), 
                                          this.database_yml.getJob().get("case_" + i), 
                                          this.database_yml.getAssign_dept().get("case_" + i), 
                                          this.database_yml.getAssign_date().get("case_" + i), 
                                          this.database_yml.getInst_charge().get("case_" + i), 
                                          this.database_yml.getOther_comment().get("case_" + i))
                                  .build();

      operation_list.add(operation);
    }

    this.config_factory.databaseExec(operation_list);
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
  public void historyDatabaseReset(){

    List<Operation> operation_list = new ArrayList<>();

    operation_list.add(FOREIGN_KEY_CHECK_0);
    operation_list.add(HISTORY_TABLE);
    operation_list.add(FOREIGN_KEY_CHECK_1);

    this.config_factory.databaseExec(operation_list);
  }









  /** 
   **********************************************************************************************
   * @brief 履歴用データベースの初期化データとして使ったデータを用いて、テストの際に比較用として用いる
   * モックのエンティティを作成する。
   * 
   * @details
   * - データベースの初期化データと内容が同じなので、検索メソッドテストの際の結果の比較などに幅広く用いる
   * 事が可能である。
   * - 範囲外の数字が来たときは処理を続行できないのでエラーを投げる。
   * 
   * @param[in] testcase_num 指定するテストケースの番号
   * 
   * @return データが格納されているモックエンティティ
   * 
   * @par 処理の大まかな流れ
   * -# 指定した番号のテストケースデータを、マッピングエンティティから取り出す。
   * -# 取り出したデータを、作成したモックオブジェクトに格納する。
   * -# 格納が終わったモックを、戻り値とする。
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  public Member_Info_History compareHistoryEntityMake(Integer testcase_num) throws IOException{

    if(testcase_num > 20 || testcase_num < 1){
      throw new IllegalArgumentException();
    }else{
      Member_Info_History mock_entity = mock(Member_Info_History.class);

      String filename = this.database_yml.getFace_photo().get("case_" + testcase_num);
      byte[] photo_data = null;
      
      try(InputStream in = new ClassPathResource("TestCaseFile/MemberInfo/BlobTestCaseData/setup/" + filename).getInputStream();
      BufferedInputStream buf = new BufferedInputStream(in);){
        
        photo_data = IOUtils.toByteArray(buf);
      }

      when(mock_entity.getHistory_id()).thenReturn(testcase_num);
      when(mock_entity.getChange_datetime()).thenReturn(this.hist_testcase_make.getCompareChange_datetime(testcase_num));
      when(mock_entity.getChange_kinds()).thenReturn(this.hist_testcase_make.getCompareChange_kinds(testcase_num));
      when(mock_entity.getOperation_user()).thenReturn(this.hist_testcase_make.getCompareOperation_user(testcase_num));
      when(mock_entity.getSerial_num()).thenReturn(testcase_num);
      when(mock_entity.getMember_id()).thenReturn(this.hist_testcase_make.getCompareId(testcase_num));
      when(mock_entity.getName()).thenReturn(this.database_yml.getName().get("case_" + testcase_num));
      when(mock_entity.getName_pronu()).thenReturn(this.database_yml.getName_pronu().get("case_" + testcase_num));
      when(mock_entity.getSex()).thenReturn(this.database_yml.getSex().get("case_" + testcase_num));
      when(mock_entity.getBirthday()).thenReturn(this.database_yml.getBirthday().get("case_" + testcase_num));
      when(mock_entity.getFace_photo()).thenReturn(photo_data);
      when(mock_entity.getJoin_date()).thenReturn(this.database_yml.getJoin_date().get("case_" + testcase_num));
      when(mock_entity.getRet_date()).thenReturn(this.database_yml.getRet_date().get("case_" + testcase_num));
      when(mock_entity.getEmail_1()).thenReturn(this.database_yml.getEmail_1().get("case_" + testcase_num));
      when(mock_entity.getEmail_2()).thenReturn(this.database_yml.getEmail_2().get("case_" + testcase_num));
      when(mock_entity.getTel_1()).thenReturn(this.database_yml.getTel_1().get("case_" + testcase_num));
      when(mock_entity.getTel_2()).thenReturn(this.database_yml.getTel_2().get("case_" + testcase_num));
      when(mock_entity.getAddr_postcode()).thenReturn(this.database_yml.getAddr_postcode().get("case_" + testcase_num));
      when(mock_entity.getAddr()).thenReturn(this.database_yml.getAddr().get("case_" + testcase_num));
      when(mock_entity.getPosition()).thenReturn(this.database_yml.getPosition().get("case_" + testcase_num));
      when(mock_entity.getPosition_arri_date()).thenReturn(this.database_yml.getPosition_arri_date().get("case_" + testcase_num));
      when(mock_entity.getJob()).thenReturn(this.database_yml.getJob().get("case_" + testcase_num));
      when(mock_entity.getAssign_dept()).thenReturn(this.database_yml.getAssign_dept().get("case_" + testcase_num));
      when(mock_entity.getAssign_date()).thenReturn(this.database_yml.getAssign_date().get("case_" + testcase_num));
      when(mock_entity.getInst_charge()).thenReturn(this.database_yml.getInst_charge().get("case_" + testcase_num));
      when(mock_entity.getOther_comment()).thenReturn(this.database_yml.getOther_comment().get("case_" + testcase_num));

      return mock_entity;
    }
  }









  /** 
   **********************************************************************************************
   * @brief 履歴用リポジトリでの更新＆追加処理の際に使用する、カラムに許容される文字数ギリギリの文字数を
   * 格納したモックエンティティを作成する。
   * 
   * @return データが格納されているモックエンティティ
   * 
   * @par 処理の大まかな流れ
   * -# 指定した番号のテストケースデータを、マッピングエンティティから取り出す。
   * -# 取り出したデータを、作成したモックオブジェクトに格納する。
   * -# 格納が終わったモックを、戻り値とする。
   **********************************************************************************************
   */ 
  public Member_Info_History justInTimeHistoryEntityMake(){

    Member_Info_History mock_entity = mock(Member_Info_History.class);

    when(mock_entity.getHistory_id()).thenReturn(1);
    when(mock_entity.getChange_datetime()).thenReturn(this.hist_testcase_make.getCompareChange_datetime(1));
    when(mock_entity.getChange_kinds()).thenReturn(this.hist_testcase_make.getCompareChange_kinds(1));
    when(mock_entity.getOperation_user()).thenReturn(this.hist_testcase_make.getCompareOperation_user(null));
    when(mock_entity.getSerial_num()).thenReturn(1);
    when(mock_entity.getMember_id()).thenReturn(this.database_yml.getMember_id().get("ok_length"));
    when(mock_entity.getName()).thenReturn(this.database_yml.getName().get("ok_length"));
    when(mock_entity.getName_pronu()).thenReturn(this.database_yml.getName_pronu().get("ok_length"));
    when(mock_entity.getSex()).thenReturn(this.database_yml.getSex().get("case_1"));
    when(mock_entity.getBirthday()).thenReturn(this.database_yml.getBirthday().get("case_1"));
    when(mock_entity.getFace_photo()).thenReturn(null);
    when(mock_entity.getJoin_date()).thenReturn(this.database_yml.getJoin_date().get("case_1"));
    when(mock_entity.getRet_date()).thenReturn(this.database_yml.getRet_date().get("case_1"));
    when(mock_entity.getEmail_1()).thenReturn(this.database_yml.getEmail_1().get("ok_length"));
    when(mock_entity.getEmail_2()).thenReturn(this.database_yml.getEmail_2().get("ok_length"));
    when(mock_entity.getTel_1()).thenReturn(this.database_yml.getTel_1().get("ok_length"));
    when(mock_entity.getTel_2()).thenReturn(this.database_yml.getTel_2().get("ok_length"));
    when(mock_entity.getAddr_postcode()).thenReturn(this.database_yml.getAddr_postcode().get("ok_length"));
    when(mock_entity.getAddr()).thenReturn(this.database_yml.getAddr().get("ok_length"));
    when(mock_entity.getPosition()).thenReturn(this.database_yml.getPosition().get("ok_length"));
    when(mock_entity.getPosition_arri_date()).thenReturn(this.database_yml.getPosition_arri_date().get("case_1"));
    when(mock_entity.getJob()).thenReturn(this.database_yml.getJob().get("ok_length"));
    when(mock_entity.getAssign_dept()).thenReturn(this.database_yml.getAssign_dept().get("ok_length"));
    when(mock_entity.getAssign_date()).thenReturn(this.database_yml.getAssign_date().get("case_1"));
    when(mock_entity.getInst_charge()).thenReturn(this.database_yml.getInst_charge().get("ok_length"));
    when(mock_entity.getOther_comment()).thenReturn(this.database_yml.getOther_comment().get("ok_length"));

    return mock_entity;
  }










  /** 
   **********************************************************************************************
   * @brief リポジトリでの更新＆追加処理の際に使用する、カラムに許容されるNullの値を入れた場合の
   * モックエンティティを作成する。
   * 
   * @return データが格納されているモックエンティティ
   * 
   * @par 処理の大まかな流れ
   * -# 指定した番号のテストケースデータを、マッピングエンティティから取り出す。
   * -# 取り出したデータを、作成したモックオブジェクトに格納する。
   * -# 格納が終わったモックを、戻り値とする。
   **********************************************************************************************
   */ 
  public Member_Info_History okNullHistoryEntityMake(){

    Member_Info_History mock_entity = mock(Member_Info_History.class);

    when(mock_entity.getHistory_id()).thenReturn(1);
    when(mock_entity.getChange_datetime()).thenReturn(this.hist_testcase_make.getCompareChange_datetime(1));
    when(mock_entity.getChange_kinds()).thenReturn(this.hist_testcase_make.getCompareChange_kinds(1));
    when(mock_entity.getOperation_user()).thenReturn(this.hist_testcase_make.getCompareOperation_user(1));
    when(mock_entity.getSerial_num()).thenReturn(1);
    when(mock_entity.getMember_id()).thenReturn(this.database_yml.getMember_id().get("ok_null_inspect"));
    when(mock_entity.getName()).thenReturn(this.database_yml.getName().get("case_1"));
    when(mock_entity.getName_pronu()).thenReturn(this.database_yml.getName_pronu().get("case_1"));
    when(mock_entity.getSex()).thenReturn(this.database_yml.getSex().get("case_1"));
    when(mock_entity.getBirthday()).thenReturn(this.database_yml.getBirthday().get("case_1"));
    when(mock_entity.getFace_photo()).thenReturn(null);
    when(mock_entity.getJoin_date()).thenReturn(this.database_yml.getJoin_date().get("case_1"));
    when(mock_entity.getRet_date()).thenReturn(this.database_yml.getRet_date().get("ok_null"));
    when(mock_entity.getEmail_1()).thenReturn(this.database_yml.getEmail_1().get("ok_null"));
    when(mock_entity.getEmail_2()).thenReturn(this.database_yml.getEmail_2().get("ok_null"));
    when(mock_entity.getTel_1()).thenReturn(this.database_yml.getTel_1().get("ok_null"));
    when(mock_entity.getTel_2()).thenReturn(this.database_yml.getTel_2().get("ok_null"));
    when(mock_entity.getAddr_postcode()).thenReturn(this.database_yml.getAddr_postcode().get("ok_null"));
    when(mock_entity.getAddr()).thenReturn(this.database_yml.getAddr().get("ok_null"));
    when(mock_entity.getPosition()).thenReturn(this.database_yml.getPosition().get("ok_null"));
    when(mock_entity.getPosition_arri_date()).thenReturn(this.database_yml.getPosition_arri_date().get("ok_null"));
    when(mock_entity.getJob()).thenReturn(this.database_yml.getJob().get("ok_null"));
    when(mock_entity.getAssign_dept()).thenReturn(this.database_yml.getAssign_dept().get("ok_null"));
    when(mock_entity.getAssign_date()).thenReturn(this.database_yml.getAssign_date().get("ok_null"));
    when(mock_entity.getInst_charge()).thenReturn(this.database_yml.getInst_charge().get("ok_null"));
    when(mock_entity.getOther_comment()).thenReturn(this.database_yml.getOther_comment().get("ok_null"));

    return mock_entity;
  }










  /** 
   **********************************************************************************************
   * @brief 履歴用リポジトリでの更新＆追加処理の際に使用する、保存に失敗するテストケースのモック
   * エンティティを作成。
   * 
   * @details
   * - ベースとなる成功ケースのモックエンティティを上書きすることで、実現する。
   * 
   * @return データが格納されているモックエンティティのリスト
   * 
   * @par 処理の大まかな流れ
   * -# ベースとなる成功ケースのモックエンティティを取得。
   * -# 取り出したデータを、作成したモックオブジェクトに上書きする。
   * -# 格納が終わったモックを、リストに格納する。
   * -# 格納が終わったリストを戻り値とする。
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  public List<Member_Info_History> failedHistoryEntityMake() throws IOException{

    List<Member_Info_History> fail_mock_list = new ArrayList<>();
    int i = 0;

    for(Date fail_data: this.hist_testcase_make.getFailChange_datetime()){
      Member_Info_History mock_entity = compareHistoryEntityMake(1);
      
      i++;
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getChange_datetime()).thenReturn(fail_data);

      fail_mock_list.add(mock_entity);
    }

    for(String fail_data: this.hist_testcase_make.getFailChange_kinds()){
      Member_Info_History mock_entity = compareHistoryEntityMake(1);
            
      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getChange_kinds()).thenReturn(fail_data);

      fail_mock_list.add(mock_entity);
    }

    for(String fail_data: this.hist_testcase_make.getFailOperation_user()){
      Member_Info_History mock_entity = compareHistoryEntityMake(1);
            
      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getOperation_user()).thenReturn(fail_data);

      fail_mock_list.add(mock_entity);
    }

    String[] member_id = {"ng_overflow", "ng_null"};
    for(String key_name: member_id){
      Member_Info_History mock_entity = compareHistoryEntityMake(1);

      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getMember_id()).thenReturn(this.database_yml.getMember_id().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] name = {"ng_overflow", "ng_null"};
    for(String key_name: name){
      Member_Info_History mock_entity = compareHistoryEntityMake(1);

      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getName()).thenReturn(this.database_yml.getName().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] name_pronu = {"ng_overflow", "ng_null"};
    for(String key_name: name_pronu){
      Member_Info_History mock_entity = compareHistoryEntityMake(1);

      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getName_pronu()).thenReturn(this.database_yml.getName_pronu().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] sex = {"ng_enum", "ng_null"};
    for(String key_name: sex){
      Member_Info_History mock_entity = compareHistoryEntityMake(1);

      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getSex()).thenReturn(this.database_yml.getSex().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] birthday = {"ng_null"};
    for(String key_name: birthday){
      Member_Info_History mock_entity = compareHistoryEntityMake(1);

      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getBirthday()).thenReturn(this.database_yml.getBirthday().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] join_date = {"ng_null"};
    for(String key_name: join_date){
      Member_Info_History mock_entity = compareHistoryEntityMake(1);

      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getJoin_date()).thenReturn(this.database_yml.getJoin_date().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] email_1 = {"ng_overflow"};
    for(String key_name: email_1){
      Member_Info_History mock_entity = compareHistoryEntityMake(1);

      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getEmail_1()).thenReturn(this.database_yml.getEmail_1().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] email_2 = {"ng_overflow"};
    for(String key_name: email_2){
      Member_Info_History mock_entity = compareHistoryEntityMake(1);

      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getEmail_2()).thenReturn(this.database_yml.getEmail_2().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] tel_1 = {"ng_overflow"};
    for(String key_name: tel_1){
      Member_Info_History mock_entity = compareHistoryEntityMake(1);

      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getTel_1()).thenReturn(this.database_yml.getTel_1().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] tel_2 = {"ng_overflow"};
    for(String key_name: tel_2){
      Member_Info_History mock_entity = compareHistoryEntityMake(1);

      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getTel_2()).thenReturn(this.database_yml.getTel_2().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] addr_postcode = {"ng_overflow"};
    for(String key_name: addr_postcode){
      Member_Info_History mock_entity = compareHistoryEntityMake(1);

      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getAddr_postcode()).thenReturn(this.database_yml.getAddr_postcode().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] addr = {"ng_overflow"};
    for(String key_name: addr){
      Member_Info_History mock_entity = compareHistoryEntityMake(1);

      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getAddr()).thenReturn(this.database_yml.getAddr().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] position = {"ng_overflow"};
    for(String key_name: position){
      Member_Info_History mock_entity = compareHistoryEntityMake(1);

      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getPosition()).thenReturn(this.database_yml.getPosition().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] job = {"ng_overflow"};
    for(String key_name: job){
      Member_Info_History mock_entity = compareHistoryEntityMake(1);

      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getJob()).thenReturn(this.database_yml.getJob().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] assign_dept = {"ng_overflow"};
    for(String key_name: assign_dept){
      Member_Info_History mock_entity = compareHistoryEntityMake(1);

      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getAssign_dept()).thenReturn(this.database_yml.getAssign_dept().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] inst_charge = {"ng_overflow"};
    for(String key_name: inst_charge){
      Member_Info_History mock_entity = compareHistoryEntityMake(1);

      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getInst_charge()).thenReturn(this.database_yml.getInst_charge().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] comment = {"ng_overflow"};
    for(String key_name: comment){
      Member_Info_History mock_entity = compareHistoryEntityMake(1);

      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getOther_comment()).thenReturn(this.database_yml.getOther_comment().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    return fail_mock_list;
  }
}
