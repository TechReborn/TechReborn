package techreborn.blockentity.storage.fluid;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import reborncore.api.IListInfoProvider;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blockentity.SlotConfiguration;
import reborncore.common.fluid.FluidValue;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.Tank;
import techreborn.utils.FluidUtils;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class TankUnitBaseBlockEntity extends MachineBaseBlockEntity implements InventoryProvider, IToolDrop, IListInfoProvider, IContainerProvider {
	protected Tank tank;
	private RebornInventory<TankUnitBaseBlockEntity> inventory = new RebornInventory<>(3, "TankInventory", 64, this);

	public TankUnitBaseBlockEntity(BlockEntityType<?> blockEntityTypeIn, FluidValue value) {
		super(blockEntityTypeIn);
		this.tank = new Tank("TankStorage", value, this);
	}

	// Quantum tank save compat
	public TankUnitBaseBlockEntity(BlockEntityType<?> blockEntityTypeIn) {
		super(blockEntityTypeIn);
	}

	public void readWithoutCoords(final CompoundTag tagCompound) {
		tank.read(tagCompound);
	}

	public CompoundTag writeWithoutCoords(final CompoundTag tagCompound) {
		tank.write(tagCompound);
		return tagCompound;
	}

	public ItemStack getDropWithNBT() {
		ItemStack dropStack = new ItemStack(getBlockType(), 1);
		final CompoundTag blockEntity = new CompoundTag();
		this.writeWithoutCoords(blockEntity);
		dropStack.setTag(new CompoundTag());
		dropStack.getTag().put("blockEntity", blockEntity);
		return dropStack;
	}



	@Override
	public void tick() {
		super.tick();

		if (world.isClient()){
			return;
		}

		if (FluidUtils.drainContainers(tank, inventory, 0, 1)
					|| FluidUtils.fillContainers(tank, inventory, 0, 1, tank.getFluid())) {
			this.syncWithAll();
		}
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	@Override
	public void fromTag(final CompoundTag tagCompound) {
		super.fromTag(tagCompound);
		readWithoutCoords(tagCompound);
	}

	@Override
	public CompoundTag toTag(final CompoundTag tagCompound) {
		super.toTag(tagCompound);
		writeWithoutCoords(tagCompound);
		return tagCompound;
	}

	// ItemHandlerProvider
	@Override
	public RebornInventory<TankUnitBaseBlockEntity> getInventory() {
		return this.inventory;
	}

	// IListInfoProvider
	@Override
	public void addInfo(final List<Text> info, final boolean isReal, boolean hasData) {
		if (isReal || hasData) {
			if (!this.tank.getFluidInstance().isEmpty()) {
				info.add(new LiteralText(this.tank.getFluidAmount() + " of " + this.tank.getFluid()));
			} else {
				info.add(new LiteralText("Empty"));
			}
		}
		info.add(new LiteralText(Formatting.GRAY + "Capacity: " + Formatting.GOLD + this.tank.getCapacity()));
	}

	// IContainerProvider
	@Override
	public BuiltContainer createContainer(int syncID, final PlayerEntity player) {
		return new ContainerBuilder("tank").player(player.inventory).inventory().hotbar()
				.addInventory().blockEntity(this).fluidSlot(0, 100, 53).outputSlot(1, 140, 53)
				.sync(tank).addInventory().create(this, syncID);
	}

	@Nonnull
	@Override
	public Tank getTank() {
		return tank;
	}

	@Override
	public ItemStack getToolDrop(PlayerEntity playerEntity) {
		return getDropWithNBT();
	}
}
