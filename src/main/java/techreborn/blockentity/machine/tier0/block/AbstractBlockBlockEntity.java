package techreborn.blockentity.machine.tier0.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.client.screen.builder.BlockEntityScreenHandlerBuilder;
import reborncore.client.screen.builder.ScreenHandlerBuilder;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import techreborn.blockentity.machine.GenericMachineBlockEntity;

public class AbstractBlockBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider, BlockProcessable {
	private final int ENERGY_SLOT = 0;
	private final int INPUT_SLOT = 1;

	protected BlockProcessor processor;

	/**
	 * @param blockEntityType
	 * @param pos
	 * @param state
	 * @param name            {@link String} Name for a {@link BlockEntity}.
	 * @param maxInput        {@code int} Maximum energy input, value in EU
	 * @param maxEnergy       {@code int} Maximum energy buffer, value in EU
	 * @param toolDrop        {@link Block} Block to drop with wrench
	 * @param energySlot      {@code int} Energy slot to use to charge machine from battery
	 */
	public AbstractBlockBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, String name, int maxInput, int maxEnergy, Block toolDrop, int energySlot) {
		super(blockEntityType, pos, state, name, maxInput, maxEnergy, toolDrop, energySlot);
	}


	// BuiltScreenHandlerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
		BlockEntityScreenHandlerBuilder builder = new ScreenHandlerBuilder("blockplacer")
			.player(player.getInventory())
			.inventory()
			.hotbar()
			.addInventory()
			.blockEntity(this)
			.energySlot(ENERGY_SLOT, 8 , 72).syncEnergyValue()
			.slot(INPUT_SLOT, 80, 60);

		processor.syncNbt(builder);

		return builder.addInventory().create(this, syncID);
	}

	@Override
	public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);
		if (world == null || world.isClient) {
			return;
		}

		processor.onTick(world, BlockProcessorUtils.getFrontBlockPosition(blockEntity, pos));
	}

	@Override
	public void writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		processor.writeNbt(tag);
	}

	@Override
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		processor.readNbt(tag);
	}

	public BlockProcessor getProcessor() {
		return processor;
	}

	//BlockBreakerProcessable
	@Override
	public boolean consumeEnergy(int amount) {
		return tryUseExact(getEuPerTick(amount));
	}

	//BlockBreakerProcessable
	@Override
	public void playSound() {
		if (RecipeCrafter.soundHandler != null && canPlaySound()) {
			RecipeCrafter.soundHandler.playSound(false, this);
		}
	}

	@Override
	public boolean canPlaySound() {
		return !isMuffled();
	}
}
