/** 
 **************************************************************************************
 * @file Facility_Quad_Repository_TestUtils.java
 * @brief 主に[設備管理][設備情報変更履歴][設備写真データ管理][設備写真データ変更履歴]、機能の
 * リポジトリのテストで用いる、テスト全般に用いることができるユーティリティクラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils;

import com.springproject.dockerspring.Entity.HistoryEntity.Facility_History;
import com.springproject.dockerspring.Entity.HistoryEntity.Facility_Photo_History;
import com.springproject.dockerspring.Entity.NormalEntity.Facility;
import com.springproject.dockerspring.Entity.NormalEntity.Facility_Photo;

import org.assertj.core.api.SoftAssertions;












/** 
 **************************************************************************************
 * @brief 主に[設備管理][設備情報変更履歴][設備写真データ管理][設備写真データ変更履歴]、機能の
 * リポジトリのテストで用いる、テスト全般に用いることができるユーティリティクラス
 * 
 * @details 
 * - 必要性がないため、インスタンス化を禁止する。
 * - 生成する全ての一時ファイルは、実行権限を剥奪しておく。
 **************************************************************************************
 */ 
public class Facility_Quad_Repository_TestUtils{

  private Facility_Quad_Repository_TestUtils(){
    throw new AssertionError();
  }








  /** 
   **********************************************************************************************
   * @brief 比較用の基準エンティティと、テスト結果として出力されたエンティティを比較する。
   * 
   * @details
   * - 遅延アサーションのインスタンスは、このメソッドを呼び出したクラス側の物を使用する。また、アサーションの
   * 最終実行も呼び出し側のクラスで行う。
   *
   * @param[in] compare 比較用エンティティ
   * @param[in] result テスト出力結果エンティティ
   * @param[out] softly 遅延アサーション用のインスタンス
   * @param[in] fixed シリアルナンバーが定まっている場合は「True」、新規追加などで番号が定まってない場合は
   * 「False」とする。
   * 
   * @see Facility_Photo
   * 
   * @note 同名のメソッドが複数があるが、こちらは主にバイナリデータの通常データの比較の際に用いる。
   **********************************************************************************************
   */
  public static void assertAllEquals(Facility_Photo compare, Facility_Photo result, SoftAssertions softly, Boolean fixed){

    if(fixed){
      softly.assertThat(result.getSerial_num()).isEqualTo(compare.getSerial_num());
    }
    
    softly.assertThat(result.getFaci_id()).isEqualTo(compare.getFaci_id());
    softly.assertThat(result.getPhoto_name()).isEqualTo(compare.getPhoto_name());
    softly.assertThat(result.getPhoto_hash()).isEqualTo(compare.getPhoto_hash());
  }









  /** 
   **********************************************************************************************
   * @brief 比較用の基準エンティティと、テスト結果として出力されたエンティティを比較する。
   * 
   * @details
   * - 遅延アサーションのインスタンスは、このメソッドを呼び出したクラス側の物を使用する。また、アサーションの
   * 最終実行も呼び出し側のクラスで行う。
   *
   * @param[in] compare 比較用エンティティ
   * @param[in] result テスト出力結果エンティティ
   * @param[out] softly 遅延アサーション用のインスタンス
   * @param[in] fixed シリアルナンバーが定まっている場合は「True」、新規追加などで番号が定まってない場合は
   * 「False」とする。
   * 
   * @see Facility_History
   * 
   * @note 同名のメソッドが複数があるが、こちらは主にテキストデータの通常データの比較の際に用いる。
   **********************************************************************************************
   */
  public static void assertAllEquals(Facility compare, Facility result, SoftAssertions softly, Boolean fixed){

    if(fixed){
      softly.assertThat(result.getSerial_num()).isEqualTo(compare.getSerial_num());
    }
    
    softly.assertThat(result.getFaci_id()).isEqualTo(compare.getFaci_id());
    softly.assertThat(result.getFaci_name()).isEqualTo(compare.getFaci_name());
    softly.assertThat(result.getBuy_date()).isEqualTo(compare.getBuy_date());
    softly.assertThat(result.getProducer()).isEqualTo(compare.getProducer());
    softly.assertThat(result.getStorage_loc()).isEqualTo(compare.getStorage_loc());
    softly.assertThat(result.getDisp_date()).isEqualTo(compare.getDisp_date());
    softly.assertThat(result.getOther_comment()).isEqualTo(compare.getOther_comment());
  }











  /** 
   **********************************************************************************************
   * @brief 比較用の基準エンティティと、テスト結果として出力されたエンティティを比較する。
   * 
   * @details
   * - 遅延アサーションのインスタンスは、このメソッドを呼び出したクラス側の物を使用する。また、アサーションの
   * 最終実行も呼び出し側のクラスで行う。
   *
   * @param[in] compare 比較用エンティティ
   * @param[in] result テスト出力結果エンティティ
   * @param[out] softly 遅延アサーション用のインスタンス
   * @param[in] fixed 履歴番号が定まっている場合は「True」、新規追加などで番号が定まってない場合は「False」
   * とする。
   * 
   * @see Facility_Photo_History
   * 
   * @note 同名のメソッドが複数があるが、こちらは主にバイナリデータの履歴データの比較の際に用いる。
   **********************************************************************************************
   */
  public static void assertAllEquals(Facility_Photo_History compare, Facility_Photo_History result, SoftAssertions softly, Boolean fixed){

    if(fixed){
      softly.assertThat(result.getHistory_id()).isEqualTo(compare.getHistory_id());
    }
    
    softly.assertThat(result.getChange_datetime()).isEqualTo(compare.getChange_datetime());
    softly.assertThat(result.getChange_kinds()).isEqualTo(compare.getChange_kinds());
    softly.assertThat(result.getOperation_user()).isEqualTo(compare.getOperation_user());
    softly.assertThat(result.getSerial_num()).isEqualTo(compare.getSerial_num());
    softly.assertThat(result.getFaci_id()).isEqualTo(compare.getFaci_id());
    softly.assertThat(result.getPhoto_name()).isEqualTo(compare.getPhoto_name());
    softly.assertThat(result.getPhoto_hash()).isEqualTo(compare.getPhoto_hash());
  }
  









  /** 
   **********************************************************************************************
   * @brief 比較用の基準エンティティと、テスト結果として出力されたエンティティを比較する。
   * 
   * @details
   * - 遅延アサーションのインスタンスは、このメソッドを呼び出したクラス側の物を使用する。また、アサーションの
   * 最終実行も呼び出し側のクラスで行う。
   *
   * @param[in] compare 比較用エンティティ
   * @param[in] result テスト出力結果エンティティ
   * @param[out] softly 遅延アサーション用のインスタンス
   * @param[in] fixed 履歴番号が定まっている場合は「True」、新規追加などで番号が定まってない場合は「False」
   * とする。
   * 
   * @see Facility_History
   * 
   * @note 同名のメソッドが複数があるが、こちらは主にテキストデータの履歴データの比較の際に用いる。
   **********************************************************************************************
   */
  public static void assertAllEquals(Facility_History compare, Facility_History result, SoftAssertions softly, Boolean fixed){

    if(fixed){
      softly.assertThat(result.getHistory_id()).isEqualTo(compare.getHistory_id());
    }
    
    softly.assertThat(result.getChange_datetime()).isEqualTo(compare.getChange_datetime());
    softly.assertThat(result.getChange_kinds()).isEqualTo(compare.getChange_kinds());
    softly.assertThat(result.getOperation_user()).isEqualTo(compare.getOperation_user());
    softly.assertThat(result.getSerial_num()).isEqualTo(compare.getSerial_num());
    softly.assertThat(result.getFaci_id()).isEqualTo(compare.getFaci_id());
    softly.assertThat(result.getFaci_name()).isEqualTo(compare.getFaci_name());
    softly.assertThat(result.getBuy_date()).isEqualTo(compare.getBuy_date());
    softly.assertThat(result.getProducer()).isEqualTo(compare.getProducer());
    softly.assertThat(result.getStorage_loc()).isEqualTo(compare.getStorage_loc());
    softly.assertThat(result.getDisp_date()).isEqualTo(compare.getDisp_date());
    softly.assertThat(result.getOther_comment()).isEqualTo(compare.getOther_comment());
  }











  /** 
   **********************************************************************************************
   * @brief テストケースから取得してきたモックエンティティのデータを、リポジトリで使用する本物のエンティティに
   * 格納しなおす。
   * 
   * @return 移し替えた本物のエンティティ
   *
   * @param[in] mock 移し替えの対象になるモック
   * 
   * @see Facility
   * 
   * @note 同名のメソッドが複数があるが、こちらは主にテキストデータの通常データの移し替えの際に用いる。
   **********************************************************************************************
   */
  public static Facility MockToReal(Facility mock){

    Facility real_ent = new Facility();

    real_ent.setSerial_num(mock.getSerial_num());
    real_ent.setFaci_id(mock.getFaci_id());
    real_ent.setFaci_name(mock.getFaci_name());
    real_ent.setBuy_date(mock.getBuy_date());
    real_ent.setProducer(mock.getProducer());
    real_ent.setStorage_loc(mock.getStorage_loc());
    real_ent.setDisp_date(mock.getDisp_date());
    real_ent.setOther_comment(mock.getOther_comment());
    
    return real_ent;
  }










  /** 
   **********************************************************************************************
   * @brief テストケースから取得してきたモックエンティティのデータを、リポジトリで使用する本物のエンティティに
   * 格納しなおす。
   * 
   * @return 移し替えた本物のエンティティ
   *
   * @param[in] mock 移し替えの対象になるモック
   * 
   * @see Facility_Photo
   * 
   * @note 同名のメソッドが複数があるが、こちらは主にバイナリデータの通常データの移し替えの際に用いる。
   **********************************************************************************************
   */
  public static Facility_Photo MockToReal(Facility_Photo mock){

    Facility_Photo real_ent = new Facility_Photo();

    real_ent.setSerial_num(mock.getSerial_num());
    real_ent.setFaci_id(mock.getFaci_id());
    real_ent.setPhoto_name(mock.getPhoto_name());
    real_ent.setPhoto_hash(mock.getPhoto_hash());
    
    return real_ent;
  }










  /** 
   **********************************************************************************************
   * @brief テストケースから取得してきたモックエンティティのデータを、リポジトリで使用する本物のエンティティに
   * 格納しなおす。
   * 
   * @return 移し替えた本物のエンティティ
   *
   * @param[in] mock 移し替えの対象になるモック
   * 
   * @see Facility_History
   * 
   * @note 同名のメソッドが複数があるが、こちらは主にテキストデータの履歴データの移し替えの際に用いる。
   **********************************************************************************************
   */
  public static Facility_History MockToReal(Facility_History mock){

    Facility_History real_ent = new Facility_History();

    real_ent.setHistory_id(mock.getHistory_id());
    real_ent.setChange_datetime(mock.getChange_datetime());
    real_ent.setChange_kinds(mock.getChange_kinds());
    real_ent.setOperation_user(mock.getOperation_user());
    real_ent.setSerial_num(mock.getSerial_num());
    real_ent.setFaci_id(mock.getFaci_id());
    real_ent.setFaci_name(mock.getFaci_name());
    real_ent.setBuy_date(mock.getBuy_date());
    real_ent.setProducer(mock.getProducer());
    real_ent.setStorage_loc(mock.getStorage_loc());
    real_ent.setDisp_date(mock.getDisp_date());
    real_ent.setOther_comment(mock.getOther_comment());
    
    return real_ent;
  }










  /** 
   **********************************************************************************************
   * @brief テストケースから取得してきたモックエンティティのデータを、リポジトリで使用する本物のエンティティに
   * 格納しなおす。
   * 
   * @return 移し替えた本物のエンティティ
   *
   * @param[in] mock 移し替えの対象になるモック
   * 
   * @see Facility_Photo_History
   * 
   * @note 同名のメソッドが複数があるが、こちらは主にバイナリデータの履歴データの移し替えの際に用いる。
   **********************************************************************************************
   */
  public static Facility_Photo_History MockToReal(Facility_Photo_History mock){

    Facility_Photo_History real_ent = new Facility_Photo_History();

    real_ent.setHistory_id(mock.getHistory_id());
    real_ent.setChange_datetime(mock.getChange_datetime());
    real_ent.setChange_kinds(mock.getChange_kinds());
    real_ent.setOperation_user(mock.getOperation_user());
    real_ent.setSerial_num(mock.getSerial_num());
    real_ent.setFaci_id(mock.getFaci_id());
    real_ent.setPhoto_name(mock.getPhoto_name());
    real_ent.setPhoto_hash(mock.getPhoto_hash());
    
    return real_ent;
  }
}
