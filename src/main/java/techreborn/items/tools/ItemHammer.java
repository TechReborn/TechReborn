package techreborn.items.tools;

import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import techreborn.init.ModSounds;
import techreborn.items.ItemTR;
import techreborn.lib.ModInfo;

public class ItemHammer extends ItemTR implements ITexturedItem
{
	private String iconName;

	public ItemHammer(int MaxDamage)
	{
		setUnlocalizedName("techreborn.hammer");
		setMaxDamage(MaxDamage);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		world.playSound(player, player.posX, player.posY,
				player.posZ, ModSounds.dismantle,
				SoundCategory.BLOCKS, 0.6F, 1F);
		return EnumActionResult.SUCCESS;
	}

	@Override
	public Item setUnlocalizedName(String par1Str)
	{
		iconName = par1Str;
		return super.setUnlocalizedName(par1Str);
	}

	@Override
	public boolean getShareTag()
	{
		return true;
	}

	@Override
	public ItemStack getContainerItem(ItemStack itemStack)
	{
		ItemStack copiedStack = itemStack.copy();

		copiedStack.setItemDamage(copiedStack.getItemDamage() + 1);
		copiedStack.stackSize = 1;

		return copiedStack;
	}

	@Override
	public String getTextureName(int damage)
	{
		return "techreborn:items/tool/hammer";
	}

	@Override
	public int getMaxMeta()
	{
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining)
	{
		return new ModelResourceLocation(ModInfo.MOD_ID + ":" + getUnlocalizedName(stack).substring(5), "inventory");
	}
}
