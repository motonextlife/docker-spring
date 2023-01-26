/** 
 **************************************************************************************
 * @file FileEntity.java
 * @brief バリデーションの対象となる、バイナリデータを格納するエンティティクラスを格納する
 * ファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.BlobImplForm;

import java.io.File;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;







/** 
 **************************************************************************************
 * @brief バリデーションの対象となる、バイナリデータを格納するエンティティクラスを格納
 * 
 * @details 
 * - このクラスを設けた理由としては、バイナリデータの保存の際は、ファイル名とデータファイルを
 * 切り分けてデータベースとファイルサーバーに保存しているため、一つのエンティティにペアで格納して
 * 取り扱う必要がある為。
 * 
 * @par 使用アノテーション
 * - @Data
 * - @AllArgsConstructor
 * - @NoArgsConstructor
 **************************************************************************************
 */ 
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileEntity {

	//! ファイル名
	private String filename;

	//! 中身の入ったファイル本体
	private File tmpfile;
}