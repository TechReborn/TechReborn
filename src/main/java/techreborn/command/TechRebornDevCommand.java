/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

package techreborn.command;

import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.oredict.OreDictionary;
import reborncore.api.recipe.RecipeHandler;

import java.util.ArrayList;
import java.util.List;

public class TechRebornDevCommand extends CommandBase {

	@Override
	public String getName() {
		return "trdev";
	}

	@Override
	public String getUsage(ICommandSender icommandsender) {
		return "commands.forge.usage";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0) {
			sender.sendMessage(new TextComponentString("You need to use arguments, see /trdev help"));
		} else if ("help".equals(args[0])) {
			sender.sendMessage(new TextComponentString("recipes 	- Shows size of the recipe array"));
			sender.sendMessage(new TextComponentString("fluid     	- Lists the fluid power values"));
		} else if ("recipes".equals(args[0])) {
			sender.sendMessage(new TextComponentString(RecipeHandler.recipeList.size() + " recipes loaded"));
		} else if ("clear".equals(args[0])) {
			EntityPlayerMP playerMP = (EntityPlayerMP) sender;
			List<Block> blocksToRemove = new ArrayList<>();
			blocksToRemove.add(Blocks.GRASS);
			blocksToRemove.add(Blocks.DIRT);
			blocksToRemove.add(Blocks.STONE);
			blocksToRemove.add(Blocks.END_STONE);
			for (int x = 0; x < 25; x++) {
				for (int z = 0; z < 25; z++) {
					for (int y = 0; y < playerMP.posY; y++) {
						BlockPos pos = new BlockPos(playerMP.posX + x, y, playerMP.posZ + z);
						if (blocksToRemove.contains(playerMP.world.getBlockState(pos).getBlock())) {
							playerMP.world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
						}
					}
				}
			}
		} else if ("getname".equals(args[0])) {
			EntityPlayer player = (EntityPlayer) sender;
			if (player.getHeldItem(EnumHand.MAIN_HAND) != ItemStack.EMPTY) {
				sender.sendMessage(new TextComponentString(player.getHeldItem(EnumHand.MAIN_HAND).getItem().getRegistryName() + ":" + player.getHeldItem(EnumHand.MAIN_HAND).getItemDamage()));
			} else {
				sender.sendMessage(new TextComponentString("hold an item!"));
			}
		} else if ("ores".equals(args[0])) {
			for (String ore : OreDictionary.getOreNames()) {
				System.out.println(ore);
			}
		}
	}
}
