package io.github.fengnkt.shorturlresolve.utils;

import java.net.URI;
import java.net.URISyntaxException;

public class UrlValidator {
    public static boolean isValidUrl(String url) {
        if (url == null || url.isBlank()) return false;

        try {
            URI uri = new URI(url);
            String scheme = uri.getScheme();
            if (scheme == null) return false;
            if (!scheme.equalsIgnoreCase("http") && !scheme.equalsIgnoreCase("https")) {
                return false;
            }
            return uri.getHost() != null;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
