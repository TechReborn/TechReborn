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

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;
import techreborn.config.TechRebornConfig;

import java.util.HashMap;
import java.util.List;

public class IDSUManager extends PersistentState {
	public static Codec<IDSUManager> CODEC = Codec.list(IDSUPlayer.CODEC).xmap(IDSUManager::fromIDSUPlayers, IDSUManager::getPlayers);
	private static final PersistentStateType<IDSUManager> TYPE = new PersistentStateType<>("techreborn_idsu", IDSUManager::new, CODEC, null);
	private static final String KEY = "techreborn_idsu";

	private IDSUManager() {
	}

	@NotNull
	public static IDSUPlayer getPlayer(MinecraftServer server, String uuid) {
		return get(server).getPlayer(uuid);
	}

	private static IDSUManager get(MinecraftServer server) {
		ServerWorld serverWorld = server.getWorld(World.OVERWORLD);
		return serverWorld.getPersistentStateManager().getOrCreate(TYPE);
	}

	private final HashMap<String, IDSUPlayer> playerHashMap = new HashMap<>();

	@NotNull
	public IDSUPlayer getPlayer(String uuid) {
		return playerHashMap.computeIfAbsent(uuid, s -> new IDSUPlayer(uuid, this::markDirty));
	}

	public static IDSUManager fromIDSUPlayers(List<IDSUPlayer> list) {
		IDSUManager	idsuManager = new IDSUManager();
		for (IDSUPlayer player : list) {
			player.setMarkDirty(idsuManager::markDirty);
			idsuManager.playerHashMap.put(player.getUUID(), player);
		}
		return idsuManager;
	}

	public List<IDSUPlayer> getPlayers() {
		return playerHashMap.values().stream().toList();
	}


	public static class IDSUPlayer {
		public static Codec<IDSUPlayer> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
				Codec.STRING.fieldOf("uuid").forGetter(IDSUPlayer::getUUID),
				Codec.LONG.fieldOf("energy").forGetter(IDSUPlayer::getEnergy)
				)
				.apply(instance, IDSUPlayer::new));
		private String uuid;
		private Runnable markDirty = () -> {};
		// This storage is never exposed directly, it's always wrapped behind getMaxInput()/getMaxOutput() checks
		private final SimpleEnergyStorage storage = new SimpleEnergyStorage(TechRebornConfig.idsuMaxEnergy, Long.MAX_VALUE, Long.MAX_VALUE) {
			@Override
			protected void onFinalCommit() {
				markDirty.run();
			}
		};

		private IDSUPlayer(String uuid, Runnable markDirty) {
			this.uuid = uuid;
			this.markDirty = markDirty;
		}

		public IDSUPlayer(String uuid, Long energy) {
			this.uuid = uuid;
			storage.amount = energy;
		}

		public void setMarkDirty(Runnable markDirty) {
			this.markDirty = markDirty;
		}

		public EnergyStorage getStorage() {
			return storage;
		}

		public long getEnergy() {
			return storage.amount;
		}

		public String getUUID() {
			return uuid;
		}

		public void setEnergy(long energy) {
			storage.amount = energy;
			markDirty.run();
		}
	}

}
