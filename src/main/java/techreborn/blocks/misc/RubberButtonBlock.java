package techreborn.blocks.misc;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.block.WoodButtonBlock;
import net.minecraft.sound.BlockSoundGroup;

/**
 * @author drcrazy
 *
 */
public class RubberButtonBlock extends WoodButtonBlock {

	public RubberButtonBlock() {
		super(FabricBlockSettings.of(Material.PART).noCollision().strength(0.5f, 0.5f).sounds(BlockSoundGroup.WOOD).build());
	}
}
