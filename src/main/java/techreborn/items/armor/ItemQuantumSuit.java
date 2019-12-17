package techreborn.items.armor;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import reborncore.api.items.ArmorFovHandler;
import reborncore.api.items.ArmorRemoveHandler;
import reborncore.api.items.ArmorTickable;
import reborncore.api.items.ItemStackModifiers;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.ItemUtils;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHolder;
import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyTier;
import techreborn.utils.InitUtils;

public class ItemQuantumSuit extends ItemTRArmour implements ItemStackModifiers, ArmorTickable, ArmorRemoveHandler, ArmorFovHandler, EnergyHolder {

	public static final double ENERGY_FLY = 50;
	public static final double ENERGY_SWIM = 20;
	public static final double ENERGY_BREATHING = 20;
	public static final double ENERGY_SPRINTING = 20;

	public ItemQuantumSuit(ArmorMaterial material, EquipmentSlot slot) {
		super(material, slot);
	}

	@Override
	public void getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack, Multimap<String, EntityAttributeModifier> attributes) {
		attributes.removeAll(EntityAttributes.MOVEMENT_SPEED.getId());

		if (this.slot == EquipmentSlot.LEGS && equipmentSlot == EquipmentSlot.LEGS) {
			if (Energy.of(stack).getEnergy() > ENERGY_SPRINTING) {
				attributes.put(EntityAttributes.MOVEMENT_SPEED.getId(), new EntityAttributeModifier(MODIFIERS[equipmentSlot.getEntitySlotId()],"Movement Speed", 0.15, EntityAttributeModifier.Operation.ADDITION));
			}
		}

		if(equipmentSlot == this.slot && Energy.of(stack).getEnergy() > 0) {
			attributes.put(EntityAttributes.ARMOR.getId(), new EntityAttributeModifier(MODIFIERS[slot.getEntitySlotId()], "Armor modifier", 20, EntityAttributeModifier.Operation.ADDITION));
			attributes.put(EntityAttributes.KNOCKBACK_RESISTANCE.getId(), new EntityAttributeModifier(MODIFIERS[slot.getEntitySlotId()], "Knockback modifier", 2, EntityAttributeModifier.Operation.ADDITION));
		}
	}

	@Override
	public void tickArmor(ItemStack stack, PlayerEntity playerEntity) {
		switch (this.slot) {
			case HEAD:
				if (playerEntity.isInWater()) {
					if (Energy.of(stack).use(ENERGY_BREATHING)) {
						playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 5, 1));
					}
				}
				break;
			case CHEST:
				if (Energy.of(stack).getEnergy() > ENERGY_FLY) {
					playerEntity.abilities.allowFlying = true;
					if (playerEntity.abilities.flying) {
						Energy.of(stack).use(ENERGY_FLY);
					}
				} else {
					playerEntity.abilities.allowFlying = false;
					playerEntity.abilities.flying = false;
				}
				break;
			case LEGS:
				if (playerEntity.isSprinting()) {
					Energy.of(stack).use(ENERGY_SPRINTING);
				}
				break;
			case FEET:
				if (playerEntity.isSwimming()) {
					if (Energy.of(stack).use(ENERGY_SWIM)) {
						playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 5, 1));
					}
				}
				break;
			default:

		}
	}

	@Override
	public Multimap<String, EntityAttributeModifier> getModifiers(EquipmentSlot slot) {
		return HashMultimap.create();
	}

	@Override
	public void onRemoved(PlayerEntity playerEntity) {
		if (this.slot == EquipmentSlot.CHEST) {
			if (!playerEntity.isCreative() && !playerEntity.isSpectator()) {
				playerEntity.abilities.allowFlying = false;
				playerEntity.abilities.flying = false;
			}
		}
	}

	@Override
	public float changeFov(float old, ItemStack stack, PlayerEntity playerEntity) {
		if (this.slot == EquipmentSlot.LEGS && Energy.of(stack).getEnergy() > ENERGY_SPRINTING) {
			old -= 0.6; //TODO possibly make it better
		}
		return old;
	}

	@Override
	public double getDurability(ItemStack stack) {
		return 1 - ItemUtils.getPowerForDurabilityBar(stack);
	}

	@Override
	public boolean showDurability(ItemStack stack) {
		return true;
	}

	@Override
	public int getDurabilityColor(ItemStack stack) {
		return PowerSystem.getDisplayPower().colour;
	}

	@Override
	public boolean canRepair(ItemStack itemStack_1, ItemStack itemStack_2) {
		return false;
	}

	@Override
	public double getMaxStoredPower() {
		return 40_000_000;
	}

	@Override
	public EnergyTier getTier() {
		return EnergyTier.HIGH;
	}

	@Override
	public double getMaxInput(EnergySide side) {
		return 2048;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> itemList) {
		if (!isIn(group)) {
			return;
		}
		InitUtils.initPoweredItems(this, itemList);
	}
}
