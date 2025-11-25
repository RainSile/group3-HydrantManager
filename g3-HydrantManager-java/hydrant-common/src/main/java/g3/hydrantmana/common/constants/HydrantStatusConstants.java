package g3.hydrantmana.common.constants;

/**
 * 消防栓状态常量
 */
public final class HydrantStatusConstants {

    public static final int NORMAL = 0;
    public static final int MAINTENANCE = 1;
    public static final int UNNORMAL = 2;

    /**
     * 私有构造函数，防止实例化
     */
    private HydrantStatusConstants() {
        throw new UnsupportedOperationException("常量类不能被实例化");
    }
}
