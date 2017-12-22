package com.huatu.morphling.spring.conf.base;

import com.github.bingoohuang.patchca.background.BackgroundFactory;
import com.github.bingoohuang.patchca.color.ColorFactory;
import com.github.bingoohuang.patchca.custom.ConfigurableCaptchaService;
import com.github.bingoohuang.patchca.filter.ConfigurableFilterFactory;
import com.github.bingoohuang.patchca.filter.library.AbstractImageOp;
import com.github.bingoohuang.patchca.filter.library.WobbleImageOp;
import com.github.bingoohuang.patchca.font.RandomFontFactory;
import com.github.bingoohuang.patchca.text.renderer.BestFitTextRenderer;
import com.github.bingoohuang.patchca.text.renderer.TextRenderer;
import com.github.bingoohuang.patchca.word.RandomWordFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author hanchao
 * @date 2016/12/6 13:04
 */
@Configuration
public class CaptChaConfig {

    /**
     * 验证码工具类-patcha
     * @return
     */
    @Bean
    public ConfigurableCaptchaService configurableCaptchaService(){
        ConfigurableCaptchaService cs = new ConfigurableCaptchaService();

        final Random random = new Random();
        cs.setColorFactory(new ColorFactory() {
            @Override
            public Color getColor(int x) {
                int[] c = new int[3];
                int i = random.nextInt(c.length);
                for (int fi = 0; fi < c.length; fi++) {
                    if (fi == i) {
                        c[fi] = random.nextInt(71);
                    } else {
                        c[fi] = random.nextInt(256);
                    }
                }
                return new Color(c[0], c[1], c[2]);
            }
        });
        //cs.setColorFactory(new RandomColorFactory());

        ConfigurableFilterFactory filterFactory = new ConfigurableFilterFactory();
        java.util.List<BufferedImageOp> filters = new ArrayList<BufferedImageOp>();
        WobbleImageOp wobbleImageOp = new WobbleImageOp();
        wobbleImageOp.setEdgeMode(AbstractImageOp.EDGE_MIRROR);
        wobbleImageOp.setxAmplitude(2.0);
        wobbleImageOp.setyAmplitude(1.0);
        filters.add(wobbleImageOp);
        filterFactory.setFilters(filters);
        cs.setFilterFactory(filterFactory);

        RandomFontFactory fontFactory = new RandomFontFactory();
        fontFactory.setMaxSize(26);
        fontFactory.setMinSize(26);
        cs.setFontFactory(fontFactory);

        cs.setBackgroundFactory(new BackgroundFactory() {
            @Override
            public void fillBackground(BufferedImage dest) {

            }
        });

        RandomWordFactory wf = new RandomWordFactory();
        wf.setCharacters("23456789abcdefghigkmnpqrstuvwxyzABCDEFGHGKLMNPQRSTUVWXYZ");
        wf.setMaxLength(4);
        wf.setMinLength(4);
        cs.setWordFactory(wf);

        TextRenderer textRenderer = new BestFitTextRenderer();
        textRenderer.setBottomMargin(2);
        textRenderer.setTopMargin(2);
        textRenderer.setLeftMargin(1);
        textRenderer.setRightMargin(1);
        cs.setTextRenderer(textRenderer);

        cs.setWidth(100);
        cs.setHeight(30);
        return cs;
    }
}
