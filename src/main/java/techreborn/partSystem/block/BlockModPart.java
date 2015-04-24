/*
 * This file was made by modmuss50. View the licence file to see what licence this is is on. You can always ask me if you would like to use part or all of this file in your project.
 */

package techreborn.partSystem.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import techreborn.lib.vecmath.Vecs3dCube;
import techreborn.partSystem.ICustomHighlight;
import techreborn.partSystem.IModPart;
import techreborn.partSystem.ModPart;

/**
 * Created by mark on 10/12/14.
 */
public class BlockModPart extends BlockContainer implements ICustomHighlight {

	public BlockModPart(Material met)
	{
		super(met);
	}

	public static TileEntityModPart get(IBlockAccess world, int x, int y, int z)
	{

		TileEntity te = world.getTileEntity(x, y, z);
		if (te == null)
			return null;
		if (!(te instanceof TileEntityModPart))
			return null;
		return (TileEntityModPart) te;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i)
	{
		return new TileEntityModPart();
	}

	@SuppressWarnings(
	{ "rawtypes", "unchecked" })
	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z,
			AxisAlignedBB bounds, List l, Entity entity)
	{
		TileEntityModPart te = get(world, x, y, z);
		if (te == null)
			return;

		List<Vecs3dCube> boxes = new ArrayList<Vecs3dCube>();
		te.addCollisionBoxesToList(boxes, bounds, entity);
		for (Vecs3dCube c : boxes)
			l.add(c.toAABB());
	}

	@Override
	public boolean isOpaqueCube()
	{

		return false;
	}

	@Override
	public int getRenderType()
	{

		return -1;
	}

	@Override
	// TODO move to array list
	public ArrayList<AxisAlignedBB> getBoxes(World world, int x, int y, int z,
			EntityPlayer player)
	{
		TileEntityModPart te = get(world, x, y, z);
		if (te == null)
			return null;

		List<Vecs3dCube> boxes = new ArrayList<Vecs3dCube>();
		ArrayList<AxisAlignedBB> list = new ArrayList<AxisAlignedBB>();
		if (!te.getParts().isEmpty())
		{
			for (ModPart modPart : te.getParts())
				boxes.addAll(modPart.getSelectionBoxes());
			for (int i = 0; i < boxes.size(); i++)
			{
				Vecs3dCube cube = boxes.get(i);
				list.add(cube.toAABB());
			}
		}

		return list;
	}

	public void onNeighborBlockChange(World world, int x, int y, int z,
			Block block)
	{
		for (ModPart part : get(world, x, y, z).getParts())
		{
			part.nearByChange();
		}
	}

	@Override
	public MovingObjectPosition collisionRayTrace(World wrd, int x, int y,
			int z, Vec3 origin, Vec3 direction)
	{
		ArrayList<AxisAlignedBB> aabbs = getBoxes(wrd, x, y, z, null);
		MovingObjectPosition closest = null;
		for (AxisAlignedBB aabb : aabbs)
		{
			MovingObjectPosition mop = aabb.getOffsetBoundingBox(x, y, z)
					.calculateIntercept(origin, direction);
			if (mop != null)
			{
				if (closest != null
						&& mop.hitVec.distanceTo(origin) < closest.hitVec
								.distanceTo(origin))
				{
					closest = mop;
				} else
				{
					closest = mop;
				}
			}
		}
		if (closest != null)
		{
			closest.blockX = x;
			closest.blockY = y;
			closest.blockZ = z;
		}
		return closest;
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		for (ModPart part : get(world, x, y, z).getParts())
		{
			part.onAdded();
		}
		super.onBlockAdded(world, x, y, z);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block oldid,
			int oldmeta)
	{
		for (ModPart part : get(world, x, y, z).getParts())
		{
			part.onRemoved();
		}
		super.breakBlock(world, x, y, z, oldid, oldmeta);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
			int metadata, int fortune)
	{
		ArrayList<ItemStack> l = new ArrayList<ItemStack>();
		TileEntityModPart te = get(world, x, y, z);
		if (te != null)
		{
			for (IModPart p : te.getParts())
			{
				ItemStack item = p.getItem();
				if (item != null)
					l.add(item);
			}
		}
		return l;
	}
}
