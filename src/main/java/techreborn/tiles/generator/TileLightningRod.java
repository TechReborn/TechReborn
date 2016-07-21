package techreborn.tiles.generator;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.oredict.OreDictionary;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.tile.TilePowerProducer;
import techreborn.config.ConfigTechReborn;
import techreborn.power.EnergyUtils;

public class TileLightningRod extends TilePowerProducer {

    private int onStatusHoldTicks = -1;

    @Override
    public void update() {
        super.update();

        if (onStatusHoldTicks > 0)
            --onStatusHoldTicks;

        if (onStatusHoldTicks == 0 || getEnergy() <= 0) {
            if (getBlockType() instanceof BlockMachineBase)
                ((BlockMachineBase) getBlockType()).setActive(false, worldObj, pos);
            onStatusHoldTicks = -1;
        }

        float weatherStrength = worldObj.getThunderStrength(1.0F);
        if (weatherStrength > 0.2F) {
            //lightStrikeChance = (MAX - (CHANCE * WEATHER_STRENGTH)
            float lightStrikeChance = ((100F - ConfigTechReborn.LightningRodChance) * 20F);
            float totalChance = lightStrikeChance * getLightningStrikeMultiplier() * ((1.1F - weatherStrength));
            if (worldObj.rand.nextInt((int) Math.floor(totalChance)) == 0) {
                EntityLightningBolt lightningBolt = new EntityLightningBolt(worldObj,
                        pos.getX() + 0.5F,
                        worldObj.provider.getAverageGroundLevel(),
                        pos.getZ() + 0.5F, false);
                worldObj.addWeatherEffect(lightningBolt);
                worldObj.spawnEntityInWorld(lightningBolt);
                addEnergy(32768 * (0.3F + weatherStrength));
                ((BlockMachineBase) getBlockType()).setActive(true, worldObj, pos);
                onStatusHoldTicks = 400;
            }
        }

    }

    public float getLightningStrikeMultiplier() {
        float actualHeight = worldObj.provider.getActualHeight();
        float groundLevel = worldObj.provider.getAverageGroundLevel();
        for(int i = pos.getY() + 1; i < actualHeight; i++) {
            if(!isValidIronFence(i)) {
                if(groundLevel >= i)
                    return 4.3F;
                float max = actualHeight - groundLevel;
                float got = i - groundLevel;
                return 1.2F - got / max;
            }
        }
        return 4F;
    }

    @Override
    public double emitEnergy(EnumFacing enumFacing, double amount) {
        BlockPos pos = getPos().offset(enumFacing);
        EnergyUtils.PowerNetReceiver receiver = EnergyUtils.getReceiver(
                worldObj, enumFacing.getOpposite(), pos);
        if(receiver != null) {
            addEnergy(amount - receiver.receiveEnergy(amount, false));
        } else addEnergy(amount);
        return 0; //Temporary hack die to my bug RebornCore
    }

    public boolean isValidIronFence(int y) {
        Item itemBlock = Item.getItemFromBlock(worldObj.getBlockState(new BlockPos(pos.getX(), y, pos.getZ())).getBlock());
        for(ItemStack fence : OreDictionary.getOres("fenceIron")) {
            if(fence.getItem() == itemBlock) return true;
        }
        return false;
    }

    @Override
    public double getMaxPower() {
        return 1024000;
    }

    @Override
    public double getMaxOutput() {
        return 32768;
    }

    @Override
    public EnumPowerTier getTier() {
        return EnumPowerTier.INSANE;
    }

}
