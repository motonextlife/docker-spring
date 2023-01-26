/** 
 ******************************************************************************************
 * @file Role_Enum.java
 * @brief セキュリティに用いる権限情報を定義した列挙型を格納したファイル。
 ******************************************************************************************
 */
package com.springproject.dockerspring.Security;







/** 
 ******************************************************************************************
 * @brief セキュリティに用いる権限情報を定義した列挙型
 * 
 * @details 
 * - データベースから取得した権限情報に関する文字列を、システム上で扱いやすいように列挙型に
 * マッピングするために用いる。
 ******************************************************************************************
 */
public enum Role_Enum{
  ROLE_ADMIN_TRUE,
  ROLE_ADMIN_FALSE,
  ROLE_MEMBER_INFO_NONE,
  ROLE_MEMBER_INFO_BROWS,
  ROLE_MEMBER_INFO_CHANGE,
  ROLE_MEMBER_INFO_DELETE,
  ROLE_MEMBER_INFO_HIST,
  ROLE_MEMBER_INFO_ROLLBACK,
  ROLE_FACILITY_NONE,
  ROLE_FACILITY_BROWS,
  ROLE_FACILITY_CHANGE,
  ROLE_FACILITY_DELETE,
  ROLE_FACILITY_HIST,
  ROLE_FACILITY_ROLLBACK,
  ROLE_MUSICAL_SCORE_NONE,
  ROLE_MUSICAL_SCORE_BROWS,
  ROLE_MUSICAL_SCORE_CHANGE,
  ROLE_MUSICAL_SCORE_DELETE,
  ROLE_MUSICAL_SCORE_HIST,
  ROLE_MUSICAL_SCORE_ROLLBACK,
  ROLE_SOUND_SOURCE_NONE,
  ROLE_SOUND_SOURCE_BROWS,
  ROLE_SOUND_SOURCE_CHANGE,
  ROLE_SOUND_SOURCE_DELETE,
  ROLE_SOUND_SOURCE_HIST,
  ROLE_SOUND_SOURCE_ROLLBACK,
  NO_ROLE;


  




	/** 
	 ******************************************************************************************
	 * @brief 渡された値に応じて、対応する管理者権限の列挙型文字列を取得する。
	 * 
	 * @param[in] auth 管理者権限の有無
	 * 
	 * @return 対応する列挙型を文字列化したもの
	 ******************************************************************************************
	 */
  public static String getAdminRole(Boolean auth){

    if(auth){
      return ROLE_ADMIN_TRUE.name();
    }else{
      return ROLE_ADMIN_FALSE.name();
    }
  }







	/** 
	 ******************************************************************************************
	 * @brief 渡された文字列に応じて、対応する団員管理権限の列挙型文字列を取得する。
	 * 
	 * @param[in] auth 団員管理権限の種別
	 * 
	 * @return 対応する列挙型を文字列化したもの
	 ******************************************************************************************
	 */
  public static String getMemberInfoRole(String auth){

    switch(auth){
      case "none":
        return ROLE_MEMBER_INFO_NONE.name();
      case "brows":
        return ROLE_MEMBER_INFO_BROWS.name();
      case "change":
        return ROLE_MEMBER_INFO_CHANGE.name();
      case "delete":
        return ROLE_MEMBER_INFO_DELETE.name();
      case "hist":
        return ROLE_MEMBER_INFO_HIST.name();
      case "rollback":
        return ROLE_MEMBER_INFO_ROLLBACK.name();
      default:
        throw new IllegalArgumentException("Error location [Role_Enum:getMemberInfoRole]");
    }
  }








	/** 
	 ******************************************************************************************
	 * @brief 渡された文字列に応じて、対応する設備管理権限の列挙型文字列を取得する。
	 * 
	 * @param[in] auth 設備管理権限の種別
	 * 
	 * @return 対応する列挙型を文字列化したもの
	 ******************************************************************************************
	 */
  public static String getFacilityRole(String auth){

    switch(auth){
      case "none":
        return ROLE_FACILITY_NONE.name();
      case "brows":
        return ROLE_FACILITY_BROWS.name();
      case "change":
        return ROLE_FACILITY_CHANGE.name();
      case "delete":
        return ROLE_FACILITY_DELETE.name();
      case "hist":
        return ROLE_FACILITY_HIST.name();
      case "rollback":
        return ROLE_FACILITY_ROLLBACK.name();
      default:
        throw new IllegalArgumentException("Error location [Role_Enum:getFacilityRole]");
    }
  }






	/** 
	 ******************************************************************************************
	 * @brief 渡された文字列に応じて、対応する楽譜管理権限の列挙型文字列を取得する。
	 * 
	 * @param[in] auth 楽譜管理権限の種別
	 * 
	 * @return 対応する列挙型を文字列化したもの
	 ******************************************************************************************
	 */
  public static String getMusicalScoreRole(String auth){

    switch(auth){
      case "none":
        return ROLE_MUSICAL_SCORE_NONE.name();
      case "brows":
        return ROLE_MUSICAL_SCORE_BROWS.name();
      case "change":
        return ROLE_MUSICAL_SCORE_CHANGE.name();
      case "delete":
        return ROLE_MUSICAL_SCORE_DELETE.name();
      case "hist":
        return ROLE_MUSICAL_SCORE_HIST.name();
      case "rollback":
        return ROLE_MUSICAL_SCORE_ROLLBACK.name();
      default:
        throw new IllegalArgumentException("Error location [Role_Enum:getMusicalScoreRole]");
    }
  }







	/** 
	 ******************************************************************************************
	 * @brief 渡された文字列に応じて、対応する音源管理権限の列挙型文字列を取得する。
	 * 
	 * @param[in] auth 音源管理権限の種別
	 * 
	 * @return 対応する列挙型を文字列化したもの
	 ******************************************************************************************
	 */
  public static String getSoundSourceRole(String auth){

    switch(auth){
      case "none":
        return ROLE_SOUND_SOURCE_NONE.name();
      case "brows":
        return ROLE_SOUND_SOURCE_BROWS.name();
      case "change":
        return ROLE_SOUND_SOURCE_CHANGE.name();
      case "delete":
        return ROLE_SOUND_SOURCE_DELETE.name();
      case "hist":
        return ROLE_SOUND_SOURCE_HIST.name();
      case "rollback":
        return ROLE_SOUND_SOURCE_ROLLBACK.name();
      default:
        throw new IllegalArgumentException("Error location [Role_Enum:getSoundSourceRole]");
    }
  }
}