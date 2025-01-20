package net.minecraft.block.properties;

import java.util.Collection;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import net.minecraft.util.EnumFacing;

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
public class PropertyDirection extends PropertyEnum<EnumFacing> {
	protected PropertyDirection(String name, Collection<EnumFacing> values) {
		super(name, EnumFacing.class, values);
	}

	/**+
	 * Create a new PropertyDirection with the given name
	 */
	public static PropertyDirection create(String name) {
		/**+
		 * Create a new PropertyDirection with the given name
		 */
		return create(name, Predicates.alwaysTrue());
	}

	/**+
	 * Create a new PropertyDirection with the given name
	 */
	public static PropertyDirection create(String name, Predicate<EnumFacing> filter) {
		/**+
		 * Create a new PropertyDirection with the given name
		 */
		return create(name, Collections2.filter(Lists.newArrayList(EnumFacing._VALUES), filter));
	}

	/**+
	 * Create a new PropertyDirection with the given name
	 */
	public static PropertyDirection create(String name, Collection<EnumFacing> values) {
		return new PropertyDirection(name, values);
	}
}