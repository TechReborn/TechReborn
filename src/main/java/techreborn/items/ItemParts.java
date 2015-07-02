package techreborn.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.init.ModItems;

import java.security.InvalidParameterException;
import java.util.List;

public class ItemParts extends Item {
	public static ItemStack getPartByName(String name, int count)
	{
		for (int i = 0; i < types.length; i++) {
			if (types[i].equalsIgnoreCase(name)) {
				return new ItemStack(ModItems.parts, count, i);
			}
		}
		throw new InvalidParameterException("The part " + name + " could not be found.");
	}
	
	public static ItemStack getPartByName(String name)
	{
		return getPartByName(name, 1);
	}
	
	public static final String[] types = new String[]
	{ "advancedCircuitParts", "basicCircuitBoard", "advancedCircuitBoard", "processorCircuitBoard",
			"energyFlowCircuit", "dataControlCircuit", "dataOrb", "dataStorageCircuit",
			"diamondGrindingHead", "diamondSawBlade", "tungstenGrindingHead",
			"heliumCoolantSimple", "HeliumCoolantTriple", "HeliumCoolantSix",
			"NaKCoolantSimple", "NaKCoolantTriple", "NaKCoolantSix",
			"cupronickelHeatingCoil", "nichromeHeatingCoil", "kanthalHeatingCoil",
			"bronzeGear", "ironGear", "titaniumGear", "steelGear", "tungstensteelGear",
			"laserFocus", "ductTape", "lazuriteChunk", "iridiumAlloyIngot", "rockCutterBlade", "superconductor",
			"thoriumCell", "doubleThoriumCell", "quadThoriumCell", "plutoniumCell", "doublePlutoniumCell", 
			"quadPlutoniumCell", "destructoPack", "iridiumNeutronReflector", "massHoleDevice", "computerMonitor" };

	private IIcon[] textures;

	public ItemParts()
	{
		setCreativeTab(TechRebornCreativeTab.instance);
		setHasSubtypes(true);
		setUnlocalizedName("techreborn.part");
	}

	@Override
	// Registers Textures For All Dusts
	public void registerIcons(IIconRegister iconRegister)
	{
		textures = new IIcon[types.length];

		for (int i = 0; i < types.length; ++i)
		{
			textures[i] = iconRegister.registerIcon("techreborn:" + "component/"
					+ types[i]);
		}
	}

	@Override
	// Adds Texture what match's meta data
	public IIcon getIconFromDamage(int meta)
	{
		if (meta < 0 || meta >= textures.length)
		{
			meta = 0;
		}

		return textures[meta];
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
	
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		switch (itemStack.getItemDamage()) {
			case 37: // Destructo pack
				player.openGui(Core.INSTANCE, GuiHandler.destructoPackID, world,
						(int) player.posX, (int) player.posY, (int) player.posY);
				break;
		}
		return itemStack;
	}
}
