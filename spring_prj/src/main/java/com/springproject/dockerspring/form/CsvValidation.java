package com.springproject.dockerspring.form;

import java.util.List;
import java.util.Map;



/********************************************************/
/*   [CsvValidation]
/*   このインターフェースは、フォームクラスに対して
/*   CSVデータのバリデーションチェックの機能を実装する。
/********************************************************/
public interface CsvValidation {


  /***************************************************************/
  /*   [csvChecker]
  /*   受け取ったデータ内の文字列全てをバリデーションチェックし、
  /*   エラーがあったデータは、該当データのCSV内での行数と列数を
  /*   エラーリストとして出力する。
  /***************************************************************/
  List<List<Integer>> csvChecker(List<Map<String, String>> inputlist) throws Exception;
}
