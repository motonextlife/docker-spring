/** 
 **************************************************************************************
 * @file Sound_Source_PdfImpl.java
 * @brief 主に[音源管理]機能で使用する、PDFファイルの入出力に関する機能を実装するクラスを
 * 格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.FileIO.PdfProcess;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.springproject.dockerspring.CommonEnum.NormalEnum.Sound_Source_Enum;
import com.springproject.dockerspring.FileIO.CompInterface.Sound_Source_Pdf;
import com.springproject.dockerspring.Entity.NormalEntity.Sound_Source;










/** 
 **************************************************************************************
 * @brief 主に[音源管理]機能で使用する、PDFファイルの入出力に関する機能を実装するクラス
 * 
 * @details 
 * - このPDFの生成機能では、[iText Pdf]を用いて生成を行う。
 * - この生成機能で共通で用いる処理（一時ファイル生成やフォント設定など）は共通クラスをDIすることで
 * 、そちらを持ちいて実現する。
 * - フィールド変数には、出力対象のエンティティの値と、共通クラスのDIしたインスタンスを登録しておく。
 * この不変変数から、このクラス全体で共通で用いる。
 * 
 * @par 使用アノテーション
 * - @Component
 * 
 * @see Sound_Source_Pdf
 * @see Common_Pdf
 * @see Sound_Source
 **************************************************************************************
 */ 
@Component
public class Sound_Source_PdfImpl implements Sound_Source_Pdf{

  private final Common_Pdf common_pdf;

  private String serial_num;
  private String sound_id;
  private String upload_date;
  private String song_title;
  private String composer;
  private String performer;
  private String publisher;
  private String other_comment;









  /** 
   **********************************************************************************************
   * @brief 使用する共通クラスをコンストラクタインジェクションする。
   * 
   * @details
   * - 本体であれば、Lombokを用いてこの記述を省略したいが、共通クラス以外の他のフィールド変数とはマッピング
   * したくない為、個別に実装し、共通クラスのインジェクションのみ行えるようにした。
   **********************************************************************************************
   */
  @Autowired 
  public Sound_Source_PdfImpl(Common_Pdf common_pdf){
    this.common_pdf = common_pdf;
  }









  /** 
   **********************************************************************************************
   * @brief 出力対象データが入ったエンティティを読み込み、PDFを生成して、一時ファイルに書き込んだのち
   * そのファイルを戻り値とする。
   * 
   * @details
   * - エンティティを読み込んだら、そのデータをフィールド変数に格納し、このクラス全体で共通で値を使用できる
   * ようにする。
   * - PDFのデザインの部分に関しては、膨大な量になるため他の非公開メソッドにゆだねる。
   *
   * @return 生成したPDF一時ファイル 
   *
   * @param[in] entity 出力対象となるデータのエンティティ
   * 
   * @par 大まかな処理の流れ
   * -# PDFデータ書き込み用の一時ファイルを生成。
   * -# 読み込んだエンティティからデータを文字列化して取り出し、このクラス内のフィールド変数に格納。
   * -# クラス内のデザインメソッドの処理を実行し、出力ストリームにデザイン情報を入力。
   * -# 一時ファイルに書き込んだ後、そのファイルを戻り値とする。
   * 
   * @see Sound_Source
   *
   * @throw IOException
   **********************************************************************************************
   */
  @Override
  public File makePdf(Sound_Source entity) throws IOException{

    File temp_file = this.common_pdf.makeTmpFile();
    
    try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(temp_file))){
      Map<String, String> str_map = entity.makeMap();

      this.serial_num = str_map.get(Sound_Source_Enum.Serial_Num.getEnName());
      this.sound_id = str_map.get(Sound_Source_Enum.Sound_Id.getEnName());
      this.upload_date = str_map.get(Sound_Source_Enum.Upload_Date.getEnName());
      this.song_title = str_map.get(Sound_Source_Enum.Song_Title.getEnName());
      this.composer = str_map.get(Sound_Source_Enum.Composer.getEnName());
      this.performer = str_map.get(Sound_Source_Enum.Performer.getEnName());
      this.publisher = str_map.get(Sound_Source_Enum.Publisher.getEnName());
      this.other_comment = str_map.get(Sound_Source_Enum.Other_Comment.getEnName());
      
      designPdf(bos);
      
    }catch(IOException e){
      Files.deleteIfExists(temp_file.toPath());
      throw new IOException("Error location [Sound_Source_PdfImpl:makePdf(IOException)]" + "\n" + e);
    }catch(NullPointerException e){
      Files.deleteIfExists(temp_file.toPath());
      throw new NullPointerException("Error location [Sound_Source_PdfImpl:makePdf(NullPointerException)]" + "\n" + e);
    }

    return temp_file;
  }





  





  /** 
   **********************************************************************************************
   * @brief 受け取った出力ストリームに、PDFデザインの情報を書き込んでいく。
   * 
   * @details
   * - デザインは[iText]の機能を用いて、Javaのコードにより実現する。
   *
   * @param[out] pdf_data_out 書き込み対象の一時ファイルの出力ストリーム
   * 
   * @par 大まかな処理の流れ
   * -# PDFを生成する際のフォント設定を行う。
   * -# PDF生成用の書き込みストリームを作成。条件としては[A4用紙]への書き込みとする。
   * -# デザイン関数を駆使して、PDFのデザインを決定。
   * -# 一時ファイルの出力ストリームに書き込む。
   *
   * @note デザインに関しては未完成である。
   * 
   * @throw IOException
   **********************************************************************************************
   */
  private void designPdf(BufferedOutputStream pdf_data_out) throws IOException{

    PdfFont font = this.common_pdf.fontSetup();

    try(PdfWriter writer = new PdfWriter(pdf_data_out);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf, PageSize.A4);){
      
      doc.setMargins(30.0F, 30.0F, 30.0F, 30.0F);

      doc.add(new Paragraph(this.serial_num).setFont(font));
      doc.add(new Paragraph(this.sound_id).setFont(font));
      doc.add(new Paragraph(this.upload_date).setFont(font));
      doc.add(new Paragraph(this.song_title).setFont(font));
      doc.add(new Paragraph(this.composer).setFont(font));
      doc.add(new Paragraph(this.performer).setFont(font));
      doc.add(new Paragraph(this.publisher).setFont(font));
      doc.add(new Paragraph(this.other_comment).setFont(font));

      doc.flush();
    }
  }
}