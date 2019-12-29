package techreborn.blockentity.storage.fluid;

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
import reborncore.common.fluid.FluidValue;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.Tank;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;
import techreborn.utils.FluidUtils;

import javax.annotation.Nonnull;
import java.util.List;

public class TankUnitBaseBlockEntity extends MachineBaseBlockEntity implements InventoryProvider, IToolDrop, IListInfoProvider, IContainerProvider {
	protected Tank tank;
	private RebornInventory<TankUnitBaseBlockEntity> inventory = new RebornInventory<>(2, "TankInventory", 64, this);

	private TRContent.TankUnit type;

	public TankUnitBaseBlockEntity() {
		super(TRBlockEntities.TANK_UNIT);
	}

	public TankUnitBaseBlockEntity(TRContent.TankUnit type) {
		super(TRBlockEntities.TANK_UNIT);
		configureEntity(type);
	}

	private void configureEntity(TRContent.TankUnit type){
		this.type = type;

		this.tank = new Tank("TankStorage", type.capacity, this);
	}

	@Override
	public void tick() {
		super.tick();

		if (world.isClient()) {
			return;
		}

		if (FluidUtils.drainContainers(tank, inventory, 0, 1)
				|| FluidUtils.fillContainers(tank, inventory, 0, 1, tank.getFluid())) {

			if(type == TRContent.TankUnit.CREATIVE){
				if (!tank.isEmpty() && !tank.isFull()) {
					tank.setFluidAmount(FluidValue.INFINITE);
				}
			}
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
		this.type = TRContent.TankUnit.valueOf(tagCompound.getString("unitType"));
		configureEntity(type);
		tank.read(tagCompound);
	}

	@Override
	public CompoundTag toTag(final CompoundTag tagCompound) {
		super.toTag(tagCompound);
		tagCompound.putString("unitType", this.type.name());
		tank.write(tagCompound);
		return tagCompound;
	}

	public ItemStack getDropWithNBT() {
		ItemStack dropStack = new ItemStack(getBlockType(), 1);
		final CompoundTag blockEntity = new CompoundTag();
		this.toTag(blockEntity);
		dropStack.setTag(new CompoundTag());
		dropStack.getTag().put("blockEntity", blockEntity);
		return dropStack;
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
