package com.kyj.fmk.mapper;

import com.kyj.fmk.model.ReqBottleLtrDTO;
import org.apache.ibatis.annotations.Mapper;
/**
 * 2025-08-31
 * 유리병 편지 매퍼
 *
 */
@Mapper
public interface BottleMapper {
    public void insertBtlLtr(ReqBottleLtrDTO reqBottleLtrDTO);
}
