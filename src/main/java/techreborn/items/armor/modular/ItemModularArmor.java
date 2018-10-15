package techreborn.items.armor.modular;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.apache.commons.lang3.Validate;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.ItemUtils;
import techreborn.api.armor.ArmorSlot;
import techreborn.api.armor.ModularArmorUtils;
import techreborn.items.armor.ItemTRArmor;

import javax.annotation.Nullable;
import java.util.Objects;

public class ItemModularArmor extends ItemTRArmor {

	public int upgradeSlots;

	public ItemModularArmor(ArmorMaterial material, ArmorSlot slot, int upgradeSlots) {
		super(material, slot.getEntityEquipmentSlot());
		this.upgradeSlots = upgradeSlots;
	}

	public ModularArmorManager getManager(ItemStack stack) {
		return new ModularArmorManager(stack, upgradeSlots);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack,
	                                            @Nullable
		                                            NBTTagCompound nbt) {
		return getManager(stack);
	}

	//TODO anyway to do the following in events?

	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1 - ItemUtils.getPowerForDurabilityBar(stack);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		return PowerSystem.getDisplayPower().colour;
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		Validate.isTrue(ModularArmorUtils.isModularArmor(stack));
		Multimap<String, AttributeModifier> attributes = HashMultimap.create();
		if(slot.getSlotType() != EntityEquipmentSlot.Type.ARMOR){
			return attributes;
		}
		ModularArmorUtils.getManager(stack).getAllHolders().stream()
			.filter(holder -> {
				Item item = holder.getArmorStack().getItem();
				if(item instanceof ItemArmor){
					return ((ItemArmor) item).getEquipmentSlot() == slot;
				}
				return false;
			})
			.map(holder -> holder.getUpgrade().getAttributeModifiers(holder))
			.filter(Objects::nonNull)
			.forEach(map -> map.entries().forEach(entry -> {
				if(attributes.containsKey(entry.getKey())){
					//Im not 100% sure I need this, but it seems to fix the issue of the speed uprgades not adding up
					AttributeModifier targetModifier = attributes.get(entry.getKey()).stream().findFirst().orElse(null);
					Validate.notNull(targetModifier);
					attributes.removeAll(entry.getKey());
					attributes.put(entry.getKey(), merge(targetModifier, entry.getValue()));
				} else {
					attributes.put(entry.getKey(), entry.getValue());
				}
			}));
		return attributes;
	}

	//TODO is this the best way to do this?
	private AttributeModifier merge(AttributeModifier m1, AttributeModifier m2){
		return new AttributeModifier(m1.getID(), m1.getName(), m1.getAmount() + m2.getAmount(), 0);
	}

}
