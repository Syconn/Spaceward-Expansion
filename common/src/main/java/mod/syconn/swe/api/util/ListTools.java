package mod.syconn.swe.api.util;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ListTools {

    public static <T extends @Nullable Object> T getListMostCommonElement(List<T> list, @Nullable T other) {
        if (!list.isEmpty()) {
            int times = 0;
            T resultElement = list.get(0);
            for (T element : list) {
                int found = 0;
                for (T check : list) {
                    if (element == null) continue;
                    if (element.equals(check)) found++;
                }
                if (found > times) {
                    resultElement = element;
                    times = found;
                }
            }
            return resultElement;
        }
        return other;
    }
}
