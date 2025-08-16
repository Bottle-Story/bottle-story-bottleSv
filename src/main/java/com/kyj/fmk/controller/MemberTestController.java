package com.kyj.fmk.controller;

import com.kyj.fmk.core.file.FileService;
import com.kyj.fmk.mapper.TestMapper;
import com.kyj.fmk.sec.annotation.PublicEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bottle")
public class MemberTestController {

    private final TestMapper testMapper;
    private final RedisTemplate<String,Object> redisTemplate;
    private final  FileService fileService;

    @PublicEndpoint
    @GetMapping("/devtest")
    public String test(){
        redisTemplate.opsForValue().set("dev","1234");
        return "bottle";
    }
}
