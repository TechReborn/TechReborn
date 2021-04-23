/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.items.tool.industrial;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.tool.attribute.v1.DynamicAttributeTool;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.tag.Tag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.ItemDurabilityExtensions;
import reborncore.common.util.ItemUtils;
import reborncore.common.util.TorchHelper;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHolder;
import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyTier;
import techreborn.TechReborn;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRContent;
import techreborn.items.tool.MiningLevel;
import techreborn.utils.InitUtils;

import org.jetbrains.annotations.Nullable;
import java.util.List;

public class OmniToolItem extends PickaxeItem implements EnergyHolder, ItemDurabilityExtensions, DynamicAttributeTool {

	public final int maxCharge = TechRebornConfig.omniToolCharge;
	public int cost = TechRebornConfig.omniToolCost;
	public int hitCost = TechRebornConfig.omniToolHitCost;
	public final int miningLevel;

	// 4M FE max charge with 1k charge rate
	public OmniToolItem() {
		super(ToolMaterials.DIAMOND, 3, 1, new Item.Settings().group(TechReborn.ITEMGROUP).maxCount(1).maxDamage(-1));
		this.miningLevel = MiningLevel.DIAMOND.intLevel;
	}

	// PickaxeItem
	@Override
	public boolean isEffectiveOn(BlockState state) {
		return Items.DIAMOND_AXE.isEffectiveOn(state) || Items.DIAMOND_SWORD.isEffectiveOn(state)
				|| Items.DIAMOND_PICKAXE.isEffectiveOn(state) || Items.DIAMOND_SHOVEL.isEffectiveOn(state)
				|| Items.SHEARS.isEffectiveOn(state);
	}

	@Override
	public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
		if (Energy.of(stack).getEnergy() >= cost) {
			return ToolMaterials.DIAMOND.getMiningSpeedMultiplier();
		}
		return super.getMiningSpeedMultiplier(stack, state);
	}

	// MiningToolItem
	@Override
	public boolean postMine(ItemStack stack, World worldIn, BlockState blockIn, BlockPos pos, LivingEntity entityLiving) {
		Energy.of(stack).use(cost);
		return true;
	}

	@Override
	public boolean postHit(ItemStack stack, LivingEntity entityliving, LivingEntity attacker) {
		if (Energy.of(stack).use(hitCost)) {
			entityliving.damage(DamageSource.player((PlayerEntity) attacker), 8F);
		}
		return false;
	}

	// ToolItem
	@Override
	public boolean canRepair(ItemStack itemStack_1, ItemStack itemStack_2) {
		return false;
	}

	// Item
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		return TorchHelper.placeTorch(context);
	}

	@Override
	public boolean isDamageable() {
		return false;
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendStacks(ItemGroup par2ItemGroup, DefaultedList<ItemStack> itemList) {
		if (!isIn(par2ItemGroup)) {
			return;
		}
		InitUtils.initPoweredItems(TRContent.OMNI_TOOL, itemList);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World worldIn, List<Text> tooltip, TooltipContext flagIn) {
		tooltip.add(new LiteralText(Formatting.YELLOW + I18n.translate("techreborn.tooltip.omnitool_motto")));
	}

	// ItemDurabilityExtensions
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

	// EnergyHolder
	@Override
	public double getMaxStoredPower() {
		return maxCharge;
	}

	@Override
	public double getMaxOutput(EnergySide side) {
		return 0;
	}

	@Override
	public EnergyTier getTier() {
		return EnergyTier.EXTREME;
	}

	// DynamicAttributeTool
	@Override
	public int getMiningLevel(Tag<Item> tag, BlockState state, ItemStack stack, LivingEntity user) {
		if (tag.equals(FabricToolTags.PICKAXES) || tag.equals(FabricToolTags.SHOVELS) || tag.equals(FabricToolTags.AXES) || tag.equals(FabricToolTags.SHEARS) || tag.equals(FabricToolTags.SWORDS)) {
			return miningLevel;
		}
		return 0;
	}
}
