/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.Form.CommonOutputValid
 * 
 * @brief バリデーション機能のうち、出力時バリデーションにおいて全機能共通で使用する項目を
 * 検査する機能を格納したファイル。
 * 
 * @details
 * - このパッケージでは、バイナリデータに付随するファイル名や、履歴情報に付随する共通情報などの
 * バリデーションを行う機能を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.CommonOutputValid;






/** 
 **************************************************************************************
 * @file Blob_Name_Form_ValidatorImpl.java
 * @brief 共通のバイナリデータの出力時のバリデーションにおいて、バリデーションの処理を実行する
 * クラスが格納されたファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.relational.core.dialect.Escaper;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.util.HtmlUtils;

import com.springproject.dockerspring.CommonEnum.UtilEnum.Datatype_Enum;
import com.springproject.dockerspring.CommonEnum.UtilEnum.Regex_Enum;
import com.springproject.dockerspring.Form.FormInterface.Blob_Name_Form_Validator;







/** 
 **************************************************************************************
 * @brief 共通のバイナリデータの出力時のバリデーションにおいて、バリデーションの処理を実行する
 * クラス
 * 
 * @details 
 * - 主に、データ出力時に誤って不正なデータを出力してしまわないようにする目的で用いる。
 * - 使い方としては、このクラスと対になるフォームクラスが存在するので、そのフォームクラスに検査
 * 対象データを格納した状態の物で渡す事で検査を実施する。
 * - 出力時に関しては、入力時に用いる手法でのバリデーションを用いることができない為、[Validator]
 * インターフェースを用いた手法で行う。
 * 
 * @par 使用アノテーション
 * - @Component
 * 
 * @see Blob_Name_Form
 * @see Blob_Name_Form_Validator
 **************************************************************************************
 */ 
@Component
public class Blob_Name_Form_ValidatorImpl implements Blob_Name_Form_Validator {


  //! Validator使用時に必ず実装するメソッドだが、使用しないので説明を割愛。
  @Override
  public boolean supports(Class<?> clazz) {
    return Blob_Name_Form.class.isAssignableFrom(clazz);
  }







  /** 
   **************************************************************
   * @brief 実際のバリデーション処理を行う。各項目の検査処理は
   * 非公開メソッドに切り分けてあるので、そちらを参照。
   * 
   * @note 途中でバリデーションエラーが発生した場合は、判定用の
   * 真偽値が「False」になり、後続の処理は実行されずスルーするように
   * なっている。
   *************************************************************
   */
  @Override
  public void validate(Object target, Errors errors) {
    Blob_Name_Form form = (Blob_Name_Form)target;
    BindingResult error = (BindingResult)errors;
    Boolean judge = true;

    if(judge){validSerialNum(judge, form);}
    if(judge){validId(judge, form);}

    String ext = judge ? validFilename(judge, form) : "";
    if(judge){validExtension(judge, form, ext);}

    if(!judge){
      error.addError(new FieldError(error.getObjectName(), "error", "Blob_Name_Form_Validator"));
    }
  }





  



  /** @name バリデーション用の非公開メソッド */
  /** @{ */

  /** 
   ***************************************************************
   * @brief シリアルナンバー
   * 
   * @details 以下の順番通りの項目で検査を行う。
   * -# 値が空でない事。
   * -# 符号なしで[9桁]までの数値であり、浮動小数点でない事。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] form 検査対象のデータを格納したフォーム
   ***************************************************************
   */
  private void validSerialNum(Boolean judge, Blob_Name_Form form){
    Integer num = form.getSerial_num();
    String serial_num = num != null ? String.valueOf(num) : null;

    if(serial_num == null 
        || !serial_num.matches(Regex_Enum.DIGIT.getRegex())
        || serial_num.length() > 9){

      judge = false;
    }
  }







  /** 
   ***************************************************************
   * @brief 管理番号
   * 
   * @details 以下の順番通りの項目で検査を行う。
   * -# 値が空文字や空白でない事。
   * -# 文字列が[半角英数字＆ハイフン]のみで構成されている事。
   * -# 文字数が[30文字以内]であること。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] form 検査対象のデータを格納したフォーム
   ***************************************************************
   */
  private void validId(Boolean judge, Blob_Name_Form form){
    String id = form.getId();
    if(id == null || !id.matches(Regex_Enum.ALNUM_HYPHEN_NOTNULL.getRegex()) || id.length() > 30){
      judge = false;
    }
  }








  /** 
   ***************************************************************
   * @brief ファイル名
   * 
   * @details 以下の順番通りの項目で検査を行う。
   * -# 値が空文字や空白でない事。
   * -# ファイル名の長さが50文字以内に収まっていることを確認する。
   * -# HTMLエスケープし、エスケープ後の文字列を比較。変化があれば不合格。
   * -# SQLエスケープし、エスケープ後の文字列を比較。変化があれば不合格。
   * -# ファイル名にヌル文字が含まれていないか確認する。
   * -# 渡されたファイル名の中に、ファイルパス構成文字が含まれていないか確認。
   * 
   * @return 抽出した拡張子文字列
   * 
   * @note ファイル名に含まれている拡張子は判定対象外の為、抜き出して判定。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] form 検査対象のデータを格納したフォーム
   ***************************************************************
   */
  private String validFilename(Boolean judge, Blob_Name_Form form){
    String file_name = form.getFile_name();
    
    file_name = file_name != null ? file_name : "";
    judge = judge && !file_name.isEmpty() && !file_name.isBlank();
    
    judge = judge && file_name.length() <= 50;
    
    judge = judge && file_name.equals(HtmlUtils.htmlEscape(file_name, "UTF-8"));
    
    judge = judge && file_name.equals(Escaper.of('\\').escape(file_name));
    
    judge = judge && !file_name.contains("\0");
    
    String filepath = "";
    String ext = "";
    if(judge){
      int idx = file_name.lastIndexOf(".");
      ext = idx != -1 ? file_name.substring(idx) : "";
      filepath = StringUtils.removeEnd(file_name, ext);
    }

    judge = judge && !(filepath.contains("/") || filepath.contains(".") 
                    || filepath.contains("\\")|| filepath.contains("~"));

    return StringUtils.removeStart(ext, ".");
  }







  /** 
   ***************************************************************
   * @brief 拡張子判別
   * 
   * @details 以下の順番通りの項目で検査を行う。
   * -# 含まれている拡張子が指定されたものであること。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] form 検査対象のデータを格納したフォーム
   * @param[in] target_ext 検査対象の抜き出した拡張子文字列
   ***************************************************************
   */
  private void validExtension(Boolean judge, Blob_Name_Form form, String target_ext){
    Datatype_Enum type = form.getType();

    switch(type){
      case AUDIO:
        if(!target_ext.equals(Datatype_Enum.AUDIO.getExtension())){
          judge = false; 
        };
        break;

      case PHOTO:
        if(!target_ext.equals(Datatype_Enum.PHOTO.getExtension())){
          judge = false; 
        };
        break;

      case PDF:
        if(!target_ext.equals(Datatype_Enum.PDF.getExtension())){
          judge = false; 
        };
        break;

      default:
        judge = false;
    }
  }

  /** @} */
}
