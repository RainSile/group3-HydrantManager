package g3.hydrantmana.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtils {

    private LogUtils() {
        // 工具类不允许实例化
    }

    public static void info(Class<?> clazz, String message) {
        Logger logger = LoggerFactory.getLogger(clazz);
        logger.info(message);
    }

    public static void warn(Class<?> clazz, String message) {
        Logger logger = LoggerFactory.getLogger(clazz);
        logger.warn(message);
    }

    public static void error(Class<?> clazz, String message) {
        Logger logger = LoggerFactory.getLogger(clazz);
        logger.error(message);
    }

    public static void error(Class<?> clazz, String message, Throwable e) {
        Logger logger = LoggerFactory.getLogger(clazz);
        logger.error(message, e);
    }
}
