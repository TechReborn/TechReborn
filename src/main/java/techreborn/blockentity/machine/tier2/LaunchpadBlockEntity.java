/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

package techreborn.blockentity.machine.tier2;

import org.jetbrains.annotations.Nullable;
import reborncore.api.IToolDrop;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blockentity.RedstoneConfiguration;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;

public class LaunchpadBlockEntity extends PowerAcceptorBlockEntity implements IToolDrop, BuiltScreenHandlerProvider {

	public static final int MAX_SELECTION = 3;
	private int selection = TechRebornConfig.launchpadDefaultSelection;

	public LaunchpadBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.LAUNCHPAD, pos, state);
	}

	public void handleGuiInputFromClient(int amount) {
		selection += amount;
		ensureSelectionInRange();
	}

	public void ensureSelectionInRange() {
		if (selection > MAX_SELECTION) {
			selection = MAX_SELECTION;
		}
		if (selection <= 0) {
			selection = 0;
		}
	}

	public double selectedSpeed() {
		return switch(selection) {
			case 0 -> TechRebornConfig.launchpadSpeedLow;
			case 1 -> TechRebornConfig.launchpadSpeedMedium;
			case 2 -> TechRebornConfig.launchpadSpeedHigh;
			case MAX_SELECTION -> TechRebornConfig.launchpadSpeedExtreme;
			default -> throw new IllegalArgumentException("Impossible launchpad selection value!");
		};
	}

	public int selectedEnergyCost() {
		return switch(selection) {
			case 0 -> TechRebornConfig.launchpadEnergyLow;
			case 1 -> TechRebornConfig.launchpadEnergyMedium;
			case 2 -> TechRebornConfig.launchpadEnergyHigh;
			case MAX_SELECTION -> TechRebornConfig.launchpadEnergyExtreme;
			default -> throw new IllegalArgumentException("Impossible launchpad selection value!");
		};
	}

	public String selectedTranslationKey() {
		return switch(selection) {
			case 0 -> "techreborn.message.info.block.techreborn.launchpad.low";
			case 1 -> "techreborn.message.info.block.techreborn.launchpad.medium";
			case 2 -> "techreborn.message.info.block.techreborn.launchpad.high";
			case MAX_SELECTION -> "techreborn.message.info.block.techreborn.launchpad.extreme";
			default -> throw new IllegalArgumentException("Impossible launchpad selection value!");
		};
	}

	// PowerAcceptorBlockEntity
	@Override
	public void tick(Level world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);
		if (world == null || getStored() <= 0 || !isActive(RedstoneConfiguration.Element.POWER_IO)) {
			return;
		}

		if (world.getGameTime() % TechRebornConfig.launchpadInterval != 0) {
			return;
		}

		ensureSelectionInRange();
		final double speed = selectedSpeed();
		final int energyCost = selectedEnergyCost();

		if (getStored() > energyCost) {
			List<Entity> entities = world.getEntitiesOfClass(Entity.class, new AABB(0d,1d,0d,1d,2d,1d).move(pos));
			if (entities.isEmpty()) {
				return;
			}
			world.playSound(null, pos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1f, 1f);
			for (Entity entity : entities) {
				entity.push(0d, speed, 0d);
			}
			useEnergy(energyCost);
		}
	}

	@Override
	public long getBaseMaxPower() {
		return TechRebornConfig.launchpadMaxEnergy;
	}

	@Override
	public boolean canProvideEnergy(@Nullable Direction side) {
		return false;
	}

	@Override
	public long getBaseMaxOutput() {
		return 0;
	}

	@Override
	public long getBaseMaxInput() {
		return TechRebornConfig.launchpadMaxInput;
	}

	@Override
	public void loadAdditional(ValueInput view) {
		super.loadAdditional(view);
		selection = view.getIntOr("selection", 0);
	}

	@Override
	public void saveAdditional(ValueOutput view) {
		super.saveAdditional(view);
		view.putInt("selection", selection);
	}

	// MachineBaseBlockEntity
	@Override
	public boolean hasSlotConfig() {
		return false;
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(Player p0) {
		return TRContent.Machine.LAUNCHPAD.getStack();
	}

	// BuiltScreenHandlerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, Player player) {
		return new ScreenHandlerBuilder("launchpad")
				.player(player.getInventory())
				.inventory().hotbar().addInventory()
				.blockEntity(this)
				.syncEnergyValue()
				.sync(ByteBufCodecs.INT, this::getSelection, this::setSelection)
				.addInventory().create(this, syncID);
	}

	public int getSelection() {
		return selection;
	}

	public void setSelection(int selection) {
		this.selection = selection;
	}
}
