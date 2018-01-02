package com.lianqu1990.common.utils.pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hanchao
 * @date 2016/12/11 10:44
 */
public class MatchUtil {
    public static boolean isPhoneNo(String phone) {
        return matchRegex(phone, RegexConst.PHONE_CHECK);
    }

    public static boolean isEmail(String email) {
        return matchRegex(email, RegexConst.EMAIL_CHECK);
    }

    public static boolean matchRegex(String str, String regex) {
        if(str == null){
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
}
