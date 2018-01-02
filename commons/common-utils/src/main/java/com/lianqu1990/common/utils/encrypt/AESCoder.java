package com.lianqu1990.common.utils.encrypt;

import com.lianqu1990.common.utils.encode.CharsetConsts;
import org.apache.commons.codec.binary.Hex;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * java加密技术-by梁栋
 *
 * @author hanchao
 * @date 2017/10/17 21:58
 */
public class AESCoder {
    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//默认的加密算法

    public static byte[] encrypt(byte [] content,byte [] key) throws Exception{
        KeyGenerator kgen = KeyGenerator.getInstance(KEY_ALGORITHM);
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(key);
        kgen.init(128, secureRandom);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        return cipher.doFinal(content);
    }

    public static byte[] decrypt(byte [] content,byte [] key) throws Exception{
        KeyGenerator kgen = KeyGenerator.getInstance(KEY_ALGORITHM);
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(key);
        kgen.init(128, secureRandom);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        return cipher.doFinal(content);
    }

    public static String encryptWithHex(String content,String key){
        if(key == null){
            key = "";
        }
        try {
            byte[] encrypt = encrypt(content.getBytes(CharsetConsts.DEFAULT_CHARSET), key.getBytes(CharsetConsts.DEFAULT_CHARSET));
            return Hex.encodeHexString(encrypt);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decryptWithHex(String content,String key){
        if(key == null){
            key = "";
        }
        try {
            byte[] decrypt = decrypt(Hex.decodeHex(content.toCharArray()), key.getBytes(CharsetConsts.DEFAULT_CHARSET));
            return new String(decrypt);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String encryptWithB64(String content,String key){
        if(key == null){
            key = "";
        }
        try {
            byte[] encrypt = encrypt(content.getBytes(CharsetConsts.DEFAULT_CHARSET), key.getBytes(CharsetConsts.DEFAULT_CHARSET));
            return new BASE64Encoder().encode(encrypt);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decryptWithB64(String content,String key){
        if(key == null){
            key = "";
        }
        try {
            byte[] decrypt = decrypt(new BASE64Decoder().decodeBuffer(content), key.getBytes(CharsetConsts.DEFAULT_CHARSET));
            return new String(decrypt,CharsetConsts.DEFAULT_CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}

