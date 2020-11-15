package com.gyb.questionnaire.controller;

import com.gyb.questionnaire.util.SuperVerifyCode;
import com.gyb.questionnaire.util.Verification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

/**
 * @author geng
 * 2020/11/4
 */

@Controller
public class VerificationCodeController {

    @GetMapping("/verifyCode")
    public ResponseEntity<byte[]> showCode(HttpSession httpSession){
        Verification verification = SuperVerifyCode.generateRandomSumImg();
        if(verification != null) {
            httpSession.setAttribute("_code", verification.getCode());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(verification.getBuffer(),headers, HttpStatus.OK);
        }
        return null;
    }

}
