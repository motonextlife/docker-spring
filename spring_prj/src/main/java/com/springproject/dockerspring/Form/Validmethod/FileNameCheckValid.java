/** 
 **************************************************************************************
 * @file FileNameCheckValid.java
 * @brief アノテーション[FileNameCheck]に対応する判定メソッドを実装したクラスを格納する
 * ファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.Validmethod;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.relational.core.dialect.Escaper;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import com.springproject.dockerspring.Form.OriginalAnno.FileNameCheck;







/** 
 **************************************************************************************
 * @brief アノテーション[FileNameCheck]に対応する判定メソッドを実装したクラス
 * 
 * @details 
 * - このアノテーションの目的としては、入力されたバイナリデータのファイル名が正当性を確保して
 * いることを確認する。
 * - チェック項目としては、ファイル名の文字数や、ファイル名内にXSSやディレクトリトラバーサルの
 * 危険性のある文字列がないか等を判別する。
 * 
 * @see FileNameCheck
 **************************************************************************************
 */ 
public class FileNameCheckValid implements ConstraintValidator<FileNameCheck, Object>{

  private String message;



  //! アノテーションに付与された属性値の取得
  @Override
  public void initialize(FileNameCheck annotation){
    this.message = annotation.message();
  }







  

  /** 
   *************************************************************************************
   * @brief 実際のバリデーション処理を行う。
   * 
   * @details
   * - レスポンスを早くするため、一個でも不合格になる物を見つけたら、後続の処理をスルーする。
   * そしてそのまま不合格とする。
   * 
   * @par 判定の項目
   * -# ファイル名が定義されているか確認。
   * -# ファイル名の長さが50文字以内に収まっていることを確認する。
   * -# HTMLエスケープし、エスケープ後の文字列を比較。変化があれば不合格。
   * -# SQLエスケープし、エスケープ後の文字列を比較。変化があれば不合格。
   * -# ヌル文字が含まれていないか確認する。
   * -# ファイルパス構成文字を調べ、含まれていたら不合格。
   *************************************************************************************
   */
  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {

    if(value == null){
      return true;
    }
    
    BeanWrapper bean = new BeanWrapperImpl(value);
    MultipartFile data = (MultipartFile)bean.getWrappedInstance();

    String name = data.getOriginalFilename();
    name = name == null ? "" : name;

    Boolean judge = true; 

    if(data == null || data.isEmpty()){
      return true;
    }


    judge = judge && !name.isEmpty() && !name.isBlank();
    judge = judge && name.length() <= 50;
    judge = judge && name.equals(HtmlUtils.htmlEscape(name, "UTF-8"));
    judge = judge && name.equals(Escaper.of('\\').escape(name));
    judge = judge && !name.contains("\0");

    if(judge){
      int idx = name.lastIndexOf(".");
      String ext = idx != -1 ? name.substring(idx) : "";
      String file_path = StringUtils.removeEnd(name, ext);

      judge = judge && !(file_path.contains("/") || file_path.contains(".") || 
                         file_path.contains("\\")|| file_path.contains("~"));
    }


    if(judge){ 
      return true;
    }else{
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(message)
             .addConstraintViolation();
      return false;
    }
  }
}
