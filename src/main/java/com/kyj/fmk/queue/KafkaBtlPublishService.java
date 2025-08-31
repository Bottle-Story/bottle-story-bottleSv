package com.kyj.fmk.queue;

import com.kyj.fmk.model.kafka.KafkaBtlFlowDTO;
import com.kyj.fmk.model.kafka.KafkaBtlFlowReDTO;

/**
 * 2025-08-31
 * 유리병 편지 이벤트 발행 서비스
 *
 */
public interface KafkaBtlPublishService {
    public void publishBottleFlow(KafkaBtlFlowDTO kafkaBtlFlowDTO);
    public void publishBottleFlowRe(KafkaBtlFlowReDTO kafkaBtlFlowReDTO);

}
