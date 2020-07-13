package techreborn.blockentity.conduit;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;

public class ItemTransfer {
	private ItemStack itemStack;

	private int progress = 0;
	private int duration;

	// Direction which the item came from
	private Direction origin;

	// Used for rendering, not important
	private Direction target;

	ItemTransfer(ItemStack stack, int duration, Direction origin){
		this.itemStack = stack;
		this.duration = duration;
		this.origin = origin;
	}

	ItemTransfer(){

	};

	public void progress(){
		if(!isFinished()) {
			progress++;
		}
	}

	// Helper functions

	public boolean isFinished(){
		return progress >= duration;
	}

	public void restartProgress(){
		progress = 0;
	}

	public float getProgressPercent(){
		return ((float)progress / (float)duration);
	}

	public boolean isEmpty(){
		return itemStack.isEmpty();
	}


	// NBT
	public static ItemTransfer fromTag(CompoundTag tag) {
		ItemTransfer itemTransfer = new ItemTransfer();

		itemTransfer.itemStack = ItemStack.fromTag(tag);

		itemTransfer.progress = tag.getInt("tickProgress");
		itemTransfer.duration = tag.getInt("tickFinish");

		itemTransfer.origin = Direction.byId(tag.getInt("fromDirection"));

		return itemTransfer;
	}

	public CompoundTag toTag(CompoundTag tag) {
		itemStack.toTag(tag);
		tag.putInt("tickProgress", progress);
		tag.putInt("tickFinish", duration);
		tag.putInt("fromDirection", origin.getId());

		return tag;
	}

	// Getter/Setters
	public ItemStack getItemStack(){
		return this.itemStack;
	}

	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	public Direction getOriginDirection() {
		return origin;
	}

	public void setOriginDirection(Direction origin) {
		this.origin = origin;
	}

	public Direction getTargetDirection() {
		return target;
	}

	public void setTargetDirection(Direction target) {
		this.target = target;
	}


}
