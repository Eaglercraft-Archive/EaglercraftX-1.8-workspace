package net.minecraft.util;

import org.apache.commons.lang3.Validate;

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
public class ResourceLocation {
	protected final String resourceDomain;
	protected final String resourcePath;

	public Object cachedPointer = null;
	public int cachedPointerType = 0;

	public static final int CACHED_POINTER_NONE = 0;
	public static final int CACHED_POINTER_TEXTURE = 1;
	public static final int CACHED_POINTER_EAGLER_MESH = 2;

	protected ResourceLocation(int parInt1, String... resourceName) {
		this.resourceDomain = org.apache.commons.lang3.StringUtils.isEmpty(resourceName[0]) ? "minecraft"
				: resourceName[0].toLowerCase();
		this.resourcePath = resourceName[1];
		Validate.notNull(this.resourcePath);
	}

	public ResourceLocation(String resourceName) {
		this(0, splitObjectName(resourceName));
	}

	public ResourceLocation(String resourceDomainIn, String resourcePathIn) {
		this(0, new String[] { resourceDomainIn, resourcePathIn });
	}

	/**+
	 * Splits an object name (such as minecraft:apple) into the
	 * domain and path parts and returns these as an array of length
	 * 2. If no colon is present in the passed value the returned
	 * array will contain {null, toSplit}.
	 */
	protected static String[] splitObjectName(String toSplit) {
		String[] astring = new String[] { null, toSplit };
		int i = toSplit.indexOf(58);
		if (i >= 0) {
			astring[1] = toSplit.substring(i + 1, toSplit.length());
			if (i > 1) {
				astring[0] = toSplit.substring(0, i);
			}
		}

		return astring;
	}

	public String getResourcePath() {
		return this.resourcePath;
	}

	public String getResourceDomain() {
		return this.resourceDomain;
	}

	public String toString() {
		return this.resourceDomain + ':' + this.resourcePath;
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (!(object instanceof ResourceLocation)) {
			return false;
		} else {
			ResourceLocation resourcelocation = (ResourceLocation) object;
			return this.resourceDomain.equals(resourcelocation.resourceDomain)
					&& this.resourcePath.equals(resourcelocation.resourcePath);
		}
	}

	public int hashCode() {
		return 31 * this.resourceDomain.hashCode() + this.resourcePath.hashCode();
	}
}