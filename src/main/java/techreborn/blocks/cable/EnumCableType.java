/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.blocks.cable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.registration.impl.ConfigRegistryFactory;
import techreborn.TechReborn;
import techreborn.init.ModBlocks;

public enum EnumCableType implements IStringSerializable {
	COPPER("copper", "techreborn:blocks/cables/copper_cable", 128, 12.0, true, EnumPowerTier.MEDIUM),
	TIN("tin", "techreborn:blocks/cables/tin_cable", 32, 12.0, true, EnumPowerTier.LOW),
	GOLD("gold", "techreborn:blocks/cables/gold_cable", 512, 12.0, true, EnumPowerTier.HIGH),
	HV("hv", "techreborn:blocks/cables/hv_cable", 2048, 12.0, true, EnumPowerTier.EXTREME),
	GLASSFIBER("glassfiber", "techreborn:blocks/cables/glass_fiber_cable", 8192, 12.0, false, EnumPowerTier.INSANE),
	ICOPPER("insulatedcopper", "techreborn:blocks/cables/copper_insulated_cable", 128, 10.0, false, EnumPowerTier.MEDIUM),
	IGOLD("insulatedgold", "techreborn:blocks/cables/gold_insulated_cable", 512, 10.0, false, EnumPowerTier.HIGH),
	IHV("insulatedhv", "techreborn:blocks/cables/hv_insulated_cable", 2048, 10.0, false, EnumPowerTier.EXTREME);

	public String textureName = "minecraft:blocks/iron_block";
	public int transferRate = 128;
	public int defaultTransferRate = 128;
	public double cableThickness = 3.0;
	public boolean canKill = false;
	public boolean defaultCanKill = false;
	public EnumPowerTier tier;
	private String friendlyName;

	EnumCableType(String friendlyName, String textureName, int transferRate, double cableThickness, boolean canKill,
	              EnumPowerTier tier) {
		this.friendlyName = friendlyName;
		this.textureName = textureName;
		this.transferRate = transferRate;
		this.defaultTransferRate = transferRate;
		this.cableThickness = cableThickness / 2;
		this.canKill = canKill;
		this.defaultCanKill = canKill;
		this.tier = tier;
	}

	@Override
	public String getName() {
		return friendlyName.toLowerCase();
	}

	public ItemStack getStack() {
		return new ItemStack(ModBlocks.CABLE, 1, this.ordinal());
	}

	@SubscribeEvent
	public static void handleConfig(ConfigRegistryFactory.RebornRegistryEvent event){
		Configuration config = event.getConfiguration(TechReborn.MOD_ID, "misc");
		for(EnumCableType cableType : values()){
			cableType.transferRate = config.getInt(cableType.friendlyName + "_transfer_rate", "cable", cableType.defaultTransferRate, 0, Integer.MAX_VALUE, "Cable transfer rate");
			cableType.canKill = config.getBoolean(cableType.friendlyName + "_do_damage", "cable", cableType.defaultCanKill,  "Set to true to allow cable to do damge to enties that come in contact with it");
		}
	}
}
