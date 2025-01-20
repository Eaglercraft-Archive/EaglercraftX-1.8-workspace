package net.minecraft.client.resources;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

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
public class DefaultPlayerSkin {
	/**+
	 * The default skin for the Steve model.
	 */
	private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation("textures/entity/steve.png");
	/**+
	 * The default skin for the Alex model.
	 */
	private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation("textures/entity/alex.png");

	/**+
	 * Returns the default skind for versions prior to 1.8, which is
	 * always the Steve texture.
	 */
	public static ResourceLocation getDefaultSkinLegacy() {
		return TEXTURE_STEVE;
	}

	/**+
	 * Retrieves the default skin for this player. Depending on the
	 * model used this will be Alex or Steve.
	 */
	public static ResourceLocation getDefaultSkin(EaglercraftUUID playerUUID) {
		/**+
		 * Checks if a players skin model is slim or the default. The
		 * Alex model is slime while the Steve model is default.
		 */
		return isSlimSkin(playerUUID) ? TEXTURE_ALEX : TEXTURE_STEVE;
	}

	/**+
	 * Retrieves the type of skin that a player is using. The Alex
	 * model is slim while the Steve model is default.
	 */
	public static String getSkinType(EaglercraftUUID playerUUID) {
		/**+
		 * Checks if a players skin model is slim or the default. The
		 * Alex model is slime while the Steve model is default.
		 */
		return isSlimSkin(playerUUID) ? "slim" : "default";
	}

	/**+
	 * Checks if a players skin model is slim or the default. The
	 * Alex model is slime while the Steve model is default.
	 */
	private static boolean isSlimSkin(EaglercraftUUID playerUUID) {
		return (playerUUID.hashCode() & 1) == 1;
	}
}