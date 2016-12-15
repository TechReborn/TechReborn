package techreborn.tiles.generator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
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

public class TileThermalGenerator extends TilePowerAcceptor implements IWrenchable, IInventoryProvider {

	public static final int euTick = ConfigTechReborn.ThermalGeneratorOutput;
	public Tank tank = new Tank("TileThermalGenerator", 1000 * 10, this);
	public Inventory inventory = new Inventory(3, "TileThermalGenerator", 64, this);

	public TileThermalGenerator() {
		super(ConfigTechReborn.ThermalGeneratorTier);
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
		return new ItemStack(ModBlocks.thermalGenerator, 1);
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

	private long lastOutput = 0;

	@Override
	// TODO optimise this code
	public void updateEntity() {
		super.updateEntity();

		if (!this.world.isRemote) {
			if (FluidUtils.drainContainers(this.tank, this.inventory, 0, 1))
				this.syncWithAll();
			for (final EnumFacing direction : EnumFacing.values()) {
				if (this.world
						.getBlockState(new BlockPos(this.getPos().getX() + direction.getFrontOffsetX(),
								this.getPos().getY() + direction.getFrontOffsetY(),
								this.getPos().getZ() + direction.getFrontOffsetZ()))
						.getBlock() == Blocks.LAVA) {
					if(this.tryAddingEnergy(1))
						this.lastOutput = this.world.getTotalWorldTime();
				}
			}
		}

		if (this.tank.getFluidAmount() > 0) {
			if(tryAddingEnergy(euTick))
			{
				this.tank.drain(1, true);
				this.lastOutput = this.world.getTotalWorldTime();
			}
		}

		if (!this.world.isRemote) {
			if (this.world.getTotalWorldTime() - this.lastOutput < 30 && !this.isActive())
				this.world.setBlockState(this.getPos(),
						this.world.getBlockState(this.getPos()).withProperty(BlockMachineBase.ACTIVE, true));
			else if (this.world.getTotalWorldTime() - this.lastOutput > 30 && this.isActive())
				this.world.setBlockState(this.getPos(),
						this.world.getBlockState(this.getPos()).withProperty(BlockMachineBase.ACTIVE, false));
		}

		if (this.tank.getFluidType() != null && this.getStackInSlot(2) == ItemStack.EMPTY) {
			this.inventory.setInventorySlotContents(2, new ItemStack(this.tank.getFluidType().getBlock()));
		} else if (this.tank.getFluidType() == null && this.getStackInSlot(2) != ItemStack.EMPTY) {
			this.inventory.setInventorySlotContents(2, ItemStack.EMPTY);
		}
	}
	
	private boolean tryAddingEnergy(int amount)
	{
		if(this.getMaxPower() - this.getEnergy() >= amount)
		{
			addEnergy(amount);
			return true;
		}
		else if(this.getMaxPower() - this.getEnergy() > 0)
		{
			addEnergy(this.getMaxPower() - this.getEnergy());
			return true;
		}
		return false;
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
		return 128;
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
