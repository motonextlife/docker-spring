/** 
 **************************************************************************************
 * @file FuncWrap.java
 * @brief サービス内で使用する関数型インターフェース全般において、TryCatchを使うことをなく記述
 * できるようにラップするクラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Service;

import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.springproject.dockerspring.FileIO.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.Service.OriginalException.DataEmptyException;
import com.springproject.dockerspring.Service.OriginalException.OutputDataFailedException;
import com.springproject.dockerspring.Service.OriginalException.OverCountException;











/** 
 **********************************************************************************************
 * @brief サービス内で使用する関数型インターフェース全般において、TryCatchを使うことをなく記述
 * できるようにラップするクラス
 * 
 * @details
 * - サービスクラスは、処理の集約の為に関数型インターフェースを多用しているが、検査例外をラムダ式の外部に
 * 出すことができず、TryCatch構文が増えてダサくなってしまう。
 * - このクラスでは、使用する関数型インターフェースをあらかじめラップした静的メソッドを作成しておき、
 * そのメソッドを介して関数型インターフェースを用いることでスマートに書けるようにする。
 * 
 * @par 非検査例外と検査例外の対応表
 * - DataEmptyException -> NoSuchElementException
 * - OverCountException -> IndexOutOfBoundsException
 * - InputFileDifferException -> IllegalArgumentException
 * - OutputDataFailedException -> SecurityException
 * - Exception -> RuntimeException
 **********************************************************************************************
 */
public class FuncWrap{


  /** 
   **********************************************************************************************
   * @brief 既存の関数型インターフェース[Function]をラップする、静的メソッド。
   **********************************************************************************************
   */
  public static <T, R> Function<T, R> throwFunction(ThrowFunction<T, R> target_func){
    return s -> {
      try{
        return target_func.apply(s);
      }catch(DataEmptyException e){
        throw new NoSuchElementException("\n" + e);
      }catch(OverCountException e){
        throw new IndexOutOfBoundsException("\n" + e);
      }catch(InputFileDifferException e){
        throw new IllegalArgumentException("\n" + e);
      }catch(OutputDataFailedException e){
        throw new SecurityException("\n" + e);
      }catch(Exception e){
        throw new RuntimeException("\n" + e);
      }
    };
  }



  /** 
   **********************************************************************************************
   * @brief 既存の関数型インターフェース[Predicate]をラップする、静的メソッド。
   **********************************************************************************************
   */
  public static <T, R> Predicate<T> throwPredicate(ThrowPredicate<T> target_func){
    return s -> {
      try{
        return target_func.test(s);
      }catch(DataEmptyException e){
        throw new NoSuchElementException("\n" + e);
      }catch(OverCountException e){
        throw new IndexOutOfBoundsException("\n" + e);
      }catch(InputFileDifferException e){
        throw new IllegalArgumentException("\n" + e);
      }catch(OutputDataFailedException e){
        throw new SecurityException("\n" + e);
      }catch(Exception e){
        throw new RuntimeException("\n" + e);
      }
    };
  }
  




  /** 
   **********************************************************************************************
   * @brief 既存の関数型インターフェース[Consumer]をラップする、静的メソッド。
   **********************************************************************************************
   */
  public static <T, R> Consumer<T> throwConsumer(ThrowConsumer<T> target_func){
    return s -> {
      try{
        target_func.accept(s);
      }catch(DataEmptyException e){
        throw new NoSuchElementException("\n" + e);
      }catch(OverCountException e){
        throw new IndexOutOfBoundsException("\n" + e);
      }catch(InputFileDifferException e){
        throw new IllegalArgumentException("\n" + e);
      }catch(OutputDataFailedException e){
        throw new SecurityException("\n" + e);
      }catch(Exception e){
        throw new RuntimeException("\n" + e);
      }
    };
  }






  /** 
   **********************************************************************************************
   * @brief 既存の関数型インターフェース[Supplier]をラップする、静的メソッド。
   **********************************************************************************************
   */
  public static <T, R> Supplier<T> throwSupplier(ThrowSupplier<T> target_func){
    return () -> {
      try{
        return target_func.get();
      }catch(DataEmptyException e){
        throw new NoSuchElementException("\n" + e);
      }catch(OverCountException e){
        throw new IndexOutOfBoundsException("\n" + e);
      }catch(InputFileDifferException e){
        throw new IllegalArgumentException("\n" + e);
      }catch(OutputDataFailedException e){
        throw new SecurityException("\n" + e);
      }catch(Exception e){
        throw new RuntimeException("\n" + e);
      }
    };
  }






  /** 
   **********************************************************************************************
   * @brief 既存の関数型インターフェース[BiPredicate]をラップする、静的メソッド。
   **********************************************************************************************
   */
  public static <T, R> BiPredicate<T, R> throwBiPredicate(ThrowBiPredicate<T, R> target_func){
    return (s, i) -> {
      try{
        return target_func.test(s, i);
      }catch(DataEmptyException e){
        throw new NoSuchElementException("\n" + e);
      }catch(OverCountException e){
        throw new IndexOutOfBoundsException("\n" + e);
      }catch(InputFileDifferException e){
        throw new IllegalArgumentException("\n" + e);
      }catch(OutputDataFailedException e){
        throw new SecurityException("\n" + e);
      }catch(Exception e){
        throw new RuntimeException("\n" + e);
      }
    };
  }





  /** 
   **********************************************************************************************
   * @brief 既存の関数型インターフェース[BiFunction]をラップする、静的メソッド。
   **********************************************************************************************
   */
  public static <T, R, U> BiFunction<T, R, U> throwBiFunction(ThrowBiFunction<T, R, U> target_func){
    return (s, i) -> {
      try{
        return target_func.apply(s, i);
      }catch(DataEmptyException e){
        throw new NoSuchElementException("\n" + e);
      }catch(OverCountException e){
        throw new IndexOutOfBoundsException("\n" + e);
      }catch(InputFileDifferException e){
        throw new IllegalArgumentException("\n" + e);
      }catch(OutputDataFailedException e){
        throw new SecurityException("\n" + e);
      }catch(Exception e){
        throw new RuntimeException("\n" + e);
      }
    };
  }






  /** 
   **********************************************************************************************
   * @brief 既存の関数型インターフェース[Function]の代わりとして、例外を投げることができるように作成した物。
   * 
   * @par 使用アノテーション
   * - @FunctionalInterface
   **********************************************************************************************
   */
  @FunctionalInterface
  public interface ThrowFunction<T, R> {
    R apply(T arg) throws DataEmptyException, OverCountException, InputFileDifferException, OutputDataFailedException, Exception;
  }




  /** 
   **********************************************************************************************
   * @brief 既存の関数型インターフェース[Predicate]の代わりとして、例外を投げることができるように作成した物。
   * 
   * @par 使用アノテーション
   * - @FunctionalInterface
   **********************************************************************************************
   */
  @FunctionalInterface
  public interface ThrowPredicate<T> {
    Boolean test(T arg) throws DataEmptyException, OverCountException, InputFileDifferException, OutputDataFailedException, Exception;
  }




  /** 
   **********************************************************************************************
   * @brief 既存の関数型インターフェース[Consumer]の代わりとして、例外を投げることができるように作成した物。
   * 
   * @par 使用アノテーション
   * - @FunctionalInterface
   **********************************************************************************************
   */
  @FunctionalInterface
  public interface ThrowConsumer<T> {
    void accept(T arg) throws DataEmptyException, OverCountException, InputFileDifferException, OutputDataFailedException, Exception;
  }





  /** 
   **********************************************************************************************
   * @brief 既存の関数型インターフェース[Supplier]の代わりとして、例外を投げることができるように作成した物。
   * 
   * @par 使用アノテーション
   * - @FunctionalInterface
   **********************************************************************************************
   */
  @FunctionalInterface
  public interface ThrowSupplier<T> {
    T get() throws DataEmptyException, OverCountException, InputFileDifferException, OutputDataFailedException, Exception;
  }




  /** 
   **********************************************************************************************
   * @brief 既存の関数型インターフェース[BiPredicate]の代わりとして、例外を投げることができるように作成した物。
   * 
   * @par 使用アノテーション
   * - @FunctionalInterface
   **********************************************************************************************
   */
  @FunctionalInterface
  public interface ThrowBiPredicate<T, R> {
    Boolean test(T arg_1, R arg_2) throws DataEmptyException, OverCountException, InputFileDifferException, OutputDataFailedException, Exception;
  }




  /** 
   **********************************************************************************************
   * @brief 既存の関数型インターフェース[BiFunction]の代わりとして、例外を投げることができるように作成した物。
   * 
   * @par 使用アノテーション
   * - @FunctionalInterface
   **********************************************************************************************
   */
  @FunctionalInterface
  public interface ThrowBiFunction<T, R, U> {
    U apply(T arg_1, R arg_2) throws DataEmptyException, OverCountException, InputFileDifferException, OutputDataFailedException, Exception;
  }
}
