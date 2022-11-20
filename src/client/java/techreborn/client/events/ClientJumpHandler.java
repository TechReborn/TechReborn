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

package techreborn.client.events;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import reborncore.client.ClientJumpEvent;
import reborncore.client.ClientNetworkManager;
import techreborn.blockentity.machine.tier1.ElevatorBlockEntity;
import techreborn.packets.ServerboundPackets;

public class ClientJumpHandler implements ClientJumpEvent {
	@Override
	public void jump() {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;

		if (player == null) {
			return;
		}

		BlockEntity blockEntity = player.getWorld().getBlockEntity(player.getBlockPos().down());

		if (blockEntity instanceof ElevatorBlockEntity) {
			ClientNetworkManager.sendToServer(ServerboundPackets.createPacketJump(player.getBlockPos()));
		}
	}
}
