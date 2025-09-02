package com.kyj.fmk.model;

import lombok.Getter;
import lombok.Setter;
/**
 * 2025-08-31
 * 유리병 편지 답변 글귀 저장을 위한 DTO
 *
 */
@Getter
@Setter
public class ReqBottleRpyDTO {
    private String senderSeqId;
    private String btlLtrNo;
    private String btlRpyContent;
}
