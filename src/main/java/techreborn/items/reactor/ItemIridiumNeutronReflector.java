package techreborn.items.reactor;

import net.minecraft.item.ItemStack;

import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import net.minecraftforge.fml.common.Optional;

/**
 * @author estebes
 */
@Optional.Interface(iface = "ic2.api.reactor.IReactor", modid = "ic2")
@Optional.Interface(iface = "ic2.api.reactor.IReactorComponent", modid = "ic2")
public class ItemIridiumNeutronReflector extends ItemReactorComponent {
	// Constructor >>
	public ItemIridiumNeutronReflector() {
		super("iridium_neutron_reflector");
	}
	// << Constructor

	// IReactorComponent >>
	@Optional.Method(modid = "ic2")
	@Override
	public boolean acceptUraniumPulse(ItemStack stack, IReactor reactor, ItemStack pulsingStack, int youX, int youY, int pulseX, int pulseY, boolean heatrun) {
		if (!heatrun) {
			IReactorComponent source = (IReactorComponent) pulsingStack.getItem();
			// apply a pulse at the source
			source.acceptUraniumPulse(pulsingStack, reactor, stack, pulseX, pulseY, youX, youY, heatrun);
		}

		return true;
	}

	@Optional.Method(modid = "ic2")
	@Override
	public float influenceExplosion(ItemStack stack, IReactor reactor) {
		return -1;
	}
	// << IReactorComponent
}
