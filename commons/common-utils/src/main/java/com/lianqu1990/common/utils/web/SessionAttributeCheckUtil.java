package com.lianqu1990.common.utils.web;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * @author hanchao
 * @date 2017年4月13日 下午4:35:05
 */
public class SessionAttributeCheckUtil {

    public static void remoteAttributes(HttpSession session,String ...attrs){
        for (String attr : attrs) {
            if (attr != null) {
                session.removeAttribute(attr);
            }
        }
    }
    /**
     * 字符串判断，不允许为null
     * @param session
     * @param attrName
     * @param value
     * @param ignoreCase
     * @return
     */
    public static boolean equals(HttpSession session,String attrName,String value,boolean ignoreCase){
        Preconditions.checkNotNull(session);
        Preconditions.checkArgument(StringUtils.isNotBlank(attrName));
        //强转string容易npe
        //String.valueOf可能导致null的注入攻击而绕过验证
        //所以细致的判断导致代码太冗长
        Object object = session.getAttribute(attrName);
        if(object != null ){
            return ignoreCase?String.valueOf(object).equalsIgnoreCase(value) : String.valueOf(object).equals(value);
        }
        return false;
    }
    
    /**
     * 判断session属性和所给是否相等,允许都为null
     * @param session
     * @param attrName
     * @param value
     * @return
     */
    public static boolean equals(HttpSession session,String attrName,Object value){
        Preconditions.checkNotNull(session);
        Preconditions.checkArgument(StringUtils.isNotBlank(attrName));
        return Objects.equals(session.getAttribute(attrName), attrName);
    }
    
    
    public static boolean isNull(HttpSession session,String attrName){
        Preconditions.checkNotNull(session);
        Preconditions.checkArgument(StringUtils.isNotBlank(attrName));
        return session.getAttribute(attrName) == null;
    }
}
