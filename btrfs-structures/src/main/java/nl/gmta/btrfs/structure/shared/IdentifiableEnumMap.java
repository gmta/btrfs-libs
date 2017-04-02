package nl.gmta.btrfs.structure.shared;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class IdentifiableEnumMap<K, V extends Enum<V> & IdentifiableEnum<K>> {
    private final Map<K, V> lookup;

    public IdentifiableEnumMap(V[] enumValues) {
        this.lookup = Arrays.stream(enumValues)
            .collect(Collectors.toMap(
                e -> e.getId(),
                e -> e
            ));
    }

    public V getById(K id) {
        return this.lookup.get(id);
    }
}
