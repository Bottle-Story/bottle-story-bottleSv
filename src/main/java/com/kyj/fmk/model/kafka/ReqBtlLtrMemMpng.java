package com.kyj.fmk.model.kafka;

import lombok.Getter;
import lombok.Setter;

/**
 * 2025-08-31
 * 유리병 편지 조회이력 매핑 인서트
 *
 */
@Getter
@Setter
public class ReqBtlLtrMemMpng {

    private String readSeqId;
    private String btlLtrNo;
}
