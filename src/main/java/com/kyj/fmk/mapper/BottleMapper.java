package com.kyj.fmk.mapper;

import com.kyj.fmk.model.ReqBottleLtrDTO;
import com.kyj.fmk.model.ReqBottleLtrDetialDTO;
import com.kyj.fmk.model.ResBottleLtrDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 2025-08-31
 * 유리병 편지 매퍼
 *
 */
@Mapper
public interface BottleMapper {
    public void insertBtlLtr(ReqBottleLtrDTO reqBottleLtrDTO);
    public List<String> selectExcludeBtlList (List<String> returnList,String usrSeqId);
    public ResBottleLtrDetail selectBtlLtrDetail(ReqBottleLtrDetialDTO reqBottleLtrDetialDTO);
}
