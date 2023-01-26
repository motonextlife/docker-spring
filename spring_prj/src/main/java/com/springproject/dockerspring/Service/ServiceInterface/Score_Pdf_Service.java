/** 
 **************************************************************************************
 * @file Score_Pdf_Service.java
 * @brief 主に[楽譜データ情報]機能で使用する、全体の処理を総合的に実施するサービスに実装する機能を
 * 提供するインターフェースを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Service.ServiceInterface;

import com.springproject.dockerspring.Entity.HistoryEntity.Score_Pdf_History;
import com.springproject.dockerspring.Entity.NormalEntity.Score_Pdf;
import com.springproject.dockerspring.Service.BlobService;







/** 
 **************************************************************************************
 * @brief 主に[楽譜データ情報]機能で使用する、全体の処理を総合的に実施するサービスに実装する機能を
 * 提供するインターフェース
 * 
 * @details 
 * - このインターフェースは、実装クラスのDIの際にデータ型として登録できるように設ける
 * ものである。
 * - このインターフェース内に実装するメソッドは特にない。すべてスーパーインターフェースに
 * 実装されているものである。
 * 
 * @see BlobService
 * @see Score_Pdf
 * @see Score_Pdf_History
 **************************************************************************************
 */ 
public interface Score_Pdf_Service extends BlobService<Score_Pdf, Score_Pdf_History>{}