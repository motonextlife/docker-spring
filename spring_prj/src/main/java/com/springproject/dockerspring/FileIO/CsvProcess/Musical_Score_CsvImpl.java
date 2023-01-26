/** 
 **************************************************************************************
 * @file Musical_Score_CsvImpl.java
 * @brief 主に[楽譜管理]機能で使用する、CSVファイルの入出力に関する機能を実装するクラスを
 * 格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.FileIO.CsvProcess;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVFormat.Builder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;

import com.springproject.dockerspring.CommonEnum.NormalEnum.Musical_Score_Enum;
import com.springproject.dockerspring.FileIO.CompInterface.Musical_Score_Csv;
import com.springproject.dockerspring.FileIO.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.Entity.NormalEntity.Musical_Score;

import lombok.RequiredArgsConstructor;









/** 
 **************************************************************************************
 * @brief 主に[楽譜管理]機能で使用する、CSVファイルの入出力に関する機能を実装するクラス
 * 
 * @details 
 * - CSVのヘッダー行の並び順や出力対象の値は、このクラス内の配列で自由に指定することが可能。
 * - 処理の大部分は、このクラスにDIする共通処理クラスに任せることで、こちらの特定クラスの
 * 実装内容を大幅に減らしている。
 * - CSVを操作するライブラリに関しては、[Apache Commons Csv]を用いる。これによって、複雑な
 * 条件のCSVファイルに対しても読取や書き込みが容易になる。
 * - このクラスで用いる共通クラスのインジェクションは、Lombokによりコンストラクタインジェクションを
 * 行うことで実現する。
 * 
 * @par 使用アノテーション
 * - @Component
 * - @RequiredArgsConstructor(onConstructor = @__(@Autowired))
 * 
 * @see Musical_Score_Csv
 * @see Common_Csv
 * @see Musical_Score
 **************************************************************************************
 */ 
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Musical_Score_CsvImpl implements Musical_Score_Csv{


  private final Common_Csv common_csv;


  //! ヘッダー行の定義値列挙型と順番の指定
  private final Musical_Score_Enum[] enum_list = {
    Musical_Score_Enum.Score_Id,
    Musical_Score_Enum.Buy_Date,
    Musical_Score_Enum.Song_Title,
    Musical_Score_Enum.Composer,
    Musical_Score_Enum.Arranger,
    Musical_Score_Enum.Publisher,
    Musical_Score_Enum.Storage_Loc,
    Musical_Score_Enum.Disp_Date,
    Musical_Score_Enum.Other_Comment
  };


  //! Apache Commons Csvによる、ヘッダー行のマッピング指定
  private Builder builder = CSVFormat.DEFAULT
                                     .builder()
                                     .setHeader(Musical_Score_Enum.Score_Id.getJaName(),
                                                Musical_Score_Enum.Buy_Date.getJaName(),
                                                Musical_Score_Enum.Song_Title.getJaName(),
                                                Musical_Score_Enum.Composer.getJaName(),
                                                Musical_Score_Enum.Arranger.getJaName(),
                                                Musical_Score_Enum.Publisher.getJaName(),
                                                Musical_Score_Enum.Storage_Loc.getJaName(),
                                                Musical_Score_Enum.Disp_Date.getJaName(),
                                                Musical_Score_Enum.Other_Comment.getJaName());











  /** 
   **********************************************************************************************
   * @brief 読み込んだCSVファイルからデータを抽出し、一時ファイルに再度CSVを格納する。
   * 再格納した一時ファイルのデータは、このシステムの全域で用いる。
   * 
   * @details
   * - この処理での大部分は、共通クラスに処理を任せる。
   * - このメソッド内では、共通クラスの処理に渡すヘッダー行の列挙型のマップリストを作成するのみである。
   * - なお、共通クラスに渡すマップリストに関しては、順番を保証する必要がある。
   *
   * @return 生成したCSV一時ファイル 
   *
   * @param[in] csv_file 抽出対象となるCSVファイル
   * 
   * @note このメソッドで使用する共通クラスの共通メソッドは[extractCsvCom]である。詳しい処理内容は
   * そちらを参照の事。
   *
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  @Override
  public File extractCsv(MultipartFile csv_file) throws IOException, InputFileDifferException {

    try{
      LinkedHashMap<String, String> enum_map = Arrays.stream(this.enum_list)
                                                     .collect(Collectors.toMap(Musical_Score_Enum::getJaName,
                                                                               Musical_Score_Enum::getEnName,
                                                                               (s, a) -> s, 
                                                                               LinkedHashMap::new));

      return this.common_csv.extractCsvCom(csv_file, enum_map);

    }catch(IOException e){
      throw new IOException("Error location [Musical_Score_Csv:extractCsv]" + "\n" + e);
    }
  }










  /** 
   **********************************************************************************************
   * @brief バリデーションが終わったCSVファイルのデータを、データベースに格納する。
   * 
   * @details
   * - この処理での大部分は、共通クラスに処理を任せる。
   * - このメソッド内では、共通クラスの処理に渡すヘッダー行の列挙型のマップリストと、共通クラス側で用いる
   * エンティティへのデータの関数型インターフェースの格納関数を作成するのみである。
   * - データベースに格納する処理は、サービスから関数型インターフェースで処理を受け取り、その中で行う。
   * これによって、データベース処理をこのクラスから分離し、共通クラスの汎用性を増すことが出来る。
   * なお、この特定クラスでは使用せず、そのまま共通クラスへ渡す。
   * 
   * @param[in] tmp_csv_file データベース格納対象となるCSVファイル
   * @param[in] goto_database データをデータベースへ格納する処理の関数
   * 
   * @par エンティティ格納関数の大まかな処理の流れ
   * -# データベースから取得したデータをマップリスト化した物を引数として渡す。
   * -# このクラスに対応するエンティティにデータを格納し、そのエンティティのリストを戻り値とする。
   * -# この処理の関数を、そっくりそのまま共通クラスへ渡し、そちらで処理を実行する。
   * 
   * @see Musical_Score
   * 
   * @note このメソッドで使用する共通クラスの共通メソッドは[csvToDatabaseCom]である。詳しい処理内容は
   * そちらを参照の事。
   *
   * @throw ParseException
   * @throw IOException
   **********************************************************************************************
   */
  @Override
  public void csvToDatabase(File tmp_csv_file, Consumer<List<Musical_Score>> goto_database) throws ParseException, IOException {

    try{
      LinkedHashMap<String, String> enum_map = Arrays.stream(this.enum_list)
                                                     .collect(Collectors.toMap(Musical_Score_Enum::getJaName,
                                                                               Musical_Score_Enum::getEnName,
                                                                               (s, a) -> s, 
                                                                               LinkedHashMap::new));

      Function<List<Map<String, String>>, List<Musical_Score>> goto_ent = s -> {
        List<Musical_Score> list = new ArrayList<>();
        s.parallelStream().map(map -> {
                            try {
                              return new Musical_Score(map);
                            } catch (ParseException e) {
                              throw new IllegalStateException();
                            }
                          })
                          .forEachOrdered(ent -> list.add(ent));
        return list;
      };

      this.common_csv.csvToDatabaseCom(tmp_csv_file, enum_map, this.builder, goto_ent, goto_database);

    }catch (IllegalStateException e) {
      throw new ParseException("Error location [Musical_Score_Csv:csvToEntitys(ParseException)]" + "\n" + e, 0);
    } catch (IOException e) {
      throw new IOException("Error location [Musical_Score_Csv:csvToEntitys(IOException)]" + "\n" + e);
    }
  }










  /** 
   **********************************************************************************************
   * @brief データベースに保存してあるデータを、CSVファイルとして出力する。
   * 
   * @details
   * - この処理での大部分は、共通クラスに処理を任せる。
   * - このメソッド内では、共通クラスの処理に渡すヘッダー行の列挙型のマップリストと、共通クラス側で用いる
   * 必要に応じた値の変換関数を作成するのみである。
   * - データベースから出力する処理は、サービスから関数型インターフェースで処理を受け取り、その中で行う。
   * これによって、データベース処理をこのクラスから分離し、共通クラスの汎用性を増すことが出来る。
   * 
   * @return 生成したCSVの一時ファイル
   * 
   * @param[in] database_generate データベースから出力対象のデータを出力する処理の関数
   * 
   * @par 変換関数の大まかな処理の流れ
   * -# ここで作成する変換関数の用途は、日付形式の文字列からハイフンを抜き取る為である。
   * つまり[yyyy-MM-dd]の形式から[yyyyMMdd]の形式に変換してCSVに出力するためである。
   * -# この関数に引数として渡されたマップリストから、変換対象である日付データを取り出す。
   * -# 取り出した日付データを変換して、再度引数のマップリストに上書きする。
   * -# 変換が済んだマップリストを戻り値とする。
   * -# この処理の関数を、そっくりそのまま共通クラスへ渡し、そちらで処理を実行する。
   * 
   * @note このメソッドで使用する共通クラスの共通メソッドは[outputCsvCom]である。詳しい処理内容は
   * そちらを参照の事。
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Override
  public File outputCsv(Function<Integer, List<Map<String, String>>> database_generate) throws IOException {

    try{
      LinkedHashMap<String, String> enum_map = Arrays.stream(this.enum_list)
                                                     .collect(Collectors.toMap(Musical_Score_Enum::getJaName,
                                                                               Musical_Score_Enum::getEnName,
                                                                               (s, a) -> s, 
                                                                               LinkedHashMap::new));

      UnaryOperator<Map<String, String>> func = map -> {
        String buy_date_key = Musical_Score_Enum.Buy_Date.getEnName();
        String disp_date_key = Musical_Score_Enum.Disp_Date.getEnName();
        String buy_date = map.get(buy_date_key);
        String disp_date = map.get(disp_date_key);

        buy_date = buy_date == null ? null : buy_date.replace("-", "");
        disp_date = buy_date == null ? null : disp_date.replace("-", "");
        map.put(buy_date_key, buy_date);
        map.put(disp_date_key, disp_date);

        return map;
      };
      
      return this.common_csv.outputCsvCom(database_generate, enum_map, func);

    }catch(IOException e){
      throw new IOException("Error location [Musical_Score_Csv:outputCsv(IOException)]" + "\n" + e);
    }
  }









  /** 
   **********************************************************************************************
   * @brief このシステムにCSVファイルを投入する際に便利な、CSVファイルのテンプレートを出力する。
   * 
   * @details
   * - この処理での大部分は、共通クラスに処理を任せる。
   * - このメソッド内では、共通クラスの処理に渡す出力対象のヘッダー行のリストを作成するだけである。
   * 
   * @return 生成したCSVの一時ファイル
   * 
   * @note このメソッドで使用する共通クラスの共通メソッドは[outputTemplateCom]である。詳しい処理内容は
   * そちらを参照の事。
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Override
  public File outputTemplate() throws IOException {

    try{
      List<String> janame_list = Arrays.stream(this.enum_list)
                                       .map(s -> s.getJaName())
                                       .toList();

      return this.common_csv.outputTemplateCom(janame_list);

    }catch(IOException e){
      throw new IOException("Error location [Musical_Score_Csv:outputTemplate]" + "\n" + e);
    }
  }
}
