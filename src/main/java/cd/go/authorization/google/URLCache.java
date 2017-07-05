package cd.go.authorization.google;

import org.apache.commons.collections4.map.PassiveExpiringMap;

public class URLCache {
    private final PassiveExpiringMap<String, String> urlCache = new PassiveExpiringMap(30000);

    public void cache(String key, String url) {
        urlCache.put(key, url);
    }

    public static URLCache getInstance() {
        return InstanceHolder.URL_CACHE;
    }

    public void remove(String state) {
        urlCache.remove(state);
    }

    public String get(String state) {
        return urlCache.get(state);
    }

    public String getAndRemove(String state) {
        return urlCache.remove(state);
    }

    private static final class InstanceHolder {
        private static final URLCache URL_CACHE = new URLCache();
    }
}
