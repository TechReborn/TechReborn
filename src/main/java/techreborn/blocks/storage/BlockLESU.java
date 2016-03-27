package techreborn.blocks.storage;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.init.ModBlocks;
import techreborn.tiles.lesu.TileLesu;

public class BlockLESU extends BlockMachineBase implements IAdvancedRotationTexture
{

	private final String prefix = "techreborn:blocks/machine/storage/";

	public BlockLESU(Material material)
	{
		super();
		setUnlocalizedName("techreborn.lesu");
		setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new TileLesu();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		if (!player.isSneaking())
			player.openGui(Core.INSTANCE, GuiHandler.lesuID, world, x, y, z);
		return true;
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		List<ItemStack> items = new ArrayList<ItemStack>();
		items.add(new ItemStack(ModBlocks.LesuStorage));
		return items;
	}

	@Override
	public String getFront(boolean isActive)
	{
		return prefix + "lesu_front";
	}

	@Override
	public String getSide(boolean isActive)
	{
		return prefix + "lesu_side";
	}

	@Override
	public String getTop(boolean isActive)
	{
		return prefix + "lesu_top";
	}

	@Override
	public String getBottom(boolean isActive)
	{
		return prefix + "lesu_bottom";
	}

}
