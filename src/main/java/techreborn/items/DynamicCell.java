package techreborn.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.UniversalBucket;
import techreborn.client.TechRebornCreativeTab;
import techreborn.init.ModItems;

/**
 * Created by modmuss50 on 17/05/2016.
 */
public class DynamicCell extends UniversalBucket{

    public DynamicCell() {
        super(1000, new ItemStack(ModItems.emptyCell), false);
        setCreativeTab(TechRebornCreativeTab.instance);
        setUnlocalizedName("techreborn.cellFilled");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemstack, World world, EntityPlayer player, EnumHand hand) {
        return ActionResult.newResult(EnumActionResult.FAIL, itemstack);
    }

    @Override
    public void onFillBucket(FillBucketEvent event) {
        return;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        FluidStack fluidStack = getFluid(stack);
        if (fluidStack == null)
        {
            if(getEmpty() != null)
            {
                return getEmpty().getDisplayName();
            }
            return super.getItemStackDisplayName(stack);
        }

        return fluidStack.getLocalizedName() + " Cell";
    }

    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill)
    {
        // has to be exactly 1, must be handled from the caller
        if (container.stackSize != 1)
        {
            return 0;
        }

        // can only fill exact capacity
        if (resource == null || resource.amount != getCapacity())
        {
            return 0;
        }
        // fill the container
        if (doFill)
        {
            NBTTagCompound tag = container.getTagCompound();
            if (tag == null)
            {
                tag = new NBTTagCompound();
            }
            resource.writeToNBT(tag);
            container.setTagCompound(tag);
        }
        return getCapacity();
    }

    public static ItemStack getCellWithFluid(Fluid fluid, int stackSize){
        ItemStack stack = new ItemStack(ModItems.dynamicCell, stackSize);
        ModItems.dynamicCell.fill(stack, new FluidStack(fluid, ModItems.dynamicCell.getCapacity()), true);
        return stack;
    }

    public static ItemStack getCellWithFluid(Fluid fluid){
        ItemStack stack = new ItemStack(ModItems.dynamicCell);
        ModItems.dynamicCell.fill(stack, new FluidStack(fluid, ModItems.dynamicCell.getCapacity()), true);
        return stack;
    }

}
