package techreborn.tiles;

import ic2.api.tile.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import reborncore.common.misc.Location;
import reborncore.common.multiblock.IMultiblockPart;
import reborncore.common.util.Inventory;
import techreborn.api.recipe.RecipeCrafter;
import techreborn.blocks.BlockMachineCasing;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.lib.Reference;
import techreborn.multiblocks.MultiBlockCasing;
import techreborn.powerSystem.TilePowerAcceptor;

public class TileBlastFurnace extends TilePowerAcceptor implements IWrenchable, IInventory, ISidedInventory {

    public int tickTime;
    public Inventory inventory = new Inventory(4, "TileBlastFurnace", 64);
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
        for (EnumFacing direction : EnumFacing.VALID_DIRECTIONS) {
            TileEntity tileEntity = worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
            if (tileEntity instanceof TileMachineCasing) {
                if (((TileMachineCasing) tileEntity).isConnected() && ((TileMachineCasing) tileEntity).getMultiblockController().isAssembled()) {
                    MultiBlockCasing casing = ((TileMachineCasing) tileEntity).getMultiblockController();
                    Location location = new Location(xCoord, yCoord, zCoord, direction);
                    location.modifyPositionFromSide(direction, 1);
                    int heat = 0;
                    if (worldObj.getBlock(location.getX(), location.getY() - 1, location.getZ()) == tileEntity.getBlockType()) {
                        return 0;
                    }

                    for (IMultiblockPart part : casing.connectedParts) {
                        heat += BlockMachineCasing.getHeatFromMeta(part.getWorldObj().getBlockMetadata(part.getWorldLocation().x, part.getWorldLocation().y, part.getWorldLocation().z));
                    }

                    if (worldObj.getBlock(location.getX(), location.getY(), location.getZ()).getUnlocalizedName().equals("tile.lava") && worldObj.getBlock(location.getX(), location.getY() + 1, location.getZ()).getUnlocalizedName().equals("tile.lava")) {
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
    public String getInventoryName() {
        return inventory.getInventoryName();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return inventory.hasCustomInventoryName();
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
    public void openInventory() {
        inventory.openInventory();
    }

    @Override
    public void closeInventory() {
        inventory.closeInventory();
    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return inventory.isItemValidForSlot(p_94041_1_, p_94041_2_);
    }

    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        writeToNBT(nbtTag);
        return new S35PacketUpdateTileEntity(this.getPos().getX(), this.getPos().getY(),
                this.getPos().getZ(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net,
                             S35PacketUpdateTileEntity packet) {
        worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord,
                yCoord, zCoord);
        readFromNBT(packet.func_148857_g());
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
    public int[] getAccessibleSlotsFromSide(int side) {
        return side == EnumFacing.DOWN.ordinal() ? new int[]{0, 1, 2, 3} : new int[]{0, 1, 2, 3};
    }

    @Override
    public boolean canInsertItem(int slotIndex, ItemStack itemStack, int side) {
        if (slotIndex >= 1)
            return false;
        return isItemValidForSlot(slotIndex, itemStack);
    }

    @Override
    public boolean canExtractItem(int slotIndex, ItemStack itemStack, int side) {
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
}
