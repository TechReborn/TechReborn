package techreborn.items;

import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
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

public class ItemParts extends Item implements IReactorComponent {
    public static ItemStack getPartByName(String name, int count) {
        for (int i = 0; i < types.length; i++) {
            if (types[i].equalsIgnoreCase(name)) {
                return new ItemStack(ModItems.parts, count, i);
            }
        }
        throw new InvalidParameterException("The part " + name + " could not be found.");
    }

    public static ItemStack getPartByName(String name) {
        return getPartByName(name, 1);
    }

    public static final String[] types = new String[]
            {"advancedCircuitParts", "basicCircuitBoard", "advancedCircuitBoard", "processorCircuitBoard",
                    "energyFlowCircuit", "dataControlCircuit", "dataOrb", "dataStorageCircuit",
                    "diamondGrindingHead", "diamondSawBlade", "tungstenGrindingHead",
                    "heliumCoolantSimple", "HeliumCoolantTriple", "HeliumCoolantSix",
                    "NaKCoolantSimple", "NaKCoolantTriple", "NaKCoolantSix",
                    "cupronickelHeatingCoil", "nichromeHeatingCoil", "kanthalHeatingCoil",
                    "laserFocus", "ductTape", "lazuriteChunk", "iridiumAlloyIngot", "rockCutterBlade", "superConductor",
                    "thoriumCell", "doubleThoriumCell", "quadThoriumCell", "plutoniumCell", "doublePlutoniumCell",
                    "quadPlutoniumCell", "destructoPack", "iridiumNeutronReflector", "massHoleDevice", "computerMonitor"
                    , "machineParts"};

    private IIcon[] textures;

    public ItemParts() {
        setCreativeTab(TechRebornCreativeTab.instance);
        setHasSubtypes(true);
        setUnlocalizedName("techreborn.part");
    }

    @Override
    // Registers Textures For All Dusts
    public void registerIcons(IIconRegister iconRegister) {
        textures = new IIcon[types.length];

        for (int i = 0; i < types.length; ++i) {
            textures[i] = iconRegister.registerIcon("techreborn:" + "component/"
                    + types[i]);
        }
    }

    @Override
    // Adds Texture what match's meta data
    public IIcon getIconFromDamage(int meta) {
        if (meta < 0 || meta >= textures.length) {
            meta = 0;
        }

        return textures[meta];
    }

    @Override
    // gets Unlocalized Name depending on meta data
    public String getUnlocalizedName(ItemStack itemStack) {
        int meta = itemStack.getItemDamage();
        if (meta < 0 || meta >= types.length) {
            meta = 0;
        }

        return super.getUnlocalizedName() + "." + types[meta];
    }

    // Adds Dusts SubItems To Creative Tab
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        for (int meta = 0; meta < types.length; ++meta) {
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


    @Override
    public void processChamber(IReactor iReactor, ItemStack itemStack, int i, int i1, boolean b) {

    }

    @Override
    public boolean acceptUraniumPulse(IReactor iReactor, ItemStack itemStack, ItemStack itemStack1, int i, int i1, int i2, int i3, boolean b) {
        return false;
    }

    @Override
    public boolean canStoreHeat(IReactor iReactor, ItemStack itemStack, int i, int i1) {
        return false;
    }

    @Override
    public int getMaxHeat(IReactor iReactor, ItemStack itemStack, int i, int i1) {
        return 0;
    }

    @Override
    public int getCurrentHeat(IReactor iReactor, ItemStack itemStack, int i, int i1) {
        return 0;
    }

    @Override
    public int alterHeat(IReactor iReactor, ItemStack itemStack, int i, int i1, int i2) {
        return 0;
    }

    @Override
    public float influenceExplosion(IReactor iReactor, ItemStack itemStack) {
        return 0;
    }
}
