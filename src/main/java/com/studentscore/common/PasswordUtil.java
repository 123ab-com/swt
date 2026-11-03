package com.studentscore.common;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * 口令工具：MD5 摘要存储（课程设计场景下的简化方案）。
 */
public final class PasswordUtil {

    private PasswordUtil() {
    }

    public static String md5(String raw) {
        return DigestUtils.md5DigestAsHex(raw.getBytes(StandardCharsets.UTF_8));
    }

    public static boolean matches(String raw, String encoded) {
        return raw != null && encoded != null && encoded.equalsIgnoreCase(md5(raw));
    }
}
