package io.github.teddyxlandlee.mcmod.customblocks.registry;

import com.google.gson.JsonObject;
import io.github.teddyxlandlee.mcmod.customblocks.CustomBlocksLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomRegistryHelper {
    public static @NotNull JsonDataAdapter fromJson(JsonObject root, String id)
            throws IllegalArgumentException {
        Identifier type = getId(root, "type");
        @Nullable JsonDataAdapter adapter
                = CustomBlocksLoader.ROOT_DATA_LOADER_REGISTRY.get(type);
        if (adapter == null) throw new IllegalArgumentException("Cannot" +
                "find adapter: " + type);
        if (!adapter.verify(root)) {
            throw new IllegalArgumentException("Failed while verifying extension: " + id);
        }
        return adapter;
    }

    private static Identifier getId(JsonObject root, String element) {
        return new Identifier(JsonHelper.getString(root, element));
    }
}
