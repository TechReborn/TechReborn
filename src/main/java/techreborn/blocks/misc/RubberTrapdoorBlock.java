/**
 * 
 */
package techreborn.blocks.misc;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.sound.BlockSoundGroup;

/**
 * @author drcrazy
 *
 */
public class RubberTrapdoorBlock extends TrapdoorBlock {

	public RubberTrapdoorBlock() {
		super(FabricBlockSettings.of(Material.WOOD, MaterialColor.SPRUCE).strength(3f, 3f).sounds(BlockSoundGroup.WOOD).build());
	}

}
