/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.Form.BlobImplForm
 * 
 * @brief バリデーション機能のうち、バイナリデータのバリデーションに関する機能を格納した
 * パッケージ
 * 
 * @details
 * - このパッケージでは、ファイルサーバーからとってきたバイナリデータや、圧縮ファイルから抽出
 * したバイナリデータのバリデーションを行う機能を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.BlobImplForm;






/** 
 **************************************************************************************
 * @file Audio_Data_Zip_FormImpl.java
 * @brief 主に[音源データ管理]機能で使用する、ZIPから取り出したバイナリデータをバリデーション
 * するクラスを格納するファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.dialect.Escaper;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import com.springproject.dockerspring.CommonEnum.Environment_Config;
import com.springproject.dockerspring.CommonEnum.UtilEnum.Datatype_Enum;
import com.springproject.dockerspring.FileIO.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.Form.FormInterface.Audio_Data_Zip_Form;

import lombok.RequiredArgsConstructor;










/** 
 **************************************************************************************
 * @brief 主に[音源データ管理]機能で使用する、ZIPから取り出したバイナリデータをバリデーション
 * するクラス
 * 
 * @details 
 * - このクラスで用いる環境変数のインジェクションは、Lombokによりコンストラクタインジェクションを
 * 行うことで実現する。
 * - バイナリデータのMIMEタイプを判別するライブラリに関しては、[Apache Tika]を用いる。
 * これによって、バイナリデータのタイプを拡張子だけで判断せず、バイト配列から厳密にMIMEタイプを
 * 判別し、不正なバイナリデータがシステム内に入ることを防ぐ。
 * 
 * @par 使用アノテーション
 * - @Component
 * - @RequiredArgsConstructor(onConstructor = @__(@Autowired))
 * 
 * @see Audio_Data_Zip_Form
 * @see FileEntity
 **************************************************************************************
 */ 
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Audio_Data_Zip_FormImpl implements Audio_Data_Zip_Form{

  private final Environment_Config config;









  /** 
   **********************************************************************************************
   * @brief 受け取ったデータの拡張子やMIMEタイプ、データサイズやファイル数のチェックを行う。
   * 
   * @details
   * - 実際のチェック処理は[sequentialCheck]で順次処理で実行する。
   * - 処理の途中でバリデーションエラーが発生した場合、直ちに処理を終了する。
   *
   * @param[in] input_list 検査対象のファイルリスト
   * 
   * @par 大まかな処理の流れ
   * -# 引数で渡されたファイルのリストのファイル数を確認する。環境変数で指定された数を上回っている場合は
   * 例外を出し、処理を終了する。
   * -# リストからファイルを一つづつ取り出し、検査用メソッドに渡して検査を実行する。
   * -# 処理中にエラーが発生した場合は、引数で渡されたファイルをすべて削除し、処理を終了する。
   * 
   * @see FileEntity
   *
   * @throw InputFileDifferException
   * @throw IOException
   **********************************************************************************************
   */
  @Override
  public void blobChecker(List<FileEntity> input_list) throws InputFileDifferException, IOException {

    try{
      if(input_list.size() > this.config.getAudio_list_size()){
        throw new InputFileDifferException("over list size");
      }

      for(FileEntity ent: input_list){
        sequentialCheck(ent.getFilename(), ent.getTmpfile());
      }

    }catch(InputFileDifferException e){
      for(FileEntity item: input_list){
        Files.deleteIfExists(item.getTmpfile().toPath());
      }
      throw new InputFileDifferException("\n" + e);

    }catch(IOException e){
      for(FileEntity item: input_list){
        Files.deleteIfExists(item.getTmpfile().toPath());
      }
      throw new IOException("Error location [Audio_Data_Zip_FormImpl:blobChecker]" + "\n" + e);
    }
  }





  



  /** 
   **********************************************************************************************
   * @brief 実際にチェック処理を順次処理で行う。
   * 
   * @details
   * - この機能で検査する項目としては、主にファイル名のXSSに対する安全性の確認や、拡張子＆MIMEタイプの
   * チェック、データサイズの確認などを行う。
   * - 検査対象のファイル名と、ファイル本体を渡すことで、このシステムが受け入れ可能なデータかどうかを判断。
   * - なお、少しでもフロント側へのレスポンスを早くするため、一つでもエラーが発生した後続の検査処理は
   * 行わず、直ちに例外を発生させ処理を中断する。
   * - ファイルチェックに関して、チェック基準は環境変数の値に従う。また、データ型に関しては[音声データ]の
   * 基準に従う。
   * 
   * @param[in] filename 検査対象のファイル名
   * @param[in] file 検査対象のファイルの本体
   * 
   * @par 大まかな処理の流れ
   * -# ファイル名の長さが50文字以内に収まっていることを確認する。
   * -# HTMLエスケープし、エスケープ後の文字列を比較。変化があれば不合格。
   * -# SQLエスケープし、エスケープ後の文字列を比較。変化があれば不合格。
   * -# 渡されたファイル名を分析し、拡張子が所定の物か確認。
   * -# 渡されたバイト配列を分析し、MIMEタイプが所定の物か判別。
   * -# データサイズが所定の数値に収まっているか判別。
   * -# 渡されたファイル名の中に、ファイルパス構成文字が含まれていないか確認。
   * -# ファイル名にヌル文字が含まれていないか確認する。
   * 
   * @note ファイル本体と、ファイル名が分離している理由としては、ファイルサーバーやローカルファイルに
   * 保存する際のディレクトリトラバーサル攻撃のリスクを下げるため、分離してデータベースとファイルサーバーに
   * データを保管しているためである。
   * 
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  @Override
  public void sequentialCheck(String filename, File file) throws InputFileDifferException, IOException{

    try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))){

      if(filename.length() > 50){
        throw new InputFileDifferException("filename overflow");
      }

      String html_escape = HtmlUtils.htmlEscape(filename, "UTF-8");
      if(!filename.equals(html_escape)){
        throw new InputFileDifferException("impurities html");
      }

      String sql_escape = Escaper.of('\\').escape(filename);
      if(!filename.equals(sql_escape)){
        throw new InputFileDifferException("impurities sql");
      }

      int idx = filename.lastIndexOf(".");
      String ext_name = idx != -1 ? filename.substring(idx + 1) : "";
      if(!ext_name.equals(Datatype_Enum.AUDIO.getExtension())){
        throw new InputFileDifferException("missing extension");
      }

      Tika tika = new Tika();
      String mime = tika.detect(bis);
      if(!mime.equals(Datatype_Enum.AUDIO.getMimetype())){
        throw new InputFileDifferException("missing mimetype");
      }

      if(file.length() > this.config.getAudio_limit()){
        throw new InputFileDifferException("datasize overflow");
      }

      String filepath = StringUtils.removeEnd(filename, "." + ext_name);
      if(filepath.contains("/") || filepath.contains(".") || filepath.contains("\\")|| filepath.contains("~")){
        throw new InputFileDifferException("impurities filepath string");
      }

      if(filepath.contains("\0")){
        throw new InputFileDifferException("impurities null string");
      }

    }catch(IOException e){
      throw new IOException("Error location [Audio_Data_Zip_FormImpl:sequentialCheck]" + "\n" + e);
    }
  }
}