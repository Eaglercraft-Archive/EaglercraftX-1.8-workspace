package net.minecraft.client.gui;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.util.ArrayList;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.lax1dude.eaglercraft.v1_8.internal.EnumCursorType;

import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnchantmentNameParts;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2025 lax1dude, ayunami2000. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */
public class GuiEnchantment extends GuiContainer {
	/**+
	 * The ResourceLocation containing the Enchantment GUI texture
	 * location
	 */
	private static final ResourceLocation ENCHANTMENT_TABLE_GUI_TEXTURE = new ResourceLocation(
			"textures/gui/container/enchanting_table.png");
	/**+
	 * The ResourceLocation containing the texture for the Book
	 * rendered above the enchantment table
	 */
	private static final ResourceLocation ENCHANTMENT_TABLE_BOOK_TEXTURE = new ResourceLocation(
			"textures/entity/enchanting_table_book.png");
	/**+
	 * The ModelBook instance used for rendering the book on the
	 * Enchantment table
	 */
	private static final ModelBook MODEL_BOOK = new ModelBook();
	private final InventoryPlayer playerInventory;
	/**+
	 * A Random instance for use with the enchantment gui
	 */
	private EaglercraftRandom random = new EaglercraftRandom();
	private ContainerEnchantment container;
	public int field_147073_u;
	public float field_147071_v;
	public float field_147069_w;
	public float field_147082_x;
	public float field_147081_y;
	public float field_147080_z;
	public float field_147076_A;
	ItemStack field_147077_B;
	private final IWorldNameable field_175380_I;

	public GuiEnchantment(InventoryPlayer inventory, World worldIn, IWorldNameable parIWorldNameable) {
		super(new ContainerEnchantment(inventory, worldIn));
		this.playerInventory = inventory;
		this.container = (ContainerEnchantment) this.inventorySlots;
		this.field_175380_I = parIWorldNameable;
	}

	/**+
	 * Draw the foreground layer for the GuiContainer (everything in
	 * front of the items). Args : mouseX, mouseY
	 */
	protected void drawGuiContainerForegroundLayer(int var1, int var2) {
		this.fontRendererObj.drawString(this.field_175380_I.getDisplayName().getUnformattedText(), 12, 5, 4210752);
		this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8,
				this.ySize - 96 + 2, 4210752);
	}

	/**+
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		super.updateScreen();
		this.func_147068_g();
	}

	/**+
	 * Called when the mouse is clicked. Args : mouseX, mouseY,
	 * clickedButton
	 */
	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {
		super.mouseClicked(parInt1, parInt2, parInt3);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;

		for (int k = 0; k < 3; ++k) {
			int l = parInt1 - (i + 60);
			int i1 = parInt2 - (j + 14 + 19 * k);
			if (l >= 0 && i1 >= 0 && l < 108 && i1 < 19 && this.container.enchantItem(this.mc.thePlayer, k)) {
				this.mc.playerController.sendEnchantPacket(this.container.windowId, k);
			}
		}

	}

	/**+
	 * Args : renderPartialTicks, mouseX, mouseY
	 */
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_GUI_TEXTURE);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		GlStateManager.pushMatrix();
		GlStateManager.matrixMode(GL_PROJECTION);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		ScaledResolution scaledresolution = mc.scaledResolution;
		GlStateManager.viewport((scaledresolution.getScaledWidth() - 290 - 12) / 2 * scaledresolution.getScaleFactor(),
				(scaledresolution.getScaledHeight() - 220 + 10) / 2 * scaledresolution.getScaleFactor(),
				290 * scaledresolution.getScaleFactor(), 220 * scaledresolution.getScaleFactor());
		GlStateManager.translate(-0.34F, 0.23F, 0.0F);
		GlStateManager.gluPerspective(90.0F, 1.3333334F, 9.0F, 80.0F);
		float f1 = 1.0F;
		GlStateManager.matrixMode(GL_MODELVIEW);
		GlStateManager.loadIdentity();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.translate(0.0F, 3.3F, -16.0F);
		GlStateManager.scale(f1, f1, f1);
		float f2 = 5.0F;
		GlStateManager.scale(f2, f2, f2);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_BOOK_TEXTURE);
		GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
		float f3 = this.field_147076_A + (this.field_147080_z - this.field_147076_A) * f;
		GlStateManager.translate((1.0F - f3) * 0.2F, (1.0F - f3) * 0.1F, (1.0F - f3) * 0.25F);
		GlStateManager.rotate(-(1.0F - f3) * 90.0F - 90.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
		float f4 = this.field_147069_w + (this.field_147071_v - this.field_147069_w) * f + 0.25F;
		float f5 = this.field_147069_w + (this.field_147071_v - this.field_147069_w) * f + 0.75F;
		f4 = (f4 - (float) MathHelper.truncateDoubleToInt((double) f4)) * 1.6F - 0.3F;
		f5 = (f5 - (float) MathHelper.truncateDoubleToInt((double) f5)) * 1.6F - 0.3F;
		if (f4 < 0.0F) {
			f4 = 0.0F;
		}

		if (f5 < 0.0F) {
			f5 = 0.0F;
		}

		if (f4 > 1.0F) {
			f4 = 1.0F;
		}

		if (f5 > 1.0F) {
			f5 = 1.0F;
		}

		GlStateManager.enableRescaleNormal();
		GlStateManager.enableDepth();
		MODEL_BOOK.render((Entity) null, 0.0F, f4, f5, f3, 0.0F, 0.0625F);
		GlStateManager.disableDepth();
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.matrixMode(GL_PROJECTION);
		GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(GL_MODELVIEW);
		GlStateManager.popMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		EnchantmentNameParts.getInstance().reseedRandomGenerator((long) this.container.xpSeed);
		int i1 = this.container.getLapisAmount();

		for (int j1 = 0; j1 < 3; ++j1) {
			int k1 = k + 60;
			int l1 = k1 + 20;
			byte b0 = 86;
			String s = EnchantmentNameParts.getInstance().generateNewRandomName();
			this.zLevel = 0.0F;
			this.mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_GUI_TEXTURE);
			int i2 = this.container.enchantLevels[j1];
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			if (i2 == 0) {
				this.drawTexturedModalRect(k1, l + 14 + 19 * j1, 0, 185, 108, 19);
			} else {
				String s1 = "" + i2;
				FontRenderer fontrenderer = this.mc.standardGalacticFontRenderer;
				int j2 = 6839882;
				if ((i1 < j1 + 1 || this.mc.thePlayer.experienceLevel < i2)
						&& !this.mc.thePlayer.capabilities.isCreativeMode) {
					this.drawTexturedModalRect(k1, l + 14 + 19 * j1, 0, 185, 108, 19);
					this.drawTexturedModalRect(k1 + 1, l + 15 + 19 * j1, 16 * j1, 239, 16, 16);
					fontrenderer.drawSplitString(s, l1, l + 16 + 19 * j1, b0, (j2 & 16711422) >> 1);
					j2 = 4226832;
				} else {
					int k2 = i - (k + 60);
					int l2 = j - (l + 14 + 19 * j1);
					if (k2 >= 0 && l2 >= 0 && k2 < 108 && l2 < 19) {
						this.drawTexturedModalRect(k1, l + 14 + 19 * j1, 0, 204, 108, 19);
						j2 = 16777088;
					} else {
						this.drawTexturedModalRect(k1, l + 14 + 19 * j1, 0, 166, 108, 19);
					}

					this.drawTexturedModalRect(k1 + 1, l + 15 + 19 * j1, 16 * j1, 223, 16, 16);
					fontrenderer.drawSplitString(s, l1, l + 16 + 19 * j1, b0, j2);
					j2 = 8453920;
				}

				fontrenderer = this.mc.fontRendererObj;
				fontrenderer.drawStringWithShadow(s1, (float) (l1 + 86 - fontrenderer.getStringWidth(s1)),
						(float) (l + 16 + 19 * j1 + 7), j2);
			}
		}

	}

	/**+
	 * Draws the screen and all the components in it. Args : mouseX,
	 * mouseY, renderPartialTicks
	 */
	public void drawScreen(int i, int j, float f) {
		super.drawScreen(i, j, f);
		boolean flag = this.mc.thePlayer.capabilities.isCreativeMode;
		int k = this.container.getLapisAmount();

		for (int l = 0; l < 3; ++l) {
			int i1 = this.container.enchantLevels[l];
			int j1 = this.container.field_178151_h[l];
			int k1 = l + 1;
			if (this.isPointInRegion(60, 14 + 19 * l, 108, 17, i, j) && i1 > 0 && j1 >= 0) {
				ArrayList arraylist = Lists.newArrayList();
				if (j1 >= 0 && Enchantment.getEnchantmentById(j1 & 255) != null) {
					String s = Enchantment.getEnchantmentById(j1 & 255).getTranslatedName((j1 & '\uff00') >> 8);
					arraylist.add(EnumChatFormatting.WHITE.toString() + EnumChatFormatting.ITALIC.toString()
							+ I18n.format("container.enchant.clue", new Object[] { s }));
				}

				if (!flag) {
					if (j1 >= 0) {
						arraylist.add("");
					}

					if (this.mc.thePlayer.experienceLevel < i1) {
						arraylist.add(EnumChatFormatting.RED.toString() + "Level Requirement: "
								+ this.container.enchantLevels[l]);
					} else {
						String s1 = "";
						if (k1 == 1) {
							s1 = I18n.format("container.enchant.lapis.one", new Object[0]);
						} else {
							s1 = I18n.format("container.enchant.lapis.many", new Object[] { Integer.valueOf(k1) });
						}

						if (k >= k1) {
							arraylist.add(EnumChatFormatting.GRAY.toString() + "" + s1);
						} else {
							arraylist.add(EnumChatFormatting.RED.toString() + "" + s1);
						}

						if (k1 == 1) {
							s1 = I18n.format("container.enchant.level.one", new Object[0]);
						} else {
							s1 = I18n.format("container.enchant.level.many", new Object[] { Integer.valueOf(k1) });
						}

						arraylist.add(EnumChatFormatting.GRAY.toString() + "" + s1);
					}
				}

				Mouse.showCursor(EnumCursorType.HAND);
				this.drawHoveringText(arraylist, i, j);
				break;
			}
		}

	}

	public void func_147068_g() {
		ItemStack itemstack = this.inventorySlots.getSlot(0).getStack();
		if (!ItemStack.areItemStacksEqual(itemstack, this.field_147077_B)) {
			this.field_147077_B = itemstack;

			while (true) {
				this.field_147082_x += (float) (this.random.nextInt(4) - this.random.nextInt(4));
				if (this.field_147071_v > this.field_147082_x + 1.0F
						|| this.field_147071_v < this.field_147082_x - 1.0F) {
					break;
				}
			}
		}

		++this.field_147073_u;
		this.field_147069_w = this.field_147071_v;
		this.field_147076_A = this.field_147080_z;
		boolean flag = false;

		for (int i = 0; i < 3; ++i) {
			if (this.container.enchantLevels[i] != 0) {
				flag = true;
			}
		}

		if (flag) {
			this.field_147080_z += 0.2F;
		} else {
			this.field_147080_z -= 0.2F;
		}

		this.field_147080_z = MathHelper.clamp_float(this.field_147080_z, 0.0F, 1.0F);
		float f1 = (this.field_147082_x - this.field_147071_v) * 0.4F;
		float f = 0.2F;
		f1 = MathHelper.clamp_float(f1, -f, f);
		this.field_147081_y += (f1 - this.field_147081_y) * 0.9F;
		this.field_147071_v += this.field_147081_y;
	}
}