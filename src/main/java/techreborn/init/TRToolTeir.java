package techreborn.init;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;


//TODO move values over
/*
	public static ToolMaterial BRONZE = EnumHelper.addToolMaterial("BRONZE", 2, 375, 6.0F, 2.25F, 8);
	public static ToolMaterial RUBY = EnumHelper.addToolMaterial("RUBY", 2, 320, 6.2F, 2.7F, 10);
	public static ToolMaterial SAPPHIRE = EnumHelper.addToolMaterial("SAPPHIRE", 2, 620, 5.0F, 2F, 8);
	public static ToolMaterial PERIDOT = EnumHelper.addToolMaterial("PERIDOT", 2, 400, 7.0F, 2.4F, 16);
 */

public enum TRToolTeir implements IItemTier {
	BRONZE,
	RUBY,
	SAPPHIRE,
	PERIDOT;

	@Override
	public int getMaxUses() {
		return 0;
	}

	@Override
	public float getEfficiency() {
		return 0;
	}

	@Override
	public float getAttackDamage() {
		return 0;
	}

	@Override
	public int getHarvestLevel() {
		return 0;
	}

	@Override
	public int getEnchantability() {
		return 0;
	}

	@Override
	public Ingredient getRepairMaterial() {
		return null;
	}
}
