package io.github.teddyxlandlee.mcmod.customblocks;

import com.google.gson.JsonObject;

public interface JsonObjectLoader<T> {
    T read(JsonObject json);
}
