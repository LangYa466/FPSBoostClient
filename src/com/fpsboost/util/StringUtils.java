package com.fpsboost.util;

import com.fpsboost.module.handlers.ModuleHandle;

import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class StringUtils {

    public static String findLongestModuleName(List<ModuleHandle> modules) {
        return Collections.max(modules, Comparator.comparing(module -> (module.getName() + (!module.getValues().isEmpty() ? " " + module.getSuffix() : "")).length())).getName();
    }

    public static String getLongestModeName(List<String> listOfWords) {
        String longestWord = null;
        for (String word : listOfWords) {
            if (longestWord == null || word.length() > longestWord.length()) {
                longestWord = word;
            }
        }
        return longestWord != null ? longestWord : "";
    }

    public static String b64(Object o) {
        return Base64.getEncoder().encodeToString(String.valueOf(o).getBytes());
    }

    private static final Pattern patternControlCode = Pattern.compile("(?i)\\u00A7[\\dA-FK-OR]");
    private static final Pattern colorPatternCodes = Pattern.compile("(?i)\\u00A7[\\dA-F]");

    public StringUtils() {
    }

    public static String ticksToElapsedTime(int ticks) {
        int i = ticks / 20;
        int j = i / 60;
        i %= 60;
        return i < 10 ? j + ":0" + i : j + ":" + i;
    }

    public static String stripControlCodes(String text) {
        return patternControlCode.matcher(text).replaceAll("");
    }

    public static String stripColorCodes(String text) {
        return colorPatternCodes.matcher(text).replaceAll("");
    }

    public static boolean isNullOrEmpty(String string) {
        return org.apache.commons.lang3.StringUtils.isEmpty(string);
    }
}