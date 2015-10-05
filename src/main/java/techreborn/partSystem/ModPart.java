/*
 * This file was made by modmuss50. View the licence file to see what licence this is is on. You can always ask me if you would like to use part or all of this file in your project.
 */

package techreborn.partSystem;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.lib.Location;

/**
 * Extend this class to make your multipart
 */
public abstract class ModPart extends TileEntity implements IModPart {

    /**
     * The world of the part
     */
    public World world;

    /**
     * The location of the part
     */
    public Location location;

    /**
     * This is the world
     */
    @Override
    public World getWorld() {

        return world;
    }

    /**
     * This sets the world Don't use this unless you know what you are doing.
     */
    public void setWorld(World world) {
        this.world = world;
        setWorldObj(world);
    }

    /**
     * Gets the x position in the world
     */
    @Override
    public int getX() {
        return location.getX();
    }

    /**
     * Gets the y position in the world
     */
    @Override
    public int getY() {
        return location.getY();
    }

    /**
     * Gets the z position in the world
     */
    @Override
    public int getZ() {
        return location.getZ();
    }

    /**
     * Gets the location of the part
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the x position in the world
     */
    public void setLocation(Location location) {
        this.location = location;
        this.xCoord = location.getX();
        this.yCoord = location.getY();
        this.zCoord = location.getZ();
    }

    @Override
    public World getWorldObj() {
        return getWorld();
    }

    @Override
    public void setWorldObj(World p_145834_1_) {
        super.setWorldObj(p_145834_1_);
    }

    @Override
    public String getItemTextureName() {
        return "";
    }

    @Override
    public boolean needsItem() {
        return true;
    }
}
