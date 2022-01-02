package techreborn.blockentity.storage.energy.msb;

import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import team.reborn.energy.api.EnergyStorage;

public class BatteryEnergyStore extends SnapshotParticipant<Long> implements EnergyStorage {
	private long amount = 0;
	private long capacity;

	private boolean enabled = true;

	@Override
	protected Long createSnapshot() { return amount; }

	@Override
	protected void readSnapshot(Long snapshot) { amount = snapshot; }

	@Override
	public long insert(long maxAmount, TransactionContext transaction) {
		StoragePreconditions.notNegative(maxAmount);

		if (!enabled) {
			return 0;
		}

		long inserted = Math.min(maxAmount, capacity - amount);

		if (inserted > 0) {
			updateSnapshots(transaction);
			amount += inserted;
			return inserted;
		}

		return 0;
	}

	@Override
	public long extract(long maxAmount, TransactionContext transaction) {
		StoragePreconditions.notNegative(maxAmount);

		if (!enabled) {
			return 0;
		}

		long extracted = Math.min(maxAmount, amount);

		if (extracted > 0) {
			updateSnapshots(transaction);
			amount -= extracted;
			return extracted;
		}

		return 0;
	}

	@Override
	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = Math.min(amount, capacity);
	}

	@Override
	public long getCapacity() {
		return capacity;
	}

	protected void setCapacity(long capacity) {
		this.capacity = capacity;
		this.amount = Math.min(amount, capacity);
	}

	public void setEnabled(boolean enabled) { this.enabled = enabled; }

	public boolean isEnabled() { return this.enabled; }

	public EnergyStorage newView(long maxInsert, long maxExtract) {
		return new View(maxInsert, maxExtract);
	}

	private class View implements EnergyStorage {
		private long maxInsert;
		private long maxExtract;

		protected View(long maxInsert, long maxExtract) {
			this.maxInsert = maxInsert;
			this.maxExtract = maxExtract;
		}


		@Override
		public long insert(long maxAmount, TransactionContext transaction) {
			if (maxInsert > 0) {
				return BatteryEnergyStore.this.insert(Math.min(maxAmount, maxInsert), transaction);
			}
			return 0;
		}

		@Override
		public long extract(long maxAmount, TransactionContext transaction) {
			if (maxExtract > 0) {
				return BatteryEnergyStore.this.extract(Math.min(maxAmount, maxExtract), transaction);
			}
			return 0;
		}

		@Override
		public long getAmount() {
			return BatteryEnergyStore.this.getAmount();
		}

		@Override
		public long getCapacity() {
			return BatteryEnergyStore.this.getCapacity();
		}
	}

	public static final EnergyStorage ZERO = new ZeroStore();

	public static class ZeroStore implements EnergyStorage {

		private ZeroStore() {}

		@Override
		public long insert(long maxAmount, TransactionContext transaction) {
			return 0;
		}

		@Override
		public long extract(long maxAmount, TransactionContext transaction) {
			return 0;
		}

		@Override
		public long getAmount() {
			return 0;
		}

		@Override
		public long getCapacity() {
			return 0;
		}
	}
}
