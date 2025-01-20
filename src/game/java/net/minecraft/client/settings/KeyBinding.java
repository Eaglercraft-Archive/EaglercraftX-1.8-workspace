package net.minecraft.client.settings;

import java.util.List;
import java.util.Set;

import com.carrotsearch.hppc.IntObjectHashMap;
import com.carrotsearch.hppc.IntObjectMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.client.resources.I18n;

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
public class KeyBinding implements Comparable<KeyBinding> {
	private static final List<KeyBinding> keybindArray = Lists.newArrayList();
	private static final IntObjectMap<KeyBinding> hash = new IntObjectHashMap<>();
	private static final Set<String> keybindSet = Sets.newHashSet();
	private final String keyDescription;
	private final int keyCodeDefault;
	private final String keyCategory;
	private int keyCode;
	private boolean pressed;
	private int pressTime;

	public static void onTick(int keyCode) {
		if (keyCode != 0) {
			KeyBinding keybinding = hash.get(keyCode);
			if (keybinding != null) {
				++keybinding.pressTime;
			}

		}
	}

	public static void setKeyBindState(int keyCode, boolean pressed) {
		if (keyCode != 0) {
			KeyBinding keybinding = hash.get(keyCode);
			if (keybinding != null) {
				keybinding.pressed = pressed;
			}

		}
	}

	public static void unPressAllKeys() {
		for (int i = 0, l = keybindArray.size(); i < l; ++i) {
			keybindArray.get(i).unpressKey();
		}

	}

	public static void resetKeyBindingArrayAndHash() {
		hash.clear();

		for (int i = 0, l = keybindArray.size(); i < l; ++i) {
			KeyBinding keybinding = keybindArray.get(i);
			hash.put(keybinding.keyCode, keybinding);
		}

	}

	public static Set<String> getKeybinds() {
		return keybindSet;
	}

	public KeyBinding(String description, int keyCode, String category) {
		this.keyDescription = description;
		this.keyCode = keyCode;
		this.keyCodeDefault = keyCode;
		this.keyCategory = category;
		keybindArray.add(this);
		hash.put(keyCode, this);
		keybindSet.add(category);
	}

	/**+
	 * Returns true if the key is pressed (used for continuous
	 * querying). Should be used in tickers.
	 */
	public boolean isKeyDown() {
		return this.pressed;
	}

	public String getKeyCategory() {
		return this.keyCategory;
	}

	/**+
	 * Returns true on the initial key press. For continuous
	 * querying use {@link isKeyDown()}. Should be used in key
	 * events.
	 */
	public boolean isPressed() {
		if (this.pressTime == 0) {
			return false;
		} else {
			--this.pressTime;
			return true;
		}
	}

	private void unpressKey() {
		this.pressTime = 0;
		this.pressed = false;
	}

	public String getKeyDescription() {
		return this.keyDescription;
	}

	public int getKeyCodeDefault() {
		return this.keyCodeDefault;
	}

	public int getKeyCode() {
		return this.keyCode;
	}

	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}

	public int compareTo(KeyBinding keybinding) {
		int i = I18n.format(this.keyCategory, new Object[0])
				.compareTo(I18n.format(keybinding.keyCategory, new Object[0]));
		if (i == 0) {
			i = I18n.format(this.keyDescription, new Object[0])
					.compareTo(I18n.format(keybinding.keyDescription, new Object[0]));
		}

		return i;
	}
}