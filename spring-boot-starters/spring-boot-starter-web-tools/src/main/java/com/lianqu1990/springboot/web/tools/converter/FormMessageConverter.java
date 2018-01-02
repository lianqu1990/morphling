package com.lianqu1990.springboot.web.tools.converter;

import com.lianqu1990.common.utils.web.RequestUtil;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author hanchao
 * @date 2016/12/2 0:14
 */
public class FormMessageConverter extends AbstractHttpMessageConverter<Map<String,Object>> {
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private Charset charset;
    public FormMessageConverter(){
        super(MediaType.APPLICATION_FORM_URLENCODED);
        this.charset = DEFAULT_CHARSET;
    }

    public FormMessageConverter(Charset charset) {
        super(MediaType.APPLICATION_FORM_URLENCODED);
        this.charset = charset;
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        if(Map.class.isAssignableFrom(clazz)){
            return true;
        }
        return false;
    }

    @Override
    protected boolean canRead(MediaType mediaType) {
        return false;
    }

    /**
     * unsupport
     * @param clazz
     * @param inputMessage
     * @return
     * @throws IOException
     * @throws HttpMessageNotReadableException
     */
    @Override
    protected Map<String, Object> readInternal(Class<? extends Map<String, Object>> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    protected void writeInternal(Map<String, Object> stringObjectMap, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        StreamUtils.copy(RequestUtil.expandUrl(stringObjectMap,charset), charset, outputMessage.getBody());
    }

    @Override
    protected boolean canWrite(MediaType mediaType) {
        if(mediaType == null){ // 由于目前版本的spring不支持自定义消息处理器的顺序，这里对Null处理会有问题
            return false;
        }else{
            return super.canWrite(mediaType);
        }
    }
}
