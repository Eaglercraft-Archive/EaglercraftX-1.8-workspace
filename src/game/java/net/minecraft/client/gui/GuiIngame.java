package net.minecraft.client.gui;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.util.ArrayList;
import java.util.Collection;

import net.lax1dude.eaglercraft.v1_8.Display;
import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.lax1dude.eaglercraft.v1_8.PointerInputAbstraction;
import net.lax1dude.eaglercraft.v1_8.Touch;
import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.lax1dude.eaglercraft.v1_8.touch_gui.TouchControls;
import net.lax1dude.eaglercraft.v1_8.touch_gui.TouchOverlayRenderer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.potion.Potion;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.FoodStats;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.border.WorldBorder;

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
public class GuiIngame extends Gui {
	private static final ResourceLocation vignetteTexPath = new ResourceLocation("textures/misc/vignette.png");
	private static final ResourceLocation widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");
	private static final ResourceLocation pumpkinBlurTexPath = new ResourceLocation("textures/misc/pumpkinblur.png");
	private final EaglercraftRandom rand = new EaglercraftRandom();
	private final Minecraft mc;
	private final RenderItem itemRenderer;
	private final GuiNewChat persistantChatGUI;
	private int updateCounter;
	/**+
	 * The string specifying which record music is playing
	 */
	private String recordPlaying = "";
	private int recordPlayingUpFor;
	private boolean recordIsPlaying;
	/**+
	 * Previous frame vignette brightness (slowly changes by 1% each
	 * frame)
	 */
	public float prevVignetteBrightness = 1.0F;
	private int remainingHighlightTicks;
	private ItemStack highlightingItemStack;
	public final GuiOverlayDebug overlayDebug;
	private final GuiSpectator spectatorGui;
	private final GuiPlayerTabOverlay overlayPlayerList;
	private int field_175195_w;
	private String field_175201_x = "";
	private String field_175200_y = "";
	private int field_175199_z;
	private int field_175192_A;
	private int field_175193_B;
	private int playerHealth = 0;
	private int lastPlayerHealth = 0;
	/**+
	 * The last recorded system time
	 */
	private long lastSystemTime = 0L;
	/**+
	 * Used with updateCounter to make the heart bar flash
	 */
	private long healthUpdateCounter = 0L;

	public GuiIngame(Minecraft mcIn) {
		this.mc = mcIn;
		this.itemRenderer = mcIn.getRenderItem();
		this.overlayDebug = new GuiOverlayDebug(mcIn);
		this.spectatorGui = new GuiSpectator(mcIn);
		this.persistantChatGUI = new GuiNewChat(mcIn);
		this.overlayPlayerList = new GuiPlayerTabOverlay(mcIn, this);
		this.func_175177_a();
	}

	public void func_175177_a() {
		this.field_175199_z = 10;
		this.field_175192_A = 70;
		this.field_175193_B = 20;
	}

	public void renderGameOverlay(float partialTicks) {
		ScaledResolution scaledresolution = mc.scaledResolution;
		int i = scaledresolution.getScaledWidth();
		int j = scaledresolution.getScaledHeight();
		this.mc.entityRenderer.setupOverlayRendering();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		GlStateManager.enableDepth();
		GlStateManager.disableLighting();

		ItemStack itemstack = this.mc.thePlayer.inventory.armorItemInSlot(3);
		if (this.mc.gameSettings.thirdPersonView == 0 && itemstack != null
				&& itemstack.getItem() == Item.getItemFromBlock(Blocks.pumpkin)) {
			this.renderPumpkinOverlay(scaledresolution);
		}

		if (!this.mc.thePlayer.isPotionActive(Potion.confusion)) {
			float f = this.mc.thePlayer.prevTimeInPortal
					+ (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * partialTicks;
			if (f > 0.0F) {
				this.func_180474_b(f, scaledresolution);
			}
		}

		onBeginHotbarDraw();

		if (this.mc.playerController.isSpectator()) {
			this.spectatorGui.renderTooltip(scaledresolution, partialTicks);
		} else {
			this.renderTooltip(scaledresolution, partialTicks);
		}

		this.mc.getTextureManager().bindTexture(icons);
		GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		this.renderBossHealth();
		if (this.mc.playerController.shouldDrawHUD()) {
			this.renderPlayerStats(scaledresolution);
		}

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableBlend();
		int k1 = i / 2 - 91;
		if (this.mc.thePlayer.isRidingHorse()) {
			this.renderHorseJumpBar(scaledresolution, k1);
		} else if (this.mc.playerController.gameIsSurvivalOrAdventure()) {
			this.renderExpBar(scaledresolution, k1);
		}

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableBlend();
		if (this.mc.gameSettings.heldItemTooltips && !this.mc.playerController.isSpectator()) {
			this.func_181551_a(scaledresolution);
		} else if (this.mc.thePlayer.isSpectator()) {
			this.spectatorGui.func_175263_a(scaledresolution);
		}

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableBlend();
		if (this.recordPlayingUpFor > 0) {
			float f2 = (float) this.recordPlayingUpFor - partialTicks;
			int l1 = (int) (f2 * 255.0F / 20.0F);
			if (l1 > 255) {
				l1 = 255;
			}

			if (l1 > 8) {
				GlStateManager.pushMatrix();
				GlStateManager.translate((float) (i / 2), (float) (j - 68), 0.0F);
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
				int l = 16777215;
				if (this.recordIsPlaying) {
					l = MathHelper.func_181758_c(f2 / 50.0F, 0.7F, 0.6F) & 16777215;
				}

				this.getFontRenderer().drawString(this.recordPlaying,
						-this.getFontRenderer().getStringWidth(this.recordPlaying) / 2, -4, l + (l1 << 24 & -16777216));
				GlStateManager.disableBlend();
				GlStateManager.popMatrix();
			}
		}

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		drawEaglerInteractButton(scaledresolution);

		onEndHotbarDraw();

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		if (this.mc.thePlayer.getSleepTimer() > 0) {
			GlStateManager.disableDepth();
			GlStateManager.disableAlpha();
			int j1 = this.mc.thePlayer.getSleepTimer();
			float f1 = (float) j1 / 100.0F;
			if (f1 > 1.0F) {
				f1 = 1.0F - (float) (j1 - 100) / 10.0F;
			}

			int k = (int) (220.0F * f1) << 24 | 1052704;
			drawRect(0, 0, i, j, k);
			GlStateManager.enableAlpha();
			GlStateManager.enableDepth();
		}

		if (this.mc.isDemo()) {
			this.renderDemo(scaledresolution);
		}

		this.overlayDebug.renderDebugInfo(scaledresolution);

		if (this.field_175195_w > 0) {
			float f3 = (float) this.field_175195_w - partialTicks;
			int i2 = 255;
			if (this.field_175195_w > this.field_175193_B + this.field_175192_A) {
				float f4 = (float) (this.field_175199_z + this.field_175192_A + this.field_175193_B) - f3;
				i2 = (int) (f4 * 255.0F / (float) this.field_175199_z);
			}

			if (this.field_175195_w <= this.field_175193_B) {
				i2 = (int) (f3 * 255.0F / (float) this.field_175193_B);
			}

			i2 = MathHelper.clamp_int(i2, 0, 255);
			if (i2 > 8) {
				GlStateManager.pushMatrix();
				GlStateManager.translate((float) (i / 2), (float) (j / 2), 0.0F);
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
				GlStateManager.pushMatrix();
				GlStateManager.scale(4.0F, 4.0F, 4.0F);
				int j2 = i2 << 24 & -16777216;
				this.getFontRenderer().drawString(this.field_175201_x,
						(float) (-this.getFontRenderer().getStringWidth(this.field_175201_x) / 2), -10.0F,
						16777215 | j2, true);
				GlStateManager.popMatrix();
				GlStateManager.pushMatrix();
				GlStateManager.scale(2.0F, 2.0F, 2.0F);
				this.getFontRenderer().drawString(this.field_175200_y,
						(float) (-this.getFontRenderer().getStringWidth(this.field_175200_y) / 2), 5.0F, 16777215 | j2,
						true);
				GlStateManager.popMatrix();
				GlStateManager.disableBlend();
				GlStateManager.popMatrix();
			}
		}

		Scoreboard scoreboard = this.mc.theWorld.getScoreboard();
		ScoreObjective scoreobjective = null;
		ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(this.mc.thePlayer.getName());
		if (scoreplayerteam != null) {
			int i1 = scoreplayerteam.getChatFormat().getColorIndex();
			if (i1 >= 0) {
				scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + i1);
			}
		}

		ScoreObjective scoreobjective1 = scoreobjective != null ? scoreobjective
				: scoreboard.getObjectiveInDisplaySlot(1);
		if (scoreobjective1 != null) {
			this.renderScoreboard(scoreobjective1, scaledresolution);
		}

		if (this.mc.currentScreen == null) {
			this.mc.voiceOverlay.drawOverlay();
		}

		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		GlStateManager.disableAlpha();
		GlStateManager.pushMatrix();
		if (this.mc.gameSettings.hudWorld && (mc.currentScreen == null || !(mc.currentScreen instanceof GuiChat))) {
			j -= 10;
		}
		j -= (this.mc.displayHeight - (Display.getVisualViewportX() + Display.getVisualViewportH())) * j
				/ this.mc.displayHeight;
		GlStateManager.translate(0.0F, (float) (j - 48), 0.0F);
		this.persistantChatGUI.drawChat(this.updateCounter);
		GlStateManager.popMatrix();
		scoreobjective1 = scoreboard.getObjectiveInDisplaySlot(0);
		if (!this.mc.gameSettings.keyBindPlayerList.isKeyDown() || this.mc.isIntegratedServerRunning()
				&& this.mc.thePlayer.sendQueue.getPlayerInfoMap().size() <= 1 && scoreobjective1 == null) {
			this.overlayPlayerList.updatePlayerList(false);
		} else {
			this.overlayPlayerList.updatePlayerList(true);
			this.overlayPlayerList.renderPlayerlist(i, scoreboard, scoreobjective1);
		}

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		GlStateManager.disableLighting();
		GlStateManager.enableAlpha();
	}

	public void renderGameOverlayCrosshairs(int scaledResWidth, int scaledResHeight) {
		if (this.showCrosshair()) {
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.getTextureManager().bindTexture(icons);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GL_ONE_MINUS_DST_COLOR, GL_ONE_MINUS_SRC_COLOR, 1, 0);
			GlStateManager.enableAlpha();
			this.drawTexturedModalRect(scaledResWidth / 2 - 7, scaledResHeight / 2 - 7, 0, 0, 16, 16);
		}
	}

	protected void renderTooltip(ScaledResolution sr, float partialTicks) {
		if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.getTextureManager().bindTexture(widgetsTexPath);
			EntityPlayer entityplayer = (EntityPlayer) this.mc.getRenderViewEntity();
			int i = sr.getScaledWidth() / 2;
			float f = this.zLevel;
			this.zLevel = -90.0F;
			this.drawTexturedModalRect(i - 91, sr.getScaledHeight() - 22, 0, 0, 182, 22);

			if (PointerInputAbstraction.isTouchMode()) {
				this.mc.getTextureManager().bindTexture(TouchOverlayRenderer.spriteSheet);
				this.drawTexturedModalRect(i + 89, sr.getScaledHeight() - 22, 234, 0, 22, 22);
				int areaHAdd = 12;
				hotbarAreaX = (i - 91) * mc.displayWidth / sr.getScaledWidth();
				hotbarAreaY = (sr.getScaledHeight() - 22 - areaHAdd) * mc.displayHeight / sr.getScaledHeight();
				hotbarAreaW = 203 * mc.displayWidth / sr.getScaledWidth();
				hotbarAreaH = (22 + areaHAdd) * mc.displayHeight / sr.getScaledHeight();
			} else {
				hotbarAreaX = -1;
				hotbarAreaY = -1;
				hotbarAreaW = -1;
				hotbarAreaH = -1;
			}

			this.mc.getTextureManager().bindTexture(widgetsTexPath);
			this.drawTexturedModalRect(i - 91 - 1 + entityplayer.inventory.currentItem * 20,
					sr.getScaledHeight() - 22 - 1, 0, 22, 24, 22);
			this.zLevel = f;
			GlStateManager.enableRescaleNormal();
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
			RenderHelper.enableGUIStandardItemLighting();

			for (int j = 0; j < 9; ++j) {
				int k = sr.getScaledWidth() / 2 - 90 + j * 20 + 2;
				int l = sr.getScaledHeight() - 16 - 3;
				this.renderHotbarItem(j, k, l, partialTicks, entityplayer);
			}

			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableRescaleNormal();

			GlStateManager.disableBlend();
		}
	}

	public void renderHorseJumpBar(ScaledResolution parScaledResolution, int parInt1) {
		this.mc.getTextureManager().bindTexture(Gui.icons);
		float f = this.mc.thePlayer.getHorseJumpPower();
		short short1 = 182;
		int i = (int) (f * (float) (short1 + 1));
		int j = parScaledResolution.getScaledHeight() - 32 + 3;
		this.drawTexturedModalRect(parInt1, j, 0, 84, short1, 5);
		if (i > 0) {
			this.drawTexturedModalRect(parInt1, j, 0, 89, i, 5);
		}
	}

	public void renderExpBar(ScaledResolution parScaledResolution, int parInt1) {
		this.mc.getTextureManager().bindTexture(Gui.icons);
		int i = this.mc.thePlayer.xpBarCap();
		if (i > 0) {
			short short1 = 182;
			int j = (int) (this.mc.thePlayer.experience * (float) (short1 + 1));
			int k = parScaledResolution.getScaledHeight() - 32 + 3;
			this.drawTexturedModalRect(parInt1, k, 0, 64, short1, 5);
			if (j > 0) {
				this.drawTexturedModalRect(parInt1, k, 0, 69, j, 5);
			}
		}

		if (this.mc.thePlayer.experienceLevel > 0) {
			int i1 = 8453920;
			String s = "" + this.mc.thePlayer.experienceLevel;
			int j1 = (parScaledResolution.getScaledWidth() - this.getFontRenderer().getStringWidth(s)) / 2;
			int l = parScaledResolution.getScaledHeight() - 31 - 4;
			boolean flag = false;
			this.getFontRenderer().drawString(s, j1 + 1, l, 0);
			this.getFontRenderer().drawString(s, j1 - 1, l, 0);
			this.getFontRenderer().drawString(s, j1, l + 1, 0);
			this.getFontRenderer().drawString(s, j1, l - 1, 0);
			this.getFontRenderer().drawString(s, j1, l, i1);
		}

	}

	public void func_181551_a(ScaledResolution parScaledResolution) {
		if (this.remainingHighlightTicks > 0 && this.highlightingItemStack != null) {
			String s = this.highlightingItemStack.getDisplayNameProfanityFilter();
			if (this.highlightingItemStack.hasDisplayName()) {
				s = EnumChatFormatting.ITALIC + s;
			}

			int i = (parScaledResolution.getScaledWidth() - this.getFontRenderer().getStringWidth(s)) / 2;
			int j = parScaledResolution.getScaledHeight() - 59;
			if (!this.mc.playerController.shouldDrawHUD()) {
				j += 14;
			}

			int k = (int) ((float) this.remainingHighlightTicks * 256.0F / 10.0F);
			if (k > 255) {
				k = 255;
			}

			if (k > 0) {
				GlStateManager.pushMatrix();
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
				this.getFontRenderer().drawStringWithShadow(s, (float) i, (float) j, 16777215 + (k << 24));
				GlStateManager.disableBlend();
				GlStateManager.popMatrix();
			}
		}

	}

	public void renderDemo(ScaledResolution parScaledResolution) {
		String s = "";
		if (this.mc.theWorld.getTotalWorldTime() >= 120500L) {
			s = I18n.format("demo.demoExpired", new Object[0]);
		} else {
			s = I18n.format("demo.remainingTime", new Object[] {
					StringUtils.ticksToElapsedTime((int) (120500L - this.mc.theWorld.getTotalWorldTime())) });
		}

		int i = this.getFontRenderer().getStringWidth(s);
		this.getFontRenderer().drawStringWithShadow(s, (float) (parScaledResolution.getScaledWidth() - i - 10), 5.0F,
				16777215);
	}

	protected boolean showCrosshair() {
		if (this.mc.gameSettings.showDebugInfo && !this.mc.thePlayer.hasReducedDebug()
				&& !this.mc.gameSettings.reducedDebugInfo) {
			return false;
		} else if (this.mc.playerController.isSpectator()) {
			if (this.mc.pointedEntity != null) {
				return true;
			} else {
				if (this.mc.objectMouseOver != null
						&& this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
					BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
					if (this.mc.theWorld.getTileEntity(blockpos) instanceof IInventory) {
						return true;
					}
				}

				return false;
			}
		} else {
			return true;
		}
	}

	private void renderScoreboard(ScoreObjective parScoreObjective, ScaledResolution parScaledResolution) {
		Scoreboard scoreboard = parScoreObjective.getScoreboard();
		Collection collection = scoreboard.getSortedScores(parScoreObjective);
		ArrayList arraylist = Lists.newArrayList(Iterables.filter(collection, new Predicate<Score>() {
			public boolean apply(Score score2) {
				return score2.getPlayerName() != null && !score2.getPlayerName().startsWith("#");
			}
		}));
		ArrayList arraylist1;
		if (arraylist.size() > 15) {
			arraylist1 = Lists.newArrayList(Iterables.skip(arraylist, collection.size() - 15));
		} else {
			arraylist1 = arraylist;
		}

		int i = this.getFontRenderer().getStringWidth(parScoreObjective.getDisplayName());

		for (int m = 0, n = arraylist1.size(); m < n; ++m) {
			Score score = (Score) arraylist1.get(m);
			ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
			String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": "
					+ EnumChatFormatting.RED + score.getScorePoints();
			i = Math.max(i, this.getFontRenderer().getStringWidth(s));
		}

		int i1 = arraylist1.size() * this.getFontRenderer().FONT_HEIGHT;
		int j1 = parScaledResolution.getScaledHeight() / 2 + i1 / 3;
		byte b0 = 3;
		int k1 = parScaledResolution.getScaledWidth() - i - b0;
		int j = 0;

		for (int m = 0, n = arraylist1.size(); m < n; ++m) {
			Score score1 = (Score) arraylist1.get(m);
			++j;
			ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
			String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
			String s2 = EnumChatFormatting.RED + "" + score1.getScorePoints();
			int k = j1 - j * this.getFontRenderer().FONT_HEIGHT;
			int l = parScaledResolution.getScaledWidth() - b0 + 2;
			drawRect(k1 - 2, k, l, k + this.getFontRenderer().FONT_HEIGHT, 1342177280);
			this.getFontRenderer().drawString(s1, k1, k, 0xFFFFFFFF);
			this.getFontRenderer().drawString(s2, l - this.getFontRenderer().getStringWidth(s2), k, 0xFFFFFFFF);
			if (j == arraylist1.size()) {
				String s3 = parScoreObjective.getDisplayName();
				drawRect(k1 - 2, k - this.getFontRenderer().FONT_HEIGHT - 1, l, k - 1, 1610612736);
				drawRect(k1 - 2, k - 1, l, k, 1342177280);
				this.getFontRenderer().drawString(s3, k1 + i / 2 - this.getFontRenderer().getStringWidth(s3) / 2,
						k - this.getFontRenderer().FONT_HEIGHT, 0xFFFFFFFF);
			}
		}

	}

	private void renderPlayerStats(ScaledResolution parScaledResolution) {
		if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) this.mc.getRenderViewEntity();
			int i = MathHelper.ceiling_float_int(entityplayer.getHealth());
			boolean flag = this.healthUpdateCounter > (long) this.updateCounter
					&& (this.healthUpdateCounter - (long) this.updateCounter) / 3L % 2L == 1L;
			if (i < this.playerHealth && entityplayer.hurtResistantTime > 0) {
				this.lastSystemTime = Minecraft.getSystemTime();
				this.healthUpdateCounter = (long) (this.updateCounter + 20);
			} else if (i > this.playerHealth && entityplayer.hurtResistantTime > 0) {
				this.lastSystemTime = Minecraft.getSystemTime();
				this.healthUpdateCounter = (long) (this.updateCounter + 10);
			}

			if (Minecraft.getSystemTime() - this.lastSystemTime > 1000L) {
				this.playerHealth = i;
				this.lastPlayerHealth = i;
				this.lastSystemTime = Minecraft.getSystemTime();
			}

			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
			this.playerHealth = i;
			int j = this.lastPlayerHealth;
			this.rand.setSeed((long) (this.updateCounter * 312871));
			boolean flag1 = false;
			FoodStats foodstats = entityplayer.getFoodStats();
			int k = foodstats.getFoodLevel();
			int l = foodstats.getPrevFoodLevel();
			IAttributeInstance iattributeinstance = entityplayer.getEntityAttribute(SharedMonsterAttributes.maxHealth);
			int i1 = parScaledResolution.getScaledWidth() / 2 - 91;
			int j1 = parScaledResolution.getScaledWidth() / 2 + 91;
			int k1 = parScaledResolution.getScaledHeight() - 39;
			float f = (float) iattributeinstance.getAttributeValue();
			float f1 = entityplayer.getAbsorptionAmount();
			int l1 = MathHelper.ceiling_float_int((f + f1) / 2.0F / 10.0F);
			int i2 = Math.max(10 - (l1 - 2), 3);
			int j2 = k1 - (l1 - 1) * i2 - 10;
			float f2 = f1;
			int k2 = entityplayer.getTotalArmorValue();
			int l2 = -1;
			if (entityplayer.isPotionActive(Potion.regeneration)) {
				l2 = this.updateCounter % MathHelper.ceiling_float_int(f + 5.0F);
			}

			for (int i3 = 0; i3 < 10; ++i3) {
				if (k2 > 0) {
					int j3 = i1 + i3 * 8;
					if (i3 * 2 + 1 < k2) {
						this.drawTexturedModalRect(j3, j2, 34, 9, 9, 9);
					}

					if (i3 * 2 + 1 == k2) {
						this.drawTexturedModalRect(j3, j2, 25, 9, 9, 9);
					}

					if (i3 * 2 + 1 > k2) {
						this.drawTexturedModalRect(j3, j2, 16, 9, 9, 9);
					}
				}
			}

			for (int i5 = MathHelper.ceiling_float_int((f + f1) / 2.0F) - 1; i5 >= 0; --i5) {
				int j5 = 16;
				if (entityplayer.isPotionActive(Potion.poison)) {
					j5 += 36;
				} else if (entityplayer.isPotionActive(Potion.wither)) {
					j5 += 72;
				}

				byte b0 = 0;
				if (flag) {
					b0 = 1;
				}

				int k3 = MathHelper.ceiling_float_int((float) (i5 + 1) / 10.0F) - 1;
				int l3 = i1 + i5 % 10 * 8;
				int i4 = k1 - k3 * i2;
				if (i <= 4) {
					i4 += this.rand.nextInt(2);
				}

				if (i5 == l2) {
					i4 -= 2;
				}

				byte b1 = 0;
				if (entityplayer.worldObj.getWorldInfo().isHardcoreModeEnabled()) {
					b1 = 5;
				}

				this.drawTexturedModalRect(l3, i4, 16 + b0 * 9, 9 * b1, 9, 9);
				if (flag) {
					if (i5 * 2 + 1 < j) {
						this.drawTexturedModalRect(l3, i4, j5 + 54, 9 * b1, 9, 9);
					}

					if (i5 * 2 + 1 == j) {
						this.drawTexturedModalRect(l3, i4, j5 + 63, 9 * b1, 9, 9);
					}
				}

				if (f2 > 0.0F) {
					if (f2 == f1 && f1 % 2.0F == 1.0F) {
						this.drawTexturedModalRect(l3, i4, j5 + 153, 9 * b1, 9, 9);
					} else {
						this.drawTexturedModalRect(l3, i4, j5 + 144, 9 * b1, 9, 9);
					}

					f2 -= 2.0F;
				} else {
					if (i5 * 2 + 1 < i) {
						this.drawTexturedModalRect(l3, i4, j5 + 36, 9 * b1, 9, 9);
					}

					if (i5 * 2 + 1 == i) {
						this.drawTexturedModalRect(l3, i4, j5 + 45, 9 * b1, 9, 9);
					}
				}
			}

			Entity entity = entityplayer.ridingEntity;
			if (entity == null) {
				for (int k5 = 0; k5 < 10; ++k5) {
					int i6 = k1;
					int l6 = 16;
					byte b4 = 0;
					if (entityplayer.isPotionActive(Potion.hunger)) {
						l6 += 36;
						b4 = 13;
					}

					if (entityplayer.getFoodStats().getSaturationLevel() <= 0.0F
							&& this.updateCounter % (k * 3 + 1) == 0) {
						i6 = k1 + (this.rand.nextInt(3) - 1);
					}

					if (flag1) {
						b4 = 1;
					}

					int l7 = j1 - k5 * 8 - 9;
					this.drawTexturedModalRect(l7, i6, 16 + b4 * 9, 27, 9, 9);
					if (flag1) {
						if (k5 * 2 + 1 < l) {
							this.drawTexturedModalRect(l7, i6, l6 + 54, 27, 9, 9);
						}

						if (k5 * 2 + 1 == l) {
							this.drawTexturedModalRect(l7, i6, l6 + 63, 27, 9, 9);
						}
					}

					if (k5 * 2 + 1 < k) {
						this.drawTexturedModalRect(l7, i6, l6 + 36, 27, 9, 9);
					}

					if (k5 * 2 + 1 == k) {
						this.drawTexturedModalRect(l7, i6, l6 + 45, 27, 9, 9);
					}
				}
			} else if (entity instanceof EntityLivingBase) {
				EntityLivingBase entitylivingbase = (EntityLivingBase) entity;
				int j6 = (int) Math.ceil((double) entitylivingbase.getHealth());
				float f3 = entitylivingbase.getMaxHealth();
				int j7 = (int) (f3 + 0.5F) / 2;
				if (j7 > 30) {
					j7 = 30;
				}

				int i8 = k1;

				for (int j8 = 0; j7 > 0; j8 += 20) {
					int j4 = Math.min(j7, 10);
					j7 -= j4;

					for (int k4 = 0; k4 < j4; ++k4) {
						byte b2 = 52;
						byte b3 = 0;
						if (flag1) {
							b3 = 1;
						}

						int l4 = j1 - k4 * 8 - 9;
						this.drawTexturedModalRect(l4, i8, b2 + b3 * 9, 9, 9, 9);
						if (k4 * 2 + 1 + j8 < j6) {
							this.drawTexturedModalRect(l4, i8, b2 + 36, 9, 9, 9);
						}

						if (k4 * 2 + 1 + j8 == j6) {
							this.drawTexturedModalRect(l4, i8, b2 + 45, 9, 9, 9);
						}
					}

					i8 -= 10;
				}
			}

			if (entityplayer.isInsideOfMaterial(Material.water)) {
				int l5 = this.mc.thePlayer.getAir();
				int k6 = MathHelper.ceiling_double_int((double) (l5 - 2) * 10.0D / 300.0D);
				int i7 = MathHelper.ceiling_double_int((double) l5 * 10.0D / 300.0D) - k6;

				for (int k7 = 0; k7 < k6 + i7; ++k7) {
					if (k7 < k6) {
						this.drawTexturedModalRect(j1 - k7 * 8 - 9, j2, 16, 18, 9, 9);
					} else {
						this.drawTexturedModalRect(j1 - k7 * 8 - 9, j2, 25, 18, 9, 9);
					}
				}
			}

		}
	}

	/**+
	 * Renders dragon's (boss) health on the HUD
	 */
	private void renderBossHealth() {
		if (BossStatus.bossName != null && BossStatus.statusBarTime > 0) {
			--BossStatus.statusBarTime;
			int i = mc.scaledResolution.getScaledWidth();
			short short1 = 182;
			int j = i / 2 - short1 / 2;
			int k = (int) (BossStatus.healthScale * (float) (short1 + 1));
			byte b0 = 12;
			this.drawTexturedModalRect(j, b0, 0, 74, short1, 5);
			this.drawTexturedModalRect(j, b0, 0, 74, short1, 5);
			if (k > 0) {
				this.drawTexturedModalRect(j, b0, 0, 79, k, 5);
			}

			String s = BossStatus.bossName;
			this.getFontRenderer().drawStringWithShadow(s,
					(float) (i / 2 - this.getFontRenderer().getStringWidth(s) / 2), (float) (b0 - 10), 16777215);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.getTextureManager().bindTexture(icons);
		}
	}

	private void renderPumpkinOverlay(ScaledResolution parScaledResolution) {
		GlStateManager.disableDepth();
		GlStateManager.depthMask(false);
		GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableAlpha();
		this.mc.getTextureManager().bindTexture(pumpkinBlurTexPath);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos(0.0D, (double) parScaledResolution.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
		worldrenderer.pos((double) parScaledResolution.getScaledWidth(), (double) parScaledResolution.getScaledHeight(),
				-90.0D).tex(1.0D, 1.0D).endVertex();
		worldrenderer.pos((double) parScaledResolution.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
		worldrenderer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.depthMask(true);
		GlStateManager.enableDepth();
		GlStateManager.enableAlpha();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}

	/**+
	 * Renders a Vignette arount the entire screen that changes with
	 * light level.
	 */
	public void renderVignette(float parFloat1, int scaledWidth, int scaledHeight) {
		parFloat1 = 1.0F - parFloat1;
		parFloat1 = MathHelper.clamp_float(parFloat1, 0.0F, 1.0F);
		WorldBorder worldborder = this.mc.theWorld.getWorldBorder();
		float f = (float) worldborder.getClosestDistance(this.mc.thePlayer);
		double d0 = Math.min(worldborder.getResizeSpeed() * (double) worldborder.getWarningTime() * 1000.0D,
				Math.abs(worldborder.getTargetSize() - worldborder.getDiameter()));
		double d1 = Math.max((double) worldborder.getWarningDistance(), d0);
		if ((double) f < d1) {
			f = 1.0F - (float) ((double) f / d1);
		} else {
			f = 0.0F;
		}

		this.prevVignetteBrightness = (float) ((double) this.prevVignetteBrightness
				+ (double) (parFloat1 - this.prevVignetteBrightness) * 0.01D);
		GlStateManager.disableDepth();
		GlStateManager.depthMask(false);
		GlStateManager.tryBlendFuncSeparate(0, GL_ONE_MINUS_SRC_COLOR, 1, 0);
		if (f > 0.0F) {
			GlStateManager.color(0.0F, f, f, 1.0F);
		} else {
			GlStateManager.color(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness,
					1.0F);
		}

		this.mc.getTextureManager().bindTexture(vignetteTexPath);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos(0.0D, (double) scaledHeight, -90.0D).tex(0.0D, 1.0D).endVertex();
		worldrenderer.pos((double) scaledWidth, scaledHeight, -90.0D).tex(1.0D, 1.0D).endVertex();
		worldrenderer.pos((double) scaledWidth, 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
		worldrenderer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.depthMask(true);
		GlStateManager.enableDepth();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
	}

	private void func_180474_b(float parFloat1, ScaledResolution parScaledResolution) {
		if (parFloat1 < 1.0F) {
			parFloat1 = parFloat1 * parFloat1;
			parFloat1 = parFloat1 * parFloat1;
			parFloat1 = parFloat1 * 0.8F + 0.2F;
		}

		GlStateManager.disableAlpha();
		GlStateManager.disableDepth();
		GlStateManager.depthMask(false);
		GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		GlStateManager.color(1.0F, 1.0F, 1.0F, parFloat1);
		this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		EaglerTextureAtlasSprite textureatlassprite = this.mc.getBlockRendererDispatcher().getBlockModelShapes()
				.getTexture(Blocks.portal.getDefaultState());
		float f = textureatlassprite.getMinU();
		float f1 = textureatlassprite.getMinV();
		float f2 = textureatlassprite.getMaxU();
		float f3 = textureatlassprite.getMaxV();
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos(0.0D, (double) parScaledResolution.getScaledHeight(), -90.0D).tex((double) f, (double) f3)
				.endVertex();
		worldrenderer.pos((double) parScaledResolution.getScaledWidth(), (double) parScaledResolution.getScaledHeight(),
				-90.0D).tex((double) f2, (double) f3).endVertex();
		worldrenderer.pos((double) parScaledResolution.getScaledWidth(), 0.0D, -90.0D).tex((double) f2, (double) f1)
				.endVertex();
		worldrenderer.pos(0.0D, 0.0D, -90.0D).tex((double) f, (double) f1).endVertex();
		tessellator.draw();
		GlStateManager.depthMask(true);
		GlStateManager.enableDepth();
		GlStateManager.enableAlpha();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}

	private void renderHotbarItem(int index, int xPos, int yPos, float partialTicks, EntityPlayer parEntityPlayer) {
		ItemStack itemstack = parEntityPlayer.inventory.mainInventory[index];
		if (itemstack != null) {
			float f = (float) itemstack.animationsToGo - partialTicks;
			if (f > 0.0F) {
				GlStateManager.pushMatrix();
				float f1 = 1.0F + f / 5.0F;
				GlStateManager.translate((float) (xPos + 8), (float) (yPos + 12), 0.0F);
				GlStateManager.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
				GlStateManager.translate((float) (-(xPos + 8)), (float) (-(yPos + 12)), 0.0F);
			}

			this.itemRenderer.renderItemAndEffectIntoGUI(itemstack, xPos, yPos);
			if (f > 0.0F) {
				GlStateManager.popMatrix();
			}

			this.itemRenderer.renderItemOverlays(this.mc.fontRendererObj, itemstack, xPos, yPos);
		}
	}

	/**+
	 * The update tick for the ingame UI
	 */
	public void updateTick() {
		if (this.recordPlayingUpFor > 0) {
			--this.recordPlayingUpFor;
		}

		if (this.field_175195_w > 0) {
			--this.field_175195_w;
			if (this.field_175195_w <= 0) {
				this.field_175201_x = "";
				this.field_175200_y = "";
			}
		}

		++this.updateCounter;
		if (this.mc.thePlayer != null) {
			ItemStack itemstack = this.mc.thePlayer.inventory.getCurrentItem();
			if (itemstack == null) {
				this.remainingHighlightTicks = 0;
			} else if (this.highlightingItemStack != null && itemstack.getItem() == this.highlightingItemStack.getItem()
					&& ItemStack.areItemStackTagsEqual(itemstack, this.highlightingItemStack)
					&& (itemstack.isItemStackDamageable()
							|| itemstack.getMetadata() == this.highlightingItemStack.getMetadata())) {
				if (this.remainingHighlightTicks > 0) {
					--this.remainingHighlightTicks;
				}
			} else {
				this.remainingHighlightTicks = 40;
			}

			this.highlightingItemStack = itemstack;
		}

	}

	public void setRecordPlayingMessage(String parString1) {
		this.setRecordPlaying(I18n.format("record.nowPlaying", new Object[] { parString1 }), true);
	}

	public void setRecordPlaying(String parString1, boolean parFlag) {
		this.recordPlaying = parString1;
		this.recordPlayingUpFor = 60;
		this.recordIsPlaying = parFlag;
	}

	public void displayTitle(String parString1, String parString2, int parInt1, int parInt2, int parInt3) {
		if (parString1 == null && parString2 == null && parInt1 < 0 && parInt2 < 0 && parInt3 < 0) {
			this.field_175201_x = "";
			this.field_175200_y = "";
			this.field_175195_w = 0;
		} else if (parString1 != null) {
			this.field_175201_x = parString1;
			this.field_175195_w = this.field_175199_z + this.field_175192_A + this.field_175193_B;
		} else if (parString2 != null) {
			this.field_175200_y = parString2;
		} else {
			if (parInt1 >= 0) {
				this.field_175199_z = parInt1;
			}

			if (parInt2 >= 0) {
				this.field_175192_A = parInt2;
			}

			if (parInt3 >= 0) {
				this.field_175193_B = parInt3;
			}

			if (this.field_175195_w > 0) {
				this.field_175195_w = this.field_175199_z + this.field_175192_A + this.field_175193_B;
			}

		}
	}

	public void drawEaglerPlayerOverlay(int x, int y, float partialTicks) {
		Entity e = mc.getRenderViewEntity();
		if (e != null && e instanceof EntityLivingBase) {
			EntityLivingBase ent = (EntityLivingBase) e;
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			GlStateManager.enableDepth();
			GlStateManager.enableColorMaterial();
			GlStateManager.pushMatrix();
			GlStateManager.translate((float) x - 10, (float) y + 36, 50.0F);
			GlStateManager.scale(-17.0F, 17.0F, 17.0F);
			GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
			float f = ent.renderYawOffset;
			float f1 = ent.rotationYaw;
			float f2 = ent.prevRotationYaw;
			float f3 = ent.prevRotationYawHead;
			float f4 = ent.rotationYawHead;
			float f5 = ent.prevRenderYawOffset;
			GlStateManager.rotate(115.0F, 0.0F, 1.0F, 0.0F);
			RenderHelper.enableStandardItemLighting();
			float f6 = ent.prevRenderYawOffset + (ent.renderYawOffset - ent.prevRenderYawOffset) * partialTicks;
			ent.rotationYawHead -= f6;
			ent.prevRotationYawHead -= f6;
			ent.rotationYawHead *= 0.5f;
			ent.prevRotationYawHead *= 0.5f;
			ent.renderYawOffset = 0.0f;
			ent.prevRenderYawOffset = 0.0f;
			ent.prevRotationYaw = 0.0f;
			ent.rotationYaw = 0.0f;
			GlStateManager.rotate(-135.0F
					- (ent.prevRotationYawHead + (ent.rotationYawHead - ent.prevRotationYawHead) * partialTicks) * 0.5F,
					0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(ent.rotationPitch * 0.2f, 1.0F, 0.0F, 0.0F);
			RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
			rendermanager.setPlayerViewY(180.0F);
			rendermanager.setRenderShadow(false);
			rendermanager.renderEntityWithPosYaw(ent, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks);
			rendermanager.setRenderShadow(true);
			ent.renderYawOffset = f;
			ent.rotationYaw = f1;
			ent.prevRotationYaw = f2;
			ent.prevRotationYawHead = f3;
			ent.rotationYawHead = f4;
			ent.prevRenderYawOffset = f5;
			GlStateManager.popMatrix();
			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableDepth();
			GlStateManager.disableRescaleNormal();
			GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
			GlStateManager.disableTexture2D();
			GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		}
	}

	public void setRecordPlaying(IChatComponent parIChatComponent, boolean parFlag) {
		this.setRecordPlaying(parIChatComponent.getUnformattedText(), parFlag);
	}

	/**+
	 * returns a pointer to the persistant Chat GUI, containing all
	 * previous chat messages and such
	 */
	public GuiNewChat getChatGUI() {
		return this.persistantChatGUI;
	}

	public int getUpdateCounter() {
		return this.updateCounter;
	}

	public FontRenderer getFontRenderer() {
		return this.mc.fontRendererObj;
	}

	public GuiSpectator getSpectatorGui() {
		return this.spectatorGui;
	}

	public GuiPlayerTabOverlay getTabList() {
		return this.overlayPlayerList;
	}

	public void func_181029_i() {
		this.overlayPlayerList.func_181030_a();
	}

	private int hotbarAreaX = -1;
	private int hotbarAreaY = -1;
	private int hotbarAreaW = -1;
	private int hotbarAreaH = -1;
	private int currentHotbarSlotTouch = -1;
	private long hotbarSlotTouchStart = -1l;
	private boolean hotbarSlotTouchAlreadySelected = false;
	private int interactButtonX = -1;
	private int interactButtonY = -1;
	private int interactButtonW = -1;
	private int interactButtonH = -1;
	private int touchVPosX = -1;
	private int touchVPosY = -1;
	private int touchEventUID = -1;

	private void drawEaglerInteractButton(ScaledResolution parScaledResolution) {
		if (PointerInputAbstraction.isTouchMode() && mc.objectMouseOver != null
				&& mc.objectMouseOver.typeOfHit == MovingObjectType.ENTITY) {
			int scale = parScaledResolution.getScaleFactor();
			interactButtonW = 118 * scale;
			interactButtonH = 20 * scale;
			int xx = (parScaledResolution.getScaledWidth() - 118) / 2;
			int yy = parScaledResolution.getScaledHeight() - 70;
			interactButtonX = xx * scale;
			interactButtonY = yy * scale;
			mc.getTextureManager().bindTexture(TouchOverlayRenderer.spriteSheet);
			boolean hover = touchVPosX >= interactButtonX && touchVPosY >= interactButtonY
					&& touchVPosX < interactButtonX + interactButtonW && touchVPosY < interactButtonY + interactButtonH;
			float f = MathHelper.clamp_float(mc.gameSettings.touchControlOpacity, 0.0f, 1.0f);
			if (f > 0.0f) {
				if (f < 1.0f)
					GlStateManager.enableBlend();
				GlStateManager.color(1.0f, 1.0f, 1.0f, f);
				drawTexturedModalRect(xx, yy, 0, hover ? 216 : 236, 118, 20);
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				drawCenteredString(mc.fontRendererObj, I18n.format("touch.interact.entity"),
						parScaledResolution.getScaledWidth() / 2, yy + 6,
						(hover ? 16777120 : 14737632) | ((int) (f * 255.0f) << 24));
				if (f < 1.0f)
					GlStateManager.disableBlend();
			}
		} else {
			interactButtonX = -1;
			interactButtonY = -1;
			interactButtonW = -1;
			interactButtonH = -1;
		}
	}

	private int applyTouchHotbarTransformX(int posX, boolean scaled) {
		if (scaled) {
			return (posX + mc.scaledResolution.getScaledWidth() / 4) * 2 / 3;
		} else {
			return (posX + mc.displayWidth / 4) * 2 / 3;
		}
	}

	private int applyTouchHotbarTransformY(int posY, boolean scaled) {
		if (scaled) {
			return (posY + mc.scaledResolution.getScaledHeight() / 2) * 2 / 3;
		} else {
			return (posY + mc.displayHeight / 2) * 2 / 3;
		}
	}

	private void onBeginHotbarDraw() {
		if (PointerInputAbstraction.isTouchMode()) {
			GlStateManager.pushMatrix();
			ScaledResolution res = mc.scaledResolution;
			GlStateManager.translate(res.getScaledWidth() / -4, res.getScaledHeight() / -2, field_175199_z);
			GlStateManager.scale(1.5f, 1.5f, 1.5f);
		}
	}

	private void onEndHotbarDraw() {
		if (PointerInputAbstraction.isTouchMode()) {
			GlStateManager.popMatrix();
		}
	}

	private int getHotbarSlotTouched(int pointX) {
		int xx = pointX - hotbarAreaX - 2;
		xx /= 20 * mc.scaledResolution.getScaleFactor();
		if (xx < 0)
			xx = 0;
		if (xx > 9)
			xx = 9;
		return xx;
	}

	public boolean handleTouchBeginEagler(int uid, int pointX, int pointY) {
		if (mc.thePlayer == null) {
			return false;
		}
		if (touchEventUID == -1) {
			pointX = applyTouchHotbarTransformX(pointX, false);
			pointY = applyTouchHotbarTransformY(pointY, false);
			if (pointX >= hotbarAreaX && pointY >= hotbarAreaY && pointX < hotbarAreaX + hotbarAreaW
					&& pointY < hotbarAreaY + hotbarAreaH) {
				touchEventUID = uid;
				currentHotbarSlotTouch = getHotbarSlotTouched(pointX);
				hotbarSlotTouchStart = EagRuntime.steadyTimeMillis();
				if (currentHotbarSlotTouch >= 0 && currentHotbarSlotTouch < 9) {
					if (mc.thePlayer.isSpectator()) {
						hotbarSlotTouchAlreadySelected = false;
						mc.ingameGUI.getSpectatorGui().func_175260_a(currentHotbarSlotTouch);
					} else {
						hotbarSlotTouchAlreadySelected = (mc.thePlayer.inventory.currentItem == currentHotbarSlotTouch);
						mc.thePlayer.inventory.currentItem = currentHotbarSlotTouch;
					}
				} else if (currentHotbarSlotTouch == 9) {
					hotbarSlotTouchAlreadySelected = false;
					currentHotbarSlotTouch = 69;
					if (mc.playerController.isRidingHorse()) {
						mc.thePlayer.sendHorseInventory();
					} else {
						mc.getNetHandler().addToSendQueue(
								new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
						mc.displayGuiScreen(new GuiInventory(mc.thePlayer));
					}
				}
				return true;
			}
			if (pointX >= interactButtonX && pointY >= interactButtonY && pointX < interactButtonX + interactButtonW
					&& pointY < interactButtonY + interactButtonH) {
				touchEventUID = uid;
				mc.rightClickMouse();
				return true;
			}
		}
		return false;
	}

	public boolean handleTouchEndEagler(int uid, int pointX, int pointY) {
		if (uid == touchEventUID) {
			if (hotbarSlotTouchStart != -1l && currentHotbarSlotTouch != 69) {
				if (EagRuntime.steadyTimeMillis() - hotbarSlotTouchStart < 350l) {
					if (hotbarSlotTouchAlreadySelected) {
						if (mc.thePlayer != null) {
							mc.thePlayer.dropOneItem(false);
						}
					}
				}
			}
			touchVPosX = -1;
			touchVPosY = -1;
			touchEventUID = -1;
			currentHotbarSlotTouch = -1;
			hotbarSlotTouchStart = -1l;
			hotbarSlotTouchAlreadySelected = false;
			return true;
		}
		return false;
	}

	public void updateTouchEagler(boolean screenTouched) {
		if (screenTouched) {
			int pointCount = Touch.touchPointCount();
			for (int i = 0; i < pointCount; ++i) {
				int uid = Touch.touchPointUID(i);
				if (TouchControls.touchControls.containsKey(uid)) {
					continue;
				}
				if (touchEventUID == -1 || touchEventUID == uid) {
					touchVPosX = applyTouchHotbarTransformX(Touch.touchPointX(i), false);
					touchVPosY = applyTouchHotbarTransformY(mc.displayHeight - Touch.touchPointY(i) - 1, false);
					long millis = EagRuntime.steadyTimeMillis();
					if (touchEventUID != -1 && hotbarSlotTouchStart != -1l) {
						if (currentHotbarSlotTouch != 69) {
							int slot = getHotbarSlotTouched(touchVPosX);
							if (slot != currentHotbarSlotTouch) {
								hotbarSlotTouchAlreadySelected = false;
								currentHotbarSlotTouch = slot;
								hotbarSlotTouchStart = millis;
								if (slot >= 0 && slot < 9) {
									if (mc.thePlayer.isSpectator()) {
										mc.ingameGUI.getSpectatorGui().func_175260_a(slot);
									} else {
										mc.thePlayer.inventory.currentItem = slot;
									}
								}
							} else {
								if (millis - hotbarSlotTouchStart > 1200l) {
									if (!mc.thePlayer.isSpectator()) {
										hotbarSlotTouchStart = millis;
										this.mc.thePlayer.dropOneItem(true);
									}
								}
							}
						}
					}
					return;
				}
			}
		}
		if (touchEventUID != -1) {
			handleTouchEndEagler(touchEventUID, touchVPosX, touchVPosY);
		}
		touchVPosX = -1;
		touchVPosY = -1;
		touchEventUID = -1;
		currentHotbarSlotTouch = -1;
		hotbarSlotTouchStart = -1l;
		hotbarSlotTouchAlreadySelected = false;
	}

	public boolean isTouchOverlapEagler(int uid, int tx, int ty) {
		if (touchEventUID == uid) {
			return true;
		}
		ty = mc.displayHeight - ty - 1;
		tx = applyTouchHotbarTransformX(tx, false);
		ty = applyTouchHotbarTransformY(ty, false);
		return (tx >= hotbarAreaX && ty >= hotbarAreaY && tx < hotbarAreaX + hotbarAreaW
				&& ty < hotbarAreaY + hotbarAreaH)
				|| (tx >= interactButtonX && ty >= interactButtonY && tx < interactButtonX + interactButtonW
						&& ty < interactButtonY + interactButtonH);
	}

}