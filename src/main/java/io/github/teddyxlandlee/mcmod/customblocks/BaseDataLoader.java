package io.github.teddyxlandlee.mcmod.customblocks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class BaseDataLoader {
    public static final BaseDataLoader INSTANCE = new BaseDataLoader();
    private BaseDataLoader() {}

    private static final Gson GSON = new GsonBuilder().setLenient().create();

    public static final File DATA_PATH = FabricLoader.getInstance().getGameDir().resolve("customblocks").toFile();

    public Map<String, JsonObject> load() throws IOException {
        Map<String, JsonObject> map = new HashMap<>();

        File[] files = DATA_PATH.listFiles((parentDir, filename) -> filename.endsWith(".json") || filename.endsWith("json5"));
        if (files == null) throw new IOException("Cannot load files from" + DATA_PATH);
        for (File file : files) {
            if (file.isDirectory()) continue;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            GSON.
        }
    }
}
