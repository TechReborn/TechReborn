/*
 * This file was made by modmuss50. View the licence file to see what licence this is is on. You can always ask me if you would like to use part or all of this file in your project.
 */

package me.modmuss50.network.partSystem.fmp;


import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.CommonMicroblock;
import codechicken.microblock.ISidedHollowConnect;
import codechicken.multipart.JNormalOcclusion;
import codechicken.multipart.NormalOcclusionTest;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TSlottedPart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.modmuss50.mods.lib.Location;
import me.modmuss50.mods.lib.vecmath.Vecs3d;
import me.modmuss50.mods.lib.vecmath.Vecs3dCube;
import me.modmuss50.network.partSystem.ModPart;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FMPModPart extends TMultiPart implements TSlottedPart, JNormalOcclusion, ISidedHollowConnect {

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

	public void addCollisionBoxesToList(List<Vecs3dCube> l, AxisAlignedBB bounds, Entity entity) {
		List<Vecs3dCube> boxes = new ArrayList<Vecs3dCube>();
		List<Vecs3dCube> boxes_ = new ArrayList<Vecs3dCube>();
		iModPart.addCollisionBoxesToList(boxes_, entity);
		for (Vecs3dCube c : boxes_) {
			Vecs3dCube cube = c.clone();
			cube.add(getX(), getY(), getZ());
			boxes.add(cube);
		}
		boxes_.clear();

		for (Vecs3dCube c : boxes) {
			if (c.toAABB().intersectsWith(bounds))
				l.add(c);
		}
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
		if(iModPart.location != null){
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
}
