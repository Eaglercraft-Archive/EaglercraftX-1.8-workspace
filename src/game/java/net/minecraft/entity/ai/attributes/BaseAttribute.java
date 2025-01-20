package net.minecraft.entity.ai.attributes;

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
public abstract class BaseAttribute implements IAttribute {
	private final IAttribute field_180373_a;
	private final String unlocalizedName;
	private final double defaultValue;
	private boolean shouldWatch;

	protected BaseAttribute(IAttribute parIAttribute, String unlocalizedNameIn, double defaultValueIn) {
		this.field_180373_a = parIAttribute;
		this.unlocalizedName = unlocalizedNameIn;
		this.defaultValue = defaultValueIn;
		if (unlocalizedNameIn == null) {
			throw new IllegalArgumentException("Name cannot be null!");
		}
	}

	public String getAttributeUnlocalizedName() {
		return this.unlocalizedName;
	}

	public double getDefaultValue() {
		return this.defaultValue;
	}

	public boolean getShouldWatch() {
		return this.shouldWatch;
	}

	public BaseAttribute setShouldWatch(boolean shouldWatchIn) {
		this.shouldWatch = shouldWatchIn;
		return this;
	}

	public IAttribute func_180372_d() {
		return this.field_180373_a;
	}

	public int hashCode() {
		return this.unlocalizedName.hashCode();
	}

	public boolean equals(Object object) {
		return object instanceof IAttribute
				&& this.unlocalizedName.equals(((IAttribute) object).getAttributeUnlocalizedName());
	}
}