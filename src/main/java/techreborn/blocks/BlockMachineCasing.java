package techreborn.blocks;

import com.google.common.base.CaseFormat;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.multiblock.BlockMultiblockBase;
import techreborn.client.TechRebornCreativeTab;
import techreborn.init.ModBlocks;
import techreborn.tiles.TileMachineCasing;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Random;

public class BlockMachineCasing extends BlockMultiblockBase {

	public static final String[] types = new String[] { "standard", "reinforced", "advanced" };
	public static final PropertyInteger METADATA = PropertyInteger.create("type", 0, types.length);

	public BlockMachineCasing(Material material) {
		super(material);
		setCreativeTab(TechRebornCreativeTab.instance);
		setUnlocalizedName("techreborn.machineCasing");
		setHardness(2F);
		this.setDefaultState(this.getDefaultState().withProperty(METADATA, 0));
	}

	public static ItemStack getStackByName(String name, int count) {
		name = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name);
		for (int i = 0; i < types.length; i++) {
			if (types[i].equalsIgnoreCase(name)) {
				return new ItemStack(ModBlocks.MACHINE_CASINGS, count, i);
			}
		}
		throw new InvalidParameterException("The machine casing " + name + " could not be found.");
	}

	public static ItemStack getStackByName(String name) {
		return getStackByName(name, 1);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(METADATA, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(METADATA);
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, METADATA);
	}

	public int getHeatFromState(IBlockState state) {
		switch (getMetaFromState(state)) {
			case 0:
				return 1020 / 25;
			case 1:
				return 1700 / 25;
			case 2:
				return 2380 / 25;
		}
		return 0;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
		for (int meta = 0; meta < types.length; meta++) {
			list.add(new ItemStack(item, 1, meta));
		}
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileMachineCasing();
	}

}
