package techreborn.items.tools;

import ic2.core.item.tool.ItemTreetap;
import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.RebornCore;
import techreborn.client.TechRebornCreativeTab;
import techreborn.compat.CompatManager;
import techreborn.lib.ModInfo;

public class ItemTreeTap extends Item implements ITexturedItem {

	public ItemTreeTap() {
		setMaxStackSize(1);
		setMaxDamage(20);
		setUnlocalizedName("techreborn.treetap");
		RebornCore.jsonDestroyer.registerObject(this);
		setCreativeTab(TechRebornCreativeTab.instance);
	}


	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		IBlockState state = worldIn.getBlockState(pos);
		Block block = state.getBlock();
		if(CompatManager.isIC2Loaded && block == Block.getBlockFromName("ic2:rubber_wood"))  {
			ItemTreetap.attemptExtract(playerIn, worldIn, pos, side, state, null);
			if (!worldIn.isRemote)
				stack.damageItem(1, playerIn);
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return stack.getMetadata() != 0;
	}

	@Override
	public String getTextureName(int damage) {
		return "techreborn:items/tool/treetap";
	}

	@Override
	public int getMaxMeta() {
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining) {
		return new ModelResourceLocation(ModInfo.MOD_ID + ":" + getUnlocalizedName(stack).substring(5), "inventory");
	}
}
