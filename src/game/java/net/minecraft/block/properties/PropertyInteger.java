package net.minecraft.block.properties;

import java.util.Collection;
import java.util.HashSet;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

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
public class PropertyInteger extends PropertyHelper<Integer> {
	private final ImmutableSet<Integer> allowedValues;

	protected PropertyInteger(String name, int min, int max) {
		super(name, Integer.class);
		if (min < 0) {
			throw new IllegalArgumentException("Min value of " + name + " must be 0 or greater");
		} else if (max <= min) {
			throw new IllegalArgumentException("Max value of " + name + " must be greater than min (" + min + ")");
		} else {
			HashSet hashset = Sets.newHashSet();

			for (int i = min; i <= max; ++i) {
				hashset.add(Integer.valueOf(i));
			}

			this.allowedValues = ImmutableSet.copyOf(hashset);
		}
	}

	public Collection<Integer> getAllowedValues() {
		return this.allowedValues;
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (object != null && this.getClass() == object.getClass()) {
			if (!super.equals(object)) {
				return false;
			} else {
				PropertyInteger propertyinteger = (PropertyInteger) object;
				return this.allowedValues.equals(propertyinteger.allowedValues);
			}
		} else {
			return false;
		}
	}

	public int hashCode() {
		int i = super.hashCode();
		i = 31 * i + this.allowedValues.hashCode();
		return i;
	}

	public static PropertyInteger create(String name, int min, int max) {
		return new PropertyInteger(name, min, max);
	}

	/**+
	 * Get the name for the given value.
	 */
	public String getName(Object integer) {
		return integer.toString();
	}
}