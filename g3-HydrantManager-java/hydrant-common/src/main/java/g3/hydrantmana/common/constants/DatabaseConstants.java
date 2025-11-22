package g3.hydrantmana.common.constants;

/**
 * 数据库相关常量类
 */
public final class DatabaseConstants {

    /**
     * 数据库操作影响行数常量
     */
    public static final class AffectedRows {
        public static final int ZERO = 0;         // 影响行数为0
        public static final int SUCCESS_MIN = 1;  // 成功操作最小影响行数
    }

    /**
     * 私有构造函数，防止实例化
     */
    private DatabaseConstants() {
        throw new UnsupportedOperationException("常量类不能被实例化");
    }
}