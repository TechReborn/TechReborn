package techreborn.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.Loader;
import techreborn.client.TechRebornCreativeTab;
import techreborn.init.ModBlocks;
import techreborn.tiles.TileMachineBase;

import java.util.ArrayList;
import java.util.List;
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
        return new TileMachineBase();
    }

    @Deprecated
    public void onBlockAdded(World world, int x, int y, int z) {
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        onBlockAdded(worldIn, pos.getX(), pos.getY(), pos.getZ());
        this.setDefaultDirection(worldIn, pos);
    }

    private void setDefaultDirection(World world, BlockPos pos) {

        if (!world.isRemote) {
            Block block1 = world.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1)).getBlock();
            Block block2 = world.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1)).getBlock();
            Block block3 = world.getBlockState(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ())).getBlock();
            Block block4 = world.getBlockState(new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ())).getBlock();

            byte b = 3;

            if (block1.isOpaqueCube() && !block2.isOpaqueCube()) {
                b = 3;
            }
            if (block2.isOpaqueCube() && !block1.isOpaqueCube()) {
                b = 2;
            }
            if (block3.isOpaqueCube() && !block4.isOpaqueCube()) {
                b = 5;
            }
            if (block4.isOpaqueCube() && !block3.isOpaqueCube()) {
                b = 4;
            }

            //TODO 1.8 meta
          //  world.setBlockMetadataWithNotify(x, y, z, b, 2);
            setTileRotation(world, pos, b);

        }

    }

    @Deprecated
    public void onBlockPlacedBy(World world, int x, int y, int z,
                                EntityLivingBase player, ItemStack itemstack) {


    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        onBlockPlacedBy(worldIn, pos.getX(), pos.getY(), pos.getZ(), placer, stack);

        int l = MathHelper
                .floor_double((double) (placer.rotationYaw * 4.0F / 360F) + 0.5D) & 3;

        if (l == 0) {
            setTileRotation(worldIn, pos,  2);
        }
        if (l == 1) {
            setTileRotation(worldIn, pos, 5);
        }
        if (l == 2) {
            setTileRotation(worldIn, pos, 3);
        }
        if (l == 3) {
            setTileRotation(worldIn, pos, 4);
        }
    }

    public boolean canCreatureSpawn(EnumCreatureType type, World world, int x,
                                    int y, int z) {
        return false;
    }

    @Deprecated
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {

    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        breakBlock(worldIn, pos.getX(), pos.getY(), pos.getZ(), state.getBlock(), 0);
        dropInventory(worldIn, pos);
    }

    protected void dropInventory(World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (!(tileEntity instanceof IInventory)) {
            return;
        }

        IInventory inventory = (IInventory) tileEntity;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack itemStack = inventory.getStackInSlot(i);

            if (itemStack != null && itemStack.stackSize > 0) {
                if (itemStack.getItem() instanceof ItemBlock) {
                    if (((ItemBlock) itemStack.getItem()).block instanceof BlockFluidBase || ((ItemBlock) itemStack.getItem()).block instanceof BlockStaticLiquid || ((ItemBlock) itemStack.getItem()).block instanceof BlockDynamicLiquid) {
                        return;
                    }
                }
                Random rand = new Random();

                float dX = rand.nextFloat() * 0.8F + 0.1F;
                float dY = rand.nextFloat() * 0.8F + 0.1F;
                float dZ = rand.nextFloat() * 0.8F + 0.1F;

                EntityItem entityItem = new EntityItem(world, pos.getX() + dX, pos.getY() + dY, pos.getZ() + dZ, itemStack.copy());

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

    public void setTileRotation(World world, BlockPos pos, int meta) {
        if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileMachineBase) {
            ((TileMachineBase) world.getTileEntity(pos)).setRotation(meta);
        }
    }

    public int getTileRotation(World world, BlockPos pos) {
        if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileMachineBase) {
            return ((TileMachineBase) world.getTileEntity(pos)).getRotation();
        }
        return 0;
    }

    public int getTileRotation(IBlockAccess blockAccess, BlockPos pos) {
        return blockAccess.getTileEntity(pos) != null ? getTileRotation(blockAccess.getTileEntity(pos).getWorld(), pos) : 0;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
//        if (Loader.isModLoaded("IC2")) {
//            ItemStack stack = IC2Items.getItem(isAdvanced() ? "advancedMachine" : "machine").copy();
//            stack.stackSize = 1;
//            items.add(stack);
//        } //TODO ic2
        items.add(isAdvanced() ? new ItemStack(Item.getItemFromBlock(ModBlocks.MachineCasing), 1, 2) : new ItemStack(Item.getItemFromBlock(ModBlocks.MachineCasing), 1, 0));
        return items;
    }

    public boolean isAdvanced() {
        return false;
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        if (axis == null) {
            return false;
        } else {
            TileEntity tile = world.getTileEntity(pos);
            if (tile != null && tile instanceof TileMachineBase) {
                TileMachineBase machineBase = (TileMachineBase) tile;
                machineBase.setRotation(EnumFacing.getFront(machineBase.getRotation()).getOpposite().ordinal());
                return true;
            }
            return false;
        }
    }


    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(fillBlockWithFluid(worldIn, pos, playerIn)){
            return true;
        }
        if(onBlockActivated(worldIn, pos.getX(), pos.getY(), pos.getZ(), playerIn, side.getIndex(), hitX, hitY, hitZ)){
            return true;
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
    }

    @Deprecated
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    public boolean fillBlockWithFluid(World world, BlockPos pos, EntityPlayer entityplayer) {
        ItemStack current = entityplayer.inventory.getCurrentItem();

        if (current != null) {
            TileEntity tile = world.getTileEntity(pos);

            if (tile instanceof IFluidHandler) {
                IFluidHandler tank = (IFluidHandler) tile;
                // Handle FluidContainerRegistry
                if (FluidContainerRegistry.isContainer(current)) {
                    FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(current);
                    // Handle filled containers
                    if (liquid != null) {
                        int qty = tank.fill(null, liquid, true);

                        if (qty != 0 && !entityplayer.capabilities.isCreativeMode) {
                            if (current.stackSize > 1) {
                                if (!entityplayer.inventory.addItemStackToInventory(FluidContainerRegistry.drainFluidContainer(current))) {
                                    entityplayer.dropPlayerItemWithRandomChoice(FluidContainerRegistry.drainFluidContainer(current), false);
                                }

                                entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, consumeItem(current));
                            } else {
                                entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, FluidContainerRegistry.drainFluidContainer(current));
                            }
                        }

                        return true;

                        // Handle empty containers
                    } else {
                        FluidStack available = tank.getTankInfo(null)[0].fluid;

                        if (available != null) {
                            ItemStack filled = FluidContainerRegistry.fillFluidContainer(available, current);

                            liquid = FluidContainerRegistry.getFluidForFilledItem(filled);

                            if (liquid != null) {
                                if (!entityplayer.capabilities.isCreativeMode) {
                                    if (current.stackSize > 1) {
                                        if (!entityplayer.inventory.addItemStackToInventory(filled)) {
                                            return false;
                                        } else {
                                            entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, consumeItem(current));
                                        }
                                    } else {
                                        entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, consumeItem(current));
                                        entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, filled);
                                    }
                                }

                                tank.drain(null, liquid.amount, true);

                                return true;
                            }
                        }
                    }
                } else if (current.getItem() instanceof IFluidContainerItem) {
                    if (current.stackSize != 1) {
                        return false;
                    }

                    if (!world.isRemote) {
                        IFluidContainerItem container = (IFluidContainerItem) current.getItem();
                        FluidStack liquid = container.getFluid(current);
                        FluidStack tankLiquid = tank.getTankInfo(null)[0].fluid;
                        boolean mustDrain = liquid == null || liquid.amount == 0;
                        boolean mustFill = tankLiquid == null || tankLiquid.amount == 0;
                        if (mustDrain && mustFill) {
                            // Both are empty, do nothing
                        } else if (mustDrain || !entityplayer.isSneaking()) {
                            liquid = tank.drain(null, 1000, false);
                            int qtyToFill = container.fill(current, liquid, true);
                            tank.drain(null, qtyToFill, true);
                        } else if (mustFill || entityplayer.isSneaking()) {
                            if (liquid.amount > 0) {
                                int qty = tank.fill(null, liquid, false);
                                tank.fill(null, container.drain(current, qty, true), true);
                            }
                        }
                    }

                    return true;
                }
            }
        }
        return false;
    }

    public static ItemStack consumeItem(ItemStack stack) {
        if (stack.stackSize == 1) {
            if (stack.getItem().hasContainerItem(stack)) {
                return stack.getItem().getContainerItem(stack);
            } else {
                return null;
            }
        } else {
            stack.splitStack(1);

            return stack;
        }
    }

}
