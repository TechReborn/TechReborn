package techreborn.blocks;

import cpw.mods.fml.common.Loader;
import ic2.api.item.IC2Items;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;
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
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import techreborn.client.TechRebornCreativeTab;
import techreborn.init.ModBlocks;
import techreborn.tiles.TileMachineBase;

import java.util.ArrayList;
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
            setTileRotation(world, x, y, z, b);

        }

    }

    public void onBlockPlacedBy(World world, int x, int y, int z,
                                EntityLivingBase player, ItemStack itemstack) {

        int l = MathHelper
                .floor_double((double) (player.rotationYaw * 4.0F / 360F) + 0.5D) & 3;

        if (l == 0) {
            setTileRotation(world, x, y, z, 2);
        }
        if (l == 1) {
            setTileRotation(world, x, y, z, 5);
        }
        if (l == 2) {
            setTileRotation(world, x, y, z, 3);
        }
        if (l == 3) {
            setTileRotation(world, x, y, z, 4);
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
                    if (((ItemBlock) itemStack.getItem()).field_150939_a instanceof BlockFluidBase || ((ItemBlock) itemStack.getItem()).field_150939_a instanceof BlockStaticLiquid || ((ItemBlock) itemStack.getItem()).field_150939_a instanceof BlockDynamicLiquid) {
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

    public void setTileRotation(World world, int x, int y, int z, int meta) {
        if (world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileMachineBase) {
            ((TileMachineBase) world.getTileEntity(x, y, z)).setRotation(meta);
        }
    }

    public int getTileRotation(World world, int x, int y, int z) {
        if (world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileMachineBase) {
            return ((TileMachineBase) world.getTileEntity(x, y, z)).getRotation();
        }
        return 0;
    }

    public int getTileRotation(IBlockAccess blockAccess, int x, int y, int z) {
        return blockAccess.getTileEntity(x, y, z) != null ? getTileRotation(blockAccess.getTileEntity(x, y, z).getWorldObj(), x, y, z) : 0;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        if (Loader.isModLoaded("IC2")) {
            ItemStack stack = IC2Items.getItem(isAdvanced() ? "advancedMachine" : "machine").copy();
            stack.stackSize = 1;
            items.add(stack);
        } else {
            items.add(isAdvanced() ? new ItemStack(Item.getItemFromBlock(ModBlocks.MachineCasing), 1, 2) : new ItemStack(Item.getItemFromBlock(ModBlocks.MachineCasing), 1, 0));
        }
        System.out.println(items.toString());
        return items;
    }

    public boolean isAdvanced() {
        return false;
    }

    @Override
    public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis) {
        if (axis == ForgeDirection.UNKNOWN) {
            return false;
        } else {
            TileEntity tile = worldObj.getTileEntity(x, y, z);
            if (tile != null && tile instanceof TileMachineBase) {
                TileMachineBase machineBase = (TileMachineBase) tile;
                machineBase.setRotation(ForgeDirection.getOrientation(machineBase.getRotation()).getRotation(axis).ordinal());
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int side, float hitX, float hitY, float hitZ) {
        if(super.onBlockActivated(world, x, y, z, entityplayer, side, hitX, hitY, hitZ)){
            return true;
        }
        return fillBlockWithFluid(world, x, y, z, entityplayer, side, hitX, hitY, hitZ);
    }

    public boolean fillBlockWithFluid(World world, int x, int y, int z, EntityPlayer entityplayer, int side, float hitX, float hitY, float hitZ) {
        ItemStack current = entityplayer.inventory.getCurrentItem();

        if (current != null) {
            TileEntity tile = world.getTileEntity(x, y, z);

            if (tile instanceof IFluidHandler) {
                IFluidHandler tank = (IFluidHandler) tile;
                // Handle FluidContainerRegistry
                if (FluidContainerRegistry.isContainer(current)) {
                    FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(current);
                    // Handle filled containers
                    if (liquid != null) {
                        int qty = tank.fill(ForgeDirection.UNKNOWN, liquid, true);

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
                        FluidStack available = tank.getTankInfo(ForgeDirection.UNKNOWN)[0].fluid;

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

                                tank.drain(ForgeDirection.UNKNOWN, liquid.amount, true);

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
                        FluidStack tankLiquid = tank.getTankInfo(ForgeDirection.UNKNOWN)[0].fluid;
                        boolean mustDrain = liquid == null || liquid.amount == 0;
                        boolean mustFill = tankLiquid == null || tankLiquid.amount == 0;
                        if (mustDrain && mustFill) {
                            // Both are empty, do nothing
                        } else if (mustDrain || !entityplayer.isSneaking()) {
                            liquid = tank.drain(ForgeDirection.UNKNOWN, 1000, false);
                            int qtyToFill = container.fill(current, liquid, true);
                            tank.drain(ForgeDirection.UNKNOWN, qtyToFill, true);
                        } else if (mustFill || entityplayer.isSneaking()) {
                            if (liquid.amount > 0) {
                                int qty = tank.fill(ForgeDirection.UNKNOWN, liquid, false);
                                tank.fill(ForgeDirection.UNKNOWN, container.drain(current, qty, true), true);
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
