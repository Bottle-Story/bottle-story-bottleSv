package com.kyj.fmk.model.kafka;

import com.kyj.fmk.core.model.KafkaTopic;
import com.kyj.fmk.core.model.dto.BaseKafkaDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 2025-08-31
 * 유리병 편지 작성시 이벤트 발행 dto
 *
 */
@Getter
@Setter
public class KafkaBtlFlowReDTO extends BaseKafkaDTO {

    List<String> btlLtrNoList;
    private String usrSeqId;

    public KafkaBtlFlowReDTO(List<String> btlLtrNoList,String usrSeqId){
        this.btlLtrNoList = btlLtrNoList;
        this.usrSeqId = usrSeqId;
        super.setFrom("BOTTLE");
        super.setTopic(KafkaTopic.BOTTLE_BOTTLE_FLOW_RE_1);
    }
}
