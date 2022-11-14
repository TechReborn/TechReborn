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

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
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
	public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);
		if (world == null || getStored() <= 0 || !isActive(RedstoneConfiguration.POWER_IO)) {
			return;
		}

		if (world.getTime() % TechRebornConfig.launchpadInterval != 0) {
			return;
		}

		ensureSelectionInRange();
		final double speed = selectedSpeed();
		final int energyCost = selectedEnergyCost();

		if (getStored() > energyCost) {
			List<Entity> entities = world.getNonSpectatingEntities(Entity.class, new Box(0d,1d,0d,1d,2d,1d).offset(pos));
			if (entities.size() == 0) {
				return;
			}
			world.playSound(null, pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 1f, 1f);
			for (Entity entity : entities) {
				entity.addVelocity(0d, speed, 0d);
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
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		selection = tag.getInt("selection");
	}

	@Override
	public void writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		tag.putInt("selection", selection);
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
	public ItemStack getToolDrop(PlayerEntity p0) {
		return TRContent.Machine.LAUNCHPAD.getStack();
	}

	// BuiltScreenHandlerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
		return new ScreenHandlerBuilder("launchpad")
				.player(player.getInventory())
				.inventory().hotbar().addInventory()
				.blockEntity(this)
				.syncEnergyValue()
				.sync(this::getSelection, this::setSelection)
				.addInventory().create(this, syncID);
	}

	public int getSelection() {
		return selection;
	}

	public void setSelection(int selection) {
		this.selection = selection;
	}
}
