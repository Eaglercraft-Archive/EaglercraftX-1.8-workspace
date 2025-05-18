package net.minecraft.client.network;

import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
import net.lax1dude.eaglercraft.v1_8.profanity_filter.ProfanityFilter;
import net.lax1dude.eaglercraft.v1_8.profile.SkinModel;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
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
public class NetworkPlayerInfo {
	private final GameProfile gameProfile;
	private String gameProfileProfanityFilter;
	private WorldSettings.GameType gameType;
	private int responseTime;
	private String skinType;
	private IChatComponent displayName;
	private IChatComponent displayNameProfanityFilter;
	private int field_178873_i = 0;
	private int field_178870_j = 0;
	private long field_178871_k = 0L;
	private long field_178868_l = 0L;
	private long field_178869_m = 0L;

	public NetworkPlayerInfo(GameProfile parGameProfile) {
		this.gameProfile = parGameProfile;
	}

	public NetworkPlayerInfo(S38PacketPlayerListItem.AddPlayerData parAddPlayerData) {
		this.gameProfile = parAddPlayerData.getProfile();
		this.gameType = parAddPlayerData.getGameMode();
		this.responseTime = parAddPlayerData.getPing();
		this.displayName = parAddPlayerData.getDisplayName();
		this.displayNameProfanityFilter = null;
	}

	/**+
	 * Returns the GameProfile for the player represented by this
	 * NetworkPlayerInfo instance
	 */
	public GameProfile getGameProfile() {
		return this.gameProfile;
	}

	public WorldSettings.GameType getGameType() {
		return this.gameType;
	}

	public int getResponseTime() {
		return this.responseTime;
	}

	protected void setGameType(WorldSettings.GameType parGameType) {
		this.gameType = parGameType;
	}

	protected void setResponseTime(int parInt1) {
		this.responseTime = parInt1;
	}

	public boolean hasLocationSkin() {
		return true;
	}

	public String getSkinType() {
		return getEaglerSkinModel().profileSkinType;
	}

	public SkinModel getEaglerSkinModel() {
		return Minecraft.getMinecraft().getNetHandler().getTextureCache().getPlayerSkin(this.gameProfile).getModel();
	}

	public ResourceLocation getLocationSkin() {
		return Minecraft.getMinecraft().getNetHandler().getTextureCache().getPlayerSkin(this.gameProfile).getLocation();
	}

	public ResourceLocation getLocationCape() {
		return Minecraft.getMinecraft().getNetHandler().getTextureCache().getPlayerCape(this.gameProfile);
	}

	public ScorePlayerTeam getPlayerTeam() {
		return Minecraft.getMinecraft().theWorld.getScoreboard().getPlayersTeam(this.getGameProfile().getName());
	}

	public void setDisplayName(IChatComponent displayNameIn) {
		this.displayName = displayNameIn;
		this.displayNameProfanityFilter = null;
	}

	public IChatComponent getDisplayName() {
		return this.displayName;
	}

	public IChatComponent getDisplayNameProfanityFilter() {
		if (Minecraft.getMinecraft().isEnableProfanityFilter()) {
			if (this.displayName != null) {
				if (this.displayNameProfanityFilter == null) {
					this.displayNameProfanityFilter = ProfanityFilter.getInstance()
							.profanityFilterChatComponent(this.displayName);
				}
				return this.displayNameProfanityFilter;
			} else {
				return null;
			}
		} else {
			return this.displayName;
		}
	}

	public String getGameProfileNameProfanityFilter() {
		if (Minecraft.getMinecraft().isEnableProfanityFilter()) {
			if (this.gameProfileProfanityFilter == null) {
				this.gameProfileProfanityFilter = ProfanityFilter.getInstance()
						.profanityFilterString(this.gameProfile.getName());
			}
			return this.gameProfileProfanityFilter;
		} else {
			return this.gameProfile.getName();
		}
	}

	public int func_178835_l() {
		return this.field_178873_i;
	}

	public void func_178836_b(int parInt1) {
		this.field_178873_i = parInt1;
	}

	public int func_178860_m() {
		return this.field_178870_j;
	}

	public void func_178857_c(int parInt1) {
		this.field_178870_j = parInt1;
	}

	public long func_178847_n() {
		return this.field_178871_k;
	}

	public void func_178846_a(long parLong1) {
		this.field_178871_k = parLong1;
	}

	public long func_178858_o() {
		return this.field_178868_l;
	}

	public void func_178844_b(long parLong1) {
		this.field_178868_l = parLong1;
	}

	public long func_178855_p() {
		return this.field_178869_m;
	}

	public void func_178843_c(long parLong1) {
		this.field_178869_m = parLong1;
	}
}