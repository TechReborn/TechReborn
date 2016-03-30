package techreborn.blocks.generator;

import net.minecraft.block.material.Material;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;
import techreborn.client.TechRebornCreativeTab;

public class BlockLightningRod extends BlockMachineBase implements IAdvancedRotationTexture
{

	private final String prefix = "techreborn:blocks/machine/generators/";

	public BlockLightningRod(Material material)
	{
		super();
		setUnlocalizedName("techreborn.lightningrod");
		setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public String getFront(boolean isActive)
	{
		return isActive ? prefix + "lightning_rod_side_on" : prefix + "lightning_rod_side_off";
	}

	@Override
	public String getSide(boolean isActive)
	{
		return isActive ? prefix + "lightning_rod_side_on" : prefix + "lightning_rod_side_off";
	}

	@Override
	public String getTop(boolean isActive)
	{
		return isActive ? prefix + "lightning_rod_top_on" : prefix + "lightning_rod_top_off";
	}

	@Override
	public String getBottom(boolean isActive)
	{
		return prefix + "generator_machine_bottom";
	}
}
