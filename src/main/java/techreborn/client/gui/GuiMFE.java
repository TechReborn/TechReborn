package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import reborncore.common.util.PowerLocalization;
import techreborn.client.container.ContainerMFE;
import techreborn.tiles.storage.TileMFE;

/**
 * Created by Rushmead
 */
public class GuiMFE extends GuiContainer
{

	public static final ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/mfe.png");

	TileMFE generator;

	ContainerMFE containerGenerator;

	public GuiMFE(EntityPlayer player, TileMFE generator)
	{
		super(new ContainerMFE(generator, player));
		this.xSize = 176;
		this.ySize = 167;
		this.generator = generator;
		this.containerGenerator = (ContainerMFE) this.inventorySlots;
	}

	@Override
	public void initGui()
	{
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		super.initGui();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
	{
		this.mc.getTextureManager().bindTexture(texture);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		int j = (int)(generator.getEnergy() * 24f / generator.getMaxPower());
		if (j > 0) {
			this.drawTexturedModalRect(k + 109, l + 21 + 12, 176, 0, j + 1, 16);
		}
		//
		// if (containerGenerator.burnTime != 0)
		// {
		// j = containerGenerator.getScaledBurnTime(13);
		// this.drawTexturedModalRect(k + 80, l + 38 + 12 - j, 176, 30 - j, 14,
		// j + 1);
		// }
	}

	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
	{
		String name = I18n.translateToLocal("tile.techreborn.mfe.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6,
				4210752);

		this.fontRendererObj.drawString(I18n.translateToLocalFormatted("container.inventory", new Object[0]), this.xSize - 60,
				this.ySize - 96 + 2, 4210752);
		this.fontRendererObj.drawString(PowerLocalization.getLocalizedPower(generator.getMaxPower()), 110, this.ySize - 150,
				4210752);
		this.fontRendererObj.drawString(PowerLocalization.getLocalizedPower(containerGenerator.energy), 110, this.ySize - 160,
				4210752);
	}
}
