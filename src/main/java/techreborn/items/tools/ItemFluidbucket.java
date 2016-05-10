package techreborn.items.tools;

import me.modmuss50.jsonDestroyer.api.ITexturedBucket;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import reborncore.RebornCore;
import techreborn.blocks.fluid.BlockFluidBase;
import techreborn.client.TechRebornCreativeTabMisc;

public class ItemFluidbucket extends ItemBucket implements ITexturedBucket
{
	Fluid containedFluid;
	private String iconName;

	public ItemFluidbucket(BlockFluidBase block)
	{
		super(block);
		setContainerItem(Items.BUCKET);
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		setUnlocalizedName("techreborn.fluidbucket");
		RebornCore.jsonDestroyer.registerObject(this);
		containedFluid = block.getFluid();
	}

	@Override
	public Item setUnlocalizedName(String par1Str)
	{
		iconName = par1Str;
		return super.setUnlocalizedName(par1Str);
	}

	 @Override
	 public boolean isGas(int damage) {
	 return false;
	 }

	 @Override
	 public Fluid getFluid(int damage) {
	 return containedFluid;
	 }

	@Override
	public int getMaxMeta()
	{
		return 1;
	}

	@Override
	public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining) {
		return null;
	}
}
