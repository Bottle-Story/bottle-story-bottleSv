package com.kyj.fmk.queue;

import com.kyj.fmk.model.kafka.KafkaBtlFlowDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;

/**
 * 2025-08-31
 * 유리병 편지 컨슘 서비스
 *
 */
public interface KafkaBtlConsumeService {
    public void consumeBottleFlow(ConsumerRecord<String, String> record, Acknowledgment ack);

}
