package techreborn.tiles.multiblock;

import reborncore.common.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.misc.Location;
import reborncore.common.multiblock.IMultiblockPart;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.tile.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import techreborn.api.Reference;
import techreborn.api.recipe.ITileRecipeHandler;
import techreborn.api.recipe.machines.BlastFurnaceRecipe;
import techreborn.blocks.BlockMachineCasing;
import techreborn.init.ModBlocks;
import techreborn.multiblocks.MultiBlockCasing;
import techreborn.tiles.TileMachineCasing;

public class TileBlastFurnace extends TilePowerAcceptor implements IWrenchable,IInventoryProvider, ISidedInventory, ITileRecipeHandler<BlastFurnaceRecipe>, IRecipeCrafterProvider {

	public static int euTick = 5;
	public int tickTime;
	public Inventory inventory = new Inventory(4, "TileBlastFurnace", 64, this);
	public RecipeCrafter crafter;
	public int capacity = 1000;

	public TileBlastFurnace() {
		// TODO configs
		int[] inputs = new int[2];
		inputs[0] = 0;
		inputs[1] = 1;
		int[] outputs = new int[2];
		outputs[0] = 2;
		outputs[1] = 3;
		crafter = new RecipeCrafter(Reference.blastFurnaceRecipe, this, 2, 2, inventory, inputs, outputs);
	}

	@Override
	public void update() {
		super.update();
		crafter.updateEntity();
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, EnumFacing side) {
		return false;
	}

	@Override
	public EnumFacing getFacing() {
		return getFacingEnum();
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
		return entityPlayer.isSneaking();
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
			TileEntity tileEntity = worldObj.getTileEntity(new BlockPos(getPos().getX() + direction.getFrontOffsetX(),
					getPos().getY() + direction.getFrontOffsetY(), getPos().getZ() + direction.getFrontOffsetZ()));
			if (tileEntity instanceof TileMachineCasing) {
				if (((TileMachineCasing) tileEntity).isConnected()
						&& ((TileMachineCasing) tileEntity).getMultiblockController().isAssembled()) {
					MultiBlockCasing casing = ((TileMachineCasing) tileEntity).getMultiblockController();
					Location location = new Location(getPos().getX(), getPos().getY(), getPos().getZ(), direction);
					location.modifyPositionFromSide(direction, 1);
					int heat = 0;
					if (worldObj.getBlockState(new BlockPos(location.getX(), location.getY() - 1, location.getZ()))
							.getBlock() == tileEntity.getBlockType()) {
						return 0;
					}

					for (IMultiblockPart part : casing.connectedParts) {
						BlockMachineCasing casing1 = (BlockMachineCasing) worldObj.getBlockState(part.getPos())
								.getBlock();
						heat += casing1
								.getHeatFromState(part.getWorld().getBlockState(part.getWorldLocation().toBlockPos()));
						// TODO meta fix
					}

					if (worldObj.getBlockState(new BlockPos(location.getX(), location.getY(), location.getZ()))
							.getBlock().getUnlocalizedName().equals("tile.lava")
							&& worldObj
							.getBlockState(new BlockPos(location.getX(), location.getY() + 1, location.getZ()))
							.getBlock().getUnlocalizedName().equals("tile.lava")) {
						heat += 500;
					}
					return heat;
				}
			}
		}
		return 0;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		worldObj.markBlockRangeForRenderUpdate(getPos().getX(), getPos().getY(), getPos().getZ(), getPos().getX(),
				getPos().getY(), getPos().getZ());
		readFromNBT(packet.getNbtCompound());
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		tickTime = tagCompound.getInteger("tickTime");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		writeUpdateToNBT(tagCompound);
		return tagCompound;
	}

	public void writeUpdateToNBT(NBTTagCompound tagCompound) {
		tagCompound.setInteger("tickTime", tickTime);
	}

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[] {0, 1, 2, 3};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return index == 0 || index == 1;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return index == 2 || index == 3;
    }

	public int getProgressScaled(int scale) {
		if (crafter.currentTickTime != 0) {
			return crafter.currentTickTime * scale / crafter.currentNeededTicks;
		}
		return 0;
	}

	@Override
	public double getMaxPower() {
		return 512000;
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.HIGH;
	}

	@Override
	public boolean canCraft(TileEntity tile, BlastFurnaceRecipe recipe) {
		if (tile instanceof TileBlastFurnace) {
			TileBlastFurnace blastFurnace = (TileBlastFurnace) tile;
			return blastFurnace.getHeat() >= recipe.neededHeat;
		}
		return false;
	}

	@Override
	public boolean onCraft(TileEntity tile, BlastFurnaceRecipe recipe) {
		return true;
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public RecipeCrafter getRecipeCrafter() {
		return crafter;
	}
}
