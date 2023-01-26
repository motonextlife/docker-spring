/** 
 **************************************************************************************
 * @file Sound_Source_Csv_FormImpl.java
 * @brief 主に[音源管理]機能で使用する、CSVから取り出したデータを並列処理でバリデーションする
 * 機能を実装するクラスを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.CsvImplForm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVFormat.Builder;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.springproject.dockerspring.CommonEnum.Environment_Config;
import com.springproject.dockerspring.CommonEnum.NormalEnum.Sound_Source_Enum;
import com.springproject.dockerspring.CommonEnum.UtilEnum.Regex_Enum;
import com.springproject.dockerspring.FileIO.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.Form.DistinctShell;
import com.springproject.dockerspring.Form.FormInterface.Sound_Source_Csv_Form;
import com.springproject.dockerspring.Repository.NormalRepo.Sound_Source_Repo;

import lombok.RequiredArgsConstructor;








/** 
 **************************************************************************************
 * @brief 主に[音源管理]機能で使用する、CSVから取り出したデータを並列処理でバリデーションする
 * 機能を実装するクラス
 * 
 * @details 
 * - このクラスで用いる環境変数のインジェクションは、Lombokによりコンストラクタインジェクションを
 * 行うことで実現する。
 * - CSVからデータを抽出するライブラリに関しては、[Apache commons csv]を用いる。
 * これによって、複雑な条件のデータを不具合なく抽出することが可能になる。
 * - 管理番号の重複判定に関しては、メモリの枯渇を避けるためシェルスクリプトでの実行とする。
 * 
 * @par 使用アノテーション
 * - @Component
 * - @RequiredArgsConstructor(onConstructor = @__(@Autowired))
 * 
 * @see DistinctShell
 * @see Sound_Source_Csv_Form
 * @see Sound_Source_Repo
 * @see CsvCheckResult
 **************************************************************************************
 */ 
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Sound_Source_Csv_FormImpl implements Sound_Source_Csv_Form{

  private final DistinctShell shell;
  private final Sound_Source_Repo sound_repo;
  private final Environment_Config config;


  //! ヘッダー行の定義値列挙型と順番の指定
  private final Sound_Source_Enum[] enum_list = {
    Sound_Source_Enum.Sound_Id,
    Sound_Source_Enum.Upload_Date,
    Sound_Source_Enum.Song_Title,
    Sound_Source_Enum.Composer,
    Sound_Source_Enum.Performer,
    Sound_Source_Enum.Publisher,
    Sound_Source_Enum.Other_Comment
  };


  //! Apache Commons Csvによる、ヘッダー行のマッピング指定
  private final Builder builder = CSVFormat.DEFAULT
                                           .builder()
                                           .setSkipHeaderRecord(true)
                                           .setHeader(Sound_Source_Enum.Sound_Id.getJaName(),
                                                      Sound_Source_Enum.Upload_Date.getJaName(),
                                                      Sound_Source_Enum.Song_Title.getJaName(),
                                                      Sound_Source_Enum.Composer.getJaName(),
                                                      Sound_Source_Enum.Performer.getJaName(),
                                                      Sound_Source_Enum.Publisher.getJaName(),
                                                      Sound_Source_Enum.Other_Comment.getJaName());








                                                



  /** 
   **********************************************************************************************
   * @brief 引数で受け取ったCSV一時ファイルのデータを抽出し、バリデーションを行う。また、エラーが発生
   * すれば、エラーが発生したCSVファイルの行番号と列番号をまとめたリストを出力する。
   * 
   * @details
   * - バリデーションの判定処理に関しては並列に実行し、CSVファイルの全行を効率よく判定する。
   * - 音源番号の重複判定に関してはシェルスクリプトを用いた判定を行い、大量のデータが来てもメモリリークを
   * 起こさず処理が可能になる。
   * - エラーリストに関しては、余りにも量が多い場合は後続の判定処理を行っても無意味なので、一旦途中で
   * フロント側に返却し、エラーが判明している部分のみ直してもらって再度データを投入してもらう。
   *
   * @return エラーリストと重複判定結果を格納した専用格納エンティティ 
   *
   * @param[in] tmp_file 判定対象となるCSV一時ファイル
   * 
   * @par 大まかな処理の流れ
   * -# 音源番号の重複判定の為に用いる、テキスト一時ファイルを作成。
   * -# CSVからデータを一行ずつ抽出し、並列判定処理メソッドにデータを渡す。
   * -# 一行ずつ抽出した音源番号を、バッファ用のリストに書き込んでいく。
   * -# リストに並列判定処理結果をため込んで置き、指定しておいたバッファ数の長さになったら、結果抽出処理を行う。
   * -# 判定結果抽出処理と同時に、ため込んで置いた音源番号リストをテキスト一時ファイルに書き込む。
   * -# エラーリストの行番号を、ヘッダー行を飛ばして「2」からカウントしていき、その数字をエラーの列番号と
   * 一緒に書き込む。
   * -# バッファリストの内容を書き込んだら、バッファリストをリセットし処理を繰り返す。
   * -# 一連の抽出処理を終えたら、音源番号重複チェック用のシェルスクリプトを実行し、重複判定する。
   * -# 出力されたエラーリストと重複判定結果を専用エンティティに格納し、戻り値とする。
   * -# 処理の過程でエラーリストが環境変数で指定した制限数を超えたら、バリデーション処理を中断する。また、
   * CSVファイルの読み取り行が制限値を超過しても処理を中断する。
   * 
   * @see CsvCheckResult
   * 
   * @note CSV読取時の文字コードは、[UTF-8]を指定する。
   *
   * @throw ExecutionException
   * @throw InterruptedException
   * @throw ParseException
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  @Override
  public CsvCheckResult csvChecker(File tmp_file) 
                        throws ExecutionException, InterruptedException, IOException, InputFileDifferException {

    File txt_file = null;

    try(InputStreamReader isr = new InputStreamReader(new FileInputStream(tmp_file), StandardCharsets.UTF_8);  //UTF_8で統一。
        BufferedReader br = new BufferedReader(isr);){

      LinkedHashMap<Integer, List<Integer>> error_list = new LinkedHashMap<>();
      CSVParser csv_list = this.builder.build().parse(br);
      Boolean unique = true;

      List<CompletableFuture<List<Integer>>> buf_list = new ArrayList<>();
      List<String> buf_sound_list = new ArrayList<>();

      int buffer_count = this.config.getOffset();
      int read_row = this.config.getMax();
      txt_file = this.shell.makeNumberTmpFile("soundSourceCsvChecker");
      
      Integer error_row_count = 2;
      int error_list_limit = this.config.getCsv_error_list_limit();

      int count = 0;
      for(CSVRecord item: csv_list){

        // 読み取り行超過判定
        if(count >= read_row){
          throw new InputFileDifferException("row count overflow");
        }

        Map<String, String> csv_map = Arrays.stream(this.enum_list)
                                            .parallel()
                                            .collect(Collectors.toMap(s -> s.getEnName(), 
                                                                      s -> item.get(s.getJaName())));

        buf_list.add(parallelCheck(csv_map));
        buf_sound_list.add(csv_map.get(Sound_Source_Enum.Sound_Id.getEnName()));


        if(buf_list.size() >= buffer_count){

          writeTextFile(txt_file, buf_sound_list);

          if(getValidResult(buf_list, error_list, error_row_count, error_list_limit)){
            break;
          }

          buf_list.clear();
          buf_sound_list.clear();
        }

        count++;
      }


      //残りのデータの処理
      if(!buf_list.isEmpty() && error_list.size() < error_list_limit){

        writeTextFile(txt_file, buf_sound_list);
        getValidResult(buf_list, error_list, error_row_count, error_list_limit);

        buf_list.clear();
        buf_sound_list.clear();
      }


      //重複判定
      if(error_list.size() < error_list_limit){
        unique = this.shell.execShell("soundSourceCsvChecker", txt_file);
      }
      

      return new CsvCheckResult(error_list, unique);
      
    }catch(IOException e){
      throw new IOException("Error location [SoundSourceCsvFormImpl:csvChecker(IOException)]" + "\n" + e);
    }catch(InterruptedException e) {
      throw new InterruptedException("Error location [SoundSourceCsvFormImpl:csvChecker(InterruptedException)]" + "\n" + e);
    }catch(ExecutionException e){
      throw new ExecutionException("Error location [SoundSourceCsvFormImpl:csvChecker(ExecutionException)]" + "\n" + e, e.getCause());
    }catch(RejectedExecutionException e){
      throw new RejectedExecutionException("Error location [SoundSourceCsvFormImpl:csvChecker(RejectedExecutionException)]" + "\n" + e);
    }finally{
      Files.deleteIfExists(tmp_file.toPath());

      if(txt_file != null){
        Files.deleteIfExists(txt_file.toPath());
      }
    }
  }












  /** 
   **********************************************************************************************
   * @brief 引数で受け取った判定対象のデータが入ったマップリストを並列処理で検査する。検査後は、エラー発生
   * 箇所の列番号を格納したリストを返す。
   * 
   * @details
   * - 各項目の判定処理内容は切り分けてあるので、そちらを参照の事。
   *
   * @return エラー発生列番号を格納した並列処理結果格納クラス 
   *
   * @param[in] csv_map 判定対象となるCSVファイル一行分のマップリストデータ
   * 
   * @par 使用アノテーション
   * - @Async("Form")
   *
   * @throw ParseException
   **********************************************************************************************
   */
  @Override
  @Async("Form")
  public CompletableFuture<List<Integer>> parallelCheck(Map<String, String> csv_map) {
    
    List<Integer> error_col_list = new ArrayList<>();

    validSoundId(csv_map, error_col_list);
    validUploadDate(csv_map, error_col_list);
    validSongTitle(csv_map, error_col_list);
    validComposer(csv_map, error_col_list);
    validPerformer(csv_map, error_col_list);
    validPublisher(csv_map, error_col_list);
    validOtherComment(csv_map, error_col_list);

    return CompletableFuture.completedFuture(error_col_list);
  }










  /** @name バリデーション用の非公開メソッド */
  /** @{ */

  /** 
   ***************************************************************
   * @brief 抽出した団員番号のバッファリストを、一時テキストファイルに
   * 書き込んでいく。
   * 
   * @details
   * - このテキストファイルは、シェルスクリプトで団員番号の重複チェックを
   * 行う際に用いられる。
   * - 書き込みモードは追記である。
   *
   * @param[out] txt_file 書き込み対象の一時ファイル
   * @param[in] buf_sound_list 書き込む団員番号のリスト
   ***************************************************************
   */
  private void writeTextFile(File txt_file, List<String> buf_sound_list) throws IOException{

    try(BufferedWriter bw = new BufferedWriter(new FileWriter(txt_file, true));
        PrintWriter pw = new PrintWriter(bw);){

      for(String id: buf_sound_list){
        pw.println(id);
      }
    }
  }







  /** 
   ***************************************************************
   * @brief 判定が終わった結果を並列処理ストリームの中から取り出し、
   * エラーリストに格納する。
   * 
   * @details
   * - エラーリストの行番号のカウントの際、環境変数から指定されている
   * 制限数を超えたら、抽出処理を取りやめる。
   * 
   * @return 処理を取りやめる判定
   *
   * @param[in] buf_list 判定結果抽出対象の並列ストリームリスト
   * @param[out] error_list 書き込むエラーリスト
   * @param[out] error_row_count エラーリストの行カウント
   * @param[in] error_list_limit エラーリスト行カウントの制限値
   ***************************************************************
   */
  private Boolean getValidResult(List<CompletableFuture<List<Integer>>> buf_list, 
                                 LinkedHashMap<Integer, List<Integer>> error_list, 
                                 Integer error_row_count, 
                                 int error_list_limit) throws InterruptedException, ExecutionException{

    Boolean stop_judge = false;

    CompletableFuture.allOf(buf_list.toArray(new CompletableFuture[buf_list.size()])).join();

    for(CompletableFuture<List<Integer>> result: buf_list){
      List<Integer> list = result.get();

      if(list.size() != 0){
        error_list.put(error_row_count, list);

        if(error_list.size() >= error_list_limit){
          stop_judge = true;
          break;
        }
      }

      error_row_count++;
    }

    return stop_judge;
  }













  /** 
   ***************************************************************
   * @brief 音源番号
   * 
   * @details
   * -# 値が空文字や空白でない事。
   * -# 文字数が[30文字以内]であること。
   * -# 文字列が[半角英数字＆ハイフン]のみで構成されている事。
   * -# 既に存在する音源番号と重複していない事。
   *
   * @param[in] csv_map 判定対象のデータが入ったマップリスト
   * @param[out] error_col_list エラーの列番号を書き込むリスト
   ***************************************************************
   */
  private void validSoundId(Map<String, String> csv_map, List<Integer> error_col_list){
    String key = Sound_Source_Enum.Sound_Id.getEnName();
    String str = csv_map.get(key) != null ? csv_map.get(key) : "";

    if(!str.matches(Regex_Enum.ALNUM_HYPHEN_NOTNULL.getRegex()) || str.length() > 30){
      error_col_list.add(1);
    }else{
      if(this.sound_repo.findBySound_Id(str).isPresent()){
        error_col_list.add(1);
      }
    }
  }







  /** 
   ***************************************************************
   * @brief 登録日
   * 
   * @details
   * -# 値が空でない事。
   * -# 日付文字列が[yyyyMMdd]の構成になっている事。
   * 
   * @param[in] csv_map 判定対象のデータが入ったマップリスト
   * @param[out] error_col_list エラーの列番号を書き込むリスト
   ***************************************************************
   */
  private void validUploadDate(Map<String, String> csv_map, List<Integer> error_col_list){
    String key = Sound_Source_Enum.Upload_Date.getEnName();
    String str = csv_map.get(key) != null ? csv_map.get(key) : "";

    if(!str.matches(Regex_Enum.DATE_NO_HYPHEN_NOTNULL.getRegex())){
      error_col_list.add(2);
    }
  }








  /** 
   ***************************************************************
   * @brief 曲名
   * 
   * @details
   * -# 値が空文字や空白でない事。
   * -# 文字数が[50文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   *
   * @param[in] csv_map 判定対象のデータが入ったマップリスト
   * @param[out] error_col_list エラーの列番号を書き込むリスト
   ***************************************************************
   */
  private void validSongTitle(Map<String, String> csv_map, List<Integer> error_col_list){
    String key = Sound_Source_Enum.Song_Title.getEnName();
    String str = csv_map.get(key) != null ? csv_map.get(key) : "";

    if(!str.matches(Regex_Enum.ZENKAKU_ONLY_NOTNULL.getRegex()) || str.length() > 50){
      error_col_list.add(3);
    }
  }







  /** 
   ***************************************************************
   * @brief 作曲者
   * 
   * @details
   * -# 値が空文字や空白でない事。
   * -# 文字数が[50文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   *
   * @param[in] csv_map 判定対象のデータが入ったマップリスト
   * @param[out] error_col_list エラーの列番号を書き込むリスト
   ***************************************************************
   */
  private void validComposer(Map<String, String> csv_map, List<Integer> error_col_list){
    String key = Sound_Source_Enum.Composer.getEnName();
    String str = csv_map.get(key) != null ? csv_map.get(key) : "";

    if(!str.matches(Regex_Enum.ZENKAKU_ONLY_NOTNULL.getRegex()) || str.length() > 50){
      error_col_list.add(4);
    }
  }







  /** 
   ***************************************************************
   * @brief 演奏者
   * 
   * @details
   * -# 値が空文字や空白でない事。
   * -# 文字数が[50文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   *
   * @param[in] csv_map 判定対象のデータが入ったマップリスト
   * @param[out] error_col_list エラーの列番号を書き込むリスト
   ***************************************************************
   */
  private void validPerformer(Map<String, String> csv_map, List<Integer> error_col_list){
    String key = Sound_Source_Enum.Performer.getEnName();
    String str = csv_map.get(key) != null ? csv_map.get(key) : "";

    if(!str.matches(Regex_Enum.ZENKAKU_ONLY_NOTNULL.getRegex()) || str.length() > 50){
      error_col_list.add(5);
    }
  }







  /** 
   ***************************************************************
   * @brief 出版社
   * 
   * @details
   * -# 文字数が[50文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   *
   * @param[in] csv_map 判定対象のデータが入ったマップリスト
   * @param[out] error_col_list エラーの列番号を書き込むリスト
   ***************************************************************
   */
  private void validPublisher(Map<String, String> csv_map, List<Integer> error_col_list){
    String key = Sound_Source_Enum.Publisher.getEnName();
    String str = csv_map.get(key) != null ? csv_map.get(key) : "";

    if(!str.matches(Regex_Enum.ZENKAKU_ONLY_NULLOK.getRegex()) || str.length() > 50){
      error_col_list.add(6);
    }
  }








  /** 
   ***************************************************************
   * @brief その他コメント
   * 
   * @details
   * -# 文字数が[50文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   *
   * @param[in] csv_map 判定対象のデータが入ったマップリスト
   * @param[out] error_col_list エラーの列番号を書き込むリスト
   ***************************************************************
   */
  private void validOtherComment(Map<String, String> csv_map, List<Integer> error_col_list){
    String key = Sound_Source_Enum.Other_Comment.getEnName();
    String str = csv_map.get(key) != null ? csv_map.get(key) : "";

    if(!str.matches(Regex_Enum.ZENKAKU_ONLY_NULLOK.getRegex()) || str.length() > 400){
      error_col_list.add(7);
    }
  }

  /** @} */
}