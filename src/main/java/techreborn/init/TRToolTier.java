package techreborn.init;

import java.util.function.Supplier;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyLoadBase;

//TODO: Use tags
public enum TRToolTier implements IItemTier {
	BRONZE(2, 375, 6.0f, 2.25f, 8, () -> {
		return Ingredient.fromItems(TRContent.Ingots.BRONZE.asItem());
	}), RUBY(2, 320, 6.2F, 2.7F, 10, () -> {
		return Ingredient.fromItems(TRContent.Gems.RUBY.asItem());
	}), SAPPHIRE(3, 620, 5.0F, 2F, 8, () -> {
		return Ingredient.fromItems(TRContent.Gems.SAPPHIRE.asItem());
	}), PERIDOT(2, 400, 7.0F, 2.4F, 16, () -> {
		return Ingredient.fromItems(TRContent.Gems.PERIDOT.asItem());
	});

	/**
	 * The level of material this tool can harvest (3 = DIAMOND, 2 = IRON, 1 =
	 * STONE, 0 = WOOD/GOLD)
	 */
	private final int harvestLevel;
	/**
	 * The number of uses this material allows. (wood = 59, stone = 131, iron = 250,
	 * diamond = 1561, gold = 32)
	 */
	private final int maxUses;
	/**
	 * The strength of this tool material against blocks which it is effective
	 * against.
	 */
	private final float efficiency;
	/** Damage versus entities. */
	private final float attackDamage;
	/** Defines the natural enchantability factor of the material. */
	private final int enchantability;
	private final LazyLoadBase<Ingredient> repairMaterial;

	private TRToolTier(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn,
			int enchantabilityIn, Supplier<Ingredient> repairMaterialIn) {
		this.harvestLevel = harvestLevelIn;
		this.maxUses = maxUsesIn;
		this.efficiency = efficiencyIn;
		this.attackDamage = attackDamageIn;
		this.enchantability = enchantabilityIn;
		this.repairMaterial = new LazyLoadBase<>(repairMaterialIn);
	}

	@Override
	public int getMaxUses() {
		return maxUses;
	}

	@Override
	public float getEfficiency() {
		return efficiency;
	}

	@Override
	public float getAttackDamage() {
		return attackDamage;
	}

	@Override
	public int getHarvestLevel() {
		return harvestLevel;
	}

	@Override
	public int getEnchantability() {
		return enchantability;
	}

	@Override
	public Ingredient getRepairMaterial() {
		return repairMaterial.getValue();
	}
}
