package techreborn.blocks;

import net.minecraft.block.material.Material;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;
import techreborn.client.TechRebornCreativeTab;

public class BlockElectricCraftingTable extends BlockMachineBase implements IAdvancedRotationTexture
{

	private final String prefix = "techreborn:blocks/machine/greg_machines/";

	public BlockElectricCraftingTable(Material material)
	{
		super();
		setUnlocalizedName("techreborn.electriccraftingtable");
		setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public String getFront(boolean isActive)
	{
		return prefix + "electric_crafting_table_front";
	}

	@Override
	public String getSide(boolean isActive)
	{
		return prefix + "machine_side";
	}

	@Override
	public String getTop(boolean isActive)
	{
		return prefix + "electric_crafting_table_top";
	}

	@Override
	public String getBottom(boolean isActive)
	{
		return prefix + "machine_bottom";
	}

}
