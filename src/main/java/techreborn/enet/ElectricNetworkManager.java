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

package techreborn.enet;

import net.fabricmc.fabric.api.event.registry.BlockConstructedCallback;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import team.reborn.energy.EnergyTier;
import techreborn.TechReborn;

import java.util.ArrayList;
import java.util.HashMap;

public class ElectricNetworkManager implements ServerTickCallback {
	public static ElectricNetworkManager INSTANCE;

	private int lastId = 0;
	private HashMap<Integer, ElectricNetwork> electricNetworks = new HashMap<>();

	public ElectricNetworkManager() {
		ServerTickCallback.EVENT.register(this::tick);
	}

	public static void init() {
		INSTANCE = new ElectricNetworkManager();
	}

	@Override
	public void tick(MinecraftServer minecraftServer) {
		ArrayList<Integer> networksToRemove = new ArrayList<>();

		for (ElectricNetwork currentNetwork: electricNetworks.values()) {
			currentNetwork.tick();

			if (currentNetwork.isEmpty()) {
				networksToRemove.add(currentNetwork.getId());
			}
		}

		for (Integer networkId: networksToRemove) {
			electricNetworks.remove(networkId);

			TechReborn.LOGGER.debug(
					"Electric Network {} is empty, removed (total: {})",
					networkId,
					electricNetworks.size());
		}
	}

	public ElectricNetwork getNetwork(int id, EnergyTier tier) {
		if (electricNetworks.containsKey(id)) {
			return electricNetworks.get(id);
		}

		return this.createAndTrackNetwork(id, tier);
	}

	public ElectricNetwork newNetwork(World world, EnergyTier tier) {
		return this.createAndTrackNetwork(++lastId, tier);
	}

	private ElectricNetwork createAndTrackNetwork(int id, EnergyTier tier) {
		ElectricNetwork newNetwork = new ElectricNetwork(id, tier);
		electricNetworks.put(id, newNetwork);

		TechReborn.LOGGER.debug("Created new electric network ID: {} (total: {})", id, electricNetworks.size());

		return newNetwork;
	}
}
