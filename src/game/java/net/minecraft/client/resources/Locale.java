package net.minecraft.client.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.HString;
import net.lax1dude.eaglercraft.v1_8.IOUtils;
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
public class Locale {
	/**+
	 * Splits on "="
	 */
	private static final Splitter splitter = Splitter.on('=').limit(2);
	private static final Pattern pattern = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
	Map<String, String> properties = Maps.newHashMap();
	private boolean unicode;

	private static final Set<String> hasShownMissing = new HashSet<>();

	/**+
	 * par2 is a list of languages. For each language $L and domain
	 * $D, attempts to load the resource $D:lang/$L.lang
	 */
	public synchronized void loadLocaleDataFiles(IResourceManager resourceManager, List<String> parList) {
		this.properties.clear();

		for (String s : parList) {
			String s1 = HString.format("lang/%s.lang", new Object[] { s });

			for (String s2 : resourceManager.getResourceDomains()) {
				try {
					List<IResource> res = resourceManager.getAllResources(new ResourceLocation(s2, s1));
					if (res.size() > 0) {
						this.loadLocaleData(res);
					} else {
						if (s2.equalsIgnoreCase("minecraft") && hasShownMissing.add(s)) {
							EagRuntime.showPopup("ERROR: language \"" + s + "\" is not available on this site!");
						}
					}
				} catch (IOException var9) {
					if (s2.equalsIgnoreCase("minecraft") && hasShownMissing.add(s)) {
						EagRuntime.showPopup("ERROR: language \"" + s + "\" is not available on this site!");
					}
				}
			}
		}

		this.checkUnicode();
	}

	public boolean isUnicode() {
		return this.unicode;
	}

	private void checkUnicode() {
		this.unicode = false;
		int i = 0;
		int j = 0;

		for (String s : this.properties.values()) {
			int k = s.length();
			j += k;

			for (int l = 0; l < k; ++l) {
				if (s.charAt(l) >= 256) {
					++i;
				}
			}
		}

		float f = (float) i / (float) j;
		this.unicode = (double) f > 0.1D;
	}

	/**+
	 * par1 is a list of Resources
	 */
	private void loadLocaleData(List<IResource> parList) throws IOException {
		for (IResource iresource : parList) {
			InputStream inputstream = iresource.getInputStream();

			try {
				this.loadLocaleData(inputstream);
			} finally {
				IOUtils.closeQuietly(inputstream);
			}
		}

	}

	/**+
	 * par1 is a list of Resources
	 */
	private void loadLocaleData(InputStream parInputStream) throws IOException {
		for (String s : IOUtils.readLines(parInputStream, Charsets.UTF_8)) {
			if (!s.isEmpty() && s.charAt(0) != 35) {
				String[] astring = (String[]) Iterables.toArray(splitter.split(s), String.class);
				if (astring != null && astring.length == 2) {
					String s1 = astring[0];
					String s2 = pattern.matcher(astring[1]).replaceAll("%s"); // TODO: originally "%$1s" but must be
																				// "%s" to work with TeaVM (why?)
					this.properties.put(s1, s2);
					if (s1.startsWith("eaglercraft.")) {
						this.properties.put(s1.substring(12), s2);
					}
				}
			}
		}

	}

	/**+
	 * Returns the translation, or the key itself if the key could
	 * not be translated.
	 */
	private String translateKeyPrivate(String parString1) {
		String s = (String) this.properties.get(parString1);
		return s == null ? parString1 : s;
	}

	/**+
	 * Calls String.format(translateKey(key), params)
	 */
	public String formatMessage(String translateKey, Object[] parameters) {
		String s = this.translateKeyPrivate(translateKey);

		try {
			return HString.format(s, parameters);
		} catch (IllegalFormatException var5) {
			return "Format error: " + s;
		}
	}
}