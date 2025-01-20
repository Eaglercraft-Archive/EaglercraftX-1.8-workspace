package net.minecraft.client.gui.spectator.categories;

import java.util.List;
import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuView;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
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
public class TeleportToTeam implements ISpectatorMenuView, ISpectatorMenuObject {
	private final List<ISpectatorMenuObject> field_178672_a = Lists.newArrayList();

	public TeleportToTeam() {
		Minecraft minecraft = Minecraft.getMinecraft();

		for (ScorePlayerTeam scoreplayerteam : minecraft.theWorld.getScoreboard().getTeams()) {
			this.field_178672_a.add(new TeleportToTeam.TeamSelectionObject(scoreplayerteam));
		}

	}

	public List<ISpectatorMenuObject> func_178669_a() {
		return this.field_178672_a;
	}

	public IChatComponent func_178670_b() {
		return new ChatComponentText("Select a team to teleport to");
	}

	public void func_178661_a(SpectatorMenu spectatormenu) {
		spectatormenu.func_178647_a(this);
	}

	public IChatComponent getSpectatorName() {
		return new ChatComponentText("Teleport to team member");
	}

	public void func_178663_a(float var1, int var2) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(GuiSpectator.field_175269_a);
		Gui.drawModalRectWithCustomSizedTexture(0, 0, 16.0F, 0.0F, 16, 16, 256.0F, 256.0F);
	}

	public boolean func_178662_A_() {
		for (int i = 0, l = this.field_178672_a.size(); i < l; ++i) {
			if (this.field_178672_a.get(i).func_178662_A_()) {
				return true;
			}
		}

		return false;
	}

	class TeamSelectionObject implements ISpectatorMenuObject {
		private final ScorePlayerTeam field_178676_b;
		private final ResourceLocation field_178677_c;
		private final List<NetworkPlayerInfo> field_178675_d;

		public TeamSelectionObject(ScorePlayerTeam parScorePlayerTeam) {
			this.field_178676_b = parScorePlayerTeam;
			this.field_178675_d = Lists.newArrayList();

			for (String s : parScorePlayerTeam.getMembershipCollection()) {
				NetworkPlayerInfo networkplayerinfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(s);
				if (networkplayerinfo != null) {
					this.field_178675_d.add(networkplayerinfo);
				}
			}

			this.field_178677_c = DefaultPlayerSkin.getDefaultSkinLegacy();

			// TODO: program team skins

			if (!this.field_178675_d.isEmpty()) {
				// String s1 = ((NetworkPlayerInfo) this.field_178675_d
				// .get((new
				// EaglercraftRandom()).nextInt(this.field_178675_d.size()))).getGameProfile().getName();
				// this.field_178677_c = AbstractClientPlayer.getLocationSkin(s1);
				// AbstractClientPlayer.getDownloadImageSkin(this.field_178677_c, s1);
			} else {
				// this.field_178677_c = DefaultPlayerSkin.getDefaultSkinLegacy();
			}

		}

		public void func_178661_a(SpectatorMenu spectatormenu) {
			spectatormenu.func_178647_a(new TeleportToPlayer(this.field_178675_d));
		}

		public IChatComponent getSpectatorName() {
			return new ChatComponentText(this.field_178676_b.getTeamName());
		}

		public void func_178663_a(float f, int i) {
			int j = -1;
			String s = FontRenderer.getFormatFromString(this.field_178676_b.getColorPrefix());
			if (s.length() >= 2) {
				j = Minecraft.getMinecraft().fontRendererObj.getColorCode(s.charAt(1));
			}

			if (j >= 0) {
				float f1 = (float) (j >> 16 & 255) / 255.0F;
				float f2 = (float) (j >> 8 & 255) / 255.0F;
				float f3 = (float) (j & 255) / 255.0F;
				Gui.drawRect(1, 1, 15, 15, MathHelper.func_180183_b(f1 * f, f2 * f, f3 * f) | i << 24);
			}

			Minecraft.getMinecraft().getTextureManager().bindTexture(this.field_178677_c);
			GlStateManager.color(f, f, f, (float) i / 255.0F);
			Gui.drawScaledCustomSizeModalRect(2, 2, 8.0F, 8.0F, 8, 8, 12, 12, 64.0F, 64.0F);
			Gui.drawScaledCustomSizeModalRect(2, 2, 40.0F, 8.0F, 8, 8, 12, 12, 64.0F, 64.0F);
		}

		public boolean func_178662_A_() {
			return !this.field_178675_d.isEmpty();
		}
	}
}