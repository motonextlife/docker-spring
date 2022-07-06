package com.springproject.dockerspring.entity.NormalEntity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;

import com.springproject.dockerspring.commonenum.Member_Info_Enum;
import com.springproject.dockerspring.commonenum.System_User_Enum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;





/**************************************************************/
/*   [SysPass_Join]
/*   テーブル「Facility」と「Member_Info」の結合テーブルへの
/*   データをやり取りや、エンティティ内のデータの加工を行う。
/**************************************************************/

@AllArgsConstructor
@Data
@NoArgsConstructor
public class SysPass_Join implements Serializable{


  /*****************************************************/
  /* フィールド変数は全て、対象テーブルのカラム名に
  /* 準じている。それぞれの変数の説明は
  /* 対応するそれぞれのフォームクラスのコメントを参照の事。
  /* （全く同じ変数名で定義してある。）
  /*****************************************************/

  @Id
  private String member_id;

  private String name;
  private String username;
  private String auth_id;
  private Integer fail_count;
  private Boolean locking;




  private final String TRUE_STR = "true";
  private final String FALSE_STR = "false";


  /***************************************************************/
  /*   [stringSetNull]
  /*   呼び出された時点で、変数内に格納されている「文字列型」の
  /*   データが「空文字又は空白のみ」の場合、「Null」に置き換える。
  /*   これは、空白や空文字がデータベース内に入り込み、参照性制約違反
  /*   などの不具合が発生しないよう、確実に空データに「Null」を
  /*   入れるための処置である。
  /***************************************************************/

  public void stringSetNull(){
    
    try{
      if(this.member_id == null || this.member_id.isEmpty() || this.member_id.isBlank()){
        this.member_id = null;
      }
    
      if(this.name == null || this.name.isEmpty() || this.name.isBlank()){
        this.name = null;
      }

      if(this.username == null || this.username.isEmpty() || this.username.isBlank()){
        this.username = null;
      }
    
      if(this.auth_id == null || this.auth_id.isEmpty() || this.auth_id.isBlank()){
        this.auth_id = null;
      }
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [SysPass_Join:stringSetNull]");
    }
  }






  /***************************************************************/
  /*   [makeMap]
  /*   エンティティ内のデータを、ビューへの出力用に文字列に変換し
  /*   マップリストへ格納して返却する。
  /***************************************************************/

  public Map<String, String> makeMap() {
    Map<String, String> map = new HashMap<>();

    stringSetNull();

    try{
      map.put(Member_Info_Enum.Member_Id.getKey(),  
              this.member_id == null ? "" : this.member_id);
              
      map.put(Member_Info_Enum.Name.getKey(),  
              this.name == null ? "" : this.name);
              
      map.put(System_User_Enum.Username.getKey(),  
              this.username == null ? "" : this.username);
              
      map.put(System_User_Enum.Auth_Id.getKey(),  
              this.auth_id == null ? "" : this.auth_id);
              
      map.put(System_User_Enum.Fail_Count.getKey(),  
              this.fail_count == null ? "" : String.valueOf(this.fail_count));
              

      if(this.locking == null){
        map.put(System_User_Enum.Locking.getKey(), TRUE_STR);
      }else{
        map.put(System_User_Enum.Locking.getKey(), this.locking ? TRUE_STR : FALSE_STR);
      }
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [SysPass_Join:makeMap]");
    }
    
    return map;
  }
}