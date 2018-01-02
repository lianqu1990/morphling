package com.lianqu1990.common.utils.web;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 颜色转换
 *
 * @author hanchao
 * @date 2017/11/23 13:48
 */
public class ANSIUtil {
    //只针对最终的颜色
    public static final String ESCAPE_START = "\u001B\\[([\\d;]+)m";
    public static final String ESCAPE_END = "\u001B[0m";//转义结束
    public static Map<String,String> fontColor = new HashMap();
    static {
        fontColor.put("30", "<span style='color:black'>");
        fontColor.put("31", "<span style='color:red'>");
        fontColor.put("32", "<span style='color:green'>");
        fontColor.put("33", "<span style='color:SandyBrown'>");
        fontColor.put("34", "<span style='color:blue'>");
        fontColor.put("35", "<span style='color:purple'>");
        fontColor.put("36", "<span style='color:darkgreen'>");
        fontColor.put("37", "<span style='color:white'>");
        //TODO 背景色
    }

    public static String convertHtml(String content){
        StringBuilder stringBuilder = new StringBuilder(content.replace(ESCAPE_END,"</span>"));

        Pattern pattern = Pattern.compile(ESCAPE_START);
        Matcher matcher = pattern.matcher(stringBuilder);
        while(matcher.find()){
            Code code = Code.parse(matcher.group(1));
            String html = fontColor.containsKey(code.color) ? fontColor.get(code.color):"<span>";
            stringBuilder.replace(matcher.start(),matcher.end(),html);
        }
        return stringBuilder.toString();
    }


    @Builder
    @Data
    static class Code {
        String action;
        String color;

        static Code parse(String code){
            String[] split = code.split(";");
            return Code.builder().action(split[0]).color(split[1]).build();
        }
    }
}
