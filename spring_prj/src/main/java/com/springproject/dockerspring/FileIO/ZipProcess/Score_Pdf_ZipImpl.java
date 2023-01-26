/** 
 **************************************************************************************
 * @file Score_Pdf_ZipImpl.java
 * @brief 主に[楽譜データ管理]機能で使用する、バイナリデータの圧縮ファイルの作成に関する機能を
 * 実装するクラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.FileIO.ZipProcess;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

import net.lingala.zip4j.exception.ZipException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.springproject.dockerspring.CommonEnum.Environment_Config;
import com.springproject.dockerspring.FileIO.CompInterface.Score_Pdf_Zip;
import com.springproject.dockerspring.FileIO.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.Form.BlobImplForm.FileEntity;

import lombok.RequiredArgsConstructor;








/** 
 **************************************************************************************
 * @brief 主に[楽譜データ管理]機能で使用する、バイナリデータの圧縮ファイルの作成に関する機能を
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
 * @see Score_Pdf_Zip
 * @see Common_Zip
 **************************************************************************************
 */ 
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Score_Pdf_ZipImpl implements Score_Pdf_Zip{

  private final Environment_Config config;
  private final Common_Zip common_zip;









  /** 
   **********************************************************************************************
   * @brief 読み込んだZIPファイルからデータを抽出し、専用エンティティに格納した抽出結果のファイルリストを
   * 返す。
   * 
   * @details
   * - 抽出の際に用いる、ファイル制限数とデータ制限値に関しては、[PDFファイル]の基準を用いて、その値を
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

    int count = this.config.getPdf_list_size();
    int size = this.config.getPdf_limit();

    try{
      return this.common_zip.extractZipCom(zipfile, count, size);
    }catch(ZipException e){
      throw new IOException("Error location [Score_Pdf_ZipImpl:extractZip(ZipException)]" + "\n" + e);
    }catch(IOException e){
      throw new ZipException("Error location [Score_Pdf_ZipImpl:extractZip(IOException)]" + "\n" + e);
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
  public File outputZip(Supplier<List<FileEntity>> samba_generate) throws ZipException, IOException, InputFileDifferException{

    try{
      return this.common_zip.outputZipCom(samba_generate);
    }catch(ZipException e){
      throw new ZipException("Error location [Score_Pdf_ZipImpl:outputZip(ZipException)]" + "\n" + e);
    }catch(IOException e){
      throw new IOException("Error location [Score_Pdf_ZipImpl:outputZip(IOException)]" + "\n" + e);
    }
  }
}
