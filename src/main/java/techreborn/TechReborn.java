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

package techreborn;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.DispenserBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reborncore.api.power.ItemPowerHolder;
import reborncore.common.config.Configuration;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.Torus;
import reborncore.common.world.DataAttachment;
import techreborn.blockentity.storage.idsu.IDSUManager;
import techreborn.client.GuiHandler;
import techreborn.config.TechRebornConfig;
import techreborn.events.ModRegistry;
import techreborn.init.*;
import techreborn.packets.ClientboundPackets;
import techreborn.packets.ServerboundPackets;
import techreborn.utils.BehaviorDispenseScrapbox;
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

		ItemPowerHolder.setup();

		// Done to force the class to load
		ModRecipes.GRINDER.getName();

		ClientboundPackets.init();
		ServerboundPackets.init();

		ModRegistry.setupShit();
		RecipeCrafter.soundHanlder = new ModSounds.SoundHandler();
		ModLoot.init();
		WorldGenerator.initBiomeFeatures();
		GuiHandler.register();
		FluidGeneratorRecipes.init();
		//Force loads the block entities at the right time
		TRBlockEntities.THERMAL_GEN.toString();

		// Scrapbox
		if (TechRebornConfig.dispenseScrapboxes) {
			DispenserBlock.registerBehavior(TRContent.SCRAP_BOX, new BehaviorDispenseScrapbox());
		}

		Torus.genSizeMap(TechRebornConfig.fusionControlComputerMaxCoilSize);

		DataAttachment.REGISTRY.register(IDSUManager.class, IDSUManager::new);

		LOGGER.info("TechReborn setup done!");

	}

}
