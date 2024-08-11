package com.fpsboost.util;

import java.net.*;
import java.io.*;

public class WebUtils {
    public static String get(final String url) throws IOException {
        final URL urlObj = new URL(url);
        final HttpURLConnection connection = (HttpURLConnection)urlObj.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        final int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            final StringBuilder responseBuilder = new StringBuilder();
            boolean isFirstLine = true;
            String line;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    responseBuilder.append(line);
                    isFirstLine = false;
                }
                else {
                    responseBuilder.append("\n").append(line);
                }
            }
            reader.close();
            return responseBuilder.toString();
        }
        throw new IOException("HTTP request failed with response code: " + responseCode);
    }
}
