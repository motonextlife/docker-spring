/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.FileIO.CsvProcess
 * 
 * @brief ファイル入出力機能のうち、[CSVファイルの入出力]に関係する処理を格納したパッケージ
 * 
 * @details
 * - このパッケージは、CSVファイルからのデータの抽出機能や、既にシステムに格納されているデータを
 * CSVファイルとして生成する機能のクラスを格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.FileIO.CsvProcess;






/** 
 **************************************************************************************
 * @file Common_Csv.java
 * @brief すべてのCSV出力機能で使用する共通処理を定義するクラスを格納するファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVFormat.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.springproject.dockerspring.CommonEnum.Environment_Config;
import com.springproject.dockerspring.CommonEnum.UtilEnum.Datatype_Enum;
import com.springproject.dockerspring.CommonEnum.UtilEnum.File_Path_Enum;
import com.springproject.dockerspring.FileIO.OriginalException.InputFileDifferException;

import lombok.RequiredArgsConstructor;








/** 
 **************************************************************************************
 * @brief すべてのCSV出力機能で使用する共通処理を定義するクラス。
 * 
 * @details 
 * - この共通処理では、CSVファイルの処理に関して、特定のクラスに依存しない共通の処理を
 * 定義する。作成対象の特定クラスに対してはDIでこのクラスを使えるようにする。
 * - このクラスで作成される一時ファイルに関しては、すべてローカルにある一時ファイル用の
 * ディレクトリに出力する。なお、その際のパーミッションは、実行権限をすべて剥奪しておく。
 * - このクラスで用いる環境変数のインジェクションは、Lombokによりコンストラクタインジェクションを
 * 行うことで実現する。
 * - CSVを操作するライブラリに関しては、[Apache Commons Csv]を用いる。これによって、複雑な
 * 条件のCSVファイルに対しても読取や書き込みが容易になる。
 * 
 * @par 使用アノテーション
 * - @Component
 * - @RequiredArgsConstructor(onConstructor = @__(@Autowired))
 **************************************************************************************
 */ 
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Common_Csv{

  private final Environment_Config config;

  private final Path specify_dir = Paths.get(File_Path_Enum.ResourceFilePath.getPath());

  private final FileAttribute<Set<PosixFilePermission>> perms = PosixFilePermissions
                                                                .asFileAttribute(PosixFilePermissions
                                                                                .fromString(File_Path_Enum.Permission.getPath()));







                           
                                                                                
  /** @name このクラス内で用いるユーティリティクラス（外部へは一切出さない） */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief このクラス内で生成する一時ファイルを一元的に製作するメソッド。
   * 
   * @details
   * - このメソッドを使用する際に、任意の文字列を渡すことでファイル名にその文字列を付加する。
   * これによって一時ファイル生成を指示した呼び出し元メソッドが特定できる。
   * - なお、ここで生成する一時ファイルの拡張子は何があっても[csv]に統一する。
   *
   * @return 生成した一時ファイル 
   *
   * @param[in] prefix ファイル生成時に、ファイル名に付加する名前
   * 
   * @note このメソッドは、このクラスの全域で使用。
   *
   * @throw IOException
   **********************************************************************************************
   */
  private File makeTmpFile(String prefix) throws IOException {

    try{
      Path tmp_csv_path = Files.createTempFile(this.specify_dir, 
                                               prefix, 
                                               ".".concat(Datatype_Enum.CSV.getExtension()), 
                                               this.perms);

      return tmp_csv_path.toFile();

    }catch(IOException e){
      throw new IOException("Error location [Common_Csv:makeTmpFile]" + "\n" + e);
    }
  }









  /** 
   **********************************************************************************************
   * @brief CSVファイルのヘッダー行を生成するための共通メソッド。
   * 
   * @details
   * - 実行する際に、ヘッダー行の名前のリストと、出力ストリームを渡すことで、ヘッダー行のみ自動生成。
   * - ヘッダー行の順番については、最初に渡した引数のリストの順番に従う。
   *
   * @param[in] header_list ヘッダー行文字列が格納されたリスト
   * @param[in] csv_print CSVの出力ストリーム
   * 
   * @note このメソッドは、このクラスの全域で使用。
   *
   * @throw IOException
   **********************************************************************************************
   */
  private void makeHeader(List<String> header_list, CSVPrinter csv_print) throws IOException {
    csv_print.printRecord(header_list);
    csv_print.flush();
  }







  /** 
   **********************************************************************************************
   * @brief 処理中にエラーになった際は、作成されている一時ファイルを削除し例外を投げる。
   *
   * @param[in] delete_tmp_file 削除対象の一時ファイル
   * 
   * @note このメソッドは、主に[extractCsvCom]で使用。
   *
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  private void errorTmpFileReset(File delete_tmp_file) throws IOException, InputFileDifferException {
    Files.deleteIfExists(delete_tmp_file.toPath());
    throw new InputFileDifferException("missing csv file");
  }









  /** 
   **********************************************************************************************
   * @brief 抽出する対象のCSVファイルのヘッダー行が正当性を持っているか判定する。
   *
   * @param[in] enum_map 正当性比較用のヘッダー行のマップリスト
   * @param[in] csv_record 検査対象となるヘッダー行のCSVレコード
   * @param[in] csv_print 合格時に出力を行う先の一時ファイルのストリーム
   *
   * @par 処理の大まかな流れ
   * -# 抽出したヘッダー行が、以下の条件に当てはまらない場合はエラーとする。
   *    - ヘッダー行に所定の文字列が欠落していたり、順番が違ったり、誤字があったりしないこと。
   * -# エラーがあった場合は、例外を投げ、処理を終了する。
   * -# エラーがなければ、出力先の一時CSVファイルに対して出力を行う。
   * 
   * @note このメソッドは、主に[extractCsvCom]で使用。
   *
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  private void headerJudgement(File temp_file_path, 
                               LinkedHashMap<String, String> enum_map, 
                               CSVRecord csv_record, 
                               CSVPrinter csv_print) throws IOException, InputFileDifferException {

    List<String> key_list = enum_map.keySet().stream().toList();
    List<String> header_list = csv_record.toList();

    // ヘッダー列数同一確認
    if(key_list.size() != header_list.size()){
      errorTmpFileReset(temp_file_path);
    }

    // ヘッダー行文字列正当性確認
    Boolean judge = IntStream.range(0, key_list.size())
                             .parallel()
                             .allMatch(s -> key_list.get(s).equals(header_list.get(s)));

    if(judge){
      makeHeader(header_list, csv_print);
    }else{
      errorTmpFileReset(temp_file_path);
    }
  }








  /** 
   **********************************************************************************************
   * @brief 引数で渡された関数型インターフェースを使って、CSVから抽出したデータをエンティティに格納
   * して、データベースに格納する。格納後はバッファリストをリセットする。
   *
   * @param[in] buf_list 一時CSVファイルから少しの行数のみ抽出したデータ
   * @param[in] goto_ent 抽出したデータを対象のエンティティに格納する処理の関数
   * @param[in] goto_database エンティティに格納したデータをデータベースに保存する処理の関数
   *
   * @note このメソッドは、主に[csvToDatabaseCom]で使用。
   * 
   * @throw IOException
   **********************************************************************************************
   */
  private <T> void storeData(List<Map<String, String>> buf_list, 
                             Function<List<Map<String, String>>, List<T>> goto_ent, 
                             Consumer<List<T>> goto_database) throws IOException {

    List<T> ent_list = goto_ent.apply(buf_list);
    goto_database.accept(ent_list);
    buf_list.clear();
  }









  /** 
   **********************************************************************************************
   * @brief 渡されたデータベース検索の結果をCSVファイルに出力する。
   * 
   * @param[in] database_list 出力を指定されたデータベースの検索結果のリスト
   * @param[in] csv_print 指定された一時ファイルへ出力するためのストリーム
   * @param[in] enum_map 検索データの該当ヘッダー名のデータを取得するためのマップリスト
   * @param[in] parse_func 指定されている、出力データの変換処理の関数
   * 
   * @par 処理の大まかな流れ
   * -# 渡された検索結果を、指定されていれば変換関数にしたがって該当の値を変換する。
   * -# 検索結果のデータのマップリストから、ヘッダー行指定用のマップリストを用いてデータを探す。
   * -# データを探し終えたら、ヘッダー行指定用のマップリストの順番に従ってリストを作成。
   * -# 作成したリストをそのままCSVファイルへ書き込む。
   * 
   * @note このメソッドは、主に[outputCsvCom]で使用。
   *
   * @throw IOException
   **********************************************************************************************
   */
  private void writeBodyCsv(List<Map<String, String>> database_list, 
                            CSVPrinter csv_print, 
                            LinkedHashMap<String, String> enum_map, 
                            UnaryOperator<Map<String, String>> parse_func) throws IOException {

    for(Map<String, String> csv_record: database_list){

      //変換関数が指定されていれば実行する。
      if(parse_func != null){
        csv_record = parse_func.apply(csv_record);
      }

      Map<String, String> tmp_map = csv_record;
      List<String> child = enum_map.entrySet()
                                   .stream()
                                   .map(s -> tmp_map.get(s.getValue()))
                                   .toList();
      csv_print.printRecord(child);
    }
  }

  /** @} */










  




  /** 
   **********************************************************************************************
   * @brief 読み込んだCSVファイルからデータを抽出し、正当性が確保されれば一時ファイルとして
   * 出力する。
   * 
   * @details
   * - 実行する際には、入力したCSVファイルと、正当性比較用のヘッダー行の文字列が格納されたマップリストを
   * 用いる。このヘッダー行のリストで、入力されたCSVファイルの正当性を確認する。
   * 
   * @return 正当性が確保され、一時ファイルに出力されたCSVファイル
   *
   * @param[in] csv_file 入力したCSVファイル
   * @param[in] enum_map 正当性比較用のヘッダー行のマップリスト
   *
   * @par 処理の大まかな流れ
   * -# 抽出データ書き込み用の一時ファイルを作成。
   * -# CSVの読み取り設定はデフォルトに設定し読み込むが、以下の条件に当てはまらない場合はエラーとする。
   * なお、エラーになった際は、作成しておいた一時ファイルを削除し、例外を投げる。
   *    - 読み取り行が環境変数の制限値を超過しないこと。
   *    - ヘッダー行に所定の文字列が欠落していたり、順番が違ったり、誤字があったりしないこと。
   *    - ヘッダー行以降のデータは、余りにも長い文字列（ここでは1000文字以上）がないこと。
   * -# 読み取りは環境変数のバッファ数単位の行数で行い、メモリの枯渇を避ける。
   * -# 行を順番に一時ファイルに書き込んでいき、書き込んだファイルを戻り値として終了する。
   *
   * @note CSVの読み取りは[UTF-8]で統一して読み取る。
   * 
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
   public File extractCsvCom(MultipartFile csv_file, 
                             LinkedHashMap<String, String> enum_map) throws IOException, InputFileDifferException {

    CSVFormat format = CSVFormat.DEFAULT.builder().build();
    File temp_file_path = makeTmpFile("extractCsv");

    try(InputStreamReader isr = new InputStreamReader(csv_file.getInputStream(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(temp_file_path), StandardCharsets.UTF_8);
        CSVPrinter csv_print = new CSVPrinter(new BufferedWriter(osw), format)){

      CSVParser csv_list = CSVFormat.DEFAULT.parse(br);
      int read_row = this.config.getMax();
      int buffer_count = this.config.getOffset();

      int count = 0;
      for(CSVRecord csv_record: csv_list){

        // 読み取り行超過判定
        if(count > read_row){ 
          errorTmpFileReset(temp_file_path); 
        }
        
        // ヘッダー行判定(最初の行、つまりループの一番最初のみ行う)
        if(count == 0){
          headerJudgement(temp_file_path, enum_map, csv_record, csv_print);
          count++;
          continue;
        }

        // コンテンツ文字数超過判定
        List<String> child = csv_record.toList();
        Boolean over = child.parallelStream().anyMatch(s -> s.length() > 1000);
        if(over){ 
          errorTmpFileReset(temp_file_path);
        }

        csv_print.printRecord(child);

        // 抽出した行がバッファ数に達せば、出力開始
        if(count % buffer_count == 0){
          csv_print.flush();
        }

        count++;
      }
      
      // まだ出力していない残りの行を出力
      csv_print.flush();

    }catch(IOException e){
      Files.deleteIfExists(temp_file_path.toPath());
      throw new IOException("Error location [Common_Csv:extractCsvCom(IOException)]" + "\n" + e);
    }catch(NullPointerException e){
      Files.deleteIfExists(temp_file_path.toPath());
      throw new NullPointerException("Error location [Common_Csv:extractCsvCom(NullPointerException)]" + "\n" + e);
    }

    return temp_file_path;
  }












  /** 
   **********************************************************************************************
   * @brief 正当性が確保された一時ファイル内のCSVファイルを読み込み、データをデータベースに格納する。
   * 出力する。
   * 
   * @details
   * - 実行する際には、入力したCSVファイルと、正当性比較用のヘッダー行の文字列が格納されたマップリストを
   * 用いる。このマップリストによって、読み込む列を判別する。
   * - エンティティに格納する処理は、このクラスの集約先の特定クラスから関数型インターフェースで処理を
   * 受け取り、その中で行う。これによって、特定クラスに依存する処理を共通クラスで行える。
   * - データベースに格納する処理は、サービスから関数型インターフェースで処理を受け取り、その中で行う。
   * これによって、データベース処理をこのクラスから分離し、この共通クラスの汎用性を増すことが出来る。
   *
   * @param[in] temp_file 入力する正当性が確保されたCSV一時ファイル
   * @param[in] enum_map 抽出列判別用のヘッダー行リスト
   * @param[in] builder CSV読み込み時のヘッダーが設定されているビルダー
   * @param[in] goto_ent 抽出したデータを対象のエンティティに格納する処理の関数
   * @param[in] goto_database エンティティに格納したデータをデータベースに保存する処理の関数
   *
   * @par 処理の大まかな流れ
   * -# CSVファイルから、一定の行数づつ読みとり、メモリの枯渇を避けながらエンティティに格納。
   * -# エンティティに格納したら、そのデータをデータベースに保存する。
   * -# この一連の処理を、CSVファイルからデータを全部読み取るまで繰り返す。
   * -# この処理の過程でエラーが起これば、引数で渡された一時ファイルを削除し、例外を投げる。
   *
   * @note CSVの読み取りは[UTF-8]で統一して読み取る。
   * 
   * @throw IOException
   **********************************************************************************************
   */
  public <T> void csvToDatabaseCom(File temp_file, 
                                   LinkedHashMap<String, String> enum_map, 
                                   Builder builder,
                                   Function<List<Map<String, String>>, List<T>> goto_ent,
                                   Consumer<List<T>> goto_database) throws IOException{
 
    try(InputStreamReader isr = new InputStreamReader(new FileInputStream(temp_file), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);){

      // ヘッダー行は飛ばして抽出する
      CSVParser csv_list = builder.setSkipHeaderRecord(true)
                                  .build()
                                  .parse(br);

      int buffer_count = this.config.getOffset();
      List<Map<String, String>> buf_list = new ArrayList<>();

      for(CSVRecord csv_record: csv_list){
        Map<String, String> csv_map = enum_map.keySet()
                                              .stream()
                                              .toList()
                                              .parallelStream()
                                              .collect(Collectors.toMap(s -> enum_map.get(s), 
                                                                        s -> csv_record.get(s)));
        buf_list.add(csv_map);

        // バッファリストが所定数に達せば、格納処理開始
        if(buf_list.size() >= buffer_count){
          storeData(buf_list, goto_ent, goto_database);
        }
      }

      // まだ出力していない残りのデータを格納処理開始
      if(!buf_list.isEmpty()){
        storeData(buf_list, goto_ent, goto_database);
      }
      
    }catch(IOException e){
      throw new IOException("Error location [Common_Csv:extractCsvCom]" + "\n" + e);
    }finally{
      Files.deleteIfExists(temp_file.toPath());
    }
  }











  /** 
   **********************************************************************************************
   * @brief データベースに保存してあるデータを、CSVファイルに出力する。
   * 
   * @details
   * - 実行する際には、入力したCSVファイルと、抽出用のヘッダー行の文字列が格納されたマップリストを用いる。
   * このマップリストによって、ヘッダー行を出力する。
   * - データベースから出力する処理は、サービスから関数型インターフェースで処理を受け取り、その中で行う。
   * これによって、データベース処理をこのクラスから分離し、この共通クラスの汎用性を増すことが出来る。
   * - このクラスを用いる先の機能によっては、特定の値を変換したうえでCSVに出力する事があるため、必要に応じて
   * 変換用の関数型インターフェースを受け取り、その中で変換処理を行ったうえでCSVに書き込む。
   *
   * @param[in] database_generate データベースから抽出対象のデータを出力する処理の関数
   * @param[in] enum_map 出力用のヘッダー行リスト
   * @param[in] parse_func 変換の必要がある値を変換する処理の関数
   *
   * @par 処理の大まかな流れ
   * -# CSVデータを出力する一時ファイルを作成する。
   * -# 一時ファイルに対して、ヘッダー行を出力する。
   * -# データベースから、1ページで出力可能なデータ数のみ出力し、必要に応じてデータの変換を行ったうえで
   * 一時ファイルに出力する。メモリの枯渇を防ぐため、バッファ単位での出力となる。
   * -# 前項の一連の処理を、データベースから出力対象のデータがなくなるまで繰り返す。なお、この繰り返しは
   * 環境変数で指定されている最大出力データ数以上は行わないように制限をかけている。
   * -# 出力が終わった一時ファイルを、このメソッドの戻り値とする。
   * -# この処理の過程でエラーが発生した場合は、一時ファイルを削除し例外を投げる。
   *
   * @note 
   * - CSVの書き込みは[UTF-8]で統一して行う。
   * - データベース出力の際のページ数指定に関しては、環境変数にセットされている「最大出力データ数」と
   * 「オフセット数」を元に計算して、繰り返すページ数を算出する。「最大出力データ数」を「オフセット数」で
   * 除算すれば、出力可能な最大のページ数を求めることができる。その最大ページ数までのループとなる。
   * 
   * @throw IOException
   **********************************************************************************************
   */
  public File outputCsvCom(Function<Integer, List<Map<String, String>>> database_generate, 
                           LinkedHashMap<String, String> enum_map, 
                           UnaryOperator<Map<String, String>> parse_func) throws IOException {

    CSVFormat format = CSVFormat.DEFAULT.builder().build();
    File temp_file_path = makeTmpFile("outputCsv");

    try(OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(temp_file_path), StandardCharsets.UTF_8);
        CSVPrinter csv_print = new CSVPrinter(new BufferedWriter(osw), format)){

      //ヘッダー行の出力
      List<String> header = enum_map.keySet().stream().toList();
      makeHeader(header, csv_print);

      // ループの最大ページ数算出
      int offset = this.config.getOffset();
      int max = this.config.getMax();
      int max_pagenum = Math.ceilDiv(max, offset);

      for(int i = 0; i < max_pagenum; i++){
        List<Map<String, String>> database_list = database_generate.apply(i);

        //データベース出力結果が空であれば、その時点で処理を終了。
        if(database_list.isEmpty()){break;}

        //データ本文の出力
        writeBodyCsv(database_list, csv_print, enum_map, parse_func);
        csv_print.flush();
      }

      //まだ未出力のデータを出力。
      csv_print.flush();

    }catch(IOException e){
      Files.deleteIfExists(temp_file_path.toPath());
      throw new IOException("Error location [Common_Csv:outputCsvCom]" + "\n" + e);
    }
    
    return temp_file_path;
  }












  /** 
   **********************************************************************************************
   * @brief このシステムにCSVファイルを投入する際に便利な、CSVファイルのテンプレートを出力する。
   * 
   * @details
   * - あらかじめ指定したヘッダー行のリストにしたがって、その順番通りにヘッダー行を出力する。
   * - 入力するデータを入れるであろうスペースは、改行を行って空白にしておく。
   * - このシステムへの一回のCSVファイル入力で読み取れる限界行の位置に、「これ以上は書き込んでも読み込めない」
   * 旨のメッセージを書いておくことで、利便性を上げる。
   *
   * @param[in] janame_list 出力を指定するヘッダー行のリスト
   *
   * @par 処理の大まかな流れ
   * -# CSVファイル出力用の一時ファイルを作成。
   * -# 引数で渡されたヘッダー行リストを出力する。
   * -# このシステムが読み取り可能な行数だけ改行を行い、空白を空ける。
   * -# 空白を空けた最後の行に、これ以上は読み込めない旨のメッセージを記載。
   * -# 一時ファイルに一連の書き込み処理を出力し、その一時ファイルを戻り値とする。
   *
   * @note 
   * - CSVの書き込みは[UTF-8]で統一して行う。
   * 
   * @throw IOException
   **********************************************************************************************
   */
  public File outputTemplateCom(List<String> janame_list) throws IOException {

    CSVFormat format = CSVFormat.DEFAULT.builder().build();
    File temp_file_path = makeTmpFile("outputTemplate");
                      
    try(OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(temp_file_path), StandardCharsets.UTF_8);
        CSVPrinter pri = new CSVPrinter(new BufferedWriter(osw), format)){

      //ヘッダー行の出力
      makeHeader(janame_list, pri);

      //行の空っぽの部分を出力。空っぽ行は読取制限数に従う。
      int empty_row = this.config.getMax();
      for(int i = 0; i < empty_row; i++){
        pri.println();
      }

      //表の末尾に、読み取れない旨を示す文を表示。
      pri.printRecord("この行以降は読み取りませんので、ご注意ください。なお、入力の際は、この行を削除してください。");
      pri.flush();

    }catch(IOException e){
      Files.deleteIfExists(temp_file_path.toPath());
      throw new IOException("Error location [Common_Csv:outputTemplateCom]" + "\n" + e);
    }
    
    return temp_file_path;
  }
}
