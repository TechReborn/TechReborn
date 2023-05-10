package techreborn.init;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.sound.BlockSoundGroup;

public class TRBlockSettings {
	public static FabricBlockSettings machine() {
		return FabricBlockSettings.of()
			.strength(2F, 2F)
			.mapColor(MapColor.IRON_GRAY);
	}

	public static FabricBlockSettings nuke() {
		return FabricBlockSettings.of()
			.strength(2F, 2F)
			.mapColor(MapColor.BRIGHT_RED);
	}

	public static FabricBlockSettings reinforcedGlass() {
		return FabricBlockSettings.copyOf(Blocks.GLASS)
			.strength(4f, 60f)
			.sounds(BlockSoundGroup.STONE);
	}

	public static FabricBlockSettings rubberWood() {
		//TODO 1.20: remove cast with https://github.com/FabricMC/fabric/pull/3056
		return (FabricBlockSettings) FabricBlockSettings.of()
			.strength(2f, 2f)
			.sounds(BlockSoundGroup.WOOD)
			.burnable();
	}
}
