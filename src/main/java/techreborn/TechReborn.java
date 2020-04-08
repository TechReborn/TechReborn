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

package techreborn;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reborncore.common.blockentity.RedstoneConfiguration;
import reborncore.common.config.Configuration;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.Torus;
import reborncore.common.world.DataAttachment;
import techreborn.blockentity.storage.energy.idsu.IDSUManager;
import techreborn.client.GuiType;
import techreborn.config.TechRebornConfig;
import techreborn.events.ModRegistry;
import techreborn.init.FluidGeneratorRecipes;
import techreborn.init.ModLoot;
import techreborn.init.ModRecipes;
import techreborn.init.ModSounds;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;
import techreborn.init.TRDispenserBehavior;
import techreborn.items.DynamicCellItem;
import techreborn.packets.ClientboundPackets;
import techreborn.packets.ServerboundPackets;
import techreborn.utils.PoweredCraftingHandler;
import techreborn.world.WorldGenerator;

public class TechReborn implements ModInitializer {

	public static final String MOD_ID = "techreborn";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	public static TechReborn INSTANCE;

	public static ItemGroup ITEMGROUP = FabricItemGroupBuilder.build(
			new Identifier("techreborn", "item_group"),
			() -> new ItemStack(TRContent.NUKE));

	@Override
	public void onInitialize() {
		INSTANCE = this;
		new Configuration(TechRebornConfig.class, "techreborn");

		// Done to force the class to load
		ModRecipes.GRINDER.getName();

		ClientboundPackets.init();
		ServerboundPackets.init();

		ModRegistry.setupShit();
		if (TechRebornConfig.machineSoundVolume > 0) {
			if (TechRebornConfig.machineSoundVolume > 1) TechRebornConfig.machineSoundVolume = 1F;
			RecipeCrafter.soundHanlder = new ModSounds.SoundHandler();
		}
		ModLoot.init();
		WorldGenerator.initBiomeFeatures();
		FluidGeneratorRecipes.init();
		//Force loads the block entities at the right time
		TRBlockEntities.THERMAL_GEN.toString();
		GuiType.AESU.getIdentifier();
		TRDispenserBehavior.init();
		PoweredCraftingHandler.setup();

		Torus.genSizeMap(TechRebornConfig.fusionControlComputerMaxCoilSize);

		DataAttachment.REGISTRY.register(IDSUManager.class, IDSUManager::new);

		RedstoneConfiguration.fluidStack = DynamicCellItem.getCellWithFluid(Fluids.LAVA);
		RedstoneConfiguration.powerStack = new ItemStack(TRContent.RED_CELL_BATTERY);

		LOGGER.info("TechReborn setup done!");

	}

}
