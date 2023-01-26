/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.FileIO.PdfProcess
 * 
 * @brief ファイル入出力機能のうち、[PDFファイルの入出力]に関係する処理を格納したパッケージ
 * 
 * @details
 * - このパッケージは、渡されたエンティティ内のデータを元に、PDFデータを作成する機能を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.FileIO.PdfProcess;






/** 
 **************************************************************************************
 * @file Common_Pdf.java
 * @brief すべてのPDF出力機能で使用する共通処理を定義するクラスを格納するファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.springproject.dockerspring.CommonEnum.UtilEnum.Datatype_Enum;
import com.springproject.dockerspring.CommonEnum.UtilEnum.File_Path_Enum;









/** 
 **************************************************************************************
 * @brief すべてのPDF出力機能で使用する共通処理を定義するクラス。
 * 
 * @details 
 * - この共通処理では、PDFファイルの処理に関して、特定のクラスに依存しない共通の処理を
 * 定義する。作成対象の特定クラスに対してはDIでこのクラスを使えるようにする。
 * - このクラスで作成される一時ファイルに関しては、すべてローカルにある一時ファイル用の
 * ディレクトリに出力する。なお、その際のパーミッションは、実行権限をすべて剥奪しておく。
 * 
 * @par 使用アノテーション
 * - @Component
 **************************************************************************************
 */ 
@Component
public class Common_Pdf{

  private final Path specify_dir = Paths.get(File_Path_Enum.ResourceFilePath.getPath());

  private final FileAttribute<Set<PosixFilePermission>> perms = PosixFilePermissions
                                                                .asFileAttribute(PosixFilePermissions
                                                                                .fromString(File_Path_Enum.Permission.getPath()));









  /** 
   **********************************************************************************************
   * @brief PDF生成用の一時ファイルを一元的に製作するメソッド。
   * 
   * @details
   * - なお、ここで生成する一時ファイルの拡張子は何があっても[pdf]に統一する。
   *
   * @return 生成した一時ファイル 
   *
   * @throw IOException
   **********************************************************************************************
   */
  public File makeTmpFile() throws IOException {

    try{
      Path tmp_pdf_path = Files.createTempFile(this.specify_dir, 
                                            "makePdf", 
                                            ".".concat(Datatype_Enum.PDF.getExtension()), 
                                            this.perms);

      return tmp_pdf_path.toFile();

    }catch(IOException e){
      throw new IOException("Error location [Common_Pdf:makeTmpFile]" + "\n" + e);
    }

  }







  
  /** 
   **********************************************************************************************
   * @brief PDF生成時に、使用するフォントの設定を行う。
   * 
   * @details
   * - 指定フォントは「HeiseiKakuGo-W5」に、文字コードは「UniJIS-UCS2-H」に統一する。
   *
   * @return 生成したフォント設定 
   *
   * @throw IOException
   **********************************************************************************************
   */
  public PdfFont fontSetup() throws IOException{

    try{
      return PdfFontFactory.createFont("HeiseiKakuGo-W5", "UniJIS-UCS2-H");
    }catch(IOException e){
      throw new IOException("Error location [Common_Pdf:fontSetup]" + "\n" + e);
    }
  }
}