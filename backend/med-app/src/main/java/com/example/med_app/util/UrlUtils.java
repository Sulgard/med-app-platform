package com.example.med_app.util;

import jakarta.servlet.http.HttpServletRequest;

public final class UrlUtils {
    private UrlUtils() {}

    public static String getAppUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();

        String port = "";
        if(!(scheme.equals("http") && serverPort == 80) &&
                !(scheme.equals("https") && serverPort == 443)) {
            port = ":" + serverPort;
        }
        return scheme + "://" + serverName + port + contextPath;
    }
}
