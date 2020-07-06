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
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reborncore.common.blockentity.RedstoneConfiguration;
import reborncore.common.config.Configuration;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.Torus;
import techreborn.client.GuiType;
import techreborn.compat.trinkets.Trinkets;
import techreborn.config.TechRebornConfig;
import techreborn.enet.ElectricNetworkManager;
import techreborn.events.ModRegistry;
import techreborn.init.*;
import techreborn.init.template.TechRebornTemplates;
import techreborn.items.DynamicCellItem;
import techreborn.packets.ClientboundPackets;
import techreborn.packets.ServerboundPackets;
import techreborn.utils.PoweredCraftingHandler;
import techreborn.world.WorldGenerator;

import java.util.function.Predicate;

public class TechReborn implements ModInitializer {

	public static final String MOD_ID = "techreborn";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	public static TechReborn INSTANCE;

	public static ItemGroup ITEMGROUP = FabricItemGroupBuilder.build(
			new Identifier("techreborn", "item_group"),
			() -> new ItemStack(TRContent.NUKE));

	public static Predicate<PlayerEntity> elytraPredicate = playerEntity -> false;

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
		ElectricNetworkManager.init();

		Torus.genSizeMap(TechRebornConfig.fusionControlComputerMaxCoilSize);

		RedstoneConfiguration.fluidStack = DynamicCellItem.getCellWithFluid(Fluids.LAVA);
		RedstoneConfiguration.powerStack = new ItemStack(TRContent.RED_CELL_BATTERY);

		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			ItemStack stack = player.getStackInHand(hand);

			if (stack.getItem() instanceof AxeItem) {
				BlockPos pos = hitResult.getBlockPos();
				BlockState hitState = world.getBlockState(pos);
				Block hitBlock = hitState.getBlock();

				Block strippedBlock = null;
				if (hitBlock == TRContent.RUBBER_LOG) {
					strippedBlock = TRContent.RUBBER_LOG_STRIPPED;
				} else if (hitBlock == TRContent.RUBBER_WOOD) {
					strippedBlock = TRContent.STRIPPED_RUBBER_WOOD;
				}

				if (strippedBlock != null) {
					// Play stripping sound
					world.playSound(player, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
					if (world.isClient) {
						return ActionResult.SUCCESS;
					}

					world.setBlockState(pos, strippedBlock.getDefaultState().with(PillarBlock.AXIS, hitState.get(PillarBlock.AXIS)), 11);

					// Damage axe
					if (player instanceof LivingEntity) {
						LivingEntity playerEntity = (LivingEntity) player;
						stack.damage(1, playerEntity, playerx -> {
							playerx.sendToolBreakStatus(hand);
						});
					}
					return ActionResult.SUCCESS;
				}
			}

			return ActionResult.PASS;
		});

		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(TRContent.RUBBER_SAPLING.asItem(), 0.3F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(TRContent.RUBBER_LEAVES.asItem(), 0.3F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(TRContent.Parts.PLANTBALL.asItem(), 1F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(TRContent.Parts.COMPRESSED_PLANTBALL.asItem(), 1F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(TRContent.Dusts.SAW.asItem(), 0.3F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(TRContent.SmallDusts.SAW.asItem(), 0.1F);

		if (FabricLoader.getInstance().isModLoaded("trinkets")) {
			elytraPredicate = Trinkets.isElytraEquipped();
		}

		TechRebornTemplates.init();

		LOGGER.info("TechReborn setup done!");

	}

}
