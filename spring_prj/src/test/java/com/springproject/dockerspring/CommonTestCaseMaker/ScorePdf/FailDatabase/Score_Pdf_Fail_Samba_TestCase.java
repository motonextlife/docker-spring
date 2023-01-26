/** 
 **************************************************************************************
 * @file Score_Pdf_Fail_Samba_TestCase.java
 * @brief 主に[楽譜データ情報]機能のテストにおいて、ファイルサーバーとの通信テストの際に
 * ファイルサーバーにエラーとなる初期データをセットアップするクラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.ScorePdf.FailDatabase;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.springproject.dockerspring.CommonTestCaseMaker.Configure.ConfigureFactory;







/** 
 **************************************************************************************
 * @brief 主に[楽譜データ情報]機能のテストにおいて、ファイルサーバーとの通信テストの際に
 * ファイルサーバーににエラーとなる初期データをセットアップするクラス。
 * 
 * @details 
 * - ファイルサーバーへのテスト用データの登録の他、使用したテストケースデータの削除を行う。
 * - なお、このクラスは通常データ用と履歴データ用で共用で用いる。
 * 
 * @par 使用アノテーション
 * - @Component
 * 
 * @see ConfigureFactory
 **************************************************************************************
 */ 
@Component
public class Score_Pdf_Fail_Samba_TestCase{

  private final ConfigureFactory config_factory;

  //! 基本となるテストケースのファイルパス。これに、テストケースのバイナリファイル名を連結して使用。
  private final String base_file_path = "src/main/resources/TestCaseFile/ScorePdf/BlobTestCaseData/form/";







  /** 
   **********************************************************************************************
   * @brief 使用する接続情報の環境変数のDIを行う。
   * 
   * @details
   * - ファイルサーバーのセットアップに必要なクラスを、インジェクションする。
   * 
   * @param[in] config_factory インジェクションを行う、ファイルサーバーの接続情報の設定ファイル。
   * 
   * @par 処理の大まかな流れ
   * -# 必要な設定ファイルをインジェクションし、フィールド変数に格納する。
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  @Autowired
  public Score_Pdf_Fail_Samba_TestCase(ConfigureFactory config_factory) throws IOException{
    this.config_factory = config_factory;
  }
  
  







  /** 
   **********************************************************************************************
   * @brief 通常データのファイルサーバーの検査時に用いるエラーテストケースのバイナリデータを保存する。
   * 
   * @details
   * - 指定されたテストケースのバイナリファイルを読み取って、ファイルサーバー内のディレクトリに保存する。
   * - 同時に指定されたファイル名で、ファイルサーバーに保存する。
   * 
   * @param[in] testcase_file ファイルサーバーに保存する、テストケースのバイナリデータのファイル名。
   * @param[in] filename ファイルサーバーに保存する際に付けるファイル名。
   * 
   * @return ファイルサーバーへ保存したバイナリデータのハッシュ値
   * 
   * @par 処理の大まかな流れ
   * -# 決定済みのテストケースの基本ファイルパスに、指定されたテストケースファイル名を連結。
   * -# 決定済みのファイルサーバーの保存先ディレクトリのファイルパスに、保存時の指定ファイル名を連結。
   * -# 連結した二つのファイルパスを、継承元クラスの共通のファイルサーバーセットアップ処理に渡す。
   * -# 処理が終わって共通処理から返却されてきたハッシュ値を戻り値とする。
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  public String failSambaSetup(String testcase_file, String filename) throws IOException{
    return this.config_factory.sambaOutput(this.base_file_path + testcase_file, "ScorePdf/normal/" + filename);
  }










  /** 
   **********************************************************************************************
   * @brief 通常データのファイルサーバーテスト時に使用したデータをすべて削除し、ファイルサーバをリセットする。
   * 
   * @details
   * - 指定されたファイルサーバー内のディレクトリの中身を完全に削除する。
   * 
   * @par 処理の大まかな流れ
   * -# 決定済みのファイルサーバーのディレクトリのファイルパスを、継承元クラスの共通のファイルサーバー
   * リセット処理に渡す。
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  public void failSambaReset() throws IOException{
    this.config_factory.sambaClear("ScorePdf/normal/");
  }







  /** 
   **********************************************************************************************
   * @brief 履歴データのファイルサーバーの検査時に用いるエラーテストケースのバイナリデータを保存する。
   * 
   * @details
   * - 指定されたテストケースのバイナリファイルを読み取って、ファイルサーバー内のディレクトリに保存する。
   * - 同時に指定されたファイル名で、ファイルサーバーに保存する。
   * 
   * @param[in] testcase_file ファイルサーバーに保存する、テストケースのバイナリデータのファイル名。
   * @param[in] filename ファイルサーバーに保存する際に付けるファイル名。
   * 
   * @return ファイルサーバーへ保存したバイナリデータのハッシュ値
   * 
   * @par 処理の大まかな流れ
   * -# 決定済みのテストケースの基本ファイルパスに、指定されたテストケースファイル名を連結。
   * -# 決定済みのファイルサーバーの保存先ディレクトリのファイルパスに、保存時の指定ファイル名を連結。
   * -# 連結した二つのファイルパスを、継承元クラスの共通のファイルサーバーセットアップ処理に渡す。
   * -# 処理が終わって共通処理から返却されてきたハッシュ値を戻り値とする。
   * 
   * @note 継承元の共通処理では、内部的にデータを圧縮してファイルサーバーに格納するが、戻ってくるハッシュ値は
   * 「圧縮前のデータのハッシュ値」なので注意する事。なお、要件上それで問題ない。
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  public String historyFailSambaSetup(String testcase_file, String filename) throws IOException{
    return this.config_factory.sambaHistoryOutput(this.base_file_path + testcase_file, "ScorePdf/history/" + filename);
  }








  /** 
   **********************************************************************************************
   * @brief 履歴データのファイルサーバーテスト時に使用したデータをすべて削除し、ファイルサーバをリセットする。
   * 
   * @details
   * - 指定されたファイルサーバー内のディレクトリの中身を完全に削除する。
   * 
   * @par 処理の大まかな流れ
   * -# 決定済みのファイルサーバーのディレクトリのファイルパスを、継承元クラスの共通のファイルサーバー
   * リセット処理に渡す。
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  public void historyFailSambaReset() throws IOException{
    this.config_factory.sambaClear("ScorePdf/history/");
  }
}
