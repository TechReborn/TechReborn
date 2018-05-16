/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.compat.theoneprobe;

import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.providers.ChestInfoTools;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.api.IListInfoProvider;
import reborncore.api.power.IEnergyInterfaceTile;
import reborncore.common.RebornCoreConfig;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.tile.TileLegacyMachineBase;
import reborncore.common.util.Inventory;
import reborncore.common.util.StringUtils;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Mark on 04/06/2016.
 */
public class ProbeProvider implements IProbeInfoProvider {

	ProgressStyle euStyle = new ProgressStyle().backgroundColor(0xFF8B8B8B).borderColor(0xFF373737).alternateFilledColor(PowerSystem.getDisplayPower().altColour).filledColor(PowerSystem.getDisplayPower().colour);

	MethodHandle methodHandle_addStacks;

	public ProbeProvider() {
		euStyle.suffix(" " + PowerSystem.getDisplayPower().abbreviation);
		euStyle.numberFormat(NumberFormat.COMMAS);

		try {
			//Can we just all agree to make things that might be useful to others public, thanks.
			MethodHandles.Lookup lookup = MethodHandles.lookup();
			Method pm = ChestInfoTools.class.getDeclaredMethod("showChestContents", IProbeInfo.class, World.class, BlockPos.class, List.class, boolean.class);
			pm.setAccessible(true);
			methodHandle_addStacks = lookup.unreflect(pm);
		} catch (NoSuchMethodException | IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	@Override
	public String getID() {
		return "TechReborn";
	}

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
		euStyle = new ProgressStyle().backgroundColor(0xFF8B8B8B).borderColor(0xFF373737).alternateFilledColor(PowerSystem.getDisplayPower().altColour).filledColor(PowerSystem.getDisplayPower().colour);
		euStyle.suffix(" " + PowerSystem.getDisplayPower().abbreviation);
		TileEntity tile = world.getTileEntity(data.getPos());
		if (tile instanceof IListInfoProvider) {
			List<String> strs = new ArrayList<>();
			((IListInfoProvider) tile).addInfo(strs, true);
			for (String string : strs) {
				probeInfo.text(string);
			}
		}
		if (tile instanceof IEnergyInterfaceTile) {
			IEnergyInterfaceTile energy = (IEnergyInterfaceTile) tile;
			if (PowerSystem.getDisplayPower() != PowerSystem.EnergySystem.EU) {
				probeInfo.progress((int) energy.getEnergy() * RebornCoreConfig.euPerFU, (int) energy.getMaxPower() * RebornCoreConfig.euPerFU, euStyle);
			} else {
				probeInfo.progress((int) energy.getEnergy(), (int) energy.getMaxPower(), euStyle);
			}
		}
		if (tile instanceof TileLegacyMachineBase && methodHandle_addStacks != null) {
			TileLegacyMachineBase legacyMachineBase = (TileLegacyMachineBase) tile;
			if (legacyMachineBase.getInventoryForTile().isPresent()) {
				if (player.isSneaking()) {
					probeInfo.text(StringUtils.t("techreborn.tooltip.inventory"));
				}
				Inventory inventory = legacyMachineBase.getInventoryForTile().get();
				if(!inventory.isEmpty()){
					List<ItemStack> stacks = Arrays.stream(inventory.contents).filter(stack -> !stack.isEmpty()).collect(Collectors.toList());
					try {
						methodHandle_addStacks.invoke(probeInfo, world, null, stacks, player.isSneaking());
					} catch (Throwable throwable) {
						throwable.printStackTrace();
					}
				}
			}
			if (!legacyMachineBase.upgradeInventory.isEmpty() && player.isSneaking()) {
				probeInfo.horizontal();
				probeInfo.text(StringUtils.t("techreborn.tooltip.upgrades"));
				List<ItemStack> stacks = Arrays.stream(legacyMachineBase.upgradeInventory.contents).filter(stack -> !stack.isEmpty()).collect(Collectors.toList());
				try {
					methodHandle_addStacks.invoke(probeInfo, world, null, stacks, player.isSneaking());
				} catch (Throwable throwable) {
					throwable.printStackTrace();
				}
			}
		}
	}
}
