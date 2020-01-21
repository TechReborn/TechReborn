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

import java.util.ArrayList;

public class LesuNetwork {

	public ArrayList<LSUStorageBlockEntity> storages = new ArrayList<>();

	public LapotronicSUBlockEntity master;

	public void addElement(LSUStorageBlockEntity lesuStorage) {
		if (!storages.contains(lesuStorage) && storages.size() < 5000) {
			storages.add(lesuStorage);
		}
	}

	public void removeElement(LSUStorageBlockEntity lesuStorage) {
		storages.remove(lesuStorage);
		rebuild();
	}

	private void rebuild() {
		master = null;
		for (LSUStorageBlockEntity lesuStorage : storages) {
			lesuStorage.findAndJoinNetwork(lesuStorage.getWorld(), lesuStorage.getPos());
		}
	}

	public void merge(LesuNetwork network) {
		if (network != this) {
			ArrayList<LSUStorageBlockEntity> blockEntityLesuStorages = new ArrayList<>(network.storages);
			network.clear(false);
			for (LSUStorageBlockEntity lesuStorage : blockEntityLesuStorages) {
				lesuStorage.setNetwork(this);
			}
			if (network.master != null && this.master == null) {
				this.master = network.master;
			}
		}
	}

	private void clear(boolean clearBlockEntities) {
		if (clearBlockEntities) {
			for (LSUStorageBlockEntity blockEntityLesuStorage : storages) {
				blockEntityLesuStorage.resetNetwork();
			}
		}
		storages.clear();
	}

}
