package com.kyj.fmk.service;

import com.kyj.fmk.core.model.dto.ResApiDTO;
import com.kyj.fmk.core.redis.RedisKey;
import com.kyj.fmk.model.ReqBottleLtrDTO;
import com.kyj.fmk.repository.BottleRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * 2025-08-31
 * 유리병 편지 서비스 구현체
 *
 */
@RequiredArgsConstructor
@Service
public class BottleServiceImpl implements BottleService{

    private final BottleRepository bottleRepository;
    private final RedisTemplate<String,Object> redisTemplate;
    /**
     * 유리병 편지를 작성하는 서비스
     * @param reqBottleLtrDTO
     * @return
     */
    @Override
    public ResponseEntity<ResApiDTO<?>> insertBtlLtr(ReqBottleLtrDTO reqBottleLtrDTO) {

        bottleRepository.insertBtlLtr(reqBottleLtrDTO);

        redisTemplate.opsForGeo().add(
                RedisKey.GEO_BOTTLE,
                new org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation<>(
                        reqBottleLtrDTO.getBtlLtrNo(), // bottleid
                        new org.springframework.data.geo.Point(
                                reqBottleLtrDTO.getLot(),  // lng
                                reqBottleLtrDTO.getLat()    // lat
                        )
                )
        );

        return ResponseEntity
                .ok(new ResApiDTO<>(null));
    }
}
