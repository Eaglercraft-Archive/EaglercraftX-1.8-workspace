package net.minecraft.entity.player;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

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
public enum EnumPlayerModelParts {
	CAPE(0, "cape"), JACKET(1, "jacket"), LEFT_SLEEVE(2, "left_sleeve"), RIGHT_SLEEVE(3, "right_sleeve"),
	LEFT_PANTS_LEG(4, "left_pants_leg"), RIGHT_PANTS_LEG(5, "right_pants_leg"), HAT(6, "hat");

	public static final EnumPlayerModelParts[] _VALUES = values();

	private final int partId;
	private final int partMask;
	private final String partName;
	private final IChatComponent field_179339_k;

	private EnumPlayerModelParts(int partIdIn, String partNameIn) {
		this.partId = partIdIn;
		this.partMask = 1 << partIdIn;
		this.partName = partNameIn;
		this.field_179339_k = new ChatComponentTranslation("options.modelPart." + partNameIn, new Object[0]);
	}

	public int getPartMask() {
		return this.partMask;
	}

	public int getPartId() {
		return this.partId;
	}

	public String getPartName() {
		return this.partName;
	}

	public IChatComponent func_179326_d() {
		return this.field_179339_k;
	}
}