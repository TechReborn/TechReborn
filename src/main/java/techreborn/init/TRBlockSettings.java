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

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import techreborn.TechReborn;

public class TRBlockSettings {
	private static AbstractBlock.Settings metal(String name) {
		return AbstractBlock.Settings.create()
			.sounds(BlockSoundGroup.METAL)
			.mapColor(MapColor.IRON_GRAY)
			.strength(2f, 2f)
			.registryKey(key(name));
	}

	public static AbstractBlock.Settings machine(String name) {
		return metal(name);
	}

	public static AbstractBlock.Settings nuke(String name) {
		return AbstractBlock.Settings.create()
			.strength(2F, 2F)
			.mapColor(MapColor.BRIGHT_RED)
			.registryKey(key(name));
	}

	public static AbstractBlock.Settings reinforcedGlass(String name) {
		return AbstractBlock.Settings.copy(Blocks.GLASS)
			.strength(4f, 60f)
			.sounds(BlockSoundGroup.STONE)
			.registryKey(key(name));
	}

	private static AbstractBlock.Settings rubber(boolean noCollision, float hardness, float resistance, String name) {
		var settings = AbstractBlock.Settings.create()
			.mapColor(MapColor.SPRUCE_BROWN)
			.strength(hardness, resistance)
			.sounds(BlockSoundGroup.WOOD)
			.registryKey(key(name));

		if (noCollision) {
			settings.noCollision();
		}

		return settings;
	}

	private static AbstractBlock.Settings rubber(float hardness, float resistance, String name) {
		return rubber(false, hardness, resistance, name);
	}

	public static AbstractBlock.Settings rubberWood(String name) {
		return rubber(2f, 2f, name)
			.burnable();
	}

	public static AbstractBlock.Settings rubberWoodStripped(String name) {
		return rubberWood(name)
			.strength(2.0F, 15.0F);
	}

	public static AbstractBlock.Settings rubberLeaves(String name) {
		return AbstractBlock.Settings.copy(Blocks.SPRUCE_LEAVES)
			.mapColor(MapColor.SPRUCE_BROWN)
			.registryKey(key(name));
	}

	public static AbstractBlock.Settings rubberSapling(String name) {
		return AbstractBlock.Settings.copy(Blocks.SPRUCE_SAPLING)
			.mapColor(MapColor.SPRUCE_BROWN)
			.registryKey(key(name));
	}

	public static AbstractBlock.Settings rubberLog(String name) {
		return AbstractBlock.Settings.copy(Blocks.SPRUCE_LOG)
			.ticksRandomly()
			.mapColor(MapColor.SPRUCE_BROWN)
			.registryKey(key(name));
	}

	public static AbstractBlock.Settings rubberLogStripped(String name) {
		return rubberLog(name).strength(2.0F, 15.0F);
	}

	public static AbstractBlock.Settings rubberSlab(String name) {
		return rubberLog(name);
	}

	public static AbstractBlock.Settings rubberFence(String name) {
		return rubberLog(name);
	}

	public static AbstractBlock.Settings rubberFenceGate(String name) {
		return rubberLog(name);
	}

	public static AbstractBlock.Settings pottedRubberSapling(String name) {
		return AbstractBlock.Settings.copy(Blocks.POTTED_SPRUCE_SAPLING)
			.registryKey(key(name));
	}

	public static AbstractBlock.Settings copperWall(String name) {
		return AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK)
			.strength(2f, 2f)
			.registryKey(key(name));
	}

	public static AbstractBlock.Settings rubberTrapdoor(String name) {
		return rubber(3.0F, 3.0F, name);
	}

	public static AbstractBlock.Settings rubberDoor(String name) {
		return rubber(3.0F, 3.0F, name);
	}

	public static AbstractBlock.Settings rubberButton(String name) {
		return rubber(true, 0.5F, 0.5F, name);
	}

	public static AbstractBlock.Settings rubberPressurePlate(String name) {
		return rubber(true, 0.5F, 0.5F, name);
	}

	public static AbstractBlock.Settings refinedIronFence(String name) {
		return metal(name)
			.strength(2.0F, 3.0F);
	}

	public static AbstractBlock.Settings storageBlock(boolean isHot, float hardness, float resistance, String name) {
		AbstractBlock.Settings settings = AbstractBlock.Settings.create()
			.strength(hardness, resistance)
			.mapColor(MapColor.IRON_GRAY) // TODO 1.20 maybe set the color based off the block?
			.sounds(BlockSoundGroup.METAL)
			.registryKey(key(name));

		if (isHot) {
			settings = settings.luminance(state -> 15)
				.nonOpaque();
		}

		return settings;
	}

	public static AbstractBlock.Settings ore(boolean deepslate, String name) {
		return AbstractBlock.Settings.create()
			.requiresTool()
			.sounds(deepslate ? BlockSoundGroup.DEEPSLATE : BlockSoundGroup.STONE)
			.hardness(deepslate ? 4.5f : 3f)
			.resistance(3f)
			.registryKey(key(name));
	}

	public static AbstractBlock.Settings machineFrame(String name) {
		return metal(name)
			.strength(1f, 1f);
	}

	public static AbstractBlock.Settings machineCasing(String name) {
		return metal(name)
			.strength(2f, 2f)
			.requiresTool();
	}

	public static AbstractBlock.Settings energyStorage(String name) {
		return metal(name);
	}

	public static AbstractBlock.Settings lsuStorage(String name) {
		return metal(name);
	}

	public static AbstractBlock.Settings storageUnit(boolean wooden, String name) {
		if (!wooden) {
			return metal(name);
		}

		return AbstractBlock.Settings.create()
			.sounds(BlockSoundGroup.WOOD)
			.mapColor(MapColor.OAK_TAN)
			.strength(2f, 2f)
			.registryKey(key(name));
	}

	public static AbstractBlock.Settings fusionCoil(String name) {
		return metal(name);
	}

	public static AbstractBlock.Settings transformer(String name) {
		return metal(name);
	}

	public static AbstractBlock.Settings playerDetector(String name) {
		return metal(name);
	}

	public static AbstractBlock.Settings fluid(String name) {
		return AbstractBlock.Settings.copy(Blocks.WATER)
			.registryKey(key(name));
	}

	public static AbstractBlock.Settings computerCube(String name) {
		return metal(name);
	}

	public static AbstractBlock.Settings alarm(String name) {
		return metal(name);
	}

	public static AbstractBlock.Settings genericMachine(String name) {
		return metal(name);
	}

	public static AbstractBlock.Settings tankUnit(String name) {
		return metal(name);
	}

	public static AbstractBlock.Settings fusionControlComputer(String name) {
		return metal(name);
	}

	public static AbstractBlock.Settings solarPanel(String name) {
		return metal(name);
	}

	public static AbstractBlock.Settings cable(String name) {
		return metal(name).strength(1f, 8f);
	}

	public static AbstractBlock.Settings resinBasin(String name) {
		return AbstractBlock.Settings.create()
			.mapColor(MapColor.OAK_TAN)
			.sounds(BlockSoundGroup.WOOD)
			.strength(2F, 2F)
			.registryKey(key(name));
	}

	public static AbstractBlock.Settings lightBlock(String name) {
		return AbstractBlock.Settings.copy(Blocks.REDSTONE_BLOCK)
			.strength(2f, 2f)
			.registryKey(key(name));
	}

	public static RegistryKey<Block> key(String name) {
		return RegistryKey.of(Registries.BLOCK.getKey(), Identifier.of(TechReborn.MOD_ID, name));
	}
}
