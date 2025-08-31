package com.kyj.fmk.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyj.fmk.core.exception.custom.KyjBizException;
import com.kyj.fmk.core.exception.custom.KyjSysException;
import com.kyj.fmk.core.model.enm.CmErrCode;
import com.kyj.fmk.model.kafka.KafkaBtlFlowDTO;
import com.kyj.fmk.model.kafka.KafkaBtlFlowReDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * 2025-08-31
 * 유리병 편지 이벤트 발행 서비스
 *
 */
@RequiredArgsConstructor
@Service
public class KafkaBtlPublishServiceImpl implements KafkaBtlPublishService {

    private final KafkaTemplate<String,String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    /**
     * 유라병 작성 시 카프카 이벤트 발행  2
     * @param
     */
    @Override
    public void publishBottleFlowRe(KafkaBtlFlowReDTO kafkaBtlFlowReDTO) {
        String data  = null;

        try {

            data = objectMapper.writeValueAsString(kafkaBtlFlowReDTO);

        } catch (JsonProcessingException e) {
            throw new KyjSysException(CmErrCode.CM016);
        }

        if(data == null){
            throw new KyjBizException(CmErrCode.CM019);

        }
        kafkaTemplate.send(kafkaBtlFlowReDTO.getTopic(),data);
    }


    /**
     * 유라병 작성 시 카프카 이벤트 발행
     * @param kafkaBtlFlowDTO
     */
    @Override
    public void publishBottleFlow(KafkaBtlFlowDTO kafkaBtlFlowDTO) {

        String data  = null;

        try {

            data = objectMapper.writeValueAsString(kafkaBtlFlowDTO);

        } catch (JsonProcessingException e) {
            throw new KyjSysException(CmErrCode.CM016);
        }

        if(data == null){
            throw new KyjBizException(CmErrCode.CM019);

        }
        kafkaTemplate.send(kafkaBtlFlowDTO.getTopic(),data);
    }

}
