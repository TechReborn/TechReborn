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

package techreborn.packets;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import reborncore.common.network.ExtendedPacketBuffer;
import reborncore.common.network.INetworkPacket;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.OreUtil;
import techreborn.init.ModItems;
import techreborn.lib.ModInfo;

import java.io.IOException;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class PacketRefund implements INetworkPacket<PacketRefund> {

	@ConfigRegistry(config = "misc", category = "general", key = "manualRefund", comment = "Allow refunding items used to craft the manual")
	public static boolean allowRefund = true;

	@Override
	public void writeData(ExtendedPacketBuffer buffer) throws IOException {

	}

	@Override
	public void readData(ExtendedPacketBuffer buffer) throws IOException {

	}

	@Override
	public void processData(PacketRefund message, MessageContext context) {
		if (allowRefund) {
			EntityPlayerMP playerMP = context.getServerHandler().player;
			for (int i = 0; i < playerMP.inventory.getSizeInventory(); i++) {
				ItemStack stack = playerMP.inventory.getStackInSlot(i);
				if (stack.getItem() == ModItems.MANUAL) {
					playerMP.inventory.removeStackFromSlot(i);
					playerMP.inventory.addItemStackToInventory(new ItemStack(Items.BOOK));
					playerMP.inventory.addItemStackToInventory(OreUtil.getStackFromName("ingotRefinedIron"));
					return;
				}
			}
		}

	}
}
