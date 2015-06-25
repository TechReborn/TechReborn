/*
 * This file was made by modmuss50. View the licence file to see what licence this is is on. You can always ask me if you would like to use part or all of this file in your project.
 */

package techreborn.partSystem.QLib;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import techreborn.lib.Location;
import techreborn.lib.vecmath.Vecs3d;
import techreborn.lib.vecmath.Vecs3dCube;
import techreborn.partSystem.ModPart;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartCollidable;
import uk.co.qmunity.lib.part.IPartRenderPlacement;
import uk.co.qmunity.lib.part.IPartSelectable;
import uk.co.qmunity.lib.part.IPartTicking;
import uk.co.qmunity.lib.part.IPartUpdateListener;
import uk.co.qmunity.lib.part.ITilePartHolder;
import uk.co.qmunity.lib.part.PartBase;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.raytrace.RayTracer;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

public class QModPart extends PartBase implements IPartCollidable,
        IPartSelectable, IPartRenderPlacement, IPartTicking,
        IPartUpdateListener {

    ModPart iModPart;

    public QModPart(ModPart iModPart) {
        this.iModPart = iModPart;

    }

    @Override
    public void setParent(ITilePartHolder parent) {
        super.setParent(parent);
    }

    @Override
    public String getType() {
        return iModPart.getName();
    }

    @Override
    public ItemStack getItem() {
        return iModPart.getItem();
    }

    @Override
    public void addCollisionBoxesToList(List<Vec3dCube> boxes, Entity entity) {
        List<Vecs3dCube> cubes = new ArrayList<Vecs3dCube>();
        iModPart.addCollisionBoxesToList(cubes, entity);
        for (Vecs3dCube cube : cubes) {
            if (cube != null)
                boxes.add(ModLib2QLib.convert(cube));
        }
    }

    @Override
    public void renderDynamic(Vec3d translation, double delta, int pass) {
        iModPart.renderDynamic(ModLib2QLib.convert(translation), delta);
    }

    @Override
    public boolean renderStatic(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass) {
        return iModPart.renderStatic(new Vecs3d(translation.getX(), translation.getY(), translation.getZ()), renderer, pass);
    }


    @Override
    public QMovingObjectPosition rayTrace(Vec3d start, Vec3d end) {
        return RayTracer.instance().rayTraceCubes(this, start, end);
    }

    @Override
    public List<Vec3dCube> getSelectionBoxes() {
        return ModLib2QLib.convert2(iModPart.getSelectionBoxes());
    }

    @Override
    public World getWorld() {
        return getParent().getWorld();
    }

    @Override
    public void update() {
        if (iModPart.world == null || iModPart.location == null) {
            iModPart.setWorld(getWorld());
            iModPart.setLocation(new Location(getX(), getY(), getZ()));
        }
        iModPart.tick();
    }

    @Override
    public void onPartChanged(IPart part) {
        iModPart.nearByChange();
    }

    @Override
    public void onNeighborBlockChange() {
        iModPart.nearByChange();
    }

    @Override
    public void onNeighborTileChange() {
        if (iModPart.world == null || iModPart.location == null) {
            iModPart.setWorld(getWorld());
            iModPart.setLocation(new Location(getX(), getY(), getZ()));
        }
        iModPart.nearByChange();
    }

    @Override
    public void onAdded() {
		if (iModPart.world == null || iModPart.location == null) {
			iModPart.setWorld(getWorld());
			iModPart.setLocation(new Location(getX(), getY(), getZ()));
		}
		iModPart.nearByChange();
		iModPart.onAdded();
    }

    @Override
    public void onRemoved() {
        iModPart.onRemoved();
    }

    @Override
    public void onLoaded() {
        if (iModPart.world == null || iModPart.location == null) {
            iModPart.setWorld(getWorld());
            iModPart.setLocation(new Location(getX(), getY(), getZ()));
        }
        iModPart.nearByChange();
    }

    @Override
    public void onUnloaded() {

    }

    @Override
    public void onConverted() {

    }
}
