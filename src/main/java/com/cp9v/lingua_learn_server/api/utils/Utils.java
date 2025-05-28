package com.cp9v.lingua_learn_server.api.utils;

import java.util.List;
import java.util.Optional;

public class Utils {

    public static <T> Optional<T> toOptional(List<T> objects) {
        if (objects.isEmpty()) {
            return Optional.empty();
        }
        if (objects.size() > 1) {
            throw new IllegalStateException("More than one object found");
        }
        return Optional.of(objects.getFirst());
    }
}
