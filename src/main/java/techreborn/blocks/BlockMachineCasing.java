package techreborn.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.api.IBlockTextureProvider;
import reborncore.common.multiblock.BlockMultiblockBase;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileMachineCasing;

import java.util.List;
import java.util.Random;

public class BlockMachineCasing extends BlockMultiblockBase implements IBlockTextureProvider{

    public static final String[] types = new String[]
            {"standard", "reinforced", "advanced"};

    public BlockMachineCasing(Material material) {
        super(material);
        setCreativeTab(TechRebornCreativeTab.instance);
        setUnlocalizedName("techreborn.machineCasing");
        setHardness(2F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(METADATA, 0));
    }

    public PropertyInteger METADATA;

    @Override
    public IBlockState getStateFromMetaValue(int meta) {
        return this.getDefaultState().withProperty(METADATA, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (Integer) state.getValue(METADATA);
    }

    protected BlockState createBlockState() {

        METADATA = PropertyInteger.create("Type", 0, types.length);
        return new BlockState(this, METADATA);
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
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        for (int meta = 0; meta < types.length; meta++) {
            list.add(new ItemStack(item, 1, meta));
        }
    }

    @Override
    public int damageDropped(IBlockState state) {
        return super.damageDropped(state);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileMachineCasing();
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        Block b = worldIn.getBlockState(pos).getBlock();
        return  b == (Block) this ? false : super.shouldSideBeRendered(worldIn, pos, side);
    }

    public boolean shouldConnectToBlock(IBlockAccess blockAccess, int x, int y, int z, Block block, int meta) {
        return block == (Block) this;
    }

    @Override
    public String getTextureName(IBlockState blockState, EnumFacing facing) {
        return "techreborn:blocks/machine/casing" + types[getMetaFromState(blockState)] + "_full";
    }

    @Override
    public int amountOfVariants() {
        return types.length;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getStateFromMetaValue(meta);
    }
}
