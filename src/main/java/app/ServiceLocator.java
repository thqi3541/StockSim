package app;

import java.util.HashMap;
import java.util.Map;

public class ServiceLocator {
    private static final Map<Class<?>, Object> services = new HashMap<>();

    public static <T> void registerService(Class<T> key, T service) {
        services.put(key, service);
    }

    public static <T> T getService(Class<T> key) {
        return key.cast(services.get(key));
    }
}