/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.FileIO.ZipProcess
 * 
 * @brief ファイル入出力機能のうち、[ZIP圧縮ファイルの入出力]に関係する処理を格納したパッケージ
 * 
 * @details
 * - このパッケージは、受け取った圧縮ファイルを解凍して中のデータを抽出したり、既にシステムに保存
 * されているデータを圧縮ファイルに格納してまとめて出力する機能を提供する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.FileIO.ZipProcess;






/** 
 **************************************************************************************
 * @file Audio_Data_ZipImpl.java
 * @brief 主に[音源データ管理]機能で使用する、バイナリデータの圧縮ファイルの作成に関する機能を
 * 実装するクラスを格納したファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

import net.lingala.zip4j.exception.ZipException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.springproject.dockerspring.CommonEnum.Environment_Config;
import com.springproject.dockerspring.FileIO.CompInterface.Audio_Data_Zip;
import com.springproject.dockerspring.FileIO.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.Form.BlobImplForm.FileEntity;

import lombok.RequiredArgsConstructor;










/** 
 **************************************************************************************
 * @brief 主に[音源データ管理]機能で使用する、バイナリデータの圧縮ファイルの作成に関する機能を
 * 実装するクラス
 * 
 * @details 
 * - 処理の大部分は、このクラスにDIする共通処理クラスに任せることで、こちらの特定クラスの
 * 実装内容を大幅に減らしている。
 * - このクラスで用いる環境変数のインジェクションは、Lombokによりコンストラクタインジェクションを
 * 行うことで実現する。
 * 
 * @par 使用アノテーション
 * - @Component
 * - @RequiredArgsConstructor(onConstructor = @__(@Autowired))
 * 
 * @see Audio_Data_Zip
 * @see Common_Zip
 **************************************************************************************
 */ 
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Audio_Data_ZipImpl implements Audio_Data_Zip{

  private final Environment_Config config;
  private final Common_Zip common_zip;









  /** 
   **********************************************************************************************
   * @brief 読み込んだZIPファイルからデータを抽出し、専用エンティティに格納した抽出結果のファイルリストを
   * 返す。
   * 
   * @details
   * - 抽出の際に用いる、ファイル制限数とデータ制限値に関しては、[音声ファイル]の基準を用いて、その値を
   * 共通クラスに渡す。
   *
   * @param[in] zipfile 抽出対象のZIPファイル
   * 
   * @note このメソッドで使用する共通クラスの共通メソッドは[extractZipCom]である。詳しい処理内容は
   * そちらを参照の事。
   *
   * @throw IOException
   * @throw ZipException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  @Override
  public List<FileEntity> extractZip(MultipartFile zipfile) throws IOException, ZipException, InputFileDifferException {

    int count = this.config.getAudio_list_size();
    int size = this.config.getAudio_limit();

    try{
      return this.common_zip.extractZipCom(zipfile, count, size);
    }catch(ZipException e){
      throw new IOException("Error location [Audio_Data_ZipImpl:extractZip(ZipException)]" + "\n" + e);
    }catch(IOException e){
      throw new ZipException("Error location [Audio_Data_ZipImpl:extractZip(IOException)]" + "\n" + e);
    }
  }


  




  



  /** 
   **********************************************************************************************
   * @brief ファイルサーバーから対象のファイルを取り出し、圧縮ファイルとして出力する。
   * 返す。
   * 
   * @details
   * - ファイルサーバーからのデータ取得の処理は、関数型インターフェースとして引数で渡し、その中で実行する。
   * これによって、ファイルサーバー通信処理とファイル圧縮処理を明確に分けることが可能になる。
   *
   * @param[in] samba_generate ファイルサーバーからのデータ取得の処理の関数
   * 
   * @note このメソッドで使用する共通クラスの共通メソッドは[outputZipCom]である。詳しい処理内容は
   * そちらを参照の事。
   *
   * @throw IOException
   * @throw ZipException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  @Override
  public File outputZip(Supplier<List<FileEntity>> generate) throws ZipException, IOException, InputFileDifferException{

    try{
      return this.common_zip.outputZipCom(generate);
    }catch(ZipException e){
      throw new ZipException("Error location [Audio_Data_ZipImpl:outputZip(ZipException)]" + "\n" + e);
    }catch(IOException e){
      throw new IOException("Error location [Audio_Data_ZipImpl:outputZip(IOException)]" + "\n" + e);
    }
  }
}
