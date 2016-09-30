package techreborn.itemblocks;

import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.RebornCore;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.lib.ModInfo;

/**
 * Created by modmuss50 on 20/02/2016.
 */
public class ItemBlockRubberSapling extends ItemBlock implements ITexturedItem {

	public ItemBlockRubberSapling(Block block) {
		super(block);
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		setUnlocalizedName("techreborn.rubberSapling");
		RebornCore.jsonDestroyer.registerObject(this);
	}

	@Override
	public String getTextureName(int damage) {
		return "techreborn:items/misc/rubber_sapling";
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
