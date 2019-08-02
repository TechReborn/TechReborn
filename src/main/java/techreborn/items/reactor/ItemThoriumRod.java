package techreborn.items.reactor;

import net.minecraft.item.ItemStack;

import ic2.api.reactor.IReactor;
import net.minecraftforge.fml.common.Optional;

/**
 * @author estebes
 */
@Optional.Interface(iface = "ic2.api.reactor.IReactor", modid = "ic2")
@Optional.Interface(iface = "ic2.api.reactor.IReactorComponent", modid = "ic2")
public abstract class ItemThoriumRod extends ItemDamageableReactorComponent {
	public ItemThoriumRod(String name, int rodCount) {
		super(name, 100000);

		this.rodCount = rodCount;
	}

	// IReactorComponent >>
	@Optional.Method(modid = "ic2")
	@Override
	public void processChamber(ItemStack stack, IReactor reactor, int x, int y, boolean heatrun) {

	}

	@Optional.Method(modid = "ic2")
	@Override
	public boolean acceptUraniumPulse(ItemStack stack, IReactor reactor, ItemStack pulsingStack, int youX, int youY,
	                                  int pulseX, int pulseY, boolean heatrun) {
		if (!heatrun) reactor.addOutput(1f);

		return true;
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
	public float influenceExplosion(ItemStack itemStack, IReactor reactor) {
		return (2.0F / 5.0F) * (float) rodCount;
	}
	// << IReactorComponent

	// Fields >>
	public final int rodCount;
	// << Fields
}
