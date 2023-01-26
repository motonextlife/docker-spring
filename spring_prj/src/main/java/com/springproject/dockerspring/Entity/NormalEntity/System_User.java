/** 
 **************************************************************************************
 * @file System_User.java
 * @brief 主に[システムユーザー情報]機能のデータの、データベース保存やデータのやり取りに使用する
 * エンティティを格納したファイルである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Entity.NormalEntity;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.springproject.dockerspring.CommonEnum.NormalEnum.System_User_Enum;
import com.springproject.dockerspring.Entity.EntitySetUp;
import com.springproject.dockerspring.Form.NormalForm.System_User_Form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;







/** 
 **************************************************************************************
 * @brief 主に[システムユーザー情報]機能のデータの、データベース保存やデータのやり取りに使用する
 * エンティティである。
 * 
 * @details 
 * - このエンティティが対応するデータベース内のテーブルの名称は[System_User]である。
 * - 定義しているフィールド変数に関しては、対応するデータベース内のテーブルに対応した名称である。
 * 
 * @par 使用アノテーション
 * - @AllArgsConstructor
 * - @Data
 * - @NoArgsConstructor
 * - @Table("System_User")
 * 
 * @see EntitySetUp
 * @see System_User_Form
 * @see System_User_Enum
 **************************************************************************************
 */ 
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table("System_User")
public class System_User implements EntitySetUp{

  @Id
  //! シリアルナンバー(プライマリ)
  private Integer serial_num;

  //! 団員番号
  private String member_id;

  //! ユーザネーム
  private String username;

  //! パスワード
  private String password;

  //! 権限番号
  private String auth_id;

  //! ログイン失敗回数
  private Integer fail_count;

  //! ロック有無
  private Boolean locking;









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
   * @note
   * - ログイン失敗回数に関しては、新規追加扱いになるので常に0回となる。
   * - ログイン失敗回数が0回なので、ロック有無を「無」になる。
   * 
   * @see System_User_Form
   **********************************************************************************************
   */
  public System_User(System_User_Form form_clazz){

    this.serial_num = form_clazz.getSerial_num();
    this.member_id = form_clazz.getMember_id();
    this.username = form_clazz.getUsername();
    this.password = form_clazz.getPassword();
    this.auth_id = form_clazz.getAuth_id();
    this.fail_count = 0;
    this.locking = false;

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
   **********************************************************************************************
   */
  @Override
  public void stringSetNull(){

    if(this.member_id == null || this.member_id.isEmpty() || this.member_id.isBlank()){
      this.member_id = null;
    }
  
    if(this.username == null || this.username.isEmpty() || this.username.isBlank()){
      this.username = null;
    }
  
    if(this.password == null || this.password.isEmpty() || this.password.isBlank()){
      this.password = null;
    }
  
    if(this.auth_id == null || this.auth_id.isEmpty() || this.auth_id.isBlank()){
      this.auth_id = null;
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
   * @see System_User_Enum
   **********************************************************************************************
   */
  public Map<String, String> makeMap() {
    Map<String, String> convert_map = new HashMap<>();

    stringSetNull();

    convert_map.put(System_User_Enum.Serial_Num.getEnName(), 
                    this.serial_num == null ? "" : String.valueOf(this.serial_num));

    convert_map.put(System_User_Enum.Member_Id.getEnName(), 
                    this.member_id == null ? "" : this.member_id);

    convert_map.put(System_User_Enum.Username.getEnName(), 
                    this.username == null ? "" : this.username);

    convert_map.put(System_User_Enum.Auth_Id.getEnName(), 
                    this.auth_id == null ? "" : this.auth_id);

    if(this.locking == null){
      convert_map.put(System_User_Enum.Locking.getEnName(), "true");
    }else{
      convert_map.put(System_User_Enum.Locking.getEnName(), this.locking ? "true" : "false");
    }
    
    return convert_map;
  }
}