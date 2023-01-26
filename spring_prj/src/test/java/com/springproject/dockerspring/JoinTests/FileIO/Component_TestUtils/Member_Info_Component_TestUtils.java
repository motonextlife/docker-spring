/** 
 **************************************************************************************
 * @file Member_Info_Component_TestUtils.java
 * @brief 主に[団員管理]機能の入出力コンポーネントのテストで用いる、テスト
 * 全般に用いることができるユーティリティクラスを格納したファイル。
 * 
 * @note ここと同名のパッケージが、パッケージ[SingleTests]に存在するが、こちらは依存する
 * クラスを、モックを使わずそのまま使ってテストした、結合テストである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.JoinTests.FileIO.Component_TestUtils;

import com.springproject.dockerspring.Entity.NormalEntity.Member_Info;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.assertj.core.api.SoftAssertions;
import org.springframework.core.io.ClassPathResource;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.jupiter.api.Assertions.assertTrue;











/** 
 **************************************************************************************
 * @brief 主に[団員管理]機能の入出力コンポーネントのテストで用いる、テスト
 * 全般に用いることができるユーティリティクラス
 * 
 * @details 
 * - 必要性がないため、インスタンス化を禁止する。
 * - 生成する全ての一時ファイルは、実行権限を剥奪しておく。
 * 
 * @note ここと同名のパッケージが、パッケージ[SingleTests]に存在するが、こちらは
 * 依存するクラスを、モックを使わずそのまま使ってテストした、結合テストである。
 **************************************************************************************
 */ 
public class Member_Info_Component_TestUtils{

  public static FileAttribute<Set<PosixFilePermission>> perms = PosixFilePermissions
                                                                .asFileAttribute(PosixFilePermissions
                                                                                 .fromString("rw-r--r--"));

  private Member_Info_Component_TestUtils(){
    throw new AssertionError();
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
   * @see Member_Info
   * 
   * @note 同名のメソッドが複数があるが、こちらは主に団員管理に用いる。
   **********************************************************************************************
   */
  public static Member_Info MockToReal(Member_Info mock){

    Member_Info real_ent = new Member_Info();

    real_ent.setSerial_num(mock.getSerial_num());
    real_ent.setMember_id(mock.getMember_id());
    real_ent.setName(mock.getName());
    real_ent.setName_pronu(mock.getName_pronu());
    real_ent.setSex(mock.getSex());
    real_ent.setBirthday(mock.getBirthday());
    real_ent.setFace_photo(mock.getFace_photo());
    real_ent.setJoin_date(mock.getJoin_date());
    real_ent.setRet_date(mock.getRet_date());
    real_ent.setEmail_1(mock.getEmail_1());
    real_ent.setEmail_2(mock.getEmail_2());
    real_ent.setTel_1(mock.getTel_1());
    real_ent.setTel_2(mock.getTel_2());
    real_ent.setAddr_postcode(mock.getAddr_postcode());
    real_ent.setAddr(mock.getAddr());
    real_ent.setPosition(mock.getPosition());
    real_ent.setPosition_arri_date(mock.getPosition_arri_date());
    real_ent.setJob(mock.getJob());
    real_ent.setAssign_dept(mock.getAssign_dept());
    real_ent.setAssign_date(mock.getAssign_date());
    real_ent.setInst_charge(mock.getInst_charge());
    real_ent.setOther_comment(mock.getOther_comment());

    real_ent.stringSetNull();
    
    return real_ent;
  }







  



  /** 
   **********************************************************************************************
   * @brief 比較用の基準エンティティと、テスト結果として出力されたエンティティを比較する。
   * 
   * @details
   * - 遅延アサーションのインスタンスは、このメソッドを呼び出したクラス側の物を使用する。また、アサーションの
   * 最終実行も呼び出し側のクラスで行う。
   * - 検査内容としては、シリアルナンバーは不定値なので対象外としてし、他の値は比較用のエンティティの値と
   * 完全一致する事を確認する。
   *
   * @param[in] compare 比較用エンティティ
   * @param[in] result テスト出力結果エンティティ
   * @param[out] softly 遅延アサーション用のインスタンス
   * 
   * @note 主にCSV入出力のテストでこのメソッドを用いる。
   **********************************************************************************************
   */
  public static void assertAllEquals(Member_Info compare, Member_Info result, SoftAssertions softly){
    softly.assertThat(result.getMember_id()).isEqualTo(compare.getMember_id());
    softly.assertThat(result.getName()).isEqualTo(compare.getName());
    softly.assertThat(result.getName_pronu()).isEqualTo(compare.getName_pronu());
    softly.assertThat(result.getSex()).isEqualTo(compare.getSex());
    softly.assertThat(result.getBirthday()).isEqualTo(compare.getBirthday());
    softly.assertThat(result.getFace_photo()).isNull();
    softly.assertThat(result.getJoin_date()).isEqualTo(compare.getJoin_date());
    softly.assertThat(result.getRet_date()).isEqualTo(compare.getRet_date());
    softly.assertThat(result.getEmail_1()).isEqualTo(compare.getEmail_1());
    softly.assertThat(result.getEmail_2()).isEqualTo(compare.getEmail_2());
    softly.assertThat(result.getTel_1()).isEqualTo(compare.getTel_1());
    softly.assertThat(result.getTel_2()).isEqualTo(compare.getTel_2());
    softly.assertThat(result.getAddr_postcode()).isEqualTo(compare.getAddr_postcode());
    softly.assertThat(result.getAddr()).isEqualTo(compare.getAddr());
    softly.assertThat(result.getPosition()).isEqualTo(compare.getPosition());
    softly.assertThat(result.getPosition_arri_date()).isEqualTo(compare.getPosition_arri_date());
    softly.assertThat(result.getJob()).isEqualTo(compare.getJob());
    softly.assertThat(result.getAssign_dept()).isEqualTo(compare.getAssign_dept());
    softly.assertThat(result.getAssign_date()).isEqualTo(compare.getAssign_date());
    softly.assertThat(result.getInst_charge()).isEqualTo(compare.getInst_charge());
    softly.assertThat(result.getJob()).isEqualTo(compare.getJob());
  }








  /** 
   **********************************************************************************************
   * @brief テスト時にテスト対象クラスに投入するファイルのコピーを作成する。
   * 
   * @details
   * - このメソッドを設けた理由としては、原本のファイルを用いてテストを行ってしまうと、テスト対象クラスには
   * 使用した一時ファイルの削除機能が備わっているため、原本のデータが消えてしまう為。
   * - それを防ぐために、テスト対象クラスにはコピーしたファイルを投入して検査することで、原本データの削除を
   * 発生させないようにしている。
   * - なお、引数のフラグを指定する事で、コピーファイルではなくそのまま原本ファイルを返すことも可能である。
   * 
   * @return 複製した一時ファイル、又は原本ファイル
   *
   * @param[in] target_func 原本ファイルを取り出す対象の機能名
   * @param[in] target_dir 原本ファイルの入っているディレクトリ名
   * @param[in] original_filename 原本ファイル名
   * @param[in] prefix 一時ファイルの作成時に付加する文字列
   * @param[in] extention 一時ファイル作成時の指定拡張子文字列
   * @param[in] source_file 「True」で原本ファイル指定、「False」でコピーファイル指定。
   * 
   * @note 入出力コンポーネントの全体のテストでこのメソッドを用いる。
   * 
   * @throw IOException
   **********************************************************************************************
   */
  public static File generateTmpFile(String target_func, 
                                     String target_dir, 
                                     String original_filename, 
                                     String prefix, 
                                     String extention, 
                                     Boolean source_file) throws IOException{

    Path source = new ClassPathResource(String.format("TestCaseFile/%s/BlobTestCaseData/%s/%s", 
                                        target_func, 
                                        target_dir, 
                                        original_filename))
                                        .getFile()
                                        .toPath();

    if(source_file){
      return source.toFile();
    }

    Path copy = Files.createTempFile(Paths.get("src/main/resources/TempFileBuffer"), 
                                     prefix, 
                                     ".".concat(extention), 
                                     Member_Info_Component_TestUtils.perms);

    return Files.copy(source, copy, REPLACE_EXISTING).toFile();
  }








  /** 
   **********************************************************************************************
   * @brief 指定された二つのファイルのストリームでハッシュ計算を行い、その値を利用して二つのファイルの一致
   * 確認を行う。
   * 
   * @details
   * - 遅延アサーションのインスタンスは、このメソッドを呼び出したクラス側の物を使用する。また、アサーションの
   * 最終実行も呼び出し側のクラスで行う。
   *
   * @param[in] compare 比較用ファイルストリーム
   * @param[in] result テスト出力結果ファイルストリーム
   * @param[out] softly 遅延アサーション用のインスタンス
   * 
   * @note 入出力コンポーネントの全体のテストでこのメソッドを用いる。
   * 
   * @throw IOException
   **********************************************************************************************
   */
  public static void compareHash(InputStream compare, InputStream result, SoftAssertions softly) throws IOException{
    String compare_hash = DigestUtils.sha512_256Hex(compare);
    String result_hash = DigestUtils.sha512_256Hex(result);

    softly.assertThat(compare_hash).isEqualTo(result_hash);
  }




  




  /** 
   **********************************************************************************************
   * @brief テスト後に生成した一時ファイルを削除する。
   *
   * @details
   * - 指定された一時ファイルを削除した後、遅延アサーションを使って確実にファイルが消えていることを確認した
   * 上で処理を終了する。
   * - 遅延アサーションのインスタンスは、このメソッドを呼び出したクラス側の物を使用する。また、アサーションの
   * 最終実行も呼び出し側のクラスで行う。
   * 
   * @param[in] delete_target 削除対象の一時ファイル
   * @param[out] softly 遅延アサーション用のインスタンス
   * 
   * @note 入出力コンポーネントの全体のテストでこのメソッドを用いる。
   * 
   * @throw IOException
   **********************************************************************************************
   */
  public static void deleteTmpFileCheck(File delete_target, SoftAssertions softly) throws IOException{
    Files.deleteIfExists(delete_target.toPath());
    softly.assertThat(Files.exists(delete_target.toPath())).isFalse();
  }










  /** 
   **********************************************************************************************
   * @brief テスト終了後に、一時ファイル用のディレクトリが空っぽであることを確認する。
   *
   * @details
   * - 判定前に1秒間待つ。判定に早く取り掛かりすぎると、一時ファイルの削除処理の途中にもかかわらず判定が
   * 始まってしまい、一時ファイルが削除済みでもエラー扱いとなるため。
   * 
   * @throw InterruptedException
   **********************************************************************************************
   */
  public static void checkTmpDirClear() throws InterruptedException{
    Thread.sleep(1000);
    File tmp_dir = new File("src/main/resources/TempFileBuffer");
    assertTrue(tmp_dir.list().length == 0);
  }
}
