/** 
 **************************************************************************************
 * @file Sound_Quad_Repository_TestUtils.java
 * @brief 主に[音源管理][音源情報変更履歴][音源データ管理][音源データ変更履歴]、機能のリポジトリの
 * テストで用いる、テスト全般に用いることができるユーティリティクラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils;

import org.assertj.core.api.SoftAssertions;

import com.springproject.dockerspring.Entity.HistoryEntity.Audio_Data_History;
import com.springproject.dockerspring.Entity.HistoryEntity.Sound_Source_History;
import com.springproject.dockerspring.Entity.NormalEntity.Audio_Data;
import com.springproject.dockerspring.Entity.NormalEntity.Sound_Source;










/** 
 **************************************************************************************
 * @brief 主に[音源管理][音源情報変更履歴][音源データ管理][音源データ変更履歴]、機能のリポジトリの
 * テストで用いる、テスト全般に用いることができるユーティリティクラス
 * 
 * @details 
 * - 必要性がないため、インスタンス化を禁止する。
 * - 生成する全ての一時ファイルは、実行権限を剥奪しておく。
 **************************************************************************************
 */ 
public class Sound_Quad_Repository_TestUtils{

  private Sound_Quad_Repository_TestUtils(){
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
   * @param[in] fixed 履歴番号が定まっている場合は「True」、新規追加などで番号が定まってない場合は「False」
   * とする。
   * 
   * @see Sound_Source
   * 
   * @note 同名のメソッドが複数があるが、こちらは主にテキストデータの通常データの比較の際に用いる。
   **********************************************************************************************
   */
  public static void assertAllEquals(Sound_Source compare, Sound_Source result, SoftAssertions softly, Boolean fixed){

    if(fixed){
      softly.assertThat(result.getSerial_num()).isEqualTo(compare.getSerial_num());
    }
    
    softly.assertThat(result.getSerial_num()).isEqualTo(compare.getSerial_num());
    softly.assertThat(result.getSound_id()).isEqualTo(compare.getSound_id());
    softly.assertThat(result.getUpload_date()).isEqualTo(compare.getUpload_date());
    softly.assertThat(result.getSong_title()).isEqualTo(compare.getSong_title());
    softly.assertThat(result.getComposer()).isEqualTo(compare.getComposer());
    softly.assertThat(result.getPerformer()).isEqualTo(compare.getPerformer());
    softly.assertThat(result.getPublisher()).isEqualTo(compare.getPublisher());
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
   * @param[in] fixed シリアルナンバーが定まっている場合は「True」、新規追加などで番号が定まってない場合は
   * 「False」とする。
   * 
   * @see Audio_Data
   * 
   * @note 同名のメソッドが複数があるが、こちらは主にバイナリデータの通常データの比較の際に用いる。
   **********************************************************************************************
   */
  public static void assertAllEquals(Audio_Data compare, Audio_Data result, SoftAssertions softly, Boolean fixed){

    if(fixed){
      softly.assertThat(result.getSerial_num()).isEqualTo(compare.getSerial_num());
    }
    
    softly.assertThat(result.getSound_id()).isEqualTo(compare.getSound_id());
    softly.assertThat(result.getSound_name()).isEqualTo(compare.getSound_name());
    softly.assertThat(result.getAudio_hash()).isEqualTo(compare.getAudio_hash());
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
   * @see Sound_Source_History
   * 
   * @note 同名のメソッドが複数があるが、こちらは主にテキストデータの履歴データの比較の際に用いる。
   **********************************************************************************************
   */
  public static void assertAllEquals(Sound_Source_History compare, Sound_Source_History result, SoftAssertions softly, Boolean fixed){

    if(fixed){
      softly.assertThat(result.getHistory_id()).isEqualTo(compare.getHistory_id());
    }
    
    softly.assertThat(result.getChange_datetime()).isEqualTo(compare.getChange_datetime());
    softly.assertThat(result.getChange_kinds()).isEqualTo(compare.getChange_kinds());
    softly.assertThat(result.getOperation_user()).isEqualTo(compare.getOperation_user());
    softly.assertThat(result.getSerial_num()).isEqualTo(compare.getSerial_num());
    softly.assertThat(result.getSound_id()).isEqualTo(compare.getSound_id());
    softly.assertThat(result.getUpload_date()).isEqualTo(compare.getUpload_date());
    softly.assertThat(result.getSong_title()).isEqualTo(compare.getSong_title());
    softly.assertThat(result.getComposer()).isEqualTo(compare.getComposer());
    softly.assertThat(result.getPerformer()).isEqualTo(compare.getPerformer());
    softly.assertThat(result.getPublisher()).isEqualTo(compare.getPublisher());
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
   * @see Audio_Data_History
   * 
   * @note 同名のメソッドが複数があるが、こちらは主にバイナリデータの履歴データの比較の際に用いる。
   **********************************************************************************************
   */
  public static void assertAllEquals(Audio_Data_History compare, Audio_Data_History result, SoftAssertions softly, Boolean fixed){

    if(fixed){
      softly.assertThat(result.getHistory_id()).isEqualTo(compare.getHistory_id());
    }
    
    softly.assertThat(result.getChange_datetime()).isEqualTo(compare.getChange_datetime());
    softly.assertThat(result.getChange_kinds()).isEqualTo(compare.getChange_kinds());
    softly.assertThat(result.getOperation_user()).isEqualTo(compare.getOperation_user());
    softly.assertThat(result.getSerial_num()).isEqualTo(compare.getSerial_num());
    softly.assertThat(result.getSound_id()).isEqualTo(compare.getSound_id());
    softly.assertThat(result.getSound_name()).isEqualTo(compare.getSound_name());
    softly.assertThat(result.getAudio_hash()).isEqualTo(compare.getAudio_hash());
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
   * @see Sound_Source
   * 
   * @note 同名のメソッドが複数があるが、こちらは主にテキストデータの通常データの移し替えの際に用いる。
   **********************************************************************************************
   */
  public static Sound_Source MockToReal(Sound_Source mock){

    Sound_Source real_ent = new Sound_Source();

    real_ent.setSerial_num(mock.getSerial_num());
    real_ent.setSound_id(mock.getSound_id());
    real_ent.setUpload_date(mock.getUpload_date());
    real_ent.setSong_title(mock.getSong_title());
    real_ent.setComposer(mock.getComposer());
    real_ent.setPerformer(mock.getPerformer());
    real_ent.setPublisher(mock.getPublisher());
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
   * @see Audio_Data
   * 
   * @note 同名のメソッドが複数があるが、こちらは主にバイナリデータの通常データの移し替えの際に用いる。
   **********************************************************************************************
   */
  public static Audio_Data MockToReal(Audio_Data mock){

    Audio_Data real_ent = new Audio_Data();

    real_ent.setSerial_num(mock.getSerial_num());
    real_ent.setSound_id(mock.getSound_id());
    real_ent.setSound_name(mock.getSound_name());
    real_ent.setAudio_hash(mock.getAudio_hash());

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
   * @see Sound_Source_History
   * 
   * @note 同名のメソッドが複数があるが、こちらは主にテキストデータの履歴データの移し替えの際に用いる。
   **********************************************************************************************
   */
  public static Sound_Source_History MockToReal(Sound_Source_History mock){

    Sound_Source_History real_ent = new Sound_Source_History();

    real_ent.setHistory_id(mock.getHistory_id());
    real_ent.setChange_datetime(mock.getChange_datetime());
    real_ent.setChange_kinds(mock.getChange_kinds());
    real_ent.setOperation_user(mock.getOperation_user());
    real_ent.setSerial_num(mock.getSerial_num());
    real_ent.setSound_id(mock.getSound_id());
    real_ent.setUpload_date(mock.getUpload_date());
    real_ent.setSong_title(mock.getSong_title());
    real_ent.setComposer(mock.getComposer());
    real_ent.setPerformer(mock.getPerformer());
    real_ent.setPublisher(mock.getPublisher());
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
   * @see Audio_Data_History
   * 
   * @note 同名のメソッドが複数があるが、こちらは主にバイナリデータの履歴データの移し替えの際に用いる。
   **********************************************************************************************
   */
  public static Audio_Data_History MockToReal(Audio_Data_History mock){

    Audio_Data_History real_ent = new Audio_Data_History();

    real_ent.setHistory_id(mock.getHistory_id());
    real_ent.setChange_datetime(mock.getChange_datetime());
    real_ent.setChange_kinds(mock.getChange_kinds());
    real_ent.setOperation_user(mock.getOperation_user());
    real_ent.setSerial_num(mock.getSerial_num());
    real_ent.setSound_id(mock.getSound_id());
    real_ent.setSound_name(mock.getSound_name());
    real_ent.setAudio_hash(mock.getAudio_hash());
    
    return real_ent;
  }
}
