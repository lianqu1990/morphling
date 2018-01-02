package com.lianqu1990.morphling.web.controller;

import com.github.bingoohuang.patchca.service.Captcha;
import com.github.bingoohuang.patchca.service.CaptchaService;
import com.github.bingoohuang.patchca.word.RandomWordFactory;
import com.lianqu1990.morphling.common.consts.WebParamConsts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author hanchao
 * @date 2016/12/6 12:53
 */
@Controller
@RequestMapping("/validcode")
@Slf4j
@Validated
public class ValidCodeController {
    @Autowired
    private CaptchaService captchaService;

    //验证码生成器
    private RandomWordFactory wf = new RandomWordFactory();

    public ValidCodeController(){
        wf.setCharacters("0123456789");
        wf.setMaxLength(6);
        wf.setMinLength(6);
    }


    @RequestMapping(value="/img",method = RequestMethod.GET)
    public ResponseEntity<byte[]> getImgCode(HttpSession session) throws IOException {
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.PRAGMA,"No-cache");
        header.add(HttpHeaders.CACHE_CONTROL,"no-cache");
        header.add(HttpHeaders.EXPIRES,"0");
        header.add(HttpHeaders.CONTENT_TYPE,"image/jpeg");


        Captcha captcha = captchaService.getCaptcha();
        session.setAttribute(WebParamConsts.CAPTCHA_KEY,captcha.getWord());

        BufferedImage img = captcha.getImage();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(img,"jpeg",bos);
        return new ResponseEntity<byte[]>(bos.toByteArray(),header, HttpStatus.OK);
    }



}
