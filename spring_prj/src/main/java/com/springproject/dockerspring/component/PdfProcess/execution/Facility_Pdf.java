package com.springproject.dockerspring.component.PdfProcess.execution;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.springproject.dockerspring.commonenum.Facility_Enum;
import com.springproject.dockerspring.component.OutPdf;
import com.springproject.dockerspring.entity.NormalEntity.Facility;





/**************************************************************/
/*   [Facility_Pdf]
/*   エンティティ「Facility」のデータを用いて、
/*   出力するPDFデータのデザインを行う。
/**************************************************************/

@Component
public class Facility_Pdf implements Serializable, OutPdf<Facility>{


  /***********************************************/
  /* フィールド変数は全て、対象エンティティの
  /* フィールド変数名に準じている。
  /***********************************************/
  private String serial_num;
  private String faci_id;
  private String faci_name;
  private String chief_admin;
  private String resp_person;
  private String buy_date;
  private String storage_loc;
  private String buy_price;
  private String disp_date;
  private String other_comment;







  /***************************************************************/
  /*   [makePdf]
  /*   エンティティ内のデータを、PDFとして出力するために、
  /*   出力用ストリームを生成して、PDF生成用クラスにデータを渡し、
  /*   返ってきた作成済みデータを出力する。
  /***************************************************************/

  @Override
  public String makePdf(Facility entity) throws IOException{

    String result;
    
    try(ByteArrayOutputStream byteout = new ByteArrayOutputStream();
      BufferedOutputStream out = new BufferedOutputStream(byteout);){

      Map<String, String> map = entity.makeMap();

      this.serial_num = map.get(Facility_Enum.Serial_Num.getKey());
      this.faci_id = map.get(Facility_Enum.Faci_Id.getKey());
      this.faci_name = map.get(Facility_Enum.Faci_Name.getKey());
      this.chief_admin = map.get(Facility_Enum.Chief_Admin.getKey());
      this.resp_person = map.get(Facility_Enum.Resp_Person.getKey());
      this.buy_date = map.get(Facility_Enum.Buy_Date.getKey());
      this.storage_loc = map.get(Facility_Enum.Storage_Loc.getKey());
      this.buy_price = map.get(Facility_Enum.Buy_Price.getKey());
      this.disp_date = map.get(Facility_Enum.Disp_Date.getKey());
      this.other_comment = map.get(Facility_Enum.Other_Comment.getKey());

      designPdf(out);  //PDF化実行
      result = pdfBase64Encode(byteout.toByteArray());
      
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Facility:makePdf(NullPointerException)]");
    }catch(IOException e){
      throw new IOException("Error location [Facility:makePdf(IOException)]");
    }catch(ClassCastException e){
      throw new ClassCastException("Error location [Facility:makePdf(ClassCastException)]");
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
      throw new IOException("Error location [Facility_Pdf:designPdf(IOException)]");
    }

    /******************************************************/
    /*   この段階でのPDFデータの出力は行わない。
    /*   出力はフロントサイドに任せる。
    /******************************************************/
    try(Document doc = new Document(pdf, PageSize.A4)){
      
      doc.setMargins(30.0F, 30.0F, 30.0F, 30.0F);

      doc.add(new Paragraph(this.serial_num).setFont(font));
      doc.add(new Paragraph(this.faci_id).setFont(font));
      doc.add(new Paragraph(this.faci_name).setFont(font));
      doc.add(new Paragraph(this.chief_admin).setFont(font));
      doc.add(new Paragraph(this.resp_person).setFont(font));
      doc.add(new Paragraph(this.buy_date).setFont(font));
      doc.add(new Paragraph(this.storage_loc).setFont(font));
      doc.add(new Paragraph(this.buy_price).setFont(font));
      doc.add(new Paragraph(this.disp_date).setFont(font));
      doc.add(new Paragraph(this.other_comment).setFont(font));
    }
  }
}