package net.minecraft.client.resources;

import net.lax1dude.eaglercraft.v1_8.HString;

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
public class Language implements Comparable<Language> {
	private final String languageCode;
	private final String region;
	private final String name;
	private final boolean bidirectional;

	public Language(String languageCodeIn, String regionIn, String nameIn, boolean bidirectionalIn) {
		this.languageCode = languageCodeIn;
		this.region = regionIn;
		this.name = nameIn;
		this.bidirectional = bidirectionalIn;
	}

	public String getLanguageCode() {
		return this.languageCode;
	}

	public boolean isBidirectional() {
		return this.bidirectional;
	}

	public String toString() {
		return HString.format("%s (%s)", new Object[] { this.name, this.region });
	}

	public boolean equals(Object object) {
		return this == object ? true
				: (!(object instanceof Language) ? false : this.languageCode.equals(((Language) object).languageCode));
	}

	public int hashCode() {
		return this.languageCode.hashCode();
	}

	public int compareTo(Language language) {
		return this.languageCode.compareTo(language.languageCode);
	}
}