package techreborn.blockentity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
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
import techreborn.init.TRContent;
import techreborn.init.TRBlockEntities;
import techreborn.utils.FluidUtils;

import javax.annotation.Nullable;
import java.util.List;

public abstract class TankStorageBaseBlockEntity extends MachineBaseBlockEntity implements InventoryProvider, IToolDrop, IListInfoProvider, IContainerProvider {

	public Tank tank = new Tank("blah", FluidValue.INFINITE, this);
	private RebornInventory<techreborn.blockentity.TankStorageBaseBlockEntity> inventory = new RebornInventory<>(3, "blah", 64, this);

	public TankStorageBaseBlockEntity(){
		this(TRBlockEntities.DIGITAL_TANK);
	}

	public TankStorageBaseBlockEntity(BlockEntityType<?> blockEntityTypeIn) {
		super(blockEntityTypeIn);
	}

	public void readWithoutCoords(final CompoundTag tagCompound) {
		tank.read(tagCompound);
	}

	public CompoundTag writeWithoutCoords(final CompoundTag tagCompound) {
		tank.write(tagCompound);
		return tagCompound;
	}

	public ItemStack getDropWithNBT(ItemStack dropStack) {
		final CompoundTag blockEntity = new CompoundTag();
		this.writeWithoutCoords(blockEntity);
		dropStack.setTag(new CompoundTag());
		dropStack.getTag().put("blockEntity", blockEntity);
		return dropStack;
	}

	// TileMachineBase
	@Override
	public void tick() {
		super.tick();
		if (!world.isClient) {
			if (FluidUtils.drainContainers(tank, inventory, 0, 1)
					|| FluidUtils.fillContainers(tank, inventory, 0, 1, tank.getFluid())) {
				this.syncWithAll();
			}

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
	public RebornInventory<techreborn.blockentity.TankStorageBaseBlockEntity> getInventory() {
		return this.inventory;
	}

	// IListInfoProvider
	@Override
	public void addInfo(final List<Text> info, final boolean isReal, boolean hasData) {
		if (isReal | hasData) {
			if (!this.tank.getFluidInstance().isEmpty()) {
				info.add(new LiteralText(this.tank.getFluidAmount() + " of " + this.tank.getFluid()));
			} else {
				info.add(new LiteralText("Empty"));
			}
		}
		info.add(new LiteralText("Capacity " + this.tank.getCapacity()));
	}

	// IContainerProvider
	@Override
	public BuiltContainer createContainer(int syncID, final PlayerEntity player) {
		return new ContainerBuilder("tank").player(player.inventory).inventory().hotbar()
				.addInventory().blockEntity(this).fluidSlot(0, 80, 17).outputSlot(1, 80, 53)
				.sync(tank).addInventory().create(this, syncID);
	}

	@Nullable
	@Override
	public Tank getTank() {
		return tank;
	}
}
