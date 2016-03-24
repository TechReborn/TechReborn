package techreborn.blocks.generator;

import net.minecraft.block.material.Material;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;
import techreborn.client.TechRebornCreativeTab;

public class BlockPlasmaGenerator extends BlockMachineBase implements IAdvancedRotationTexture
{

	public BlockPlasmaGenerator(Material material)
	{
		super();
		setUnlocalizedName("techreborn.plasmagenerator");
		setCreativeTab(TechRebornCreativeTab.instance);
	}

	private final String prefix = "techreborn:blocks/machine/generators/";

	@Override
	public String getFront(boolean isActive)
	{
		return prefix + "plasma_generator_front";
	}

	@Override
	public String getSide(boolean isActive)
	{
		return prefix + "plasma_generator_side_off";
	}

	@Override
	public String getTop(boolean isActive)
	{
		return prefix + "plasma_generator_side_off";
	}

	@Override
	public String getBottom(boolean isActive)
	{
		return prefix + "plasma_generator_side_off";
	}
}
