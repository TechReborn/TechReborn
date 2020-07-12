package techreborn.blockentity.conduit;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;

public class ItemTransfer {
	ItemStack itemStack;

	int progress = 0;
	int duration;

	// Direction which the item came from
	Direction from;

	ItemTransfer(ItemStack stack, int duration, Direction from){
		this.itemStack = stack;
		this.duration = duration;
		this.from = from;
	}

	private ItemTransfer(){

	}

	public void progress(){
		if(!isFinished()) {
			progress++;
		}
	}

	public boolean isFinished(){
		return progress >= duration;
	}

	public float getProgress(){
		return ((float)progress / (float)duration);
	}

	public ItemStack getItemStack(){
		return this.itemStack;
	}


	public static ItemTransfer fromTag(CompoundTag tag) {
		ItemTransfer itemTransfer = new ItemTransfer();

		itemTransfer.itemStack = ItemStack.fromTag(tag);

		itemTransfer.progress = tag.getInt("tickProgress");
		itemTransfer.duration = tag.getInt("tickFinish");

		itemTransfer.from = Direction.byId(tag.getInt("fromDirection"));

		return itemTransfer;
	}

	public CompoundTag toTag(CompoundTag tag) {
		itemStack.toTag(tag);
		tag.putInt("tickProgress", progress);
		tag.putInt("tickFinish", duration);
		tag.putInt("fromDirection", from.getId());

		return tag;
	}

}
