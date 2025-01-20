package net.minecraft.client.gui;

import net.minecraft.client.settings.GameSettings;

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
public class GuiOptionButton extends GuiButton {
	private final GameSettings.Options enumOptions;

	public GuiOptionButton(int parInt1, int parInt2, int parInt3, String parString1) {
		this(parInt1, parInt2, parInt3, (GameSettings.Options) null, parString1);
	}

	public GuiOptionButton(int parInt1, int parInt2, int parInt3, int parInt4, int parInt5, String parString1) {
		super(parInt1, parInt2, parInt3, parInt4, parInt5, parString1);
		this.enumOptions = null;
	}

	public GuiOptionButton(int parInt1, int parInt2, int parInt3, GameSettings.Options parOptions, String parString1) {
		super(parInt1, parInt2, parInt3, 150, 20, parString1);
		this.enumOptions = parOptions;
	}

	public GameSettings.Options returnEnumOptions() {
		return this.enumOptions;
	}
}