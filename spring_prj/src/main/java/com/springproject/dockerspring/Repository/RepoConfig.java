/** 
 **************************************************************************************
 * @file RepoConfig.java
 * @brief リポジトリクラスで共通で利用する、非同期実行時の設定を定義したBeanをDIコンテナに
 * 登録する設定クラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Repository;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;







/** 
 **************************************************************************************
 * @brief リポジトリクラスで共通で利用する、非同期実行時の設定を定義したBeanをDIコンテナに
 * 登録する設定クラス。
 * 
 * @par 使用アノテーション
 * - @Configuration
 **************************************************************************************
 */ 
@Configuration
public class RepoConfig {


  /** 
   **********************************************************************************************
   * @brief DIコンテナに登録される非同期実行設定を記述したBeanのメソッド。
   * 
   * @details
   * - Beanの登録名は[Repo]として、非同期実行の際にはこのBeanを指定する。
   * - 設定するコアスレッドプールのサイズは[20個]までとする。
   * - 登録が可能なキューの許容数は[20個]までとする。
   * - 設定する最大スレッドプールのサイズは[200個]までとする。
   * - 設定するスレッドの名称は、自動で作成されるスレッド名の頭文字に[Repo--]と付加したものである。
   * 
   * @return 設定済みの非同期実行設定クラス
	 * 
	 * @par 使用アノテーション
	 * - @Bean("Repo")
   **********************************************************************************************
   */
	@Bean("Repo")
	public Executor RepoThread(){
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(20);
		executor.setQueueCapacity(20);
		executor.setMaxPoolSize(200);
		executor.setThreadNamePrefix("Repo--");
		executor.initialize();
		return executor;
	}
}