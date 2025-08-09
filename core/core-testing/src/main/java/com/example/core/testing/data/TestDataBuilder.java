package com.example.core.testing.data;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class TestDataBuilder<T> {

    protected Map<String, Object> data = new HashMap<>();

    public abstract T build();

    @SuppressWarnings("unchecked")
    public T with(Consumer<T> customizer) {
        T instance = build();
        customizer.accept(instance);
        return instance;
    }

    protected void setDefaults() {
        // Override in subclasses to set default values
    }
}