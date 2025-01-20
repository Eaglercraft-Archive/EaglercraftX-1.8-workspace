package net.minecraft.util;

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
public class ChatAllowedCharacters {
	/**+
	 * Array of the special characters that are allowed in any text
	 * drawing of Minecraft.
	 */
	public static final char[] allowedCharactersArray = new char[] { '/', '\n', '\r', '\t', '\u0000', '\f', '`', '?',
			'*', '\\', '<', '>', '|', '\"', ':' };

	public static boolean isAllowedCharacter(char character) {
		return character != 167 && character >= 32 && character != 127;
	}

	/**+
	 * Filter string by only keeping those characters for which
	 * isAllowedCharacter() returns true.
	 */
	public static String filterAllowedCharacters(String input) {
		StringBuilder stringbuilder = new StringBuilder();

		char[] chars = input.toCharArray();
		for (int i = 0; i < chars.length; ++i) {
			char c0 = chars[i];
			if (isAllowedCharacter(c0)) {
				stringbuilder.append(c0);
			}
		}

		return stringbuilder.toString();
	}
}