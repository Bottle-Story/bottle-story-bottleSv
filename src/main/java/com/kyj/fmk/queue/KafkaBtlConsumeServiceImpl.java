package com.kyj.fmk.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyj.fmk.core.exception.custom.KyjSysException;
import com.kyj.fmk.core.model.KafkaTopic;
import com.kyj.fmk.core.model.enm.CmErrCode;
import com.kyj.fmk.model.kafka.ConsumeBtlFlowDTO;
import com.kyj.fmk.model.kafka.KafkaBtlFlowReDTO;
import com.kyj.fmk.service.BottleService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 2025-08-31
 * 유리병 편지 컨슘 서비스
 *
 */
@RequiredArgsConstructor
@Service
public class KafkaBtlConsumeServiceImpl implements KafkaBtlConsumeService {

    private final ObjectMapper objectMapper;
    private final KafkaBtlPublishService kafkaBtlPublishService;
    private final BottleService bottleService;

    /**
     * 유리병 작성 후 카프카 컨슘(사용자 조회 및 사용자 기준 btl리스트 조회)
     * @param record
     * @param ack
     */
    @Override
    @KafkaListener(
            topics = KafkaTopic.BOTTLE_BOTTLE_FLOW,
            groupId = "#{ 'bottle.consume.' + T(com.kyj.fmk.core.model.KafkaTopic).BOTTLE_BOTTLE_FLOW }"
    )
    public void consumeBottleFlow(ConsumerRecord<String, String> record, Acknowledgment ack) {

        String json = record.value();
        ConsumeBtlFlowDTO consumeBtlFlowDTO = null;

        try {
             consumeBtlFlowDTO = objectMapper.readValue(json, ConsumeBtlFlowDTO.class);
        } catch (JsonProcessingException e) {
            throw new KyjSysException(CmErrCode.CM016);
        }

        if (consumeBtlFlowDTO == null){
            throw new KyjSysException(CmErrCode.CM020);
        }

       List<String> list =  bottleService.selectMemRangeByBtl(consumeBtlFlowDTO.getBtlLtrNo());

        for (String usrSeqId : list){

         List<String> btlList =   bottleService.selectBtlRangeByMem(usrSeqId);

            KafkaBtlFlowReDTO kafkaBtlFlowReDTO = new KafkaBtlFlowReDTO(btlList,usrSeqId);

            kafkaBtlPublishService.publishBottleFlowRe(kafkaBtlFlowReDTO);

            ack.acknowledge();
        }

    }
}
