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
import com.springproject.dockerspring.commonenum.Equip_Inspect_Enum;
import com.springproject.dockerspring.component.OutPdf;
import com.springproject.dockerspring.entity.NormalEntity.Equip_Inspect;






/**************************************************************/
/*   [Equip_Inspect_Pdf]
/*   エンティティ「Equip_Inspect」のデータを用いて、
/*   出力するPDFデータの出力を行う。
/**************************************************************/

@Component
public class Equip_Inspect_Pdf implements Serializable, OutPdf<Equip_Inspect>{


  /***********************************************/
  /* フィールド変数は全て、対象エンティティの
  /* フィールド変数名に準じている。
  /***********************************************/
  private String serial_num;
  private String inspect_id;
  private String faci_id;
  private String inspect_name;
  private String inspect_date;
  private String inspsheet_id;
  private String insp_contents_1;
  private String insp_rank_1;
  private String insp_contents_2;
  private String insp_rank_2;
  private String insp_contents_3;
  private String insp_rank_3;
  private String insp_contents_4;
  private String insp_rank_4;
  private String insp_contents_5;
  private String insp_rank_5;
  private String insp_contents_6;
  private String insp_rank_6;
  private String insp_contents_7;
  private String insp_rank_7;
  private String insp_contents_8;
  private String insp_rank_8;
  private String insp_contents_9;
  private String insp_rank_9;
  private String insp_contents_10;
  private String insp_rank_10;






  /***************************************************************/
  /*   [makePdf]
  /*   エンティティ内のデータを、PDFとして出力するために、
  /*   出力用ストリームを生成して、PDF生成用クラスにデータを渡し、
  /*   返ってきた作成済みデータを出力する。
  /***************************************************************/

  @Override
  public String makePdf(Equip_Inspect entity) throws IOException{

    String result;
    
    try(ByteArrayOutputStream byteout = new ByteArrayOutputStream();
      BufferedOutputStream out = new BufferedOutputStream(byteout);){

      Map<String, String> map = entity.makeMap();
      
      this.serial_num = map.get(Equip_Inspect_Enum.Serial_Num.getKey());
      this.inspect_id = map.get(Equip_Inspect_Enum.Inspect_Id.getKey());
      this.faci_id = map.get(Equip_Inspect_Enum.Faci_Id.getKey());
      this.inspect_name = map.get(Equip_Inspect_Enum.Inspect_Name.getKey());
      this.inspect_date = map.get(Equip_Inspect_Enum.Inspect_Date.getKey());
      this.inspsheet_id = map.get(Equip_Inspect_Enum.Inspsheet_Id.getKey());
      this.insp_contents_1 = map.get(Equip_Inspect_Enum.Insp_Contents_1.getKey());
      this.insp_rank_1 = map.get(Equip_Inspect_Enum.Insp_Rank_1.getKey());
      this.insp_contents_2 = map.get(Equip_Inspect_Enum.Insp_Contents_2.getKey());
      this.insp_rank_2 = map.get(Equip_Inspect_Enum.Insp_Rank_2.getKey());
      this.insp_contents_3 = map.get(Equip_Inspect_Enum.Insp_Contents_3.getKey());
      this.insp_rank_3 = map.get(Equip_Inspect_Enum.Insp_Rank_3.getKey());
      this.insp_contents_4 = map.get(Equip_Inspect_Enum.Insp_Contents_4.getKey());
      this.insp_rank_4 = map.get(Equip_Inspect_Enum.Insp_Rank_4.getKey());
      this.insp_contents_5 = map.get(Equip_Inspect_Enum.Insp_Contents_5.getKey());
      this.insp_rank_5 = map.get(Equip_Inspect_Enum.Insp_Rank_5.getKey());
      this.insp_contents_6 = map.get(Equip_Inspect_Enum.Insp_Contents_6.getKey());
      this.insp_rank_6 = map.get(Equip_Inspect_Enum.Insp_Rank_6.getKey());
      this.insp_contents_7 = map.get(Equip_Inspect_Enum.Insp_Contents_7.getKey());
      this.insp_rank_7 = map.get(Equip_Inspect_Enum.Insp_Rank_7.getKey());
      this.insp_contents_8 = map.get(Equip_Inspect_Enum.Insp_Contents_8.getKey());
      this.insp_rank_8 = map.get(Equip_Inspect_Enum.Insp_Rank_8.getKey());
      this.insp_contents_9 = map.get(Equip_Inspect_Enum.Insp_Contents_9.getKey());
      this.insp_rank_9 = map.get(Equip_Inspect_Enum.Insp_Rank_9.getKey());
      this.insp_contents_10 = map.get(Equip_Inspect_Enum.Insp_Contents_10.getKey());
      this.insp_rank_10 = map.get(Equip_Inspect_Enum.Insp_Rank_10.getKey());

      designPdf(out);  //PDF化実行
      result = pdfBase64Encode(byteout.toByteArray());
      
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Equip_Inspect_Pdf:makePdf(NullPointerException)]");
    }catch(IOException e){
      throw new IOException("Error location [Equip_Inspect_Pdf:makePdf(IOException)]");
    }catch(ClassCastException e){
      throw new ClassCastException("Error location [Equip_Inspect_Pdf:makePdf(ClassCastException)]");
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
      throw new IOException("Error location [Equip_Inspect_Pdf:designPdf(IOException)]");
    }

    /******************************************************/
    /*   この段階でのPDFデータの出力は行わない。
    /*   出力はフロントサイドに任せる。
    /******************************************************/
    try(Document doc = new Document(pdf, PageSize.A4)){
      
      doc.setMargins(30.0F, 30.0F, 30.0F, 30.0F);

      doc.add(new Paragraph(this.serial_num).setFont(font));
      doc.add(new Paragraph(this.inspect_id).setFont(font));
      doc.add(new Paragraph(this.faci_id).setFont(font));
      doc.add(new Paragraph(this.inspect_name).setFont(font));
      doc.add(new Paragraph(this.inspect_date).setFont(font));
      doc.add(new Paragraph(this.inspsheet_id).setFont(font));
      doc.add(new Paragraph(this.insp_contents_1).setFont(font));
      doc.add(new Paragraph(this.insp_rank_1).setFont(font));
      doc.add(new Paragraph(this.insp_contents_2).setFont(font));
      doc.add(new Paragraph(this.insp_rank_2).setFont(font));
      doc.add(new Paragraph(this.insp_contents_3).setFont(font));
      doc.add(new Paragraph(this.insp_rank_3).setFont(font));
      doc.add(new Paragraph(this.insp_contents_4).setFont(font));
      doc.add(new Paragraph(this.insp_rank_4).setFont(font));
      doc.add(new Paragraph(this.insp_contents_5).setFont(font));
      doc.add(new Paragraph(this.insp_rank_5).setFont(font));
      doc.add(new Paragraph(this.insp_contents_6).setFont(font));
      doc.add(new Paragraph(this.insp_rank_6).setFont(font));
      doc.add(new Paragraph(this.insp_contents_7).setFont(font));
      doc.add(new Paragraph(this.insp_rank_7).setFont(font));
      doc.add(new Paragraph(this.insp_contents_8).setFont(font));
      doc.add(new Paragraph(this.insp_rank_8).setFont(font));
      doc.add(new Paragraph(this.insp_contents_9).setFont(font));
      doc.add(new Paragraph(this.insp_rank_9).setFont(font));
      doc.add(new Paragraph(this.insp_contents_10).setFont(font));
      doc.add(new Paragraph(this.insp_rank_10).setFont(font));
    }
  }
}