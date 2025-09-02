package com.kyj.fmk.model.kafka;

import com.kyj.fmk.core.model.KafkaTopic;
import com.kyj.fmk.core.model.dto.BaseKafkaDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * 2025-08-31
 * 유리병 편지 답변글귀작성 시 이벤트 발행 dto
 *
 */
@Getter
@Setter
public class KafkaBtlRpyDTO extends BaseKafkaDTO {

    private String btlLtrNo;
    private char [] contentArr;
    private String usrSeqId;
    public KafkaBtlRpyDTO(String btlLtrNo,char [] contentArr){

        this.btlLtrNo = btlLtrNo;
        this.contentArr = contentArr;
        super.setFrom("BOTTLE");
        super.setTopic(KafkaTopic.BOTTLE_WRITE_REPLY);
    }
}
