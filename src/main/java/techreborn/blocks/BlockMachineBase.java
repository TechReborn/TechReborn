package techreborn.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileMachineBase;

import java.util.Random;

public class BlockMachineBase extends BlockContainer {

    public BlockMachineBase(Material material) {
        super(Material.rock);
        setCreativeTab(TechRebornCreativeTab.instance);
        setHardness(2f);
        setStepSound(soundTypeMetal);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return null;
    }

    public void onBlockAdded(World world, int x, int y, int z) {

        super.onBlockAdded(world, x, y, z);
        this.setDefaultDirection(world, x, y, z);

    }

    private void setDefaultDirection(World world, int x, int y, int z) {

        if (!world.isRemote) {
            Block block1 = world.getBlock(x, y, z - 1);
            Block block2 = world.getBlock(x, y, z + 1);
            Block block3 = world.getBlock(x - 1, y, z);
            Block block4 = world.getBlock(x + 1, y, z);

            byte b = 3;

            if (block1.func_149730_j() && !block2.func_149730_j()) {
                b = 3;
            }
            if (block2.func_149730_j() && !block1.func_149730_j()) {
                b = 2;
            }
            if (block3.func_149730_j() && !block4.func_149730_j()) {
                b = 5;
            }
            if (block4.func_149730_j() && !block3.func_149730_j()) {
                b = 4;
            }

            world.setBlockMetadataWithNotify(x, y, z, b, 2);
            setTileMeta(world, x, y, z, b);

        }

    }

    public void onBlockPlacedBy(World world, int x, int y, int z,
                                EntityLivingBase player, ItemStack itemstack) {

        int l = MathHelper
                .floor_double((double) (player.rotationYaw * 4.0F / 360F) + 0.5D) & 3;

        if (l == 0) {
            setTileMeta(world, x, y, z, 2);
        }
        if (l == 1) {
            setTileMeta(world, x, y, z, 5);
        }
        if (l == 2) {
            setTileMeta(world, x, y, z, 3);
        }
        if (l == 3) {
            setTileMeta(world, x, y, z, 4);
        }
        super.onBlockPlacedBy(world, x, y, z, player, itemstack);
    }

    public boolean canCreatureSpawn(EnumCreatureType type, World world, int x,
                                    int y, int z) {
        return false;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        dropInventory(world, x, y, z);
        super.breakBlock(world, x, y, z, block, meta);
    }

    protected void dropInventory(World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (!(tileEntity instanceof IInventory)) {
            return;
        }

        IInventory inventory = (IInventory) tileEntity;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack itemStack = inventory.getStackInSlot(i);

            if (itemStack != null && itemStack.stackSize > 0) {
                if (itemStack.getItem() instanceof ItemBlock) {
                    if (((ItemBlock) itemStack.getItem()).field_150939_a instanceof BlockLiquid || ((ItemBlock) itemStack.getItem()).field_150939_a instanceof BlockStaticLiquid || ((ItemBlock) itemStack.getItem()).field_150939_a instanceof BlockDynamicLiquid) {
                        return;
                    }
                }
                Random rand = new Random();

                float dX = rand.nextFloat() * 0.8F + 0.1F;
                float dY = rand.nextFloat() * 0.8F + 0.1F;
                float dZ = rand.nextFloat() * 0.8F + 0.1F;

                EntityItem entityItem = new EntityItem(world, x + dX, y + dY, z + dZ, itemStack.copy());

                if (itemStack.hasTagCompound()) {
                    entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
                }

                float factor = 0.05F;
                entityItem.motionX = rand.nextGaussian() * factor;
                entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                entityItem.motionZ = rand.nextGaussian() * factor;
                world.spawnEntityInWorld(entityItem);
                itemStack.stackSize = 0;
            }
        }
    }

    public void setTileMeta(World world, int x, int y, int z, int meta){
        if(world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileMachineBase){
            ((TileMachineBase) world.getTileEntity(x, y, z)).setMeta(meta);
        }
    }

    public int getTileMeta(World world, int x, int y, int z){
        if(world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileMachineBase){
            return ((TileMachineBase) world.getTileEntity(x, y, z)).getMeta();
        }
        return 0;
    }

    public int getTileMeta(IBlockAccess blockAccess, int x, int y, int z){
        return blockAccess.getTileEntity(x, y, z) != null ? getTileMeta(blockAccess.getTileEntity(x, y,z).getWorldObj(), x, y, z) : 0;
    }
}
