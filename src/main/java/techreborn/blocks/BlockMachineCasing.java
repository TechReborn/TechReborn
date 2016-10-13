package techreborn.blocks;

import com.google.common.collect.Lists;
import me.modmuss50.jsonDestroyer.api.ITexturedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.blocks.PropertyString;
import reborncore.common.multiblock.BlockMultiblockBase;
import reborncore.common.util.ArrayUtils;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileMachineCasing;

import java.util.List;
import java.util.Random;

public class BlockMachineCasing extends BlockMultiblockBase {

	public static final String[] types = new String[] { "standard", "reinforced", "advanced" };
	public static final PropertyString VARIANTS = new PropertyString("type", types);
	public static final List<String> typesList = Lists.newArrayList(ArrayUtils.arrayToLowercase(types));

	public BlockMachineCasing(Material material) {
		super(material);
		setCreativeTab(TechRebornCreativeTab.instance);
		setUnlocalizedName("techreborn.machineCasing");
		setHardness(2F);
		this.setDefaultState(this.getDefaultState().withProperty(VARIANTS, "standard"));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta > types.length) {
			meta = 0;
		}
		return getBlockState().getBaseState().withProperty(VARIANTS, typesList.get(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return typesList.indexOf(state.getValue(VARIANTS));
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANTS);
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

//	@Override
//	public String getTextureNameFromState(IBlockState blockState, EnumFacing facing) {
//		return "techreborn:blocks/machines/structure/casing" + types[getMetaFromState(blockState)] + "_full";
//	}
//
//	@Override
//	public int amountOfStates() {
//		return types.length;
//	}

}
