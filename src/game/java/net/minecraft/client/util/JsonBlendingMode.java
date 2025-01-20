package net.minecraft.client.util;

import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

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
public class JsonBlendingMode {
	private static JsonBlendingMode field_148118_a = null;
	private final int field_148116_b;
	private final int field_148117_c;
	private final int field_148114_d;
	private final int field_148115_e;
	private final int field_148112_f;
	private final boolean field_148113_g;
	private final boolean field_148119_h;

	private JsonBlendingMode(boolean parFlag, boolean parFlag2, int parInt1, int parInt2, int parInt3, int parInt4,
			int parInt5) {
		this.field_148113_g = parFlag;
		this.field_148116_b = parInt1;
		this.field_148114_d = parInt2;
		this.field_148117_c = parInt3;
		this.field_148115_e = parInt4;
		this.field_148119_h = parFlag2;
		this.field_148112_f = parInt5;
	}

	public JsonBlendingMode() {
		this(false, true, 1, 0, 1, 0, '\u8006');
	}

	public JsonBlendingMode(int parInt1, int parInt2, int parInt3) {
		this(false, false, parInt1, parInt2, parInt1, parInt2, parInt3);
	}

	public JsonBlendingMode(int parInt1, int parInt2, int parInt3, int parInt4, int parInt5) {
		this(true, false, parInt1, parInt2, parInt3, parInt4, parInt5);
	}

	public void func_148109_a() {
		if (!this.equals(field_148118_a)) {
			if (field_148118_a == null || this.field_148119_h != field_148118_a.func_148111_b()) {
				field_148118_a = this;
				if (this.field_148119_h) {
					GlStateManager.disableBlend();
					return;
				}

				GlStateManager.enableBlend();
			}

			EaglercraftGPU.glBlendEquation(this.field_148112_f);
			if (this.field_148113_g) {
				GlStateManager.tryBlendFuncSeparate(this.field_148116_b, this.field_148114_d, this.field_148117_c,
						this.field_148115_e);
			} else {
				GlStateManager.blendFunc(this.field_148116_b, this.field_148114_d);
			}

		}
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (!(object instanceof JsonBlendingMode)) {
			return false;
		} else {
			JsonBlendingMode jsonblendingmode = (JsonBlendingMode) object;
			return this.field_148112_f != jsonblendingmode.field_148112_f ? false
					: (this.field_148115_e != jsonblendingmode.field_148115_e ? false
							: (this.field_148114_d != jsonblendingmode.field_148114_d ? false
									: (this.field_148119_h != jsonblendingmode.field_148119_h ? false
											: (this.field_148113_g != jsonblendingmode.field_148113_g ? false
													: (this.field_148117_c != jsonblendingmode.field_148117_c ? false
															: this.field_148116_b == jsonblendingmode.field_148116_b)))));
		}
	}

	public int hashCode() {
		int i = this.field_148116_b;
		i = 31 * i + this.field_148117_c;
		i = 31 * i + this.field_148114_d;
		i = 31 * i + this.field_148115_e;
		i = 31 * i + this.field_148112_f;
		i = 31 * i + (this.field_148113_g ? 1 : 0);
		i = 31 * i + (this.field_148119_h ? 1 : 0);
		return i;
	}

	public boolean func_148111_b() {
		return this.field_148119_h;
	}

	public static JsonBlendingMode func_148110_a(JSONObject parJsonObject) {
		if (parJsonObject == null) {
			return new JsonBlendingMode();
		} else {
			int i = '\u8006';
			int j = 1;
			int k = 0;
			int l = 1;
			int i1 = 0;
			boolean flag = true;
			boolean flag1 = false;
			if (parJsonObject.get("func") instanceof String) {
				i = func_148108_a(parJsonObject.getString("func"));
				if (i != '\u8006') {
					flag = false;
				}
			}

			if (parJsonObject.get("srcrgb") instanceof String) {
				j = func_148107_b(parJsonObject.getString("srcrgb"));
				if (j != 1) {
					flag = false;
				}
			}

			if (parJsonObject.get("dstrgb") instanceof String) {
				k = func_148107_b(parJsonObject.getString("dstrgb"));
				if (k != 0) {
					flag = false;
				}
			}

			if (parJsonObject.get("srcalpha") instanceof String) {
				l = func_148107_b(parJsonObject.getString("srcalpha"));
				if (l != 1) {
					flag = false;
				}

				flag1 = true;
			}

			if (parJsonObject.get("dstalpha") instanceof String) {
				i1 = func_148107_b(parJsonObject.getString("dstalpha"));
				if (i1 != 0) {
					flag = false;
				}

				flag1 = true;
			}

			return flag ? new JsonBlendingMode()
					: (flag1 ? new JsonBlendingMode(j, k, l, i1, i) : new JsonBlendingMode(j, k, i));
		}
	}

	private static int func_148108_a(String parString1) {
		String s = parString1.trim().toLowerCase();
		return s.equals("add") ? '\u8006'
				: (s.equals("subtract") ? '\u800a'
						: (s.equals("reversesubtract") ? '\u800b'
								: (s.equals("reverse_subtract") ? '\u800b'
										: (s.equals("min") ? '\u8007' : (s.equals("max") ? '\u8008' : '\u8006')))));
	}

	private static int func_148107_b(String parString1) {
		String s = parString1.trim().toLowerCase();
		s = s.replaceAll("_", "");
		s = s.replaceAll("one", "1");
		s = s.replaceAll("zero", "0");
		s = s.replaceAll("minus", "-");
		return s.equals("0") ? 0
				: (s.equals("1") ? 1
						: (s.equals("srccolor") ? 768
								: (s.equals("1-srccolor") ? 769
										: (s.equals("dstcolor") ? 774
												: (s.equals("1-dstcolor") ? 775
														: (s.equals("srcalpha") ? 770
																: (s.equals("1-srcalpha") ? 771
																		: (s.equals("dstalpha") ? 772
																				: (s.equals("1-dstalpha") ? 773
																						: -1)))))))));
	}
}