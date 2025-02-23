package net.minecraft.client.gui;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EagUtils;
import net.lax1dude.eaglercraft.v1_8.EaglerInputStream;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.lax1dude.eaglercraft.v1_8.EaglercraftVersion;
import net.lax1dude.eaglercraft.v1_8.Mouse;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.crypto.SHA1Digest;
import net.lax1dude.eaglercraft.v1_8.internal.EnumCursorType;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.minecraft.MainMenuSkyboxTexture;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.lax1dude.eaglercraft.v1_8.profile.GuiScreenEditProfile;
import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;
import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenDemoPlayWorldSelection;
import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenIntegratedServerBusy;
import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenIntegratedServerStartup;
import net.lax1dude.eaglercraft.v1_8.update.GuiUpdateCheckerOverlay;
import net.lax1dude.eaglercraft.v1_8.update.GuiUpdateVersionSlot;
import net.lax1dude.eaglercraft.v1_8.update.UpdateCertificate;
import net.lax1dude.eaglercraft.v1_8.update.UpdateService;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.ISaveFormat;

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
public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback {
	private static final Logger logger = LogManager.getLogger();
	private static final EaglercraftRandom RANDOM = new EaglercraftRandom();
	private float updateCounter;
	private boolean isDefault;
	private static final int lendef = 5987;
	private static final byte[] sha1def = new byte[] { -107, 77, 108, 49, 11, -100, -8, -119, -1, -100, -85, -55, 18,
			-69, -107, 113, -93, -101, -79, 32 };
	private String splashText;
	private GuiButton buttonResetDemo;
	private int panoramaTimer;
	/**+
	 * Texture allocated for the current viewport of the main menu's
	 * panorama background.
	 */
	private static MainMenuSkyboxTexture viewportTexture = null;
	private static MainMenuSkyboxTexture viewportTexture2 = null;
	private boolean field_175375_v = true;
	private String openGLWarning1;
	private String openGLWarning2;
	private static final ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");
	private static final ResourceLocation minecraftTitleTextures = new ResourceLocation(
			"textures/gui/title/minecraft.png");
	private static final ResourceLocation minecraftTitleBlurFlag = new ResourceLocation(
			"textures/gui/title/background/enable_blur.txt");
	private static final ResourceLocation eaglerGuiTextures = new ResourceLocation("eagler:gui/eagler_gui.png");
	/**+
	 * An array of all the paths to the panorama pictures.
	 */
	private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[] {
			new ResourceLocation("textures/gui/title/background/panorama_0.png"),
			new ResourceLocation("textures/gui/title/background/panorama_1.png"),
			new ResourceLocation("textures/gui/title/background/panorama_2.png"),
			new ResourceLocation("textures/gui/title/background/panorama_3.png"),
			new ResourceLocation("textures/gui/title/background/panorama_4.png"),
			new ResourceLocation("textures/gui/title/background/panorama_5.png") };
	private int field_92024_r;
	private int field_92023_s;
	private int field_92022_t;
	private int field_92021_u;
	private int field_92020_v;
	private int field_92019_w;
	private static ResourceLocation backgroundTexture = null;
	private static ResourceLocation backgroundTexture2 = null;
	private GuiUpdateCheckerOverlay updateCheckerOverlay;
	private GuiButton downloadOfflineButton;
	private boolean enableBlur = true;
	private boolean shouldReload = false;

	private static GuiMainMenu instance = null;

	public GuiMainMenu() {
		instance = this;
		this.splashText = "missingno";
		updateCheckerOverlay = new GuiUpdateCheckerOverlay(false, this);
		BufferedReader bufferedreader = null;

		try {
			ArrayList arraylist = Lists.newArrayList();
			bufferedreader = new BufferedReader(new InputStreamReader(
					Minecraft.getMinecraft().getResourceManager().getResource(splashTexts).getInputStream(),
					Charsets.UTF_8));

			String s;
			while ((s = bufferedreader.readLine()) != null) {
				s = s.trim();
				if (!s.isEmpty()) {
					arraylist.add(s);
				}
			}

			if (!arraylist.isEmpty()) {
				while (true) {
					this.splashText = (String) arraylist.get(RANDOM.nextInt(arraylist.size()));
					if (this.splashText.hashCode() != 125780783) {
						break;
					}
				}
			}
		} catch (IOException var12) {
			;
		} finally {
			if (bufferedreader != null) {
				try {
					bufferedreader.close();
				} catch (IOException var11) {
					;
				}
			}

		}

		this.updateCounter = RANDOM.nextFloat();

		reloadResourceFlags();
	}

	private void reloadResourceFlags() {
		if (Minecraft.getMinecraft().isDemo()) {
			this.isDefault = false;
		} else {
			if (!EagRuntime.getConfiguration().isEnableMinceraft()) {
				this.isDefault = false;
			} else {
				try {
					byte[] bytes = EaglerInputStream.inputStreamToBytesQuiet(Minecraft.getMinecraft()
							.getResourceManager().getResource(minecraftTitleTextures).getInputStream());
					if (bytes != null && bytes.length == lendef) {
						SHA1Digest sha1 = new SHA1Digest();
						byte[] sha1out = new byte[20];
						sha1.update(bytes, 0, bytes.length);
						sha1.doFinal(sha1out, 0);
						this.isDefault = Arrays.equals(sha1out, sha1def);
					} else {
						this.isDefault = false;
					}
				} catch (IOException e) {
					this.isDefault = false;
				}
			}
		}

		this.enableBlur = true;

		try {
			byte[] bytes = EaglerInputStream.inputStreamToBytesQuiet(
					Minecraft.getMinecraft().getResourceManager().getResource(minecraftTitleBlurFlag).getInputStream());
			if (bytes != null) {
				String[] blurCfg = EagUtils.linesArray(new String(bytes, StandardCharsets.UTF_8));
				for (int i = 0; i < blurCfg.length; ++i) {
					String s = blurCfg[i];
					if (s.startsWith("enable_blur=")) {
						s = s.substring(12).trim();
						this.enableBlur = s.equals("1") || s.equals("true");
						break;
					}
				}
			}
		} catch (IOException e) {
			;
		}
	}

	public static void doResourceReloadHack() {
		if (instance != null) {
			instance.shouldReload = true;
		}
	}

	/**+
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		++this.panoramaTimer;
		if (downloadOfflineButton != null) {
			downloadOfflineButton.enabled = !UpdateService.shouldDisableDownloadButton();
		}
		if (shouldReload) {
			reloadResourceFlags();
			shouldReload = false;
		}
	}

	/**+
	 * Returns true if this GUI should pause the game when it is
	 * displayed in single-player
	 */
	public boolean doesGuiPauseGame() {
		return false;
	}

	/**+
	 * Fired when a key is typed (except F11 which toggles full
	 * screen). This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e). Args : character (character
	 * on the key), keyCode (lwjgl Keyboard key code)
	 */
	protected void keyTyped(char parChar1, int parInt1) {
	}

	/**+
	 * Adds the buttons (and other controls) to the screen in
	 * question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui() {
		if (viewportTexture == null) {
			viewportTexture = new MainMenuSkyboxTexture(256, 256);
			backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", viewportTexture);
			viewportTexture2 = new MainMenuSkyboxTexture(256, 256);
			backgroundTexture2 = this.mc.getTextureManager().getDynamicTextureLocation("background", viewportTexture2);
		}
		this.updateCheckerOverlay.setResolution(mc, width, height);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		if (calendar.get(2) + 1 == 12 && calendar.get(5) == 24) {
			this.splashText = "Merry X-mas!";
		} else if (calendar.get(2) + 1 == 1 && calendar.get(5) == 1) {
			this.splashText = "Happy new year!";
		} else if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31) {
			this.splashText = "OOoooOOOoooo! Spooky!";
		}

		int i = this.height / 4 + 48;

		boolean isFork = !EaglercraftVersion.projectOriginAuthor.equalsIgnoreCase(EaglercraftVersion.projectForkVendor);

		if (isFork && EaglercraftVersion.mainMenuStringF != null && EaglercraftVersion.mainMenuStringF.length() > 0) {
			i += 11;
		}

		if (this.mc.isDemo()) {
			this.addDemoButtons(i, 24);
		} else {
			this.addSingleplayerMultiplayerButtons(i, 24);
		}

		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, i + 72 + 12, 98, 20,
				I18n.format("menu.options", new Object[0])));
		this.buttonList.add(new GuiButton(4, this.width / 2 + 2, i + 72 + 12, 98, 20,
				I18n.format("menu.editProfile", new Object[0])));

		this.buttonList.add(new GuiButtonLanguage(5, this.width / 2 - 124, i + 72 + 12));

		if (isFork) {
			this.openGLWarning1 = EaglercraftVersion.mainMenuStringE;
			this.openGLWarning2 = EaglercraftVersion.mainMenuStringF;
			boolean line2 = this.openGLWarning2 != null && this.openGLWarning2.length() > 0;
			this.field_92023_s = this.fontRendererObj.getStringWidth(this.openGLWarning1);
			this.field_92024_r = this.fontRendererObj.getStringWidth(this.openGLWarning2);
			int j = Math.max(this.field_92023_s, this.field_92024_r);
			this.field_92022_t = (this.width - j) / 2;
			this.field_92021_u = ((GuiButton) this.buttonList.get(0)).yPosition - (line2 ? 32 : 21);
			this.field_92020_v = this.field_92022_t + j;
			this.field_92019_w = this.field_92021_u + (line2 ? 24 : 11);
		}

		this.mc.func_181537_a(false);
	}

	/**+
	 * Adds Singleplayer and Multiplayer buttons on Main Menu for
	 * players who have bought the game.
	 */
	private void addSingleplayerMultiplayerButtons(int parInt1, int parInt2) {
		this.buttonList
				.add(new GuiButton(1, this.width / 2 - 100, parInt1, I18n.format("menu.singleplayer", new Object[0])));
		this.buttonList.add(new GuiButton(2, this.width / 2 - 100, parInt1 + parInt2 * 1,
				I18n.format("menu.multiplayer", new Object[0])));
		if (EaglercraftVersion.mainMenuEnableGithubButton) {
			this.buttonList.add(
					new GuiButton(14, this.width / 2 - 100, parInt1 + parInt2 * 2, I18n.format("menu.forkOnGitlab")));
		} else {
			if (EagRuntime.getConfiguration().isEnableDownloadOfflineButton()
					&& (EagRuntime.getConfiguration().getDownloadOfflineButtonLink() != null
							|| (!EagRuntime.isOfflineDownloadURL() && UpdateService.supported()
									&& UpdateService.getClientSignatureData() != null))) {
				this.buttonList.add(downloadOfflineButton = new GuiButton(15, this.width / 2 - 100,
						parInt1 + parInt2 * 2, I18n.format("update.downloadOffline")));
				downloadOfflineButton.enabled = !UpdateService.shouldDisableDownloadButton();
			}
		}
	}

	/**+
	 * Adds Demo buttons on Main Menu for players who are playing
	 * Demo.
	 */
	private void addDemoButtons(int parInt1, int parInt2) {
		this.buttonList
				.add(new GuiButton(11, this.width / 2 - 100, parInt1, I18n.format("menu.playdemo", new Object[0])));
		this.buttonList.add(this.buttonResetDemo = new GuiButton(12, this.width / 2 - 100, parInt1 + parInt2 * 1,
				I18n.format("menu.resetdemo", new Object[0])));
		this.buttonResetDemo.enabled = this.mc.gameSettings.hasCreatedDemoWorld;
	}

	/**+
	 * Called by the controls from the buttonList when activated.
	 * (Mouse pressed for buttons)
	 */
	protected void actionPerformed(GuiButton parGuiButton) {
		if (parGuiButton.id == 0) {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
		}

		if (parGuiButton.id == 5) {
			this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
		}

		if (parGuiButton.id == 1) {
			this.mc.displayGuiScreen(new GuiScreenIntegratedServerStartup(this));
		}

		if (parGuiButton.id == 2) {
			this.mc.displayGuiScreen(new GuiMultiplayer(this));
		}

		if (parGuiButton.id == 4) {
			this.mc.displayGuiScreen(new GuiScreenEditProfile(this));
		}

		if (parGuiButton.id == 14) {
			EagRuntime.openLink(EaglercraftVersion.projectForkURL);
		}

		if (parGuiButton.id == 11) {
			this.mc.displayGuiScreen(new GuiScreenDemoPlayWorldSelection(this));
		}

		if (parGuiButton.id == 12) {
			GuiYesNo guiyesno = GuiSelectWorld.func_152129_a(this, "Demo World", 12);
			this.mc.displayGuiScreen(guiyesno);
		}

		if (parGuiButton.id == 15) {
			if (EagRuntime.getConfiguration().isEnableDownloadOfflineButton()) {
				String link = EagRuntime.getConfiguration().getDownloadOfflineButtonLink();
				if (link != null) {
					EagRuntime.openLink(link);
				} else {
					UpdateService.quine();
				}
			}
		}
	}

	public void confirmClicked(boolean flag, int i) {
		if (flag && i == 12) {
			this.mc.gameSettings.hasCreatedDemoWorld = false;
			this.mc.gameSettings.saveOptions();
			ISaveFormat isaveformat = this.mc.getSaveLoader();
			isaveformat.deleteWorldDirectory("Demo World");
			this.mc.displayGuiScreen(new GuiScreenIntegratedServerBusy(this, "singleplayer.busy.deleting",
					"singleplayer.failed.deleting", SingleplayerServerController::isReady));
		} else {
			this.mc.displayGuiScreen(this);
		}
	}

	/**+
	 * Draws the main menu panorama
	 */
	private void drawPanorama(int parInt1, int parInt2, float parFloat1) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.matrixMode(GL_PROJECTION);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		if (enableBlur) {
			GlStateManager.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
		} else {
			GlStateManager.gluPerspective(85.0F, (float) width / (float) height, 0.05F, 10.0F);
		}
		GlStateManager.matrixMode(GL_MODELVIEW);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
		if (enableBlur) {
			GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
		}
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.disableCull();
		GlStateManager.depthMask(false);
		GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		byte b0 = enableBlur ? (byte) 4 : (byte) 1;

		for (int i = 0; i < b0 * b0; ++i) {
			GlStateManager.pushMatrix();
			float f = ((float) (i % b0) / (float) b0 - 0.5F) / 64.0F;
			float f1 = ((float) (i / b0) / (float) b0 - 0.5F) / 64.0F;
			float f2 = 0.0F;
			GlStateManager.translate(f, f1, f2);
			GlStateManager.rotate(MathHelper.sin(((float) this.panoramaTimer + parFloat1) / 400.0F) * 25.0F + 20.0F,
					1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(-((float) this.panoramaTimer + parFloat1) * 0.1F, 0.0F, 1.0F, 0.0F);

			for (int j = 0; j < 6; ++j) {
				GlStateManager.pushMatrix();
				if (j == 1) {
					GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
				}

				if (j == 2) {
					GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				}

				if (j == 3) {
					GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
				}

				if (j == 4) {
					GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
				}

				if (j == 5) {
					GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
				}

				this.mc.getTextureManager().bindTexture(titlePanoramaPaths[j]);
				worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				int k = 255 / (i + 1);
				float f3 = 0.0F;
				worldrenderer.pos(-1.0D, -1.0D, 1.0D).tex(0.0D, 0.0D).color(255, 255, 255, k).endVertex();
				worldrenderer.pos(1.0D, -1.0D, 1.0D).tex(1.0D, 0.0D).color(255, 255, 255, k).endVertex();
				worldrenderer.pos(1.0D, 1.0D, 1.0D).tex(1.0D, 1.0D).color(255, 255, 255, k).endVertex();
				worldrenderer.pos(-1.0D, 1.0D, 1.0D).tex(0.0D, 1.0D).color(255, 255, 255, k).endVertex();
				tessellator.draw();
				GlStateManager.popMatrix();
			}

			GlStateManager.popMatrix();
			GlStateManager.colorMask(true, true, true, false);
		}

		worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
		GlStateManager.colorMask(true, true, true, true);
		GlStateManager.matrixMode(GL_PROJECTION);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(GL_MODELVIEW);
		GlStateManager.popMatrix();
		GlStateManager.depthMask(true);
		GlStateManager.enableCull();
		GlStateManager.enableDepth();
	}

	/**+
	 * Rotate and blurs the skybox view in the main menu
	 */
	private void rotateAndBlurSkybox(float parFloat1) {
		EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		// EaglercraftGPU.glCopyTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		GlStateManager.colorMask(true, true, true, false);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		GlStateManager.disableAlpha();
		byte b0 = 3;

		for (int i = 0; i < b0; ++i) {
			float f = 1.0F / (float) (i + 1);
			int j = this.width;
			int k = this.height;
			float f1 = (float) (i - b0 / 2) / 256.0F;
			worldrenderer.pos((double) j, (double) k, (double) this.zLevel).tex((double) (0.0F + f1), 1.0D)
					.color(1.0F, 1.0F, 1.0F, f).endVertex();
			worldrenderer.pos((double) j, 0.0D, (double) this.zLevel).tex((double) (1.0F + f1), 1.0D)
					.color(1.0F, 1.0F, 1.0F, f).endVertex();
			worldrenderer.pos(0.0D, 0.0D, (double) this.zLevel).tex((double) (1.0F + f1), 0.0D)
					.color(1.0F, 1.0F, 1.0F, f).endVertex();
			worldrenderer.pos(0.0D, (double) k, (double) this.zLevel).tex((double) (0.0F + f1), 0.0D)
					.color(1.0F, 1.0F, 1.0F, f).endVertex();
		}

		tessellator.draw();
		GlStateManager.enableAlpha();
		GlStateManager.colorMask(true, true, true, true);
	}

	/**+
	 * Renders the skybox in the main menu
	 */
	private void renderSkybox(int parInt1, int parInt2, float parFloat1) {
		viewportTexture.bindFramebuffer();
		GlStateManager.viewport(0, 0, 256, 256);
		GlStateManager.clearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.clear(GL_COLOR_BUFFER_BIT);
		this.drawPanorama(parInt1, parInt2, parFloat1);
		viewportTexture2.bindFramebuffer();
		GlStateManager.clearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.clear(GL_COLOR_BUFFER_BIT);
		this.mc.getTextureManager().bindTexture(backgroundTexture);
		this.rotateAndBlurSkybox(parFloat1);
		viewportTexture.bindFramebuffer();
		this.mc.getTextureManager().bindTexture(backgroundTexture2);
		this.rotateAndBlurSkybox(parFloat1);
		viewportTexture2.bindFramebuffer();
		this.mc.getTextureManager().bindTexture(backgroundTexture);
		this.rotateAndBlurSkybox(parFloat1);
		viewportTexture.bindFramebuffer();
		this.mc.getTextureManager().bindTexture(backgroundTexture2);
		this.rotateAndBlurSkybox(parFloat1);
		viewportTexture2.bindFramebuffer();
		this.mc.getTextureManager().bindTexture(backgroundTexture);
		this.rotateAndBlurSkybox(parFloat1);
		viewportTexture.bindFramebuffer();
		this.mc.getTextureManager().bindTexture(backgroundTexture2);
		this.rotateAndBlurSkybox(parFloat1);

		// Notch fucked up, the last iteration is not necessary, in the vanilla renderer
		// it is unintentionally discarded and the previous iteration is used

		// viewportTexture2.bindFramebuffer();
		// this.mc.getTextureManager().bindTexture(backgroundTexture);
		// this.rotateAndBlurSkybox(parFloat1);

		_wglBindFramebuffer(0x8D40, null);

		this.mc.getTextureManager().bindTexture(backgroundTexture);
		GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
		float f = this.width > this.height ? 120.0F / (float) this.width : 120.0F / (float) this.height;
		float f1 = (float) this.height * f / 256.0F;
		float f2 = (float) this.width * f / 256.0F;
		int i = this.width;
		int j = this.height;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		worldrenderer.pos(0.0D, (double) j, (double) this.zLevel).tex((double) (0.5F - f1), (double) (0.5F + f2))
				.color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		worldrenderer.pos((double) i, (double) j, (double) this.zLevel).tex((double) (0.5F - f1), (double) (0.5F - f2))
				.color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		worldrenderer.pos((double) i, 0.0D, (double) this.zLevel).tex((double) (0.5F + f1), (double) (0.5F - f2))
				.color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		worldrenderer.pos(0.0D, 0.0D, (double) this.zLevel).tex((double) (0.5F + f1), (double) (0.5F + f2))
				.color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		tessellator.draw();
	}

	/**+
	 * Draws the screen and all the components in it. Args : mouseX,
	 * mouseY, renderPartialTicks
	 */
	public void drawScreen(int i, int j, float f) {
		GlStateManager.disableAlpha();
		if (enableBlur) {
			this.renderSkybox(i, j, f);
		} else {
			this.drawPanorama(i, j, f);
		}
		GlStateManager.enableAlpha();
		short short1 = 274;
		int k = this.width / 2 - short1 / 2;
		byte b0 = 30;
		if (enableBlur) {
			this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
			this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
		}
		this.mc.getTextureManager().bindTexture(minecraftTitleTextures);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		boolean minc = (double) this.updateCounter < 1.0E-4D;
		if (this.isDefault) {
			minc = !minc;
		}
		if (minc) {
			this.drawTexturedModalRect(k + 0, b0 + 0, 0, 0, 99, 44);
			this.drawTexturedModalRect(k + 99, b0 + 0, 129, 0, 27, 44);
			this.drawTexturedModalRect(k + 99 + 26, b0 + 0, 126, 0, 3, 44);
			this.drawTexturedModalRect(k + 99 + 26 + 3, b0 + 0, 99, 0, 26, 44);
			this.drawTexturedModalRect(k + 154, b0 + 0, 0, 45, 155, 44);
		} else {
			this.drawTexturedModalRect(k + 0, b0 + 0, 0, 0, 155, 44);
			this.drawTexturedModalRect(k + 155, b0 + 0, 0, 45, 155, 44);
		}

		boolean isForkLabel = ((this.openGLWarning1 != null && this.openGLWarning1.length() > 0)
				|| (this.openGLWarning2 != null && this.openGLWarning2.length() > 0));

		if (isForkLabel) {
			drawRect(this.field_92022_t - 3, this.field_92021_u - 3, this.field_92020_v + 3, this.field_92019_w,
					1428160512);
			if (this.openGLWarning1 != null)
				this.drawString(this.fontRendererObj, this.openGLWarning1, this.field_92022_t, this.field_92021_u, -1);
			if (this.openGLWarning2 != null)
				this.drawString(this.fontRendererObj, this.openGLWarning2, (this.width - this.field_92024_r) / 2,
						this.field_92021_u + 12, -1);
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate((float) (this.width / 2 + 90), 70.0F, 0.0F);
		GlStateManager.rotate(isForkLabel ? -12.0F : -20.0F, 0.0F, 0.0F, 1.0F);
		float f1 = 1.8F - MathHelper
				.abs(MathHelper.sin((float) (Minecraft.getSystemTime() % 1000L) / 1000.0F * 3.1415927F * 2.0F) * 0.1F);
		f1 = f1 * 100.0F / (float) (this.fontRendererObj.getStringWidth(this.splashText) + 32);
		if (isForkLabel) {
			f1 *= 0.8f;
		}
		GlStateManager.scale(f1, f1, f1);
		this.drawCenteredString(this.fontRendererObj, this.splashText, 0, -8, -256);
		GlStateManager.popMatrix();

		String s = EaglercraftVersion.mainMenuStringA;
		if (this.mc.isDemo()) {
			s += " Demo";
		}
		this.drawString(this.fontRendererObj, s, 2, this.height - 20, -1);
		s = EaglercraftVersion.mainMenuStringB;
		this.drawString(this.fontRendererObj, s, 2, this.height - 10, -1);

		String s1 = EaglercraftVersion.mainMenuStringC;
		this.drawString(this.fontRendererObj, s1, this.width - this.fontRendererObj.getStringWidth(s1) - 2,
				this.height - 20, -1);
		s1 = EaglercraftVersion.mainMenuStringD;
		if (this.mc.isDemo()) {
			s1 = "Copyright Mojang AB. Do not distribute!";
		}
		this.drawString(this.fontRendererObj, s1, this.width - this.fontRendererObj.getStringWidth(s1) - 2,
				this.height - 10, -1);

		if (!this.mc.isDemo()) {
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.75f, 0.75f, 0.75f);
			int www = 0;
			int hhh = 0;
			s1 = EaglercraftVersion.mainMenuStringG;
			if (s1 != null) {
				www = this.fontRendererObj.getStringWidth(s1);
				hhh += 10;
			}
			s1 = EaglercraftVersion.mainMenuStringH;
			if (s1 != null) {
				www = Math.max(www, this.fontRendererObj.getStringWidth(s1));
				hhh += 10;
			}
			if (www > 0) {
				drawRect(0, 0, www + 6, hhh + 4, 0x55200000);
				s1 = EaglercraftVersion.mainMenuStringG;
				if (s1 != null) {
					www = this.fontRendererObj.getStringWidth(s1);
					this.drawString(this.fontRendererObj, s1, 3, 3, 0xFFFFFF99);
				}
				s1 = EaglercraftVersion.mainMenuStringH;
				if (s1 != null) {
					www = Math.max(www, this.fontRendererObj.getStringWidth(s1));
					this.drawString(this.fontRendererObj, s1, 3, 13, 0xFFFFFF99);
				}
			}
			if (EagRuntime.getConfiguration().isEnableSignatureBadge()) {
				UpdateCertificate cert = UpdateService.getClientCertificate();
				GlStateManager.scale(0.66667f, 0.66667f, 0.66667f);
				if (cert != null) {
					s1 = I18n.format("update.digitallySigned",
							GuiUpdateVersionSlot.dateFmt.format(new Date(cert.sigTimestamp)));
				} else {
					s1 = I18n.format("update.signatureInvalid");
				}
				www = this.fontRendererObj.getStringWidth(s1) + 14;
				drawRect((this.width * 2 - www) / 2, 0, (this.width * 2 - www) / 2 + www, 12, 0x33000000);
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				this.drawString(this.fontRendererObj, s1, (this.width * 2 - www) / 2 + 12, 2,
						cert != null ? 0xFFFFFF99 : 0xFFFF5555);
				GlStateManager.scale(0.6f, 0.6f, 0.6f);
				mc.getTextureManager().bindTexture(eaglerGuiTextures);
				drawTexturedModalRect((int) ((this.width * 2 - www) / 2 / 0.6f) + 2, 1, cert != null ? 32 : 16, 0, 16,
						16);
			}
			GlStateManager.popMatrix();
		}

		String lbl = "CREDITS.txt";
		int w = fontRendererObj.getStringWidth(lbl) * 3 / 4;

		if (i >= (this.width - w - 4) && i <= this.width && j >= 0 && j <= 9) {
			Mouse.showCursor(EnumCursorType.HAND);
			drawRect((this.width - w - 4), 0, this.width, 10, 0x55000099);
		} else {
			drawRect((this.width - w - 4), 0, this.width, 10, 0x55200000);
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate((this.width - w - 2), 2.0f, 0.0f);
		GlStateManager.scale(0.75f, 0.75f, 0.75f);
		drawString(fontRendererObj, lbl, 0, 0, 16777215);
		GlStateManager.popMatrix();

		this.updateCheckerOverlay.drawScreen(i, j, f);
		super.drawScreen(i, j, f);
	}

	/**+
	 * Called when the mouse is clicked. Args : mouseX, mouseY,
	 * clickedButton
	 */
	protected void mouseClicked(int par1, int par2, int par3) {
		if (par3 == 0) {
			String lbl = "CREDITS.txt";
			int w = fontRendererObj.getStringWidth(lbl) * 3 / 4;
			if (par1 >= (this.width - w - 4) && par1 <= this.width && par2 >= 0 && par2 <= 10) {
				String resStr = EagRuntime.getResourceString("/assets/eagler/CREDITS.txt");
				if (resStr != null) {
					EagRuntime.openCreditsPopup(resStr);
				}
				mc.getSoundHandler()
						.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
				return;
			}
		}
		this.updateCheckerOverlay.mouseClicked(par1, par2, par3);
		super.mouseClicked(par1, par2, par3);
	}
}