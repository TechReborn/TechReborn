package techreborn.tiles.generator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.IWrenchable;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.FluidUtils;
import reborncore.common.util.Inventory;
import reborncore.common.util.Tank;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;

import java.util.HashMap;
import java.util.Map;

public class TileGasTurbine extends TilePowerAcceptor implements IWrenchable, IInventoryProvider {

	// TODO: run this off config
	public static final int euTick = 16;
	public Tank tank = new Tank("TileGasTurbine", 1000 * 10, this);
	public Inventory inventory = new Inventory(3, "TileGasTurbine", 64, this);
	Map<String, Integer> fluids = new HashMap<>();

	// We use this to keep track of fractional millibuckets, allowing us to hit
	// our eu/bucket targets while still only ever removing integer millibucket
	// amounts.
	double pendingWithdraw = 0.0;

	public TileGasTurbine() {
		super(ConfigTechReborn.ThermalGeneratorTier);
		// TODO: fix this to have Gas Turbine generator values

		fluids.put("fluidhydrogen", 15000);
		fluids.put("fluidmethane", 45000);
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
		return new ItemStack(ModBlocks.gasTurbine, 1);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (T) tank;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		tank.readFromNBT(tagCompound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tank.writeToNBT(tagCompound);
		return tagCompound;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		world.markBlockRangeForRenderUpdate(getPos().getX(), getPos().getY(), getPos().getZ(), getPos().getX(),
			getPos().getY(), getPos().getZ());
		readFromNBT(packet.getNbtCompound());
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (!this.world.isRemote) {
			if (FluidUtils.drainContainers(this.tank, this.inventory, 0, 1))
				this.syncWithAll();
		}

		if (this.tank.getFluidAmount() > 0 && this.getMaxPower() - this.getEnergy() >= TileGasTurbine.euTick) {
			final Integer euPerBucket = this.fluids.get(this.tank.getFluidType().getName());
			// float totalTicks = (float)euPerBucket / 8f; //x eu per bucket / 8
			// eu per tick
			// float millibucketsPerTick = 1000f / totalTicks;
			final float millibucketsPerTick = 16000f / (float) euPerBucket;
			this.pendingWithdraw += millibucketsPerTick;

			final int currentWithdraw = (int) this.pendingWithdraw; // float -->
																	// int
			// conversion floors
			// the float
			this.pendingWithdraw -= currentWithdraw;

			this.tank.drain(currentWithdraw, true);
			this.addEnergy(TileGasTurbine.euTick);

			if (!this.isActive())
				this.world.setBlockState(this.getPos(),
						this.world.getBlockState(this.getPos()).withProperty(BlockMachineBase.ACTIVE, true));
		} else if (this.isActive())
			this.world.setBlockState(this.getPos(),
					this.world.getBlockState(this.getPos()).withProperty(BlockMachineBase.ACTIVE, false));
		if (this.tank.getFluidType() != null && this.getStackInSlot(2) == ItemStack.EMPTY) {
			this.inventory.setInventorySlotContents(2, new ItemStack(this.tank.getFluidType().getBlock()));
		} else if (this.tank.getFluidType() == null && this.getStackInSlot(2) != ItemStack.EMPTY) {
			this.setInventorySlotContents(2, ItemStack.EMPTY);
		}
	}

	@Override
	public double getMaxPower() {
		return ConfigTechReborn.ThermalGeneratorCharge;
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing direction) {
		return false;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing direction) {
		return true;
	}

	@Override
	public double getMaxOutput() {
		return euTick;
	}

	@Override
	public double getMaxInput() {
		return 0;
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.MEDIUM;
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}
}
