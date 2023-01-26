/** 
 **************************************************************************************
 * @file Member_Info_PdfImpl.java
 * @brief 主に[団員管理]機能で使用する、PDFファイルの入出力に関する機能を実装するクラスを
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

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.springproject.dockerspring.CommonEnum.NormalEnum.Member_Info_Enum;
import com.springproject.dockerspring.FileIO.CompInterface.Member_Info_Pdf;
import com.springproject.dockerspring.Entity.NormalEntity.Member_Info;









/** 
 **************************************************************************************
 * @brief 主に[団員管理]機能で使用する、PDFファイルの入出力に関する機能を実装するクラス
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
 * @see Member_Info_Pdf
 * @see Common_Pdf
 * @see Member_Info
 **************************************************************************************
 */ 
@Component
public class Member_Info_PdfImpl implements Member_Info_Pdf{

  private final Common_Pdf common_pdf;

  private String serial_num;
  private String member_id;
  private String name;
  private String name_pronu;
  private String sex;
  private String birthday;
  private byte[] face_photo;
  private String join_date;
  private String ret_date;
  private String email_1;
  private String email_2;
  private String tel_1;
  private String tel_2;
  private String addr_postcode;
  private String addr;
  private String position;
  private String position_arri_date;
  private String job;
  private String assign_dept;
  private String assign_date;
  private String inst_charge;
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
  public Member_Info_PdfImpl(Common_Pdf common_pdf){
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
   * -# 顔写真に関しては、エンティティからそのままバイト配列として取り出す。
   * -# クラス内のデザインメソッドの処理を実行し、出力ストリームにデザイン情報を入力。
   * -# 一時ファイルに書き込んだ後、そのファイルを戻り値とする。
   * 
   * @see Member_Info
   *
   * @throw IOException
   **********************************************************************************************
   */
  @Override
  public File makePdf(Member_Info entity) throws IOException{

    File temp_file = this.common_pdf.makeTmpFile();

    try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(temp_file))){
      Map<String, String> str_map = entity.makeMap();

      this.serial_num = str_map.get(Member_Info_Enum.Serial_Num.getEnName());
      this.member_id = str_map.get(Member_Info_Enum.Member_Id.getEnName());
      this.name = str_map.get(Member_Info_Enum.Name.getEnName());
      this.name_pronu = str_map.get(Member_Info_Enum.Name_Pronu.getEnName());
      this.sex = str_map.get(Member_Info_Enum.Sex.getEnName());
      this.birthday = str_map.get(Member_Info_Enum.Birthday.getEnName());
      this.face_photo = entity.getFace_photo();
      this.join_date = str_map.get(Member_Info_Enum.Join_Date.getEnName());
      this.ret_date = str_map.get(Member_Info_Enum.Ret_Date.getEnName());
      this.email_1 = str_map.get(Member_Info_Enum.Email_1.getEnName());
      this.email_2 = str_map.get(Member_Info_Enum.Email_2.getEnName());
      this.tel_1 = str_map.get(Member_Info_Enum.Tel_1.getEnName());
      this.tel_2 = str_map.get(Member_Info_Enum.Tel_2.getEnName());
      this.addr_postcode = str_map.get(Member_Info_Enum.Addr_Postcode.getEnName());
      this.addr = str_map.get(Member_Info_Enum.Addr.getEnName());
      this.position = str_map.get(Member_Info_Enum.Position.getEnName());
      this.position_arri_date = str_map.get(Member_Info_Enum.Position_Arri_Date.getEnName());
      this.job = str_map.get(Member_Info_Enum.Job.getEnName());
      this.assign_dept = str_map.get(Member_Info_Enum.Assign_Dept.getEnName());
      this.assign_date = str_map.get(Member_Info_Enum.Assign_Date.getEnName());
      this.inst_charge = str_map.get(Member_Info_Enum.Inst_Charge.getEnName());
      this.other_comment = str_map.get(Member_Info_Enum.Other_Comment.getEnName());
      
      designPdf(bos);
      
    }catch(IOException e){
      Files.deleteIfExists(temp_file.toPath());
      throw new IOException("Error location [Member_Info_PdfImpl:makePdf(IOException)]" + "\n" + e);
    }catch(NullPointerException e){
      Files.deleteIfExists(temp_file.toPath());
      throw new NullPointerException("Error location [Member_Info_PdfImpl:makePdf(NullPointerException)]" + "\n" + e);
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
  private void designPdf(BufferedOutputStream out) throws IOException{

    PdfFont font = this.common_pdf.fontSetup();

    try(PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf, PageSize.A4);){
      
      doc.setMargins(30.0F, 30.0F, 30.0F, 30.0F);

      doc.add(new Paragraph(this.serial_num).setFont(font));
      doc.add(new Paragraph(this.member_id).setFont(font));
      doc.add(new Paragraph(this.name).setFont(font));
      doc.add(new Paragraph(this.name_pronu).setFont(font));
      doc.add(new Paragraph(this.sex).setFont(font));
      doc.add(new Paragraph(this.birthday).setFont(font));

      Image image = new Image(ImageDataFactory.create(this.face_photo));
      image.setHeight(50.0F);
      image.setWidth(50.0F);
      doc.add(image);
      
      doc.add(new Paragraph(this.join_date).setFont(font));
      doc.add(new Paragraph(this.ret_date).setFont(font));
      doc.add(new Paragraph(this.email_1).setFont(font));
      doc.add(new Paragraph(this.email_2).setFont(font));
      doc.add(new Paragraph(this.tel_1).setFont(font));
      doc.add(new Paragraph(this.tel_2).setFont(font));
      doc.add(new Paragraph(this.addr_postcode).setFont(font));
      doc.add(new Paragraph(this.addr).setFont(font));
      doc.add(new Paragraph(this.position).setFont(font));
      doc.add(new Paragraph(this.position_arri_date).setFont(font));
      doc.add(new Paragraph(this.job).setFont(font));
      doc.add(new Paragraph(this.assign_dept).setFont(font));
      doc.add(new Paragraph(this.assign_date).setFont(font));
      doc.add(new Paragraph(this.inst_charge).setFont(font));
      doc.add(new Paragraph(this.other_comment).setFont(font));

      doc.flush();
    }
  }
}