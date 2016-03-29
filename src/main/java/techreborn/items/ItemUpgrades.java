package techreborn.items;

import java.security.InvalidParameterException;
import java.util.List;

import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import techreborn.api.recipe.RecipeCrafter;
import techreborn.api.upgrade.IMachineUpgrade;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.init.ModItems;

public class ItemUpgrades extends ItemTextureBase implements IMachineUpgrade, ITexturedItem
{

	public static final String[] types = new String[] { "Overclock", "Transformer", "EnergyStorage" };

	public ItemUpgrades()
	{
		setUnlocalizedName("techreborn.upgrade");
		setHasSubtypes(true);
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		setMaxStackSize(1);
	}

	public static ItemStack getUpgradeByName(String name, int count)
	{
		for (int i = 0; i < types.length; i++)
		{
			if (types[i].equalsIgnoreCase(name))
			{
				return new ItemStack(ModItems.plate, count, i);
			}
		}
		throw new InvalidParameterException("The upgrade " + name + " could not be found.");
	}

	public static ItemStack getUpgradeByName(String name)
	{
		return getUpgradeByName(name, 1);
	}

	@Override
	// gets Unlocalized Name depending on meta data
	public String getUnlocalizedName(ItemStack itemStack)
	{
		int meta = itemStack.getItemDamage();
		if (meta < 0 || meta >= types.length)
		{
			meta = 0;
		}

		return super.getUnlocalizedName() + "." + types[meta];
	}

	// Adds Dusts SubItems To Creative Tab
	public void getSubItems(Item item, CreativeTabs creativeTabs, List list)
	{
		for (int meta = 0; meta < types.length; ++meta)
		{
			list.add(new ItemStack(item, 1, meta));
		}
	}

	@Override
	public void processUpgrade(RecipeCrafter crafter, ItemStack stack)
	{
		// Remember the max speed multiplier can only be 0.99!!

		if (stack.getItemDamage() == 0)
		{// Check the meta data here
			crafter.addSpeedMulti(0.2);// This will set the speed multiplier to
										// 0.8
			crafter.addPowerMulti(0.5);// This will use eu/tick x 1.5
			// crafter.addPowerMulti(2); This will use twice the amount of
			// power.
		}
		if (stack.getItemDamage() == 1)
		{
			crafter.addPowerMulti(-0.2);// This will use eu/tick 0.8
		}
		if (stack.getItemDamage() == 2)
		{
			crafter.addSpeedMulti(0.5);
			crafter.addPowerMulti(1);
		}
	}

	@Override
	public int getMaxMeta()
	{
		return types.length;
	}

	@Override
	public String getTextureName(int damage)
	{
		return "techreborn:items/upgrade/upgrade"+types[damage];
	}
}