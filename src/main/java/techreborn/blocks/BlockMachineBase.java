package techreborn.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
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
import reborncore.api.IBlockTextureProvider;
import reborncore.common.BaseBlock;
import reborncore.common.BaseTileBlock;
import techreborn.client.TechRebornCreativeTab;
import techreborn.init.ModBlocks;
import techreborn.tiles.TileMachineBase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public abstract class BlockMachineBase extends BaseTileBlock implements IBlockTextureProvider {

    public static PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static PropertyBool ACTIVE = PropertyBool.create("active");

    public BlockMachineBase(Material material) {
        super(Material.rock);
        setCreativeTab(TechRebornCreativeTab.instance);
        setHardness(2f);
        setStepSound(soundTypeMetal);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ACTIVE, false));
    }

    protected BlockState createBlockState() {

        FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
        ACTIVE = PropertyBool.create("active");
        return new BlockState(this, FACING, ACTIVE);
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
        this.setDefaultFacing(worldIn, pos, state);
    }

    private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote)
        {
            Block block = worldIn.getBlockState(pos.north()).getBlock();
            Block block1 = worldIn.getBlockState(pos.south()).getBlock();
            Block block2 = worldIn.getBlockState(pos.west()).getBlock();
            Block block3 = worldIn.getBlockState(pos.east()).getBlock();
            EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);

            if (enumfacing == EnumFacing.NORTH && block.isFullBlock() && !block1.isFullBlock())
            {
                enumfacing = EnumFacing.SOUTH;
            }
            else if (enumfacing == EnumFacing.SOUTH && block1.isFullBlock() && !block.isFullBlock())
            {
                enumfacing = EnumFacing.NORTH;
            }
            else if (enumfacing == EnumFacing.WEST && block2.isFullBlock() && !block3.isFullBlock())
            {
                enumfacing = EnumFacing.EAST;
            }
            else if (enumfacing == EnumFacing.EAST && block3.isFullBlock() && !block2.isFullBlock())
            {
                enumfacing = EnumFacing.WEST;
            }

            worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
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
        setFacing(placer.getHorizontalFacing().getOpposite(), worldIn, pos);
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
                //TODO fix
             //   world.setBlockState(pos, world.getBlockState(pos).withProperty(FACING, EnumFacing.getFront(world.getBlockState(pos).getValue(FACING)).getOpposite()));
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

    @Override
    public String getTextureName(IBlockState blockState, EnumFacing facing) {
        if(this instanceof IRotationTexture){
            IRotationTexture rotationTexture = (IRotationTexture) this;
            if(getFacing(blockState) == facing){
                return isActive(blockState) ? rotationTexture.getFrontOn() : rotationTexture.getFrontOff();
            }
            if(facing == EnumFacing.UP){
                return rotationTexture.getTop();
            }
            if(facing == EnumFacing.DOWN){
                return rotationTexture.getBottom();
            }
            return rotationTexture.getSide();
        }
        if(this instanceof IAdvancedRotationTexture){
            IAdvancedRotationTexture advancedRotationTexture = (IAdvancedRotationTexture) this;
            if(getFacing(blockState) == facing){
                return advancedRotationTexture.getFront(isActive(blockState));
            }
            if(facing == EnumFacing.UP){
                return advancedRotationTexture.getTop(isActive(blockState));
            }
            if(facing == EnumFacing.DOWN){
                return advancedRotationTexture.getBottom(isActive(blockState));
            }
            return advancedRotationTexture.getSide(isActive(blockState));
        }
        return "techreborn:blocks/machine/machine_side";
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean active = false;
        if(meta > 6){
            active = true;
            meta =- 6;
        }
        int facingInt = meta;
        EnumFacing facing = EnumFacing.getFront(facingInt);
        if(facing == EnumFacing.DOWN || facing == EnumFacing.UP){
            facing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(FACING, facing).withProperty(ACTIVE, active);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int facingInt = state.getValue(FACING).getIndex();
        int activeInt = state.getValue(ACTIVE) ? 0 : 6;
        return facingInt + activeInt;
    }

    public boolean isActive(IBlockState state){
        return state.getValue(ACTIVE);
    }

    public EnumFacing getFacing(IBlockState state){
        return state.getValue(FACING);
    }

    public void setFacing(EnumFacing facing, World world, BlockPos pos){
        world.setBlockState(pos, world.getBlockState(pos).withProperty(FACING, facing));
    }


    public void setActive(Boolean active, World world, BlockPos pos){
        world.setBlockState(pos, world.getBlockState(pos).withProperty(ACTIVE, active));
    }
    @Override
    public int amoutOfVariants() {
        return 12; //six for Facing, and then 2 for eatch of them for if active
    }
}
