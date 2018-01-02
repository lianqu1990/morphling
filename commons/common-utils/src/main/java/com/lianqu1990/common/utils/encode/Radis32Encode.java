package com.lianqu1990.common.utils.encode;

/**
 * @auther hanchao
 * @date 2016/12/8 15:35
 */
public class Radis32Encode {

    private static final String CHS_STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

    public static String numToStr(long _int) {
        if (_int < 0) {
            return null;
        }
        int len = CHS_STR.length();
        char[] chs = CHS_STR.toCharArray();
        int n = -1;
        long intVal = _int;
        StringBuffer val = new StringBuffer();
        do {
            if (intVal < len) {
                n = (int) intVal;
            } else {
                n = (int) (intVal % len);
            }
            intVal = (int) Math.floor(intVal / len);
            val.append(chs[n]);
        } while (intVal > 0);
        val = val.reverse();
        return val.toString();
    }

    public static long strToInt(String str) {
        int len = CHS_STR.length();
        char[] chs = CHS_STR.toCharArray();
        StringBuffer sb = new StringBuffer(str.trim());
        char[] strs = sb.reverse().toString().toCharArray();
        long num = 0;
        for (int i = 0; i < strs.length; i++) {
            int ind = 0;
            for (char c : chs) {
                if (strs[i] == c) {
                    break;
                }
                ind++;
            }
            if(ind == chs.length){
                throw new IllegalArgumentException("not find the char!");
            }
            num += ind * (int) Math.pow(len, i);
        }
        return num;
    }
}
