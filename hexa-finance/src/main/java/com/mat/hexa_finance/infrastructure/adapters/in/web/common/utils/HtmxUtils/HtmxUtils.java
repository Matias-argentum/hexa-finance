package com.mat.hexa_finance.infrastructure.adapters.in.web.common.utils.HtmxUtils;

import jakarta.servlet.http.HttpServletResponse;

public class HtmxUtils {
    public static void addNotification(HttpServletResponse response, String message) {
        String json = """
            {
                "notify": {
                    "message": "%s"
                }
            }
            """.formatted(message);

        response.setHeader("HX-Trigger", json.trim().replace("\n", ""));
    }

    public static void addNotificationAndClose(HttpServletResponse response, String message) {
        String json = """
        {
            "notify": {"message": "%s"},
            "closeModal": true
        }
        """.formatted(message);
        response.setHeader("HX-Trigger", json.trim().replace("\n", ""));
    }
}
