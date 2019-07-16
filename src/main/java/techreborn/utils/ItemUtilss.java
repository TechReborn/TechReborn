package techreborn.utils;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemUtilss {
	/**
	 * Get or create an ItemStack NBT data
	 *
	 * @param stack the itemstack
	 * @return the existing NBT or a new one if it does not exist
	 */
	public static NBTTagCompound getStackNbtData(ItemStack stack) {
		NBTTagCompound ret = stack.getTagCompound();

		if (ret == null) { // does not exist so we need to create it
			ret = new NBTTagCompound();
			stack.setTagCompound(ret);
		}

		return ret;
	}

	/**
	 * Check if a stack is empty
	 *
	 * @param stack the itemstack
	 * @return true if the stack is empty or false otherwise
	 */
	public static boolean isEmpty(ItemStack stack) {
		return stack == ItemStack.EMPTY || stack == null || stack.getCount() <= 0;
	}

	/**
	 * Get the size of a stack
	 *
	 * @param stack the itemstack
	 * @return the size of the stack
	 */
	public static int getSize(ItemStack stack) {
		return isEmpty(stack) ? 0 : stack.getCount();
	}

	/**
	 * Set the size of a stack
	 *
	 * @param stack the itemstack
	 * @param size the new size
	 * @return the resulting stack
	 */
	public static ItemStack setSize(ItemStack stack, int size) {
		if (size <= 0) return ItemStack.EMPTY;

		stack.setCount(size);

		return stack;
	}

	/**
	 * Increase the size of a stack
	 *
	 * @param stack the itemstack
	 * @param amount amount to be increased by
	 * @return the resulting stack
	 */
	public static ItemStack increaseSize(ItemStack stack, int amount) {
		return setSize(stack, getSize(stack) + amount);
	}

	/**
	 * Increase the size of a stack
	 *
	 * @param stack the itemstack
	 * @return the resulting stack
	 */
	public static ItemStack increaseSize(ItemStack stack) {
		return increaseSize(stack, 1);
	}

	/**
	 * Decrease the size of a stack
	 *
	 * @param stack the itemstack
	 * @param amount amount to be decreased by
	 * @return the resulting stack
	 */
	public static ItemStack decreaseSize(ItemStack stack, int amount) {
		return setSize(stack, getSize(stack) - amount);
	}

	/**
	 * Decrease the size of a stack
	 *
	 * @param stack the itemstack
	 * @return the resulting stack
	 */
	public static ItemStack decreaseSize(ItemStack stack) {
		return decreaseSize(stack, 1);
	}

	/**
	 * Drop a stack to the world
	 *
	 * @param world the world
	 * @param pos the position
	 * @param stack the itemstack
	 */
	public static void dropToWorld(World world, BlockPos pos, ItemStack stack) {
		if (isEmpty(stack)) return;

		double f = 0.7;
		double dx = world.rand.nextFloat() * f + (1 - f) * 0.5;
		double dy = world.rand.nextFloat() * f + (1 - f) * 0.5;
		double dz = world.rand.nextFloat() * f + (1 - f) * 0.5;

		EntityItem entityItem = new EntityItem(world, pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz, stack.copy());
		entityItem.setDefaultPickupDelay();
		world.spawnEntity(entityItem);
	}
}
