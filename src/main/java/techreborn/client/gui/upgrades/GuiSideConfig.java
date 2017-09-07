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

package techreborn.client.gui.upgrades;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import reborncore.common.network.NetworkManager;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.gui.GuiBase;
import techreborn.packets.PacketSyncSideConfig;

import java.io.IOException;

/**
 * Created by Mark on 12/04/2017.
 */
public class GuiSideConfig extends GuiBase {

	TileEntity tileEntity;
	int slotID;

	public GuiSideConfig(EntityPlayer player, TileEntity tile, BuiltContainer container, int slotID) {
		super(player, tile, container);
		this.tileEntity = tile;
		this.slotID = slotID;
	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.clear();
		int i = 0;
		for (EnumFacing facing : EnumFacing.VALUES) {
			buttonList.add(new GuiButton(facing.getIndex(), guiLeft + 150, guiTop + i++ * 23 + 22, 20, 20, "✓"));
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, final int mouseX, final int mouseY) {
		super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.FOREGROUND;
		int offset = 10;
		RenderHelper.enableStandardItemLighting();
		RenderHelper.enableGUIStandardItemLighting();
		for (EnumFacing facing : EnumFacing.VALUES) {
			BlockPos pos = tileEntity.getPos().offset(facing);
			IBlockState state = tileEntity.getWorld().getBlockState(pos);
			ItemStack stack = state.getBlock().getPickBlock(state, new RayTraceResult(new Vec3d(0, 0, 0), facing, pos), tileEntity.getWorld(), pos, Minecraft.getMinecraft().player);
			if (stack != null && !stack.isEmpty() && stack.getItem() != null) {
				drawCentredString(stack.getDisplayName() + " - " + facing.getName(), offset += 22, 4210752, layer);
				itemRender.renderItemIntoGUI(stack, 10, offset - 2);
			} else {
				drawCentredString(state.getBlock().getLocalizedName() + " - " + facing.getName(), offset += 22, 4210752, layer);
			}
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		NetworkManager.sendToServer(new PacketSyncSideConfig(slotID, button.id, tileEntity.getPos()));
		Minecraft.getMinecraft().displayGuiScreen(null);
	}

	@Override
	protected void drawTitle() {
		drawCentredString("Side Configuration", 6, 4210752, Layer.FOREGROUND);
	}

	@Override
	public boolean tryAddUpgrades() {
		return false;
	}

	@Override
	public boolean drawPlayerSlots() {
		return false;
	}
}
