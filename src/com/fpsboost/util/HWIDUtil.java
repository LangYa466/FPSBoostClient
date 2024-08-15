package com.fpsboost.util;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * @author LangYa
 * @since 2024/07/09/下午8:08
 */
public class HWIDUtil {


    public static String getHWID() {
        Process process;
        try {
            process = Runtime.getRuntime().exec(new String[] { "wmic", "cpu", "get", "ProcessorId"});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            process.getOutputStream().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scanner sc = new Scanner(process.getInputStream());
        String property = md5(convertMD5(md5(sc.next())));
        String serial = md5(sc.next());

        return property + serial;
    }

    public static String md5(String text) {

        byte[] bytes;

        try {
            bytes = MessageDigest.getInstance("md5").digest(text.getBytes());
        } catch (NoSuchAlgorithmException nosuchalgorithmexception) {
            throw new IllegalStateException("md5 error");
        }

        String md5code = (new BigInteger(1, bytes)).toString(16);

        for (int i = 0; i < 32 - md5code.length(); ++i) {
            md5code = "0" + md5code;
        }

        return md5code;
    }

    public static String convertMD5(String inStr) {
        char[] a = inStr.toCharArray();

        for (int s = 0; s < a.length; ++s) {
            a[s] = (char) (a[s] ^ 116);
        }

        String s = new String(a);

        return s;
    }

}
