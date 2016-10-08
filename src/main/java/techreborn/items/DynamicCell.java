package techreborn.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import org.apache.commons.lang3.Validate;
import techreborn.client.TechRebornCreativeTab;
import techreborn.init.ModItems;

import java.util.List;

/**
 * Created by modmuss50 on 17/05/2016.
 */
public class DynamicCell extends Item {

	public static final int CAPACITY = Fluid.BUCKET_VOLUME;

	public DynamicCell() {
		super();
		setCreativeTab(TechRebornCreativeTab.instance);
		setUnlocalizedName("techreborn.cell");
		setMaxStackSize(16);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		//Clearing tag because ItemUtils.isItemEqual doesn't handle tags ForgeCaps and display
		//And breaks ability to use in recipes
		//TODO: Property ItemUtils.isItemEquals tags equality handling?
		if (stack.hasTagCompound()) {
			NBTTagCompound tag = stack.getTagCompound();
			if (tag.getSize() != 1 || tag.hasKey("Fluid")) {
				NBTTagCompound clearTag = new NBTTagCompound();
				clearTag.setTag("Fluid", tag.getCompoundTag("Fluid"));
				stack.setTagCompound(clearTag);
			}
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (!worldIn.isRemote) {
			RayTraceResult result = rayTrace(worldIn, playerIn, true);

			if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
				BlockPos pos = result.getBlockPos();
				IBlockState state = worldIn.getBlockState(pos);
				Block block = state.getBlock();

				if (block instanceof IFluidBlock) {
					IFluidBlock fluidBlock = (IFluidBlock) block;

					if (fluidBlock.canDrain(worldIn, pos)) {
						FluidStack fluid = fluidBlock.drain(worldIn, pos, false);

						if (fluid != null && fluid.amount == DynamicCell.CAPACITY) {
							if (tryAddCellToInventory(playerIn, stack, fluid.getFluid())) {
								fluidBlock.drain(worldIn, pos, true);
								return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
							}
						}

					}

				} else if (block instanceof BlockStaticLiquid) {
					Fluid fluid = block.getMaterial(state) == Material.LAVA ? FluidRegistry.LAVA : FluidRegistry.WATER;

					if (tryAddCellToInventory(playerIn, stack, fluid)) {
						if (fluid != FluidRegistry.WATER)
							worldIn.setBlockToAir(pos);
						return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
					}

				}
			}
		}
		return ActionResult.newResult(EnumActionResult.FAIL, stack);
	}

	public boolean tryAddCellToInventory(EntityPlayer player, ItemStack stack, Fluid fluid) {
		if (player.inventory.addItemStackToInventory(DynamicCell.getCellWithFluid(fluid))) {
			--stack.stackSize;
			return true;
		}
		return false;
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		subItems.add(getEmptyCell(1));
		for (Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {
			subItems.add(getCellWithFluid(fluid));
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		FluidStack fluidStack = getFluidHandler(stack).getFluid();
		if (fluidStack == null)
			return super.getItemStackDisplayName(stack);
		return fluidStack.getLocalizedName() + " Cell";
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return getFluidHandler(stack);
	}

	public static FluidHandler getFluidHandler(ItemStack stack) {
		return new FluidHandler(stack, CAPACITY);
	}

	public static ItemStack getCellWithFluid(Fluid fluid, int stackSize) {
		Validate.notNull(fluid);
		ItemStack stack = new ItemStack(ModItems.dynamicCell);
		getFluidHandler(stack).fill(new FluidStack(fluid, CAPACITY), true);
		stack.stackSize = stackSize;
		return stack;
	}

	public static ItemStack getEmptyCell(int amount) {
		return new ItemStack(ModItems.dynamicCell, amount);
	}

	public static ItemStack getCellWithFluid(Fluid fluid) {
		return getCellWithFluid(fluid, 1);
	}

	public static class FluidHandler extends FluidHandlerItemStack {

		public FluidHandler(ItemStack container, int capacity) {
			super(container, capacity);

			//backwards compatibility
			if (container.hasTagCompound() && container.getTagCompound().hasKey("FluidName")) {
				FluidStack stack = FluidStack.loadFluidStackFromNBT(container.getTagCompound());
				if (stack != null) {
					container.setTagCompound(new NBTTagCompound());
					fill(stack, true);
				}
			}

		}

		@Override
		public int fill(FluidStack resource, boolean doFill) {
			if (resource.amount != capacity)
				return 0;
			return super.fill(resource, doFill);
		}

		@Override
		public FluidStack drain(int maxDrain, boolean doDrain) {
			if (maxDrain != capacity)
				return null;
			return super.drain(maxDrain, doDrain);
		}

	}

}
