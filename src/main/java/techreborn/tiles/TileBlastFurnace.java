package techreborn.tiles;

import ic2.api.tile.IWrenchable;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import reborncore.common.BaseTileBlock;
import reborncore.common.misc.Location;
import reborncore.common.multiblock.IMultiblockPart;
import reborncore.common.util.Inventory;
import techreborn.api.recipe.RecipeCrafter;
import techreborn.blocks.BlockMachineBase;
import techreborn.blocks.BlockMachineCasing;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.lib.Reference;
import techreborn.multiblocks.MultiBlockCasing;
import techreborn.powerSystem.TilePowerAcceptor;

public class TileBlastFurnace extends TilePowerAcceptor implements IWrenchable, IInventory, ISidedInventory {

    public int tickTime;
    public Inventory inventory = new Inventory(4, "TileBlastFurnace", 64, this);
    public RecipeCrafter crafter;
    public int capacity = 1000;
    public static int euTick = 5;

    public TileBlastFurnace() {
        super(ConfigTechReborn.CentrifugeTier);
        //TODO configs
        int[] inputs = new int[2];
        inputs[0] = 0;
        inputs[1] = 1;
        int[] outputs = new int[2];
        outputs[0] = 2;
        outputs[1] = 3;
        crafter = new RecipeCrafter(Reference.blastFurnaceRecipe, this, 2, 2, inventory, inputs, outputs);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        crafter.updateEntity();
    }

    @Override
    public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side) {
        return false;
    }

    @Override
    public short getFacing() {
        Block block = worldObj.getBlockState(pos).getBlock();
        if(block instanceof BlockMachineBase){
            return (short) ((BlockMachineBase) block).getFacing(worldObj.getBlockState(pos)).getIndex();
        }
        return 0;
    }

    @Override
    public void setFacing(short facing) {
    }

    @Override
    public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
        if (entityPlayer.isSneaking()) {
            return true;
        }
        return false;
    }

    @Override
    public float getWrenchDropRate() {
        return 1.0F;
    }

    @Override
    public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
        return new ItemStack(ModBlocks.BlastFurnace, 1);
    }

    public int getHeat() {
        for (EnumFacing direction : EnumFacing.values()) {
            TileEntity tileEntity = worldObj.getTileEntity(new BlockPos(getPos().getX() + direction.getFrontOffsetX(), getPos().getY() + direction.getFrontOffsetY(), getPos().getZ() + direction.getFrontOffsetZ()));
            if (tileEntity instanceof TileMachineCasing) {
                if (((TileMachineCasing) tileEntity).isConnected() && ((TileMachineCasing) tileEntity).getMultiblockController().isAssembled()) {
                    MultiBlockCasing casing = ((TileMachineCasing) tileEntity).getMultiblockController();
                    Location location = new Location(getPos().getX(), getPos().getY(), getPos().getZ(), direction);
                    location.modifyPositionFromSide(direction, 1);
                    int heat = 0;
                    if (worldObj.getBlockState(new BlockPos(location.getX(), location.getY() - 1, location.getZ())).getBlock() == tileEntity.getBlockType()) {
                        return 0;
                    }

                    for (IMultiblockPart part : casing.connectedParts) {
//                        heat += BlockMachineCasing.getHeatFromMeta(part.getWorld().getBlockMetadata(part.getWorldLocation().x, part.getWorldLocation().y, part.getWorldLocation().z));
                        //TODO meta fix
                    }

                    if (worldObj.getBlockState(new BlockPos(location.getX(), location.getY(), location.getZ())).getBlock().getUnlocalizedName().equals("tile.lava") && worldObj.getBlockState(new BlockPos(location.getX(), location.getY() + 1, location.getZ())).getBlock().getUnlocalizedName().equals("tile.lava")) {
                        heat += 500;
                    }
                    return heat;
                }
            }
        }
        return 0;
    }

    @Override
    public int getSizeInventory() {
        return inventory.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int p_70301_1_) {
        return inventory.getStackInSlot(p_70301_1_);
    }

    @Override
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
        return inventory.decrStackSize(p_70298_1_, p_70298_2_);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        return inventory.getStackInSlotOnClosing(p_70304_1_);
    }

    @Override
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
        inventory.setInventorySlotContents(p_70299_1_, p_70299_2_);
    }


    @Override
    public int getInventoryStackLimit() {
        return inventory.getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return inventory.isUseableByPlayer(p_70300_1_);
    }

    @Override
    public void openInventory(EntityPlayer player) {
        inventory.openInventory(player);
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        inventory.closeInventory(player);
    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return inventory.isItemValidForSlot(p_94041_1_, p_94041_2_);
    }

    @Override
    public int getField(int id) {
        return inventory.getField(id);
    }

    @Override
    public void setField(int id, int value) {
        inventory.setField(id, value);
    }

    @Override
    public int getFieldCount() {
        return inventory.getFieldCount();
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        writeToNBT(nbtTag);
        return new S35PacketUpdateTileEntity(this.getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net,
                             S35PacketUpdateTileEntity packet) {
        worldObj.markBlockRangeForRenderUpdate(getPos().getX(), getPos().getY(), getPos().getZ(), getPos().getX(),
                getPos().getY(), getPos().getZ());
        readFromNBT(packet.getNbtCompound());
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        inventory.readFromNBT(tagCompound);
        tickTime = tagCompound.getInteger("tickTime");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        inventory.writeToNBT(tagCompound);
        writeUpdateToNBT(tagCompound);
    }

    public void writeUpdateToNBT(NBTTagCompound tagCompound) {
        tagCompound.setInteger("tickTime", tickTime);
    }

    // ISidedInventory
    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return side == EnumFacing.DOWN ? new int[]{0, 1, 2, 3} : new int[]{0, 1, 2, 3};
    }

    @Override
    public boolean canInsertItem(int slotIndex, ItemStack itemStack, EnumFacing side) {
        if (slotIndex >= 1)
            return false;
        return isItemValidForSlot(slotIndex, itemStack);
    }

    @Override
    public boolean canExtractItem(int slotIndex, ItemStack itemStack, EnumFacing side) {
        return slotIndex == 2 || slotIndex == 3;
    }

    public int getProgressScaled(int scale) {
        if (crafter.currentTickTime != 0) {
            return crafter.currentTickTime * scale / crafter.currentNeededTicks;
        }
        return 0;
    }

    @Override
    public double getMaxPower() {
        return 10000;
    }

    @Override
    public boolean canAcceptEnergy(EnumFacing direction) {
        return true;
    }

    @Override
    public boolean canProvideEnergy(EnumFacing direction) {
        return false;
    }

    @Override
    public double getMaxOutput() {
        return 0;
    }

    @Override
    public double getMaxInput() {
        return 128;
    }

    @Override
    public String getCommandSenderName() {
        return inventory.getCommandSenderName();
    }

    @Override
    public boolean hasCustomName() {
        return inventory.hasCustomName();
    }

    @Override
    public IChatComponent getDisplayName() {
        return inventory.getDisplayName();
    }
}
