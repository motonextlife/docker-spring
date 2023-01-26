/** 
 **************************************************************************************
 * @file Member_Info_CsvImpl_OutputCsv_Test.java
 * @brief 主に[団員管理]の保存されているデータのCSVファイル出力機能のテストを行うクラスを格納した
 * ファイル。
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、こちらは依存する
 * クラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.FileIO.CsvProcess_Test.Member_Info_CsvImpl_Test;

import com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Member_Info_TestCase_Make;
import com.springproject.dockerspring.SingleTests.FileIO.Component_TestUtils.Member_Info_Component_TestUtils;
import com.springproject.dockerspring.SingleTests.FileIO.CsvProcess_Test.TestInterface.CsvProcess_OutputCsv_Test;
import com.springproject.dockerspring.CommonEnum.Environment_Config;
import com.springproject.dockerspring.FileIO.CompInterface.Member_Info_Csv;
import com.springproject.dockerspring.FileIO.CsvProcess.Common_Csv;
import com.springproject.dockerspring.FileIO.CsvProcess.Member_Info_CsvImpl;
import com.springproject.dockerspring.Entity.NormalEntity.Member_Info;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;











/** 
 **************************************************************************************
 * @brief 主に[団員管理]の保存されているデータのCSVファイル出力機能のテストを行うクラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[Member_Info_CsvImpl]と[Common_Csv]である。
 * - 主に、共通クラス側のCSVファイル出力機能と、集約クラス側のCSVファイル出力機能のテストを行う。
 * 
 * @see Member_Info_Component_TestUtils
 * @see CsvProcess_OutputCsv_Test
 * @see Member_Info_CsvImpl
 * @see Member_Info_Csv
 * @see Common_Csv
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、
 * こちらは依存するクラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
@SpringBootTest(classes = Member_Info_TestCase_Make.class)
public class Member_Info_CsvImpl_OutputCsv_Test implements CsvProcess_OutputCsv_Test{

  private final Member_Info_TestCase_Make testcase;
  private final Common_Csv common_csv;
  private final Member_Info_Csv member_csv;

  private final SoftAssertions softly = new SoftAssertions();


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
  private final String prefix = "MemberInfoCsvImplOutputCsvTest";
  private final String extension = "csv";









  /** 
   **********************************************************************************************
   * @brief コンストラクタインジェクションで、テストケースと検査対象クラスをDIする。
   * 
   * @details
   * - 環境変数に関しては、不定値の為、モックとしてインジェクションを行う。
   * - モックの環境変数において、[オフセット値]は[7]として設定したうえでテストを行う。
   * なおここでのオフセット値は、バッファのカウント数として用いる。
   * - モックの環境変数において、[検索結果限界数]は[30]として設定したうえでテストを行う。
   * - テスト対象クラスに関してはDIでインジェクションせず、手動でインスタンス化する際にモックの環境変数を
   * 渡すことで、モックの環境変数が有効なテスト対象クラスを作成する。
   *
   * @param[in] testcase テストケースクラス
   * 
   * @see Common_Csv
   * @see Member_Info_Csv
   **********************************************************************************************
   */
  @Autowired
  public Member_Info_CsvImpl_OutputCsv_Test(Member_Info_TestCase_Make testcase){
                                            
    Environment_Config config = mock(Environment_Config.class);
    when(config.getMax()).thenReturn(30);
    when(config.getOffset()).thenReturn(7);

    this.common_csv = new Common_Csv(config);
    this.member_csv = new Member_Info_CsvImpl(this.common_csv);
    this.testcase = testcase;
  }










  /** 
   **********************************************************************************************
   * @brief 比較用のCSVファイルと、テスト対象クラスから生成されたCSVファイルの同一性を確認する。
   * 
   * @param[in] tmp_file_path 生成されたファイル
   * @param[in] compare_file 比較用のファイル
   * 
   * @par 大まかな処理の流れ
   * -# 二つのファイルのハッシュ値を比較し、同一であることを確認する。
   * -# 確認が終わったら、テスト対象クラスから生成されたファイルは削除する。
   * 
   * @see Member_Info_Component_TestUtils
   * 
   * @throw IOException
   **********************************************************************************************
   */
  private void checkIdentity(File tmp_file_path, File compare_file) throws IOException{

    try(BufferedInputStream bis_1 = new BufferedInputStream(new FileInputStream(tmp_file_path));
        BufferedInputStream bis_2 = new BufferedInputStream(new FileInputStream(compare_file));){

      Member_Info_Component_TestUtils.compareHash(bis_1, bis_2, this.softly);
    }finally{
      Member_Info_Component_TestUtils.deleteTmpFileCheck(tmp_file_path, this.softly);
    }
  }













  /** 
   **********************************************************************************************
   * @brief 共通クラス側のCSVデータベース保存データ出力用の共通メソッドの検査を行う。
   * 
   * @details
   * - 検査方法としては、比較用のCSVファイルと、テスト対象クラスから生成された一時ファイルを
   * ハッシュ化して比較し、データが同一であることを確認する。
   *
   * @par 大まかな処理の流れ
   * -# テストケースとして投入するデータをテストケースクラスから取得する。
   * -# 検査対象クラス内で、データベースから検索結果に応じてデータを取り出す関数型インターフェースを
   * 定義する。このモックでは、テストケースのエンティティリストを、指定されたページ数に応じて切り分けて
   * 切り分けたデータのリストを返却する。
   * -# 検査対象クラスに投入する列挙型マップリストを作成。
   * -# 出力対象のデータのうち、一部変換が必要な値を変換する関数型インターフェースを定義する。
   * ここでは、日付型文字列からハイフンを抜き出す処理を記述する。
   * -# 検査対象クラスに、一連の関数型インターフェースを投入しデータ生成を行う。
   * -# 戻り値の一時ファイルと、比較用のファイルをハッシュ化して同一性を検査するアサーションを実行。
   * -# エラーの有無にかかわらず、テストが終了したら一時ファイルを全削除する。
   * 
   * @see Member_Info_Component_TestUtils
   * 
   * @throw IOException
   * @throw ParseException
   **********************************************************************************************
   */
  @Test
  @Override
  public void 共通クラス側のCSVデータベース保存データ出力機能の動作確認() throws IOException, ParseException{

    List<Member_Info> comp_ent = new ArrayList<>();
    for(int i = 1; i <= 20; i++){
      Member_Info mock_ent = this.testcase.compareEntityMake(i);
      comp_ent.add(Member_Info_Component_TestUtils.methodMockMakeMap(mock_ent));
    }


    //検索結果マップリスト出力関数
    Function<Integer, List<Map<String, String>>> generate = (page_num) -> {
      List<Member_Info> cut_ent = comp_ent.stream()
                                          .skip(page_num * 7)
                                          .limit(7)
                                          .toList();
      return cut_ent.stream().map(s -> s.makeMap()).toList();
    };


    //列挙型マップリスト作成
    LinkedHashMap<String, String> enummap = new LinkedHashMap<>();
    for(int i = 0; i < janame_order.length; i++){
      enummap.put(janame_order[i], enname_order[i]);
    }


    //一部データ変換関数
    UnaryOperator<Map<String, String>> func = map -> {
      map.remove("face_photo");

      String birthday = map.get("birthday");
      String join_date = map.get("join_date");
      String ret_date = map.get("ret_date");
      String pos_arri_date = map.get("position_arri_date");
      String assign_date = map.get("assign_date");

      birthday = birthday == null ? null : birthday.replace("-", "");
      join_date = join_date == null ? null : join_date.replace("-", "");
      ret_date = ret_date == null ? null : ret_date.replace("-", "");
      pos_arri_date = pos_arri_date == null ? null : pos_arri_date.replace("-", "");
      assign_date = assign_date == null ? null : assign_date.replace("-", "");
      map.put("birthday", birthday);
      map.put("join_date", join_date);
      map.put("ret_date", ret_date);
      map.put("position_arri_date", pos_arri_date);
      map.put("assign_date", assign_date);

      String sex = map.get("sex");

      if(sex == null){
        map.put("sex", "該当なし");
      }else if(sex.equals("male")){
        map.put("sex", "男");
      }else if(sex.equals("female")){
        map.put("sex", "女");
      }else{
        map.put("sex", "該当なし");
      }

      return map;
    };
    
    
    File compare_file = Member_Info_Component_TestUtils.generateTmpFile(this.target_func, 
                                                                        this.target_dir, 
                                                                        this.testcase.getOutputCsv(false), 
                                                                        this.prefix, 
                                                                        this.extension, 
                                                                        true);

    File tmp_file_path = this.common_csv.outputCsvCom(generate, enummap, func);


    checkIdentity(tmp_file_path, compare_file);
    this.softly.assertAll();
  }











  /** 
   **********************************************************************************************
   * @brief 集約クラス側のCSVデータベース保存データ出力用のメソッドの検査を行う。
   * 
   * @details
   * - 検査方法としては、比較用のCSVファイルと、テスト対象クラスから生成された一時ファイルを
   * ハッシュ化して比較し、データが同一であることを確認する。
   *
   * @par 大まかな処理の流れ
   * -# テストケースとして投入するデータをテストケースクラスから取得する。
   * -# 検査対象クラス内で、データベースから検索結果に応じてデータを取り出す関数型インターフェースを
   * 定義する。このモックでは、テストケースのエンティティリストを、指定されたページ数に応じて切り分けて
   * 切り分けたデータのリストを返却する。
   * -# 検査対象クラスに、一連の関数型インターフェースを投入しデータ生成を行う。
   * -# 戻り値の一時ファイルと、比較用のファイルをハッシュ化して同一性を検査するアサーションを実行。
   * -# エラーの有無にかかわらず、テストが終了したら一時ファイルを全削除する。
   * 
   * @see Member_Info_Component_TestUtils
   * 
   * @throw IOException
   * @throw ParseException
   **********************************************************************************************
   */
  @Test
  @Override
  public void 集約クラス側のCSVデータベース保存データ出力機能の動作確認() throws IOException, ParseException{

    List<Member_Info> comp_ent = new ArrayList<>();
    for(int i = 1; i <= 20; i++){
      Member_Info mock_ent = this.testcase.compareEntityMake(i);
      comp_ent.add(Member_Info_Component_TestUtils.methodMockMakeMap(mock_ent));
    }


    //検索結果マップリスト出力関数
    Function<Integer, List<Map<String, String>>> generate = (page_num) -> {
      List<Member_Info> cut_ent = comp_ent.stream()
                                          .skip(page_num * 7)
                                          .limit(7)
                                          .toList();
      return cut_ent.stream().map(s -> s.makeMap()).toList();
    };
    

    File compare_file = Member_Info_Component_TestUtils.generateTmpFile(this.target_func, 
                                                                        this.target_dir, 
                                                                        this.testcase.getOutputCsv(false), 
                                                                        this.prefix, 
                                                                        this.extension, 
                                                                        true);

    File tmp_file_path = this.member_csv.outputCsv(generate);


    checkIdentity(tmp_file_path, compare_file);
    this.softly.assertAll();
  }









  /** 
   **********************************************************************************************
   * @brief テスト終了後に、一時ファイル用のディレクトリが空っぽであることを確認する。
   * 
   * @throw InterruptedException
   **********************************************************************************************
   */
  @AfterAll
  public static void 一時ファイルディレクトリの初期化確認() throws InterruptedException{
    Member_Info_Component_TestUtils.checkTmpDirClear();
  }
}
