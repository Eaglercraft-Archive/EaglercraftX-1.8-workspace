package net.minecraft.profiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class Profiler {
	private static final Logger logger = LogManager.getLogger();
	/**+
	 * List of parent sections
	 */
	private final List<String> sectionList = Lists.newArrayList();
	/**+
	 * List of timestamps (System.nanoTime)
	 */
	private final List<Long> timestampList = Lists.newArrayList();
	public boolean profilingEnabled;
	/**+
	 * Current profiling section
	 */
	private String profilingSection = "";
	private final Map<String, Long> profilingMap = Maps.newHashMap();

	/**+
	 * Clear profiling.
	 */
	public void clearProfiling() {
		this.profilingMap.clear();
		this.profilingSection = "";
		this.sectionList.clear();
	}

	/**+
	 * Start section
	 */
	public void startSection(String name) {
		if (this.profilingEnabled) {
			if (this.profilingSection.length() > 0) {
				this.profilingSection = this.profilingSection + ".";
			}

			this.profilingSection = this.profilingSection + name;
			this.sectionList.add(this.profilingSection);
			this.timestampList.add(Long.valueOf(System.nanoTime()));
		}
	}

	/**+
	 * End section
	 */
	public void endSection() {
		if (this.profilingEnabled) {
			long i = System.nanoTime();
			long j = ((Long) this.timestampList.remove(this.timestampList.size() - 1)).longValue();
			this.sectionList.remove(this.sectionList.size() - 1);
			long k = i - j;
			if (this.profilingMap.containsKey(this.profilingSection)) {
				this.profilingMap.put(this.profilingSection,
						Long.valueOf(((Long) this.profilingMap.get(this.profilingSection)).longValue() + k));
			} else {
				this.profilingMap.put(this.profilingSection, Long.valueOf(k));
			}

			if (k > 100000000L) {
				logger.warn("Something\'s taking too long! \'" + this.profilingSection + "\' took aprox "
						+ (double) k / 1000000.0D + " ms");
			}

			this.profilingSection = !this.sectionList.isEmpty()
					? (String) this.sectionList.get(this.sectionList.size() - 1)
					: "";
		}
	}

	/**+
	 * Get profiling data
	 */
	public List<Profiler.Result> getProfilingData(String parString1) {
		if (!this.profilingEnabled) {
			return null;
		} else {
			long i = this.profilingMap.containsKey("root") ? ((Long) this.profilingMap.get("root")).longValue() : 0L;
			long j = this.profilingMap.containsKey(parString1) ? ((Long) this.profilingMap.get(parString1)).longValue()
					: -1L;
			ArrayList arraylist = Lists.newArrayList();
			if (parString1.length() > 0) {
				parString1 = parString1 + ".";
			}

			long k = 0L;

			for (String s : this.profilingMap.keySet()) {
				if (s.length() > parString1.length() && s.startsWith(parString1)
						&& s.indexOf(".", parString1.length() + 1) < 0) {
					k += ((Long) this.profilingMap.get(s)).longValue();
				}
			}

			float f = (float) k;
			if (k < j) {
				k = j;
			}

			if (i < k) {
				i = k;
			}

			for (String s1 : this.profilingMap.keySet()) {
				if (s1.length() > parString1.length() && s1.startsWith(parString1)
						&& s1.indexOf(".", parString1.length() + 1) < 0) {
					long l = ((Long) this.profilingMap.get(s1)).longValue();
					double d0 = (double) l * 100.0D / (double) k;
					double d1 = (double) l * 100.0D / (double) i;
					String s2 = s1.substring(parString1.length());
					arraylist.add(new Profiler.Result(s2, d0, d1));
				}
			}

			for (String s3 : this.profilingMap.keySet()) {
				this.profilingMap.put(s3, Long.valueOf(((Long) this.profilingMap.get(s3)).longValue() * 999L / 1000L));
			}

			if ((float) k > f) {
				arraylist.add(new Profiler.Result("unspecified", (double) ((float) k - f) * 100.0D / (double) k,
						(double) ((float) k - f) * 100.0D / (double) i));
			}

			Collections.sort(arraylist);
			arraylist.add(0, new Profiler.Result(parString1, 100.0D, (double) k * 100.0D / (double) i));
			return arraylist;
		}
	}

	/**+
	 * End current section and start a new section
	 */
	public void endStartSection(String name) {
		this.endSection();
		this.startSection(name);
	}

	public String getNameOfLastSection() {
		return this.sectionList.size() == 0 ? "[UNKNOWN]" : (String) this.sectionList.get(this.sectionList.size() - 1);
	}

	public static final class Result implements Comparable<Profiler.Result> {
		public double field_76332_a;
		public double field_76330_b;
		public String field_76331_c;

		public Result(String parString1, double parDouble1, double parDouble2) {
			this.field_76331_c = parString1;
			this.field_76332_a = parDouble1;
			this.field_76330_b = parDouble2;
		}

		public int compareTo(Profiler.Result profiler$result) {
			return profiler$result.field_76332_a < this.field_76332_a ? -1
					: (profiler$result.field_76332_a > this.field_76332_a ? 1
							: profiler$result.field_76331_c.compareTo(this.field_76331_c));
		}

		public int func_76329_a() {
			return (this.field_76331_c.hashCode() & 11184810) + 4473924;
		}
	}
}