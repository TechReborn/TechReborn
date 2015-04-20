/*
 * This file was made by modmuss50. View the licence file to see what licence this is is on. You can always ask me if you would like to use part or all of this file in your project.
 */

package techreborn.partSystem;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import techreborn.lib.vecmath.Vecs3d;
import techreborn.lib.vecmath.Vecs3dCube;

import java.util.List;


/**
 * This is based of https://github.com/Qmunity/QmunityLib/blob/master/src/main/java/uk/co/qmunity/lib/part/IPart.java
 * <p/>
 * You should not be implementing this.
 */
public interface IModPart {

	/**
	 * Adds all of this part's collision boxes to the list. These boxes can depend on the entity that's colliding with them.
	 */
	public void addCollisionBoxesToList(List<Vecs3dCube> boxes, Entity entity);

	/**
	 * Gets this part's selection boxes.
	 */
	public List<Vecs3dCube> getSelectionBoxes();


	/**
	 * Gets this part's occlusion boxes.
	 */
	public List<Vecs3dCube> getOcclusionBoxes();

	/**
	 * Renders this part dynamically (every render tick).
	 */
	@SideOnly(Side.CLIENT)
	public void renderDynamic(Vecs3d translation, double delta);

	/**
	 * Writes the part's data to an NBT tag, which is saved with the game data.
	 */
	public void writeToNBT(NBTTagCompound tag);

	/**
	 * Reads the part's data from an NBT tag, which was stored in the game data.
	 */
	public void readFromNBT(NBTTagCompound tag);

	/**
	 * Gets the itemstack that places this part.
	 */
	public ItemStack getItem();

	/**
	 * Gets the name of this part.
	 */
	public String getName();

	/**
	 * Gets the world of this part.
	 */
	public World getWorld();

	/**
	 * This is the item texture eg: "network:cable"
	 */
	public String getItemTextureName();

	/**
	 * Gets the X cord of this part.
	 */
	public int getX();

	/**
	 * Gets the Y cord of this part.
	 */
	public int getY();

	/**
	 * Gets the Z cord of this part.
	 */
	public int getZ();

	/**
	 * Called every tick
	 */
	public void tick();

	/**
	 * Called when a block or part has been changed. Can be used for cables to check nearby blocks
	 */
	public void nearByChange();

	public void onAdded();

	public void onRemoved();
}
