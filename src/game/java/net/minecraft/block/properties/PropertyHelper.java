package net.minecraft.block.properties;

import com.google.common.base.Objects;

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
public abstract class PropertyHelper<T extends Comparable<T>> implements IProperty<T> {
	private final Class<T> valueClass;
	private final String name;

	protected PropertyHelper(String name, Class<T> valueClass) {
		this.valueClass = valueClass;
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	/**+
	 * The class of the values of this property
	 */
	public Class<T> getValueClass() {
		return this.valueClass;
	}

	public String toString() {
		return Objects.toStringHelper(this).add("name", this.name).add("clazz", this.valueClass)
				.add("values", this.getAllowedValues()).toString();
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (object != null && this.getClass() == object.getClass()) {
			PropertyHelper propertyhelper = (PropertyHelper) object;
			return this.valueClass.equals(propertyhelper.valueClass) && this.name.equals(propertyhelper.name);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return 31 * this.valueClass.hashCode() + this.name.hashCode();
	}
}