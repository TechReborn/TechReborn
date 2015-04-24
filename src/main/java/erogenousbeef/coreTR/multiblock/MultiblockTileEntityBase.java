package erogenousbeef.coreTR.multiblock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.chunk.IChunkProvider;
import erogenousbeef.coreTR.common.BeefCoreLog;
import erogenousbeef.coreTR.common.CoordTriplet;

/**
 * Base logic class for Multiblock-connected tile entities. Most multiblock
 * machines should derive from this and implement their game logic in certain
 * abstract methods.
 */
public abstract class MultiblockTileEntityBase extends IMultiblockPart {
	private MultiblockControllerBase controller;
	private boolean visited;

	private boolean saveMultiblockData;
	private NBTTagCompound cachedMultiblockData;
	private boolean paused;

	public MultiblockTileEntityBase()
	{
		super();
		controller = null;
		visited = false;
		saveMultiblockData = false;
		paused = false;
		cachedMultiblockData = null;
	}

	// /// Multiblock Connection Base Logic
	@Override
	public Set<MultiblockControllerBase> attachToNeighbors()
	{
		Set<MultiblockControllerBase> controllers = null;
		MultiblockControllerBase bestController = null;

		// Look for a compatible controller in our neighboring parts.
		IMultiblockPart[] partsToCheck = getNeighboringParts();
		for (IMultiblockPart neighborPart : partsToCheck)
		{
			if (neighborPart.isConnected())
			{
				MultiblockControllerBase candidate = neighborPart
						.getMultiblockController();
				if (!candidate.getClass().equals(
						this.getMultiblockControllerType()))
				{
					// Skip multiblocks with incompatible types
					continue;
				}

				if (controllers == null)
				{
					controllers = new HashSet<MultiblockControllerBase>();
					bestController = candidate;
				} else if (!controllers.contains(candidate)
						&& candidate.shouldConsume(bestController))
				{
					bestController = candidate;
				}

				controllers.add(candidate);
			}
		}

		// If we've located a valid neighboring controller, attach to it.
		if (bestController != null)
		{
			// attachBlock will call onAttached, which will set the controller.
			this.controller = bestController;
			bestController.attachBlock(this);
		}

		return controllers;
	}

	@Override
	public void assertDetached()
	{
		if (this.controller != null)
		{
			BeefCoreLog
					.info("[assert] Part @ (%d, %d, %d) should be detached already, but detected that it was not. This is not a fatal error, and will be repaired, but is unusual.",
							xCoord, yCoord, zCoord);
			this.controller = null;
		}
	}

	// /// Overrides from base TileEntity methods

	@Override
	public void readFromNBT(NBTTagCompound data)
	{
		super.readFromNBT(data);

		// We can't directly initialize a multiblock controller yet, so we cache
		// the data here until
		// we receive a validate() call, which creates the controller and hands
		// off the cached data.
		if (data.hasKey("multiblockData"))
		{
			this.cachedMultiblockData = data.getCompoundTag("multiblockData");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound data)
	{
		super.writeToNBT(data);

		if (isMultiblockSaveDelegate() && isConnected())
		{
			NBTTagCompound multiblockData = new NBTTagCompound();
			this.controller.writeToNBT(multiblockData);
			data.setTag("multiblockData", multiblockData);
		}
	}

	/**
	 * Generally, TileEntities that are part of a multiblock should not
	 * subscribe to updates from the main game loop. Instead, you should have
	 * lists of TileEntities which need to be notified during an update() in
	 * your Controller and perform callbacks from there.
	 * 
	 * @see net.minecraft.tileentity.TileEntity#canUpdate()
	 */
	@Override
	public boolean canUpdate()
	{
		return false;
	}

	/**
	 * Called when a block is removed by game actions, such as a player breaking
	 * the block or the block being changed into another block.
	 * 
	 * @see net.minecraft.tileentity.TileEntity#invalidate()
	 */
	@Override
	public void invalidate()
	{
		super.invalidate();
		detachSelf(false);
	}

	/**
	 * Called from Minecraft's tile entity loop, after all tile entities have
	 * been ticked, as the chunk in which this tile entity is contained is
	 * unloading. Happens before the Forge TickEnd event.
	 * 
	 * @see net.minecraft.tileentity.TileEntity#onChunkUnload()
	 */
	@Override
	public void onChunkUnload()
	{
		super.onChunkUnload();
		detachSelf(true);
	}

	/**
	 * This is called when a block is being marked as valid by the chunk, but
	 * has not yet fully been placed into the world's TileEntity cache.
	 * this.worldObj, xCoord, yCoord and zCoord have been initialized, but any
	 * attempts to read data about the world can cause infinite loops - if you
	 * call getTileEntity on this TileEntity's coordinate from within
	 * validate(), you will blow your call stack.
	 * 
	 * TL;DR: Here there be dragons.
	 * 
	 * @see net.minecraft.tileentity.TileEntity#validate()
	 */
	@Override
	public void validate()
	{
		super.validate();
		MultiblockRegistry.onPartAdded(this.worldObj, this);
	}

	// Network Communication
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound packetData = new NBTTagCompound();
		encodeDescriptionPacket(packetData);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0,
				packetData);
	}

	@Override
	public void onDataPacket(NetworkManager network,
			S35PacketUpdateTileEntity packet)
	{
		decodeDescriptionPacket(packet.func_148857_g());
	}

	// /// Things to override in most implementations (IMultiblockPart)
	/**
	 * Override this to easily modify the description packet's data without
	 * having to worry about sending the packet itself. Decode this data in
	 * decodeDescriptionPacket.
	 * 
	 * @param packetData
	 *            An NBT compound tag into which you should write your custom
	 *            description data.
	 * @see erogenousbeef.coreTR.multiblock.MultiblockTileEntityBase#decodeDescriptionPacket(NBTTagCompound)
	 */
	protected void encodeDescriptionPacket(NBTTagCompound packetData)
	{
		if (this.isMultiblockSaveDelegate() && isConnected())
		{
			NBTTagCompound tag = new NBTTagCompound();
			getMultiblockController().formatDescriptionPacket(tag);
			packetData.setTag("multiblockData", tag);
		}
	}

	/**
	 * Override this to easily read in data from a TileEntity's description
	 * packet. Encoded in encodeDescriptionPacket.
	 * 
	 * @param packetData
	 *            The NBT data from the tile entity's description packet.
	 * @see erogenousbeef.coreTR.multiblock.MultiblockTileEntityBase#encodeDescriptionPacket(NBTTagCompound)
	 */
	protected void decodeDescriptionPacket(NBTTagCompound packetData)
	{
		if (packetData.hasKey("multiblockData"))
		{
			NBTTagCompound tag = packetData.getCompoundTag("multiblockData");
			if (isConnected())
			{
				getMultiblockController().decodeDescriptionPacket(tag);
			} else
			{
				// This part hasn't been added to a machine yet, so cache the
				// data.
				this.cachedMultiblockData = tag;
			}
		}
	}

	@Override
	public boolean hasMultiblockSaveData()
	{
		return this.cachedMultiblockData != null;
	}

	@Override
	public NBTTagCompound getMultiblockSaveData()
	{
		return this.cachedMultiblockData;
	}

	@Override
	public void onMultiblockDataAssimilated()
	{
		this.cachedMultiblockData = null;
	}

	// /// Game logic callbacks (IMultiblockPart)

	@Override
	public abstract void onMachineAssembled(
			MultiblockControllerBase multiblockControllerBase);

	@Override
	public abstract void onMachineBroken();

	@Override
	public abstract void onMachineActivated();

	@Override
	public abstract void onMachineDeactivated();

	// /// Miscellaneous multiblock-assembly callbacks and support methods
	// (IMultiblockPart)

	@Override
	public boolean isConnected()
	{
		return (controller != null);
	}

	@Override
	public MultiblockControllerBase getMultiblockController()
	{
		return controller;
	}

	@Override
	public CoordTriplet getWorldLocation()
	{
		return new CoordTriplet(this.xCoord, this.yCoord, this.zCoord);
	}

	@Override
	public void becomeMultiblockSaveDelegate()
	{
		this.saveMultiblockData = true;
	}

	@Override
	public void forfeitMultiblockSaveDelegate()
	{
		this.saveMultiblockData = false;
	}

	@Override
	public boolean isMultiblockSaveDelegate()
	{
		return this.saveMultiblockData;
	}

	@Override
	public void setUnvisited()
	{
		this.visited = false;
	}

	@Override
	public void setVisited()
	{
		this.visited = true;
	}

	@Override
	public boolean isVisited()
	{
		return this.visited;
	}

	@Override
	public void onAssimilated(MultiblockControllerBase newController)
	{
		assert (this.controller != newController);
		this.controller = newController;
	}

	@Override
	public void onAttached(MultiblockControllerBase newController)
	{
		this.controller = newController;
	}

	@Override
	public void onDetached(MultiblockControllerBase oldController)
	{
		this.controller = null;
	}

	@Override
	public abstract MultiblockControllerBase createNewMultiblock();

	@Override
	public IMultiblockPart[] getNeighboringParts()
	{
		CoordTriplet[] neighbors = new CoordTriplet[]
		{ new CoordTriplet(this.xCoord - 1, this.yCoord, this.zCoord),
				new CoordTriplet(this.xCoord, this.yCoord - 1, this.zCoord),
				new CoordTriplet(this.xCoord, this.yCoord, this.zCoord - 1),
				new CoordTriplet(this.xCoord, this.yCoord, this.zCoord + 1),
				new CoordTriplet(this.xCoord, this.yCoord + 1, this.zCoord),
				new CoordTriplet(this.xCoord + 1, this.yCoord, this.zCoord) };

		TileEntity te;
		List<IMultiblockPart> neighborParts = new ArrayList<IMultiblockPart>();
		IChunkProvider chunkProvider = worldObj.getChunkProvider();
		for (CoordTriplet neighbor : neighbors)
		{
			if (!chunkProvider.chunkExists(neighbor.getChunkX(),
					neighbor.getChunkZ()))
			{
				// Chunk not loaded, skip it.
				continue;
			}

			te = this.worldObj
					.getTileEntity(neighbor.x, neighbor.y, neighbor.z);
			if (te instanceof IMultiblockPart)
			{
				neighborParts.add((IMultiblockPart) te);
			}
		}
		IMultiblockPart[] tmp = new IMultiblockPart[neighborParts.size()];
		return neighborParts.toArray(tmp);
	}

	@Override
	public void onOrphaned(MultiblockControllerBase controller, int oldSize,
			int newSize)
	{
		this.markDirty();
		worldObj.markTileEntityChunkModified(xCoord, yCoord, zCoord, this);
	}

	// // Helper functions for notifying neighboring blocks
	protected void notifyNeighborsOfBlockChange()
	{
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord,
				getBlockType());
	}

	protected void notifyNeighborsOfTileChange()
	{
		worldObj.func_147453_f(xCoord, yCoord, zCoord, getBlockType());
	}

	// /// Private/Protected Logic Helpers
	/*
	 * Detaches this block from its controller. Calls detachBlock() and clears
	 * the controller member.
	 */
	protected void detachSelf(boolean chunkUnloading)
	{
		if (this.controller != null)
		{
			// Clean part out of controller
			this.controller.detachBlock(this, chunkUnloading);

			// The above should call onDetached, but, just in case...
			this.controller = null;
		}

		// Clean part out of lists in the registry
		MultiblockRegistry.onPartRemovedFromWorld(worldObj, this);
	}
}
