/**
 * 
 */
package techreborn.init;

import java.util.Arrays;

import com.google.common.base.CaseFormat;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.RebornRegistry;
import techreborn.items.ItemNuggets;
import techreborn.lib.ModInfo;

/**
 * @author drcrazy
 *
 */
public enum ModNuggets implements IStringSerializable {
	ALUMINUM, BRASS, BRONZE, CHROME, COPPER, DIAMOND, ELECTRUM, HOT_TUNGSTENSTEEL, INVAR, IRIDIUM, LEAD, NICKEL,
	PLATINUM, REFINED_IRON, SILVER, STEEL, TIN, TITANIUM, TUNGSTEN, TUNGSTENSTEEL, ZINC;

	public final String name;
	public final Item item;
	
	private ModNuggets() {
		name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "NUGGET_" + this.toString());
		item = new ItemNuggets();
		item.setRegistryName(new ResourceLocation(ModInfo.MOD_ID, name));
		item.setTranslationKey(ModInfo.MOD_ID + "." + name);
	}
	
	public ItemStack getStack() {
		return new ItemStack(item);
	}

	public static void register() {
		Arrays.stream(ModNuggets.values()).forEach(nugget -> RebornRegistry.registerItem(nugget.item));
	}

	@SideOnly(Side.CLIENT)
	public static void registerModel() {
		ResourceLocation blockstateJson = new ResourceLocation(ModInfo.MOD_ID, "items/materials/nuggets");
		Arrays.stream(ModNuggets.values()).forEach(nugget -> ModelLoader.setCustomModelResourceLocation(nugget.item, 0,
				new ModelResourceLocation(blockstateJson, "type=" + nugget.name)));
	}

	@Override
	public String getName() {
		return name;
	}

}
