package net.minecraft.client.gui;

import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;
import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.lax1dude.eaglercraft.v1_8.internal.EnumCursorType;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
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
public class GuiMerchant extends GuiContainer {
	private static final Logger logger = LogManager.getLogger();
	/**+
	 * The GUI texture for the villager merchant GUI.
	 */
	private static final ResourceLocation MERCHANT_GUI_TEXTURE = new ResourceLocation(
			"textures/gui/container/villager.png");
	private IMerchant merchant;
	private GuiMerchant.MerchantButton nextButton;
	private GuiMerchant.MerchantButton previousButton;
	private int selectedMerchantRecipe;
	private IChatComponent chatComponent;

	public GuiMerchant(InventoryPlayer parInventoryPlayer, IMerchant parIMerchant, World worldIn) {
		super(new ContainerMerchant(parInventoryPlayer, parIMerchant, worldIn));
		this.merchant = parIMerchant;
		this.chatComponent = parIMerchant.getDisplayName();
	}

	/**+
	 * Adds the buttons (and other controls) to the screen in
	 * question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui() {
		super.initGui();
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.buttonList.add(this.nextButton = new GuiMerchant.MerchantButton(1, i + 120 + 27, j + 24 - 1, true));
		this.buttonList.add(this.previousButton = new GuiMerchant.MerchantButton(2, i + 36 - 19, j + 24 - 1, false));
		this.nextButton.enabled = false;
		this.previousButton.enabled = false;
	}

	/**+
	 * Draw the foreground layer for the GuiContainer (everything in
	 * front of the items). Args : mouseX, mouseY
	 */
	protected void drawGuiContainerForegroundLayer(int var1, int var2) {
		String s = this.chatComponent.getUnformattedText();
		this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2,
				4210752);
	}

	/**+
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		super.updateScreen();
		MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(this.mc.thePlayer);
		if (merchantrecipelist != null) {
			this.nextButton.enabled = this.selectedMerchantRecipe < merchantrecipelist.size() - 1;
			this.previousButton.enabled = this.selectedMerchantRecipe > 0;
		}

	}

	/**+
	 * Called by the controls from the buttonList when activated.
	 * (Mouse pressed for buttons)
	 */
	protected void actionPerformed(GuiButton parGuiButton) {
		boolean flag = false;
		if (parGuiButton == this.nextButton) {
			++this.selectedMerchantRecipe;
			MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(this.mc.thePlayer);
			if (merchantrecipelist != null && this.selectedMerchantRecipe >= merchantrecipelist.size()) {
				this.selectedMerchantRecipe = merchantrecipelist.size() - 1;
			}

			flag = true;
		} else if (parGuiButton == this.previousButton) {
			--this.selectedMerchantRecipe;
			if (this.selectedMerchantRecipe < 0) {
				this.selectedMerchantRecipe = 0;
			}

			flag = true;
		}

		if (flag) {
			((ContainerMerchant) this.inventorySlots).setCurrentRecipeIndex(this.selectedMerchantRecipe);
			PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
			packetbuffer.writeInt(this.selectedMerchantRecipe);
			this.mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload("MC|TrSel", packetbuffer));
		}

	}

	/**+
	 * Args : renderPartialTicks, mouseX, mouseY
	 */
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
		MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(this.mc.thePlayer);
		if (merchantrecipelist != null && !merchantrecipelist.isEmpty()) {
			int k = this.selectedMerchantRecipe;
			if (k < 0 || k >= merchantrecipelist.size()) {
				return;
			}

			MerchantRecipe merchantrecipe = (MerchantRecipe) merchantrecipelist.get(k);
			if (merchantrecipe.isRecipeDisabled()) {
				this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.disableLighting();
				this.drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 21, 212, 0, 28, 21);
				this.drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 51, 212, 0, 28, 21);
			}
		}

	}

	/**+
	 * Draws the screen and all the components in it. Args : mouseX,
	 * mouseY, renderPartialTicks
	 */
	public void drawScreen(int i, int j, float f) {
		super.drawScreen(i, j, f);
		MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(this.mc.thePlayer);
		if (merchantrecipelist != null && !merchantrecipelist.isEmpty()) {
			int k = (this.width - this.xSize) / 2;
			int l = (this.height - this.ySize) / 2;
			int i1 = this.selectedMerchantRecipe;
			MerchantRecipe merchantrecipe = (MerchantRecipe) merchantrecipelist.get(i1);
			ItemStack itemstack = merchantrecipe.getItemToBuy();
			ItemStack itemstack1 = merchantrecipe.getSecondItemToBuy();
			ItemStack itemstack2 = merchantrecipe.getItemToSell();
			GlStateManager.pushMatrix();
			RenderHelper.enableGUIStandardItemLighting();
			GlStateManager.disableLighting();
			GlStateManager.enableRescaleNormal();
			GlStateManager.enableColorMaterial();
			GlStateManager.enableLighting();
			this.itemRender.zLevel = 100.0F;
			this.itemRender.renderItemAndEffectIntoGUI(itemstack, k + 36, l + 24);
			this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack, k + 36, l + 24);
			if (itemstack1 != null) {
				this.itemRender.renderItemAndEffectIntoGUI(itemstack1, k + 62, l + 24);
				this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack1, k + 62, l + 24);
			}

			this.itemRender.renderItemAndEffectIntoGUI(itemstack2, k + 120, l + 24);
			this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack2, k + 120, l + 24);
			this.itemRender.zLevel = 0.0F;
			GlStateManager.disableLighting();
			if (this.isPointInRegion(36, 24, 16, 16, i, j) && itemstack != null) {
				this.renderToolTip(itemstack, i, j);
			} else if (itemstack1 != null && this.isPointInRegion(62, 24, 16, 16, i, j) && itemstack1 != null) {
				this.renderToolTip(itemstack1, i, j);
			} else if (itemstack2 != null && this.isPointInRegion(120, 24, 16, 16, i, j) && itemstack2 != null) {
				this.renderToolTip(itemstack2, i, j);
			} else if (merchantrecipe.isRecipeDisabled()
					&& (this.isPointInRegion(83, 21, 28, 21, i, j) || this.isPointInRegion(83, 51, 28, 21, i, j))) {
				this.drawCreativeTabHoveringText(I18n.format("merchant.deprecated", new Object[0]), i, j);
			}

			GlStateManager.popMatrix();
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
			RenderHelper.enableStandardItemLighting();
		}

	}

	public IMerchant getMerchant() {
		return this.merchant;
	}

	static class MerchantButton extends GuiButton {
		private final boolean field_146157_o;

		public MerchantButton(int buttonID, int x, int y, boolean parFlag) {
			super(buttonID, x, y, 12, 19, "");
			this.field_146157_o = parFlag;
		}

		public void drawButton(Minecraft minecraft, int i, int j) {
			if (this.visible) {
				minecraft.getTextureManager().bindTexture(GuiMerchant.MERCHANT_GUI_TEXTURE);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				boolean flag = i >= this.xPosition && j >= this.yPosition && i < this.xPosition + this.width
						&& j < this.yPosition + this.height;
				if (flag && this.enabled) {
					Mouse.showCursor(EnumCursorType.HAND);
				}
				int k = 0;
				int l = 176;
				if (!this.enabled) {
					l += this.width * 2;
				} else if (flag) {
					l += this.width;
				}

				if (!this.field_146157_o) {
					k += this.height;
				}

				this.drawTexturedModalRect(this.xPosition, this.yPosition, l, k, this.width, this.height);
			}
		}
	}
}