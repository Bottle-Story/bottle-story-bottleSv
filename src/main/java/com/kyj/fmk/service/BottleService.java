package com.kyj.fmk.service;

import com.kyj.fmk.core.model.dto.ResApiDTO;
import com.kyj.fmk.model.ReqBottleLtrDTO;
import org.springframework.http.ResponseEntity;
/**
 * 2025-08-31
 * 유리병 편지 서비스
 *
 */
public interface BottleService {


    public ResponseEntity<ResApiDTO<?>> insertBtlLtr(ReqBottleLtrDTO reqBottleLtrDTO);
}
