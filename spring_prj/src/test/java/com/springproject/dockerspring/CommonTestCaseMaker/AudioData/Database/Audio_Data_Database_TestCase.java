/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.AudioData.Database
 * 
 * @brief [音源データ管理機能]に関するテストケース生成処理のうち、[データベースとファイルサーバー]
 * 関連のテストケースを生成する処理を格納したパッケージ
 * 
 * @details
 * - このパッケージには、テストケースを記述したYAMLファイルとマッピングするエンティティや、
 * 取り込んだテストケースをデータベースにセットアップしたりする機能を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.AudioData.Database;





/** 
 **************************************************************************************
 * @file Audio_Data_Database_TestCase.java
 * @brief 主に[音源データ情報]機能のテストにおいて、データベースとの通信テストの際に、あらかじめ
 * データベース内に初期値データを格納してセットアップするクラスを格納したファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
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
import com.springproject.dockerspring.Entity.HistoryEntity.Audio_Data_History;
import com.springproject.dockerspring.Entity.NormalEntity.Audio_Data;









/** 
 **************************************************************************************
 * @brief 主に[音源データ情報]機能のテストにおいて、データベースとの通信テストの際に、あらかじめ
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
 * @see Audio_Data_Database_Yaml
 * @see ConfigureFactory
 * @see History_TestCase_Make
 * @see Audio_Data_Samba_TestCase
 **************************************************************************************
 */ 
@Component
@Import({ConfigureFactory.class, History_TestCase_Make.class})
public class Audio_Data_Database_TestCase{
  
  private final Audio_Data_Database_Yaml database_yml;
  private final History_TestCase_Make hist_testcase_make;
  private final Audio_Data_Samba_TestCase samba_testcase;
  private final ConfigureFactory config_factory;


  private final SqlOperation FOREIGN_KEY_CHECK_0 = SqlOperation.of("SET FOREIGN_KEY_CHECKS = 0");
  private final SqlOperation FOREIGN_KEY_CHECK_1 = SqlOperation.of("SET FOREIGN_KEY_CHECKS = 1");
  private final String NORMAL_TABLE_NAME = "Audio_Data";
  private final String HISTORY_TABLE_NAME = "Audio_Data_History";
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
   * @param[in] hist_testcase_make 履歴情報のテストケースクラス
   * @param[in] samba_testcase [音源データ情報]のファイルサーバー関連のテストケースクラス
   * 
   * @par 処理の大まかな流れ
   * -# 引数で渡されたクラスを、コンストラクタインジェクションする。
   * -# 指定されたYMLファイルのテストケースを読み込む。
   * -# 読み込んだYMLファイルを専用のエンティティにマッピングする。
   * -# インジェクションしたクラスを、フィールド変数に格納する。
   * 
   * @see Audio_Data_Database_Yaml
   * @see ConfigureFactory
   * @see History_TestCase_Make
   * @see Audio_Data_Samba_TestCase
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  @Autowired
  public Audio_Data_Database_TestCase(ConfigureFactory config_factory, 
                                      History_TestCase_Make hist_testcase_make, 
                                      Audio_Data_Samba_TestCase samba_testcase) throws IOException{

    try(InputStream in_database_yml = new ClassPathResource("TestCaseFile/AudioData/audio-data-database.yml").getInputStream()){
      Yaml yaml = new Yaml();
      this.database_yml = yaml.loadAs(in_database_yml, Audio_Data_Database_Yaml.class);
    }

    this.config_factory = config_factory;
    this.hist_testcase_make = hist_testcase_make;
    this.samba_testcase = samba_testcase;
  }















  /** @name 通常データのテスト時のセットアップ */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief リポジトリの検査時に用いるデータベースに、テストケースのデータを保存する。
   * 
   * @details
   * - フラグを立てることで、データベース単体のセットアップか、ファイルサーバーと連携したセットアップか、
   * 選ぶことが可能である。
   * 
   * @param[in] link_samba 「True」でファイルサーバーと連携してセットアップする。「False」でデータベースのみ。
   * 
   * @par 処理の大まかな流れ
   * -# 保存前に保存対象のテーブルを初期化するクエリを組み立てる。
   * -# ファイルサーバーとのリンクが指定されていれば、指定のファイルをファイルサーバーに保存。
   * -# ファイルサーバーに保存した後に返ってきたハッシュ値を一旦変数に保存。
   * -# 初期化データを保存するクエリを組み立てる。その際に、ファイルサーバーとのリンクが指定されていれば
   * データベースにハッシュ値を保存する。
   * -# ファイルサーバーを使用しない設定であれば、そのままテストケースの内容を保存。
   * -# 組み上げたクエリを実行する。
   * 
   * @note 特に指定はないが、ファイルサーバーへの保存時の拡張子は[wav]とする。
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  public void databaseSetup(Boolean link_samba) throws IOException{

    List<Operation> operation_list = new ArrayList<>();

    operation_list.add(FOREIGN_KEY_CHECK_0);
    operation_list.add(NORMAL_TABLE);
    operation_list.add(FOREIGN_KEY_CHECK_1);

    if(link_samba){
      this.samba_testcase.sambaReset();
    }

    for(int i = 1; i <= 20; i++){
      String sound_name = "";
      String audio_hash = "";

      if(link_samba){
        String filename = i + ".wav";
        sound_name = this.database_yml.getAudio_data().get("case_" + i);
        audio_hash = this.samba_testcase.sambaSetup(sound_name, filename);
      }else{
        sound_name = this.database_yml.getSound_name().get("case_" + i);
        audio_hash = this.database_yml.getAudio_hash().get("case_" + i);
      }

      Operation operation = Insert.into(NORMAL_TABLE_NAME)
                                  .columns("serial_num",
                                          "sound_id",
                                          "sound_name",
                                          "audio_hash")
                                  .values(i,
                                          this.database_yml.getSound_id().get("case_" + i), 
                                          sound_name, 
                                          audio_hash)
                                  .build();

      operation_list.add(operation);
    }

    this.config_factory.databaseExec(operation_list);
  }









  /** 
   **********************************************************************************************
   * @brief リポジトリの検査時に使用したデータベース内のデータを全削除してリセットする。
   * 
   * @details
   * - フラグを立てることで、データベース単体のリセットか、ファイルサーバーを含んだリセットか、
   * 選ぶことが可能である。
   * - さらに、リセットデータベースを、本番環境の物か、モックの物かを選択することも可能。
   * 
   * @param[in] link_samba 「True」でファイルサーバーを含んでリセットする。「False」でデータベースのみ。
   * 
   * @par 処理の大まかな流れ
   * -# ファイルサーバーも対象なら、先にファイルサーバーを初期化する。
   * -# 対象のデータベースのテーブルを初期化するクエリを組み立てる。
   * -# 組み上げたクエリを実行する。
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  public void databaseReset(Boolean link_samba) throws IOException{

    List<Operation> operation_list = new ArrayList<>();

    if(link_samba){
      this.samba_testcase.sambaReset();
    }

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
   * - ファイルサーバーとリンクしているデータを取得する際は、エンティティ内のハッシュ値がファイルサーバー内の
   * ファイルを計算した物になる。
   * 
   * @param[in] testcase_num 指定するテストケースの番号
   * @param[in] link_samba 指定するテストケースの番号
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
  public Audio_Data compareEntityMake(Integer testcase_num, Boolean link_samba) throws IOException{

    if(testcase_num > 20 || testcase_num < 1){
      throw new IllegalArgumentException();
    }else{
      Audio_Data mock_entity = mock(Audio_Data.class);

      when(mock_entity.getSerial_num()).thenReturn(testcase_num);
      when(mock_entity.getSound_id()).thenReturn(this.database_yml.getSound_id().get("case_" + testcase_num));
      when(mock_entity.getSound_name()).thenReturn(this.database_yml.getSound_name().get("case_" + testcase_num));

      if(link_samba){
        when(mock_entity.getAudio_hash()).thenReturn(makeHashString(testcase_num));
      }else{
        when(mock_entity.getAudio_hash()).thenReturn(this.database_yml.getAudio_hash().get("case_" + testcase_num));
      }
      
      return mock_entity;
    }
  }









  /** 
   **********************************************************************************************
   * @brief リポジトリでの更新処理の際に使用する、更新後のデータのモックエンティティを作成する。
   * 
   * @details
   * - 更新後のデータの内容としては、明らかに既存のテストケースの値とは被らないような適当な値を用いている。
   * - ただし音源番号に関しては参照制約があり、適当な値が入れれないので、若干既存のテストケースと被る。
   * - 範囲外の数字が来たときは処理を続行できないのでエラーを投げる。
   * 
   * @param[in] testcase_num 指定するテストケースの番号
   * 
   * @return データが格納されているモックエンティティ
   * 
   * @par 処理の大まかな流れ
   * -# 適当な被らない値を、作成したモックオブジェクトに格納する。
   * -# 格納が終わったモックを、戻り値とする。
   **********************************************************************************************
   */ 
  public Audio_Data UpdateAfterEntityMake(Integer testcase_num){

    if(testcase_num > 20 || testcase_num < 1){
      throw new IllegalArgumentException();
    }else{
      Audio_Data mock_entity = mock(Audio_Data.class);

      when(mock_entity.getSerial_num()).thenReturn(testcase_num);
      when(mock_entity.getSound_id()).thenReturn(this.database_yml.getSound_id().get("case_1"));
      when(mock_entity.getSound_name()).thenReturn("Blank");
      when(mock_entity.getAudio_hash()).thenReturn("Blank");

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
  public Audio_Data justInTimeEntityMake(){

    Audio_Data mock_entity = mock(Audio_Data.class);

    when(mock_entity.getSerial_num()).thenReturn(1);
    when(mock_entity.getSound_id()).thenReturn(this.database_yml.getSound_id().get("ok_length"));
    when(mock_entity.getSound_name()).thenReturn(this.database_yml.getSound_name().get("ok_length"));
    when(mock_entity.getAudio_hash()).thenReturn(this.database_yml.getAudio_hash().get("ok_length"));

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
  public List<Audio_Data> failedEntityMake() throws IOException{

    List<Audio_Data> fail_mock_list = new ArrayList<>();
    int i = 1;

    String[] sound_id = {"ng_overflow", "ng_foreign"};
    for(String key_name: sound_id){
      Audio_Data mock_entity = compareEntityMake(1, false);

      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getSound_id()).thenReturn(this.database_yml.getSound_id().get(key_name));

      fail_mock_list.add(mock_entity);
    }
    
    String[] sound_name = {"ng_overflow", "ng_null"};
    for(String key_name: sound_name){
      Audio_Data mock_entity = compareEntityMake(1, false);
            
      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getSound_name()).thenReturn(this.database_yml.getSound_name().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] audio_hash = {"ng_overflow", "ng_null"};
    for(String key_name: audio_hash){
      Audio_Data mock_entity = compareEntityMake(1, false);
            
      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getAudio_hash()).thenReturn(this.database_yml.getAudio_hash().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    return fail_mock_list;
  }

  /** @} */


















  /** @name 履歴データのテスト時のセットアップ */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief 履歴用のリポジトリの検査時に用いるデータベースに、テストケースのデータを保存する。
   * 
   * @details
   * - フラグを立てることで、データベース単体のセットアップか、ファイルサーバーと連携したセットアップか、
   * 選ぶことが可能である。
   * 
   * @param[in] link_samba 「True」でファイルサーバーと連携してセットアップする。「False」でデータベースのみ。
   * 
   * @par 処理の大まかな流れ
   * -# 保存前に保存対象のテーブルを初期化するクエリを組み立てる。
   * -# ファイルサーバーとのリンクが指定されていれば、指定のファイルをファイルサーバーに保存。
   * -# ファイルサーバーに保存した後に返ってきたハッシュ値を一旦変数に保存。
   * -# 初期化データを保存するクエリを組み立てる。その際に、ファイルサーバーとのリンクが指定されていれば
   * データベースにハッシュ値を保存する。
   * -# ファイルサーバーを使用しない設定であれば、そのままテストケースの内容を保存。
   * -# 組み上げたクエリを実行する。
   * 
   * @note 特に指定はないが、ファイルサーバーへの保存時の拡張子は[gz]とする。
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  public void historyDatabaseSetup(Boolean link_samba) throws IOException{

    List<Operation> operation_list = new ArrayList<>();

    operation_list.add(FOREIGN_KEY_CHECK_0);
    operation_list.add(HISTORY_TABLE);
    operation_list.add(FOREIGN_KEY_CHECK_1);

    if(link_samba){
      this.samba_testcase.historySambaReset();
    }

    for(int i = 1; i <= 20; i++){
      String sound_name = "";
      String audio_hash = "";

      if(link_samba){
        String filename = i + ".gz";
        sound_name = this.database_yml.getAudio_data().get("case_" + i);
        audio_hash = this.samba_testcase.historySambaSetup(sound_name, filename);
      }else{
        sound_name = this.database_yml.getSound_name().get("case_" + i);
        audio_hash = this.database_yml.getAudio_hash().get("case_" + i);
      }

      Operation operation = Insert.into(HISTORY_TABLE_NAME)
                                  .columns("history_id",
                                          "change_datetime",
                                          "change_kinds",
                                          "operation_user",
                                          "serial_num",
                                          "sound_id",
                                          "sound_name",
                                          "audio_hash")
                                  .values(i,
                                          this.hist_testcase_make.getCompareChange_datetime(i), 
                                          this.hist_testcase_make.getCompareChange_kinds(i), 
                                          this.hist_testcase_make.getCompareOperation_user(i), 
                                          i, 
                                          this.hist_testcase_make.getCompareId(i), 
                                          sound_name, 
                                          audio_hash)
                                  .build();

      operation_list.add(operation);
    }

    this.config_factory.databaseExec(operation_list);
  }










  /** 
   **********************************************************************************************
   * @brief 履歴用のリポジトリの検査時に使用したデータベース内のデータを全削除してリセットする。
   * 
   * @details
   * - フラグを立てることで、データベース単体のリセットか、ファイルサーバーを含んだリセットか、
   * 選ぶことが可能である。
   * - さらに、リセットデータベースを、本番環境の物か、モックの物かを選択することも可能。
   * 
   * @param[in] link_samba 「True」でファイルサーバーを含んでリセットする。「False」でデータベースのみ。
   * 
   * @par 処理の大まかな流れ
   * -# ファイルサーバーも対象なら、先にファイルサーバーを初期化する。
   * -# 対象のデータベースのテーブルを初期化するクエリを組み立てる。
   * -# 組み上げたクエリを実行する。
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  public void historyDatabaseReset(Boolean link_samba) throws IOException{

    List<Operation> operation_list = new ArrayList<>();

    if(link_samba){
      this.samba_testcase.historySambaReset();
    }

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
   * - ファイルサーバーとリンクしているデータを取得する際は、エンティティ内のハッシュ値がファイルサーバー内の
   * ファイルを計算した物になる。
   * 
   * @param[in] testcase_num 指定するテストケースの番号
   * @param[in] link_samba 指定するテストケースの番号
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
  public Audio_Data_History compareHistoryEntityMake(Integer testcase_num, Boolean link_samba) throws IOException{

    if(testcase_num > 20 || testcase_num < 1){
      throw new IllegalArgumentException();
    }else{
      Audio_Data_History mock_entity = mock(Audio_Data_History.class);

      when(mock_entity.getHistory_id()).thenReturn(testcase_num);
      when(mock_entity.getChange_datetime()).thenReturn(this.hist_testcase_make.getCompareChange_datetime(testcase_num));
      when(mock_entity.getChange_kinds()).thenReturn(this.hist_testcase_make.getCompareChange_kinds(testcase_num));
      when(mock_entity.getOperation_user()).thenReturn(this.hist_testcase_make.getCompareOperation_user(testcase_num));
      when(mock_entity.getSerial_num()).thenReturn(testcase_num);
      when(mock_entity.getSound_id()).thenReturn(this.hist_testcase_make.getCompareId(testcase_num));
      when(mock_entity.getSound_name()).thenReturn(this.database_yml.getSound_name().get("case_" + testcase_num));

      if(link_samba){
        when(mock_entity.getAudio_hash()).thenReturn(makeHashString(testcase_num));
      }else{
        when(mock_entity.getAudio_hash()).thenReturn(this.database_yml.getAudio_hash().get("case_" + testcase_num));
      }
      
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
  public Audio_Data_History justInTimeHistoryEntityMake(){

    Audio_Data_History mock_entity = mock(Audio_Data_History.class);

    when(mock_entity.getHistory_id()).thenReturn(1);
    when(mock_entity.getChange_datetime()).thenReturn(this.hist_testcase_make.getCompareChange_datetime(1));
    when(mock_entity.getChange_kinds()).thenReturn(this.hist_testcase_make.getCompareChange_kinds(1));
    when(mock_entity.getOperation_user()).thenReturn(this.hist_testcase_make.getCompareOperation_user(null));
    when(mock_entity.getSerial_num()).thenReturn(1);
    when(mock_entity.getSound_id()).thenReturn(this.database_yml.getSound_id().get("ok_length"));
    when(mock_entity.getSound_name()).thenReturn(this.database_yml.getSound_name().get("ok_length"));
    when(mock_entity.getAudio_hash()).thenReturn(this.database_yml.getAudio_hash().get("ok_length"));

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
  public List<Audio_Data_History> failedHistoryEntityMake() throws IOException{

    List<Audio_Data_History> fail_mock_list = new ArrayList<>();
    int i = 0;

    for(Date fail_data: this.hist_testcase_make.getFailChange_datetime()){
      Audio_Data_History mock_entity = compareHistoryEntityMake(1, false);
      
      i++;
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getChange_datetime()).thenReturn(fail_data);

      fail_mock_list.add(mock_entity);
    }

    for(String fail_data: this.hist_testcase_make.getFailChange_kinds()){
      Audio_Data_History mock_entity = compareHistoryEntityMake(1, false);
            
      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getChange_kinds()).thenReturn(fail_data);

      fail_mock_list.add(mock_entity);
    }

    for(String fail_data: this.hist_testcase_make.getFailOperation_user()){
      Audio_Data_History mock_entity = compareHistoryEntityMake(1, false);
            
      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getOperation_user()).thenReturn(fail_data);

      fail_mock_list.add(mock_entity);
    }

    String[] sound_id = {"ng_overflow"};
    for(String key_name: sound_id){
      Audio_Data_History mock_entity = compareHistoryEntityMake(1, false);
            
      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getSound_id()).thenReturn(this.database_yml.getSound_id().get(key_name));

      fail_mock_list.add(mock_entity);
    }
    
    String[] sound_name = {"ng_overflow", "ng_null"};
    for(String key_name: sound_name){
      Audio_Data_History mock_entity = compareHistoryEntityMake(1, false);
            
      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getSound_name()).thenReturn(this.database_yml.getSound_name().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] audio_hash = {"ng_overflow", "ng_null"};
    for(String key_name: audio_hash){
      Audio_Data_History mock_entity = compareHistoryEntityMake(1, false);
            
      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getAudio_hash()).thenReturn(this.database_yml.getAudio_hash().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    return fail_mock_list;
  }

  /** @} */












  /** @name 通常データと履歴データ共通で用いるメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief ファイルサーバーにセットアップしているバイナリデータのハッシュ値を作成する。指定された
   * テストケースの番号に従って、該当するファイルのハッシュ値を計算する。
   * 
   * @details
   * - 通常データと履歴データの両方で、同じバイナリデータをテストケースとして用いるため、このメソッドに
   * ハッシュ計算を集約している。
   * - 履歴データに関しては、ファイルサーバーには圧縮されて保存されているが、基本取り出す際には自動的に
   * 解凍されるようになっているため、圧縮データのハッシュ値は必要ないことから、計算は必要ない。
   * 
   * @return 計算したハッシュ値
   * 
   * @note このメソッドは、このクラス内部で用いる非公開メソッドである。
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  private String makeHashString(Integer testcase_num) throws IOException{

    String file_name = this.database_yml.getAudio_data().get("case_" + testcase_num);
    String file_path = "src/main/resources/TestCaseFile/AudioData/BlobTestCaseData/setup/" + file_name;

    try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file_path))){
      return DigestUtils.sha512_256Hex(bis);
    }
  }









  /** 
   **********************************************************************************************
   * @brief ファイルサーバーにセットアップしたバイナリデータの比較用として、保存したファイルのファイル名を
   * 返す。
   * 
   * @details
   * - ファイルサーバーの初期化データと内容が同じなので、検索メソッドテストの際の結果の比較などに幅広く用いる
   * 事が可能である。
   * - 範囲外の数字が来たときは処理を続行できないのでエラーを投げる。
   * - なお、返ってくるのはファイル名のみなので、実際のデータの取得はテストクラス側でストリームを作って、
   * そのファイル名で取得する必要がある。
   * 
   * @param[in] testcase_num 指定するテストケースの番号
   * 
   * @return 該当するファイルの名称
   **********************************************************************************************
   */ 
  public String compareSambaFileMake(Integer testcase_num){

    if(testcase_num > 20 || testcase_num < 1){
      throw new IllegalArgumentException();
    }else{
      return this.database_yml.getAudio_data().get("case_" + testcase_num);
    }
  }

  /** @} */
}
