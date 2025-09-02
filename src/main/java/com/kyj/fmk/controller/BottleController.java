package com.kyj.fmk.controller;

import com.kyj.fmk.core.model.dto.ResApiDTO;
import com.kyj.fmk.model.*;
import com.kyj.fmk.model.kafka.ReqBtlLtrMemMpng;
import com.kyj.fmk.sec.dto.oauth2.CustomOAuth2User;
import com.kyj.fmk.service.BottleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 2025-08-31
 * 유리병 편지 컨트롤러
 *
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bottle")
public class BottleController {

    private final BottleService bottleService;

    /**
     * 루트 진입 시 유리병 조회
     * @param customOAuth2User
     * @return
     */
    @GetMapping("/letter")
    public ResponseEntity<ResApiDTO<?>> selectBottleLtr(@AuthenticationPrincipal CustomOAuth2User customOAuth2User){

        String usrSeqId = String.valueOf(customOAuth2User.getUsrSeqId());

        List<String> list = bottleService.selectBtlRangeByMem(usrSeqId);
        List<ResBtlIdDTO> result = new ArrayList<>();

        for (String id: list){
            ResBtlIdDTO resBtlIdDTO = new ResBtlIdDTO();
            resBtlIdDTO.setId(id);

            result.add(resBtlIdDTO);
        }
        ResApiDTO<List<ResBtlIdDTO>> res = new ResApiDTO<>(result);

        return ResponseEntity.ok(res);
    }
    /**
     * 유리병 편지를 작성하는 컨트롤러
     * @param reqBottleLtrDTO
     * @param customOAuth2User
     * @return
     */
    @PostMapping("/letter")
    public ResponseEntity<ResApiDTO<?>> bottleLtrInsert(@RequestBody ReqBottleLtrDTO reqBottleLtrDTO
                                 , @AuthenticationPrincipal CustomOAuth2User customOAuth2User){

        String usrSeqId = String.valueOf(customOAuth2User.getUsrSeqId());
        reqBottleLtrDTO.setUsrSeqId(usrSeqId);


        return bottleService.insertBtlLtr(reqBottleLtrDTO);
    }


    /**
     * 유리병 편지 답글
     * @param reqBottleLtrDTO
     * @param customOAuth2User
     * @return
     */
    @PostMapping("/reply")
    public ResponseEntity<ResApiDTO<?>> bottleLtrRpyInsert(@RequestBody ReqBottleRpyDTO reqBottleLtrDTO
            , @AuthenticationPrincipal CustomOAuth2User customOAuth2User){

        String usrSeqId = String.valueOf(customOAuth2User.getUsrSeqId());
        reqBottleLtrDTO.setSenderSeqId(usrSeqId);

        return bottleService.insertBtlRpy(reqBottleLtrDTO);
    }

    /**
     * 유리병 편지 그냥 흘려보내기 컨트롤러
     * @param reqBtlLtrMemMpng
     * @param customOAuth2User
     * @return
     */
    @PostMapping("/justFlow")
    public ResponseEntity<ResApiDTO<?>> bottleLtrInsert(@RequestBody ReqBtlLtrMemMpng reqBtlLtrMemMpng
            , @AuthenticationPrincipal CustomOAuth2User customOAuth2User){

        String usrSeqId = String.valueOf(customOAuth2User.getUsrSeqId());
        reqBtlLtrMemMpng.setReadSeqId(usrSeqId);


        return bottleService.btlLtrJustFlow(reqBtlLtrMemMpng);
    }
    /**
     * 유리병 디테일을 조회하는 컨트롤러
     * @param reqBottleLtrDetialDTO
     * @return
     */
    @GetMapping("/letter/detail")
    public ResponseEntity<ResApiDTO<ResBottleLtrDetail>> selectBtlLtrDetail( @ModelAttribute ReqBottleLtrDetialDTO reqBottleLtrDetialDTO){
        log.info("asdsads={}",reqBottleLtrDetialDTO.getBtlLtrNo());
        return bottleService.selectBtlLtrDetail(reqBottleLtrDetialDTO);
    }
}
