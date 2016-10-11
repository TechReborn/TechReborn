package techreborn.api;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.util.EnumHelper;

/**
 * This contains some static stuff used in recipes and other things
 */
//TODO move this out of the api, and make it nicer
public class Reference {

	private static final Class<?>[] ARMOR_PARAMETERS = { String.class, int.class, int[].class, int.class, SoundEvent.class, float.class };
	public static ArmorMaterial BRONZE_ARMOUR = EnumHelper.addEnum(ArmorMaterial.class, "BRONZE", ARMOR_PARAMETERS, "techreborn:bronze", 17, new int[] { 3, 6, 5,
		2 }, 8, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0F);
	public static ArmorMaterial RUBY_ARMOUR = EnumHelper.addEnum(ArmorMaterial.class, "RUBY", ARMOR_PARAMETERS, "techreborn:ruby", 16, new int[] { 2, 7, 5,
		2 }, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0F);
	public static ArmorMaterial SAPPHIRE_ARMOUR = EnumHelper.addEnum(ArmorMaterial.class, "SAPPHIRE", ARMOR_PARAMETERS, "techreborn:sapphire", 19, new int[] { 4, 4, 4,
		4 }, 8, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0F);
	public static ArmorMaterial PERIDOT_ARMOUR = EnumHelper.addEnum(ArmorMaterial.class, "PERIDOT", ARMOR_PARAMETERS, "techreborn:peridot", 17, new int[] { 3, 8, 3,
		2 }, 16, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0F);

	public static ToolMaterial BRONZE = EnumHelper.addToolMaterial("BRONZE", 2, 375, 6.0F, 2.25F, 8);

	public static ToolMaterial RUBY = EnumHelper.addToolMaterial("RUBY", 2, 320, 6.2F, 2.7F, 10);

	public static ToolMaterial SAPPHIRE = EnumHelper.addToolMaterial("SAPPHIRE", 2, 620, 5.0F, 2F, 8);

	public static ToolMaterial PERIDOT = EnumHelper.addToolMaterial("PERIDOT", 2, 400, 7.0F, 2.4F, 16);

	public static String alloySmelteRecipe = I18n.translateToLocal("techreborn.recipe.alloysmelter");
	public static String assemblingMachineRecipe = I18n.translateToLocal("techreborn.recipe.assemblingmachine");
	public static String blastFurnaceRecipe = I18n.translateToLocal("techreborn.recipe.blastfurnace");
	public static String centrifugeRecipe = I18n.translateToLocal("techreborn.recipe.centrifuge");
	public static String chemicalReactorRecipe = I18n.translateToLocal("techreborn.recipe.chemicalReactor");
	public static String industrialGrinderRecipe = I18n.translateToLocal("techreborn.recipe.grinder");
	public static String implosionCompressorRecipe = I18n.translateToLocal("techreborn.recipe.implosioncompressor");
	public static String industrialElectrolyzerRecipe = I18n
		.translateToLocal("techreborn.recipe.industrialelectrolyzer");
	public static String industrialSawmillRecipe = I18n.translateToLocal("techreborn.recipe.industrialsawmill");
	public static String latheRecipe = I18n.translateToLocal("techreborn.recipe.lathe");
	public static String plateCuttingMachineRecipe = I18n.translateToLocal("techreborn.recipe.platecuttingmachine");
	public static String vacuumFreezerRecipe = I18n.translateToLocal("tile.techreborn.vacuumfreezer.name");
	public static String grinderRecipe = I18n.translateToLocal("tile.techreborn.grinder.name");
	public static String extractorRecipe = I18n.translateToLocal("tile.techreborn.extractor.name");
	public static String compressorRecipe = I18n.translateToLocal("tile.techreborn.compressor.name");
	public static String recyclerRecipe = I18n.translateToLocal("tile.techreborn.recycler.name");
	public static String scrapboxRecipe = I18n.translateToLocal("techreborn.recipe.scrapbox");
}
