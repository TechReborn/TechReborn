package techreborn.items.tools;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import reborncore.RebornCore;
import techreborn.client.TechRebornCreativeTabMisc;

import java.util.Set;

public class ItemTRAxe extends ItemTool {
	private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(new Block[] { Blocks.PLANKS, Blocks.BOOKSHELF,
		Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK,
		Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE });
	private ToolMaterial material = ToolMaterial.WOOD;

	public ItemTRAxe(ToolMaterial material) {
		super(material, EFFECTIVE_ON);
		this.damageVsEntity = material.getDamageVsEntity() + 5.75F;
		this.attackSpeed = (material.getDamageVsEntity() + 6.75F) * -0.344444F;
		setUnlocalizedName(material.name().toLowerCase() + "Axe");
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		this.material = material;
	}

	public float getStrVsBlock(ItemStack stack, IBlockState state) {
		Material material = state.getMaterial();
		return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE
		       ? super.getStrVsBlock(stack, state) : this.efficiencyOnProperMaterial;
	}
}
