package net.minecraft.client.gui;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.util.Comparator;
import java.util.List;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldSettings;

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
public class GuiPlayerTabOverlay extends Gui {
	private static final Ordering<NetworkPlayerInfo> field_175252_a = Ordering
			.from(new GuiPlayerTabOverlay.PlayerComparator());
	private final Minecraft mc;
	private final GuiIngame guiIngame;
	private IChatComponent footer;
	private IChatComponent header;
	private long lastTimeOpened;
	private boolean isBeingRendered;

	public GuiPlayerTabOverlay(Minecraft mcIn, GuiIngame guiIngameIn) {
		this.mc = mcIn;
		this.guiIngame = guiIngameIn;
	}

	/**+
	 * Returns the name that should be renderd for the player
	 * supplied
	 */
	public String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn) {
		IChatComponent dname = networkPlayerInfoIn.getDisplayNameProfanityFilter();
		return dname != null ? dname.getFormattedText()
				: ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(),
						networkPlayerInfoIn.getGameProfileNameProfanityFilter());
	}

	/**+
	 * Called by GuiIngame to update the information stored in the
	 * playerlist, does not actually render the list, however.
	 */
	public void updatePlayerList(boolean willBeRendered) {
		if (willBeRendered && !this.isBeingRendered) {
			this.lastTimeOpened = Minecraft.getSystemTime();
		}

		this.isBeingRendered = willBeRendered;
	}

	/**+
	 * Renders the playerlist, its background, headers and footers.
	 */
	public void renderPlayerlist(int width, Scoreboard scoreboardIn, ScoreObjective scoreObjectiveIn) {
		NetHandlerPlayClient nethandlerplayclient = this.mc.thePlayer.sendQueue;
		List list = field_175252_a.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
		int i = 0;
		int j = 0;

		for (int m = 0, n = list.size(); m < n; ++m) {
			NetworkPlayerInfo networkplayerinfo = (NetworkPlayerInfo) list.get(m);
			int k = this.mc.fontRendererObj.getStringWidth(this.getPlayerName(networkplayerinfo));
			i = Math.max(i, k);
			if (scoreObjectiveIn != null
					&& scoreObjectiveIn.getRenderType() != IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
				k = this.mc.fontRendererObj.getStringWidth(" " + scoreboardIn
						.getValueFromObjective(networkplayerinfo.getGameProfile().getName(), scoreObjectiveIn)
						.getScorePoints());
				j = Math.max(j, k);
			}
		}

		list = list.subList(0, Math.min(list.size(), 80));
		int l3 = list.size();
		int i4 = l3;

		int j4;
		for (j4 = 1; i4 > 20; i4 = (l3 + j4 - 1) / j4) {
			++j4;
		}

		boolean flag = true;
		int l;
		if (scoreObjectiveIn != null) {
			if (scoreObjectiveIn.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
				l = 90;
			} else {
				l = j;
			}
		} else {
			l = 0;
		}

		int i1 = Math.min(j4 * ((flag ? 9 : 0) + i + l + 13), width - 50) / j4;
		int j1 = width / 2 - (i1 * j4 + (j4 - 1) * 5) / 2;
		int k1 = 10;
		int l1 = i1 * j4 + (j4 - 1) * 5;
		List<String> list1 = null;
		List<String> list2 = null;
		if (this.header != null) {
			list1 = this.mc.fontRendererObj.listFormattedStringToWidth(this.header.getFormattedText(), width - 50);

			for (int m = 0, n = list1.size(); m < n; ++m) {
				l1 = Math.max(l1, this.mc.fontRendererObj.getStringWidth(list1.get(m)));
			}
		}

		if (this.footer != null) {
			list2 = this.mc.fontRendererObj.listFormattedStringToWidth(this.footer.getFormattedText(), width - 50);

			for (int m = 0, n = list2.size(); m < n; ++m) {
				l1 = Math.max(l1, this.mc.fontRendererObj.getStringWidth(list2.get(m)));
			}
		}

		if (list1 != null) {
			drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1,
					k1 + list1.size() * this.mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);

			for (int m = 0, n = list1.size(); m < n; ++m) {
				String s3 = list1.get(m);
				int i2 = this.mc.fontRendererObj.getStringWidth(s3);
				this.mc.fontRendererObj.drawStringWithShadow(s3, (float) (width / 2 - i2 / 2), (float) k1, -1);
				k1 += this.mc.fontRendererObj.FONT_HEIGHT;
			}

			++k1;
		}

		drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + i4 * 9, Integer.MIN_VALUE);

		for (int k4 = 0; k4 < l3; ++k4) {
			int l4 = k4 / i4;
			int i5 = k4 % i4;
			int j2 = j1 + l4 * i1 + l4 * 5;
			int k2 = k1 + i5 * 9;
			drawRect(j2, k2, j2 + i1, k2 + 8, 553648127);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableAlpha();
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
			if (k4 < list.size()) {
				NetworkPlayerInfo networkplayerinfo1 = (NetworkPlayerInfo) list.get(k4);
				String s1 = this.getPlayerName(networkplayerinfo1);
				GameProfile gameprofile = networkplayerinfo1.getGameProfile();
				if (flag) {
					EntityPlayer entityplayer = this.mc.theWorld.getPlayerEntityByUUID(gameprofile.getId());
					boolean flag1 = entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.CAPE)
							&& (gameprofile.getName().equals("Dinnerbone") || gameprofile.getName().equals("Grumm"));
					this.mc.getTextureManager().bindTexture(networkplayerinfo1.getLocationSkin());
					int l2 = 8 + (flag1 ? 8 : 0);
					int i3 = 8 * (flag1 ? -1 : 1);
					Gui.drawScaledCustomSizeModalRect(j2, k2, 8.0F, (float) l2, 8, i3, 8, 8, 64.0F, 64.0F);
					if (entityplayer == null || entityplayer.isWearing(EnumPlayerModelParts.HAT)) {
						int j3 = 8 + (flag1 ? 8 : 0);
						int k3 = 8 * (flag1 ? -1 : 1);
						Gui.drawScaledCustomSizeModalRect(j2, k2, 40.0F, (float) j3, 8, k3, 8, 8, 64.0F, 64.0F);
					}

					j2 += 9;
				}

				if (networkplayerinfo1.getGameType() == WorldSettings.GameType.SPECTATOR) {
					s1 = EnumChatFormatting.ITALIC + s1;
					this.mc.fontRendererObj.drawStringWithShadow(s1, (float) j2, (float) k2, -1862270977);
				} else {
					this.mc.fontRendererObj.drawStringWithShadow(s1, (float) j2, (float) k2, -1);
				}

				if (scoreObjectiveIn != null && networkplayerinfo1.getGameType() != WorldSettings.GameType.SPECTATOR) {
					int k5 = j2 + i + 1;
					int l5 = k5 + l;
					if (l5 - k5 > 5) {
						this.drawScoreboardValues(scoreObjectiveIn, k2, gameprofile.getName(), k5, l5,
								networkplayerinfo1);
					}
				}

				this.drawPing(i1, j2 - (flag ? 9 : 0), k2, networkplayerinfo1);
			}
		}

		if (list2 != null) {
			k1 = k1 + i4 * 9 + 1;
			drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1,
					k1 + list2.size() * this.mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);

			for (int m = 0, n = list2.size(); m < n; ++m) {
				String s4 = list2.get(m);
				int j5 = this.mc.fontRendererObj.getStringWidth(s4);
				this.mc.fontRendererObj.drawStringWithShadow(s4, (float) (width / 2 - j5 / 2), (float) k1, -1);
				k1 += this.mc.fontRendererObj.FONT_HEIGHT;
			}
		}

	}

	protected void drawPing(int networkPlayerInfoIn, int parInt2, int parInt3, NetworkPlayerInfo parNetworkPlayerInfo) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(icons);
		byte b0 = 0;
		byte b1 = 0;
		if (parNetworkPlayerInfo.getResponseTime() < 0) {
			b1 = 5;
		} else if (parNetworkPlayerInfo.getResponseTime() < 150) {
			b1 = 0;
		} else if (parNetworkPlayerInfo.getResponseTime() < 300) {
			b1 = 1;
		} else if (parNetworkPlayerInfo.getResponseTime() < 600) {
			b1 = 2;
		} else if (parNetworkPlayerInfo.getResponseTime() < 1000) {
			b1 = 3;
		} else {
			b1 = 4;
		}

		this.zLevel += 100.0F;
		this.drawTexturedModalRect(parInt2 + networkPlayerInfoIn - 11, parInt3, 0 + b0 * 10, 176 + b1 * 8, 10, 8);
		this.zLevel -= 100.0F;
	}

	private void drawScoreboardValues(ScoreObjective parScoreObjective, int parInt1, String parString1, int parInt2,
			int parInt3, NetworkPlayerInfo parNetworkPlayerInfo) {
		int i = parScoreObjective.getScoreboard().getValueFromObjective(parString1, parScoreObjective).getScorePoints();
		if (parScoreObjective.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
			this.mc.getTextureManager().bindTexture(icons);
			if (this.lastTimeOpened == parNetworkPlayerInfo.func_178855_p()) {
				if (i < parNetworkPlayerInfo.func_178835_l()) {
					parNetworkPlayerInfo.func_178846_a(Minecraft.getSystemTime());
					parNetworkPlayerInfo.func_178844_b((long) (this.guiIngame.getUpdateCounter() + 20));
				} else if (i > parNetworkPlayerInfo.func_178835_l()) {
					parNetworkPlayerInfo.func_178846_a(Minecraft.getSystemTime());
					parNetworkPlayerInfo.func_178844_b((long) (this.guiIngame.getUpdateCounter() + 10));
				}
			}

			if (Minecraft.getSystemTime() - parNetworkPlayerInfo.func_178847_n() > 1000L
					|| this.lastTimeOpened != parNetworkPlayerInfo.func_178855_p()) {
				parNetworkPlayerInfo.func_178836_b(i);
				parNetworkPlayerInfo.func_178857_c(i);
				parNetworkPlayerInfo.func_178846_a(Minecraft.getSystemTime());
			}

			parNetworkPlayerInfo.func_178843_c(this.lastTimeOpened);
			parNetworkPlayerInfo.func_178836_b(i);
			int j = MathHelper.ceiling_float_int((float) Math.max(i, parNetworkPlayerInfo.func_178860_m()) / 2.0F);
			int k = Math.max(MathHelper.ceiling_float_int((float) (i / 2)),
					Math.max(MathHelper.ceiling_float_int((float) (parNetworkPlayerInfo.func_178860_m() / 2)), 10));
			boolean flag = parNetworkPlayerInfo.func_178858_o() > (long) this.guiIngame.getUpdateCounter()
					&& (parNetworkPlayerInfo.func_178858_o() - (long) this.guiIngame.getUpdateCounter()) / 3L
							% 2L == 1L;
			if (j > 0) {
				float f = Math.min((float) (parInt3 - parInt2 - 4) / (float) k, 9.0F);
				if (f > 3.0F) {
					for (int l = j; l < k; ++l) {
						this.drawTexturedModalRect((float) parInt2 + (float) l * f, (float) parInt1, flag ? 25 : 16, 0,
								9, 9);
					}

					for (int j1 = 0; j1 < j; ++j1) {
						this.drawTexturedModalRect((float) parInt2 + (float) j1 * f, (float) parInt1, flag ? 25 : 16, 0,
								9, 9);
						if (flag) {
							if (j1 * 2 + 1 < parNetworkPlayerInfo.func_178860_m()) {
								this.drawTexturedModalRect((float) parInt2 + (float) j1 * f, (float) parInt1, 70, 0, 9,
										9);
							}

							if (j1 * 2 + 1 == parNetworkPlayerInfo.func_178860_m()) {
								this.drawTexturedModalRect((float) parInt2 + (float) j1 * f, (float) parInt1, 79, 0, 9,
										9);
							}
						}

						if (j1 * 2 + 1 < i) {
							this.drawTexturedModalRect((float) parInt2 + (float) j1 * f, (float) parInt1,
									j1 >= 10 ? 160 : 52, 0, 9, 9);
						}

						if (j1 * 2 + 1 == i) {
							this.drawTexturedModalRect((float) parInt2 + (float) j1 * f, (float) parInt1,
									j1 >= 10 ? 169 : 61, 0, 9, 9);
						}
					}
				} else {
					float f1 = MathHelper.clamp_float((float) i / 20.0F, 0.0F, 1.0F);
					int i1 = (int) ((1.0F - f1) * 255.0F) << 16 | (int) (f1 * 255.0F) << 8;
					String s = "" + (float) i / 2.0F;
					if (parInt3 - this.mc.fontRendererObj.getStringWidth(s + "hp") >= parInt2) {
						s = s + "hp";
					}

					this.mc.fontRendererObj.drawStringWithShadow(s,
							(float) ((parInt3 + parInt2) / 2 - this.mc.fontRendererObj.getStringWidth(s) / 2),
							(float) parInt1, i1);
				}
			}
		} else {
			String s1 = EnumChatFormatting.YELLOW + "" + i;
			this.mc.fontRendererObj.drawStringWithShadow(s1,
					(float) (parInt3 - this.mc.fontRendererObj.getStringWidth(s1)), (float) parInt1, 16777215);
		}

	}

	public void setFooter(IChatComponent footerIn) {
		this.footer = footerIn;
	}

	public void setHeader(IChatComponent headerIn) {
		this.header = headerIn;
	}

	public void func_181030_a() {
		this.header = null;
		this.footer = null;
	}

	static class PlayerComparator implements Comparator<NetworkPlayerInfo> {
		private PlayerComparator() {
		}

		public int compare(NetworkPlayerInfo networkplayerinfo, NetworkPlayerInfo networkplayerinfo1) {
			ScorePlayerTeam scoreplayerteam = networkplayerinfo.getPlayerTeam();
			ScorePlayerTeam scoreplayerteam1 = networkplayerinfo1.getPlayerTeam();
			return ComparisonChain.start()
					.compareTrueFirst(networkplayerinfo.getGameType() != WorldSettings.GameType.SPECTATOR,
							networkplayerinfo1.getGameType() != WorldSettings.GameType.SPECTATOR)
					.compare(scoreplayerteam != null ? scoreplayerteam.getRegisteredName() : "",
							scoreplayerteam1 != null ? scoreplayerteam1.getRegisteredName() : "")
					.compare(networkplayerinfo.getGameProfile().getName(),
							networkplayerinfo1.getGameProfile().getName())
					.result();
		}
	}
}