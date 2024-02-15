package net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.material;

import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.material.MapColor;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.material.Material;

import static net.lax1dude.eaglercraft.v1_8.sp.server.classes.ContextUtil.__checkIntegratedContextValid;

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
public class MaterialLogic extends Material {

	static {
		__checkIntegratedContextValid("net/minecraft/block/material/MaterialLogic");
	}

	public MaterialLogic(MapColor color) {
		super(color);
		this.setAdventureModeExempt();
	}

	/**+
	 * Returns true if the block is a considered solid. This is true
	 * by default.
	 */
	public boolean isSolid() {
		return false;
	}

	/**+
	 * Will prevent grass from growing on dirt underneath and kill
	 * any grass below it if it returns true
	 */
	public boolean blocksLight() {
		return false;
	}

	/**+
	 * Returns if this material is considered solid or not
	 */
	public boolean blocksMovement() {
		return false;
	}
}