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

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;
import reborncore.common.util.NBTSerializable;
import reborncore.common.world.DataAttachment;
import reborncore.common.world.DataAttachmentProvider;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class IDSUManager implements DataAttachment {

	@Nonnull
	public static IDSUPlayer getPlayer(World world, String uuid){
		return get(world).getPlayer(uuid);
	}

	public static IDSUManager get(World world){
		return DataAttachmentProvider.get(world, IDSUManager.class);
	}

	private final HashMap<String, IDSUPlayer> playerHashMap = new HashMap<>();

	@Nonnull
	public IDSUPlayer getPlayer(String uuid){
		return playerHashMap.computeIfAbsent(uuid, s -> new IDSUPlayer());
	}

	@Override
	public void read(@Nonnull CompoundTag tag) {
		for(String uuid : tag.getKeys()){
			playerHashMap.put(uuid, new IDSUPlayer(tag.getCompound(uuid)));
		}
	}

	@Nonnull
	@Override
	public CompoundTag write() {
		CompoundTag tag = new CompoundTag();
		playerHashMap.forEach((uuid, player) -> tag.put(uuid, player.write()));
		return tag;
	}

	public static class IDSUPlayer implements NBTSerializable {

		private double energy;

		private IDSUPlayer() {
		}

		private IDSUPlayer(CompoundTag compoundTag){
			read(compoundTag);
		}

		@Nonnull
		@Override
		public CompoundTag write() {
			CompoundTag tag = new CompoundTag();
			tag.putDouble("energy", energy);
			return tag;
		}

		@Override
		public void read(@Nonnull CompoundTag tag) {
			energy = tag.getDouble("energy");
		}

		public double getEnergy() {
			return energy;
		}

		public void setEnergy(double energy) {
			this.energy = energy;
		}
	}

}
