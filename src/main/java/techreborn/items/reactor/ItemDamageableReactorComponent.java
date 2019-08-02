package techreborn.items.reactor;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.util.ItemUtils;

import techreborn.items.ItemTR;

import java.util.List;

/**
 * @author estebes
 */
@Optional.Interface(iface = "ic2.api.reactor.IReactor", modid = "ic2")
@Optional.Interface(iface = "ic2.api.reactor.IReactorComponent", modid = "ic2")
public abstract class ItemDamageableReactorComponent extends ItemTR implements IReactorComponent {
	// Constructor >>
	public ItemDamageableReactorComponent(String name, int maxDamage) {
		setUnlocalizedName("techreborn." + name);
		setNoRepair();

		this.maxDamage = maxDamage;
	}
	// << Constructor

	// ItemTR >>
	@Override
	public boolean isDamageable() {
		return true;
	}

	@Override
	public boolean isDamaged(ItemStack stack) {
		return getDamage(stack) > 0;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return (double) getDamage(stack) / (double) getMaxDamage(stack);
	}

	@Override
	public int getDamage(ItemStack stack) {
		NBTTagCompound data = ItemUtils.getStackNbtData(stack);
		return data.hasKey("damage") ? data.getInteger("damage") : 0;
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		return maxDamage;
	}

	@Override
	public void setDamage(ItemStack stack, int damage) {
		NBTTagCompound data = ItemUtils.getStackNbtData(stack);
		data.setInteger("damage", damage);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		super.addInformation(stack, world, tooltip, advanced);

		tooltip.add((getMaxDamage(stack) - getMaxDamage(stack)) + "/" + getMaxDamage(stack));
	}
	// << ItemTR

	// IReactorComponent >>
	@Optional.Method(modid = "ic2")
	@Override
	public abstract void processChamber(ItemStack stack, IReactor reactor, int x, int y, boolean heatrun);

	@Optional.Method(modid = "ic2")
	@Override
	public boolean acceptUraniumPulse(ItemStack stack, IReactor reactor, ItemStack pulsingStack, int youX, int youY,
	                                  int pulseX, int pulseY, boolean heatrun) {
		return false;
	}

	@Optional.Method(modid = "ic2")
	@Override
	public boolean canStoreHeat(ItemStack stack, IReactor reactor, int x, int y) {
		return false;
	}

	@Optional.Method(modid = "ic2")
	@Override
	public int getMaxHeat(ItemStack stack, IReactor reactor, int x, int y) {
		return 0;
	}

	@Optional.Method(modid = "ic2")
	@Override
	public int getCurrentHeat(ItemStack stack, IReactor reactor, int x, int y) {
		return 0;
	}

	@Optional.Method(modid = "ic2")
	@Override
	public int alterHeat(ItemStack stack, IReactor reactor, int x, int y, int heat) {
		return heat;
	}

	@Optional.Method(modid = "ic2")
	@Override
	public float influenceExplosion(ItemStack stack, IReactor reactor) {
		return 0;
	}

	@Optional.Method(modid = "ic2")
	@Override
	public boolean canBePlacedIn(ItemStack itemStack, IReactor reactor) {
		return true;
	}
	// << IReactorComponent

	// Fields >>
	private final int maxDamage;
	// << Fields
}
