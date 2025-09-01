package com.kyj.fmk.controller;

import com.kyj.fmk.core.model.dto.ResApiDTO;
import com.kyj.fmk.model.ReqBottleLtrDTO;
import com.kyj.fmk.model.ReqBottleLtrDetialDTO;
import com.kyj.fmk.model.ResBottleLtrDetail;
import com.kyj.fmk.sec.dto.oauth2.CustomOAuth2User;
import com.kyj.fmk.service.BottleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
