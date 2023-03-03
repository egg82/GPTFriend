package me.egg82.gpt.api;

import me.egg82.gpt.logging.GELFLogger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// https://github.com/lucko/LuckPerms/blob/master/common/src/main/java/me/lucko/luckperms/common/api/ApiRegistrationUtil.java
public class APIRegistrationUtil {
    private static final Logger logger = new GELFLogger(LoggerFactory.getLogger(APIRegistrationUtil.class));

    private static final Method REGISTER;
    private static final Method DEREGISTER;

    static {
        try {
            REGISTER = GPTAPIProvider.class.getDeclaredMethod("register",GPTAPI.class);
            REGISTER.setAccessible(true);

            DEREGISTER = GPTAPIProvider.class.getDeclaredMethod("deregister");
            DEREGISTER.setAccessible(true);
        } catch (NoSuchMethodException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    private APIRegistrationUtil() { }

    public static void register(@NotNull GPTAPI api) {
        try {
            REGISTER.invoke(null, api);
        } catch (InvocationTargetException | IllegalAccessException ex) {
            logger.error("{}: {}", ex.getClass().getName(), ex.getMessage(), ex);
        }
    }

    public static void deregister() {
        try {
            DEREGISTER.invoke(null);
        } catch (InvocationTargetException | IllegalAccessException ex) {
            logger.error("{}: {}", ex.getClass().getName(), ex.getMessage(), ex);
        }
    }
}
