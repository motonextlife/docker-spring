/** 
 **************************************************************************************
 * @file Audio_Data_Service.java
 * @brief 主に[音源データ情報]機能で使用する、全体の処理を総合的に実施するサービスに実装する機能を
 * 提供するインターフェースを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Service.ServiceInterface;

import com.springproject.dockerspring.Entity.HistoryEntity.Audio_Data_History;
import com.springproject.dockerspring.Entity.NormalEntity.Audio_Data;
import com.springproject.dockerspring.Service.BlobService;









/** 
 **************************************************************************************
 * @brief 主に[音源データ情報]機能で使用する、全体の処理を総合的に実施するサービスに実装する機能を
 * 提供するインターフェース
 * 
 * @details 
 * - このインターフェースは、実装クラスのDIの際にデータ型として登録できるように設ける
 * ものである。
 * - このインターフェース内に実装するメソッドは特にない。すべてスーパーインターフェースに
 * 実装されているものである。
 * 
 * @see BlobService
 * @see Audio_Data
 * @see Audio_Data_History
 **************************************************************************************
 */ 
public interface Audio_Data_Service extends BlobService<Audio_Data, Audio_Data_History>{}