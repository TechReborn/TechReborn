/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2016-2017 TeamReborn
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

package reborncore.common.config.impl;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import reborncore.common.config.Config;
import reborncore.common.config.impl.serialization.ConfigParser;
import reborncore.common.config.impl.serialization.ConfigWriter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class RebornCoreConfigApiImpl {
	private static final String FILE_EXTENSION = ".cfg";
	private static final Map<Identifier, Config> CONFIGS = new HashMap<>();

	private RebornCoreConfigApiImpl() {
	}

	public static void register(Config config) {
		register(config, FabricLoader.getInstance().getConfigDir());
	}

	public static void register(Config config, Path configDir) {
		ConfigRoot root = (ConfigRoot) config;
		Identifier id = root.getId();

		if (CONFIGS.containsKey(id)) {
			throw new IllegalStateException("Config already registered: " + id);
		}

		// Don't allow any further modification to the config specification
		root.finalizeSpec();

		Path configPath = resolvePath(configDir, id);
		loadConfig(root, configPath);
		saveConfig(root, configPath);

		CONFIGS.put(id, config);
	}

	private static Path resolvePath(Path configDir, Identifier id) {
		return configDir.resolve(id.getNamespace()).resolve(id.getPath() + FILE_EXTENSION);
	}

	private static void loadConfig(ConfigRoot root, Path path) {
		if (!Files.exists(path)) {
			return;
		}

		try {
			ConfigParser.loadInto(Files.readString(path, StandardCharsets.UTF_8), root);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read config file: " + path, e);
		}
	}

	private static void saveConfig(ConfigRoot root, Path path) {
		try {
			Files.createDirectories(path.getParent());
			Files.writeString(path, ConfigWriter.writeToSNBT(root), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Failed to write config file: " + path, e);
		}
	}
}
