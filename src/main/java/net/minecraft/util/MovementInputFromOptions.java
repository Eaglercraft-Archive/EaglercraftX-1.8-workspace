package net.minecraft.util;

import net.minecraft.client.settings.GameSettings;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class MovementInputFromOptions extends MovementInput {
	private final GameSettings gameSettings;

	public MovementInputFromOptions(GameSettings gameSettingsIn) {
		this.gameSettings = gameSettingsIn;
	}

	public void updatePlayerMoveState() {
		this.moveStrafe = 0.0F;
		this.moveForward = 0.0F;
		if (this.gameSettings.keyBindForward.isKeyDown()) {
			++this.moveForward;
		}

		if (this.gameSettings.keyBindBack.isKeyDown()) {
			--this.moveForward;
		}

		if (this.gameSettings.keyBindLeft.isKeyDown()) {
			++this.moveStrafe;
		}

		if (this.gameSettings.keyBindRight.isKeyDown()) {
			--this.moveStrafe;
		}

		this.jump = this.gameSettings.keyBindJump.isKeyDown();
		this.sneak = this.gameSettings.keyBindSneak.isKeyDown();
		if (this.sneak) {
			this.moveStrafe = (float) ((double) this.moveStrafe * 0.3D);
			this.moveForward = (float) ((double) this.moveForward * 0.3D);
		}

	}
}