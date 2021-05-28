/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package reborncore.common.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Configuration {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private final Class clazz;
	private final String modId;

	public Configuration(Class clazz, String modId) {
		this.clazz = clazz;
		this.modId = modId;
		setup();
	}

	private void setup() {
		final File configDir = new File(FabricLoader.getInstance().getConfigDir().toFile(), modId);

		if (!configDir.exists()) {
			configDir.mkdirs();
		}

		final File[] configFiles = configDir.listFiles();
		if (configFiles != null) {
			final HashMap<String, JsonObject> configs = new HashMap<>();
			for (File file : configFiles) {
				final String name = file.getName().substring(0, file.getName().length() - (".json".length()));
				try {
					final String fileContents = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
					final JsonObject jsonObject = GSON.fromJson(fileContents, JsonObject.class);
					configs.put(name, jsonObject);
				} catch (IOException e) {
					System.err.println("Failed to read config file: " + file.getAbsolutePath());
					e.printStackTrace();
				}
			}
			readFromJson(configs);
		}

		//Save the configs
		for (Map.Entry<String, JsonObject> entry : toJson().entrySet()) {
			final File configFile = new File(configDir, entry.getKey() + ".json");
			final String jsonStr = GSON.toJson(entry.getValue());
			try {
				FileUtils.writeStringToFile(configFile, jsonStr, StandardCharsets.UTF_8);
			} catch (IOException e) {
				throw new RuntimeException("Failed to write config file: " + configFile.getAbsolutePath(), e);
			}
		}
	}

	private HashMap<Field, Config> getConfigFields() {
		final HashMap<Field, Config> fieldMap = new HashMap<>();
		for (Field field : clazz.getDeclaredFields()) {
			if (!field.isAnnotationPresent(Config.class)) {
				continue;
			}
			if (!Modifier.isStatic(field.getModifiers())) {
				throw new UnsupportedOperationException("Config field must be static");
			}
			Config annotation = field.getAnnotation(Config.class);
			fieldMap.put(field, annotation);
		}
		return fieldMap;
	}

	public HashMap<String, JsonObject> toJson() {
		final HashMap<Field, Config> fieldMap = getConfigFields();
		final HashMap<String, JsonObject> configs = new HashMap<>();

		for (Map.Entry<Field, Config> entry : fieldMap.entrySet()) {
			Field field = entry.getKey();
			Config annotation = entry.getValue();

			final JsonObject config = configs.computeIfAbsent(annotation.config(), s -> new JsonObject());

			JsonObject categoryObject;
			if (config.has(annotation.category())) {
				categoryObject = config.getAsJsonObject(annotation.category());
			} else {
				categoryObject = new JsonObject();
				config.add(annotation.category(), categoryObject);
			}

			String key = annotation.key().isEmpty() ? field.getName() : annotation.key();
			if (categoryObject.has(key)) {
				throw new UnsupportedOperationException("Some bad happened, duplicate key found: " + key);
			}

			JsonObject fieldObject = new JsonObject();
			fieldObject.addProperty("comment", annotation.comment());

			Object value;
			try {
				value = field.get(null);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}

			JsonElement jsonElement = GSON.toJsonTree(value);
			fieldObject.add("value", jsonElement);

			categoryObject.add(key, fieldObject);
		}

		return configs;
	}

	public void readFromJson(HashMap<String, JsonObject> configs) {
		final HashMap<Field, Config> fieldMap = getConfigFields();

		for (Map.Entry<Field, Config> entry : fieldMap.entrySet()) {
			Field field = entry.getKey();
			Config annotation = entry.getValue();

			final JsonObject config = configs.get(annotation.config());

			if (config == null) {
				continue; //Could be possible if a new config is added
			}

			JsonObject categoryObject = config.getAsJsonObject(annotation.category());
			if (categoryObject == null) {
				continue;
			}

			String key = annotation.key().isEmpty() ? field.getName() : annotation.key();
			if (!categoryObject.has(key)) {
				continue;
			}

			JsonObject fieldObject = categoryObject.get(key).getAsJsonObject();
			if (!fieldObject.has("value")) {
				continue;
			}
			JsonElement jsonValue = fieldObject.get("value");
			Class<?> fieldType = field.getType();

			Object fieldValue = GSON.fromJson(jsonValue, fieldType);

			try {
				field.set(null, fieldValue);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Failed to set field value", e);
			}
		}
	}

}
