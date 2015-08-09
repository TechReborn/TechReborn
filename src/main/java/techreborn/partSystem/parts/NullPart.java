/*
 * This file was made by modmuss50. View the licence file to see what licence this is is on. You can always ask me if you would like to use part or all of this file in your project.
 */

package techreborn.partSystem.parts;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import techreborn.lib.vecmath.Vecs3d;
import techreborn.lib.vecmath.Vecs3dCube;
import techreborn.partSystem.IModPart;
import techreborn.partSystem.ModPart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mark on 11/12/14.
 */
public class NullPart extends ModPart {
    @Override
    public void addCollisionBoxesToList(List<Vecs3dCube> boxes, Entity entity) {
        boxes.add(new Vecs3dCube(0, 0, 0, 1, 1, 1));
    }

    @Override
    public List<Vecs3dCube> getSelectionBoxes() {
        List<Vecs3dCube> cubes = new ArrayList<Vecs3dCube>();
        cubes.add(new Vecs3dCube(0, 0, 0, 1, 1, 1));
        return cubes;
    }

    @Override
    public List<Vecs3dCube> getOcclusionBoxes() {
        return null;
    }

    @Override
    public void renderDynamic(Vecs3d translation, double delta) {

    }

    @Override
    public boolean renderStatic(Vecs3d translation, int pass) {
        return false;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

    }

    @Override
    public ItemStack getItem() {
        return null;
    }

    @Override
    public String getName() {
        return "NullPart";
    }

    @Override
    public String getItemTextureName() {
        return "";
    }

    @Override
    public void tick() {

    }

    @Override
    public void nearByChange() {

    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRemoved() {

    }

    @Override
    public IModPart copy() {
        return new NullPart();
    }
}
