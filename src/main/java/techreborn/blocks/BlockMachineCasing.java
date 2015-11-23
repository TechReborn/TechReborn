package techreborn.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.multiblock.BlockMultiblockBase;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileMachineCasing;

import java.util.List;
import java.util.Random;

public class BlockMachineCasing extends BlockMultiblockBase {

    public static final String[] types = new String[]
            {"standard", "reinforced", "advanced"};

    public BlockMachineCasing(Material material) {
        super(material);
        setCreativeTab(TechRebornCreativeTab.instance);
        setUnlocalizedName("techreborn.machineCasing");
        setHardness(2F);
    }

    public static int getHeatFromMeta(int meta) {
        switch (meta) {
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
    public Item getItemDropped(int meta, Random random, int fortune) {
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
    public int damageDropped(int metaData) {
        return metaData;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileMachineCasing();
    }


    @Override
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        Block b = par1IBlockAccess.getBlock(par2, par3, par4);
        return b == (Block) this ? false : super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
    }

    public boolean shouldConnectToBlock(IBlockAccess blockAccess, int x, int y, int z, Block block, int meta) {
        return block == (Block) this;
    }
}
