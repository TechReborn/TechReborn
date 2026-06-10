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
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.block.ComposterBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reborncore.common.blockentity.RedstoneConfiguration;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.Torus;
import techreborn.blockentity.GuiType;
import techreborn.component.TRDataComponentTypes;
import techreborn.config.TechRebornConfig;
import techreborn.events.ApplyArmorToDamageHandler;
import techreborn.events.OreDepthSyncHandler;
import techreborn.events.UseBlockHandler;
import techreborn.init.FuelRecipes;
import techreborn.init.ModLoot;
import techreborn.init.ModRecipes;
import techreborn.init.ModSounds;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRCauldronBehavior;
import techreborn.init.TRContent;
import techreborn.init.TRDispenserBehavior;
import techreborn.init.template.TechRebornTemplates;
import techreborn.packets.Packets;
import techreborn.packets.ServerboundPackets;
import techreborn.utils.PoweredCraftingHandler;
import techreborn.world.WorldGenerator;

public class TechReborn implements ModInitializer {
	public static final String MOD_ID = "techreborn";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		TechRebornConfig.init();
		TRContent.register();

		// Done to force the class to load
		//noinspection ResultOfMethodCallIgnored
		ModRecipes.GRINDER.hashCode();
		TRDataComponentTypes.init();
		TRContent.SCRAP_BOX.asItem();

		Packets.register();;
		ServerboundPackets.init();
		OreDepthSyncHandler.setup();

		if (TechRebornConfig.machineSoundVolume.get() > 0) {
			RecipeCrafter.soundHandler = new ModSounds.SoundHandler();
		}
		ModLoot.init();
		WorldGenerator.initWorldGen();
		//Force loads the block entities at the right time
		//noinspection ResultOfMethodCallIgnored
		TRBlockEntities.THERMAL_GEN.toString();
		//noinspection ResultOfMethodCallIgnored
		GuiType.AESU.getIdentifier();
		TRDispenserBehavior.init();
		TRCauldronBehavior.init();
		PoweredCraftingHandler.setup();
		UseBlockHandler.init();
		ApplyArmorToDamageHandler.init();
		FuelRecipes.init();


		Torus.genSizeMap(TechRebornConfig.fusionControlComputerMaxCoilSize.get());

		RedstoneConfiguration.fluidStack = new ItemStackTemplate(TRContent.Cells.LAVA.asItem());
		RedstoneConfiguration.powerStack = new ItemStackTemplate(TRContent.RED_CELL_BATTERY);

		ComposterBlock.COMPOSTABLES.put(TRContent.RUBBER_SAPLING.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(TRContent.RUBBER_LEAVES.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(TRContent.Parts.PLANTBALL.asItem(), 1F);
		ComposterBlock.COMPOSTABLES.put(TRContent.Parts.COMPRESSED_PLANTBALL.asItem(), 1F);
		ComposterBlock.COMPOSTABLES.put(TRContent.Dusts.SAW.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(TRContent.SmallDusts.SAW.asItem(), 0.1F);

		TechRebornTemplates.init();

		LOGGER.info("TechReborn setup done!");
	}
}
