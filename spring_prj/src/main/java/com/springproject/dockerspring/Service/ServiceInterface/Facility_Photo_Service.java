/** 
 **************************************************************************************
 * @file Facility_Photo_Service.java
 * @brief 主に[設備写真データ情報]機能で使用する、全体の処理を総合的に実施するサービスに実装する機能を
 * 提供するインターフェースを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Service.ServiceInterface;

import com.springproject.dockerspring.Entity.HistoryEntity.Facility_Photo_History;
import com.springproject.dockerspring.Entity.NormalEntity.Facility_Photo;
import com.springproject.dockerspring.Service.BlobService;









/** 
 **************************************************************************************
 * @brief 主に[設備写真データ情報]機能で使用する、全体の処理を総合的に実施するサービスに実装する機能を
 * 提供するインターフェース
 * 
 * @details 
 * - このインターフェースは、実装クラスのDIの際にデータ型として登録できるように設ける
 * ものである。
 * - このインターフェース内に実装するメソッドは特にない。すべてスーパーインターフェースに
 * 実装されているものである。
 * 
 * @see BlobService
 * @see Facility_Photo
 * @see Facility_Photo_History
 **************************************************************************************
 */ 
public interface Facility_Photo_Service extends BlobService<Facility_Photo, Facility_Photo_History>{}