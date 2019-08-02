package techreborn.items.reactor;

import net.minecraft.item.ItemStack;

import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import net.minecraftforge.fml.common.Optional;

import techreborn.items.ItemTR;

/**
 * @author estebes
 */
@Optional.Interface(iface = "ic2.api.reactor.IReactor", modid = "ic2")
@Optional.Interface(iface = "ic2.api.reactor.IReactorComponent", modid = "ic2")
public abstract class ItemReactorComponent extends ItemTR implements IReactorComponent {
//	public static final String[] types = new String[] { "energy_flow_circuit", "data_control_circuit", "data_storage_circuit",
//		"data_orb", "diamond_grinding_head", "diamond_saw_blade", "tungsten_grinding_head", "helium_coolant_simple",
//		"helium_coolant_triple", "helium_coolant_six", "nak_coolant_simple", "nak_coolant_triple", "nak_coolant_six",
//		"cupronickel_heating_coil", "nichrome_heating_coil", "kanthal_heating_coil", ModItems.META_PLACEHOLDER, "super_conductor",
//		"thorium_cell", "double_thorium_cell", "quad_thorium_cell", "plutonium_cell", "double_plutonium_cell",
//		"quad_plutonium_cell", "computer_monitor", "machine_parts", "neutron_reflector", "iridium_neutron_reflector",
//		"thick_neutron_reflector", "electronic_circuit", "advanced_circuit", "sap", "rubber", "scrap",
//		"carbon_mesh", "carbon_fiber", "coolant_simple", "coolant_triple", "coolant_six", "enhanced_super_conductor", "basic_circuit_board",
//		"advanced_circuit_board", "advanced_circuit_parts", "processor_circuit_board" };

	// Constructor >>
	public ItemReactorComponent(String name) {
		setUnlocalizedName("techreborn." + name);
	}
	// << Constructor

	// IReactorComponent >>
	@Optional.Method(modid = "ic2")
	@Override
	public void processChamber(ItemStack stack, IReactor reactor, int x, int y, boolean heatrun) {
		return;
	}

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
}
