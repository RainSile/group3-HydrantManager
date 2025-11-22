package g3.hydrantmana.common.constants;

/**
 * 用户相关常量类
 */
public final class UserConstants {

    /**
     * 用户状态常量
     */
    public static final class Status {
        public static final int LOCKED = 0;   // 账号锁定
        public static final int ACTIVE = 1;   // 账号激活
    }

    /**
     * 用户权限常量
     */
    public static final class Privilege {
        public static final int USER = 0;     // 普通用户
        public static final int ADMIN = 1;    // 管理员
    }

    /**
     * 私有构造函数，防止实例化
     */
    private UserConstants() {
        throw new UnsupportedOperationException("常量类不能被实例化");
    }
}