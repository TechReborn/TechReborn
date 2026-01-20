/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2026 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.blockentity.generator.nuclear;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jetbrains.annotations.Nullable;

import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blockentity.MultiblockWriter;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.BlockEntityScreenHandlerBuilder;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;
import techreborn.items.reactor.ReactorComponentItem;

public class NuclearReactorBlockEntity extends PowerAcceptorBlockEntity implements IToolDrop, InventoryProvider, BuiltScreenHandlerProvider {
	// Grid constants
	public static final int GRID_WIDTH = 9;
	public static final int GRID_HEIGHT = 6;
	public static final int GRID_SLOTS = GRID_WIDTH * GRID_HEIGHT;
	public static final int BASE_COLUMNS = 3;
	public static final int MAX_CHAMBERS = 6;

	public final RebornInventory<NuclearReactorBlockEntity> inventory = new RebornInventory<>(GRID_SLOTS, "NuclearReactorBlockEntity", 1, this);

	// Core state
	private int heat = 0;
	private int maxHeat = TechRebornConfig.nuclearReactorMaxHeat;
	private float heatEffectModifier = 1.0f;
	private int euPerTick = 0;
	private int chamberCount = 0;
	private int tickCounter = 0;

	public NuclearReactorBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.NUCLEAR_REACTOR, pos, state);
		checkTier();
	}

	// Tick processing

	@Override
	public void tick(Level world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);

		if (world == null) return;

		if (world.isClientSide()) {
			// Maybe add client-side heat particles? smoke/fire/etc.
			return;
		}

		// Generate EU continuously
		if (euPerTick > 0) {
			addEnergy((long) (euPerTick * TechRebornConfig.nuclearReactorEUMultiplier));
		}

		// Process reactor once per second
		if (++tickCounter >= TechRebornConfig.nuclearReactorTickRate) {
			tickCounter = 0;
			processReactor();
		}
	}

	/**
	 * Main reactor processing - single pass through all components.
	 */
	private void processReactor() {
		if (level == null) return;

		// Count chambers and eject items from locked slots
		chamberCount = countAdjacentChambers();
		ejectLockedItems();

		// Reset cycle values
		euPerTick = 0;
		maxHeat = TechRebornConfig.nuclearReactorMaxHeat;
		heatEffectModifier = 1.0f;

		// Process all components
		int size = getReactorSize();
		for (int y = 0; y < GRID_HEIGHT; y++) {
			for (int x = 0; x < size; x++) {
				ItemStack stack = getItemAt(x, y);
				if (!stack.isEmpty() && stack.getItem() instanceof ReactorComponentItem comp) {
					comp.processComponent(stack, this, x, y);
				}
			}
		}

		// Handle heat effects
		if (processHeatEffects()) {
			return; // Reactor melted down
		}

		// Update block active state
		float heatPercent = (float) heat / maxHeat;
		boolean active = heatPercent >= 0.1f || euPerTick > 0;
		BlockState currentState = level.getBlockState(worldPosition);
		if (currentState.getBlock() instanceof BlockMachineBase machine) {
			machine.setActive(active, level, worldPosition);
		}

		setChanged();
	}

	/**
	 * Count adjacent reactor chambers.
	 */
	private int countAdjacentChambers() {
		if (level == null) return 0;
		int count = 0;
		for (Direction dir : Direction.values()) {
			if (level.getBlockEntity(worldPosition.relative(dir)) instanceof ReactorChamberBlockEntity) {
				count++;
			}
		}
		return Math.min(count, MAX_CHAMBERS);
	}

	/**
	 * Eject items from columns that are no longer accessible.
	 */
	private void ejectLockedItems() {
		if (level == null) return;
		int size = getReactorSize();
		for (int slot = 0; slot < GRID_SLOTS; slot++) {
			if (slot % GRID_WIDTH >= size) {
				ItemStack stack = inventory.getItem(slot);
				if (!stack.isEmpty()) {
					inventory.setItem(slot, ItemStack.EMPTY);
					Containers.dropItemStack(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1.0, worldPosition.getZ() + 0.5, stack);
				}
			}
		}
	}

	/**
	 * Process heat effects and check for meltdown.
	 *
	 * @return true if reactor melted down
	 */
	private boolean processHeatEffects() {
		if (level == null) return false;

		if (maxHeat <= 0) return false;

		float heatPercent = (float) heat / maxHeat;

		// Meltdown at 100%
		if (heatPercent >= 1.0f) {
			meltdown();
			return true;
		}

		// Maybe add overheat effects for lower heat percentages?

		return false;
	}

	/**
	 * Handle reactor meltdown.
	 */
	private void meltdown() {
		if (level == null) return;
		// If explosions disabled, just disable energy generation and keep heat maxed out
		if (!TechRebornConfig.nuclearReactorExplosionEnabled) {
			heat = maxHeat;
			euPerTick = 0;
			return;
		}

		// Calculate explosion power from components
		float power = 10.0f;
		float modifier = 1.0f;

		for (int i = 0; i < GRID_SLOTS; i++) {
			ItemStack stack = inventory.getItem(i);
			if (!stack.isEmpty() && stack.getItem() instanceof ReactorComponentItem comp) {
				float influence = comp.getExplosionInfluence(stack);
				if (influence > 0) power += influence;
				else if (influence < 0) modifier *= -influence;
			}
			inventory.setItem(i, ItemStack.EMPTY);
		}

		power = Math.min(power * heatEffectModifier * modifier, TechRebornConfig.nuclearReactorExplosionPowerLimit);

		// Remove chambers
		for (Direction dir : Direction.values()) {
			BlockPos chamberPos = worldPosition.relative(dir);
			if (level.getBlockEntity(chamberPos) instanceof ReactorChamberBlockEntity) {
				level.removeBlock(chamberPos, false);
			}
		}

		// Remove reactor and explode
		level.removeBlock(worldPosition, false);
		if (!level.isClientSide()) {
			level.explode(null, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, power, Level.ExplosionInteraction.BLOCK);
		}

		heat = 0;
		euPerTick = 0;
	}

	// Public API for components

	public int getHeat() {
		return heat;
	}

	public void setHeat(int heat) {
		this.heat = heat;
	}

	public void addHeat(int amount) {
		this.heat += amount;
	}

	public int getMaxHeat() {
		return maxHeat;
	}

	public void setMaxHeat(int maxHeat) {
		this.maxHeat = maxHeat;
	}

	public void addMaxHeat(int amount) {
		this.maxHeat += amount;
	}

	public float getHeatEffectModifier() {
		return heatEffectModifier;
	}

	public void multiplyHeatEffectModifier(float mult) {
		this.heatEffectModifier *= mult;
	}

	public void addEuOutput(int eu) {
		this.euPerTick += eu;
	}

	public boolean isActive() {
		return level != null && level.hasNeighborSignal(worldPosition);
	}

	public int getReactorSize() {
		return Math.min(BASE_COLUMNS + chamberCount, GRID_WIDTH);
	}

	public ItemStack getItemAt(int x, int y) {
		if (x < 0 || x >= getReactorSize() || y < 0 || y >= GRID_HEIGHT) return null;
		return inventory.getItem(y * GRID_WIDTH + x);
	}

	public void setItemAt(int x, int y, ItemStack stack) {
		if (x < 0 || x >= getReactorSize() || y < 0 || y >= GRID_HEIGHT) return;
		inventory.setItem(y * GRID_WIDTH + x, stack != null ? stack : ItemStack.EMPTY);
	}

	/**
	 * Check if an adjacent slot contains a fuel rod.
	 */
	public boolean hasFuelRodAt(int x, int y) {
		ItemStack stack = getItemAt(x, y);
		return stack != null && !stack.isEmpty() && stack.getItem() instanceof ReactorComponentItem comp && comp.isFuelRod();
	}

	/**
	 * Check if an adjacent slot can accept heat.
	 */
	public boolean canAcceptHeatAt(int x, int y) {
		ItemStack stack = getItemAt(x, y);
		return stack != null && !stack.isEmpty() && stack.getItem() instanceof ReactorComponentItem comp && comp.canStoreHeat();
	}

	/**
	 * Transfer heat to a component at the given position.
	 *
	 * @return amount of heat that couldn't be stored
	 */
	public int transferHeatTo(int x, int y, int heat) {
		ItemStack stack = getItemAt(x, y);
		if (stack == null || stack.isEmpty()) return heat;
		if (!(stack.getItem() instanceof ReactorComponentItem comp)) return heat;
		return comp.addHeat(stack, this, x, y, heat);
	}

	// Chamber management

	public void onChamberAdded(BlockPos chamberPos) {
		chamberCount = countAdjacentChambers();
		setChanged();
	}

	public void onChamberRemoved(BlockPos chamberPos) {
		chamberCount = countAdjacentChambers();
		ejectLockedItems();
		setChanged();
	}

	public boolean isSlotAvailable(int slot) {
		return (slot % GRID_WIDTH) < getReactorSize();
	}

	private boolean isValidItem(ItemStack stack) {
		return stack.getItem() instanceof ReactorComponentItem;
	}

	// NBT

	@Override
	public void saveAdditional(ValueOutput view) {
		super.saveAdditional(view);
		view.putInt("Heat", heat);
		view.putInt("EuPerTick", euPerTick);
	}

	@Override
	public void loadAdditional(ValueInput view) {
		super.loadAdditional(view);
		heat = view.getIntOr("Heat", 0);
		euPerTick = view.getIntOr("EuPerTick", 0);
	}

	// GUI sync

	public int getEuPerTick() {
		return euPerTick;
	}

	public void setEuPerTick(int eu) {
		this.euPerTick = eu;
	}

	public int getChamberCount() {
		return chamberCount;
	}

	public void setChamberCount(int count) {
		this.chamberCount = count;
	}

	// PowerAcceptorBlockEntity

	@Override
	public long getBaseMaxPower() {
		return TechRebornConfig.nuclearReactorMaxEnergy;
	}

	@Override
	public boolean canAcceptEnergy(@Nullable Direction side) {
		return false;
	}

	@Override
	public boolean canProvideEnergy(@Nullable Direction side) {
		return true;
	}

	@Override
	public long getBaseMaxOutput() {
		return TechRebornConfig.nuclearReactorMaxOutput;
	}

	@Override
	public long getBaseMaxInput() {
		return 0;
	}

	// Multiblock

	@Override
	public boolean hasMultiblock() {
		return true;
	}

	@Override
	public void writeMultiblock(MultiblockWriter writer) {
		BlockState chamber = TRContent.Machine.REACTOR_CHAMBER.block.defaultBlockState();
		for (Direction dir : Direction.values()) {
			writer.add(dir.getStepX(), dir.getStepY(), dir.getStepZ(), (w, p) -> true, chamber);
		}
	}

	// Other interfaces

	@Override
	public ItemStack getToolDrop(Player player) {
		return TRContent.Machine.NUCLEAR_REACTOR.getStack();
	}

	@Override
	public RebornInventory<NuclearReactorBlockEntity> getInventory() {
		return inventory;
	}

	@Override
	public boolean canBeUpgraded() {
		return false; // Nuclear reactor cannot be upgraded
	}

	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, Player player) {
		BlockEntityScreenHandlerBuilder builder = new ScreenHandlerBuilder("nuclear_reactor").player(player.getInventory()).inventory(8, 146).hotbar(8, 204).addInventory().blockEntity(this);

		for (int row = 0; row < GRID_HEIGHT; row++) {
			for (int col = 0; col < GRID_WIDTH; col++) {
				int slot = row * GRID_WIDTH + col;
				int x = 8 + col * 18;
				int y = 18 + row * 18;
				final int s = slot;
				builder = builder.filterSlot(slot, x, y, stack -> isSlotAvailable(s) && isValidItem(stack), 1);
			}
		}

		return builder.sync(ByteBufCodecs.INT, this::getHeat, this::setHeat).sync(ByteBufCodecs.INT, this::getMaxHeat, this::setMaxHeat).sync(ByteBufCodecs.INT, this::getEuPerTick, this::setEuPerTick).sync(ByteBufCodecs.INT, this::getChamberCount, this::setChamberCount).syncEnergyValue().addInventory().create(this, syncID);
	}
}
