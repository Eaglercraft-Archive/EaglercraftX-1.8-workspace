package net.minecraft.entity.ai.attributes;

import java.util.Collection;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

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
public interface IAttributeInstance {
	/**+
	 * Get the Attribute this is an instance of
	 */
	IAttribute getAttribute();

	double getBaseValue();

	void setBaseValue(double var1);

	Collection<AttributeModifier> getModifiersByOperation(int var1);

	Collection<AttributeModifier> func_111122_c();

	boolean hasModifier(AttributeModifier var1);

	/**+
	 * Returns attribute modifier, if any, by the given UUID
	 */
	AttributeModifier getModifier(EaglercraftUUID var1);

	void applyModifier(AttributeModifier var1);

	void removeModifier(AttributeModifier var1);

	void removeAllModifiers();

	double getAttributeValue();
}