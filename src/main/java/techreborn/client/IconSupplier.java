package techreborn.client;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;

public class IconSupplier {
    public static IIcon insulatedCopperCable;
    public static IIcon copperCable;
    public static IIcon goldCable;
    public static IIcon insulatedGoldCable;
    public static IIcon doubleInsulatedGoldCable;
    public static IIcon ironCable;
    public static IIcon insulatedIronCable;
    public static IIcon doubleInsulatedIronCable;
    public static IIcon trippleInsulatedIronCable;
    public static IIcon glassFiberCable;
    public static IIcon tinCable;
    public static IIcon detectorCableBlock;
    public static IIcon splitterCableBlock;
    public static IIcon insulatedtinCableBlock;

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event) {
        TextureMap reg = event.map;
        if (reg.getTextureType() == 0) {
            insulatedCopperCable = reg.registerIcon("ic2" + ":wiring/cable/blockCable");
            copperCable = reg.registerIcon("ic2" + ":wiring/cable/blockCableO");
            goldCable = reg.registerIcon("ic2" + ":wiring/cable/blockGoldCable");
            insulatedGoldCable = reg.registerIcon("ic2" + ":wiring/cable/blockGoldCableI");
            doubleInsulatedGoldCable = reg.registerIcon("ic2" + ":wiring/cable/blockGoldCableII");
            ironCable = reg.registerIcon("ic2" + ":wiring/cable/blockIronCable");
            insulatedIronCable = reg.registerIcon("ic2" + ":wiring/cable/blockIronCableI");
            doubleInsulatedIronCable = reg.registerIcon("ic2" + ":wiring/cable/blockIronCableII");
            trippleInsulatedIronCable = reg.registerIcon("ic2" + ":wiring/cable/blockIronCableIII");
            glassFiberCable = reg.registerIcon("ic2" + ":wiring/cable/blockGlassCable");
            tinCable = reg.registerIcon("ic2" + ":wiring/cable/blockTinCable");
            detectorCableBlock = reg.registerIcon("ic2" + ":wiring/cable/blockDetectorCable");
            splitterCableBlock = reg.registerIcon("ic2" + ":wiring/cable/blockSplitterCable");
            insulatedtinCableBlock = reg.registerIcon("ic2" + ":wiring/cable/blockTinCableI");
        }
    }
}
