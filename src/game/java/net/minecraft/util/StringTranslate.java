package net.minecraft.util;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.HString;
import net.lax1dude.eaglercraft.v1_8.IOUtils;
import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

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
public class StringTranslate {
	/**+
	 * Pattern that matches numeric variable placeholders in a
	 * resource string, such as "%d", "%3$d", "%.2f"
	 */
	private static final Pattern numericVariablePattern = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
	/**+
	 * A Splitter that splits a string on the first "=". For
	 * example, "a=b=c" would split into ["a", "b=c"].
	 */
	private static final Splitter equalSignSplitter = Splitter.on('=').limit(2);
	/**+
	 * Is the private singleton instance of StringTranslate.
	 */
	private static StringTranslate instance = new StringTranslate();
	static StringTranslate fallbackInstance = null;
	private final Map<String, String> languageList = Maps.newHashMap();
	private long lastUpdateTimeInMilliseconds;

	private StringTranslate() {
	}

	public static void initClient() {
		try (InputStream inputstream = EagRuntime.getRequiredResourceStream("/assets/minecraft/lang/en_US.lang")) {
			initServer(IOUtils.readLines(inputstream, StandardCharsets.UTF_8));
			fallbackInstance = new StringTranslate();
			fallbackInstance.replaceWith(instance.languageList);
		} catch (IOException e) {
			EagRuntime.debugPrintStackTrace(e);
		}
	}

	public static void initServer(List<String> strs) {
		instance.languageList.clear();
		for (int i = 0, l = strs.size(); i < l; ++i) {
			String s = strs.get(i);
			if (!s.isEmpty() && s.charAt(0) != 35) {
				String[] astring = (String[]) Iterables.toArray(equalSignSplitter.split(s), String.class);
				if (astring != null && astring.length == 2) {
					String s1 = astring[0];
					String s2 = numericVariablePattern.matcher(astring[1]).replaceAll("%s"); // TODO: originally "%$1s"
																								// but must be "%s" to
																								// work with TeaVM
																								// (why?)
					instance.languageList.put(s1, s2);
				}
			}
		}

		instance.lastUpdateTimeInMilliseconds = EagRuntime.steadyTimeMillis();
	}

	/**+
	 * Return the StringTranslate singleton instance
	 */
	static StringTranslate getInstance() {
		return instance;
	}

	/**+
	 * Replaces all the current instance's translations with the
	 * ones that are passed in.
	 */
	public static void replaceWith(Map<String, String> parMap) {
		instance.languageList.clear();
		instance.languageList.putAll(parMap);
		instance.lastUpdateTimeInMilliseconds = EagRuntime.steadyTimeMillis();
		SingleplayerServerController.updateLocale(dump());
	}

	/**+
	 * Translate a key to current language.
	 */
	public String translateKey(String key) {
		return this.tryTranslateKey(key);
	}

	/**+
	 * Translate a key to current language applying String.format()
	 */
	public String translateKeyFormat(String key, Object... format) {
		String s = this.tryTranslateKey(key);

		try {
			return HString.format(s, format);
		} catch (IllegalFormatException var5) {
			return "Format error: " + s;
		}
	}

	/**+
	 * Tries to look up a translation for the given key; spits back
	 * the key if no result was found.
	 */
	private String tryTranslateKey(String key) {
		String s = (String) this.languageList.get(key);
		return s == null ? key : s;
	}

	/**+
	 * Returns true if the passed key is in the translation table.
	 */
	public boolean isKeyTranslated(String key) {
		return this.languageList.containsKey(key);
	}

	/**+
	 * Gets the time, in milliseconds since epoch, that this
	 * instance was last updated
	 */
	public long getLastUpdateTimeInMilliseconds() {
		return this.lastUpdateTimeInMilliseconds;
	}

	public static List<String> dump() {
		List<String> ret = new ArrayList(instance.languageList.size());
		for (Entry<String, String> etr : instance.languageList.entrySet()) {
			ret.add(etr.getKey() + "=" + etr.getValue());
		}
		return ret;
	}
}