package io.github.fengnkt.shorturlresolve.utils;

public class ShortCodeValidator {
    private static final int MAX_CODE_LENGTH = 6;

    public static boolean isValidShortCode(String shortCode) {
        if (shortCode == null) return false;
        if (shortCode.isEmpty()) return false;
        if (shortCode.length() > MAX_CODE_LENGTH) return false;
        for (char c : shortCode.toCharArray()) {
            if (c >= '0' && c <= '9') continue;
            if (c >= 'a' && c <= 'z') continue;
            if (c >= 'A' && c <= 'Z') continue;
            return false;
        }
        return true;
    }
}
