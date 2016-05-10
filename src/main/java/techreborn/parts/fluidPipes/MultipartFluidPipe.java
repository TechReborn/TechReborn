package techreborn.parts.fluidPipes;

import com.mojang.realmsclient.gui.ChatFormatting;
import mcmultipart.MCMultiPartMod;
import mcmultipart.microblock.IMicroblock;
import mcmultipart.multipart.*;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import reborncore.common.misc.Functions;
import reborncore.common.misc.vecmath.Vecs3dCube;
import reborncore.common.util.Tank;
import reborncore.common.util.WorldUtils;
import techreborn.parts.TechRebornParts;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by modmuss50 on 09/05/2016.
 */
public class MultipartFluidPipe extends Multipart implements INormallyOccludingPart, ISlottedPart, ITickable
{

	public static final IUnlistedProperty<Boolean> UP = Properties.toUnlisted(PropertyBool.create("up"));
	public static final IUnlistedProperty<Boolean> DOWN = Properties.toUnlisted(PropertyBool.create("down"));
	public static final IUnlistedProperty<Boolean> NORTH = Properties.toUnlisted(PropertyBool.create("north"));
	public static final IUnlistedProperty<Boolean> EAST = Properties.toUnlisted(PropertyBool.create("east"));
	public static final IUnlistedProperty<Boolean> SOUTH = Properties.toUnlisted(PropertyBool.create("south"));
	public static final IUnlistedProperty<Boolean> WEST = Properties.toUnlisted(PropertyBool.create("west"));
	public static final IProperty<EnumFluidPipeTypes> TYPE = PropertyEnum.create("type", EnumFluidPipeTypes.class);
	public static final double thickness = 11;
	public Vecs3dCube[] boundingBoxes = new Vecs3dCube[14];
	public float center = 0.6F;
	public float offset = 0.10F;
	public Map<EnumFacing, BlockPos> connectedSides;
	EnumFluidPipeTypes currentType = EnumFluidPipeTypes.EMPTY;
	Tank tank = new Tank("MultipartFluidPipe", 1000, null);

	public MultipartFluidPipe()
	{
		connectedSides = new HashMap<>();
		refreshBounding();
	}

	public static MultipartFluidPipe getPartFromWorld(World world, BlockPos pos, EnumFacing side)
	{
		if (world == null || pos == null)
		{
			return null;
		}
		IMultipartContainer container = MultipartHelper.getPartContainer(world, pos);
		if (side != null && container != null)
		{
			ISlottedPart slottedPart = container.getPartInSlot(PartSlot.getFaceSlot(side));
			if (slottedPart instanceof IMicroblock.IFaceMicroblock && !((IMicroblock.IFaceMicroblock) slottedPart)
					.isFaceHollow())
			{
				return null;
			}
		}

		if (container == null)
		{
			return null;
		}
		ISlottedPart part = container.getPartInSlot(PartSlot.CENTER);
		if (part instanceof MultipartFluidPipe)
		{
			return (MultipartFluidPipe) part;
		}
		return null;
	}

	public void refreshBounding()
	{
		float centerFirst = center - offset;
		double w = (thickness / 16) - 0.5;
		boundingBoxes[6] = new Vecs3dCube(centerFirst - w, centerFirst - w, centerFirst - w, centerFirst + w,
				centerFirst + w, centerFirst + w);

		int i = 0;
		for (EnumFacing dir : EnumFacing.VALUES)
		{
			double xMin1 = (dir.getFrontOffsetX() < 0 ?
					0.0 :
					(dir.getFrontOffsetX() == 0 ? centerFirst - w : centerFirst + w));
			double xMax1 = (dir.getFrontOffsetX() > 0 ?
					1.0 :
					(dir.getFrontOffsetX() == 0 ? centerFirst + w : centerFirst - w));

			double yMin1 = (dir.getFrontOffsetY() < 0 ?
					0.0 :
					(dir.getFrontOffsetY() == 0 ? centerFirst - w : centerFirst + w));
			double yMax1 = (dir.getFrontOffsetY() > 0 ?
					1.0 :
					(dir.getFrontOffsetY() == 0 ? centerFirst + w : centerFirst - w));

			double zMin1 = (dir.getFrontOffsetZ() < 0 ?
					0.0 :
					(dir.getFrontOffsetZ() == 0 ? centerFirst - w : centerFirst + w));
			double zMax1 = (dir.getFrontOffsetZ() > 0 ?
					1.0 :
					(dir.getFrontOffsetZ() == 0 ? centerFirst + w : centerFirst - w));

			boundingBoxes[i] = new Vecs3dCube(xMin1, yMin1, zMin1, xMax1, yMax1, zMax1);
			i++;
		}
	}

	@Override public void addCollisionBoxes(AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
	{

		for (EnumFacing dir : EnumFacing.VALUES)
		{
			if (connectedSides.containsKey(dir) && mask
					.intersectsWith(boundingBoxes[Functions.getIntDirFromDirection(dir)].toAABB()))
				list.add(boundingBoxes[Functions.getIntDirFromDirection(dir)].toAABB());
		}
		if (mask.intersectsWith(boundingBoxes[6].toAABB()))
		{
			list.add(boundingBoxes[6].toAABB());
		}
		super.addCollisionBoxes(mask, list, collidingEntity);
	}

	@Override public void addSelectionBoxes(List<AxisAlignedBB> list)
	{

		for (EnumFacing dir : EnumFacing.VALUES)
		{
			if (connectedSides.containsKey(dir))
				list.add(boundingBoxes[Functions.getIntDirFromDirection(dir)].toAABB());
		}
		list.add(boundingBoxes[6].toAABB());
		super.addSelectionBoxes(list);
	}

	@Override public void onRemoved()
	{
		super.onRemoved();
		for (EnumFacing dir : EnumFacing.VALUES)
		{
			MultipartFluidPipe multipart = getPartFromWorld(getWorld(), getPos().offset(dir), dir);
			if (multipart != null)
			{
				multipart.nearByChange();
			}
		}
	}

	@Override public void addOcclusionBoxes(List<AxisAlignedBB> list)
	{
		for (EnumFacing dir : EnumFacing.VALUES)
		{
			if (connectedSides.containsKey(dir))
				list.add(boundingBoxes[Functions.getIntDirFromDirection(dir)].toAABB());
		}
		list.add(boundingBoxes[6].toAABB());
	}

	@Override public void onNeighborBlockChange(Block block)
	{
		super.onNeighborBlockChange(block);
		nearByChange();

	}

	public void nearByChange()
	{
		checkConnectedSides();
		for (EnumFacing direction : EnumFacing.VALUES)
		{
			BlockPos blockPos = getPos().offset(direction);
			WorldUtils.updateBlock(getWorld(), blockPos);
			MultipartFluidPipe part = getPartFromWorld(getWorld(), blockPos, direction);
			if (part != null)
			{
				part.checkConnectedSides();
			}
		}
	}

	@Override public void onAdded()
	{
		nearByChange();
	}

	public boolean shouldConnectTo(EnumFacing dir)
	{
		if (dir != null)
		{
			if (internalShouldConnectTo(dir))
			{
				MultipartFluidPipe multipartFluidPipe = getPartFromWorld(getWorld(), getPos().offset(dir), dir);
				if (multipartFluidPipe != null && multipartFluidPipe.internalShouldConnectTo(dir.getOpposite()))
				{
					return true;
				}
			} else
			{
				TileEntity tile = getNeighbourTile(dir);

				if (tile instanceof IFluidHandler && (currentType == EnumFluidPipeTypes.EXTRACT
						|| currentType == EnumFluidPipeTypes.INSERT))
				{
					return true;
				}
			}
		}
		return false;
	}

	public boolean internalShouldConnectTo(EnumFacing dir)
	{
		ISlottedPart part = getContainer().getPartInSlot(PartSlot.getFaceSlot(dir));
		if (part instanceof IMicroblock.IFaceMicroblock)
		{
			if (!((IMicroblock.IFaceMicroblock) part).isFaceHollow())
			{
				return false;
			}
		}

		if (!OcclusionHelper.occlusionTest(getContainer().getParts(), p -> p == this,
				boundingBoxes[Functions.getIntDirFromDirection(dir)].toAABB()))
		{
			return false;
		}

		MultipartFluidPipe multipartFluidPipe = getPartFromWorld(getWorld(), getPos().offset(dir), dir.getOpposite());

		return multipartFluidPipe != null;
	}

	public TileEntity getNeighbourTile(EnumFacing side)
	{
		return side != null ? getWorld().getTileEntity(getPos().offset(side)) : null;
	}

	public void checkConnectedSides()
	{
		refreshBounding();
		connectedSides = new HashMap<>();
		for (EnumFacing dir : EnumFacing.values())
		{
			int d = Functions.getIntDirFromDirection(dir);
			if (getWorld() == null)
			{
				return;
			}
			TileEntity te = getNeighbourTile(dir);
			if (shouldConnectTo(dir))
			{
				connectedSides.put(dir, te.getPos());
			}
		}
	}

	@Override public EnumSet<PartSlot> getSlotMask()
	{
		return EnumSet.of(PartSlot.CENTER);
	}

	@Override public IBlockState getExtendedState(IBlockState state)
	{
		IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;
		return extendedBlockState.withProperty(DOWN, shouldConnectTo(EnumFacing.DOWN))
				.withProperty(UP, shouldConnectTo(EnumFacing.UP)).withProperty(NORTH, shouldConnectTo(EnumFacing.NORTH))
				.withProperty(SOUTH, shouldConnectTo(EnumFacing.SOUTH))
				.withProperty(WEST, shouldConnectTo(EnumFacing.WEST))
				.withProperty(EAST, shouldConnectTo(EnumFacing.EAST)).withProperty(TYPE, currentType);
	}

	@Override public BlockStateContainer createBlockState()
	{
		return new ExtendedBlockState(MCMultiPartMod.multipart, new IProperty[] { TYPE },
				new IUnlistedProperty[] { DOWN, UP, NORTH, SOUTH, WEST, EAST });
	}

	@Override public float getHardness(PartMOP hit)
	{
		return 0.5F;
	}

	public Material getMaterial()
	{
		return Material.CLOTH;
	}

	@Override public List<ItemStack> getDrops()
	{
		List<ItemStack> list = new ArrayList<>();
		list.add(new ItemStack(TechRebornParts.fluidPipe, 1, 0));
		return list;
	}

	@Override public ResourceLocation getModelPath()
	{
		return new ResourceLocation("techreborn:fluidpipe");
	}

	@Override public boolean canRenderInLayer(BlockRenderLayer layer)
	{
		return layer == BlockRenderLayer.CUTOUT;
	}

	@Override public void update()
	{
		if (!getWorld().isRemote)
		{
			for (EnumFacing dir : EnumFacing.VALUES)
			{
				if (connectedSides.containsKey(dir))
				{
					if (currentType != EnumFluidPipeTypes.EMPTY)
					{
						MultipartFluidPipe fluidPipe = getPartFromWorld(getWorld(), getPos().offset(dir), dir);
						if (fluidPipe != null)
						{
							if (!fluidPipe.tank.isFull() && (fluidPipe.tank.getFluid() == null
									|| fluidPipe.tank.getFluid().getFluid() == tank.getFluid().getFluid()))
							{
								int sharedAmount = (fluidPipe.tank.getFluidAmount() + tank.getFluidAmount()) / 2;
								fluidPipe.tank.setFluidAmount(sharedAmount);
								tank.setFluidAmount(sharedAmount);
							}
						}
					}
				}
				TileEntity tileEntity = getNeighbourTile(dir);
				if (tileEntity != null)
				{
					if (tileEntity instanceof IFluidHandler)
					{
						IFluidHandler handler = (IFluidHandler) tileEntity;
						if (currentType == EnumFluidPipeTypes.EXTRACT)
						{
							if (!tank.isFull())
							{
								FluidTankInfo[] fluidTankInfos = handler.getTankInfo(dir.getOpposite());
								if (fluidTankInfos.length > 1)
								{
									FluidTankInfo info = fluidTankInfos[1];
									if (tank.getFluid() == null
											|| info.fluid.getFluid() == tank.getFluid().getFluid() && handler
											.canDrain(dir.getOpposite(), tank.getFluid().getFluid()))
									{
										int amountToMove = Math.min(100, tank.getCapacity() - tank.getFluidAmount());
										tank.fill(handler.drain(dir.getOpposite(), amountToMove, false), false);
									}
								}
							}
						} else if (currentType == EnumFluidPipeTypes.INSERT)
						{
							if (!tank.isEmpty())
							{
								FluidTankInfo[] fluidTankInfos = handler.getTankInfo(dir.getOpposite());
								if (fluidTankInfos.length > 1)
								{
									FluidTankInfo info = fluidTankInfos[1];
									if (info.fluid.getFluid() == null
											|| info.fluid.getFluid() == tank.getFluid().getFluid() && handler
											.canFill(dir.getOpposite(), info.fluid.getFluid()))
									{
										int amountToMove = Math.min(100, info.capacity - info.fluid.amount);
										tank.fill(handler.drain(dir.getOpposite(), amountToMove, false), false);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@Override public boolean onActivated(EntityPlayer player, EnumHand hand, ItemStack heldItem, PartMOP hit)
	{
		//TODO make only wrench able to change mode, shift-click with wrench picks up pipe, and click with empty hand displays current mode in chat (doesn't change it)

		//TODO move to client side only class
		if (getWorld().isRemote)
		{
			try
			{
				GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
				Field field = chat.getClass().getDeclaredField("chatLines");
				field.setAccessible(true);
				List<ChatLine> lines = (List<ChatLine>) field.get(chat);
				List<Integer> linesToRemove = new ArrayList<>();
				for (ChatLine line : lines)
				{
					if (line.getChatComponent() instanceof TextComponetValue)
					{
						linesToRemove.add(line.getChatLineID());
					}
				}
				for (Integer integer : linesToRemove)
				{
					chat.deleteChatLine(integer);
				}
			} catch (NoSuchFieldException e)
			{
				e.printStackTrace();
			} catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}

		//TODO sync this with the server/client
		if (currentType == EnumFluidPipeTypes.EMPTY)
		{
			currentType = EnumFluidPipeTypes.EXTRACT;
			if (getWorld().isRemote)
				player.addChatMessage(new TextComponetValue("Mode set to: " + ChatFormatting.GOLD + "Extract"));
		} else if (currentType == EnumFluidPipeTypes.EXTRACT)
		{
			currentType = EnumFluidPipeTypes.INSERT;
			if (getWorld().isRemote)
				player.addChatMessage(new TextComponetValue("Mode set to: " + ChatFormatting.BLUE + "Insert"));
		} else if (currentType == EnumFluidPipeTypes.INSERT)
		{
			currentType = EnumFluidPipeTypes.EMPTY;
			if (getWorld().isRemote)
				player.addChatMessage(new TextComponetValue("Mode set to: " + ChatFormatting.DARK_GRAY + "Normal"));
		}

		markRenderUpdate();
		return true;
	}

	@Override public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		tank.writeToNBT(tag);
		tag.setString("mode", currentType.getName());
	}

	@Override public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		tank.readFromNBT(tag);
		String mode = tag.getString("mode");
		if (mode.equals(EnumFluidPipeTypes.EMPTY))
		{
			currentType = EnumFluidPipeTypes.EMPTY;
		} else if (mode.equals(EnumFluidPipeTypes.INSERT))
		{
			currentType = EnumFluidPipeTypes.INSERT;
		} else if (mode.equals(EnumFluidPipeTypes.EXTRACT))
		{
			currentType = EnumFluidPipeTypes.EXTRACT;
		}
	}

	public class TextComponetValue extends TextComponentString
	{

		public TextComponetValue(String msg)
		{
			super(msg);
		}
	}
}
