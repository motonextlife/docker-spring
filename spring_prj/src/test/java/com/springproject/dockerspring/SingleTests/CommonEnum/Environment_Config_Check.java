/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.SingleTests
 * 
 * @brief このシステムのテストのうち、[単体テスト]を格納したパッケージ
 * 
 * @details
 * - このパッケージは、テスト対象クラスが外部のパッケージに依存している場合、モックに置き換えて
 * 行う単体テストを格納する。
 **************************************************************************************
 */ 

/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.SingleTests.CommonEnum
 * 
 * @brief 単体テストのうち、[システム共通の列挙型]に関するテストを格納する。
 * 
 * @details
 * - このパッケージは、システム全体で共通で用いるマップキー文字列などを定義した列挙型をテストする
 * テストを格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.CommonEnum;






/** 
 **************************************************************************************
 * @file Environment_Config_Check.java
 * 
 * @brief 環境変数の取り込みを行うクラスをテストしているクラスを格納したファイルである。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。また、現在位置のパッケージの上の階層の
 * パッケージの説明の記載も行う。
 **************************************************************************************
 */ 
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.springproject.dockerspring.CommonEnum.Environment_Config;

import lombok.RequiredArgsConstructor;

import org.assertj.core.api.SoftAssertions;







/** 
 **************************************************************************************
 * @brief 環境変数の取り込みを行うクラスをテストしているクラス。
 * 
 * @details 
 * - 検査対象のクラス名は[Environment_Config]である。
 * - テスト内容としては、該当の環境変数に値が存在することを確認している。
 * - 環境変数に関しては不定値の為、一致検査はできない。そのため、テストでは存在確認のみ行う事。
 * 
 * @par 大まかなテストの手順
 * - 検査対象のフィールド変数内に格納されている値をゲッターで取得。
 * - 取得した値が存在することを確認する。
 * 
 * @see Environment_Config
 **************************************************************************************
 */ 
@SpringBootTest(classes = Environment_Config.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Environment_Config_Check {

  private final Environment_Config config;
  private final SoftAssertions softly = new SoftAssertions();







  /** 
   ****************************************************************************************
   * @brief フィールド変数に値が存在することを確認する。
   ****************************************************************************************
   */ 
  @Test
  public void 環境変数の存在確認(){
    this.softly.assertThat(this.config.getNormal_list_size()).isNotNull();
    this.softly.assertThat(this.config.getPhoto_list_size()).isNotNull();
    this.softly.assertThat(this.config.getAudio_list_size()).isNotNull();
    this.softly.assertThat(this.config.getPdf_list_size()).isNotNull();
    this.softly.assertThat(this.config.getPhoto_limit()).isNotNull();
    this.softly.assertThat(this.config.getAudio_limit()).isNotNull();
    this.softly.assertThat(this.config.getPdf_limit()).isNotNull();
    this.softly.assertThat(this.config.getCsv_limit()).isNotNull();
    this.softly.assertThat(this.config.getZip_limit()).isNotNull();
    this.softly.assertThat(this.config.getOffset()).isNotNull();
    this.softly.assertThat(this.config.getMax()).isNotNull();
    this.softly.assertThat(this.config.getCsv_error_list_limit()).isNotNull();
    this.softly.assertThat(this.config.getPhoto_save_limit()).isNotNull();
    this.softly.assertThat(this.config.getAudio_save_limit()).isNotNull();
    this.softly.assertThat(this.config.getPdf_save_limit()).isNotNull();
    this.softly.assertThat(this.config.getSamba_url()).isNotNull();
    this.softly.assertThat(this.config.getSamba_username()).isNotNull();
    this.softly.assertThat(this.config.getSamba_password()).isNotNull();
    this.softly.assertThat(this.config.getSamba_min_ver()).isNotNull();
    this.softly.assertThat(this.config.getSamba_max_ver()).isNotNull();
    this.softly.assertThat(this.config.getSamba_timeout()).isNotNull();
    this.softly.assertThat(this.config.getExpiration_time()).isNotNull();
    this.softly.assertThat(this.config.getMember_info_csv()).isNotNull();
    this.softly.assertThat(this.config.getMember_info_pdf()).isNotNull();
    this.softly.assertThat(this.config.getFacility_csv()).isNotNull();
    this.softly.assertThat(this.config.getFacility_pdf()).isNotNull();
    this.softly.assertThat(this.config.getFacility_zip()).isNotNull();
    this.softly.assertThat(this.config.getMusical_score_csv()).isNotNull();
    this.softly.assertThat(this.config.getMusical_score_pdf()).isNotNull();
    this.softly.assertThat(this.config.getMusical_score_zip()).isNotNull();
    this.softly.assertThat(this.config.getSound_source_csv()).isNotNull();
    this.softly.assertThat(this.config.getSound_source_pdf()).isNotNull();
    this.softly.assertThat(this.config.getSound_source_zip()).isNotNull();
        
    this.softly.assertAll();
  }
}