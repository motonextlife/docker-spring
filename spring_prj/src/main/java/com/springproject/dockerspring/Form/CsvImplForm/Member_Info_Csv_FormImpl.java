/** 
 **************************************************************************************
 * @file Member_Info_Csv_FormImpl.java
 * @brief 主に[団員管理]機能で使用する、CSVから取り出したデータを並列処理でバリデーションする
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.springproject.dockerspring.CommonEnum.Environment_Config;
import com.springproject.dockerspring.CommonEnum.NormalEnum.Member_Info_Enum;
import com.springproject.dockerspring.CommonEnum.UtilEnum.DateFormat_Enum;
import com.springproject.dockerspring.CommonEnum.UtilEnum.Regex_Enum;
import com.springproject.dockerspring.FileIO.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.Form.DistinctShell;
import com.springproject.dockerspring.Form.FormInterface.Member_Info_Csv_Form;
import com.springproject.dockerspring.Repository.NormalRepo.Member_Info_Repo;

import lombok.RequiredArgsConstructor;









/** 
 **************************************************************************************
 * @brief 主に[団員管理]機能で使用する、CSVから取り出したデータを並列処理でバリデーションする
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
 * @see Member_Info_Csv_Form
 * @see Member_Info_Repo
 * @see CsvCheckResult
 **************************************************************************************
 */ 
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Member_Info_Csv_FormImpl implements Member_Info_Csv_Form{

  private final DistinctShell shell;
  private final Member_Info_Repo memb_info_repo;
  private final Environment_Config config;

  private final SimpleDateFormat parse_date = new SimpleDateFormat(DateFormat_Enum.DATE_NO_HYPHEN.getFormat());
  private final EmailValidator email_valid = EmailValidator.getInstance();


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
  private final Builder builder = CSVFormat.DEFAULT
                                           .builder()
                                           .setSkipHeaderRecord(true)
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
   * @brief 引数で受け取ったCSV一時ファイルのデータを抽出し、バリデーションを行う。また、エラーが発生
   * すれば、エラーが発生したCSVファイルの行番号と列番号をまとめたリストを出力する。
   * 
   * @details
   * - バリデーションの判定処理に関しては並列に実行し、CSVファイルの全行を効率よく判定する。
   * - 団員番号の重複判定に関してはシェルスクリプトを用いた判定を行い、大量のデータが来てもメモリリークを
   * 起こさず処理が可能になる。
   * - エラーリストに関しては、余りにも量が多い場合は後続の判定処理を行っても無意味なので、一旦途中で
   * フロント側に返却し、エラーが判明している部分のみ直してもらって再度データを投入してもらう。
   *
   * @return エラーリストと重複判定結果を格納した専用格納エンティティ 
   *
   * @param[in] tmp_file 判定対象となるCSV一時ファイル
   * 
   * @par 大まかな処理の流れ
   * -# 団員番号の重複判定の為に用いる、テキスト一時ファイルを作成。
   * -# CSVからデータを一行ずつ抽出し、並列判定処理メソッドにデータを渡す。
   * -# 一行ずつ抽出した団員番号を、バッファ用のリストに書き込んでいく。
   * -# リストに並列判定処理結果をため込んで置き、指定しておいたバッファ数の長さになったら、結果抽出処理を行う。
   * -# 判定結果抽出処理と同時に、ため込んで置いた団員番号リストをテキスト一時ファイルに書き込む。
   * -# エラーリストの行番号を、ヘッダー行を飛ばして「2」からカウントしていき、その数字をエラーの列番号と
   * 一緒に書き込む。
   * -# バッファリストの内容を書き込んだら、バッファリストをリセットし処理を繰り返す。
   * -# 一連の抽出処理を終えたら、団員番号重複チェック用のシェルスクリプトを実行し、重複判定する。
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
  public CsvCheckResult csvChecker(File tmp_file) 
                        throws ExecutionException, InterruptedException, ParseException, IOException, InputFileDifferException {

    File txt_file = null;

    try(InputStreamReader isr = new InputStreamReader(new FileInputStream(tmp_file), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);){

      LinkedHashMap<Integer, List<Integer>> error_list = new LinkedHashMap<>();
      CSVParser csv_list = this.builder.build().parse(br);
      Boolean unique = true;

      List<CompletableFuture<List<Integer>>> buf_list = new ArrayList<>();
      List<String> buf_member_list = new ArrayList<>();

      int buffer_count = this.config.getOffset();
      int read_row = this.config.getMax();
      txt_file = this.shell.makeNumberTmpFile("memberInfoCsvChecker");
      
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
        buf_member_list.add(csv_map.get(Member_Info_Enum.Member_Id.getEnName()));


        if(buf_list.size() >= buffer_count){

          writeTextFile(txt_file, buf_member_list);

          if(getValidResult(buf_list, error_list, error_row_count, error_list_limit)){
            break;
          }

          buf_list.clear();
          buf_member_list.clear();
        }

        count++;
      }


      //残りのデータの処理
      if(!buf_list.isEmpty() && error_list.size() < error_list_limit){

        writeTextFile(txt_file, buf_member_list);
        getValidResult(buf_list, error_list, error_row_count, error_list_limit);

        buf_list.clear();
        buf_member_list.clear();
      }


      //重複判定
      if(error_list.size() < error_list_limit){
        unique = this.shell.execShell("memberInfoCsvChecker", txt_file);
      }
      

      return new CsvCheckResult(error_list, unique);
      
    }catch(IOException e){
      throw new IOException("Error location [Member_Info_Csv_FormImpl:csvChecker(IOException)]" + "\n" + e);
    }catch(InterruptedException e) {
      throw new InterruptedException("Error location [Member_Info_Csv_FormImpl:csvChecker(InterruptedException)]" + "\n" + e);
    }catch(ExecutionException e){
      throw new ExecutionException("Error location [Member_Info_Csv_FormImpl:csvChecker(ExecutionException)]" + "\n" + e, e.getCause());
    }catch(RejectedExecutionException e){
      throw new RejectedExecutionException("Error location [Member_Info_Csv_FormImpl:csvChecker(RejectedExecutionException)]" + "\n" + e);
    }catch (ParseException e){
      throw new ParseException("Error location [Member_Info_Csv_FormImpl:csvChecker(ParseException)]" + "\n" + e, e.getErrorOffset());
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
  public CompletableFuture<List<Integer>> parallelCheck(Map<String, String> csv_map) throws ParseException {
    
    List<Integer> error_col_list = new ArrayList<>();

    validMemberId(csv_map, error_col_list);
    validName(csv_map, error_col_list);
    validNamePronu(csv_map, error_col_list);
    validSex(csv_map, error_col_list);

    Boolean flag_1 = true;
    Date birthday = validBirthday(csv_map, error_col_list, flag_1);
    
    Boolean flag_2 = true;
    Date join_date = validJoinDate(csv_map, error_col_list, flag_2);

    Boolean flag_3 = true;
    Date ret_date = validRetDate(csv_map, error_col_list, flag_3);

    validEmail_1(csv_map, error_col_list);
    validEmail_2(csv_map, error_col_list);
    validTel_1(csv_map, error_col_list);
    validTel_2(csv_map, error_col_list);
    validAddrPostcode(csv_map, error_col_list);
    validAddr(csv_map, error_col_list);
    validPosition(csv_map, error_col_list);

    Boolean flag_4 = true;
    Date position_arri_date = validArriDate(csv_map, error_col_list, flag_4);

    validJob(csv_map, error_col_list);
    validAssignDept(csv_map, error_col_list);

    Boolean flag_5 = true;
    Date assign_date = validAssignDate(csv_map, error_col_list, flag_5);

    validInstCharge(csv_map, error_col_list);
    validOtherComment(csv_map, error_col_list);
    dateLogicCheck_BirthAndJoin(flag_1, flag_2, birthday, join_date, error_col_list);
    dateLogicCheck_JoinAndRet(flag_2, flag_3, join_date, ret_date, error_col_list);
    dateLogicCheck_JoinAndPosition(flag_2, flag_4, join_date, position_arri_date, error_col_list);
    dateLogicCheck_JoinAndAssign(flag_2, flag_4, join_date, assign_date, error_col_list);

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
   * @param[in] buf_faci_list 書き込む団員番号のリスト
   ***************************************************************
   */
  private void writeTextFile(File txt_file, List<String> buf_faci_list) throws IOException{

    try(BufferedWriter bw = new BufferedWriter(new FileWriter(txt_file, true));
        PrintWriter pw = new PrintWriter(bw);){

      for(String id: buf_faci_list){
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
   * @brief 団員番号
   * 
   * @details
   * -# 値が空文字や空白でない事。
   * -# 文字数が[30文字以内]であること。
   * -# 文字列が[半角英数字＆ハイフン]のみで構成されている事。
   * -# 既に存在する団員番号と重複していない事。
   *
   * @param[in] csv_map 判定対象のデータが入ったマップリスト
   * @param[out] error_col_list エラーの列番号を書き込むリスト
   ***************************************************************
   */
  private void validMemberId(Map<String, String> csv_map, List<Integer> error_col_list){
    String key = Member_Info_Enum.Member_Id.getEnName();
    String str = csv_map.get(key) != null ? csv_map.get(key) : "";

    if(!str.matches(Regex_Enum.ALNUM_HYPHEN_NOTNULL.getRegex()) || str.length() > 30){
      error_col_list.add(1);
    }else{
      if(memb_info_repo.findByMember_id(str).isPresent()){
        error_col_list.add(1);
      }
    }
  }








  /** 
   ***************************************************************
   * @brief 名前
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
  private void validName(Map<String, String> csv_map, List<Integer> error_col_list){
    String key = Member_Info_Enum.Name.getEnName();
    String str = csv_map.get(key) != null ? csv_map.get(key) : "";

    if(!str.matches(Regex_Enum.ZENKAKU_ONLY_NOTNULL.getRegex()) || str.length() > 50){
      error_col_list.add(2);
    }
  }








  /** 
   ***************************************************************
   * @brief ふりがな
   * 
   * @details
   * -# 値が空文字や空白でない事。
   * -# 文字数が[100文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   *
   * @param[in] csv_map 判定対象のデータが入ったマップリスト
   * @param[out] error_col_list エラーの列番号を書き込むリスト
   ***************************************************************
   */
  private void validNamePronu(Map<String, String> csv_map, List<Integer> error_col_list){
    String key = Member_Info_Enum.Name_Pronu.getEnName();
    String str = csv_map.get(key) != null ? csv_map.get(key) : "";

    if(!str.matches(Regex_Enum.ZENKAKU_ONLY_NOTNULL.getRegex()) || str.length() > 100){
      error_col_list.add(3);
    }
  }








  /** 
   ***************************************************************
   * @brief 性別
   * 
   * @details
   * -# 値が空文字や空白でない事。
   * -# 指定文字列のみで構成されている事。
   * (男, 女)
   *
   * @param[in] csv_map 判定対象のデータが入ったマップリスト
   * @param[out] error_col_list エラーの列番号を書き込むリスト
   ***************************************************************
   */
  private void validSex(Map<String, String> csv_map, List<Integer> error_col_list){
    String key = Member_Info_Enum.Sex.getEnName();
    String str = csv_map.get(key) != null ? csv_map.get(key) : "";

    if(!str.matches("男|女")){
      error_col_list.add(4);
    }
  }








  /** 
   ***************************************************************
   * @brief 誕生日
   * 
   * @details
   * -# 値が空でない事。
   * -# 日付文字列が[yyyyMMdd]の構成になっている事。
   *
   * @return 抽出した誕生日データ
   * 
   * @param[in] csv_map 判定対象のデータが入ったマップリスト
   * @param[out] error_col_list エラーの列番号を書き込むリスト
   * @param[out] flag_1 日付論理チェックの際に使用する真偽値フラグ
   ***************************************************************
   */
  private Date validBirthday(Map<String, String> csv_map, List<Integer> error_col_list, Boolean flag_1) throws ParseException {
    String key = Member_Info_Enum.Birthday.getEnName();
    String str = csv_map.get(key) != null ? csv_map.get(key) : "";
    Date birthday = null;

    if(!str.matches(Regex_Enum.DATE_NO_HYPHEN_NOTNULL.getRegex())){
      error_col_list.add(5);
    }else{
      birthday = this.parse_date.parse(str);
      flag_1 = true;
    }

    return birthday;
  }







  /** 
   ***************************************************************
   * @brief 入団日
   * 
   * @details
   * -# 値が空でない事。
   * -# 日付文字列が[yyyyMMdd]の構成になっている事。
   *
   * @return 抽出した入団日データ
   * 
   * @param[in] csv_map 判定対象のデータが入ったマップリスト
   * @param[out] error_col_list エラーの列番号を書き込むリスト
   * @param[out] flag_2 日付論理チェックの際に使用する真偽値フラグ
   ***************************************************************
   */
  private Date validJoinDate(Map<String, String> csv_map, List<Integer> error_col_list, Boolean flag_2) throws ParseException {
    String key = Member_Info_Enum.Join_Date.getEnName();
    String str = csv_map.get(key) != null ? csv_map.get(key) : "";
    Date join_date = null;

    if(!str.matches(Regex_Enum.DATE_NO_HYPHEN_NOTNULL.getRegex())){
      error_col_list.add(6);
    }else{
      join_date = this.parse_date.parse(str);
      flag_2 = true;
    }

    return join_date;
  }








  /** 
   ***************************************************************
   * @brief 退団日
   * 
   * @details
   * -# 日付文字列が[yyyyMMdd]の構成になっている事。
   *
   * @return 抽出した退団日データ
   * 
   * @param[in] csv_map 判定対象のデータが入ったマップリスト
   * @param[out] error_col_list エラーの列番号を書き込むリスト
   * @param[out] flag_3 日付論理チェックの際に使用する真偽値フラグ
   ***************************************************************
   */
  private Date validRetDate(Map<String, String> csv_map, List<Integer> error_col_list, Boolean flag_3) throws ParseException {
    String key = Member_Info_Enum.Ret_Date.getEnName();
    String str = csv_map.get(key) != null ? csv_map.get(key) : "";
    Date ret_date = null;

    if(!str.matches(Regex_Enum.DATE_NO_HYPHEN_NULLOK.getRegex())){
      error_col_list.add(7);
    }else{
      if(!str.equals("")){
        ret_date = this.parse_date.parse(str);
        flag_3 = true;
      }
    }

    return ret_date;
  }








  /** 
   ***************************************************************
   * @brief メールアドレス1
   * 
   * @details
   * -# [Eメール形式]として成立する文字列であること。
   * -# [半角英数字＆メールアドレスで使用する記号]のみで構成されている事。
   * -# 文字数が[100文字以内]であること。
   *
   * @param[in] csv_map 判定対象のデータが入ったマップリスト
   * @param[out] error_col_list エラーの列番号を書き込むリスト
   ***************************************************************
   */
  private void validEmail_1(Map<String, String> csv_map, List<Integer> error_col_list){
    String key = Member_Info_Enum.Email_1.getEnName();
    String str = csv_map.get(key) != null ? csv_map.get(key) : "";

    if(!str.equals("") 
        && (!this.email_valid.isValid(str) || !str.matches(Regex_Enum.EMAIL_SUPPORT.getRegex()) || str.length() > 100)){
      error_col_list.add(8);
    }
  }








  /** 
   ***************************************************************
   * @brief メールアドレス2
   * 
   * @details
   * -# [Eメール形式]として成立する文字列であること。
   * -# [半角英数字＆メールアドレスで使用する記号]のみで構成されている事。
   * -# 文字数が[100文字以内]であること。
   *
   * @param[in] csv_map 判定対象のデータが入ったマップリスト
   * @param[out] error_col_list エラーの列番号を書き込むリスト
   ***************************************************************
   */
  private void validEmail_2(Map<String, String> csv_map, List<Integer> error_col_list){
    String key = Member_Info_Enum.Email_2.getEnName();
    String str = csv_map.get(key) != null ? csv_map.get(key) : "";

    if(!str.equals("") 
        && (!this.email_valid.isValid(str) || !str.matches(Regex_Enum.EMAIL_SUPPORT.getRegex()) || str.length() > 100)){
      error_col_list.add(9);
    }
  }








  /** 
   ***************************************************************
   * @brief 電話番号1
   * 
   * @details
   * -# 文字数が[20文字以内]であること。
   * -# [半角英数字＆ハイフン]のみで構成されている事。
   *
   * @param[in] csv_map 判定対象のデータが入ったマップリスト
   * @param[out] error_col_list エラーの列番号を書き込むリスト
   ***************************************************************
   */
  private void validTel_1(Map<String, String> csv_map, List<Integer> error_col_list){
    String key = Member_Info_Enum.Tel_1.getEnName();
    String str = csv_map.get(key) != null ? csv_map.get(key) : "";

    if(!str.matches(Regex_Enum.DIGIT_HYPHEN.getRegex()) || str.length() > 20){
      error_col_list.add(10);
    }
  }







  /** 
   ***************************************************************
   * @brief 電話番号2
   * 
   * @details
   * -# 文字数が[20文字以内]であること。
   * -# [半角英数字＆ハイフン]のみで構成されている事。
   *
   * @param[in] csv_map 判定対象のデータが入ったマップリスト
   * @param[out] error_col_list エラーの列番号を書き込むリスト
   ***************************************************************
   */
  private void validTel_2(Map<String, String> csv_map, List<Integer> error_col_list){
    String key = Member_Info_Enum.Tel_2.getEnName();
    String str = csv_map.get(key) != null ? csv_map.get(key) : "";

    if(!str.matches(Regex_Enum.DIGIT_HYPHEN.getRegex()) || str.length() > 20){
      error_col_list.add(11);
    }
  }







  /** 
   ***************************************************************
   * @brief 現住所郵便番号
   * 
   * @details
   * -# 文字数が[15文字以内]であること。
   * -# [半角英数字＆ハイフン]のみで構成されている事。
   *
   * @param[in] csv_map 判定対象のデータが入ったマップリスト
   * @param[out] error_col_list エラーの列番号を書き込むリスト
   ***************************************************************
   */
  private void validAddrPostcode(Map<String, String> csv_map, List<Integer> error_col_list){
    String key = Member_Info_Enum.Addr_Postcode.getEnName();
    String str = csv_map.get(key) != null ? csv_map.get(key) : "";

    if(!str.matches(Regex_Enum.DIGIT_HYPHEN.getRegex()) || str.length() > 15){
      error_col_list.add(12);
    }
  }







  /** 
   ***************************************************************
   * @brief 現住所
   * 
   * @details
   * -# 文字数が[200文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   *
   * @param[in] csv_map 判定対象のデータが入ったマップリスト
   * @param[out] error_col_list エラーの列番号を書き込むリスト
   ***************************************************************
   */
  private void validAddr(Map<String, String> csv_map, List<Integer> error_col_list){
    String key = Member_Info_Enum.Addr.getEnName();
    String str = csv_map.get(key) != null ? csv_map.get(key) : "";

    if(!str.matches(Regex_Enum.ZENKAKU_ONLY_NULLOK.getRegex()) || str.length() > 200){
      error_col_list.add(13);
    }
  }







  /** 
   ***************************************************************
   * @brief 役職名
   * 
   * @details
   * -# 文字数が[50文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   *
   * @param[in] csv_map 判定対象のデータが入ったマップリスト
   * @param[out] error_col_list エラーの列番号を書き込むリスト
   ***************************************************************
   */
  private void validPosition(Map<String, String> csv_map, List<Integer> error_col_list){
    String key = Member_Info_Enum.Position.getEnName();
    String str = csv_map.get(key) != null ? csv_map.get(key) : "";

    if(!str.matches(Regex_Enum.ZENKAKU_ONLY_NULLOK.getRegex()) || str.length() > 50){
      error_col_list.add(14);
    }
  }








  /** 
   ***************************************************************
   * @brief 現役職着任日
   * 
   * @details
   * -# 日付文字列が[yyyyMMdd]の構成になっている事。
   *
   * @return 抽出した現役職着任日データ
   * 
   * @param[in] csv_map 判定対象のデータが入ったマップリスト
   * @param[out] error_col_list エラーの列番号を書き込むリスト
   * @param[out] flag_4 日付論理チェックの際に使用する真偽値フラグ
   ***************************************************************
   */
  private Date validArriDate(Map<String, String> csv_map, List<Integer> error_col_list, Boolean flag_4) throws ParseException {
    String key = Member_Info_Enum.Position_Arri_Date.getEnName();
    String str = csv_map.get(key) != null ? csv_map.get(key) : "";
    Date position_arri_date = null;

    if(!str.matches(Regex_Enum.DATE_NO_HYPHEN_NULLOK.getRegex())){
      error_col_list.add(15);
    }else{
      if(!str.equals("")){
        position_arri_date = this.parse_date.parse(str);
        flag_4 = true;
      }
    }

    return position_arri_date;
  }







  /** 
   ***************************************************************
   * @brief 職種名
   * 
   * @details
   * -# 文字数が[50文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   *
   * @param[in] csv_map 判定対象のデータが入ったマップリスト
   * @param[out] error_col_list エラーの列番号を書き込むリスト
   ***************************************************************
   */
  private void validJob(Map<String, String> csv_map, List<Integer> error_col_list){
    String key = Member_Info_Enum.Job.getEnName();
    String str = csv_map.get(key) != null ? csv_map.get(key) : "";

    if(!str.matches(Regex_Enum.ZENKAKU_ONLY_NULLOK.getRegex()) || str.length() > 50){
      error_col_list.add(16);
    }
  }







  /** 
   ***************************************************************
   * @brief 配属部署
   * 
   * @details
   * -# 文字数が[50文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   *
   * @param[in] csv_map 判定対象のデータが入ったマップリスト
   * @param[out] error_col_list エラーの列番号を書き込むリスト
   ***************************************************************
   */
  private void validAssignDept(Map<String, String> csv_map, List<Integer> error_col_list){
    String key = Member_Info_Enum.Assign_Dept.getEnName();
    String str = csv_map.get(key) != null ? csv_map.get(key) : "";

    if(!str.matches(Regex_Enum.ZENKAKU_ONLY_NULLOK.getRegex()) || str.length() > 50){
      error_col_list.add(17);
    }
  }







  /** 
   ***************************************************************
   * @brief 配属日
   * 
   * @details
   * -# 日付文字列が[yyyyMMdd]の構成になっている事。
   *
   * @return 抽出した配属日データ
   * 
   * @param[in] csv_map 判定対象のデータが入ったマップリスト
   * @param[out] error_col_list エラーの列番号を書き込むリスト
   * @param[out] flag_5 日付論理チェックの際に使用する真偽値フラグ
   ***************************************************************
   */
  private Date validAssignDate(Map<String, String> csv_map, List<Integer> error_col_list, Boolean flag_5) throws ParseException {
    String key = Member_Info_Enum.Assign_Date.getEnName();
    String str = csv_map.get(key) != null ? csv_map.get(key) : "";
    Date assign_date = null;

    if(!str.matches(Regex_Enum.DATE_NO_HYPHEN_NULLOK.getRegex())){
      error_col_list.add(18);
    }else{
      if(!str.equals("")){
        assign_date = this.parse_date.parse(str);
        flag_5 = true;
      }
    }

    return assign_date;
  }







  /** 
   ***************************************************************
   * @brief 担当楽器
   * 
   * @details
   * -# 文字数が[50文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   *
   * @param[in] csv_map 判定対象のデータが入ったマップリスト
   * @param[out] error_col_list エラーの列番号を書き込むリスト
   ***************************************************************
   */
  private void validInstCharge(Map<String, String> csv_map, List<Integer> error_col_list){
    String key = Member_Info_Enum.Inst_Charge.getEnName();
    String str = csv_map.get(key) != null ? csv_map.get(key) : "";

    if(!str.matches(Regex_Enum.ZENKAKU_ONLY_NULLOK.getRegex()) || str.length() > 50){
      error_col_list.add(19);
    }
  }







  /** 
   ***************************************************************
   * @brief その他コメント
   * 
   * @details
   * -# 文字数が[400文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   *
   * @param[in] csv_map 判定対象のデータが入ったマップリスト
   * @param[out] error_col_list エラーの列番号を書き込むリスト
   ***************************************************************
   */
  private void validOtherComment(Map<String, String> csv_map, List<Integer> error_col_list){
    String key = Member_Info_Enum.Other_Comment.getEnName();
    String str = csv_map.get(key) != null ? csv_map.get(key) : "";

    if(!str.matches(Regex_Enum.ZENKAKU_ONLY_NULLOK.getRegex()) || str.length() > 400){
      error_col_list.add(20);
    }
  }







  /** 
   ***************************************************************
   * @brief 「誕生日」と「入団日」の論理チェック
   * 
   * @details
   * -# 「誕生日」が「入団日」以前に設定されている事を判別。
   *
   * @param[in] flag_1 日付論理チェックの際に使用する真偽値フラグ
   * @param[in] flag_2 日付論理チェックの際に使用する真偽値フラグ
   * @param[in] birthday 検査対象誕生日
   * @param[in] join_date 検査対象入団日
   * @param[out] error_col_list エラーの列番号を書き込むリスト
   * 
   * @note 日付論理チェックの際の真偽値フラグとは、対象の日付が有効、
   * つまりNullやバリデーションエラーの状態ではなくて検査が可能だ
   * 状態であることを示すためにある。なので、検査対象の日付が全て
   * 有効状態になっていないと検査ができない。
   ***************************************************************
   */
  private void dateLogicCheck_BirthAndJoin(Boolean flag_1, 
                                           Boolean flag_2, 
                                           Date birthday, 
                                           Date join_date, 
                                           List<Integer> error_col_list){

    if(flag_1 && flag_2 && birthday != null && birthday.after(join_date)){
      error_col_list.add(6);
    }
  }







  /** 
   ***************************************************************
   * @brief 「入団日」と「退団日」の論理チェック
   * 
   * @details
   * -# 「入団日」が「退団日」以前に設定されている事を判別。
   *
   * @param[in] flag_2 日付論理チェックの際に使用する真偽値フラグ
   * @param[in] flag_3 日付論理チェックの際に使用する真偽値フラグ
   * @param[in] join_date 検査対象入団日
   * @param[in] ret_date 検査対象退団日
   * @param[out] error_col_list エラーの列番号を書き込むリスト
   * 
   * @note 日付論理チェックの際の真偽値フラグとは、対象の日付が有効、
   * つまりNullやバリデーションエラーの状態ではなくて検査が可能だ
   * 状態であることを示すためにある。なので、検査対象の日付が全て
   * 有効状態になっていないと検査ができない。
   ***************************************************************
   */
  private void dateLogicCheck_JoinAndRet(Boolean flag_2, 
                                         Boolean flag_3, 
                                         Date join_date, 
                                         Date ret_date, 
                                         List<Integer> error_col_list){

    if(flag_2 && flag_3 && join_date != null && join_date.after(ret_date)){
      error_col_list.add(7);
    }
  }







  /** 
   ***************************************************************
   * @brief 「入団日」と「現役職着任日」の論理チェック
   * 
   * @details
   * -# 「入団日」が「現役職着任日」以前に設定されている事を判別。
   *
   * @param[in] flag_2 日付論理チェックの際に使用する真偽値フラグ
   * @param[in] flag_4 日付論理チェックの際に使用する真偽値フラグ
   * @param[in] join_date 検査対象入団日
   * @param[in] position_arri_date 検査対象現役職着任日
   * @param[out] error_col_list エラーの列番号を書き込むリスト
   * 
   * @note 日付論理チェックの際の真偽値フラグとは、対象の日付が有効、
   * つまりNullやバリデーションエラーの状態ではなくて検査が可能だ
   * 状態であることを示すためにある。なので、検査対象の日付が全て
   * 有効状態になっていないと検査ができない。
   ***************************************************************
   */
  private void dateLogicCheck_JoinAndPosition(Boolean flag_2, 
                                              Boolean flag_4, 
                                              Date join_date, 
                                              Date position_arri_date, 
                                              List<Integer> error_col_list){

    if(flag_2 && flag_4 && join_date != null && join_date.after(position_arri_date)){
      error_col_list.add(15);
    }
  }








  /** 
   ***************************************************************
   * @brief 「入団日」と「配属日」の論理チェック
   * 
   * @details
   * -# 「入団日」が「配属日」以前に設定されている事を判別。
   *
   * @param[in] flag_2 日付論理チェックの際に使用する真偽値フラグ
   * @param[in] flag_5 日付論理チェックの際に使用する真偽値フラグ
   * @param[in] join_date 検査対象入団日
   * @param[in] assign_date 検査対象配属日
   * @param[out] error_col_list エラーの列番号を書き込むリスト
   * 
   * @note 日付論理チェックの際の真偽値フラグとは、対象の日付が有効、
   * つまりNullやバリデーションエラーの状態ではなくて検査が可能だ
   * 状態であることを示すためにある。なので、検査対象の日付が全て
   * 有効状態になっていないと検査ができない。
   ***************************************************************
   */
  private void dateLogicCheck_JoinAndAssign(Boolean flag_2, 
                                            Boolean flag_5, 
                                            Date join_date, 
                                            Date assign_date, 
                                            List<Integer> error_col_list){

    if(flag_2 && flag_5 && join_date != null && join_date.after(assign_date)){
      error_col_list.add(18);
    }
  }

  /** @} */
}