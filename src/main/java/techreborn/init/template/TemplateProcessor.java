/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

package techreborn.init.template;

import com.google.gson.*;
import net.minecraft.block.Block;
import net.minecraft.util.registry.Registry;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateProcessor {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private final Path resources;

	public TemplateProcessor(Path resources) {
		this.resources = resources;
	}

	public void processSimpleBlocks(String template, List<Block> blocks) throws IOException {
		for (Block block : blocks) {
			Map<String, String> values = new HashMap<>();
			values.put("name", Registry.BLOCK.getId(block).getPath());

			process(template, values);
		}
	}

	public void process(String template, Map<String, String> values) throws IOException {
		Path directory = resources.resolve("templates").resolve(template);
		JsonObject info = getJson(directory.resolve("info.json"));

		JsonArray files = info.getAsJsonArray("files");
		for (JsonElement fileElement : files) {
			JsonObject file = fileElement.getAsJsonObject();
			Path inputFile = directory.resolve(file.get("from").getAsString());
			Path outputFile = resources.resolve(replaceValues(file.get("to").getAsString(), values));

			processFile(inputFile, outputFile, values);
		}
	}

	private void processFile(Path inputFile, Path outputFile, Map<String, String> values) throws IOException {
		String input = new String(Files.readAllBytes(inputFile), StandardCharsets.UTF_8);
		String output = replaceValues(input, values);
		Files.write(outputFile, output.getBytes(StandardCharsets.UTF_8));
	}

	private static String replaceValues(String input, Map<String, String> values) {
		for (Map.Entry<String, String> entry : values.entrySet()) {
			input = input.replaceAll("%" + entry.getKey() + "%", entry.getValue());
		}
		return input;
	}

	private JsonObject getJson(Path path) throws IOException {
		if (!Files.exists(path)) {
			throw new FileNotFoundException("Failed to find " + path.toString());
		}

		try (InputStream stream = Files.newInputStream(path)) {
			return GSON.fromJson(new InputStreamReader(stream), JsonObject.class);
		}
	}
}
