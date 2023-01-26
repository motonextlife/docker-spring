/** 
 **************************************************************************************
 * @file Usage_Authority.java
 * @brief 主に[権限情報]機能のデータの、データベース保存やデータのやり取りに使用する
 * エンティティを格納したファイルである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Entity.NormalEntity;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.springproject.dockerspring.CommonEnum.NormalEnum.Usage_Authority_Enum;
import com.springproject.dockerspring.CommonEnum.UtilEnum.Authority_Kinds_Enum;
import com.springproject.dockerspring.Entity.EntitySetUp;
import com.springproject.dockerspring.Form.NormalForm.Usage_Authority_Form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;







/** 
 **************************************************************************************
 * @brief 主に[権限情報]機能のデータの、データベース保存やデータのやり取りに使用する
 * エンティティである。
 * 
 * @details 
 * - このエンティティが対応するデータベース内のテーブルの名称は[Usage_Authority]である。
 * - 定義しているフィールド変数に関しては、対応するデータベース内のテーブルに対応した名称である。
 * 
 * @par 使用アノテーション
 * - @AllArgsConstructor
 * - @Data
 * - @NoArgsConstructor
 * - @Table("Usage_Authority")
 * 
 * @see EntitySetUp
 * @see Usage_Authority_Form
 * @see Usage_Authority_Enum
 **************************************************************************************
 */ 
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table("Usage_Authority")
public class Usage_Authority implements EntitySetUp{

  @Id
  //! シリアルナンバー(プライマリ)
  private Integer serial_num;

  //! 権限番号
  private String auth_id;

  //! 権限名
  private String auth_name;

  //! 管理者権限
  private Boolean admin;

  //! 団員管理権限
  private String member_info;

  //! 設備管理権限
  private String facility;

  //! 楽譜管理権限
  private String musical_score;

  //! 音源管理権限
  private String sound_source;





  
  


  /** 
   **********************************************************************************************
   * @brief 対になっているフォームクラスから、この通常データ用エンティティにデータを移し替える。
   * 
   * @param[in] form_clazz この通常データ用エンティティと対となる、フォームクラス
   * 
   * @par 処理の大まかな流れ
   * -# フォームクラスからデータを抽出し、この通常データ用エンティティの同名のフィールド変数に格納する。
   * -# 移し替えた後、Null初期化を実行する。
   * 
   * @see Usage_Authority_Form
   **********************************************************************************************
   */
  public Usage_Authority(Usage_Authority_Form form_clazz){

    this.serial_num = form_clazz.getSerial_num();
    this.auth_id = form_clazz.getAuth_id();
    this.auth_name = form_clazz.getAuth_name();
		this.admin = form_clazz.getAdmin();
    this.member_info = form_clazz.getMember_info();
    this.facility = form_clazz.getFacility();
    this.musical_score = form_clazz.getMusical_score();
    this.sound_source = form_clazz.getSound_source();
    
    stringSetNull();
  }








  /** 
   **********************************************************************************************
   * @brief エンティティ内に格納されているデータのうち、文字列型かつ空文字や空白のデータをNullに初期化する。
   * 
   * @details このメソッドは以下の理由で設けた。
   * - 空文字や空白のままデータベースに保存されると、他のテーブルとの参照制約に悪影響を及ぼすため。
   * 
   * @par 処理の大まかな流れ
   * -# 各フィールド変数のうち、文字列型の物のみを対象とする。
   * -# フィールド内に格納されている文字列が「空白」又は「空文字」か判別する。
   * -# 該当すれば、内容を「Null」に置き換える。
   * -# 権限情報に関しては、初期値が「Null」の場合は、「権限なし」として扱う。
   **********************************************************************************************
   */
  @Override
  public void stringSetNull(){

    if(this.auth_id == null || this.auth_id.isEmpty() || this.auth_id.isBlank()){
      this.auth_id = null;
    }
    
    if(this.auth_name == null || this.auth_name.isEmpty() || this.auth_name.isBlank()){
      this.auth_name = null;
    }
    
    this.admin = this.admin == null ? false : this.admin;
    
    if(this.member_info == null || this.member_info.isEmpty() || this.member_info.isBlank()){
      this.member_info = Authority_Kinds_Enum.NONE.getKinds();
    }
    
    if(this.facility == null || this.facility.isEmpty() || this.facility.isBlank()){
      this.facility = Authority_Kinds_Enum.NONE.getKinds();
    }
    
    if(this.musical_score == null || this.musical_score.isEmpty() || this.musical_score.isBlank()){
      this.musical_score = Authority_Kinds_Enum.NONE.getKinds();
    }
    
    if(this.sound_source == null || this.sound_source.isEmpty() || this.sound_source.isBlank()){
      this.sound_source = Authority_Kinds_Enum.NONE.getKinds();
    }
  }







  /** 
   **********************************************************************************************
   * @brief エンティティ内に格納されているデータを全て文字列型に変換し、マップリストへ格納して返却する。
   * 
   * @details
   * - 主に、フロント側の画面への出力の際に用いるデータを作成する目的として、このメソッドを使用する。
   * - 「Null」が入っているデータは、全て「空文字」に変換される。
   * 
   * @return 文字列に変換が終わり、キー名を項目の英語名としたマップリスト。
   * 
   * @par 処理の大まかな流れ
   * -# 格納済みのデータを全てNull初期化する。
   * -# フィールドに格納されているデータが「Null」であれば、例外なく「空文字」をマップリストに登録。
   * -# 文字列型データはそのままマップリストに登録し、それ以外のデータ型は文字列型に変換して登録。
   * 
   * @see Usage_Authority_Enum
   **********************************************************************************************
   */
  @Override
  public Map<String, String> makeMap() {
    Map<String, String> convert_map = new HashMap<>();

    stringSetNull();

		convert_map.put(Usage_Authority_Enum.Serial_Num.getEnName(), 
							      this.serial_num == null ? "" : String.valueOf(this.serial_num));
      
		convert_map.put(Usage_Authority_Enum.Auth_Id.getEnName(), 
							      this.auth_id == null ? "" : this.auth_id);
      
		convert_map.put(Usage_Authority_Enum.Auth_Name.getEnName(), 
							      this.auth_name == null ? "" : this.auth_name);
      
		convert_map.put(Usage_Authority_Enum.Admin.getEnName(), 
							      this.admin ? Authority_Kinds_Enum.ADMIN_TRUE.getKinds() : Authority_Kinds_Enum.ADMIN_FALSE.getKinds()); 
			
		convert_map.put(Usage_Authority_Enum.Member_Info.getEnName(), this.member_info);

		convert_map.put(Usage_Authority_Enum.Facility.getEnName(), this.facility);

		convert_map.put(Usage_Authority_Enum.Musical_Score.getEnName(), this.musical_score);

		convert_map.put(Usage_Authority_Enum.Sound_Source.getEnName(), this.sound_source);

    return convert_map;
  }
}