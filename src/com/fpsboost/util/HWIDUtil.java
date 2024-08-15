package com.fpsboost.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author LangYa
 * @since 2024/07/09/下午8:08
 */
public class HWIDUtil {


    public static String getHWID() throws NoSuchAlgorithmException {
        StringBuilder s = new StringBuilder();
        String main = System.getenv("PROCESS_IDENTIFIER") + System.getenv("COMPUTERNAME");
        byte[] bytes = main.getBytes(StandardCharsets.UTF_8);
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] md5 = messageDigest.digest(bytes);
        int i = 0;
        for(byte b : md5) {
            s.append(Integer.toHexString((b & 0xFF) | 0x300),0,3);
            if(i != md5.length -1) {
                s.append("-");
            }
            i++;
        }
        return s.toString();
    }

}
