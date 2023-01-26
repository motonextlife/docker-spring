/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.Form.InjectValids.AggregationValid
 * 
 * @brief DIが必要なバリデーション項目を切り分けたクラスを格納するパッケージ。
 * 
 * @details
 * - 一つのクラスにDIが必要なバリデーションを実装すると、DIするクラスの量が多くなり、一つのクラスの
 * 責務が多くなりすぎてしまう。それを防ぐため、一つのクラス当たりのDIクラス数を減らすために切り分けた
 * クラスを格納している。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.InjectValids.AggregationValid;






/** 
 **************************************************************************************
 * @file InjectEnvValid.java
 * @brief DIが必要なフォームバリデーションの項目に関して、[環境変数からの値が必要な項目]の
 * 判定を行うフォームバリデーションクラスを格納するファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import com.springproject.dockerspring.CommonEnum.Environment_Config;
import com.springproject.dockerspring.CommonEnum.UtilEnum.Datatype_Enum;
import com.springproject.dockerspring.Form.ValidTable_Enum;
import com.springproject.dockerspring.Repository.SubjectEnum.Audio_Data_Subject;
import com.springproject.dockerspring.Repository.SubjectEnum.Common_History_Subject;
import com.springproject.dockerspring.Repository.SubjectEnum.Facility_Photo_Subject;
import com.springproject.dockerspring.Repository.SubjectEnum.Facility_Subject;
import com.springproject.dockerspring.Repository.SubjectEnum.Member_Info_Subject;
import com.springproject.dockerspring.Repository.SubjectEnum.Musical_Score_Subject;
import com.springproject.dockerspring.Repository.SubjectEnum.Score_Pdf_Subject;
import com.springproject.dockerspring.Repository.SubjectEnum.Sound_Source_Subject;
import com.springproject.dockerspring.Repository.SubjectEnum.System_User_Subject;
import com.springproject.dockerspring.Repository.SubjectEnum.Usage_Authority_Subject;

import lombok.RequiredArgsConstructor;









/** 
 **************************************************************************************
 * @brief DIが必要なフォームバリデーションの項目に関して、[環境変数からの値が必要な項目]の
 * 判定を行うフォームバリデーションクラス
 * 
 * @details 
 * - このクラスのようにDIが必要なバリデーションの項目を分離した理由としては、他のアノテーション
 * ベースのバリデーションのようにアノテーション内でDIを行おうとすると、正常にDIが行われず
 * テストができない為、動作が保証できないからである。
 * - そのため、確実に動作し品質の保証が可能な方法として、通常のフォームクラスとは分離し、
 * コントローラー内で別途バリデーションを行う方式として、このクラスを設けた。
 * 
 * @par 使用アノテーション
 * - @Component
 * - @RequiredArgsConstructor(onConstructor = @__(@Autowired))
 * 
 * @see Environment_Config
 **************************************************************************************
 */ 
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InjectEnvValid{
  
  private final Environment_Config config;









  /** 
   *******************************************************************************
   * @brief データが格納されたリストの長さを確認する。
   * 
   * @details　
   * - 使用の用途としては、データ削除の対象シリアルナンバーが格納されたリストを確認し、
   * 一回の処理での削除数制限を設ける際などに用いる。また、バイナリデータの一回の
   * 処理での受付数を制限する際にも用いる。
   * - さらに、リスト内で同じデータが重複していないことも確認可能。
   * - なお、レスポンスを早くするため、この処理を実行する前にすでに他の箇所でバリデーション
   * エラーが発生しておりエラーが登録されている場合は、処理を行わずスルーする。
   * 
   * @return 登録したエラーの情報
   * 
   * @param[in] list 検査対象となるリスト
   * @param[in] bind_data エラー情報を登録するためのインスタンス
   * @param[in] field_name エラー情報を登録する対象のフィールド変数名
   * @param[in] single 「True」の場合は、リストの長さは常に1つまで、「False」の場合は
   * リストの長さは環境変数の指定の数に従う、といった制限を変更する。
   * @param[in] type 検査対象となるデータのデータタイプを指定する。なお、「Null」だった
   * 場合は共通のリスト数制限値が適用され、他のバイナリデータのタイプ指定だと、そのデータ型の
   * ファイルの制限数を適用する。
   * 
   * @par 大まかな処理の流れ
   * -# 指定されたデータタイプに従って、適用する制限数の値を環境変数から取り出す。
   * -# 制限数が常に1つと制限されていれば、制限数を1に上書きする。
   * -# データに重複がないかつ、リスト長さが規定値以内に収まっていることを判別する。
   * -# 条件に当てはまらなければ、指定されたフィールドにエラーインスタンスにエラーを
   * 登録して戻り値とする。条件に合致していれば、空のエラーインスタンスを返す。
   *******************************************************************************
   */
  public <T> BindingResult dataListLimit(List<T> list, 
                                         BindingResult bind_data, 
                                         String field_name, 
                                         Boolean single, 
                                         Datatype_Enum type) {

    if(bind_data.hasErrors()){
      return bind_data;
    }

    int limit = 0;

    if(type == null){
      limit = this.config.getNormal_list_size();
    }else{
      switch(type){
        case AUDIO:
          limit = this.config.getAudio_list_size();
          break;
        case PHOTO:
          limit = this.config.getPhoto_list_size();
          break;
        case PDF:
          limit = this.config.getPdf_list_size();
          break;
        default:
          throw new IllegalArgumentException("Error location [InjectEnvValid:dataListLimit]");
      }
    }

    limit = single ? 1 : limit;
    
    if(list.size() <= limit && list.size() == list.stream().distinct().toList().size()){ 
      return bind_data;
    }else{
      bind_data.addError(new FieldError(bind_data.getObjectName(), field_name, "dataListLimit"));
      return bind_data;
    }
  }













  /** 
   *******************************************************************************
   * @brief 入力されたバイナリデータの正当性を確認する。
   * 
   * @details　
   * - 使用用途としては、入力されたバイナリデータのデータ量や拡張子、MIMEタイプが条件に
   * 合致しているかを判別する。
   * - なお、レスポンスを早くするため、この処理を実行する前にすでに他の箇所でバリデーション
   * エラーが発生しておりエラーが登録されている場合は、処理を行わずスルーする。
   * - また、CSVファイルなどでは、データ量が0バイトで来るといったパターンも十分あり得るため、
   * 全くデータが無いバイナリデータも受付対象外とする。
   * - バイナリデータのMIMEタイプを判別するライブラリに関しては、[Apache Tika]を用いる。
   * これによって、バイナリデータのタイプを拡張子だけで判断せず、バイト配列から厳密にMIMEタイプを
   * 判別し、不正なバイナリデータがシステム内に入ることを防ぐ。
   * 
   * @return 登録したエラーの情報
   * 
   * @param[in] file 検査対象となるリスト
   * @param[in] kind 検査対象となるデータのタイプ
   * @param[in] bind_data エラー情報を登録するためのインスタンス
   * @param[in] field_name エラー情報を登録する対象のフィールド変数名
   * 
   * @par 大まかな処理の流れ
   * -# 入力ファイルからストリームを読み込み、MIMEタイプを取得する。
   * -# ファイル名から拡張子のみを抜き出す。
   * -# 指定されたデータタイプに対応する環境変数を取得する。
   * -# 入力されたデータのMIMEタイプと拡張子、データ量が環境変数の制限値に収まっているか
   * どうかを判定する。
   * -# 入力されたデータのデータ量が、0バイトで無い事を判定する。
   * -# 条件に当てはまらなければ、指定されたフィールドにエラーインスタンスにエラーを
   * 登録して戻り値とする。条件に合致していれば、空のエラーインスタンスを返す。
   *******************************************************************************
   */
  public BindingResult matchFileCheck(MultipartFile file, 
                                      Datatype_Enum kind, 
                                      BindingResult bind_data, 
                                      String field_name) throws IOException {

    if(bind_data.hasErrors() || file == null){
      return bind_data;
    }

    String type = "";
    try(BufferedInputStream bis = new BufferedInputStream(file.getInputStream())){
      type = new Tika().detect(bis);
    }

    String name = file.getOriginalFilename();
    name = name == null ? "" : name;
    int idx = name.lastIndexOf(".");
    name = idx != -1 ? name.substring(idx + 1) : "";
    
    Boolean judge = false;

    switch(kind){
      case AUDIO:
        judge = type.equals(Datatype_Enum.AUDIO.getMimetype()) 
             && file.getSize() <= this.config.getAudio_limit()
             && name.equals(Datatype_Enum.AUDIO.getExtension());
        break;

      case CSV:
        judge = type.equals(Datatype_Enum.CSV.getMimetype()) 
             && file.getSize() <= this.config.getCsv_limit()
             && name.equals(Datatype_Enum.CSV.getExtension());
        break;

      case PHOTO:
        judge = type.equals(Datatype_Enum.PHOTO.getMimetype()) 
             && file.getSize() <= this.config.getPhoto_limit()
             && name.equals(Datatype_Enum.PHOTO.getExtension());
        break;

      case PDF:
        judge = type.equals(Datatype_Enum.PDF.getMimetype()) 
             && file.getSize() <= this.config.getPdf_limit()
             && name.equals(Datatype_Enum.PDF.getExtension());
        break;

      case ZIP:
        judge = type.equals(Datatype_Enum.ZIP.getMimetype()) 
             && file.getSize() <= this.config.getZip_limit()
             && name.equals(Datatype_Enum.ZIP.getExtension());
        break;

      default:
        throw new IllegalArgumentException("Error location [InjectEnvValid:matchFileCheck]");
    }

    if(file.getSize() == 0){
      judge = false;
    }

    if(judge){
      return bind_data;
    }else{
      bind_data.addError(new FieldError(bind_data.getObjectName(), field_name, "match" + kind.name()));
      return bind_data;
    }
  }















  /** 
   *******************************************************************************
   * @brief 検索用フォームにおいて入力されたページ数指定の数値の正当性と、入力された
   * 検索種別の正当性を確認する。
   * 
   * @details　
   * - 使用用途としては、入力されたページ指定数値が使用可能な範囲に収まっているか、また
   * 渡された検索種別が検索対象の機能に存在するかを確認する。
   * - なお、レスポンスを早くするため、この処理を実行する前にすでに他の箇所でバリデーション
   * エラーが発生しておりエラーが登録されている場合は、処理を行わずスルーする。
   * 
   * @return 登録したエラーの情報
   * 
   * @param[in] page_num 指定されたページ数
   * @param[in] subject 指定された検索種別
   * @param[in] table 検索対象となるテーブル名
   * @param[in] bind_data エラー情報を登録するためのインスタンス
   * 
   * @note ページ数の制限値は以下の数式で求められる。
   * (環境変数指定のオフセット値 * 指定ページ数) <= 環境変数の最大出力可能検索結果数
   * 
   * @par 大まかな処理の流れ
   * -# ページ数の制限数を判定する。
   * -# 指定されたテーブルを元に、検索種別の列挙型をインポートし、その列挙型内に
   * 指定された検索種別が存在するかを判定する。
   * -# 条件に当てはまらなければ、指定されたフィールドにエラーインスタンスにエラーを
   * 登録して戻り値とする。条件に合致していれば、空のエラーインスタンスを返す。
   *******************************************************************************
   */
  public BindingResult pageNumAndSubjectCheck(Integer page_num, 
                                              String subject, 
                                              ValidTable_Enum table, 
                                              BindingResult bind_data) {

    if(bind_data.hasErrors()){
      return bind_data;
    }

    Boolean page_num_judge = false;
    Boolean subject_judge = false;

    page_num_judge = (page_num * this.config.getOffset()) <= this.config.getMax();

    switch(table){
      case Audio_Data_History:
      case Facility_History:
      case Facility_Photo_History:
      case Member_Info_History:
      case Musical_Score_History:
      case Score_Pdf_History:
      case Sound_Source_History:
        subject_judge = Arrays.stream(Common_History_Subject.values())
                              .parallel()
                              .filter(s -> s.name().equals(subject))
                              .findFirst()
                              .isPresent();
        break;

      case Audio_Data:
        subject_judge = Arrays.stream(Audio_Data_Subject.values())
                        .parallel()
                        .filter(s -> s.name().equals(subject))
                        .findFirst()
                        .isPresent();
        break;

      case Facility_Photo:
        subject_judge = Arrays.stream(Facility_Photo_Subject.values())
                        .parallel()
                        .filter(s -> s.name().equals(subject))
                        .findFirst()
                        .isPresent();
        break;

      case Facility:
        subject_judge = Arrays.stream(Facility_Subject.values())
                        .parallel()
                        .filter(s -> s.name().equals(subject))
                        .findFirst()
                        .isPresent();
        break;

      case Member_Info:
        subject_judge = Arrays.stream(Member_Info_Subject.values())
                        .parallel()
                        .filter(s -> s.name().equals(subject))
                        .findFirst()
                        .isPresent();
        break;

      case Musical_Score:
        subject_judge = Arrays.stream(Musical_Score_Subject.values())
                        .parallel()
                        .filter(s -> s.name().equals(subject))
                        .findFirst()
                        .isPresent();
        break;

      case Score_Pdf:
        subject_judge = Arrays.stream(Score_Pdf_Subject.values())
                        .parallel()
                        .filter(s -> s.name().equals(subject))
                        .findFirst()
                        .isPresent();
        break;

      case Sound_Source:
        subject_judge = Arrays.stream(Sound_Source_Subject.values())
                        .parallel()
                        .filter(s -> s.name().equals(subject))
                        .findFirst()
                        .isPresent();
        break;

      case System_User:
        subject_judge = Arrays.stream(System_User_Subject.values())
                        .parallel()
                        .filter(s -> s.name().equals(subject))
                        .findFirst()
                        .isPresent();
        break;

      case Usage_Authority:
        subject_judge = Arrays.stream(Usage_Authority_Subject.values())
                        .parallel()
                        .filter(s -> s.name().equals(subject))
                        .findFirst()
                        .isPresent();
        break;

      default:
        throw new IllegalArgumentException("Error location [InjectEnvValid:pageNumAndSubjectCheck]");
    }


    if(page_num_judge && subject_judge){
      return bind_data;
    }else{
      if(page_num_judge){
        bind_data.addError(new FieldError(bind_data.getObjectName(), "page_num", "pageNumAndSubjectCheck"));
      }

      if(subject_judge){
        bind_data.addError(new FieldError(bind_data.getObjectName(), "subject", "pageNumAndSubjectCheck"));
      }
      
      return bind_data;
    }
  }









  /** 
   *******************************************************************************
   * @brief データの新規追加や更新時のシリアルナンバーの状態を判別する。
   * 
   * @details　
   * - 使用用途としては、新規追加時にはシリアルナンバーは「Null」、更新時には更新対象の
   * データのシリアルナンバーが存在することを確認する。
   * - Spring Data JDBCの仕様上、新規追加の際は主キーを「Null」とすることで挿入扱いに
   * なるので、その仕様に沿った判定である。
   * 
   * @return 登録したエラーの情報
   * 
   * @param[in] serial_num 入力されたシリアルナンバー
   * @param[in] bind_data エラー情報を登録するためのインスタンス
   * @param[in] update 更新の際は「True」、新規追加の際は「False」を指定。
   * 
   * @par 大まかな処理の流れ
   * -# 引数の指定で、更新処理の際はシリアルナンバーが存在し、新規追加処理の際は存在しない
   * 事を確認する。
   * -# 条件に当てはまらなければ、指定されたフィールドにエラーインスタンスにエラーを
   * 登録して戻り値とする。条件に合致していれば、空のエラーインスタンスを返す。
   *******************************************************************************
   */
  public BindingResult insertOrUpdateCheck(Integer serial_num, BindingResult bind_data, Boolean update) {

    if(bind_data.hasErrors()){
      return bind_data;
    }

    Boolean judge = false;

    if(update){
      judge = serial_num != null ? true : false;
    }else{
      judge = serial_num == null ? true : false;
    }

    if(judge){
      return bind_data;
    }else{
      bind_data.addError(new FieldError(bind_data.getObjectName(), "serial_num", "insertOrUpdateCheck"));
      return bind_data;
    }
  }
}
