package net.minecraft.client.renderer;

import java.util.List;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

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
public abstract class InventoryEffectRenderer extends GuiContainer {
	private boolean hasActivePotionEffects;

	public InventoryEffectRenderer(Container inventorySlotsIn) {
		super(inventorySlotsIn);
	}

	/**+
	 * Adds the buttons (and other controls) to the screen in
	 * question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui() {
		super.initGui();
		this.updateActivePotionEffects();
	}

	protected void updateActivePotionEffects() {
		if (!this.mc.thePlayer.getActivePotionEffects().isEmpty()) {
			this.guiLeft = 160 + (this.width - this.xSize - 200) / 2;
			this.hasActivePotionEffects = true;
		} else {
			this.guiLeft = (this.width - this.xSize) / 2;
			this.hasActivePotionEffects = false;
		}

	}

	/**+
	 * Draws the screen and all the components in it. Args : mouseX,
	 * mouseY, renderPartialTicks
	 */
	public void drawScreen(int i, int j, float f) {
		super.drawScreen(i, j, f);
		if (this.hasActivePotionEffects) {
			this.drawActivePotionEffects();
		}

	}

	/**+
	 * Display the potion effects list
	 */
	private void drawActivePotionEffects() {
		int i = this.guiLeft - 124;
		int j = this.guiTop;
		boolean flag = true;
		List<PotionEffect> collection = this.mc.thePlayer.getActivePotionEffectsList();
		if (!collection.isEmpty()) {
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.disableLighting();
			GlStateManager.enableAlpha();
			int k = 33;
			if (collection.size() > 5) {
				k = 132 / (collection.size() - 1);
			}

			for (PotionEffect potioneffect : collection) {
				Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				this.mc.getTextureManager().bindTexture(inventoryBackground);
				this.drawTexturedModalRect(i, j, 0, 166, 140, 32);
				if (potion.hasStatusIcon()) {
					int l = potion.getStatusIconIndex();
					this.drawTexturedModalRect(i + 6, j + 7, 0 + l % 8 * 18, 198 + l / 8 * 18, 18, 18);
				}

				String s1 = I18n.format(potion.getName(), new Object[0]);
				if (potioneffect.getAmplifier() == 1) {
					s1 = s1 + " " + I18n.format("enchantment.level.2", new Object[0]);
				} else if (potioneffect.getAmplifier() == 2) {
					s1 = s1 + " " + I18n.format("enchantment.level.3", new Object[0]);
				} else if (potioneffect.getAmplifier() == 3) {
					s1 = s1 + " " + I18n.format("enchantment.level.4", new Object[0]);
				}

				this.fontRendererObj.drawStringWithShadow(s1, (float) (i + 10 + 18), (float) (j + 6), 16777215);
				String s = Potion.getDurationString(potioneffect);
				this.fontRendererObj.drawStringWithShadow(s, (float) (i + 10 + 18), (float) (j + 6 + 10), 8355711);
				j += k;
			}

		}
	}
}