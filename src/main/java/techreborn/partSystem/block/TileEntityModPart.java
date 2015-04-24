/*
 * This file was made by modmuss50. View the licence file to see what licence this is is on. You can always ask me if you would like to use part or all of this file in your project.
 */

package techreborn.partSystem.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import techreborn.lib.Location;
import techreborn.lib.vecmath.Vecs3dCube;
import techreborn.partSystem.ModPart;
import techreborn.partSystem.ModPartRegistry;
import techreborn.partSystem.parts.NullPart;

public class TileEntityModPart extends TileEntity {

	private Map<String, ModPart> parts = new HashMap<String, ModPart>();

	public void addCollisionBoxesToList(List<Vecs3dCube> l,
			AxisAlignedBB bounds, Entity entity)
	{
		if (getParts().size() == 0)
			addPart(new NullPart());
		List<Vecs3dCube> boxes2 = new ArrayList<Vecs3dCube>();
		for (ModPart mp : getParts())
		{
			List<Vecs3dCube> boxes = new ArrayList<Vecs3dCube>();
			mp.addCollisionBoxesToList(boxes, entity);

			for (int i = 0; i < boxes.size(); i++)
			{
				Vecs3dCube cube = boxes.get(i).clone();
				cube.add(getX(), getY(), getZ());
				boxes2.add(cube);
			}
		}

		for (Vecs3dCube c : boxes2)
		{
			// if (c.toAABB().intersectsWith(bounds))
			l.add(c);
		}

	}

	public List<Vecs3dCube> getOcclusionBoxes()
	{
		List<Vecs3dCube> boxes = new ArrayList<Vecs3dCube>();
		for (ModPart mp : getParts())
		{
			boxes.addAll(mp.getOcclusionBoxes());
		}
		return boxes;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{

		return AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1,
				yCoord + 1, zCoord + 1);
	}

	public World getWorld()
	{
		return getWorldObj();
	}

	public int getX()
	{
		return xCoord;
	}

	public int getY()
	{
		return yCoord;
	}

	public int getZ()
	{
		return zCoord;
	}

	@Override
	public void updateEntity()
	{
		if (parts.isEmpty())
		{
			worldObj.setBlockToAir(xCoord, yCoord, zCoord);
		}
		for (ModPart mp : getParts())
		{
			if (mp.world != null && mp.location != null)
			{
				mp.tick();
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{

		super.writeToNBT(tag);

		NBTTagList l = new NBTTagList();
		writeParts(l, false);
		tag.setTag("parts", l);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{

		super.readFromNBT(tag);

		NBTTagList l = tag.getTagList("parts", new NBTTagCompound().getId());
		try
		{
			readParts(l);
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		} catch (InstantiationException e)
		{
			e.printStackTrace();
		}
	}

	private void writeParts(NBTTagList l, boolean update)
	{

		for (ModPart p : getParts())
		{
			String id = getIdentifier(p);

			NBTTagCompound tag = new NBTTagCompound();

			tag.setString("id", id);
			tag.setString("type", p.getName());
			NBTTagCompound data = new NBTTagCompound();
			p.writeToNBT(data);
			tag.setTag("data", data);

			l.appendTag(tag);
		}
	}

	private void readParts(NBTTagList l) throws IllegalAccessException,
			InstantiationException
	{

		for (int i = 0; i < l.tagCount(); i++)
		{
			NBTTagCompound tag = l.getCompoundTagAt(i);

			String id = tag.getString("id");
			ModPart p = getPart(id);
			if (p == null)
			{
				for (ModPart modPart : ModPartRegistry.parts)
				{
					if (modPart.getName().equals(id))
					{
						p = modPart.getClass().newInstance();
					}
				}
				if (p == null)
					continue;
				addPart(p);
			}

			NBTTagCompound data = tag.getCompoundTag("data");
			p.readFromNBT(data);
		}
	}

	public void addPart(ModPart modPart)
	{
		try
		{
			ModPart newPart = modPart.getClass().newInstance();
			newPart.setWorld(getWorldObj());
			newPart.setLocation(new Location(xCoord, yCoord, zCoord));
			parts.put(newPart.getName(), newPart);
		} catch (InstantiationException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}

	private ModPart getPart(String id)
	{
		for (String s : parts.keySet())
			if (s.equals(id))
				return parts.get(s);

		return null;
	}

	private String getIdentifier(ModPart part)
	{
		for (String s : parts.keySet())
			if (parts.get(s).equals(part))
				return s;

		return null;
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.func_148857_g());
	}

	public List<ModPart> getParts()
	{
		List<ModPart> parts = new ArrayList<ModPart>();
		for (String s : this.parts.keySet())
		{
			ModPart p = this.parts.get(s);
			parts.add(p);
		}
		return parts;
	}

	public List<String> getPartsByName()
	{
		List<String> parts = new ArrayList<String>();
		for (String s : this.parts.keySet())
		{
			parts.add(s);
		}
		return parts;
	}

	public boolean canAddPart(ModPart modpart)
	{
		List<Vecs3dCube> cubes = new ArrayList<Vecs3dCube>();
		modpart.addCollisionBoxesToList(cubes, null);
		for (Vecs3dCube c : cubes)
			if (c != null
					&& !getWorld().checkNoEntityCollision(
							c.clone().add(getX(), getY(), getZ()).toAABB()))
				return false;

		List<Vecs3dCube> l = getOcclusionBoxes();
		for (Vecs3dCube b : modpart.getOcclusionBoxes())
			for (Vecs3dCube c : l)
				if (c != null && b != null
						&& b.toAABB().intersectsWith(c.toAABB()))
					return false;
		return true;
	}

}
