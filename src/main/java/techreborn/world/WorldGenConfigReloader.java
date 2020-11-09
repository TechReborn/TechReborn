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

package techreborn.world;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import reborncore.common.crafting.ConditionManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;

public class WorldGenConfigReloader implements IdentifiableResourceReloadListener {

	private static final Logger LOGGER = LogManager.getLogger();

	@Override
	public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
		return CompletableFuture.supplyAsync(() -> loadConfig(manager), prepareExecutor)
				.thenCompose(synchronizer::whenPrepared)
				.thenApplyAsync(this::apply, applyExecutor);
	}

	private List<DataDrivenFeature> loadConfig(ResourceManager manager) {
		final Collection<Identifier> featureResources = manager.findResources("techreborn/features", s -> s.endsWith(".json"));
		final List<DataDrivenFeature> features = new LinkedList<>();

		for (Identifier resource : featureResources) {
			DataDrivenFeature identifiableObject = parse(DataDrivenFeature::deserialise, resource, manager);

			if (identifiableObject != null) {
				features.add(identifiableObject);
			}
		}

		LOGGER.info("Loaded " + features.size() + " features");

		return features;
	}

	@Nullable
	private <T> T parse(BiFunction<Identifier, JsonObject, T> deserialiser, Identifier resource, ResourceManager manager) {
		try(InputStreamReader inputStreamReader = new InputStreamReader(manager.getResource(resource).getInputStream(), StandardCharsets.UTF_8)) {
			JsonElement jsonElement = new JsonParser().parse(inputStreamReader);
			JsonObject jsonObject = jsonElement.getAsJsonObject();

			if (!ConditionManager.shouldLoadRecipe(jsonObject)) {
				return null;
			}

			return deserialiser.apply(resource, jsonObject);
		} catch (JsonParseException |IOException e) {
			LOGGER.error("Failed to parse " + resource.toString());
			LOGGER.error(e);
		}
		return null;
	}

	private Void apply(List<DataDrivenFeature> features) {
		WorldGenerator.worldGenObseravable.pushB(features);
		return null;
	}

	@Override
	public Identifier getFabricId() {
		return new Identifier("techreborn", "worldgenerator");
	}

	public static List<DataDrivenFeature> getActiveFeatures() {
		return WorldGenerator.worldGenObseravable.getB();
	}

	@Override
	public Collection<Identifier> getFabricDependencies() {
		// Load before tags, so we are soon enough to get into the world gen
		return Collections.singletonList(ResourceReloadListenerKeys.TAGS);
	}
}
