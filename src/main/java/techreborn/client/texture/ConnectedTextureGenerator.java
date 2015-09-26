package techreborn.client.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.util.ResourceLocation;
import techreborn.lib.ModInfo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ConnectedTextureGenerator extends TextureAtlasSprite {

    public BufferedImage output_image = null;
    String type;

    public ConnectedTextureGenerator(String name, String type) {
        super(name);
        this.type = type;
    }

    public static String getDerivedName(String s2) {
        return ModInfo.MOD_ID + ":machine/casing/" + s2;
    }

    public static ResourceLocation getTypeResource(String type) {
        return new ResourceLocation("techreborn", "textures/blocks/machine/casing" + type + ".png");
    }

    private static int[] createTexture(int w, int[] type_data) {
        int red = 0;
        int green = 0;
        int blue = 0;
        int alpha = 0;
        for (int i = 0; i < type_data.length; i++) {
            red += getRed(type_data[i]);
            green += getGreen(type_data[i]);
            blue += getBlue(type_data[i]);
            alpha += getAlpha(type_data[i]);
        }
        red = red / type_data.length;
        green = green / type_data.length;
        blue = blue / type_data.length;
        alpha = alpha / type_data.length;


        int[] new_data = type_data;

        for (int i = 0; i < 256; i += 1) {
            int x = (i % w);
            int y = (i - x) / w;

            if (y <= 2 || y >= 14 || x <= 2 || x >= 14) {
                new_data[i] = makeCol(red, green, blue, alpha);
            }
        }
        return new_data;
    }

    public static int makeCol(int red, int green, int blue, int alpha) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    public static int getAlpha(int col) {
        return (col & 0xff000000) >> 24;
    }

    public static int getRed(int col) {
        return (col & 0x00ff0000) >> 16;
    }

    public static int getGreen(int col) {
        return (col & 0x0000ff00) >> 8;
    }

    public static int getBlue(int col) {
        return col & 0x000000ff;
    }

    @Override
    public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
        return true;
    }

    @Override
    public boolean load(IResourceManager manager, ResourceLocation location) {
        int mp = Minecraft.getMinecraft().gameSettings.mipmapLevels;
        BufferedImage[] type_image = new BufferedImage[1 + mp];

        AnimationMetadataSection animation = null;

        try {
            IResource typeResource = manager.getResource(getTypeResource(type));
            type_image[0] = ImageIO.read(typeResource.getInputStream());
            animation = (AnimationMetadataSection) typeResource.getMetadata("animation");
        } catch (IOException e) {
            e.printStackTrace();
        }
        int w = type_image[0].getWidth();
        int h = type_image[0].getHeight();

        output_image = new BufferedImage(w, h, 2);
        int[] type_data = new int[w * w];
        
        for (int y = 0; y < h; y += w) {
            type_image[0].getRGB(0, y, w, w, type_data, 0, w);
            int[] new_data = createTexture(w, type_data);
            output_image.setRGB(0, y, w, w, new_data, 0, w);
        }

        type_image[0] = output_image;
        this.loadSprite(type_image, animation, (float) Minecraft.getMinecraft().gameSettings.anisotropicFiltering > 1.0F);
        System.out.println("Loaded texture!");
        return false;
    }
}
