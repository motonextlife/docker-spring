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
import com.springproject.dockerspring.commonenum.Musical_Score_Enum;
import com.springproject.dockerspring.component.OutPdf;
import com.springproject.dockerspring.entity.NormalEntity.Musical_Score;




/**************************************************************/
/*   [Musical_Score_Pdf]
/*   エンティティ「Musical_Score」のデータを用いて、
/*   出力するPDFデータのデザインを行う。
/**************************************************************/

@Component
public class Musical_Score_Pdf implements Serializable, OutPdf<Musical_Score>{


  /***********************************************/
  /* フィールド変数は全て、対象エンティティの
  /* フィールド変数名に準じている。
  /***********************************************/
  private String serial_num;
  private String score_id;
  private String buy_date;
  private String song_title;
  private String composer;
  private String arranger;
  private String publisher;
  private String strage_loc;
  private String disp_date;
  private String other_comment;






  /***************************************************************/
  /*   [makePdf]
  /*   エンティティ内のデータを、PDFとして出力するために、
  /*   出力用ストリームを生成して、PDF生成用クラスにデータを渡し、
  /*   返ってきた作成済みデータを出力する。
  /***************************************************************/

  @Override
  public String makePdf(Musical_Score entity) throws IOException{

    String result;
    
    try(ByteArrayOutputStream byteout = new ByteArrayOutputStream();
      BufferedOutputStream out = new BufferedOutputStream(byteout);){

      Map<String, String> map = entity.makeMap();

      this.serial_num = map.get(Musical_Score_Enum.Serial_Num.getKey());
      this.score_id = map.get(Musical_Score_Enum.Score_Id.getKey());
      this.buy_date = map.get(Musical_Score_Enum.Buy_Date.getKey());
      this.song_title = map.get(Musical_Score_Enum.Song_Title.getKey());
      this.composer = map.get(Musical_Score_Enum.Composer.getKey());
      this.arranger = map.get(Musical_Score_Enum.Arranger.getKey());
      this.publisher = map.get(Musical_Score_Enum.Publisher.getKey());
      this.strage_loc = map.get(Musical_Score_Enum.Strage_Loc.getKey());
      this.disp_date = map.get(Musical_Score_Enum.Disp_Date.getKey());
      this.other_comment = map.get(Musical_Score_Enum.Other_Comment.getKey());
      
      designPdf(out);  //PDF化実行
      result = pdfBase64Encode(byteout.toByteArray());
      
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Musical_Score:makePdf(NullPointerException)]");
    }catch(IOException e){
      throw new IOException("Error location [Musical_Score:makePdf(IOException)]");
    }catch(ClassCastException e){
      throw new ClassCastException("Error location [Musical_Score:makePdf(ClassCastException)]");
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
      throw new IOException("Error location [Musical_Score_Pdf:designPdf(IOException)]");
    }


    /******************************************************/
    /*   この段階でのPDFデータの出力は行わない。
    /*   出力はフロントサイドに任せる。
    /******************************************************/
    try(Document doc = new Document(pdf, PageSize.A4)){
      
      doc.setMargins(30.0F, 30.0F, 30.0F, 30.0F);

      doc.add(new Paragraph(this.serial_num).setFont(font));
      doc.add(new Paragraph(this.score_id).setFont(font));
      doc.add(new Paragraph(this.buy_date).setFont(font));
      doc.add(new Paragraph(this.song_title).setFont(font));
      doc.add(new Paragraph(this.composer).setFont(font));
      doc.add(new Paragraph(this.arranger).setFont(font));
      doc.add(new Paragraph(this.publisher).setFont(font));
      doc.add(new Paragraph(this.strage_loc).setFont(font));
      doc.add(new Paragraph(this.disp_date).setFont(font));
      doc.add(new Paragraph(this.other_comment).setFont(font));
    }
  }
}