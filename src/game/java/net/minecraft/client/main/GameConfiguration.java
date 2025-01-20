package net.minecraft.client.main;

import net.minecraft.util.Session;

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
public class GameConfiguration {
	public final GameConfiguration.UserInformation userInfo;
	public final GameConfiguration.DisplayInformation displayInfo;
	public final GameConfiguration.GameInformation gameInfo;

	public GameConfiguration(GameConfiguration.UserInformation userInfoIn,
			GameConfiguration.DisplayInformation displayInfoIn, GameConfiguration.GameInformation gameInfoIn) {
		this.userInfo = userInfoIn;
		this.displayInfo = displayInfoIn;
		this.gameInfo = gameInfoIn;
	}

	public static class DisplayInformation {
		public final int width;
		public final int height;
		public final boolean fullscreen;
		public final boolean checkGlErrors;

		public DisplayInformation(int widthIn, int heightIn, boolean fullscreenIn, boolean checkGlErrorsIn) {
			this.width = widthIn;
			this.height = heightIn;
			this.fullscreen = fullscreenIn;
			this.checkGlErrors = checkGlErrorsIn;
		}
	}

	public static class GameInformation {
		public final boolean isDemo;
		public final String version;

		public GameInformation(boolean isDemoIn, String versionIn) {
			this.isDemo = isDemoIn;
			this.version = versionIn;
		}
	}

	public static class UserInformation {
		public final Session session;

		public UserInformation(Session parSession) {
			this.session = parSession;
		}
	}
}