package com.springproject.dockerspring.repository.NormalRepo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springproject.dockerspring.entity.NormalEntity.Usage_Authority;
import com.springproject.dockerspring.repository.NormalRepo.FormEnum.FormUsaAuth;





/**************************************************************/
/*   [UsaAuthRepo]
/*   テーブル「Usage_Authority」へのデータをやり取りや、
/*   エンティティ内のデータの加工を行う。
/**************************************************************/
public interface UsaAuthRepo extends CrudRepository<Usage_Authority, Integer>{


  /***************************************************************/
  /*   [findAllJudge]
  /*   引数で渡された検索ジャンルと検索ワードに従い、検索処理に
  /*   使用するクエリメソッドを選定する。
  /*   また選定の際に、検索結果の並び順（昇順or降順）も指定する。
  /***************************************************************/
  public default Iterable<Usage_Authority> findAllJudge(String word, String subject, Boolean order){


    /**********************************************************************/
    /* 列挙型「FormUsaAuth」を用いて、引数の検索ジャンルが列挙型内に
    /* 定義されているものか判定。
    /* 無ければ、不正な値が渡されたと判断し、空の検索結果を返す。
    /**********************************************************************/
    Optional<FormUsaAuth> name = Arrays.stream(FormUsaAuth.values())
                                            .parallel()
                                            .filter(s -> s.name().equals(subject))
                                            .findFirst();

    if(name.isEmpty()){
      return new ArrayList<Usage_Authority>();
    }


    /**********************************************************************/
    /* 検索ジャンルを元に分岐を行い、該当する検索メソッドを実行する。
    /* なお、分岐先では並び順指定に伴ってさらに分岐を行う。
    /**********************************************************************/
    Boolean bool = false;
    if(word.equals("true")){  //「true」という文字列以外は全て「false」とみなす。
      bool = true;
    }

    Iterable<Usage_Authority> list = null;
    switch(name.get()){
      case All:
        list = order ? findAllOriginal() : findAllOriginalDESC();
        break;
      case Admin:
        list = order ? findAllByAdmin(bool) : findAllByAdminDESC(bool);
        break;
      case Memb_Info_Brows:
        list = order ? findAllByM_i_brows(bool) : findAllByM_i_browsDESC(bool);
        break;
      case Rec_Eval_Brows:
        list = order ? findAllByR_e_brows(bool) : findAllByR_e_browsDESC(bool);
        break;
      case Facility_Brows:
        list = order ? findAllByF_brows(bool) : findAllByF_browsDESC(bool);
        break;
      case Equip_Insp_Brows:
        list = order ? findAllByE_i_brows(bool) : findAllByE_i_browsDESC(bool);
        break;
      case Musical_Score_Brows:
        list = order ? findAllByM_s_brows(bool) :findAllByM_s_browsDESC(bool);
        break;
      case Sound_Source_Brows:
        list = order ? findAllByS_s_brows(bool) : findAllByS_s_browsDESC(bool);
        break;
      case Memb_Info_Change:
        list = order ? findAllByM_i_change(bool) : findAllByM_i_changeDESC(bool);
        break;
      case Rec_Eval_Change:
        list = order ? findAllByR_e_change(bool) : findAllByR_e_changeDESC(bool);
        break;
      case Facility_Change:
        list = order ? findAllByF_change(bool) : findAllByF_changeDESC(bool);
        break;
      case Equip_Insp_Change:
        list = order ? findAllByE_i_change(bool) : findAllByE_i_changeDESC(bool);
        break;
      case Musical_Score_Change:
        list = order ? findAllByM_s_change(bool) :findAllByM_s_changeDESC(bool);
        break;
      case Sound_Source_Change:
        list = order ? findAllByS_s_change(bool) : findAllByS_s_changeDESC(bool);
        break;
      case Memb_Info_Delete:
        list = order ? findAllByM_i_delete(bool) : findAllByM_i_deleteDESC(bool);
        break;
      case Rec_Eval_Delete:
        list = order ? findAllByR_e_delete(bool) : findAllByR_e_deleteDESC(bool);
        break;
      case Facility_Delete:
        list = order ? findAllByF_delete(bool) : findAllByF_deleteDESC(bool);
        break;
      case Equip_Insp_Delete:
        list = order ? findAllByE_i_delete(bool) : findAllByE_i_deleteDESC(bool);
        break;
      case Musical_Score_Delete:
        list = order ? findAllByM_s_delete(bool) :findAllByM_s_deleteDESC(bool);
        break;
      case Sound_Source_Delete:
        list = order ? findAllByS_s_delete(bool) : findAllByS_s_deleteDESC(bool);
        break;
      case Memb_Info_Hist_Brows:
        list = order ? findAllByM_i_hist_brows(bool) : findAllByM_i_hist_browsDESC(bool);
        break;
      case Rec_Eval_Hist_Brows:
        list = order ? findAllByR_e_hist_brows(bool) : findAllByR_e_hist_browsDESC(bool);
        break;
      case Facility_Hist_Brows:
        list = order ? findAllByF_hist_brows(bool) : findAllByF_hist_browsDESC(bool);
        break;
      case Equip_Insp_Hist_Brows:
        list = order ? findAllByE_i_hist_brows(bool) : findAllByE_i_hist_browsDESC(bool);
        break;
      case Musical_Score_Hist_Brows:
        list = order ? findAllByM_s_hist_brows(bool) :findAllByM_s_hist_browsDESC(bool);
        break;
      case Sound_Source_Hist_Brows:
        list = order ? findAllByS_s_hist_brows(bool) : findAllByS_s_hist_browsDESC(bool);
        break;
      case Memb_Info_Hist_Rollback:
        list = order ? findAllByM_i_hist_rollback(bool) : findAllByM_i_hist_rollbackDESC(bool);
        break;
      case Rec_Eval_Hist_Rollback:
        list = order ? findAllByR_e_hist_rollback(bool) : findAllByR_e_hist_rollbackDESC(bool);
        break;
      case Facility_Hist_Rollback:
        list = order ? findAllByF_hist_rollback(bool) : findAllByF_hist_rollbackDESC(bool);
        break;
      case Equip_Insp_Hist_Rollback:
        list = order ? findAllByE_i_hist_rollback(bool) : findAllByE_i_hist_rollbackDESC(bool);
        break;
      case Musical_Score_Hist_Rollback:
        list = order ? findAllByM_s_hist_rollback(bool) :findAllByM_s_hist_rollbackDESC(bool);
        break;
      case Sound_Source_Hist_Rollback:
        list = order ? findAllByS_s_hist_rollback(bool) : findAllByS_s_hist_rollbackDESC(bool);
        break;
    }
    
    return list;
  }


  /************************************************************/
  /*   [findByAuth_id]
  /*   指定された権限番号に該当する情報を個別で取得。
  /************************************************************/
  @Query("SELECT * FROM Usage_Authority WHERE auth_id = :auth_id LIMIT 1")
  Optional<Usage_Authority> findByAuth_id(@Param("auth_id") String auth_id);


  /**************************************************************/
  /*   [findAllOriginal]
  /*   [findAllOriginalDESC]
  /*   全データを権限番号順ですべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /**************************************************************/
  @Query("SELECT * FROM Usage_Authority ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllOriginal();

  @Query("SELECT * FROM Usage_Authority ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllOriginalDESC();


  /*************************************************************************/
  /*   [findAllByAdmin]
  /*   [findAllByAdminDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「権限番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Usage_Authority WHERE admin = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByAdmin(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE admin = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByAdminDESC(@Param("bool") Boolean bool);


  /*************************************************************************/
  /*   [findAllByAuth_id]
  /*   [findAllByAuth_idDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「権限番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Usage_Authority WHERE auth_id LIKE CONCAT('%', :bool, '%') ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByAuth_id(@Param("bool") String bool);

  @Query("SELECT * FROM Usage_Authority WHERE auth_id LIKE CONCAT('%', :bool, '%') ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByAuth_idDESC(@Param("bool") String bool);


  /*************************************************************************/
  /*   [findAllByAuth_name]
  /*   [findAllByAuth_nameDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「権限名五十音順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Usage_Authority WHERE auth_name LIKE CONCAT('%', :bool, '%') ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByAuth_name(@Param("bool") String bool);

  @Query("SELECT * FROM Usage_Authority WHERE auth_name LIKE CONCAT('%', :bool, '%') ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByAuth_nameDESC(@Param("bool") String bool);


  /*************************************************************************/
  /*   [findAllByM_i_brows]
  /*   [findAllByM_i_browsDESC]
  /*   [findAllByM_i_change]
  /*   [findAllByM_i_changeDESC]
  /*   [findAllByM_i_delete]
  /*   [findAllByM_i_deleteDESC]
  /*   [findAllByM_i_hist_brows]
  /*   [findAllByM_i_hist_browsDESC]
  /*   [findAllByM_i_hist_rollback]
  /*   [findAllByM_i_hist_rollbackDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「権限番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Usage_Authority WHERE m_i_brows = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByM_i_brows(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE m_i_brows = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByM_i_browsDESC(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE m_i_change = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByM_i_change(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE m_i_change = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByM_i_changeDESC(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE m_i_delete = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByM_i_delete(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE m_i_delete = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByM_i_deleteDESC(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE m_i_hist_brows = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByM_i_hist_brows(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE m_i_hist_brows = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByM_i_hist_browsDESC(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE m_i_hist_rollback = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByM_i_hist_rollback(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE m_i_hist_rollback = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByM_i_hist_rollbackDESC(@Param("bool") Boolean bool);



  /*************************************************************************/
  /*   [findAllByR_e_brows]
  /*   [findAllByR_e_browsDESC]
  /*   [findAllByR_e_change]
  /*   [findAllByR_e_changeDESC]
  /*   [findAllByR_e_delete]
  /*   [findAllByR_e_deleteDESC]
  /*   [findAllByR_e_hist_brows]
  /*   [findAllByR_e_hist_browsDESC]
  /*   [findAllByR_e_hist_rollback]
  /*   [findAllByR_e_hist_rollbackDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「権限番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Usage_Authority WHERE r_e_brows = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByR_e_brows(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE r_e_brows = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByR_e_browsDESC(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE r_e_change = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByR_e_change(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE r_e_change = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByR_e_changeDESC(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE r_e_delete = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByR_e_delete(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE r_e_delete = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByR_e_deleteDESC(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE r_e_hist_brows = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByR_e_hist_brows(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE r_e_hist_brows = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByR_e_hist_browsDESC(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE r_e_hist_rollback = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByR_e_hist_rollback(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE r_e_hist_rollback = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByR_e_hist_rollbackDESC(@Param("bool") Boolean bool);



  /*************************************************************************/
  /*   [findAllByF_brows]
  /*   [findAllByF_browsDESC]
  /*   [findAllByF_change]
  /*   [findAllByF_changeDESC]
  /*   [findAllByF_delete]
  /*   [findAllByF_deleteDESC]
  /*   [findAllByF_hist_brows]
  /*   [findAllByF_hist_browsDESC]
  /*   [findAllByF_hist_rollback]
  /*   [findAllByF_hist_rollbackDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「権限番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Usage_Authority WHERE f_brows = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByF_brows(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE f_brows = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByF_browsDESC(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE f_change = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByF_change(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE f_change = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByF_changeDESC(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE f_delete = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByF_delete(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE f_delete = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByF_deleteDESC(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE f_hist_brows = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByF_hist_brows(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE f_hist_brows = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByF_hist_browsDESC(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE f_hist_rollback = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByF_hist_rollback(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE f_hist_rollback = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByF_hist_rollbackDESC(@Param("bool") Boolean bool);



  /*************************************************************************/
  /*   [findAllByE_i_brows]
  /*   [findAllByE_i_browsDESC]
  /*   [findAllByE_i_change]
  /*   [findAllByE_i_changeDESC]
  /*   [findAllByE_i_delete]
  /*   [findAllByE_i_deleteDESC]
  /*   [findAllByE_i_hist_brows]
  /*   [findAllByE_i_hist_browsDESC]
  /*   [findAllByE_i_hist_rollback]
  /*   [findAllByE_i_hist_rollbackDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「権限番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Usage_Authority WHERE e_i_brows = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByE_i_brows(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE e_i_brows = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByE_i_browsDESC(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE e_i_change = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByE_i_change(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE e_i_change = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByE_i_changeDESC(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE e_i_delete = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByE_i_delete(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE e_i_delete = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByE_i_deleteDESC(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE e_i_hist_brows = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByE_i_hist_brows(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE e_i_hist_brows = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByE_i_hist_browsDESC(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE e_i_hist_rollback = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByE_i_hist_rollback(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE e_i_hist_rollback = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByE_i_hist_rollbackDESC(@Param("bool") Boolean bool);



  /*************************************************************************/
  /*   [findAllByM_s_brows]
  /*   [findAllByM_s_browsDESC]
  /*   [findAllByM_s_change]
  /*   [findAllByM_s_changeDESC]
  /*   [findAllByM_s_delete]
  /*   [findAllByM_s_deleteDESC]
  /*   [findAllByM_s_hist_brows]
  /*   [findAllByM_s_hist_browsDESC]
  /*   [findAllByM_s_hist_rollback]
  /*   [findAllByM_s_hist_rollbackDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「権限番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Usage_Authority WHERE m_s_brows = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByM_s_brows(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE m_s_brows = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByM_s_browsDESC(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE m_s_change = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByM_s_change(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE m_s_change = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByM_s_changeDESC(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE m_s_delete = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByM_s_delete(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE m_s_delete = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByM_s_deleteDESC(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE m_s_hist_brows = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByM_s_hist_brows(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE m_s_hist_brows = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByM_s_hist_browsDESC(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE m_s_hist_rollback = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByM_s_hist_rollback(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE m_s_hist_rollback = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByM_s_hist_rollbackDESC(@Param("bool") Boolean bool);



  /*************************************************************************/
  /*   [findAllByS_s_brows]
  /*   [findAllByS_s_browsDESC]
  /*   [findAllByS_s_change]
  /*   [findAllByS_s_changeDESC]
  /*   [findAllByS_s_delete]
  /*   [findAllByS_s_deleteDESC]
  /*   [findAllByS_s_hist_brows]
  /*   [findAllByS_s_hist_browsDESC]
  /*   [findAllByS_s_hist_rollback]
  /*   [findAllByS_s_hist_rollbackDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「権限番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Usage_Authority WHERE s_s_brows = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByS_s_brows(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE s_s_brows = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByS_s_browsDESC(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE s_s_change = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByS_s_change(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE s_s_change = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByS_s_changeDESC(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE s_s_delete = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByS_s_delete(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE s_s_delete = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByS_s_deleteDESC(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE s_s_hist_brows = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByS_s_hist_brows(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE s_s_hist_brows = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByS_s_hist_browsDESC(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE s_s_hist_rollback = :bool ORDER BY auth_id ASC")
  Iterable<Usage_Authority> findAllByS_s_hist_rollback(@Param("bool") Boolean bool);

  @Query("SELECT * FROM Usage_Authority WHERE s_s_hist_rollback = :bool ORDER BY auth_id DESC")
  Iterable<Usage_Authority> findAllByS_s_hist_rollbackDESC(@Param("bool") Boolean bool);
}