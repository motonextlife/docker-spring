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
import com.springproject.dockerspring.commonenum.Rec_Eval_Items_Enum;
import com.springproject.dockerspring.component.OutPdf;
import com.springproject.dockerspring.entity.NormalEntity.Rec_Eval_Items;






/**************************************************************/
/*   [Rec_Eval_Items_Pdf]
/*   エンティティ「Rec_Eval_Items」のデータを用いて、
/*   出力するPDFデータのデザインを行う。
/**************************************************************/

@Component
public class Rec_Eval_Items_Pdf implements Serializable, OutPdf<Rec_Eval_Items>{


  /***********************************************/
  /* フィールド変数は全て、対象エンティティの
  /* フィールド変数名に準じている。
  /***********************************************/
  private String serial_num;
  private String evalsheet_id;
  private String evalsheet_name;
  private String evalsheet_date;
  private String evalsheet_kinds;
  private String eval_item_contents_1;
  private String eval_item_kinds_1;
  private String eval_item_contents_2;
  private String eval_item_kinds_2;
  private String eval_item_contents_3;
  private String eval_item_kinds_3;
  private String eval_item_contents_4;
  private String eval_item_kinds_4;
  private String eval_item_contents_5;
  private String eval_item_kinds_5;
  private String eval_item_contents_6;
  private String eval_item_kinds_6;
  private String eval_item_contents_7;
  private String eval_item_kinds_7;
  private String eval_item_contents_8;
  private String eval_item_kinds_8;
  private String eval_item_contents_9;
  private String eval_item_kinds_9;
  private String eval_item_contents_10;
  private String eval_item_kinds_10;






  /***************************************************************/
  /*   [makePdf]
  /*   エンティティ内のデータを、PDFとして出力するために、
  /*   出力用ストリームを生成して、PDF生成用クラスにデータを渡し、
  /*   返ってきた作成済みデータを出力する。
  /***************************************************************/

  @Override
  public String makePdf(Rec_Eval_Items entity) throws IOException{

    String result;
    
    try(ByteArrayOutputStream byteout = new ByteArrayOutputStream();
      BufferedOutputStream out = new BufferedOutputStream(byteout);){

      Map<String, String> map = entity.makeMap();

      this.serial_num = map.get(Rec_Eval_Items_Enum.Serial_Num.getKey());
      this.evalsheet_id = map.get(Rec_Eval_Items_Enum.Evalsheet_Id.getKey());
      this.evalsheet_name = map.get(Rec_Eval_Items_Enum.Evalsheet_Name.getKey());
      this.evalsheet_date = map.get(Rec_Eval_Items_Enum.Evalsheet_Date.getKey());
      this.evalsheet_kinds = map.get(Rec_Eval_Items_Enum.Eval_Item_Kinds_1.getKey());
      this.eval_item_contents_1 = map.get(Rec_Eval_Items_Enum.Eval_Item_Contents_1.getKey());
      this.eval_item_kinds_1 = map.get(Rec_Eval_Items_Enum.Eval_Item_Kinds_2.getKey());
      this.eval_item_contents_2 = map.get(Rec_Eval_Items_Enum.Eval_Item_Contents_2.getKey());
      this.eval_item_kinds_2 = map.get(Rec_Eval_Items_Enum.Eval_Item_Kinds_3.getKey());
      this.eval_item_contents_3 = map.get(Rec_Eval_Items_Enum.Eval_Item_Contents_3.getKey());
      this.eval_item_kinds_3 = map.get(Rec_Eval_Items_Enum.Eval_Item_Kinds_4.getKey());
      this.eval_item_contents_4 = map.get(Rec_Eval_Items_Enum.Eval_Item_Contents_4.getKey());
      this.eval_item_kinds_4 = map.get(Rec_Eval_Items_Enum.Eval_Item_Kinds_5.getKey());
      this.eval_item_contents_5 = map.get(Rec_Eval_Items_Enum.Eval_Item_Contents_5.getKey());
      this.eval_item_kinds_5 = map.get(Rec_Eval_Items_Enum.Eval_Item_Kinds_6.getKey());
      this.eval_item_contents_6 = map.get(Rec_Eval_Items_Enum.Eval_Item_Contents_6.getKey());
      this.eval_item_kinds_6 = map.get(Rec_Eval_Items_Enum.Eval_Item_Kinds_7.getKey());
      this.eval_item_contents_7 = map.get(Rec_Eval_Items_Enum.Eval_Item_Contents_7.getKey());
      this.eval_item_kinds_7 = map.get(Rec_Eval_Items_Enum.Eval_Item_Kinds_8.getKey());
      this.eval_item_contents_8 = map.get(Rec_Eval_Items_Enum.Eval_Item_Contents_8.getKey());
      this.eval_item_kinds_8 = map.get(Rec_Eval_Items_Enum.Eval_Item_Kinds_9.getKey());
      this.eval_item_contents_9 = map.get(Rec_Eval_Items_Enum.Eval_Item_Contents_9.getKey());
      this.eval_item_kinds_9 = map.get(Rec_Eval_Items_Enum.Eval_Item_Kinds_10.getKey());
      this.eval_item_contents_10 = map.get(Rec_Eval_Items_Enum.Eval_Item_Contents_10.getKey());
      this.eval_item_kinds_10 = map.get(Rec_Eval_Items_Enum.Eval_Item_Kinds_1.getKey());

      designPdf(out);  //PDF化実行
      result = pdfBase64Encode(byteout.toByteArray());
      
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Rec_Eval_Items:makePdf(NullPointerException)]");
    }catch(IOException e){
      throw new IOException("Error location [Rec_Eval_Items:makePdf(IOException)]");
    }catch(ClassCastException e){
      throw new ClassCastException("Error location [Rec_Eval_Items:makePdf(ClassCastException)]");
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
      throw new IOException("Error location [Rec_Eval_Items_Pdf:designPdf(IOException)]");
    }

    /******************************************************/
    /*   この段階でのPDFデータの出力は行わない。
    /*   出力はフロントサイドに任せる。
    /******************************************************/
    try(Document doc = new Document(pdf, PageSize.A4)){
      
      doc.setMargins(30.0F, 30.0F, 30.0F, 30.0F);

      doc.add(new Paragraph(this.serial_num).setFont(font));
      doc.add(new Paragraph(this.evalsheet_id).setFont(font));
      doc.add(new Paragraph(this.evalsheet_name).setFont(font));
      doc.add(new Paragraph(this.evalsheet_date).setFont(font));
      doc.add(new Paragraph(this.evalsheet_kinds).setFont(font));
      doc.add(new Paragraph(this.eval_item_contents_1).setFont(font));
      doc.add(new Paragraph(this.eval_item_kinds_1).setFont(font));
      doc.add(new Paragraph(this.eval_item_contents_2).setFont(font));
      doc.add(new Paragraph(this.eval_item_kinds_2).setFont(font));
      doc.add(new Paragraph(this.eval_item_contents_3).setFont(font));
      doc.add(new Paragraph(this.eval_item_kinds_3).setFont(font));
      doc.add(new Paragraph(this.eval_item_contents_4).setFont(font));
      doc.add(new Paragraph(this.eval_item_kinds_4).setFont(font));
      doc.add(new Paragraph(this.eval_item_contents_5).setFont(font));
      doc.add(new Paragraph(this.eval_item_kinds_5).setFont(font));
      doc.add(new Paragraph(this.eval_item_contents_6).setFont(font));
      doc.add(new Paragraph(this.eval_item_kinds_6).setFont(font));
      doc.add(new Paragraph(this.eval_item_contents_7).setFont(font));
      doc.add(new Paragraph(this.eval_item_kinds_7).setFont(font));
      doc.add(new Paragraph(this.eval_item_contents_8).setFont(font));
      doc.add(new Paragraph(this.eval_item_kinds_8).setFont(font));
      doc.add(new Paragraph(this.eval_item_contents_9).setFont(font));
      doc.add(new Paragraph(this.eval_item_kinds_9).setFont(font));
      doc.add(new Paragraph(this.eval_item_contents_10).setFont(font));
      doc.add(new Paragraph(this.eval_item_kinds_10).setFont(font));
    }
  }
}