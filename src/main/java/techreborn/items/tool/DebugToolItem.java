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

package techreborn.items.tool;

import reborncore.common.powerSystem.PowerSystem;
import team.reborn.energy.api.EnergyStorage;
import techreborn.init.TRItemSettings;

import java.util.Map.Entry;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.commands.data.BlockDataAccessor;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

/**
 * Created by Mark on 20/03/2016.
 */
public class DebugToolItem extends Item {

	public DebugToolItem(String name) {
		super(TRItemSettings.item(name));
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		BlockState blockState = context.getLevel().getBlockState(context.getClickedPos());
		Block block = blockState.getBlock();
		if (block == null) {
			return InteractionResult.FAIL;
		}
		if (context.getLevel().isClientSide()) {
			return InteractionResult.SUCCESS;
		}
		sendMessage(context, Component.literal(getRegistryName(block)));

		for (Entry<Property<?>, Comparable<?>> entry : blockState.getValues().entrySet()) {
			sendMessage(context, Component.literal(getPropertyString(entry)));
		}

		EnergyStorage energyStorage = EnergyStorage.SIDED.find(context.getLevel(), context.getClickedPos(), context.getClickedFace());
		if (energyStorage != null) {
			sendMessage(context, Component.literal(getRCPower(energyStorage)));
		}

		BlockEntity blockEntity = context.getLevel().getBlockEntity(context.getClickedPos());
		if (blockEntity == null) {
			return InteractionResult.CONSUME;
		}

		sendMessage(context, Component.literal(getBlockEntityType(blockEntity)));

		sendMessage(context, getBlockEntityTags(blockEntity));

		return InteractionResult.CONSUME;
	}

	private void sendMessage(UseOnContext context, Component message) {
		if (context.getLevel().isClientSide() || context.getPlayer() == null) {
			return;
		}
		context.getPlayer().displayClientMessage(message, false); // TODO check if this is correct boolean
	}

	private String getPropertyString(Entry<Property<?>, Comparable<?>> entryIn) {
		Property<?> property = entryIn.getKey();
		Comparable<?> comparable = entryIn.getValue();
		String s = Util.getPropertyName(property, comparable);
		if (Boolean.TRUE.equals(comparable)) {
			s = ChatFormatting.GREEN + s;
		} else if (Boolean.FALSE.equals(comparable)) {
			s = ChatFormatting.RED + s;
		}

		return property.getName() + ": " + s;
	}

	private String getRegistryName(Block block) {
		String s = "" + ChatFormatting.GREEN;
		s += "Block Registry Name: ";
		s += ChatFormatting.BLUE;
		s += BuiltInRegistries.BLOCK.getKey(block);

		return s;
	}

	private String getBlockEntityType(BlockEntity blockEntity) {
		String s = "" + ChatFormatting.GREEN;
		s += "Block Entity: ";
		s += ChatFormatting.BLUE;
		s += blockEntity.getType().toString();

		return s;
	}

	private String getRCPower(EnergyStorage energyStorage) {
		String s = "" + ChatFormatting.GREEN;
		s += "Power: ";
		s += ChatFormatting.BLUE;
		s += PowerSystem.getLocalizedPower(energyStorage.getAmount());
		s += "/";
		s += PowerSystem.getLocalizedPower(energyStorage.getCapacity());

		return s;
	}

	private Component getBlockEntityTags(BlockEntity blockEntity){
		MutableComponent s = Component.literal("BlockEntity Tags:").withStyle(ChatFormatting.GREEN);

		BlockDataAccessor bdo = new BlockDataAccessor(blockEntity, blockEntity.getBlockPos());
		s.append(bdo.getData().toString());

		return s;
	}
}
