package net.minecraft.client.gui.spectator.categories;

import java.util.List;

import com.google.common.base.Objects;

import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuView;
import net.minecraft.client.gui.spectator.SpectatorMenu;

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
public class SpectatorDetails {
	private final ISpectatorMenuView field_178684_a;
	private final List<ISpectatorMenuObject> field_178682_b;
	private final int field_178683_c;

	public SpectatorDetails(ISpectatorMenuView parISpectatorMenuView, List<ISpectatorMenuObject> parList, int parInt1) {
		this.field_178684_a = parISpectatorMenuView;
		this.field_178682_b = parList;
		this.field_178683_c = parInt1;
	}

	public ISpectatorMenuObject func_178680_a(int parInt1) {
		return parInt1 >= 0 && parInt1 < this.field_178682_b.size() ? (ISpectatorMenuObject) Objects
				.firstNonNull(this.field_178682_b.get(parInt1), SpectatorMenu.field_178657_a)
				: SpectatorMenu.field_178657_a;
	}

	public int func_178681_b() {
		return this.field_178683_c;
	}
}