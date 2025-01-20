package net.minecraft.world.gen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;

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
public class FlatGeneratorInfo {
	/**+
	 * List of layers on this preset.
	 */
	private final List<FlatLayerInfo> flatLayers = Lists.newArrayList();
	private final Map<String, Map<String, String>> worldFeatures = Maps.newHashMap();
	private int biomeToUse;

	/**+
	 * Return the biome used on this preset.
	 */
	public int getBiome() {
		return this.biomeToUse;
	}

	/**+
	 * Set the biome used on this preset.
	 */
	public void setBiome(int parInt1) {
		this.biomeToUse = parInt1;
	}

	public Map<String, Map<String, String>> getWorldFeatures() {
		return this.worldFeatures;
	}

	/**+
	 * Return the list of layers on this preset.
	 */
	public List<FlatLayerInfo> getFlatLayers() {
		return this.flatLayers;
	}

	public void func_82645_d() {
		int i = 0;

		for (int j = 0, l = this.flatLayers.size(); j < l; ++j) {
			FlatLayerInfo flatlayerinfo = this.flatLayers.get(j);
			flatlayerinfo.setMinY(i);
			i += flatlayerinfo.getLayerCount();
		}

	}

	public String toString() {
		StringBuilder stringbuilder = new StringBuilder();
		stringbuilder.append(3);
		stringbuilder.append(";");

		for (int i = 0; i < this.flatLayers.size(); ++i) {
			if (i > 0) {
				stringbuilder.append(",");
			}

			stringbuilder.append(((FlatLayerInfo) this.flatLayers.get(i)).toString());
		}

		stringbuilder.append(";");
		stringbuilder.append(this.biomeToUse);
		if (!this.worldFeatures.isEmpty()) {
			stringbuilder.append(";");
			int k = 0;

			for (Entry entry : this.worldFeatures.entrySet()) {
				if (k++ > 0) {
					stringbuilder.append(",");
				}

				stringbuilder.append(((String) entry.getKey()).toLowerCase());
				Map map = (Map) entry.getValue();
				if (!map.isEmpty()) {
					stringbuilder.append("(");
					int j = 0;

					for (Entry entry1 : (Set<Entry>) map.entrySet()) {
						if (j++ > 0) {
							stringbuilder.append(" ");
						}

						stringbuilder.append((String) entry1.getKey());
						stringbuilder.append("=");
						stringbuilder.append((String) entry1.getValue());
					}

					stringbuilder.append(")");
				}
			}
		} else {
			stringbuilder.append(";");
		}

		return stringbuilder.toString();
	}

	private static FlatLayerInfo func_180715_a(int parInt1, String parString1, int parInt2) {
		String[] astring = parInt1 >= 3 ? parString1.split("\\*", 2) : parString1.split("x", 2);
		int i = 1;
		int j = 0;
		if (astring.length == 2) {
			try {
				i = Integer.parseInt(astring[0]);
				if (parInt2 + i >= 256) {
					i = 256 - parInt2;
				}

				if (i < 0) {
					i = 0;
				}
			} catch (Throwable var8) {
				return null;
			}
		}

		Block block = null;

		try {
			String s = astring[astring.length - 1];
			if (parInt1 < 3) {
				astring = s.split(":", 2);
				if (astring.length > 1) {
					j = Integer.parseInt(astring[1]);
				}

				block = Block.getBlockById(Integer.parseInt(astring[0]));
			} else {
				astring = s.split(":", 3);
				block = astring.length > 1 ? Block.getBlockFromName(astring[0] + ":" + astring[1]) : null;
				if (block != null) {
					j = astring.length > 2 ? Integer.parseInt(astring[2]) : 0;
				} else {
					block = Block.getBlockFromName(astring[0]);
					if (block != null) {
						j = astring.length > 1 ? Integer.parseInt(astring[1]) : 0;
					}
				}

				if (block == null) {
					return null;
				}
			}

			if (block == Blocks.air) {
				j = 0;
			}

			if (j < 0 || j > 15) {
				j = 0;
			}
		} catch (Throwable var9) {
			return null;
		}

		FlatLayerInfo flatlayerinfo = new FlatLayerInfo(parInt1, i, block, j);
		flatlayerinfo.setMinY(parInt2);
		return flatlayerinfo;
	}

	private static List<FlatLayerInfo> func_180716_a(int parInt1, String parString1) {
		if (parString1 != null && parString1.length() >= 1) {
			ArrayList arraylist = Lists.newArrayList();
			String[] astring = parString1.split(",");
			int i = 0;

			for (int j = 0; j < astring.length; ++j) {
				FlatLayerInfo flatlayerinfo = func_180715_a(parInt1, astring[j], i);
				if (flatlayerinfo == null) {
					return null;
				}

				arraylist.add(flatlayerinfo);
				i += flatlayerinfo.getLayerCount();
			}

			return arraylist;
		} else {
			return null;
		}
	}

	public static FlatGeneratorInfo createFlatGeneratorFromString(String parString1) {
		if (parString1 == null) {
			return getDefaultFlatGenerator();
		} else {
			String[] astring = parString1.split(";", -1);
			int i = astring.length == 1 ? 0 : MathHelper.parseIntWithDefault(astring[0], 0);
			if (i >= 0 && i <= 3) {
				FlatGeneratorInfo flatgeneratorinfo = new FlatGeneratorInfo();
				int j = astring.length == 1 ? 0 : 1;
				List list = func_180716_a(i, astring[j++]);
				if (list != null && !list.isEmpty()) {
					flatgeneratorinfo.getFlatLayers().addAll(list);
					flatgeneratorinfo.func_82645_d();
					int k = BiomeGenBase.plains.biomeID;
					if (i > 0 && astring.length > j) {
						k = MathHelper.parseIntWithDefault(astring[j++], k);
					}

					flatgeneratorinfo.setBiome(k);
					if (i > 0 && astring.length > j) {
						String[] astring1 = astring[j++].toLowerCase().split(",");

						for (int m = 0; m < astring1.length; ++m) {
							String[] astring2 = astring1[m].split("\\(", 2);
							HashMap hashmap = Maps.newHashMap();
							if (astring2[0].length() > 0) {
								flatgeneratorinfo.getWorldFeatures().put(astring2[0], hashmap);
								if (astring2.length > 1 && astring2[1].endsWith(")") && astring2[1].length() > 1) {
									String[] astring3 = astring2[1].substring(0, astring2[1].length() - 1).split(" ");

									for (int l = 0; l < astring3.length; ++l) {
										String[] astring4 = astring3[l].split("=", 2);
										if (astring4.length == 2) {
											hashmap.put(astring4[0], astring4[1]);
										}
									}
								}
							}
						}
					} else {
						flatgeneratorinfo.getWorldFeatures().put("village", Maps.newHashMap());
					}

					return flatgeneratorinfo;
				} else {
					return getDefaultFlatGenerator();
				}
			} else {
				return getDefaultFlatGenerator();
			}
		}
	}

	public static FlatGeneratorInfo getDefaultFlatGenerator() {
		FlatGeneratorInfo flatgeneratorinfo = new FlatGeneratorInfo();
		flatgeneratorinfo.setBiome(BiomeGenBase.plains.biomeID);
		flatgeneratorinfo.getFlatLayers().add(new FlatLayerInfo(1, Blocks.bedrock));
		flatgeneratorinfo.getFlatLayers().add(new FlatLayerInfo(2, Blocks.dirt));
		flatgeneratorinfo.getFlatLayers().add(new FlatLayerInfo(1, Blocks.grass));
		flatgeneratorinfo.func_82645_d();
		flatgeneratorinfo.getWorldFeatures().put("village", Maps.newHashMap());
		return flatgeneratorinfo;
	}
}