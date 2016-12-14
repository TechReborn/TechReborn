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

public class TileSemifluidGenerator extends TilePowerAcceptor implements IWrenchable, IInventoryProvider {

	// TODO: run this off config
	public static final int euTick = 8;
	public Tank tank = new Tank("TileSemifluidGenerator", 1000 * 10, this);
	public Inventory inventory = new Inventory(3, "TileSemifluidGenerator", 64, this);
	Map<String, Integer> fluids = new HashMap<>();

	// We use this to keep track of fractional millibuckets, allowing us to hit
	// our eu/bucket targets while still only ever removing integer millibucket
	// amounts.
	double pendingWithdraw = 0.0;

	public TileSemifluidGenerator() {
		super(ConfigTechReborn.ThermalGeneratorTier);
		// TODO: fix this to have SemiFluid generator values

		fluids.put("creosote", 3000);
		fluids.put("biomass", 8000);
		fluids.put("fluidoil", 64000);
		fluids.put("fluidsodium", 30000);
		fluids.put("fluidlithium", 60000);
		fluids.put("biofuel", 32000);
		fluids.put("bioethanol", 32000);
		fluids.put("fuel", 128000);
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
		return new ItemStack(ModBlocks.semiFluidGenerator, 1);
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
		if (!world.isRemote)
		{
			if(FluidUtils.drainContainers(tank, inventory, 0, 1))
				this.syncWithAll();
		}

		if (tank.getFluidAmount() > 0 && getMaxPower() - getEnergy() >= euTick && tank.getFluidType() != null && fluids.containsKey(tank.getFluidType().getName())) {
			Integer euPerBucket = fluids.get(tank.getFluidType().getName());
			// float totalTicks = (float)euPerBucket / 8f; //x eu per bucket / 8
			// eu per tick
			// float millibucketsPerTick = 1000f / totalTicks;
			float millibucketsPerTick = 8000f / (float) euPerBucket;
			pendingWithdraw += millibucketsPerTick;

			int currentWithdraw = (int) pendingWithdraw; // float --> int
			// conversion floors
			// the float
			pendingWithdraw -= currentWithdraw;

			tank.drain(currentWithdraw, true);
			addEnergy(euTick);
			if(!this.isActive())
				this.world.setBlockState(this.getPos(),
						this.world.getBlockState(this.getPos()).withProperty(BlockMachineBase.ACTIVE, true));
		}
		else if(this.isActive())
			this.world.setBlockState(this.getPos(),
					this.world.getBlockState(this.getPos()).withProperty(BlockMachineBase.ACTIVE, false));
		if (tank.getFluidType() != null && getStackInSlot(2) == ItemStack.EMPTY) {
			inventory.setInventorySlotContents(2, new ItemStack(tank.getFluidType().getBlock()));
		} else if (tank.getFluidType() == null && getStackInSlot(2) != ItemStack.EMPTY) {
			setInventorySlotContents(2, ItemStack.EMPTY);
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
		return EnumPowerTier.LOW;
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}
}
