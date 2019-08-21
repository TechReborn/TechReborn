package techreborn.blocks.misc;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.sound.BlockSoundGroup;

/**
 * @author drcrazy
 *
 */
public class RubberPressurePlateBlock extends PressurePlateBlock {

	public RubberPressurePlateBlock() {
		super(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.of(Material.WOOD, MaterialColor.SPRUCE).noCollision().strength(0.5f, 0.5f).sounds(BlockSoundGroup.WOOD).build());
	}

}
