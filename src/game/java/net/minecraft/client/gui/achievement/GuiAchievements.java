package net.minecraft.client.gui.achievement;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.lax1dude.eaglercraft.v1_8.PointerInputAbstraction;
import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

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
public class GuiAchievements extends GuiScreen implements IProgressMeter {
	private static final int field_146572_y = AchievementList.minDisplayColumn * 24 - 112;
	private static final int field_146571_z = AchievementList.minDisplayRow * 24 - 112;
	private static final int field_146559_A = AchievementList.maxDisplayColumn * 24 - 77;
	private static final int field_146560_B = AchievementList.maxDisplayRow * 24 - 77;
	private static final ResourceLocation ACHIEVEMENT_BACKGROUND = new ResourceLocation(
			"textures/gui/achievement/achievement_background.png");
	protected GuiScreen parentScreen;
	protected int field_146555_f = 256;
	protected int field_146557_g = 202;
	protected int field_146563_h;
	protected int field_146564_i;
	protected float field_146570_r = 1.0F;
	protected double field_146569_s;
	protected double field_146568_t;
	protected double field_146567_u;
	protected double field_146566_v;
	protected double field_146565_w;
	protected double field_146573_x;
	private int field_146554_D;
	private StatFileWriter statFileWriter;
	private boolean loadingAchievements = true;

	public GuiAchievements(GuiScreen parentScreenIn, StatFileWriter statFileWriterIn) {
		this.parentScreen = parentScreenIn;
		this.statFileWriter = statFileWriterIn;
		short short1 = 141;
		short short2 = 141;
		this.field_146569_s = this.field_146567_u = this.field_146565_w = (double) (AchievementList.openInventory.displayColumn
				* 24 - short1 / 2 - 12);
		this.field_146568_t = this.field_146566_v = this.field_146573_x = (double) (AchievementList.openInventory.displayRow
				* 24 - short2 / 2);
	}

	/**+
	 * Adds the buttons (and other controls) to the screen in
	 * question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui() {
		this.mc.getNetHandler()
				.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.REQUEST_STATS));
		this.buttonList.clear();
		this.buttonList.add(new GuiOptionButton(1, this.width / 2 + 24, this.height / 2 + 74, 80, 20,
				I18n.format("gui.done", new Object[0])));
	}

	/**+
	 * Called by the controls from the buttonList when activated.
	 * (Mouse pressed for buttons)
	 */
	protected void actionPerformed(GuiButton parGuiButton) {
		if (!this.loadingAchievements) {
			if (parGuiButton.id == 1) {
				this.mc.displayGuiScreen(this.parentScreen);
			}

		}
	}

	protected int getCloseKey() {
		return this.mc.gameSettings.keyBindInventory.getKeyCode();
	}

	/**+
	 * Draws the screen and all the components in it. Args : mouseX,
	 * mouseY, renderPartialTicks
	 */
	public void drawScreen(int i, int j, float f) {
		if (this.loadingAchievements) {
			this.drawDefaultBackground();
			this.drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.downloadingStats", new Object[0]),
					this.width / 2, this.height / 2, 16777215);
			this.drawCenteredString(this.fontRendererObj,
					lanSearchStates[(int) (Minecraft.getSystemTime() / 150L % (long) lanSearchStates.length)],
					this.width / 2, this.height / 2 + this.fontRendererObj.FONT_HEIGHT * 2, 16777215);
		} else {
			if (PointerInputAbstraction.getVCursorButtonDown(0)) {
				int k = (this.width - this.field_146555_f) / 2;
				int l = (this.height - this.field_146557_g) / 2;
				int i1 = k + 8;
				int j1 = l + 17;
				if ((this.field_146554_D == 0 || this.field_146554_D == 1) && i >= i1 && i < i1 + 224 && j >= j1
						&& j < j1 + 155) {
					if (this.field_146554_D == 0) {
						this.field_146554_D = 1;
					} else {
						this.field_146567_u -= (double) ((float) (i - this.field_146563_h) * this.field_146570_r);
						this.field_146566_v -= (double) ((float) (j - this.field_146564_i) * this.field_146570_r);
						this.field_146565_w = this.field_146569_s = this.field_146567_u;
						this.field_146573_x = this.field_146568_t = this.field_146566_v;
					}

					this.field_146563_h = i;
					this.field_146564_i = j;
				}
			} else {
				this.field_146554_D = 0;
			}

			int k1 = Mouse.getDWheel();
			float f4 = this.field_146570_r;
			if (k1 < 0) {
				this.field_146570_r += 0.25F;
			} else if (k1 > 0) {
				this.field_146570_r -= 0.25F;
			}

			this.field_146570_r = MathHelper.clamp_float(this.field_146570_r, 1.0F, 2.0F);
			if (this.field_146570_r != f4) {
				float f6 = f4 - this.field_146570_r;
				float f5 = f4 * (float) this.field_146555_f;
				float f1 = f4 * (float) this.field_146557_g;
				float f2 = this.field_146570_r * (float) this.field_146555_f;
				float f3 = this.field_146570_r * (float) this.field_146557_g;
				this.field_146567_u -= (double) ((f2 - f5) * 0.5F);
				this.field_146566_v -= (double) ((f3 - f1) * 0.5F);
				this.field_146565_w = this.field_146569_s = this.field_146567_u;
				this.field_146573_x = this.field_146568_t = this.field_146566_v;
			}

			if (this.field_146565_w < (double) field_146572_y) {
				this.field_146565_w = (double) field_146572_y;
			}

			if (this.field_146573_x < (double) field_146571_z) {
				this.field_146573_x = (double) field_146571_z;
			}

			if (this.field_146565_w >= (double) field_146559_A) {
				this.field_146565_w = (double) (field_146559_A - 1);
			}

			if (this.field_146573_x >= (double) field_146560_B) {
				this.field_146573_x = (double) (field_146560_B - 1);
			}

			this.drawDefaultBackground();
			this.drawAchievementScreen(i, j, f);
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			this.drawTitle();
			GlStateManager.disableLighting();
			GlStateManager.enableDepth();
		}

	}

	public void doneLoading() {
		if (this.loadingAchievements) {
			this.loadingAchievements = false;
		}

	}

	/**+
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		if (!this.loadingAchievements) {
			this.field_146569_s = this.field_146567_u;
			this.field_146568_t = this.field_146566_v;
			double d0 = this.field_146565_w - this.field_146567_u;
			double d1 = this.field_146573_x - this.field_146566_v;
			if (d0 * d0 + d1 * d1 < 4.0D) {
				this.field_146567_u += d0;
				this.field_146566_v += d1;
			} else {
				this.field_146567_u += d0 * 0.85D;
				this.field_146566_v += d1 * 0.85D;
			}

		}
	}

	protected void drawTitle() {
		int i = (this.width - this.field_146555_f) / 2;
		int j = (this.height - this.field_146557_g) / 2;
		this.fontRendererObj.drawString(I18n.format("gui.achievements", new Object[0]), i + 15, j + 5, 4210752);
	}

	protected void drawAchievementScreen(int parInt1, int parInt2, float parFloat1) {
		int i = MathHelper
				.floor_double(this.field_146569_s + (this.field_146567_u - this.field_146569_s) * (double) parFloat1);
		int j = MathHelper
				.floor_double(this.field_146568_t + (this.field_146566_v - this.field_146568_t) * (double) parFloat1);
		if (i < field_146572_y) {
			i = field_146572_y;
		}

		if (j < field_146571_z) {
			j = field_146571_z;
		}

		if (i >= field_146559_A) {
			i = field_146559_A - 1;
		}

		if (j >= field_146560_B) {
			j = field_146560_B - 1;
		}

		int k = (this.width - this.field_146555_f) / 2;
		int l = (this.height - this.field_146557_g) / 2;
		int i1 = k + 16;
		int j1 = l + 17;
		this.zLevel = 0.0F;
		GlStateManager.enableDepth();
		GlStateManager.clearDepth(0.0f);
		GlStateManager.clear(GL_DEPTH_BUFFER_BIT);
		GlStateManager.clearDepth(1.0f);
		GlStateManager.depthFunc(GL_GEQUAL);
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) i1, (float) j1, -200.0F);
		GlStateManager.scale(1.0F / this.field_146570_r, 1.0F / this.field_146570_r, 0.0F);
		GlStateManager.enableTexture2D();
		GlStateManager.disableLighting();
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableColorMaterial();
		int k1 = i + 288 >> 4;
		int l1 = j + 288 >> 4;
		int i2 = (i + 288) % 16;
		int j2 = (j + 288) % 16;
		boolean flag = true;
		boolean flag1 = true;
		boolean flag2 = true;
		boolean flag3 = true;
		boolean flag4 = true;
		EaglercraftRandom random = new EaglercraftRandom();
		float f = 16.0F / this.field_146570_r;
		float f1 = 16.0F / this.field_146570_r;

		for (int k2 = 0; (float) k2 * f - (float) j2 < 155.0F; ++k2) {
			float f2 = 0.6F - (float) (l1 + k2) / 25.0F * 0.3F;
			GlStateManager.color(f2, f2, f2, 1.0F);

			for (int l2 = 0; (float) l2 * f1 - (float) i2 < 224.0F; ++l2) {
				random.setSeed(
						(long) (this.mc.getSession().getProfile().getId().hashCode() + k1 + l2 + (l1 + k2) * 16));
				int i3 = random.nextInt(1 + l1 + k2) + (l1 + k2) / 2;
				EaglerTextureAtlasSprite textureatlassprite = this.func_175371_a(Blocks.sand);
				if (i3 <= 37 && l1 + k2 != 35) {
					if (i3 == 22) {
						if (random.nextInt(2) == 0) {
							textureatlassprite = this.func_175371_a(Blocks.diamond_ore);
						} else {
							textureatlassprite = this.func_175371_a(Blocks.redstone_ore);
						}
					} else if (i3 == 10) {
						textureatlassprite = this.func_175371_a(Blocks.iron_ore);
					} else if (i3 == 8) {
						textureatlassprite = this.func_175371_a(Blocks.coal_ore);
					} else if (i3 > 4) {
						textureatlassprite = this.func_175371_a(Blocks.stone);
					} else if (i3 > 0) {
						textureatlassprite = this.func_175371_a(Blocks.dirt);
					}
				} else {
					Block block = Blocks.bedrock;
					textureatlassprite = this.func_175371_a(block);
				}

				this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
				this.drawTexturedModalRect(l2 * 16 - i2, k2 * 16 - j2, textureatlassprite, 16, 16);
			}
		}

		GlStateManager.depthFunc(GL_LEQUAL);
		this.mc.getTextureManager().bindTexture(ACHIEVEMENT_BACKGROUND);

		for (int i4 = 0; i4 < AchievementList.achievementList.size(); ++i4) {
			Achievement achievement1 = (Achievement) AchievementList.achievementList.get(i4);
			if (achievement1.parentAchievement != null) {
				int j4 = achievement1.displayColumn * 24 - i + 11;
				int k4 = achievement1.displayRow * 24 - j + 11;
				int i5 = achievement1.parentAchievement.displayColumn * 24 - i + 11;
				int j5 = achievement1.parentAchievement.displayRow * 24 - j + 11;
				boolean flag5 = this.statFileWriter.hasAchievementUnlocked(achievement1);
				boolean flag6 = this.statFileWriter.canUnlockAchievement(achievement1);
				int j3 = this.statFileWriter.func_150874_c(achievement1);
				if (j3 <= 4) {
					int k3 = -16777216;
					if (flag5) {
						k3 = -6250336;
					} else if (flag6) {
						k3 = -16711936;
					}

					this.drawHorizontalLine(j4, i5, k4, k3);
					this.drawVerticalLine(i5, k4, j5, k3);
					if (j4 > i5) {
						this.drawTexturedModalRect(j4 - 11 - 7, k4 - 5, 114, 234, 7, 11);
					} else if (j4 < i5) {
						this.drawTexturedModalRect(j4 + 11, k4 - 5, 107, 234, 7, 11);
					} else if (k4 > j5) {
						this.drawTexturedModalRect(j4 - 5, k4 - 11 - 7, 96, 234, 11, 7);
					} else if (k4 < j5) {
						this.drawTexturedModalRect(j4 - 5, k4 + 11, 96, 241, 11, 7);
					}
				}
			}
		}

		Achievement achievement = null;
		float f3 = (float) (parInt1 - i1) * this.field_146570_r;
		float f4 = (float) (parInt2 - j1) * this.field_146570_r;
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableColorMaterial();

		for (int l4 = 0; l4 < AchievementList.achievementList.size(); ++l4) {
			Achievement achievement2 = (Achievement) AchievementList.achievementList.get(l4);
			int k5 = achievement2.displayColumn * 24 - i;
			int i6 = achievement2.displayRow * 24 - j;
			if (k5 >= -24 && i6 >= -24 && (float) k5 <= 224.0F * this.field_146570_r
					&& (float) i6 <= 155.0F * this.field_146570_r) {
				int k6 = this.statFileWriter.func_150874_c(achievement2);
				if (this.statFileWriter.hasAchievementUnlocked(achievement2)) {
					float f5 = 0.75F;
					GlStateManager.color(f5, f5, f5, 1.0F);
				} else if (this.statFileWriter.canUnlockAchievement(achievement2)) {
					float f6 = 1.0F;
					GlStateManager.color(f6, f6, f6, 1.0F);
				} else if (k6 < 3) {
					float f7 = 0.3F;
					GlStateManager.color(f7, f7, f7, 1.0F);
				} else if (k6 == 3) {
					float f8 = 0.2F;
					GlStateManager.color(f8, f8, f8, 1.0F);
				} else {
					if (k6 != 4) {
						continue;
					}

					float f9 = 0.1F;
					GlStateManager.color(f9, f9, f9, 1.0F);
				}

				this.mc.getTextureManager().bindTexture(ACHIEVEMENT_BACKGROUND);
				if (achievement2.getSpecial()) {
					this.drawTexturedModalRect(k5 - 2, i6 - 2, 26, 202, 26, 26);
				} else {
					this.drawTexturedModalRect(k5 - 2, i6 - 2, 0, 202, 26, 26);
				}

				if (!this.statFileWriter.canUnlockAchievement(achievement2)) {
					float f10 = 0.1F;
					GlStateManager.color(f10, f10, f10, 1.0F);
					this.itemRender.func_175039_a(false);
				}

				GlStateManager.enableLighting();
				GlStateManager.enableCull();
				this.itemRender.renderItemAndEffectIntoGUI(achievement2.theItemStack, k5 + 3, i6 + 3);
				GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				GlStateManager.disableLighting();
				if (!this.statFileWriter.canUnlockAchievement(achievement2)) {
					this.itemRender.func_175039_a(true);
				}

				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				if (f3 >= (float) k5 && f3 <= (float) (k5 + 22) && f4 >= (float) i6 && f4 <= (float) (i6 + 22)) {
					achievement = achievement2;
				}
			}
		}

		GlStateManager.disableDepth();
		GlStateManager.enableBlend();
		GlStateManager.popMatrix();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(ACHIEVEMENT_BACKGROUND);
		this.drawTexturedModalRect(k, l, 0, 0, this.field_146555_f, this.field_146557_g);
		this.zLevel = 0.0F;
		GlStateManager.depthFunc(GL_LEQUAL);
		GlStateManager.disableDepth();
		GlStateManager.enableTexture2D();
		super.drawScreen(parInt1, parInt2, parFloat1);
		if (achievement != null) {
			String s = achievement.getStatName().getUnformattedText();
			String s1 = achievement.getDescription();
			int l5 = parInt1 + 12;
			int j6 = parInt2 - 4;
			int l6 = this.statFileWriter.func_150874_c(achievement);
			if (this.statFileWriter.canUnlockAchievement(achievement)) {
				int i7 = Math.max(this.fontRendererObj.getStringWidth(s), 120);
				int l7 = this.fontRendererObj.splitStringWidth(s1, i7);
				if (this.statFileWriter.hasAchievementUnlocked(achievement)) {
					l7 += 12;
				}

				this.drawGradientRect(l5 - 3, j6 - 3, l5 + i7 + 3, j6 + l7 + 3 + 12, -1073741824, -1073741824);
				this.fontRendererObj.drawSplitString(s1, l5, j6 + 12, i7, -6250336);
				if (this.statFileWriter.hasAchievementUnlocked(achievement)) {
					this.fontRendererObj.drawStringWithShadow(I18n.format("achievement.taken", new Object[0]),
							(float) l5, (float) (j6 + l7 + 4), -7302913);
				}
			} else if (l6 == 3) {
				s = I18n.format("achievement.unknown", new Object[0]);
				int j7 = Math.max(this.fontRendererObj.getStringWidth(s), 120);
				String s2 = (new ChatComponentTranslation("achievement.requires",
						new Object[] { achievement.parentAchievement.getStatName() })).getUnformattedText();
				int l3 = this.fontRendererObj.splitStringWidth(s2, j7);
				this.drawGradientRect(l5 - 3, j6 - 3, l5 + j7 + 3, j6 + l3 + 12 + 3, -1073741824, -1073741824);
				this.fontRendererObj.drawSplitString(s2, l5, j6 + 12, j7, -9416624);
			} else if (l6 < 3) {
				int k7 = Math.max(this.fontRendererObj.getStringWidth(s), 120);
				String s3 = (new ChatComponentTranslation("achievement.requires",
						new Object[] { achievement.parentAchievement.getStatName() })).getUnformattedText();
				int i8 = this.fontRendererObj.splitStringWidth(s3, k7);
				this.drawGradientRect(l5 - 3, j6 - 3, l5 + k7 + 3, j6 + i8 + 12 + 3, -1073741824, -1073741824);
				this.fontRendererObj.drawSplitString(s3, l5, j6 + 12, k7, -9416624);
			} else {
				s = null;
			}

			if (s != null) {
				this.fontRendererObj.drawStringWithShadow(s, (float) l5, (float) j6,
						this.statFileWriter.canUnlockAchievement(achievement) ? (achievement.getSpecial() ? -128 : -1)
								: (achievement.getSpecial() ? -8355776 : -8355712));
			}
		}

		GlStateManager.enableDepth();
		GlStateManager.disableBlend();
		RenderHelper.disableStandardItemLighting();
	}

	private EaglerTextureAtlasSprite func_175371_a(Block parBlock) {
		return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes()
				.getTexture(parBlock.getDefaultState());
	}

	/**+
	 * Returns true if this GUI should pause the game when it is
	 * displayed in single-player
	 */
	public boolean doesGuiPauseGame() {
		return !this.loadingAchievements;
	}
}