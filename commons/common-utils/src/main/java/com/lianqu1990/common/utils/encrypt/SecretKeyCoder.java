package com.lianqu1990.common.utils.encrypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * @author hanchao
 * @date 2017/10/17 22:10
 */
public class SecretKeyCoder {
    /**
     * 生成密钥
     *
     * @param seed
     * @return
     * @throws Exception
     */
    public static SecretKey initKey(String algorithm,String seed) throws Exception {
        SecureRandom secureRandom = null;
        if (seed != null) {
            secureRandom = new SecureRandom(seed.getBytes());
        } else {
            secureRandom = new SecureRandom();
        }

        KeyGenerator kg = KeyGenerator.getInstance(algorithm);
        kg.init(secureRandom);

        return kg.generateKey();
    }

}
