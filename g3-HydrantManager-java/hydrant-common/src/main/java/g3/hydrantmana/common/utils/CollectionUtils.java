package g3.hydrantmana.common.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class CollectionUtils {

    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return !isEmpty(collection);
    }

    public static <T> List<T> emptyIfNull(Collection<T> collection) {
        return collection == null ? Collections.emptyList() : new ArrayList<>(collection);
    }
}
