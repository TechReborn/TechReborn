package techreborn.command;


import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fluids.Fluid;
import reborncore.api.fuel.FluidPowerManager;
import reborncore.common.explosion.RebornExplosion;
import techreborn.api.recipe.RecipeHandler;

import java.util.ArrayList;
import java.util.List;

public class TechRebornDevCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "trdev";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {
        return "commands.forge.usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.addChatMessage(new ChatComponentText("You need to use arguments, see /trdev help"));
        } else if ("help".equals(args[0])) {
            sender.addChatMessage(new ChatComponentText("recipes 	- Shows size of the recipe array"));
            sender.addChatMessage(new ChatComponentText("fluid     	- Lists the fluid power values"));
        } else if ("recipes".equals(args[0])) {
            sender.addChatMessage(new ChatComponentText(RecipeHandler.recipeList.size() + " recipes loaded"));
        } else if ("fluid".equals(args[0])) {
            for (Object object : FluidPowerManager.fluidPowerValues.keySet().toArray()) {
                if (object instanceof Fluid) {
                    Fluid fluid = (Fluid) object;
                    sender.addChatMessage(new ChatComponentText(fluid.getUnlocalizedName() + " : " + FluidPowerManager.fluidPowerValues.get(fluid)));
                } else {
                    sender.addChatMessage(new ChatComponentText("Found invalid fluid entry"));
                }
            }
        } else if ("clear".equals(args[0])) {
            EntityPlayerMP playerMP = (EntityPlayerMP) sender;
            List<Block> blocksToRemove = new ArrayList<>();
            blocksToRemove.add(Blocks.grass);
            blocksToRemove.add(Blocks.dirt);
            blocksToRemove.add(Blocks.stone);
            for (int x = 0; x < 25; x++) {
                for (int z = 0; z < 25; z++) {
                    for (int y = 0; y < playerMP.posY; y++) {
                        BlockPos pos = new BlockPos(playerMP.posX + x, y, playerMP.posZ + z);
                        if (blocksToRemove.contains(playerMP.worldObj.getBlockState(pos).getBlock())) {
                            playerMP.worldObj.setBlockState(pos, Blocks.air.getDefaultState(), 2);
                        }
                    }
                }
            }
        }else if ("explode".equals(args[0])) {
            EntityPlayerMP playerMP = (EntityPlayerMP) sender;
            RebornExplosion explosion = new RebornExplosion(playerMP.getPosition(), playerMP.worldObj, 40);
            explosion.explode();
        }
    }
}


