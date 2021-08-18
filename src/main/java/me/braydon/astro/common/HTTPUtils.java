package me.braydon.astro.common;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Braydon
 */
public class HTTPUtils {
    /**
     * Parse the given {@link URI} query and convert it to a {@link Map} of
     * parameters.
     *
     * @param query the uri query
     * @return the parameters map
     */
    public static Map<String, String> getParameters(String query) {
        if (query == null || (query = query.trim()).isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> parameters = new HashMap<>();
        for (String param : query.split("&")) {
            String[] split = param.split("=");
            if (split.length > 1) {
                parameters.put(split[0], split[1]);
            } else {
                parameters.put(split[0], "");
            }
        }
        return parameters;
    }
}