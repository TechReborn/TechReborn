package techreborn.blocks;

import me.modmuss50.jsonDestroyer.api.ITexturedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import reborncore.RebornCore;
import techreborn.client.TechRebornCreativeTabMisc;

/**
 * Created by modmuss50 on 20/02/2016.
 */
public class BlockRubberPlank extends Block implements ITexturedBlock {

	public BlockRubberPlank() {
		super(Material.WOOD);
		RebornCore.jsonDestroyer.registerObject(this);
		setUnlocalizedName("techreborn.rubberplank");
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		this.setHardness(2.0F);
		this.setSoundType(SoundType.WOOD);
		Blocks.FIRE.setFireInfo(this, 5, 20);
	}

	@Override
	public String getTextureNameFromState(IBlockState state, EnumFacing side) {
		return "techreborn:blocks/rubber_planks";
	}

	@Override
	public int amountOfStates() {
		return 1;
	}
}
