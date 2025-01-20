package net.minecraft.client.audio;

import java.util.Map;

import com.carrotsearch.hppc.IntObjectHashMap;
import com.carrotsearch.hppc.IntObjectMap;
import com.google.common.collect.Maps;

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
public enum SoundCategory {
	MASTER("master", 0), MUSIC("music", 1), RECORDS("record", 2), WEATHER("weather", 3), BLOCKS("block", 4),
	MOBS("hostile", 5), ANIMALS("neutral", 6), PLAYERS("player", 7), AMBIENT("ambient", 8);

	public static final SoundCategory[] _VALUES = values();

	private static final Map<String, SoundCategory> NAME_CATEGORY_MAP = Maps.newHashMap();
	private static final IntObjectMap<SoundCategory> ID_CATEGORY_MAP = new IntObjectHashMap<>();
	private final String categoryName;
	private final int categoryId;

	private SoundCategory(String name, int id) {
		this.categoryName = name;
		this.categoryId = id;
	}

	public String getCategoryName() {
		return this.categoryName;
	}

	public int getCategoryId() {
		return this.categoryId;
	}

	public static SoundCategory getCategory(String name) {
		return (SoundCategory) NAME_CATEGORY_MAP.get(name);
	}

	static {
		SoundCategory[] categories = _VALUES;
		for (int i = 0; i < categories.length; ++i) {
			SoundCategory soundcategory = categories[i];
			if (NAME_CATEGORY_MAP.containsKey(soundcategory.getCategoryName())
					|| ID_CATEGORY_MAP.containsKey(soundcategory.getCategoryId())) {
				throw new Error("Clash in Sound Category ID & Name pools! Cannot insert " + soundcategory);
			}

			NAME_CATEGORY_MAP.put(soundcategory.getCategoryName(), soundcategory);
			ID_CATEGORY_MAP.put(soundcategory.getCategoryId(), soundcategory);
		}

	}
}