package net.minecraft.client.gui;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.lax1dude.eaglercraft.v1_8.profile.EaglerProfile;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.util.EnumChatFormatting;
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
public class GuiWinGame extends GuiScreen {
	private static final Logger logger = LogManager.getLogger();
	private static final ResourceLocation MINECRAFT_LOGO = new ResourceLocation("textures/gui/title/minecraft.png");
	private static final ResourceLocation VIGNETTE_TEXTURE = new ResourceLocation("textures/misc/vignette.png");
	private int field_146581_h;
	private List<String> field_146582_i;
	private int field_146579_r;
	private float field_146578_s = 0.5F;

	/**+
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		MusicTicker musicticker = this.mc.func_181535_r();
		SoundHandler soundhandler = this.mc.getSoundHandler();
		if (this.field_146581_h == 0) {
			musicticker.func_181557_a();
			musicticker.func_181558_a(MusicTicker.MusicType.CREDITS);
			soundhandler.resumeSounds();
		}

		soundhandler.update();
		++this.field_146581_h;
		float f = (float) (this.field_146579_r + this.height + this.height + 24) / this.field_146578_s;
		if ((float) this.field_146581_h > f) {
			this.sendRespawnPacket();
		}

	}

	/**+
	 * Fired when a key is typed (except F11 which toggles full
	 * screen). This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e). Args : character (character
	 * on the key), keyCode (lwjgl Keyboard key code)
	 */
	protected void keyTyped(char parChar1, int parInt1) {
		if (parInt1 == 1) {
			this.sendRespawnPacket();
		}

	}

	private void sendRespawnPacket() {
		this.mc.thePlayer.sendQueue
				.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
		this.mc.displayGuiScreen((GuiScreen) null);
	}

	/**+
	 * Returns true if this GUI should pause the game when it is
	 * displayed in single-player
	 */
	public boolean doesGuiPauseGame() {
		return true;
	}

	/**+
	 * Adds the buttons (and other controls) to the screen in
	 * question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui() {
		if (this.field_146582_i == null) {
			this.field_146582_i = Lists.newArrayList();

			try {
				String s = "";
				String s1 = "" + EnumChatFormatting.WHITE + EnumChatFormatting.OBFUSCATED + EnumChatFormatting.GREEN
						+ EnumChatFormatting.AQUA;
				short short1 = 274;
				InputStream inputstream = this.mc.getResourceManager()
						.getResource(new ResourceLocation("texts/end.txt")).getInputStream();
				BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream, Charsets.UTF_8));
				EaglercraftRandom random = new EaglercraftRandom(8124371L);

				while ((s = bufferedreader.readLine()) != null) {
					String s2;
					String s3;
					for (s = s.replaceAll("PLAYERNAME", EaglerProfile.getName()); s
							.contains(s1); s = s2 + EnumChatFormatting.WHITE + EnumChatFormatting.OBFUSCATED
									+ "XXXXXXXX".substring(0, random.nextInt(4) + 3) + s3) {
						int i = s.indexOf(s1);
						s2 = s.substring(0, i);
						s3 = s.substring(i + s1.length());
					}

					this.field_146582_i.addAll(this.mc.fontRendererObj.listFormattedStringToWidth(s, short1));
					this.field_146582_i.add("");
				}

				inputstream.close();

				for (int j = 0; j < 8; ++j) {
					this.field_146582_i.add("");
				}

				inputstream = this.mc.getResourceManager().getResource(new ResourceLocation("texts/credits.txt"))
						.getInputStream();
				bufferedreader = new BufferedReader(new InputStreamReader(inputstream, Charsets.UTF_8));

				while ((s = bufferedreader.readLine()) != null) {
					s = s.replaceAll("PLAYERNAME", EaglerProfile.getName());
					s = s.replaceAll("\t", "    ");
					this.field_146582_i.addAll(this.mc.fontRendererObj.listFormattedStringToWidth(s, short1));
					this.field_146582_i.add("");
				}

				inputstream.close();
				this.field_146579_r = this.field_146582_i.size() * 12;
			} catch (Exception exception) {
				logger.error("Couldn\'t load credits", exception);
			}

		}
	}

	private void drawWinGameScreen(int parInt1, int parInt2, float parFloat1) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		int i = this.width;
		float f = 0.0F - ((float) this.field_146581_h + parFloat1) * 0.5F * this.field_146578_s;
		float f1 = (float) this.height - ((float) this.field_146581_h + parFloat1) * 0.5F * this.field_146578_s;
		float f2 = 0.015625F;
		float f3 = ((float) this.field_146581_h + parFloat1 - 0.0F) * 0.02F;
		float f4 = (float) (this.field_146579_r + this.height + this.height + 24) / this.field_146578_s;
		float f5 = (f4 - 20.0F - ((float) this.field_146581_h + parFloat1)) * 0.005F;
		if (f5 < f3) {
			f3 = f5;
		}

		if (f3 > 1.0F) {
			f3 = 1.0F;
		}

		f3 = f3 * f3;
		f3 = f3 * 96.0F / 255.0F;
		worldrenderer.pos(0.0D, (double) this.height, (double) this.zLevel).tex(0.0D, (double) (f * f2))
				.color(f3, f3, f3, 1.0F).endVertex();
		worldrenderer.pos((double) i, (double) this.height, (double) this.zLevel)
				.tex((double) ((float) i * f2), (double) (f * f2)).color(f3, f3, f3, 1.0F).endVertex();
		worldrenderer.pos((double) i, 0.0D, (double) this.zLevel).tex((double) ((float) i * f2), (double) (f1 * f2))
				.color(f3, f3, f3, 1.0F).endVertex();
		worldrenderer.pos(0.0D, 0.0D, (double) this.zLevel).tex(0.0D, (double) (f1 * f2)).color(f3, f3, f3, 1.0F)
				.endVertex();
		tessellator.draw();
	}

	/**+
	 * Draws the screen and all the components in it. Args : mouseX,
	 * mouseY, renderPartialTicks
	 */
	public void drawScreen(int i, int j, float f) {
		this.drawWinGameScreen(i, j, f);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		short short1 = 274;
		int k = this.width / 2 - short1 / 2;
		int l = this.height + 50;
		float f1 = -((float) this.field_146581_h + f) * this.field_146578_s;
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0F, f1, 0.0F);
		this.mc.getTextureManager().bindTexture(MINECRAFT_LOGO);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.drawTexturedModalRect(k, l, 0, 0, 155, 44);
		this.drawTexturedModalRect(k + 155, l, 0, 45, 155, 44);
		int i1 = l + 200;

		for (int j1 = 0; j1 < this.field_146582_i.size(); ++j1) {
			if (j1 == this.field_146582_i.size() - 1) {
				float f2 = (float) i1 + f1 - (float) (this.height / 2 - 6);
				if (f2 < 0.0F) {
					GlStateManager.translate(0.0F, -f2, 0.0F);
				}
			}

			if ((float) i1 + f1 + 12.0F + 8.0F > 0.0F && (float) i1 + f1 < (float) this.height) {
				String s = (String) this.field_146582_i.get(j1);
				if (s.startsWith("[C]")) {
					this.fontRendererObj.drawStringWithShadow(s.substring(3),
							(float) (k + (short1 - this.fontRendererObj.getStringWidth(s.substring(3))) / 2),
							(float) i1, 16777215);
				} else {
					this.fontRendererObj.fontRandom.setSeed((long) j1 * 4238972211L + (long) (this.field_146581_h / 4));
					this.fontRendererObj.drawStringWithShadow(s, (float) k, (float) i1, 16777215);
				}
			}

			i1 += 12;
		}

		GlStateManager.popMatrix();
		this.mc.getTextureManager().bindTexture(VIGNETTE_TEXTURE);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL_ZERO, GL_ONE_MINUS_SRC_COLOR);
		int k1 = this.width;
		int l1 = this.height;
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		worldrenderer.pos(0.0D, (double) l1, (double) this.zLevel).tex(0.0D, 1.0D).color(1.0F, 1.0F, 1.0F, 1.0F)
				.endVertex();
		worldrenderer.pos((double) k1, (double) l1, (double) this.zLevel).tex(1.0D, 1.0D).color(1.0F, 1.0F, 1.0F, 1.0F)
				.endVertex();
		worldrenderer.pos((double) k1, 0.0D, (double) this.zLevel).tex(1.0D, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F)
				.endVertex();
		worldrenderer.pos(0.0D, 0.0D, (double) this.zLevel).tex(0.0D, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		tessellator.draw();
		GlStateManager.disableBlend();
		super.drawScreen(i, j, f);
	}
}