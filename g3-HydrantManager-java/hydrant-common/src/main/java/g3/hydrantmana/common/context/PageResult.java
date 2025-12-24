package g3.hydrantmana.common.context;

import java.util.Collections;
import java.util.List;

public class PageResult<T> {

    private long total;       // 总记录数
    private int page;         // 当前页
    private int size;         // 每页大小
    private List<T> records;  // 当前页数据

    public PageResult() {
        this.records = Collections.emptyList();
    }

    public PageResult(List<T> records, long total, int page, int size) {
        this.records = records;
        this.total = total;
        this.page = page;
        this.size = size;
    }

    public static <T> PageResult<T> empty() {
        return new PageResult<>(Collections.emptyList(), 0, 0, 0);
    }

    // ======== getter & setter ========
    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }
}
