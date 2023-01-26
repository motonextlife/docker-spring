/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.JoinTests.FileIO.CsvProcess_Test.Member_Info_CsvImpl_Test
 * 
 * @brief CSVファイルの入出力結合テストのうち、[団員情報]に関するテストを行うクラスを格納した
 * パッケージ
 * 
 * @details
 * - このパッケージは、団員情報に関するCSVファイルの処理機能をテストするクラスを格納している。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.JoinTests.FileIO.CsvProcess_Test.Member_Info_CsvImpl_Test;





/** 
 **************************************************************************************
 * @file Member_Info_CsvImpl_CsvToDatabase_Test.java
 * @brief 主に[団員管理]のCSVデータのデータベースへの格納機能のテストを行うクラスを格納した
 * ファイル。
 * 
 * @note ここと同名のパッケージが、パッケージ[SingleTests]に存在するが、こちらは依存する
 * クラスを、モックを使わずそのまま使ってテストした、結合テストである。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Member_Info_TestCase_Make;
import com.springproject.dockerspring.JoinTests.FileIO.Component_TestUtils.Member_Info_Component_TestUtils;
import com.springproject.dockerspring.JoinTests.FileIO.CsvProcess_Test.TestInterface.CsvProcess_CsvToDatabase_Test;
import com.springproject.dockerspring.CommonEnum.Environment_Config;
import com.springproject.dockerspring.FileIO.CompInterface.Member_Info_Csv;
import com.springproject.dockerspring.FileIO.CsvProcess.Common_Csv;
import com.springproject.dockerspring.FileIO.CsvProcess.Member_Info_CsvImpl;
import com.springproject.dockerspring.Entity.NormalEntity.Member_Info;
import com.springproject.dockerspring.Repository.NormalRepo.Member_Info_Repo;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVFormat.Builder;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.Import;










/** 
 **************************************************************************************
 * @brief 主に[団員管理]のCSVデータのデータベースへの格納機能のテストを行うクラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[Member_Info_CsvImpl]と[Common_Csv]である。
 * - 主に、共通クラス側のCSVデータベース格納機能と、集約クラス側のCSVデータベース格納機能のて
 * テストを行う。
 * 
 * @see Member_Info_Component_TestUtils
 * @see CsvProcess_CsvToDatabase_Test
 * @see Member_Info_Csv
 * @see Member_Info_CsvImpl
 * @see Common_Csv
 * @see Member_Info_Repo
 * 
 * @note ここと同名のパッケージが、パッケージ[SingleTests]に存在するが、こちらは
 * 依存するクラスを、モックを使わずそのまま使ってテストした、結合テストである。
 **************************************************************************************
 */ 
@Import(Member_Info_TestCase_Make.class)
@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class Member_Info_CsvImpl_CsvToDatabase_Test implements CsvProcess_CsvToDatabase_Test{

  private final Member_Info_TestCase_Make testcase;
  private static Member_Info_TestCase_Make testcase_static;
  private final Common_Csv common_csv;
  private final Member_Info_Csv member_csv;
  private final Member_Info_Repo member_repo;

  private final SoftAssertions softly = new SoftAssertions();
  

  //! CSV抽出の際のヘッダー定義
  private Builder builder = CSVFormat.DEFAULT
                                     .builder()
                                     .setHeader("団員番号",
                                                "名前",
                                                "ふりがな",
                                                "性別",
                                                "誕生日",
                                                "入団日",
                                                "退団日",
                                                "メールアドレス１",
                                                "メールアドレス２",
                                                "電話番号１",
                                                "電話番号２",
                                                "現住所郵便番号",
                                                "現住所",
                                                "役職名",
                                                "現役職着任日",
                                                "職種名",
                                                "配属部署",
                                                "配属日",
                                                "担当楽器",
                                                "その他コメント");
                                           
  //! データ項目の日本語名定義
  private String[] janame_order = {
    "団員番号",
    "名前",
    "ふりがな",
    "性別",
    "誕生日",
    "入団日",
    "退団日",
    "メールアドレス１",
    "メールアドレス２",
    "電話番号１",
    "電話番号２",
    "現住所郵便番号",
    "現住所",
    "役職名",
    "現役職着任日",
    "職種名",
    "配属部署",
    "配属日",
    "担当楽器",
    "その他コメント"
  };

  //! データ項目の英語名定義
  private String[] enname_order = {
    "member_id",
    "name",
    "name_pronu",
    "sex",
    "birthday",
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
    "other_comment"
  };


  private final String target_func = "MemberInfo";
  private final String target_dir = "compcsv";
  private final String prefix = "MemberInfoCsvImplCsvToDatabaseTest";
  private final String extension = "csv";











  /** 
   **********************************************************************************************
   * @brief コンストラクタインジェクションで、テストケースをDIする。
   * 
   * @details
   * - 環境変数に関しては、不定値の為、モックとしてインジェクションを行う。
   * - モックの環境変数において、[オフセット値]は[8]として設定したうえでテストを行う。
   * - テスト対象クラスに関してはDIでインジェクションせず、手動でインスタンス化する際にモックの環境変数を
   * 渡すことで、モックの環境変数が有効なテスト対象クラスを作成する。
   *
   * @param[in] testcase テストケースクラス
   * 
   * @see Common_Csv
   * @see Member_Info_CsvImpl
   * @see Member_Info_Repo
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Autowired
  public Member_Info_CsvImpl_CsvToDatabase_Test(Member_Info_TestCase_Make testcase, 
                                                Member_Info_Repo member_repo) throws IOException{
                                            
    Environment_Config config = mock(Environment_Config.class);
    when(config.getOffset()).thenReturn(8);

    this.common_csv = new Common_Csv(config);
    this.member_csv = new Member_Info_CsvImpl(this.common_csv);
    this.testcase = testcase;
    this.member_repo = member_repo;

    this.testcase.databaseReset();
  }











  /** 
   **********************************************************************************************
   * @brief DIした環境変数を、静的変数でも使えるようにする。
   *
   * @details
   * - 設けた理由としては、テスト後のデータベースのリセット確認の際に環境変数を用いる必要があるが、AfterAll
   * は静的メソッドでしか実装できない為、通常のフィールド変数にDIされている環境変数が使えない。
   * - そのため、新たに静的フィールド変数を作成して、その中にDIした環境変数の参照を入れておくことで、テスト後の
   * データベースリセット確認にも環境変数を使用できるようにしている。
   * 
   * @par 使用アノテーション
   * - @PostConstruct
   * 
   * @note このメソッドは、セットアップのための非公開メソッドであり、外部に出さない。
   **********************************************************************************************
   */
  @PostConstruct
  private void configStaticSet(){
    Member_Info_CsvImpl_CsvToDatabase_Test.testcase_static = this.testcase;
  }












  /** 
   **********************************************************************************************
   * @brief 保存終了後にデータベースから取得してきたデータと、比較用のエンティティリストを比較して、想定
   * 通りのデータが格納されていることを確認する。
   * 
   * @param[in] compare 比較用のエンティティリスト
   * @param[in] result データベースから取得したエンティティリスト
   * 
   * @par 大まかな処理の流れ
   * -# データベースから格納したデータを取得し、比較用のエンティティの数量と、取得してきたデータの終了が
   * 同一であることを確認する。
   * -# 比較用エンティティとテスト対象エンティティを一つづつ比較していく際に、管理番号が同一であれば、
   * 比較アサーションを実行し、同一であることを確認する。
   * -# 一つのエンティティの確認が終わったら、テスト対象エンティティをリストから一つづつ削除していく。
   * -# 比較終了後に、テスト対象エンティティが一つも残っておらず全てチェックしたことを確認する。
   * 
   * @see Member_Info_Component_TestUtils
   **********************************************************************************************
   */
  private void checkIdentity(List<Member_Info> compare){

    List<Member_Info> result = (List<Member_Info>)this.member_repo.findAll();
    this.softly.assertThat(result.size()).isEqualTo(compare.size());

    for(Member_Info comp_ent: compare){
      for(Member_Info result_ent: result){
        
        if(comp_ent.getMember_id().equals(result_ent.getMember_id())){
          Member_Info_Component_TestUtils.assertAllEquals(comp_ent, result_ent, this.softly);
          result.remove(result_ent);
          break;
        }
      }
    }

    this.softly.assertThat(result).isEmpty();
  }














  /** 
   **********************************************************************************************
   * @brief 共通クラス側のCSVファイルデータベース保存用の共通メソッドの検査を行う。
   * 
   * @details
   * - 検査対象には、モックとなる関数型インターフェースを作成して、それを渡し実行する。
   * - テストケースとなるCSVファイルを渡して処理をした際に、データベース格納関数となる関数型インターフェースに
   * 渡されたエンティティのデータが、比較用のエンティティと同一であることで、正常にデータが渡ったことを判別。
   *
   * @par 大まかな処理の流れ
   * -# テストケースとして検査対象クラスに投入するCSVファイルと、比較用エンティティのリストをテストケース
   * クラスから取得する。
   * -# モックとなる関数型インターフェースとして、[マップデータのエンティティ格納]の関数を準備する。
   * 内容としては、渡されたマップリストのデータを、エンティティに格納してリストとして返すものである。
   * -# 次にモックの関数型インターフェースとして、[エンティティのデータベース格納]の関数を用意する。
   * 格納対象となるエンティティを受け取り、その関数の中で、対象のデータベースの中にデータを格納する。
   * -# テスト対象クラスに渡す、列挙型のマップリストと、投入するCSVファイルをコピーした物を作成し、
   * これまで作った関数型インターフェースなどと一緒に全て検査対象クラスに渡す。
   * -# 格納対象のデータベースからデータを取り出し、比較用エンティティと比較することで想定通りのデータが
   * 格納されていることを確認する。なお、新規追加扱いの事から、データの順番が保証されていない為、
   * 「データベース内部に同じデータが存在すれば良い」物とし、順番は問わない。
   * 
   * @see Member_Info_Component_TestUtils
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Test
  @Override
  public void 共通クラス側のCSV抽出データのデータベース格納機能の動作確認() throws IOException{

    List<Member_Info> comp_ent = new ArrayList<>();
    for(int i = 1; i <= 20; i++){
      comp_ent.add(this.testcase.compareEntityMake(i));
    }


    //エンティティ格納関数
    Function<List<Map<String, String>>, List<Member_Info>> goto_ent = s -> {
      return s.stream().map(map -> {
                         try {
                           return new Member_Info(map);
                         } catch (ParseException e) {
                           throw new IllegalStateException();
                         }
                       })
                       .toList();
    };


    //データベース格納関数
    Consumer<List<Member_Info>> goto_database = s -> {
      this.member_repo.saveAll(s);
    };


    //列挙型マップリスト作成
    LinkedHashMap<String, String> enum_map = new LinkedHashMap<>();
    for(int i = 0; i < janame_order.length; i++){
      enum_map.put(janame_order[i], enname_order[i]);
    }


    //テストケースCSVファイルのコピー作成
    File temp_file = Member_Info_Component_TestUtils.generateTmpFile(this.target_func, 
                                                                     this.target_dir, 
                                                                     this.testcase.getOutputCsv(false), 
                                                                     this.prefix, 
                                                                     this.extension, 
                                                                     false);

    this.common_csv.csvToDatabaseCom(temp_file, enum_map, this.builder, goto_ent, goto_database);

    checkIdentity(comp_ent);
    this.softly.assertAll();
  }












  /** 
   **********************************************************************************************
   * @brief 集約クラス側のCSVファイルデータベース保存用のメソッドの検査を行う。
   * 
   * @details
   * - 検査対象には、モックとなる関数型インターフェースを作成して、それを渡し実行する。
   * - テストケースとなるCSVファイルを渡して処理をした際に、データベース格納関数となる関数型インターフェースに
   * 渡されたエンティティのデータが、比較用のエンティティと同一であることで、正常にデータが渡ったことを判別。
   *
   * @par 大まかな処理の流れ
   * -# テストケースとして検査対象クラスに投入するCSVファイルと、比較用エンティティのリストをテストケース
   * クラスから取得する。
   * -# モックの関数型インターフェースとして、[エンティティのデータベース格納]の関数を用意する。
   * 格納対象となるエンティティを受け取り、その関数の中で、対象のデータベースの中にデータを格納する。
   * -# テスト対象クラスに渡す、列挙型のマップリストと、投入するCSVファイルをコピーした物を作成し、
   * これまで作った関数型インターフェースなどと一緒に全て検査対象クラスに渡す。
   * -# 格納対象のデータベースからデータを取り出し、比較用エンティティと比較することで想定通りのデータが
   * 格納されていることを確認する。なお、新規追加扱いの事から、データの順番が保証されていない為、
   * 「データベース内部に同じデータが存在すれば良い」物とし、順番は問わない。
   * 
   * @see Member_Info_Component_TestUtils
   * 
   * @throw IOException
   * @throw ParseException
   **********************************************************************************************
   */
  @Test
  @Override
  public void 集約クラス側のCSV抽出データのデータベース格納機能の動作確認() throws IOException, ParseException{

    List<Member_Info> comp_ent = new ArrayList<>();
    for(int i = 1; i <= 20; i++){
      comp_ent.add(this.testcase.compareEntityMake(i));
    }


    //データベース格納関数
    Consumer<List<Member_Info>> goto_database = s -> {
      this.member_repo.saveAll(s);
    };


    //テストケースCSVファイルのコピー作成
    File temp_file = Member_Info_Component_TestUtils.generateTmpFile(this.target_func, 
                                                                     this.target_dir, 
                                                                     this.testcase.getOutputCsv(false), 
                                                                     this.prefix, 
                                                                     this.extension, 
                                                                     false);

    this.member_csv.csvToDatabase(temp_file, goto_database);
    
    checkIdentity(comp_ent);
    this.softly.assertAll();
  }




  





  /** 
   **********************************************************************************************
   * @brief テスト終了後に、テストケースとして利用したデータを、データベースから削除する。
   * 
   * @throw IOException
   * @throw InterruptedException
   **********************************************************************************************
   */
  @AfterAll
  public static void testcaseReset() throws IOException, InterruptedException{
    Member_Info_Component_TestUtils.checkTmpDirClear();
    Member_Info_CsvImpl_CsvToDatabase_Test.testcase_static.databaseReset();
  }
}
