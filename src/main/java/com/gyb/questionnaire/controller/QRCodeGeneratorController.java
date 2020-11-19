package com.gyb.questionnaire.controller;

import com.gyb.questionnaire.util.QRCodeUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 二维码生成服务
 *
 * @author geng
 * 2020/11/19
 */
@Controller
public class QRCodeGeneratorController {

    @GetMapping("/qrcode")
    public ResponseEntity<byte[]> generate(@RequestParam String content){
        if(!StringUtils.hasText(content))
            return null;
        final byte[] bytes = QRCodeUtil.generateQRCode(content);
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(bytes,httpHeaders, HttpStatus.OK);
    }
}
