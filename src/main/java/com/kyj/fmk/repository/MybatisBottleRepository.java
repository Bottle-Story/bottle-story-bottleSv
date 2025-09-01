package com.kyj.fmk.repository;

import com.kyj.fmk.mapper.BottleMapper;
import com.kyj.fmk.model.ReqBottleLtrDTO;
import com.kyj.fmk.model.ReqBottleLtrDetialDTO;
import com.kyj.fmk.model.ResBottleLtrDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 2025-08-31
 * 유리병 편지 리포지토리 마이바티스
 *
 */
@Repository
@RequiredArgsConstructor
public class MybatisBottleRepository implements BottleRepository{

    private final BottleMapper bottleMapper;

    /**
     * 유리병 편지 상세 조회
     * @param reqBottleLtrDetialDTO
     * @return
     */
    @Override
    public ResBottleLtrDetail selectBtlLtrDetail(ReqBottleLtrDetialDTO reqBottleLtrDetialDTO) {
        return bottleMapper.selectBtlLtrDetail(reqBottleLtrDetialDTO);
    }

    /**
     * 회원기준 유리병 편지를 조회할때 자신을 제외 등 제외 리스트
     * @param returnList
     * @return
     */
    @Override
    public List<String> selectExcludeBtlList(List<String> returnList,String usrSeqId) {
        return bottleMapper.selectExcludeBtlList(returnList,usrSeqId);
    }

    /**
     * 유리병 편지 작성
     * @param reqBottleLtrDTO
     */
    @Override
    public void insertBtlLtr(ReqBottleLtrDTO reqBottleLtrDTO) {
        bottleMapper.insertBtlLtr(reqBottleLtrDTO);
    }
}
