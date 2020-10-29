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

import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.world.biome.Biome;

import java.util.function.Predicate;

public enum WorldTargetType implements StringIdentifiable {
	DEFAULT("default", category -> category != Biome.Category.NETHER && category != Biome.Category.THEEND),
	NETHER("nether", category -> category == Biome.Category.NETHER),
	END("end", category -> category == Biome.Category.THEEND);

	private final String name;
	private final Predicate<Biome.Category> biomeCategoryPredicate;
	public static final Codec<WorldTargetType> CODEC = StringIdentifiable.createCodec(WorldTargetType::values, WorldTargetType::getByName);

	WorldTargetType(String name, Predicate<Biome.Category> biomeCategoryPredicate) {
		this.name = name;
		this.biomeCategoryPredicate = biomeCategoryPredicate;
	}

	public String getName() {
		return name;
	}

	public boolean isApplicable(Biome.Category biomeCategory) {
		return biomeCategoryPredicate.test(biomeCategory);
	}

	public static WorldTargetType getByName(String name) {
		return null;
	}

	@Override
	public String asString() {
		return name;
	}
}
