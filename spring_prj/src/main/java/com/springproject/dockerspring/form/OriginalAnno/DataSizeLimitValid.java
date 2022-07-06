package com.springproject.dockerspring.form.OriginalAnno;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.web.multipart.MultipartFile;

import com.springproject.dockerspring.component.DatatypeEnum;





/***********************************************************/
/*   [DataSizeLimitValid]
/*   アノテーション「DataSizeLimit」に指定された処理内容として
/*   機能する。
/***********************************************************/

public class DataSizeLimitValid implements ConstraintValidator<DataSizeLimit, Object>{

  private String message;

  @Override
  public void initialize(DataSizeLimit annotation){
    this.message = annotation.message();
  }


/****************************************************************/
/*   [isValid]
/*   指定されたファイルの拡張子とデータサイズを確認する。
/*   判別条件は以下のメモ書きに書いてある。
/*   なお、ファイルが指定されていないときは無条件で「条件合致」となる。
/****************************************************************/

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {

    BeanWrapper bean = new BeanWrapperImpl(value);
    MultipartFile data = (MultipartFile)bean.getWrappedInstance();
    String type = data.getContentType();
    String name = data.getOriginalFilename();

    if(data.isEmpty()){
      return true;
    }

    //ファイル名から拡張子を抜き出す。
    int idx = name.lastIndexOf(".");
    name = idx != -1 ? name.substring(idx + 1) : null;
    

    if(type.equals(DatatypeEnum.PHOTO.getType()) 
        && data.getSize() <= DatatypeEnum.PHOTO.getLimit() 
        && name.equals(DatatypeEnum.PHOTO.getExt())){                               //画像は「PNG」のみ「1MB」

      return true;

    }else if(type.equals(DatatypeEnum.MOVIE.getType()) 
        && data.getSize() <= DatatypeEnum.MOVIE.getLimit() 
        && name.equals(DatatypeEnum.MOVIE.getExt())){                               //動画は「MP4」のみ「200MB」

      return true;

    }else if(type.equals(DatatypeEnum.AUDIO.getType()) 
        && data.getSize() <= DatatypeEnum.AUDIO.getLimit() 
        && name.equals(DatatypeEnum.AUDIO.getExt())){                               //音声は「WAVE」のみ「700MB」

      return true;

    }else if(type.equals(DatatypeEnum.PDF.getType()) 
        && data.getSize() <= DatatypeEnum.PDF.getLimit() 
        && name.equals(DatatypeEnum.PDF.getExt())){                               //PDFは「20MB」

      return true;

    }else{
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
      return false;
    }
  }
}
