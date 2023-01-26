/** 
 **************************************************************************************
 * @file Musical_Score_Service.java
 * @brief 主に[楽譜管理]機能で使用する、全体の処理を総合的に実施するサービスに実装する機能を
 * 提供するインターフェースを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Service.ServiceInterface;

import com.springproject.dockerspring.Entity.HistoryEntity.Musical_Score_History;
import com.springproject.dockerspring.Entity.NormalEntity.Musical_Score;
import com.springproject.dockerspring.Form.CsvImplForm.Musical_Score_Form;
import com.springproject.dockerspring.Service.NormalService;








/** 
 **************************************************************************************
 * @brief 主に[楽譜管理]機能で使用する、全体の処理を総合的に実施するサービスに実装する機能を
 * 提供するインターフェース
 * 
 * @details 
 * - このインターフェースは、実装クラスのDIの際にデータ型として登録できるように設ける
 * ものである。
 * - このインターフェース内に実装するメソッドは特にない。すべてスーパーインターフェースに
 * 実装されているものである。
 * 
 * @see NormalService
 * @see Musical_Score
 * @see Musical_Score_History
 * @see Musical_Score_Form
 **************************************************************************************
 */ 
public interface Musical_Score_Service 
    extends NormalService<Musical_Score, Musical_Score_History, Musical_Score_Form>{}