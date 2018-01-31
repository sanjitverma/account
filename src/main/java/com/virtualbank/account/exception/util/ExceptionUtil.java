package com.virtualbank.account.exception.util;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Created by SANJIT on 13/01/18.
 */
public class ExceptionUtil {

    public static <K, V> Map<K, V> toMap(Class<K> keyType, Class<V> valueType, Object... entries) {
        if (entries.length % 2 == 1){
            throw new IllegalArgumentException("Invalid entries");
        }
        return IntStream.range(0, entries.length/2)
                .map(i -> i * 2)
                .collect(HashMap::new,(m, i) -> m.put(keyType.cast(entries[i]), valueType.cast(entries[i + 1])), Map::putAll);
    }
}
