package net.minecraft.block.properties;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.util.IStringSerializable;

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
public class PropertyEnum<T extends Enum<T> & IStringSerializable> extends PropertyHelper<T> {
	private final ImmutableSet<T> allowedValues;
	private final Map<String, T> nameToValue = Maps.newHashMap();

	protected PropertyEnum(String name, Class<T> valueClass, Collection<T> allowedValues) {
		super(name, valueClass);
		this.allowedValues = ImmutableSet.copyOf(allowedValues);

		for (T oenum : allowedValues) {
			String s = ((IStringSerializable) oenum).getName();
			if (this.nameToValue.containsKey(s)) {
				throw new IllegalArgumentException("Multiple values have the same name \'" + s + "\'");
			}

			this.nameToValue.put(s, oenum);
		}

	}

	public Collection<T> getAllowedValues() {
		return this.allowedValues;
	}

	/**+
	 * Get the name for the given value.
	 */
	public String getName(Object oenum) {
		return ((IStringSerializable) oenum).getName();
	}

	public static <T extends Enum<T> & IStringSerializable> PropertyEnum<T> create(String name, Class<T> clazz) {
		/**+
		 * Create a new PropertyEnum with all Enum constants of the
		 * given class.
		 */
		return create(name, clazz, Predicates.alwaysTrue());
	}

	public static <T extends Enum<T> & IStringSerializable> PropertyEnum<T> create(String name, Class<T> clazz,
			Predicate<T> filter) {
		/**+
		 * Create a new PropertyEnum with all Enum constants of the
		 * given class.
		 */
		return create(name, clazz, Collections2.filter(Lists.newArrayList(clazz.getEnumConstants()), filter));
	}

	public static <T extends Enum<T> & IStringSerializable> PropertyEnum<T> create(String name, Class<T> clazz,
			T... values) {
		/**+
		 * Create a new PropertyEnum with all Enum constants of the
		 * given class.
		 */
		return create(name, clazz, Lists.newArrayList(values));
	}

	public static <T extends Enum<T> & IStringSerializable> PropertyEnum<T> create(String name, Class<T> clazz,
			Collection<T> values) {
		return new PropertyEnum(name, clazz, values);
	}
}