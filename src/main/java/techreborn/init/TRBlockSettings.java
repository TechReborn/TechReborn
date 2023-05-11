package techreborn.init;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.sound.BlockSoundGroup;

public class TRBlockSettings {
	private static FabricBlockSettings metal() {
		return FabricBlockSettings.create()
			.sounds(BlockSoundGroup.METAL)
			.mapColor(MapColor.IRON_GRAY)
			.strength(2f, 2f);
	}

	public static FabricBlockSettings machine() {
		return metal();
	}

	public static FabricBlockSettings nuke() {
		return FabricBlockSettings.create()
			.strength(2F, 2F)
			.mapColor(MapColor.BRIGHT_RED);
	}

	public static FabricBlockSettings reinforcedGlass() {
		return FabricBlockSettings.copyOf(Blocks.GLASS)
			.strength(4f, 60f)
			.sounds(BlockSoundGroup.STONE);
	}

	private static FabricBlockSettings rubber(boolean noCollision, float hardness, float resistance) {
		var settings = FabricBlockSettings.create()
			.mapColor(MapColor.SPRUCE_BROWN)
			.strength(hardness, resistance)
			.sounds(BlockSoundGroup.WOOD);

		if (noCollision) {
			settings.noCollision();
		}

		return settings;
	}

	private static FabricBlockSettings rubber(float hardness, float resistance) {
		return rubber(false, hardness, resistance);
	}

	public static FabricBlockSettings rubberWood() {
		return rubber(2f, 2f)
			.burnable();
	}

	public static FabricBlockSettings rubberLeaves() {
		return FabricBlockSettings.copyOf(Blocks.OAK_LEAVES)
			.mapColor(MapColor.SPRUCE_BROWN);
	}

	public static FabricBlockSettings rubberSapling() {
		return FabricBlockSettings.copyOf(Blocks.OAK_SAPLING)
			.mapColor(MapColor.SPRUCE_BROWN);
	}

	public static FabricBlockSettings rubberLog() {
		return FabricBlockSettings.copyOf(Blocks.OAK_LOG)
			.mapColor(MapColor.SPRUCE_BROWN);
	}

	public static FabricBlockSettings rubberTrapdoor() {
		return rubber(3.0F, 3.0F);
	}

	public static FabricBlockSettings rubberDoor() {
		return rubber(3.0F, 3.0F);
	}

	public static FabricBlockSettings rubberButton() {
		return rubber(true, 0.5F, 0.5F);
	}

	public static FabricBlockSettings rubberPressurePlate() {
		return rubber(true, 0.5F, 0.5F);
	}

	public static FabricBlockSettings refinedIronFence() {
		return metal()
			.strength(2.0F, 3.0F);
	}

	public static FabricBlockSettings storageBlock(boolean isHot, float hardness, float resistance) {
		FabricBlockSettings settings = FabricBlockSettings.create()
			.strength(hardness, resistance)
			.mapColor(MapColor.IRON_GRAY) // TODO 1.20 maybe set the color based off the block?
			.sounds(BlockSoundGroup.METAL);

		if (isHot) {
			settings = settings.luminance(15)
				.nonOpaque();
		}

		return settings;
	}

	public static FabricBlockSettings ore(boolean deepslate) {
		return FabricBlockSettings.create()
			.requiresTool()
			.sounds(deepslate ? BlockSoundGroup.DEEPSLATE : BlockSoundGroup.STONE)
			.hardness(deepslate ? 4.5f : 3f)
			.resistance(3f);
	}

	public static FabricBlockSettings machineFrame() {
		return metal()
			.strength(1f, 1f);
	}

	public static FabricBlockSettings machineCasing() {
		return metal()
			.strength(2f, 2f)
			.requiresTool();
	}

	public static FabricBlockSettings energyStorage() {
		return metal();
	}

	public static FabricBlockSettings lsuStorage() {
		return metal();
	}

	public static FabricBlockSettings storageUnit(boolean wooden) {
		if (!wooden) {
			return metal();
		}

		return FabricBlockSettings.create()
			.sounds(BlockSoundGroup.WOOD)
			.mapColor(MapColor.OAK_TAN)
			.strength(2f, 2f);
	}

	public static FabricBlockSettings fusionCoil() {
		return metal();
	}

	public static FabricBlockSettings transformer() {
		return metal();
	}

	public static FabricBlockSettings playerDetector() {
		return metal();
	}

	public static FabricBlockSettings fluid() {
		return FabricBlockSettings.copyOf(Blocks.WATER);
	}

	public static FabricBlockSettings computerCube() {
		return metal();
	}

	public static FabricBlockSettings alarm() {
		return metal();
	}

	public static FabricBlockSettings genericMachine() {
		return metal();
	}

	public static FabricBlockSettings tankUnit() {
		return metal();
	}

	public static FabricBlockSettings fusionControlComputer() {
		return metal();
	}

	public static FabricBlockSettings solarPanel() {
		return metal();
	}

	public static FabricBlockSettings cable() {
		return metal().strength(1f, 8f);
	}

	public static FabricBlockSettings resinBasin() {
		return FabricBlockSettings.create()
			.mapColor(MapColor.OAK_TAN)
			.sounds(BlockSoundGroup.WOOD)
			.strength(2F, 2F);
	}

	public static FabricBlockSettings lightBlock() {
		return FabricBlockSettings.copyOf(Blocks.REDSTONE_BLOCK)
			.strength(2f, 2f);
	}
}
