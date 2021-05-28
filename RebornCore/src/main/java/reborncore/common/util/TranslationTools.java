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

package reborncore.common.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

//Quick too to migrate to the new lang format, and to try and keep as many lang entrys as possible
public class TranslationTools {

	//Scanner used for manual matching
	private static final Scanner SCANNER = new Scanner(System.in);

	public static void main(String[] args) throws IOException {
		Path dir = Paths.get("C:\\Users\\mark\\Desktop\\translations");
		//generateMigrationMap(dir);
		migrateMappings(dir);
	}

	private static void migrateMappings(Path dir) throws IOException {
		final Map<String, String> keyMap = readJsonFile(dir.resolve("map.json"));
		final Map<String, String> newLang = readJsonFile(dir.resolve("en_us.json"));

		Path outputDir = dir.resolve("out");
		Files.createDirectories(outputDir);

		for (Path path : Files.walk(dir.resolve("old")).collect(Collectors.toList())) {
			if (Files.isDirectory(path)) {
				continue;
			}
			Map<String, String> oldLang = readLangFile(path);
			Map<String, String> output = new HashMap<>();

			for (Map.Entry<String, String> entry : oldLang.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				if (keyMap.containsKey(key)) {
					key = keyMap.get(key);
				}
				if (!newLang.containsKey(key)) {
					//Lost key, no point copying them over
					continue;
				}
				output.put(key, value);
			}


			Path outputPath = outputDir.resolve(path.getFileName().toString().toLowerCase().replace(".lang", ".json"));
			writeJsonMap(outputPath, output);
		}
	}

	@SuppressWarnings("unused")
	private static void generateMigrationMap(Path dir) throws IOException {
		Map<String, String> oldLang = readLangFile(dir.resolve("en_us.lang"));
		Map<String, String> newLang = readJsonFile(dir.resolve("en_us.json"));

		Map<String, String> conversion = new HashMap<>();

		for (Map.Entry<String, String> entry : oldLang.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();

			List<String> newKeys = getKeysByValue(newLang, value);
			if (newKeys.size() == 1) {
				conversion.put(key, newKeys.get(0));
			} else if (newKeys.size() > 0) {
				boolean autoMatched = false;
				String[][] autoMatches = new String[][]{{"tile.", "block."}, {"fluid.", "fluid."}};
				for (String[] arr : autoMatches) {
					if (key.startsWith(arr[0])) {
						for (String newKey : newKeys) {
							if (newKey.startsWith(arr[1])) {
								autoMatched = true;
								conversion.put(key, newKey);
							}
						}
					}
				}
				if (!autoMatched) {
					System.out.println();
					System.out.println(key);
					System.out.println();
					for (int i = 0; i < newKeys.size(); i++) {
						System.out.println(String.format("%d) %s", i, newKeys.get(i)));
					}
					System.out.print("Input selection:");
					int input = SCANNER.nextInt();
					conversion.put(key, newKeys.get(input));
					System.out.println();
				}
			}
		}

		writeJsonMap(dir.resolve("map.json"), conversion);
	}

	private static Map<String, String> readJsonFile(Path path) throws IOException {
		Type mapType = new TypeToken<Map<String, String>>() {
		}.getType();
		return new Gson().fromJson(new String(Files.readAllBytes(path), StandardCharsets.UTF_8), mapType);
	}

	private static Map<String, String> readLangFile(Path path) throws IOException {
		List<String> lines = Files.lines(path).collect(Collectors.toList());
		Map<String, String> map = new HashMap<>();
		for (String line : lines) {
			line = line.trim();
			if (line.isEmpty() || line.startsWith("#")) {
				continue;
			}
			String[] split = line.split("=");
			if (split.length != 2) {
				throw new UnsupportedOperationException();
			}
			map.put(split[0], split[1]);
		}
		return map;
	}

	private static void writeJsonMap(Path path, Map<String, String> map) throws IOException {
		Files.deleteIfExists(path);
		String json = new Gson().toJson(map);
		Files.write(path, json.getBytes());
	}

	private static <T, E> List<T> getKeysByValue(Map<T, E> map, E value) {
		List<T> keys = new ArrayList<>();
		for (Map.Entry<T, E> entry : map.entrySet()) {
			if (Objects.equals(value, entry.getValue())) {
				keys.add(entry.getKey());
			}
		}
		return keys;
	}

}
