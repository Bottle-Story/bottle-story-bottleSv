package com.kyj.fmk.repository;

import com.kyj.fmk.mapper.BottleMapper;
import com.kyj.fmk.model.ReqBottleLtrDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
     * 유리병 편지 작성
     * @param reqBottleLtrDTO
     */
    @Override
    public void insertBtlLtr(ReqBottleLtrDTO reqBottleLtrDTO) {
        bottleMapper.insertBtlLtr(reqBottleLtrDTO);
    }
}
