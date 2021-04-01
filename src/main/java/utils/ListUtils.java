package utils;

import java.util.List;

public class ListUtils {
    public static <T> int find(List<T> list, T obj) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(obj)) {
                return i;
            }
        }
        return -1;
    }
}
