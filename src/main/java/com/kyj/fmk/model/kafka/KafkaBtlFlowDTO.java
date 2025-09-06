package com.kyj.fmk.model.kafka;

import com.kyj.fmk.core.model.KafkaTopic;
import com.kyj.fmk.core.model.dto.BaseKafkaDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * 2025-08-31
 * 유리병 편지 작성시 이벤트 발행 dto
 *
 */
@Getter
@Setter
public class KafkaBtlFlowDTO extends BaseKafkaDTO {

    String btlLtrNo;

    public KafkaBtlFlowDTO(String btlLtrNo){
        this.btlLtrNo = btlLtrNo;
        super.setFrom("BOTTLE");
        super.setTopic(KafkaTopic.BOTTLE_BOTTLE_FLOW);
    }
}
