/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

package techreborn.packets;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import reborncore.common.network.ExtendedPacketBuffer;
import reborncore.common.network.INetworkPacket;
import techreborn.tiles.TileAutoCraftingTable;

import java.io.IOException;

/**
 * Created by modmuss50 on 20/06/2017.
 */
public class PacketSetRecipe implements INetworkPacket<PacketSetRecipe> {

	BlockPos pos;
	ResourceLocation recipe;
	boolean custom;

	public PacketSetRecipe(TileAutoCraftingTable tile, IRecipe recipe, boolean custom) {
		this.pos = tile.getPos();
		if (recipe == null) {
			this.recipe = new ResourceLocation("");
		} else {
			this.recipe = recipe.getRegistryName();
		}
		this.custom = custom;
	}

	public PacketSetRecipe() {
	}

	@Override
	public void writeData(ExtendedPacketBuffer buffer) throws IOException {
		buffer.writeBlockPos(pos);
		buffer.writeResourceLocation(recipe);
		buffer.writeBoolean(custom);
	}

	@Override
	public void readData(ExtendedPacketBuffer buffer) throws IOException {
		pos = buffer.readBlockPos();
		recipe = buffer.readResourceLocation();
		custom = buffer.readBoolean();
	}

	@Override
	public void processData(PacketSetRecipe message, MessageContext context) {
		TileEntity tileEntity = context.getServerHandler().player.world.getTileEntity(message.pos);
		if (tileEntity instanceof TileAutoCraftingTable) {
			((TileAutoCraftingTable) tileEntity).setCurrentRecipe(message.recipe, message.custom);
		}
	}
}
