/** 
 **************************************************************************************
 * @file Member_Info_CsvImpl.java
 * @brief 主に[団員管理]機能で使用する、CSVファイルの入出力に関する機能を実装するクラスを
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

import com.springproject.dockerspring.CommonEnum.NormalEnum.Member_Info_Enum;
import com.springproject.dockerspring.FileIO.CompInterface.Member_Info_Csv;
import com.springproject.dockerspring.FileIO.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.Entity.NormalEntity.Member_Info;

import lombok.RequiredArgsConstructor;








/** 
 **************************************************************************************
 * @brief 主に[団員管理]機能で使用する、CSVファイルの入出力に関する機能を実装するクラス
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
 * @see Member_Info_Csv
 * @see Common_Csv
 * @see Member_Info
 **************************************************************************************
 */ 
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Member_Info_CsvImpl implements Member_Info_Csv{

  private final Common_Csv common_csv;


  //! ヘッダー行の定義値列挙型と順番の指定
  private final Member_Info_Enum[] enum_list = {
    Member_Info_Enum.Member_Id,
    Member_Info_Enum.Name,
    Member_Info_Enum.Name_Pronu,
    Member_Info_Enum.Sex,
    Member_Info_Enum.Birthday,
    Member_Info_Enum.Join_Date,
    Member_Info_Enum.Ret_Date,
    Member_Info_Enum.Email_1,
    Member_Info_Enum.Email_2,
    Member_Info_Enum.Tel_1,
    Member_Info_Enum.Tel_2,
    Member_Info_Enum.Addr_Postcode,
    Member_Info_Enum.Addr,
    Member_Info_Enum.Position,
    Member_Info_Enum.Position_Arri_Date,
    Member_Info_Enum.Job,
    Member_Info_Enum.Assign_Dept,
    Member_Info_Enum.Assign_Date,
    Member_Info_Enum.Inst_Charge,
    Member_Info_Enum.Other_Comment
  };


  //! Apache Commons Csvによる、ヘッダー行のマッピング指定
  private Builder builder = CSVFormat.DEFAULT
                                     .builder()
                                     .setHeader(Member_Info_Enum.Member_Id.getJaName(),
                                                Member_Info_Enum.Name.getJaName(),
                                                Member_Info_Enum.Name_Pronu.getJaName(),
                                                Member_Info_Enum.Sex.getJaName(),
                                                Member_Info_Enum.Birthday.getJaName(),
                                                Member_Info_Enum.Join_Date.getJaName(),
                                                Member_Info_Enum.Ret_Date.getJaName(),
                                                Member_Info_Enum.Email_1.getJaName(),
                                                Member_Info_Enum.Email_2.getJaName(),
                                                Member_Info_Enum.Tel_1.getJaName(),
                                                Member_Info_Enum.Tel_2.getJaName(),
                                                Member_Info_Enum.Addr_Postcode.getJaName(),
                                                Member_Info_Enum.Addr.getJaName(),
                                                Member_Info_Enum.Position.getJaName(),
                                                Member_Info_Enum.Position_Arri_Date.getJaName(),
                                                Member_Info_Enum.Job.getJaName(),
                                                Member_Info_Enum.Assign_Dept.getJaName(),
                                                Member_Info_Enum.Assign_Date.getJaName(),
                                                Member_Info_Enum.Inst_Charge.getJaName(),
                                                Member_Info_Enum.Other_Comment.getJaName());


                                                



                                                





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
      //マップリストの順番は保証する必要あり。
      LinkedHashMap<String, String> enum_map = Arrays.stream(this.enum_list)
                                                     .collect(Collectors.toMap(Member_Info_Enum::getJaName,
                                                                               Member_Info_Enum::getEnName,
                                                                               (s, a) -> s, 
                                                                               LinkedHashMap::new));

      return this.common_csv.extractCsvCom(csv_file, enum_map);

    }catch(IOException e){
      throw new IOException("Error location [Member_Info_Csv:extractCsv]" + "\n" + e);
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
   * @see Member_Info
   * 
   * @note このメソッドで使用する共通クラスの共通メソッドは[csvToDatabaseCom]である。詳しい処理内容は
   * そちらを参照の事。
   *
   * @throw ParseException
   * @throw IOException
   **********************************************************************************************
   */
  @Override
  public void csvToDatabase(File tmp_csv_file, Consumer<List<Member_Info>> goto_database) throws ParseException, IOException {

    try{
      LinkedHashMap<String, String> enum_map = Arrays.stream(this.enum_list)
                                                     .collect(Collectors.toMap(Member_Info_Enum::getJaName,
                                                                               Member_Info_Enum::getEnName,
                                                                               (s, a) -> s, 
                                                                               LinkedHashMap::new));

      Function<List<Map<String, String>>, List<Member_Info>> goto_ent = s -> {
        List<Member_Info> list = new ArrayList<>();
        s.parallelStream().map(map -> {
                            try {
                              return new Member_Info(map);
                            } catch (ParseException e) {
                              throw new IllegalStateException();
                            }
                          })
                          .forEachOrdered(ent -> list.add(ent));
        return list;
      };

      this.common_csv.csvToDatabaseCom(tmp_csv_file, enum_map, this.builder, goto_ent, goto_database);

    }catch (IllegalStateException e) {
      throw new ParseException("Error location [Member_Info_Csv:csvToEntitys(ParseException)]" + "\n" + e, 0);
    } catch (IOException e) {
      throw new IOException("Error location [Member_Info_Csv:csvToEntitys(IOException)]" + "\n" + e);
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
   * -# また追加で、性別の文字列（「male」「female」から「男」「女」に）と、顔写真データの削除を行う。
   * -# この関数に引数として渡されたマップリストから、変換対象であるデータを取り出す。
   * -# 取り出したデータを変換して、再度引数のマップリストに上書きする。
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
                                                     .collect(Collectors.toMap(Member_Info_Enum::getJaName,
                                                                               Member_Info_Enum::getEnName,
                                                                               (s, a) -> s, 
                                                                               LinkedHashMap::new));


      UnaryOperator<Map<String, String>> func = map -> {
        map.remove(Member_Info_Enum.Face_Photo.getEnName());

        String birthday_key = Member_Info_Enum.Birthday.getEnName();
        String join_date_key = Member_Info_Enum.Join_Date.getEnName();
        String ret_date_key = Member_Info_Enum.Ret_Date.getEnName();
        String pos_arri_date_key = Member_Info_Enum.Position_Arri_Date.getEnName();
        String assign_date_key = Member_Info_Enum.Assign_Date.getEnName();

        String birthday = map.get(birthday_key);
        String join_date = map.get(join_date_key);
        String ret_date = map.get(ret_date_key);
        String pos_arri_date = map.get(pos_arri_date_key);
        String assign_date = map.get(assign_date_key);

        birthday = birthday == null ? null : birthday.replace("-", "");
        join_date = join_date == null ? null : join_date.replace("-", "");
        ret_date = ret_date == null ? null : ret_date.replace("-", "");
        pos_arri_date = pos_arri_date == null ? null : pos_arri_date.replace("-", "");
        assign_date = assign_date == null ? null : assign_date.replace("-", "");

        map.put(birthday_key, birthday);
        map.put(join_date_key, join_date);
        map.put(ret_date_key, ret_date);
        map.put(pos_arri_date_key, pos_arri_date);
        map.put(assign_date_key, assign_date);

        String key = Member_Info_Enum.Sex.getEnName();
        String sex = map.get(key);

        if(sex == null){
          map.put(key, "該当なし");
        }else if(sex.equals("male")){
          map.put(key, "男");
        }else if(sex.equals("female")){
          map.put(key, "女");
        }else{
          map.put(key, "該当なし");
        }

        return map;
      };
      
      return this.common_csv.outputCsvCom(database_generate, enum_map, func);

    }catch(IOException e){
      throw new IOException("Error location [Member_Info_Csv:outputCsv(IOException)]" + "\n" + e);
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
      throw new IOException("Error location [Member_Info_Csv:outputTemplate]" + "\n" + e);
    }
  }
}
