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

package techreborn.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class TagRemapper {

	private static final String TAG_NAMESPACE = "c";
	private static final Path DATA_DIR = Paths.get("C:\\Users\\mark\\Documents\\Modding\\1.16\\TechReborn\\src\\main\\resources\\data");
	private static final Path TAGS_DIR = DATA_DIR.resolve(TAG_NAMESPACE).resolve("tags");
	private static final Path BLOCK_TAGS_DIR = TAGS_DIR.resolve("blocks");
	private static final Path ITEM_TAGS_DIR = TAGS_DIR.resolve("items");

	private static final Path RECIPES_DIR = DATA_DIR.resolve("techreborn").resolve("recipes");

	public static void main(String[] args) throws IOException {
		applyRenames(getRenames(BLOCK_TAGS_DIR));
		applyRenames(getRenames(ITEM_TAGS_DIR));
	}

	private static void applyRenames(List<TagRename> renames) {
		renames.forEach(TagRemapper::applyRename);
	}

	private static void applyRename(TagRename rename) {
		try {
			Files.deleteIfExists(rename.getNewPath());
			Files.move(rename.getOldPath(), rename.getNewPath());
			applyToRecipes(rename);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void applyToRecipes(TagRename rename) throws IOException {
		Files.walk(RECIPES_DIR)
				.filter(path -> path.getFileName().toString().endsWith(".json"))
				.forEach(renameInFile(rename));
	}

	private static Consumer<Path> renameInFile(TagRename rename) {
		final char quote = '"';
		return path -> {
			try {
				String recipe = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
				recipe = recipe.replaceAll(quote + rename.getOldTagName() + quote, quote + rename.getNewTagName() + quote);
				Files.write(path, recipe.getBytes(StandardCharsets.UTF_8));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		};
	}

	private static List<TagRename> getRenames(Path dir) throws IOException {
		return Files.list(dir)
				.map(TagRename::new)
				.collect(Collectors.toList());
	}

	private static class TagRename {
		Path parent;

		String oldName;
		String newName;

		public TagRename(Path tag) {
			this.parent = tag.getParent();
			this.oldName = tag.getFileName().toString().replace(".json", "");
			this.newName = oldName + "s";
		}

		Path getOldPath() {
			return parent.resolve(oldName + ".json");
		}

		Path getNewPath() {
			return parent.resolve(newName + ".json");
		}

		String getOldTagName() {
			return TAG_NAMESPACE + ":" + oldName;
		}

		String getNewTagName() {
			return TAG_NAMESPACE + ":" + newName;
		}

	}

}
