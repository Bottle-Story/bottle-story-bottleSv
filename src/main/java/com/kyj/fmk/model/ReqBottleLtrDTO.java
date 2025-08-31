package com.kyj.fmk.model;

import lombok.Getter;
import lombok.Setter;
/**
 * 2025-08-31
 * 유리병 편지 저장을 위한 DTO
 *
 */
@Getter
@Setter
public class ReqBottleLtrDTO {

    private String btlLtrNo;
    private String title;
    private String content;
    private double lat;
    private double lot;
    private String usrSeqId;
}
