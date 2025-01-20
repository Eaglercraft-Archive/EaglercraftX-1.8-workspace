package net.minecraft.client.resources.model;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.util.ResourceLocation;

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
public class ModelResourceLocation extends ResourceLocation {
	private final String variant;

	protected ModelResourceLocation(int parInt1, String... parArrayOfString) {
		super(0, new String[] { parArrayOfString[0], parArrayOfString[1] });
		this.variant = StringUtils.isEmpty(parArrayOfString[2]) ? "normal" : parArrayOfString[2].toLowerCase();
	}

	public ModelResourceLocation(String parString1) {
		this(0, parsePathString(parString1));
	}

	public ModelResourceLocation(ResourceLocation parResourceLocation, String parString1) {
		this(parResourceLocation.toString(), parString1);
	}

	public ModelResourceLocation(String parString1, String parString2) {
		this(0, parsePathString(parString1 + '#' + (parString2 == null ? "normal" : parString2)));
	}

	protected static String[] parsePathString(String parString1) {
		String[] astring = new String[] { null, parString1, null };
		int i = parString1.indexOf(35);
		String s = parString1;
		if (i >= 0) {
			astring[2] = parString1.substring(i + 1, parString1.length());
			if (i > 1) {
				s = parString1.substring(0, i);
			}
		}

		System.arraycopy(ResourceLocation.splitObjectName(s), 0, astring, 0, 2);
		return astring;
	}

	public String getVariant() {
		return this.variant;
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (object instanceof ModelResourceLocation && super.equals(object)) {
			ModelResourceLocation modelresourcelocation = (ModelResourceLocation) object;
			return this.variant.equals(modelresourcelocation.variant);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return 31 * super.hashCode() + this.variant.hashCode();
	}

	public String toString() {
		return super.toString() + '#' + this.variant;
	}
}