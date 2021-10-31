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

package techreborn.blockentity.storage.energy.idsu;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import reborncore.common.util.NBTSerializable;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;
import techreborn.config.TechRebornConfig;

import java.util.HashMap;

public class IDSUManager extends PersistentState {

	private static final String KEY = "techreborn_idsu";

	private IDSUManager() {
	}

	@NotNull
	public static IDSUPlayer getPlayer(MinecraftServer server, String uuid) {
		return get(server).getPlayer(uuid);
	}

	private static IDSUManager get(MinecraftServer server) {
		ServerWorld serverWorld = server.getWorld(World.OVERWORLD);
		return serverWorld.getPersistentStateManager().getOrCreate(IDSUManager::createFromTag, IDSUManager::new, KEY);
	}

	private final HashMap<String, IDSUPlayer> playerHashMap = new HashMap<>();

	@NotNull
	public IDSUPlayer getPlayer(String uuid) {
		return playerHashMap.computeIfAbsent(uuid, s -> new IDSUPlayer());
	}

	public static IDSUManager createFromTag(NbtCompound tag) {
		IDSUManager	idsuManager = new IDSUManager();
		idsuManager.fromTag(tag);
		return idsuManager;
	}

	public void fromTag(NbtCompound tag) {
		for (String uuid : tag.getKeys()) {
			playerHashMap.put(uuid, new IDSUPlayer(tag.getCompound(uuid)));
		}
	}

	@Override
	public NbtCompound writeNbt(NbtCompound tag) {
		playerHashMap.forEach((uuid, player) -> tag.put(uuid, player.write()));
		return tag;
	}

	public class IDSUPlayer implements NBTSerializable {
		// This storage is never exposed directly, it's always wrapped behind getMaxInput()/getMaxOutput() checks
		private final SimpleEnergyStorage storage = new SimpleEnergyStorage(TechRebornConfig.idsuMaxEnergy, Long.MAX_VALUE, Long.MAX_VALUE) {
			@Override
			protected void onFinalCommit() {
				markDirty();
			}
		};

		private IDSUPlayer() {
		}

		private IDSUPlayer(NbtCompound compoundTag) {
			read(compoundTag);
		}

		@NotNull
		@Override
		public NbtCompound write() {
			NbtCompound tag = new NbtCompound();
			tag.putLong("energy", storage.amount);
			return tag;
		}

		@Override
		public void read(@NotNull NbtCompound tag) {
			storage.amount = tag.getLong("energy");
		}

		public EnergyStorage getStorage() {
			return storage;
		}

		public long getEnergy() {
			return storage.amount;
		}

		public void setEnergy(long energy) {
			storage.amount = energy;
			markDirty();
		}
	}

}
