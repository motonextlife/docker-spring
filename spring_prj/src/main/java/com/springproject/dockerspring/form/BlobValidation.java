package com.springproject.dockerspring.form;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


/********************************************************/
/*   [BlobValidation]
/*   このインターフェースは、フォームクラスに対して
/*   ZIPファイルから抽出したバイナリデータの
/*   バリデーションチェックの機能を実装する。
/********************************************************/
public interface BlobValidation {


  /***************************************************************/
  /*   [blobChecker]
  /*   受け取ったデータの、拡張子やMIMEタイプ、データサイズを
  /*   検証する。
  /***************************************************************/
  Boolean blobChecker(Map<String, byte[]> inputblob) throws InterruptedException, ExecutionException;


  /***************************************************************/
  /*   [parallelCheck]
  /*   実際のチェック処理を並列に実行するための
  /*   メソッドである。
  /***************************************************************/
  CompletableFuture<Boolean> parallelCheck(String filename, byte[] data);
}
