package techreborn.blockentity.storage.energy;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reborncore.client.screen.BuiltScreenHandlerProvider;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.client.screen.builder.ScreenHandlerBuilder;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blockentity.MultiblockWriter;
import reborncore.common.powerSystem.RcEnergyTier;
import techreborn.blockentity.machine.GenericMachineBlockEntity;
import techreborn.init.ModFluids;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;
import techreborn.init.TRContent.Machine;

public class MoltenSaltBatteryBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider {

	private static final int E_PER_CELL = 10000000;

	private int radius = 1;
	private int layers = 1;
	private int cells = calculateCells(1, 1);

	private boolean isFormed = false;

	public MoltenSaltBatteryBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.MOLTEN_SALT_BATTERY, pos, state, "MoltenSaltBattery",
			  RcEnergyTier.LOW.getMaxInput(), -1, Machine.MOLTEN_SALT_BATTERY.block, -1);
	}

	@Override
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		NbtCompound data = tag.getCompound("MoltenSaltBattery");
		radius = Math.max(1, data.getInt("radius"));
		layers = Math.max(1, data.getInt("layers"));
		cells = calculateCells(radius, layers);
	}

	@Override
	public NbtCompound writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		NbtCompound data = new NbtCompound();
		data.putInt("radius", radius);
		data.putInt("layers", layers);
		tag.put("MoltenSaltBattery", data);
		return tag;
	}

	@Override
	public void writeMultiblock(MultiblockWriter writer) {
		BlockState casing = TRContent.MachineBlocks.BASIC.getCasing().getDefaultState();
		writer = writer.translate(radius+1, 0, 0).cylinder(radius, casing, casing);
		for (int i = 0; i < layers; i++) {
			writer = writer.translate(0, 1, 0).cylinder(radius, casing, TRContent.SULFUR_BLOCK.getDefaultState())
				  .translate(0, 1, 0).cylinder(radius, casing, ModFluids.SODIUM.getBlock().getDefaultState());
		}
		writer.translate(0, 1, 0).cylinder(radius, casing, casing);
	}

	@Override
	public BuiltScreenHandler createScreenHandler(int syncID,
		  PlayerEntity player) {
		return new ScreenHandlerBuilder("molten_salt_battery")
			  .player(player.getInventory()).inventory().hotbar()
			  .addInventory().blockEntity(this)
			  .syncEnergyValue()
			  .sync(this::getCells, this::setCells)
			  .sync(this::getRadius, this::setRadius)
			  .sync(this::getLayers, this::setLayers)
			  .sync(this::isFormed, this::setIsFormed)
			  .addInventory().create(this, syncID);
	}

	@Override
	public long getBaseMaxPower() {
		if (isFormed) {
			return getEstimatedCapacity();
		} else {
			return 0;
		}
	}

	@Override
	public long getBaseMaxOutput() {
		if (isFormed) {
			return RcEnergyTier.LOW.getMaxOutput();
		} else {
			return 0;
		}
	}

	@Override
	public boolean canBeUpgraded() { return false; }

	@Override
	public boolean canProvideEnergy(@Nullable Direction side) {
		return (isFormed && (side == Direction.DOWN || side == Direction.UP));
	}

	@Override
	public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);
		if (world == null || world.isClient) {
			return;
		}

		// Every 250ms, check if multiblock is formed
		if (ticktime % 5 == 0) {
			isFormed = isMultiblockValid();
		}
	}

	private int calculateCells(int radius, int layers) {
		int cells = 0;
		double rSquared = Math.pow(radius, 2);
		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				double dist = (Math.pow(x, 2) + Math.pow(z, 2));
				double diff = Math.abs(rSquared - dist);
				if (diff >= radius && dist < rSquared) {
					cells++;
				}
			}
		}
		return cells * layers;
	}

	public void changeDimensions(int radiusDelta, int layersDelta) {
		// Limit dimensions such that we will not overflow a long in energy storage
		int newCells = calculateCells(this.radius + radiusDelta, this.layers + layersDelta);
		if (newCells > 0 && newCells <= 200) {
			this.radius = Math.max(1, this.radius + radiusDelta);
			this.layers = Math.max(1, this.layers + layersDelta);
			this.cells = newCells;
		}
	}

	public long getEstimatedCapacity() {
		return cells * E_PER_CELL;
	}

	public boolean isFormed() { return isFormed; }
	public void setIsFormed(boolean isFormed) { this.isFormed = isFormed; }

	public int getCells() { return cells; }
	public void setCells(int cells) { this.cells = cells; }

	public int getRadius() { return radius; }
	public void setRadius(int radius) { this.radius = radius; }

	public int getLayers() { return layers; }
	public void setLayers(int layers) { this.layers = layers; }
}
