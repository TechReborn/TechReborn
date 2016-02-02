package techreborn.items.tools;

import me.modmuss50.jsonDestroyer.api.ITexturedBucket;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraftforge.fluids.Fluid;
import reborncore.RebornCore;
import techreborn.blocks.fluid.BlockFluidBase;
import techreborn.client.TechRebornCreativeTabMisc;

public class ItemFluidbucket extends ItemBucket implements ITexturedBucket {
    private String iconName;

    Fluid containedFluid;

    public ItemFluidbucket(BlockFluidBase block) {
        super(block);
        setContainerItem(Items.bucket);
        setCreativeTab(TechRebornCreativeTabMisc.instance);
        setUnlocalizedName("techreborn.fluidbucket");
        RebornCore.jsonDestroyer.registerObject(this);
        containedFluid = block.getFluid();
    }

    @Override
    public Item setUnlocalizedName(String par1Str) {
        iconName = par1Str;
        return super.setUnlocalizedName(par1Str);
    }


    @Override
    public boolean isGas(int damage) {
        return false;
    }

    @Override
    public Fluid getFluid(int damage) {
        return containedFluid;
    }

    @Override
    public int getMaxMeta() {
        return 1;
    }
}
