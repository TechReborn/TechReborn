/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2024 TeamReborn
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

package reborncore.client.gui;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ThemeManager implements SimpleResourceReloadListener<Theme> {
	private static Theme theme = null;

	@Override
	public Identifier getFabricId() {
		return Identifier.of("reborncore", "theme_manager");
	}

	@Override
	public CompletableFuture<Theme> load(ResourceManager manager, Profiler profiler, Executor executor) {
		return CompletableFuture.supplyAsync(() -> {
			Optional<Resource> theme = manager.getResource(Identifier.of("reborncore", "theme.json"));

			if (theme.isEmpty()) {
				throw new IllegalStateException("Failed to find reborn core theme.json");
			}

			try (InputStreamReader reader = new InputStreamReader(theme.get().getInputStream())) {
				JsonElement element = JsonParser.parseReader(reader);
				return Theme.CODEC.parse(JsonOps.INSTANCE, element).getOrThrow(JsonParseException::new);
			} catch (Exception e) {
				throw new IllegalStateException("Failed to parse reborn core theme.json", e);
			}
		}, executor);
	}

	@Override
	public CompletableFuture<Void> apply(Theme theme, ResourceManager manager, Profiler profiler, Executor executor) {
		// Set the theme instance on the main thread.
		return CompletableFuture.runAsync(() -> ThemeManager.theme = theme, executor);
	}

	public static Theme getTheme() {
		return Objects.requireNonNull(theme, "Theme not loaded");
	}
}
