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

package techreborn.blockentity.storage.energy.lesu;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.apache.commons.lang3.tuple.Pair;
import reborncore.api.IToolDrop;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import static techreborn.blockentity.storage.energy.lesu.LapotronicSUBlockEntity.*;

public class LSUStorageBlockEntity extends MachineBaseBlockEntity
		implements IToolDrop {

	public LapotronicSUBlockEntity master = null;
	public byte neighbors = 0b000000;
	public byte links = 0b000000;

	public LSUStorageBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.LSU_STORAGE, pos, state);
	}

	public final void connectNeighbors() {
		if (world == null) return;

		// 1. Collect information and change the relationship between surrounding blocks
		byte flagCanConnect = 0b000000;
		LinkedList<LSUStorageBlockEntity> canConnect = new LinkedList<>();
		HashSet<BlockPos> visited = new HashSet<>();
		visited.add(pos);
		for (int i = 0; i < DIRECTIONS_LENGTH; i++) {
			switch (world.getBlockEntity(pos.offset(DIRECTIONS[i]))) {
				case LSUStorageBlockEntity lsu_storage -> {
					neighbors |= FLAGS[i];
					lsu_storage.neighbors |= OPP_FLAGS[i];
					lsu_storage.markDirty();
					if (lsu_storage.master == null) {
						flagCanConnect |= FLAGS[i];
						canConnect.add(lsu_storage);
						visited.add(lsu_storage.pos);
					} else if (master == null) {
						master = lsu_storage.master;
						lsu_storage.links |= OPP_FLAGS[i];
					}
				}
				case LapotronicSUBlockEntity lapotronic_su -> {
					lapotronic_su.neighbors |= OPP_FLAGS[i];
					lapotronic_su.markDirty();
					if (master == null) {
						master = lapotronic_su;
					}
				}
				case null, default -> {}
			}
		}
		if (neighbors != 0b000000) {
			markDirty();
		}
		if (master == null) {
			return;
		}

		// 2. Check if only one is added
		int count = master.getConnectedBlocksNum() + 1;
		if (flagCanConnect == 0b000000) {
			master.setConnectedBlocksNum(count);
			return;
		}

		// 3. Expand outward layer by layer to search for connectable blocks and connect them
		links = flagCanConnect;
		LSUStorageBlockEntity lsu_storage;
		BlockPos linkPos;
		while (!canConnect.isEmpty()) {
			lsu_storage = canConnect.poll();
			lsu_storage.master = master;
			count++;
			for (int i = 0; i < DIRECTIONS_LENGTH; i++) {
				if ((lsu_storage.neighbors & FLAGS[i]) != 0) {
					linkPos = lsu_storage.pos.offset(DIRECTIONS[i]);
					if (visited.add(linkPos) && world.getBlockEntity(linkPos) instanceof LSUStorageBlockEntity link_lsu_storage) {
						lsu_storage.links |= FLAGS[i];
						canConnect.add(link_lsu_storage);
					}
				}
			}
		}

		// 4. Set the number of connected blocks
		master.setConnectedBlocksNum(count);
	}

	public final void disconnectNeighbors() {
		if (world == null) return;

		// 1. Collect information and delete relationships with surrounding blocks
		LSUStorageBlockEntity lsu_storage;
		ArrayList<LSUStorageBlockEntity> branches = new ArrayList<>(DIRECTIONS_LENGTH);
		for (int i = 0; i < DIRECTIONS_LENGTH; i++) {
			if ((links & FLAGS[i]) != 0) {
				lsu_storage = fastGetLSUS(DIRECTIONS[i]);
				lsu_storage.neighbors ^= OPP_FLAGS[i];
				lsu_storage.markDirty();
				branches.add(lsu_storage);
			} else if ((neighbors & FLAGS[i]) != 0) {
				lsu_storage = fastGetLSUS(DIRECTIONS[i]);
				lsu_storage.neighbors ^= OPP_FLAGS[i];
				lsu_storage.links &= (byte) ~OPP_FLAGS[i];
				lsu_storage.markDirty();
			} else if (world.getBlockEntity(pos.offset(DIRECTIONS[i])) instanceof LapotronicSUBlockEntity lapotronic_su) {
				lapotronic_su.neighbors ^= OPP_FLAGS[i];
				lapotronic_su.markDirty();
			}
		}
		if (master == null) {
			return;
		}

		// 2. Check if only one is deleted
		int count = master.getConnectedBlocksNum() - 1;
		int size = branches.size();
		if (size == 0) {
			master.setConnectedBlocksNum(count);
			return;
		}

		// 3. Handling the case of only one branch (simple)
		if (size == 1) {
			HashMap<BlockPos, LSUStorageBlockEntity> visited = new HashMap<>();
			ArrayList<LSUStorageBlockEntity> canDelete = new ArrayList<>(count);
			lsu_storage = branches.getFirst();
			visited.put(lsu_storage.pos, null);
			canDelete.add(lsu_storage);

			// 3.1 Expand outwards layer by layer, marking all connected blocks and collecting parent and unused route directions
			LSUStorageBlockEntity link_lsu_storage;
			LinkedList<Pair<LSUStorageBlockEntity, Integer>> unused = new LinkedList<>();
			for (int i = 0, length = canDelete.size(); i < length; i++) {
				lsu_storage = canDelete.get(i);
				for (int j = 0; j < DIRECTIONS_LENGTH; j++) {
					if ((lsu_storage.links & FLAGS[j]) != 0) {
						link_lsu_storage = lsu_storage.fastGetLSUS(DIRECTIONS[j]);
						visited.put(link_lsu_storage.pos, lsu_storage);
						canDelete.add(link_lsu_storage);
						length++;
					} else if ((lsu_storage.neighbors & FLAGS[j]) != 0) {
						unused.add(Pair.of(lsu_storage, j));
					}
				}
			}

			// 3.2 Check if the block can be connected to the master
			BlockPos linkPos;
			for (Pair<LSUStorageBlockEntity, Integer> pair : unused) {
				lsu_storage = pair.getLeft();
				linkPos = lsu_storage.pos.offset(DIRECTIONS[pair.getRight()]);
				if (!visited.containsKey(linkPos)) {
					link_lsu_storage = fastGetLSUS(linkPos);
					if (link_lsu_storage.master == master) {
						// 3.3 Connect the block
						link_lsu_storage.links |= OPP_FLAGS[pair.getRight()];

						// 3.4 Change route direction from current location to start point
						LSUStorageBlockEntity child = lsu_storage;
						LSUStorageBlockEntity parent = visited.get(child.pos);
						while (parent != null) {
							for (int j = 0; j < DIRECTIONS_LENGTH; j++) {
								if ((parent.links & FLAGS[j]) != 0 && parent.pos.offset(DIRECTIONS[j]).equals(child.pos)) {
									parent.links ^= FLAGS[j];
									child.links |= OPP_FLAGS[j];
									break;
								}
							}
							child = parent;
							parent = visited.get(parent.pos);
						}

						// 3.5 Delete only one
						master.setConnectedBlocksNum(count);
						return;
					}
				}
			}

			// 3.6 Failure to find a block connected to the master will delete all connected blocks
			for (LSUStorageBlockEntity blockEntity : canDelete) {
				blockEntity.master = null;
				blockEntity.links = 0b000000;
			}
			master.setConnectedBlocksNum(count - canDelete.size());
			return;
		}

		// 4. Handling the case of multiple branches (interdependence)
		ArrayList<HashMap<BlockPos, LSUStorageBlockEntity>> visitedList = new ArrayList<>(size);
		ArrayList<LinkedList<Pair<LSUStorageBlockEntity, Integer>>> unusedList = new ArrayList<>(size);
		ArrayList<ArrayList<LSUStorageBlockEntity>> canDeleteList = new ArrayList<>(size);

		HashMap<BlockPos, LSUStorageBlockEntity> visited;
		LinkedList<Pair<LSUStorageBlockEntity, Integer>> unused;
		ArrayList<LSUStorageBlockEntity> canDelete;

		LSUStorageBlockEntity link_lsu_storage;
		for (int i = 0; i < size; i++) {
			visited = new HashMap<>();
			unused = new LinkedList<>();
			canDelete = new ArrayList<>(count);

			lsu_storage = branches.get(i);
			visited.put(lsu_storage.pos, null);
			canDelete.add(lsu_storage);

			// 4.1 Expand outwards layer by layer, marking all connected blocks and collecting parent and unused route directions
			for (int j = 0, length = canDelete.size(); j < length; j++) {
				lsu_storage = canDelete.get(j);
				for (int k = 0; k < DIRECTIONS_LENGTH; k++) {
					if ((lsu_storage.links & FLAGS[k]) != 0) {
						link_lsu_storage = lsu_storage.fastGetLSUS(DIRECTIONS[k]);
						visited.put(link_lsu_storage.pos, lsu_storage);
						canDelete.add(link_lsu_storage);
						length++;
					} else if ((lsu_storage.neighbors & FLAGS[k]) != 0) {
						unused.add(Pair.of(lsu_storage, k));
					}
				}
			}

			// 4.2 Save to list
			visitedList.add(visited);
			unusedList.add(unused);
			canDeleteList.add(canDelete);
		}

		// 4.3 Check if the block can be connected to the master
		boolean[][] depend = new boolean[size][size];
		ArrayList<LinkedList<Pair<Integer, Pair<LSUStorageBlockEntity, Integer>>>> canConnectList = new ArrayList<>(size);
		LinkedList<Pair<Integer, Pair<LSUStorageBlockEntity, Integer>>> canConnect;
		BlockPos linkPos;
		for (int i = 0; i < size; i++) {
			visited = visitedList.get(i);
			canConnect = new LinkedList<>();
			checkUnused:
			for (Pair<LSUStorageBlockEntity, Integer> pair : unusedList.get(i)) {
				lsu_storage = pair.getLeft();
				linkPos = lsu_storage.pos.offset(DIRECTIONS[pair.getRight()]);
				if (!visited.containsKey(linkPos)) {
					// 4.4 Check if the block is connected to another branch
					for (int j = 0; j < size; j++) {
						if (j != i && visitedList.get(j).containsKey(linkPos)) {
							if (!depend[i][j]) {
								depend[i][j] = true;
								canConnect.add(Pair.of(j, pair));
							}
							continue checkUnused;
						}
					}

					// 4.5 Branch passes check
					if (fastGetLSUS(linkPos).master == master) {
						depend[i][i] = true;
						canConnect.addFirst(Pair.of(i, pair));
						break;
					}
				}
			}
			canConnectList.add(canConnect);
		}

		// 4.6 A branch that can reach master lets branches that depend on it also reach master
		LinkedList<Integer> indexList = new LinkedList<>();
		for (int i = 0; i < size; i++) indexList.add(i);
		while (!indexList.isEmpty()) {
			int i = indexList.poll();
			if (depend[i][i]) {
				for (int j = 0; j < size; j++) {
					if (i != j && depend[j][i] && !depend[j][j]) {
						depend[j][j] = true;
						indexList.add(j);
					}
				}
			}
		}

		// 4.7 According to whether the branch can reach the master for processing
		Pair<LSUStorageBlockEntity, Integer> param;
		LSUStorageBlockEntity child, parent;
		int index;
		for (int i = 0, j; i < size; i++) {
			for (Pair<Integer, Pair<LSUStorageBlockEntity, Integer>> pair : canConnectList.get(i)) {
				j = pair.getLeft();
				if (j != i && !depend[j][j]) {
					continue;
				}

				// 4.8 Connect the block
				param = pair.getRight();
				lsu_storage = param.getLeft();
				index = param.getRight();
				link_lsu_storage = lsu_storage.fastGetLSUS(DIRECTIONS[index]);
				link_lsu_storage.links |= OPP_FLAGS[index];

				// 4.9 Change route direction from current location to start point
				visited = visitedList.get(i);
				child = lsu_storage;
				parent = visited.get(child.pos);
				while (parent != null) {
					for (int k = 0; k < DIRECTIONS_LENGTH; k++) {
						if ((parent.links & FLAGS[k]) != 0 && parent.pos.offset(DIRECTIONS[k]).equals(child.pos)) {
							parent.links ^= FLAGS[k];
							child.links |= OPP_FLAGS[k];
							break;
						}
					}
					child = parent;
					parent = visited.get(parent.pos);
				}

				// 4.10 Branch passes check
				depend[i][i] = true;
				break;
			}

			// 4.11 Failure to find a block connected to the master will delete all connected blocks
			if (!depend[i][i]) {
				for (LSUStorageBlockEntity blockEntity : canDeleteList.get(i)) {
					blockEntity.master = null;
					blockEntity.links = 0b000000;
				}
				count -= canDeleteList.get(i).size();
			}
		}
		master.setConnectedBlocksNum(count);
	}

	public LSUStorageBlockEntity fastGetLSUS(BlockPos pos) {
		assert world != null;
		return (LSUStorageBlockEntity) world.getBlockEntity(pos);
	}

	public LSUStorageBlockEntity fastGetLSUS(Direction direction) {
		assert world != null;
		return (LSUStorageBlockEntity) world.getBlockEntity(pos.offset(direction));
	}

	public BlockPos posOffset(Direction direction) {
		return pos.offset(direction);
	}

	public void addTo(HashSet<BlockPos> visited) {
		visited.add(pos);
	}

	@Override
	public void writeNbt(NbtCompound tagCompound, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(tagCompound, registryLookup);
		tagCompound.putByte("neighbors", neighbors);
	}

	@Override
	public void readNbt(NbtCompound tagCompound, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(tagCompound, registryLookup);
		if (tagCompound.contains("neighbors")) {
			neighbors = tagCompound.getByte("neighbors");
		} else {
			// Compatible with older versions: judge not initialized
			neighbors = (byte) 0b10111111;
		}
	}

	// MachineBaseBlockEntity
	@Override
	public void onLoad() {
		super.onLoad();
		// Compatible with older versions: initialize neighbors
		if (world != null && !world.isClient && ((neighbors & 0b10000000) != 0)) {
			neighbors = 0b000000;
			for (int i = 0; i < DIRECTIONS_LENGTH; i++) {
				if (world.getBlockEntity(pos.offset(DIRECTIONS[i])) instanceof LSUStorageBlockEntity) {
					neighbors |= FLAGS[i];
				}
			}
			markDirty();
		}
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(PlayerEntity entityPlayer) {
		return TRContent.Machine.LSU_STORAGE.getStack();
	}
}
