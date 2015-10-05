/*
 * This file was made by modmuss50. View the licence file to see what licence this is is on. You can always ask me if you would like to use part or all of this file in your project.
 */

package techreborn.partSystem.fmp;

import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.ISidedHollowConnect;
import codechicken.multipart.JNormalOcclusion;
import codechicken.multipart.NormalOcclusionTest;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TSlottedPart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import techreborn.lib.Location;
import techreborn.lib.vecmath.Vecs3d;
import techreborn.lib.vecmath.Vecs3dCube;
import techreborn.partSystem.IPartDesc;
import techreborn.partSystem.ModPart;
import techreborn.util.LogHelper;

import java.util.ArrayList;
import java.util.List;

public class FMPModPart extends TMultiPart implements TSlottedPart,
        JNormalOcclusion, ISidedHollowConnect {

    ModPart iModPart;

    public FMPModPart(ModPart iModPart) {
        this.iModPart = iModPart;

    }

    @Override
    public int getHollowSize(int i) {
        return 0;
    }

    @Override
    public Iterable<Cuboid6> getOcclusionBoxes() {
        List<Cuboid6> cubes = new ArrayList<Cuboid6>();
        for (Vecs3dCube c : iModPart.getOcclusionBoxes())
            cubes.add(new Cuboid6(c.toAABB()));
        return cubes;
    }

    @Override
    public boolean occlusionTest(TMultiPart npart) {
        return NormalOcclusionTest.apply(this, npart);
    }


    @Override
    public Iterable<Cuboid6> getCollisionBoxes() {
        List<Cuboid6> cubes = new ArrayList<Cuboid6>();
        List<Vecs3dCube> boxes = new ArrayList<Vecs3dCube>();
        iModPart.addCollisionBoxesToList(boxes, null);
        for (Vecs3dCube c : boxes) {
            if (c != null)
                cubes.add(new Cuboid6(c.toAABB()));
        }

        return cubes;
    }

    @Override
    public Iterable<IndexedCuboid6> getSubParts() {
        List<IndexedCuboid6> cubes = new ArrayList<IndexedCuboid6>();
        if (iModPart.getSelectionBoxes() != null) {
            for (Vecs3dCube c : iModPart.getSelectionBoxes())
                if (c != null)
                    cubes.add(new IndexedCuboid6(0, new Cuboid6(c.toAABB())));

            if (cubes.size() == 0)
                cubes.add(new IndexedCuboid6(0, new Cuboid6(0, 0, 0, 1, 1, 1)));

        }
        return cubes;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderDynamic(Vector3 pos, float frame, int pass) {
        iModPart.renderDynamic(new Vecs3d(pos.x, pos.y, pos.z), frame);
    }

    @Override
    public String getType() {
        return iModPart.getName();
    }

    @Override
    public int getSlotMask() {
        return 0;
    }

    public World getWorld() {
        return world();
    }

    public int getX() {
        if (iModPart.world == null || iModPart.location == null) {
            iModPart.setWorld(world());
            iModPart.setLocation(new Location(x(), y(), z()));
        }
        return x();
    }

    public int getY() {
        if (iModPart.world == null || iModPart.location == null) {
            iModPart.setWorld(world());
            iModPart.setLocation(new Location(x(), y(), z()));
        }
        return y();
    }

    public int getZ() {
        if (iModPart.world == null || iModPart.location == null) {
            iModPart.setWorld(world());
            iModPart.setLocation(new Location(x(), y(), z()));
        }
        return z();
    }

    @Override
    public void onAdded() {
        iModPart.setWorld(world());
        iModPart.setLocation(new Location(x(), y(), z()));
        iModPart.nearByChange();
        iModPart.onAdded();
    }

    @Override
    public void update() {
        if (iModPart.location != null) {
            iModPart.tick();
        }
    }

    @Override
    public void onNeighborChanged() {
        super.onNeighborChanged();
        if (iModPart.world == null || iModPart.location == null) {
            iModPart.setWorld(world());
            iModPart.setLocation(new Location(x(), y(), z()));
        }
        iModPart.nearByChange();
    }

    public void onRemoved() {
        iModPart.onRemoved();
        super.onRemoved();

    }

    @Override
    public boolean renderStatic(Vector3 pos, int pass) {
        boolean render;
        render = iModPart.renderStatic(new Vecs3d((int) pos.x, (int) pos.y, (int) pos.z), pass);
        return render;
    }

    @Override
    public Iterable<ItemStack> getDrops() {
        List<ItemStack> stackArrayList = new ArrayList<ItemStack>();
        if (iModPart.getItem() != null) {
            stackArrayList.add(iModPart.getItem().copy());
        } else {
            LogHelper.error("Part " + iModPart.getName() + " has a null drop");
        }
        return stackArrayList;
    }


    @Override
    public void onPartChanged(TMultiPart part) {
        super.onPartChanged(part);
        if (iModPart.world == null || iModPart.location == null) {
            iModPart.setWorld(world());
            iModPart.setLocation(new Location(x(), y(), z()));
        }
        iModPart.nearByChange();
    }

    @Override
    public void readDesc(MCDataInput packet) {
        super.readDesc(packet);
        if (iModPart instanceof IPartDesc) {
            ((IPartDesc) iModPart).readDesc(packet.readNBTTagCompound());
        }
    }

    @Override
    public void writeDesc(MCDataOutput packet) {
        super.writeDesc(packet);
        if (iModPart instanceof IPartDesc) {
            NBTTagCompound tagCompound = new NBTTagCompound();
            ((IPartDesc) iModPart).writeDesc(tagCompound);
            packet.writeNBTTagCompound(tagCompound);
        }
    }
}
