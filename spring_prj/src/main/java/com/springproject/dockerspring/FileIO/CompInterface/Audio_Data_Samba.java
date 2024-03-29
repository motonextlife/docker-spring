/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.FileIO
 * 
 * @brief このシステムでのファイル入出力関係のクラスを格納するパッケージ
 * 
 * @details
 * - このパッケージでは、CSVファイルやZIPファイルの処理機能、PDFの出力やファイルサーバーへの
 * 通信機能など、ファイル入出力全般の機能を扱う。
 **************************************************************************************
 */ 
/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.FileIO.CompInterface
 * 
 * @brief ファイル入出力関連の処理で使用するインターフェースを格納するパッケージ
 * 
 * @details
 * - このパッケージに一緒くたに全てのインターフェースを格納した理由としては、SpringのDIに対応する
 * 為のメソッドの定義が全くないインターフェースがほとんどで、新たなパッケージを作る必要はないと
 * 判断したためである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.FileIO.CompInterface;





/** 
 **************************************************************************************
 * @file Audio_Data_Samba.java
 * @brief 主に[音源データ情報]機能で使用する、ファイルサーバーとの通信を行うクラスに
 * 機能を提供するインターフェースを格納したファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。また、現在位置のパッケージの一つ上の階層の
 * パッケージの説明も同時に記載する。
 **************************************************************************************
 */ 






/** 
 **************************************************************************************
 * @brief 主に[音源データ情報]機能で使用する、ファイルサーバーとの通信を行うクラスに
 * 機能を提供するインターフェース。
 * 
 * @details 
 * - 通信対象のファイルサーバーは[samba]とし、それに準じた実装を行う。
 * - このインターフェースは、実装クラスのDIの際にデータ型として登録できるように設ける
 * ものである。
 * - このインターフェース内に実装するメソッドは特にない。すべてスーパーインターフェースに
 * 実装されているものである。
 * 
 * @see InOutSamba
 **************************************************************************************
 */ 
public interface Audio_Data_Samba extends InOutSamba{}
