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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.property.Property;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.IDebuggable;
import team.reborn.energy.Energy;
import techreborn.TechReborn;

import java.util.Map.Entry;

import static reborncore.common.util.IDebuggable.propertyFormat;

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

		// Name of block
		sendMessage(context, new LiteralText(Formatting.GOLD + "<<< " + block.getName().getString() + " >>>"));


		if(context.getPlayer() != null && context.getPlayer().isSneaking()) {
			sendMessage(context, new LiteralText(getRegistryName(block)));

			for (Entry<Property<?>, Comparable<?>> entry : blockState.getEntries().entrySet()) {
				sendMessage(context, new LiteralText(getPropertyString(entry)));
			}
		}

		BlockEntity blockEntity = context.getWorld().getBlockEntity(context.getBlockPos());
		if (blockEntity != null) {
			sendMessage(context, new LiteralText(getBlockEntityType(blockEntity)));
			if (Energy.valid(blockEntity)) {
				sendMessage(context, new LiteralText(getRCPower(blockEntity)));
			}

			if(blockEntity instanceof IDebuggable){
				sendMessage(context, new LiteralText(((IDebuggable) blockEntity).getDebugText()));
			}

		}

		sendMessage(context, new LiteralText(getBlockEntityType(blockEntity)));

		if (Energy.valid(blockEntity)) {
			sendMessage(context, new LiteralText(getRCPower(blockEntity)));
		}

		sendMessage(context, getBlockEntityTags(blockEntity));

		return ActionResult.SUCCESS;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if(world.isClient) return super.use(world, user, hand);

		// Any right/click functionality here in air or specific
		//sendMessage(user, new LiteralText(getElectricManagerInfo( ElectricNetworkManager.INSTANCE)));

		return super.use(world, user, hand);
	}

	private void sendMessage(ItemUsageContext context, Text string) {
		if (!context.getWorld().isClient) {
			context.getPlayer().sendSystemMessage(string, Util.NIL_UUID);
		}
		context.getPlayer().sendSystemMessage(message, Util.NIL_UUID);
	}

	private void sendMessage(PlayerEntity user, Text string) {
		user.sendSystemMessage(string, Util.NIL_UUID);
	}

	private static String getPropertyString(Entry<Property<?>, Comparable<?>> entryIn) {
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
		return propertyFormat("Registry Name", Registry.BLOCK.getId(block).toString());
	}



	private String getBlockEntityType(BlockEntity blockEntity) {
		return propertyFormat("Tile Entity", blockEntity.getType().toString());
	}

	private String getRCPower(BlockEntity blockEntity) {
		return propertyFormat("Power",
				PowerSystem.getLocaliszedPower(Energy.of(blockEntity).getEnergy())
						+ "/"
						+ PowerSystem.getLocaliszedPower(Energy.of(blockEntity).getMaxStored()));

	}

	private Text getBlockEntityTags(BlockEntity blockEntity){
		MutableText s = new LiteralText("BlockEntity Tags:").formatted(Formatting.GREEN);

		BlockDataObject bdo = new BlockDataObject(blockEntity, blockEntity.getPos());
		s.append(bdo.getTag().toText());

		return s;
	}
}
