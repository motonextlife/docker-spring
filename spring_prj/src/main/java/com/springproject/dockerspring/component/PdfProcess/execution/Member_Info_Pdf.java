package com.springproject.dockerspring.component.PdfProcess.execution;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Base64;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageType;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.springproject.dockerspring.commonenum.Member_Info_Enum;
import com.springproject.dockerspring.component.OutPdf;
import com.springproject.dockerspring.entity.NormalEntity.Member_Info;






/**************************************************************/
/*   [Member_Info_Pdf]
/*   エンティティ「Member_Info」のデータを用いて、
/*   出力するPDFデータのデザインを行う。
/**************************************************************/

@Component
public class Member_Info_Pdf implements Serializable, OutPdf<Member_Info>{


  /***********************************************/
  /* フィールド変数は全て、対象エンティティの
  /* フィールド変数名に準じている。
  /***********************************************/
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







  /***************************************************************/
  /*   [makePdf]
  /*   エンティティ内のデータを、PDFとして出力するために、
  /*   出力用ストリームを生成して、PDF生成用クラスにデータを渡し、
  /*   返ってきた作成済みデータを出力する。
  /***************************************************************/

  @Override
  public String makePdf(Member_Info entity) throws IOException{

    String result;
    
    try(ByteArrayOutputStream byteout = new ByteArrayOutputStream();
      BufferedOutputStream out = new BufferedOutputStream(byteout);){

      Map<String, String> map = entity.makeMap();

      this.serial_num = map.get(Member_Info_Enum.Serial_Num.getKey());
      this.member_id = map.get(Member_Info_Enum.Member_Id.getKey());
      this.name = map.get(Member_Info_Enum.Name.getKey());
      this.name_pronu = map.get(Member_Info_Enum.Name_Pronu.getKey());
      this.sex = map.get(Member_Info_Enum.Sex.getKey());
      this.birthday = map.get(Member_Info_Enum.Birthday.getKey());

      //画像はBase64形式で来るので、不必要な文字列を削除し、バイト配列に変換。
      String base64 = map.get(Member_Info_Enum.Face_Photo.getKey());
      base64 = base64.replace("data:image/jpeg;base64,", "");
      this.face_photo = Base64.getDecoder().decode(base64);

      this.join_date = map.get(Member_Info_Enum.Join_Date.getKey());
      this.ret_date = map.get(Member_Info_Enum.Ret_Date.getKey());
      this.email_1 = map.get(Member_Info_Enum.Email_1.getKey());
      this.email_2 = map.get(Member_Info_Enum.Email_2.getKey());
      this.tel_1 = map.get(Member_Info_Enum.Tel_1.getKey());
      this.tel_2 = map.get(Member_Info_Enum.Tel_2.getKey());
      this.addr_postcode = map.get(Member_Info_Enum.Addr_Postcode.getKey());
      this.addr = map.get(Member_Info_Enum.Addr.getKey());
      this.position = map.get(Member_Info_Enum.Position.getKey());
      this.position_arri_date = map.get(Member_Info_Enum.Position_Arri_Date.getKey());
      this.job = map.get(Member_Info_Enum.Job.getKey());
      this.assign_dept = map.get(Member_Info_Enum.Assign_Dept.getKey());
      this.assign_date = map.get(Member_Info_Enum.Assign_Date.getKey());
      this.inst_charge = map.get(Member_Info_Enum.Inst_Charge.getKey());
      this.other_comment = map.get(Member_Info_Enum.Other_Comment.getKey());
      
      designPdf(out);  //PDF化実行
      result = pdfBase64Encode(byteout.toByteArray());
      
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Member_Info:makePdf(NullPointerException)]");
    }catch(IOException e){
      throw new IOException("Error location [Member_Info:makePdf(IOException)]");
    }catch(ClassCastException e){
      throw new ClassCastException("Error location [Member_Info:makePdf(ClassCastException)]");
    }

    return result;
  }





  
  //ここの領域は、デザイン、つまりフロントエンドの領域としてデザイン確定後に作成。現在は仮。

  /********************************************************************/
  /*   [designPdf]
  /*   エンティティから受け取ったデータを用いてPDFデータをデザインする。
  /********************************************************************/
  @Override
  public void designPdf(BufferedOutputStream out) throws IOException{

    PdfFont font;
    PdfWriter writer;
    PdfDocument pdf;

    /*********************************************************/
    /*   指定フォントは「HeiseiKakuGo-W5」に統一。
    /*   文字コードは「UniJIS-UCS2-H」に統一。
    /*********************************************************/
    try{
      font = PdfFontFactory.createFont("HeiseiKakuGo-W5", "UniJIS-UCS2-H");
      writer = new PdfWriter(out);
      pdf = new PdfDocument(writer);
    }catch(IOException e){
      throw new IOException("Error location [Member_Info_Pdf:designPdf(IOException)]");
    }

    /******************************************************/
    /*   この段階でのPDFデータの出力は行わない。
    /*   出力はフロントサイドに任せる。
    /******************************************************/
    try(Document doc = new Document(pdf, PageSize.A4)){
      
      doc.setMargins(30.0F, 30.0F, 30.0F, 30.0F);

      doc.add(new Paragraph(this.serial_num).setFont(font));
      doc.add(new Paragraph(this.member_id).setFont(font));
      doc.add(new Paragraph(this.name).setFont(font));
      doc.add(new Paragraph(this.name_pronu).setFont(font));
      doc.add(new Paragraph(this.sex).setFont(font));
      doc.add(new Paragraph(this.birthday).setFont(font));

      Image image = new Image(new ImageData(this.face_photo, ImageType.JPEG){});
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
    }
  }
}