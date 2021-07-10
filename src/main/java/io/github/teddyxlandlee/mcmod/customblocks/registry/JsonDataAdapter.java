package io.github.teddyxlandlee.mcmod.customblocks.registry;

import com.google.gson.JsonObject;

//@FunctionalInterface
public interface JsonDataAdapter {
    void registry(JsonObject json);

    /**
     * Returns {@code true} if {@code json} is valid to registry,
     * otherwise {@code false}.
     * @return {@code true} if {@code json} is valid to registry,
     * otherwise {@code false}.
     */
    boolean verify(JsonObject json);
}
