package techreborn.lib;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.util.EnumHelper;

public class Reference
{
	public static ToolMaterial BRONZE = EnumHelper.addToolMaterial("BRONZE", 2, 375, 6.0F, 2.25F, 8);
	public static ArmorMaterial BRONZE_ARMOUR = EnumHelper.addArmorMaterial("BRONZE", ModInfo.MOD_ID+":bronze", 17, new int[] {3,6,5,2}, 8, null/*TODO: SoundEvent*/);
	
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
