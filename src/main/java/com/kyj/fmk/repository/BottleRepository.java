package com.kyj.fmk.repository;

import com.kyj.fmk.model.ReqBottleLtrDTO;
import com.kyj.fmk.model.ReqBottleLtrDetialDTO;
import com.kyj.fmk.model.ResBottleLtrDetail;

import java.util.List;

/**
 * 2025-08-31
 * 유리병 편지 리포지토리
 *
 */
public interface BottleRepository {
    public void insertBtlLtr(ReqBottleLtrDTO reqBottleLtrDTO);
    public List<String> selectExcludeBtlList (List<String> returnList,String usrSeqId);
    public ResBottleLtrDetail selectBtlLtrDetail(ReqBottleLtrDetialDTO reqBottleLtrDetialDTO);



}
