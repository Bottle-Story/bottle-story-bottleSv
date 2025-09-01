package com.kyj.fmk.service;

import com.kyj.fmk.core.exception.custom.KyjSysException;
import com.kyj.fmk.core.model.dto.ResApiDTO;
import com.kyj.fmk.core.model.enm.CmErrCode;
import com.kyj.fmk.core.redis.RedisKey;
import com.kyj.fmk.core.util.GeoUtil;
import com.kyj.fmk.model.ReqBottleLtrDTO;
import com.kyj.fmk.model.ReqBottleLtrDetialDTO;
import com.kyj.fmk.model.ReqMoveBtlLtrGeoDTO;
import com.kyj.fmk.model.ResBottleLtrDetail;
import com.kyj.fmk.model.kafka.KafkaBtlFlowDTO;
import com.kyj.fmk.model.kafka.ReqBtlLtrMemMpng;
import com.kyj.fmk.queue.KafkaBtlPublishService;
import com.kyj.fmk.repository.BottleRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.Metrics;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final KafkaBtlPublishService kafkaBtlPublishService;
    private static final double SEARCH_RADIUS_METERS = 10000; // 10km

    /**
     * 회원 기준 유리병 편지 리스트 조회
     * @param usrSeqId
     * @return
     */
    @Override
    public List<String> selectBtlRangeByMem(String usrSeqId) {
        // 1. 유저 위치 조회
        Point userPoint = redisTemplate.opsForGeo().position(RedisKey.GEO_MEMBER, usrSeqId).get(0);
        if (userPoint == null) return List.of();


        // 2. 반경 10km 내 유리병 조회
        Circle circle = new Circle(userPoint, new Distance(SEARCH_RADIUS_METERS / 1000.0, Metrics.KILOMETERS));

        List<String> returnList = redisTemplate.opsForGeo()
                .radius(RedisKey.GEO_BOTTLE, circle)
                .getContent()
                .stream()
                .map(geoResult -> (String) geoResult.getContent().getName())
                .collect(Collectors.toList());

        //제외리스트
        List<String> excludeList = bottleRepository.selectExcludeBtlList(returnList,usrSeqId);

        // 4. 제외 로직
        if (excludeList != null && !excludeList.isEmpty()) {
            returnList = returnList.stream()
                    .filter(btlId -> !excludeList.contains(btlId))
                    .collect(Collectors.toList());
        }

        return returnList;

    }

    /**
     * 유리병 조회 상세
     * @param reqBottleLtrDetialDTO
     * @return
     */
    @Override
    public ResponseEntity<ResApiDTO<ResBottleLtrDetail>> selectBtlLtrDetail(ReqBottleLtrDetialDTO reqBottleLtrDetialDTO) {
        ResBottleLtrDetail resBottleLtrDetail = bottleRepository.selectBtlLtrDetail(reqBottleLtrDetialDTO);
        return ResponseEntity
                .ok(new ResApiDTO<ResBottleLtrDetail>(resBottleLtrDetail));
    }



    /**
     * 유리병 편지 기준 회원 리스트 조회
     * @param btlLtrNo
     * @return
     */
    @Override
    public List<String> selectMemRangeByBtl(String btlLtrNo) {
        // 1. 만료된 회원 제거
        Set<Object> expiredMembers = redisTemplate.opsForZSet()
                .rangeByScore(RedisKey.WS_SESSION_Z_SET_KEY, 0, System.currentTimeMillis());

        // 2. 유리병 위치 조회
        List<Point> bottlePositions = redisTemplate.opsForGeo().position(RedisKey.GEO_BOTTLE, btlLtrNo);
        if (bottlePositions == null || bottlePositions.isEmpty()) return List.of();

        Point bottlePoint = bottlePositions.get(0);

        // 3. 반경 10km 내 유저 조회
        Circle circle = new Circle(bottlePoint, new Distance(SEARCH_RADIUS_METERS / 1000.0, Metrics.KILOMETERS));
        return redisTemplate.opsForGeo()
                .radius(RedisKey.GEO_MEMBER, circle)
                .getContent()
                .stream()
                .map(geoResult -> (String) geoResult.getContent().getName())
                // 4. 만료 회원 제외
                .filter(memberId -> expiredMembers == null || !expiredMembers.contains(memberId))
                .collect(Collectors.toList());
    }

    /**
     * 유리병 편지 좌표 랜덤 이동
     * @param reqMoveBtlLtrGeoDTO
     */
    @Override
    public void moveBtlLtrGeo(ReqMoveBtlLtrGeoDTO reqMoveBtlLtrGeoDTO) {

        List<Point> points = redisTemplate.opsForGeo().position(RedisKey.GEO_BOTTLE, reqMoveBtlLtrGeoDTO.getBtlLtrNo());
        Point newPoint = null;

        if (points != null && !points.isEmpty()) {
            Point p = points.get(0);
            newPoint=  GeoUtil.randomizeLocationWithinKorea(p.getY(),p.getX(),10);

            redisTemplate.opsForGeo().add(
                    RedisKey.GEO_BOTTLE,
                    new org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation<>(
                            reqMoveBtlLtrGeoDTO.getBtlLtrNo(), // bottleid
                            newPoint
                    )
            );
        } else {
            throw new KyjSysException(CmErrCode.CM004);
        }

    }

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

        KafkaBtlFlowDTO kafkaBtlFlowDTO = new KafkaBtlFlowDTO(reqBottleLtrDTO.getBtlLtrNo());

        kafkaBtlPublishService.publishBottleFlow(kafkaBtlFlowDTO);

        return ResponseEntity
                .ok(new ResApiDTO<>(null));
    }

    /**
     * 유리병 편지 그냥 흘려보내기(조회이력 인서트, 좌표이동, 카프카 전송)
     * @param reqBtlLtrMemMpng
     * @return
     */
    @Override
    @Transactional
    public ResponseEntity<ResApiDTO<?>> btlLtrJustFlow(ReqBtlLtrMemMpng reqBtlLtrMemMpng) {

        //이력 인서트
        bottleRepository.insertBtlLtrMemMpng(reqBtlLtrMemMpng);

        ReqMoveBtlLtrGeoDTO reqMoveBtlLtrGeoDTO = new ReqMoveBtlLtrGeoDTO();
        reqMoveBtlLtrGeoDTO.setBtlLtrNo(reqBtlLtrMemMpng.getBtlLtrNo());
        //좌표이동
        this.moveBtlLtrGeo(reqMoveBtlLtrGeoDTO);

        //유리병 조회 푸시를 위한 카프카  큐 전송
        KafkaBtlFlowDTO kafkaBtlFlowDTO = new KafkaBtlFlowDTO(reqBtlLtrMemMpng.getBtlLtrNo());
        kafkaBtlPublishService.publishBottleFlow(kafkaBtlFlowDTO);

        return ResponseEntity
                .ok(new ResApiDTO<>(null));
    }
}
