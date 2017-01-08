package techreborn.blocks.iron_machines;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IRotationTexture;

import techreborn.Core;
import techreborn.client.EGui;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileAlloyFurnace;

import java.util.ArrayList;
import java.util.List;

public class BlockAlloyFurnace extends BlockMachineBase implements IRotationTexture {

	private final String prefix = "techreborn:blocks/machine/iron_machines/";

	public BlockAlloyFurnace(final Material material) {
		super();
		this.setUnlocalizedName("techreborn.alloyfurnace");
		this.setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int p_149915_2_) {
		return new TileAlloyFurnace();
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX,
			final float hitY, final float hitZ) {
		if (!player.isSneaking())
			player.openGui(Core.INSTANCE, EGui.ALLOY_FURNACE.ordinal(), world, x, y, z);
		return true;
	}

	@Override
	public List<ItemStack> getDrops(final IBlockAccess world, final BlockPos pos, final IBlockState state, final int fortune) {
		final List<ItemStack> items = new ArrayList<>();
		items.add(new ItemStack(this));
		return items;
	}

	@Override
	public String getFrontOff() {
		return this.prefix + "alloy_furnace_front_off";
	}

	@Override
	public String getFrontOn() {
		return this.prefix + "alloy_furnace_front_on";
	}

	@Override
	public String getSide() {
		return this.prefix + "iron_machine_side";
	}

	@Override
	public String getTop() {
		return this.prefix + "iron_machine_top";
	}

	@Override
	public String getBottom() {
		return this.prefix + "iron_machine_bottom";
	}
}
