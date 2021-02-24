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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.BlockDataObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.property.Property;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import reborncore.common.powerSystem.PowerSystem;
import team.reborn.energy.Energy;
import techreborn.TechReborn;

import java.util.Map.Entry;

/**
 * Created by Mark on 20/03/2016.
 */
public class DebugToolItem extends Item {

	public DebugToolItem() {
		super(new Item.Settings().group(TechReborn.ITEMGROUP));
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		BlockState blockState = context.getWorld().getBlockState(context.getBlockPos());
		Block block = blockState.getBlock();
		if (block == null) {
			return ActionResult.FAIL;
		}
		sendMessage(context, new LiteralText(getRegistryName(block)));

		for (Entry<Property<?>, Comparable<?>> entry : blockState.getEntries().entrySet()) {
			sendMessage(context, new LiteralText(getPropertyString(entry)));
		}

		BlockEntity blockEntity = context.getWorld().getBlockEntity(context.getBlockPos());
		if (blockEntity == null) {
			return ActionResult.SUCCESS;
		}

		sendMessage(context, new LiteralText(getBlockEntityType(blockEntity)));

		if (Energy.valid(blockEntity)) {
			sendMessage(context, new LiteralText(getRCPower(blockEntity)));
		}

		sendMessage(context, getBlockEntityTags(blockEntity));

		return ActionResult.SUCCESS;
	}

	private void sendMessage(ItemUsageContext context, Text message) {
		if (context.getWorld().isClient || context.getPlayer() == null) {
			return;
		}
		context.getPlayer().sendSystemMessage(message, Util.NIL_UUID);
	}

	private String getPropertyString(Entry<Property<?>, Comparable<?>> entryIn) {
		Property<?> iproperty = entryIn.getKey();
		Comparable<?> comparable = entryIn.getValue();
		String s = Util.getValueAsString(iproperty, comparable);
		if (Boolean.TRUE.equals(comparable)) {
			s = Formatting.GREEN + s;
		} else if (Boolean.FALSE.equals(comparable)) {
			s = Formatting.RED + s;
		}

		return iproperty.getName() + ": " + s;
	}

	private String getRegistryName(Block block) {
		String s = "" + Formatting.GREEN;
		s += "Block Registry Name: ";
		s += Formatting.BLUE;
		s += Registry.BLOCK.getId(block);

		return s;
	}

	private String getBlockEntityType(BlockEntity blockEntity) {
		String s = "" + Formatting.GREEN;
		s += "Block Entity: ";
		s += Formatting.BLUE;
		s += blockEntity.getType().toString();

		return s;
	}

	private String getRCPower(BlockEntity blockEntity) {
		String s = "" + Formatting.GREEN;
		s += "Power: ";
		s += Formatting.BLUE;
		s += PowerSystem.getLocalizedPower(Energy.of(blockEntity).getEnergy());
		s += "/";
		s += PowerSystem.getLocalizedPower(Energy.of(blockEntity).getMaxStored());

		return s;
	}

	private Text getBlockEntityTags(BlockEntity blockEntity){
		MutableText s = new LiteralText("BlockEntity Tags:").formatted(Formatting.GREEN);

		BlockDataObject bdo = new BlockDataObject(blockEntity, blockEntity.getPos());
		s.append(bdo.getTag().toText());

		return s;
	}
}
