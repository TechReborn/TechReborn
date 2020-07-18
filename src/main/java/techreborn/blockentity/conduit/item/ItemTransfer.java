package techreborn.blockentity.conduit.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;
import reborncore.api.systems.conduit.BaseConduitTransfer;

public class ItemTransfer extends BaseConduitTransfer<ItemStack> {

	public ItemTransfer(ItemStack stored, int duration, Direction origin, Direction target) {
		super(stored, duration, origin, target);
	}

	public ItemTransfer() {
	}

	public void fromTag(CompoundTag tag) {
		this.fromTagBase(tag);

		this.setStored(ItemStack.fromTag(tag));
	}

	public CompoundTag toTag(CompoundTag tag) {
		toTagBase(tag);

		getStored().toTag(tag);

		return tag;
	}

	@Override
	public boolean isEmpty() {
		return getStored().isEmpty();
	}
}
