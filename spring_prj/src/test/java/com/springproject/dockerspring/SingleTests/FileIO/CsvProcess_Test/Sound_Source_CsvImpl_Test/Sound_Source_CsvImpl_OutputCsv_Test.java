/** 
 **************************************************************************************
 * @file Sound_Source_CsvImpl_OutputCsv_Test.java
 * @brief 主に[音源管理]の保存されているデータのCSVファイル出力機能のテストを行うクラスを格納した
 * ファイル。
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、こちらは依存する
 * クラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.FileIO.CsvProcess_Test.Sound_Source_CsvImpl_Test;

import com.springproject.dockerspring.CommonTestCaseMaker.SoundSource.Sound_Source_TestCase_Make;
import com.springproject.dockerspring.SingleTests.FileIO.Component_TestUtils.Sound_Double_Component_TestUtils;
import com.springproject.dockerspring.SingleTests.FileIO.CsvProcess_Test.TestInterface.CsvProcess_OutputCsv_Test;
import com.springproject.dockerspring.CommonEnum.Environment_Config;
import com.springproject.dockerspring.FileIO.CompInterface.Sound_Source_Csv;
import com.springproject.dockerspring.FileIO.CsvProcess.Common_Csv;
import com.springproject.dockerspring.FileIO.CsvProcess.Sound_Source_CsvImpl;
import com.springproject.dockerspring.Entity.NormalEntity.Sound_Source;

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
 * @brief 主に[音源管理]の保存されているデータのCSVファイル出力機能のテストを行うクラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[Sound_Source_CsvImpl]と[Common_Csv]である。
 * - 主に、共通クラス側のCSVファイル出力機能と、集約クラス側のCSVファイル出力機能のテストを行う。
 * 
 * @see Sound_Double_Component_TestUtils
 * @see CsvProcess_OutputCsv_Test
 * @see Sound_Source_CsvImpl
 * @see Sound_Source_Csv
 * @see Common_Csv
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、
 * こちらは依存するクラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
@SpringBootTest(classes = Sound_Source_TestCase_Make.class)
public class Sound_Source_CsvImpl_OutputCsv_Test implements CsvProcess_OutputCsv_Test{

  private final Sound_Source_TestCase_Make testcase;
  private final Common_Csv common_csv;
  private final Sound_Source_Csv sound_csv;

  private final SoftAssertions softly = new SoftAssertions();


  //! データ項目の日本語名定義
  private String[] janame_order = {
    "音源番号",
    "登録日",
    "曲名",
    "作曲者",
    "演奏者",
    "出版社",
    "その他コメント"
  };

  //! データ項目の英語名定義
  private String[] enname_order = {
    "sound_id",
    "upload_date",
    "song_title",
    "composer",
    "performer",
    "publisher",
    "other_comment"
  };


  private final String target_func = "SoundSource";
  private final String target_dir = "compcsv";
  private final String prefix = "SoundSourceCsvImplOutputCsvTest";
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
   * @see Sound_Source_Csv
   **********************************************************************************************
   */
  @Autowired
  public Sound_Source_CsvImpl_OutputCsv_Test(Sound_Source_TestCase_Make testcase){
                                            
    Environment_Config config = mock(Environment_Config.class);
    when(config.getMax()).thenReturn(30);
    when(config.getOffset()).thenReturn(7);

    this.common_csv = new Common_Csv(config);
    this.sound_csv = new Sound_Source_CsvImpl(this.common_csv);
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
   * @see Sound_Double_Component_TestUtils
   * 
   * @throw IOException
   **********************************************************************************************
   */
  private void checkIdentity(File tmp_file_path, File compare_file) throws IOException{

    try(BufferedInputStream bis_1 = new BufferedInputStream(new FileInputStream(tmp_file_path));
        BufferedInputStream bis_2 = new BufferedInputStream(new FileInputStream(compare_file));){

      Sound_Double_Component_TestUtils.compareHash(bis_1, bis_2, this.softly);
    }finally{
      Sound_Double_Component_TestUtils.deleteTmpFileCheck(tmp_file_path, this.softly);
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
   * @see Sound_Double_Component_TestUtils
   * 
   * @throw IOException
   * @throw ParseException
   **********************************************************************************************
   */
  @Test
  @Override
  public void 共通クラス側のCSVデータベース保存データ出力機能の動作確認() throws IOException, ParseException{

    List<Sound_Source> comp_ent = new ArrayList<>();
    for(int i = 1; i <= 20; i++){
      Sound_Source mock_ent = this.testcase.compareEntityMake(i);
      comp_ent.add(Sound_Double_Component_TestUtils.methodMockMakeMap(mock_ent));
    }


    //検索結果マップリスト出力関数
    Function<Integer, List<Map<String, String>>> generate = (page_num) -> {
      List<Sound_Source> cut_ent = comp_ent.stream()
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
      String upload_date = map.get("upload_date");

      upload_date = upload_date == null ? null : upload_date.replace("-", "");
      map.put("upload_date", upload_date);

      return map;
    };
    
    
    File compare_file = Sound_Double_Component_TestUtils.generateTmpFile(this.target_func, 
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
   * @see Sound_Double_Component_TestUtils
   * 
   * @throw IOException
   * @throw ParseException
   **********************************************************************************************
   */
  @Test
  @Override
  public void 集約クラス側のCSVデータベース保存データ出力機能の動作確認() throws IOException, ParseException{

    List<Sound_Source> comp_ent = new ArrayList<>();
    for(int i = 1; i <= 20; i++){
      Sound_Source mock_ent = this.testcase.compareEntityMake(i);
      comp_ent.add(Sound_Double_Component_TestUtils.methodMockMakeMap(mock_ent));
    }


    //検索結果マップリスト出力関数
    Function<Integer, List<Map<String, String>>> generate = (page_num) -> {
      List<Sound_Source> cut_ent = comp_ent.stream()
                                           .skip(page_num * 7)
                                           .limit(7)
                                           .toList();
      return cut_ent.stream().map(s -> s.makeMap()).toList();
    };
    

    File compare_file = Sound_Double_Component_TestUtils.generateTmpFile(this.target_func, 
                                                                         this.target_dir, 
                                                                         this.testcase.getOutputCsv(false), 
                                                                         this.prefix, 
                                                                         this.extension, 
                                                                         true);

    File tmp_file_path = this.sound_csv.outputCsv(generate);


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
    Sound_Double_Component_TestUtils.checkTmpDirClear();
  }
}
