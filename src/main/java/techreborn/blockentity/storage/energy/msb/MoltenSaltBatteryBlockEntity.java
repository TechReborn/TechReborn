package techreborn.blockentity.storage.energy.msb;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiPredicate;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reborncore.client.screen.BuiltScreenHandlerProvider;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.client.screen.builder.ScreenHandlerBuilder;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blockentity.MultiblockWriter;
import reborncore.common.powerSystem.RcEnergyTier;
import team.reborn.energy.api.EnergyStorage;
import techreborn.blockentity.machine.GenericMachineBlockEntity;
import techreborn.blocks.storage.energy.msb.MoltenSaltPortBlock;
import techreborn.init.ModFluids;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;
import techreborn.init.TRContent.Machine;
import techreborn.init.TRContent.MoltenSaltPorts;

public class MoltenSaltBatteryBlockEntity extends MachineBaseBlockEntity implements BuiltScreenHandlerProvider {

	private final BatteryEnergyStore energyStore = new BatteryEnergyStore() {
		@Override
		protected void onFinalCommit() {
			MoltenSaltBatteryBlockEntity.this.markDirty();
		}
	};

	private final EnergyStorage localInserter = energyStore.newView(RcEnergyTier.LOW.getMaxInput(), 0);
	private final EnergyStorage localExtractor = energyStore.newView(0, RcEnergyTier.LOW.getMaxOutput());

	private static final long E_PER_CELL = 10000000;

	private int radius;
	private int layers;
	private int cells;

	private boolean isFormed = false;

	private Set<BlockPos> ports = new HashSet<>();

	public MoltenSaltBatteryBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.MOLTEN_SALT_BATTERY, pos, state);
		updateCapacity(1, 1);
	}

	@Override
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		NbtCompound data = tag.getCompound("MoltenSaltBattery");
		updateCapacity(
			  Math.max(1, data.getInt("radius")),
			  Math.max(1, data.getInt("layers")));
		energyStore.setAmount(Math.max(0, data.getLong("energy")));
	}

	@Override
	public NbtCompound writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		NbtCompound data = new NbtCompound();
		data.putInt("radius", radius);
		data.putInt("layers", layers);
		data.putLong("energy", energyStore.getAmount());
		tag.put("MoltenSaltBattery", data);
		return tag;
	}

	@Override
	public void writeMultiblock(MultiblockWriter writer) {
		BlockState casing = TRContent.MachineBlocks.BASIC.getCasing().getDefaultState();

		BiPredicate<BlockView, BlockPos> casingOrPort = (view, pos) -> {
			BlockState s = view.getBlockState(pos);
			if (MoltenSaltPorts.BLOCKS.containsKey(s.getBlock())) {
				ports.add(pos);
				return true;
			}

			return s == casing;
		};

		writer = writer.translate(radius+1, 0, 0).cylinderSolid(radius, casingOrPort, casing);
		for (int i = 0; i < layers; i++) {
			writer = writer.translate(0, 1, 0).cylinder(radius, casingOrPort, casing, TRContent.SULFUR_BLOCK.getDefaultState())
				  .translate(0, 1, 0).cylinder(radius, casingOrPort, casing, ModFluids.SODIUM.getBlock().getDefaultState());
		}
		writer.translate(0, 1, 0).cylinderSolid(radius, casingOrPort, casing);
	}

	@Override
	public BuiltScreenHandler createScreenHandler(int syncID,
		  PlayerEntity player) {
		return new ScreenHandlerBuilder("molten_salt_battery")
			  .player(player.getInventory()).inventory().hotbar()
			  .addInventory().blockEntity(this)
			  .sync(this::getCells, this::setCells)
			  .sync(this::getRadius, this::setRadius)
			  .sync(this::getLayers, this::setLayers)
			  .sync(this::isFormed, this::setIsFormed)
			  .sync(this.energyStore::getAmount, this.energyStore::setAmount)
			  .sync(this.energyStore::getCapacity, this.energyStore::setCapacity)
			  .addInventory().create(this, syncID);
	}

	public EnergyStorage getSideEnergyStorage(Direction direction) {
		if (direction == Direction.UP || direction == Direction.DOWN) {
			return localExtractor;
		} else {
			return localInserter;
		}
	}

	@Override
	public boolean canBeUpgraded() { return false; }

	@Override
	public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);
		if (world == null || world.isClient) {
			return;
		}

		// Every 250ms, check if multiblock is formed
		if (ticktime % 5 == 0) {
			ports.clear();
			boolean isFormedPending = isMultiblockValid();
			if (isFormedPending != isFormed) {
				// Change of formation state
				if (isFormedPending) {
					// Not formed -> formed; register all ports
					for (BlockPos port : ports) {
						BlockEntity portEntity = world.getBlockEntity(port);
						if (portEntity instanceof MoltenSaltPortBlockEntity) {
							((MoltenSaltPortBlockEntity)portEntity).link(this.energyStore);
						}
					}
				}
				isFormed = isFormedPending;
				energyStore.setEnabled(isFormed);
			}
		}

		// If multiblock is formed, tick discharge on each of the ports
		if (isFormed) {
			for (BlockPos port : ports) {
				BlockEntity portEntity = world.getBlockEntity(port);
				if (portEntity instanceof MoltenSaltPortBlockEntity) {
//					((MoltenSaltPortBlockEntity) portEntity).dischargeTick();
				}
			}
		}
	}

	@Override
	public void onBreak(World world, PlayerEntity playerEntity, BlockPos blockPos,
		  BlockState blockState) {
		// Make sure to disable the energy store; ports will still have a reference to it and this stops them
		// from producing/consuming further
		energyStore.setEnabled(false);
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
		updateCapacity(this.radius + radiusDelta, this.layers + layersDelta);
	}

	private void updateCapacity(int newRadius, int newLayers) {
		int newCells = calculateCells(newRadius, newLayers);
		if (newCells > 0) {
			this.radius = Math.max(1, newRadius);
			this.layers = Math.max(1, newLayers);
			this.cells = newCells;
			energyStore.setCapacity(E_PER_CELL * (long)cells);
		}
	}

	public boolean isFormed() { return isFormed; }
	public void setIsFormed(boolean isFormed) { this.isFormed = isFormed; }

	public int getCells() { return cells; }
	public void setCells(int cells) { this.cells = cells; }

	public int getRadius() { return radius; }
	public void setRadius(int radius) { this.radius = radius; }

	public int getLayers() { return layers; }
	public void setLayers(int layers) { this.layers = layers; }

	public long getEnergyCapacity() { return energyStore.getCapacity(); }
	public long getEnergyAmount() { return energyStore.getAmount(); }
}
