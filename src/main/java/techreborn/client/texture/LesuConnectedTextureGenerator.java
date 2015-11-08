package techreborn.client.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import reborncore.client.texture.ConnectedTexture;
import techreborn.lib.ModInfo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class LesuConnectedTextureGenerator extends TextureAtlasSprite {

    public BufferedImage output_image = null;
    String type;
    ConnectedTexture connectedTexture;

    public LesuConnectedTextureGenerator(String name, String type, ConnectedTexture connectedTexture) {
        super(name);
        this.type = type;
        this.connectedTexture = connectedTexture;
    }

    public static String getDerivedName(String s2) {
        return ModInfo.MOD_ID + ":machine/casing/" + s2;
    }

    public static ResourceLocation getTypeResource(String type) {
        return new ResourceLocation("techreborn", "textures/blocks/machine/" + type + ".png");
    }

    public static ResourceLocation getLapisResource() {
        return new ResourceLocation("minecraft", "textures/blocks/lapis_block.png");
    }


    private static int[] createTexture(int w, int[] type_data, int[] edge_data, ConnectedTexture connectedTexture) {
        int[] new_data = type_data;
        for (int i = 0; i < type_data.length; i += 1) {
            int x = (i % w);
            int y = (i - x) / w;
            if (getAlpha(edge_data[i]) != 0) {
                if (y <= 1 && connectedTexture.isUp()) {
                    new_data[i] = edge_data[i];
                }
                if (y >= 14 && connectedTexture.isDown()) {
                    new_data[i] = edge_data[i];
                }
                if (x <= 1 && connectedTexture.isRight()) {
                    new_data[i] = edge_data[i];
                }
                if (x >= 14 && connectedTexture.isLeft()) {
                    new_data[i] = edge_data[i];
                }
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
        BufferedImage[] edge_image = new BufferedImage[1 + mp];

        AnimationMetadataSection animation = null;

        try {
            IResource typeResource = manager.getResource(getLapisResource());
            type_image[0] = ImageIO.read(typeResource.getInputStream());
            animation = (AnimationMetadataSection) typeResource.getMetadata("animation");

            IResource edgeResource = manager.getResource(getTypeResource("lesu_block"));
            edge_image[0] = ImageIO.read(edgeResource.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int w = type_image[0].getWidth();
        int h = type_image[0].getHeight();

        output_image = new BufferedImage(w, h, 2);
        int[] type_data = new int[w * w];
        int[] edge_data = new int[w * w];

        for (int y = 0; y < h; y += w) {
            try {
                type_image[0].getRGB(0, y, w, w, type_data, 0, w);
                edge_image[0].getRGB(0, y, w, w, edge_data, 0, w);
            } catch (Exception e) {
                e.fillInStackTrace();
            }
            int[] new_data = createTexture(w, type_data, edge_data, connectedTexture);
            output_image.setRGB(0, y, w, w, new_data, 0, w);
        }

        type_image[0] = output_image;
        this.loadSprite(type_image, animation, (float) Minecraft.getMinecraft().gameSettings.anisotropicFiltering > 1.0F);
        return false;
    }

    public static IIcon genIcon(ConnectedTexture connectedTexture, IIconRegister iconRegister, int texNum, int meta) {
        if (iconRegister instanceof TextureMap) {
            TextureMap map = (TextureMap) iconRegister;
            String name = LesuConnectedTextureGenerator.getDerivedName("lesu." + texNum);
            TextureAtlasSprite texture = map.getTextureExtry(name);
            if (texture == null) {
                texture = new LesuConnectedTextureGenerator(name, "lesu", connectedTexture);
                map.setTextureEntry(name, texture);
            }
            return map.getTextureExtry(name);
        } else {
            return null;
        }
    }
}
