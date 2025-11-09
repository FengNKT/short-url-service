package io.github.fengnkt.shorturlresolve.utils;

import java.time.Duration;

public class RedisConstants {
    public static final String URL_MAP_KEY_PREFIX = "url_map:";

    public static final Duration URL_MAP_CACHE_DURATION = Duration.ofHours(24);
    public static final Duration URL_MAP_EMPTY_CACHE_DURATION = Duration.ofMinutes(1);
}
