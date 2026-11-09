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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import reborncore.common.config.ConfigValue;
import techreborn.config.TechRebornConfig;

public record WorldgenConfigCondition(String config) implements ResourceCondition {
	private static final Map<String, ConfigValue<Boolean>> CONFIG_VALUES = createConfigValues();
	public static final ResourceConditionType<WorldgenConfigCondition> TYPE = ResourceConditionType.create(
		Identifier.fromNamespaceAndPath("techreborn", "worldgen_config"),
		Codec.STRING.validate(config -> CONFIG_VALUES.containsKey(config)
			? DataResult.success(config)
			: DataResult.error(() -> "Unknown world generation config: " + config))
			.fieldOf("config").xmap(WorldgenConfigCondition::new, WorldgenConfigCondition::config)
	);

	private static Map<String, ConfigValue<Boolean>> createConfigValues() {
		Map<String, ConfigValue<Boolean>> values = new HashMap<>();
		values.put("ores", TechRebornConfig.enableOreGeneration);
		values.put("rubber_trees", TechRebornConfig.enableRubberTreeGeneration);
		values.put("oil_lakes", TechRebornConfig.enableOilLakeGeneration);
		for (OreDistribution ore : OreDistribution.values()) {
			values.put(ore.name().toLowerCase(Locale.ROOT), ore.generationEnabled);
		}
		return Map.copyOf(values);
	}

	@Override
	public ResourceConditionType<?> getType() {
		return TYPE;
	}

	@Override
	public boolean test(RegistryOps.RegistryInfoLookup registryInfo) {
		return CONFIG_VALUES.get(config).get();
	}
}
