/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2023 TechReborn
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

package techreborn.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import techreborn.TechReborn;

public class TRBlockSettings {
	private static BlockBehaviour.Properties metal(String name) {
		return BlockBehaviour.Properties.of()
			.sound(SoundType.METAL)
			.mapColor(MapColor.METAL)
			.strength(2f, 2f)
			.setId(key(name));
	}

	public static BlockBehaviour.Properties machine(String name) {
		return metal(name);
	}

	public static BlockBehaviour.Properties nuke(String name) {
		return BlockBehaviour.Properties.of()
			.strength(2F, 2F)
			.mapColor(MapColor.FIRE)
			.setId(key(name));
	}

	public static BlockBehaviour.Properties reinforcedGlass(String name) {
		return BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)
			.strength(4f, 60f)
			.sound(SoundType.STONE)
			.setId(key(name));
	}

	private static BlockBehaviour.Properties rubber(boolean noCollision, float hardness, float resistance, String name) {
		var settings = BlockBehaviour.Properties.of()
			.mapColor(MapColor.PODZOL)
			.strength(hardness, resistance)
			.sound(SoundType.WOOD)
			.setId(key(name));

		if (noCollision) {
			settings.noCollission();
		}

		return settings;
	}

	private static BlockBehaviour.Properties rubber(float hardness, float resistance, String name) {
		return rubber(false, hardness, resistance, name);
	}

	public static BlockBehaviour.Properties rubberWood(String name) {
		return rubber(2f, 2f, name)
			.ignitedByLava();
	}

	public static BlockBehaviour.Properties rubberWoodStripped(String name) {
		return rubberWood(name)
			.strength(2.0F, 15.0F);
	}

	public static BlockBehaviour.Properties rubberLeaves(String name) {
		return BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_LEAVES)
			.mapColor(MapColor.PODZOL)
			.setId(key(name));
	}

	public static BlockBehaviour.Properties rubberSapling(String name) {
		return BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_SAPLING)
			.mapColor(MapColor.PODZOL)
			.setId(key(name));
	}

	public static BlockBehaviour.Properties rubberLog(String name) {
		return BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_LOG)
			.randomTicks()
			.mapColor(MapColor.PODZOL)
			.setId(key(name));
	}

	public static BlockBehaviour.Properties rubberLogStripped(String name) {
		return rubberLog(name).strength(2.0F, 15.0F);
	}

	public static BlockBehaviour.Properties rubberSlab(String name) {
		return rubberLog(name);
	}

	public static BlockBehaviour.Properties rubberFence(String name) {
		return rubberLog(name);
	}

	public static BlockBehaviour.Properties rubberFenceGate(String name) {
		return rubberLog(name);
	}

	public static BlockBehaviour.Properties pottedRubberSapling(String name) {
		return BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_SPRUCE_SAPLING)
			.setId(key(name));
	}

	public static BlockBehaviour.Properties copperWall(String name) {
		return BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK)
			.strength(2f, 2f)
			.setId(key(name));
	}

	public static BlockBehaviour.Properties rubberTrapdoor(String name) {
		return rubber(3.0F, 3.0F, name);
	}

	public static BlockBehaviour.Properties rubberDoor(String name) {
		return rubber(3.0F, 3.0F, name);
	}

	public static BlockBehaviour.Properties rubberButton(String name) {
		return rubber(true, 0.5F, 0.5F, name);
	}

	public static BlockBehaviour.Properties rubberPressurePlate(String name) {
		return rubber(true, 0.5F, 0.5F, name);
	}

	public static BlockBehaviour.Properties refinedIronFence(String name) {
		return metal(name)
			.strength(2.0F, 3.0F);
	}

	public static BlockBehaviour.Properties storageBlock(boolean isHot, float hardness, float resistance, String name) {
		BlockBehaviour.Properties settings = BlockBehaviour.Properties.of()
			.strength(hardness, resistance)
			.mapColor(MapColor.METAL) // TODO 1.20 maybe set the color based off the block?
			.sound(SoundType.METAL)
			.setId(key(name));

		if (isHot) {
			settings = settings.lightLevel(state -> 15)
				.noOcclusion();
		}

		return settings;
	}

	public static BlockBehaviour.Properties ore(boolean deepslate, String name) {
		return BlockBehaviour.Properties.of()
			.requiresCorrectToolForDrops()
			.sound(deepslate ? SoundType.DEEPSLATE : SoundType.STONE)
			.destroyTime(deepslate ? 4.5f : 3f)
			.explosionResistance(3f)
			.setId(key(name));
	}

	public static BlockBehaviour.Properties machineFrame(String name) {
		return metal(name)
			.strength(1f, 1f);
	}

	public static BlockBehaviour.Properties machineCasing(String name) {
		return metal(name)
			.strength(2f, 2f)
			.requiresCorrectToolForDrops();
	}

	public static BlockBehaviour.Properties energyStorage(String name) {
		return metal(name);
	}

	public static BlockBehaviour.Properties lsuStorage(String name) {
		return metal(name);
	}

	public static BlockBehaviour.Properties storageUnit(boolean wooden, String name) {
		if (!wooden) {
			return metal(name);
		}

		return BlockBehaviour.Properties.of()
			.sound(SoundType.WOOD)
			.mapColor(MapColor.WOOD)
			.strength(2f, 2f)
			.setId(key(name));
	}

	public static BlockBehaviour.Properties fusionCoil(String name) {
		return metal(name);
	}

	public static BlockBehaviour.Properties transformer(String name) {
		return metal(name);
	}

	public static BlockBehaviour.Properties playerDetector(String name) {
		return metal(name);
	}

	public static BlockBehaviour.Properties fluid(String name) {
		return BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)
			.setId(key(name));
	}

	public static BlockBehaviour.Properties computerCube(String name) {
		return metal(name);
	}

	public static BlockBehaviour.Properties alarm(String name) {
		return metal(name);
	}

	public static BlockBehaviour.Properties genericMachine(String name) {
		return metal(name);
	}

	public static BlockBehaviour.Properties tankUnit(String name) {
		return metal(name);
	}

	public static BlockBehaviour.Properties fusionControlComputer(String name) {
		return metal(name);
	}

	public static BlockBehaviour.Properties solarPanel(String name) {
		return metal(name);
	}

	public static BlockBehaviour.Properties cable(String name) {
		return metal(name).strength(1f, 8f);
	}

	public static BlockBehaviour.Properties resinBasin(String name) {
		return BlockBehaviour.Properties.of()
			.mapColor(MapColor.WOOD)
			.sound(SoundType.WOOD)
			.strength(2F, 2F)
			.setId(key(name));
	}

	public static BlockBehaviour.Properties lightBlock(String name) {
		return BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_BLOCK)
			.strength(2f, 2f)
			.setId(key(name));
	}

	public static ResourceKey<Block> key(String name) {
		return ResourceKey.create(BuiltInRegistries.BLOCK.key(), ResourceLocation.fromNamespaceAndPath(TechReborn.MOD_ID, name));
	}
}
