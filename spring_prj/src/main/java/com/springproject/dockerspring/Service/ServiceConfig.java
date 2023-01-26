/** 
 **************************************************************************************
 * @file ServiceConfig.java
 * @brief サービスクラスで共通で利用する、非同期実行時の設定を定義したBeanをDIコンテナに
 * 登録する設定クラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Service;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;






/** 
 **************************************************************************************
 * @brief サービスクラスで共通で利用する、非同期実行時の設定を定義したBeanをDIコンテナに
 * 登録する設定クラス。
 * 
 * @par 使用アノテーション
 * - @Configuration
 **************************************************************************************
 */ 
@Configuration
public class ServiceConfig {


  /** 
   **********************************************************************************************
   * @brief DIコンテナに登録される非同期実行設定を記述したBeanのメソッド。
   * 
   * @details
   * - Beanの登録名は[Service]として、非同期実行の際にはこのBeanを指定する。
   * - 設定するコアスレッドプールのサイズは[20個]までとする。
   * - 登録が可能なキューの許容数は[20個]までとする。
   * - 設定する最大スレッドプールのサイズは[200個]までとする。
   * - 設定するスレッドの名称は、自動で作成されるスレッド名の頭文字に[Service--]と付加したものである。
   * 
   * @return 設定済みの非同期実行設定クラス
	 * 
	 * @par 使用アノテーション
	 * - @Bean("Service")
   **********************************************************************************************
   */
	@Bean("Service")
	public Executor formThread(){
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(20);
		executor.setQueueCapacity(20);
		executor.setMaxPoolSize(200);
		executor.setThreadNamePrefix("Service--");
		executor.initialize();
		return executor;
	}
}