package techreborn.blocks.storage;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import techreborn.blocks.BlockMachineBase;
import techreborn.tiles.lesu.TileLesuStorage;

public class BlockLesuStorage extends BlockMachineBase {

    public BlockLesuStorage(Material material) {
        super(material);
        setUnlocalizedName("techreborn.lesustorage");
    }


    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemstack) {
        super.onBlockPlacedBy(world, x, y, z, player, itemstack);
        if (world.getTileEntity(x, y, z) instanceof TileLesuStorage) {
            ((TileLesuStorage) world.getTileEntity(x, y, z)).rebuildNetwork();
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        if (world.getTileEntity(x, y, z) instanceof TileLesuStorage) {
            ((TileLesuStorage) world.getTileEntity(x, y, z)).removeFromNetwork();
        }
        super.breakBlock(world, x, y, z, block, meta);
    }


    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileLesuStorage();
    }

    public boolean shouldConnectToBlock(IBlockAccess blockAccess, int x, int y, int z, Block block, int meta) {
        return block == (Block) this;
    }
}
