package com.springproject.dockerspring.form.OriginalAnno;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.web.multipart.MultipartFile;

import com.springproject.dockerspring.component.DatatypeEnum;





/***********************************************************/
/*   [NotPdfValid]
/*   アノテーション「NotPdf」に指定された処理内容として
/*   機能する。
/***********************************************************/

public class NotPdfValid implements ConstraintValidator<NotPdf, Object>{

  private String message;

  @Override
  public void initialize(NotPdf annotation){
    this.message = annotation.message();
  }


/****************************************************************/
/*   [isValid]
/*   指定されたファイルの拡張子が「pdf」であれば「条件不一致」とする。
/*   なお、ファイルが指定されていないときは無条件で「条件合致」となる。
/****************************************************************/

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {

    BeanWrapper bean = new BeanWrapperImpl(value);
    MultipartFile data = (MultipartFile)bean.getWrappedInstance();
    String type = data.getContentType();

    if(data.isEmpty()){
      return true;
    }

    if(type.equals(DatatypeEnum.PDF.getType())){
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
      return false;
    }else{
      return true;
    }
  }
}
