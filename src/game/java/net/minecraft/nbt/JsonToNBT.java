package net.minecraft.nbt;

import java.util.Stack;
import java.util.regex.Pattern;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

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
public class JsonToNBT {
	private static final Logger logger = LogManager.getLogger();
	private static final Pattern field_179273_b = Pattern.compile("\\[[-+\\d|,\\s]+\\]");

	public static NBTTagCompound getTagFromJson(String jsonString) throws NBTException {
		jsonString = jsonString.trim();
		if (!jsonString.startsWith("{")) {
			throw new NBTException("Invalid tag encountered, expected \'{\' as first char.");
		} else if (func_150310_b(jsonString) != 1) {
			throw new NBTException("Encountered multiple top tags, only one expected");
		} else {
			return (NBTTagCompound) func_150316_a("tag", jsonString).parse();
		}
	}

	static int func_150310_b(String parString1) throws NBTException {
		int i = 0;
		boolean flag = false;
		Stack stack = new Stack();

		for (int j = 0; j < parString1.length(); ++j) {
			char c0 = parString1.charAt(j);
			if (c0 == 34) {
				if (func_179271_b(parString1, j)) {
					if (!flag) {
						throw new NBTException("Illegal use of \\\": " + parString1);
					}
				} else {
					flag = !flag;
				}
			} else if (!flag) {
				if (c0 != 123 && c0 != 91) {
					if (c0 == 125 && (stack.isEmpty() || ((Character) stack.pop()).charValue() != 123)) {
						throw new NBTException("Unbalanced curly brackets {}: " + parString1);
					}

					if (c0 == 93 && (stack.isEmpty() || ((Character) stack.pop()).charValue() != 91)) {
						throw new NBTException("Unbalanced square brackets []: " + parString1);
					}
				} else {
					if (stack.isEmpty()) {
						++i;
					}

					stack.push(Character.valueOf(c0));
				}
			}
		}

		if (flag) {
			throw new NBTException("Unbalanced quotation: " + parString1);
		} else if (!stack.isEmpty()) {
			throw new NBTException("Unbalanced brackets: " + parString1);
		} else {
			if (i == 0 && !parString1.isEmpty()) {
				i = 1;
			}

			return i;
		}
	}

	static JsonToNBT.Any func_179272_a(String... parArrayOfString) throws NBTException {
		return func_150316_a(parArrayOfString[0], parArrayOfString[1]);
	}

	static JsonToNBT.Any func_150316_a(String parString1, String parString2) throws NBTException {
		parString2 = parString2.trim();
		if (parString2.startsWith("{")) {
			parString2 = parString2.substring(1, parString2.length() - 1);

			JsonToNBT.Compound jsontonbt$compound;
			String s1;
			for (jsontonbt$compound = new JsonToNBT.Compound(parString1); parString2
					.length() > 0; parString2 = parString2.substring(s1.length() + 1)) {
				s1 = func_150314_a(parString2, true);
				if (s1.length() > 0) {
					boolean flag1 = false;
					jsontonbt$compound.field_150491_b.add(func_179270_a(s1, flag1));
				}

				if (parString2.length() < s1.length() + 1) {
					break;
				}

				char c1 = parString2.charAt(s1.length());
				if (c1 != 44 && c1 != 123 && c1 != 125 && c1 != 91 && c1 != 93) {
					throw new NBTException("Unexpected token \'" + c1 + "\' at: " + parString2.substring(s1.length()));
				}
			}

			return jsontonbt$compound;
		} else if (parString2.startsWith("[") && !field_179273_b.matcher(parString2).matches()) {
			parString2 = parString2.substring(1, parString2.length() - 1);

			JsonToNBT.List jsontonbt$list;
			String s;
			for (jsontonbt$list = new JsonToNBT.List(parString1); parString2.length() > 0; parString2 = parString2
					.substring(s.length() + 1)) {
				s = func_150314_a(parString2, false);
				if (s.length() > 0) {
					boolean flag = true;
					jsontonbt$list.field_150492_b.add(func_179270_a(s, flag));
				}

				if (parString2.length() < s.length() + 1) {
					break;
				}

				char c0 = parString2.charAt(s.length());
				if (c0 != 44 && c0 != 123 && c0 != 125 && c0 != 91 && c0 != 93) {
					throw new NBTException("Unexpected token \'" + c0 + "\' at: " + parString2.substring(s.length()));
				}
			}

			return jsontonbt$list;
		} else {
			return new JsonToNBT.Primitive(parString1, parString2);
		}
	}

	private static JsonToNBT.Any func_179270_a(String parString1, boolean parFlag) throws NBTException {
		String s = func_150313_b(parString1, parFlag);
		String s1 = func_150311_c(parString1, parFlag);
		return func_179272_a(new String[] { s, s1 });
	}

	private static String func_150314_a(String parString1, boolean parFlag) throws NBTException {
		int i = func_150312_a(parString1, ':');
		int j = func_150312_a(parString1, ',');
		if (parFlag) {
			if (i == -1) {
				throw new NBTException("Unable to locate name/value separator for string: " + parString1);
			}

			if (j != -1 && j < i) {
				throw new NBTException("Name error at: " + parString1);
			}
		} else if (i == -1 || i > j) {
			i = -1;
		}

		return func_179269_a(parString1, i);
	}

	private static String func_179269_a(String parString1, int parInt1) throws NBTException {
		Stack stack = new Stack();
		int i = parInt1 + 1;
		boolean flag = false;
		boolean flag1 = false;
		boolean flag2 = false;

		for (int j = 0; i < parString1.length(); ++i) {
			char c0 = parString1.charAt(i);
			if (c0 == 34) {
				if (func_179271_b(parString1, i)) {
					if (!flag) {
						throw new NBTException("Illegal use of \\\": " + parString1);
					}
				} else {
					flag = !flag;
					if (flag && !flag2) {
						flag1 = true;
					}

					if (!flag) {
						j = i;
					}
				}
			} else if (!flag) {
				if (c0 != 123 && c0 != 91) {
					if (c0 == 125 && (stack.isEmpty() || ((Character) stack.pop()).charValue() != 123)) {
						throw new NBTException("Unbalanced curly brackets {}: " + parString1);
					}

					if (c0 == 93 && (stack.isEmpty() || ((Character) stack.pop()).charValue() != 91)) {
						throw new NBTException("Unbalanced square brackets []: " + parString1);
					}

					if (c0 == 44 && stack.isEmpty()) {
						return parString1.substring(0, i);
					}
				} else {
					stack.push(Character.valueOf(c0));
				}
			}

			if (!Character.isWhitespace(c0)) {
				if (!flag && flag1 && j != i) {
					return parString1.substring(0, j + 1);
				}

				flag2 = true;
			}
		}

		return parString1.substring(0, i);
	}

	private static String func_150313_b(String parString1, boolean parFlag) throws NBTException {
		if (parFlag) {
			parString1 = parString1.trim();
			if (parString1.startsWith("{") || parString1.startsWith("[")) {
				return "";
			}
		}

		int i = func_150312_a(parString1, ':');
		if (i == -1) {
			if (parFlag) {
				return "";
			} else {
				throw new NBTException("Unable to locate name/value separator for string: " + parString1);
			}
		} else {
			return parString1.substring(0, i).trim();
		}
	}

	private static String func_150311_c(String parString1, boolean parFlag) throws NBTException {
		if (parFlag) {
			parString1 = parString1.trim();
			if (parString1.startsWith("{") || parString1.startsWith("[")) {
				return parString1;
			}
		}

		int i = func_150312_a(parString1, ':');
		if (i == -1) {
			if (parFlag) {
				return parString1;
			} else {
				throw new NBTException("Unable to locate name/value separator for string: " + parString1);
			}
		} else {
			return parString1.substring(i + 1).trim();
		}
	}

	private static int func_150312_a(String parString1, char parChar1) {
		int i = 0;

		for (boolean flag = true; i < parString1.length(); ++i) {
			char c0 = parString1.charAt(i);
			if (c0 == 34) {
				if (!func_179271_b(parString1, i)) {
					flag = !flag;
				}
			} else if (flag) {
				if (c0 == parChar1) {
					return i;
				}

				if (c0 == 123 || c0 == 91) {
					return -1;
				}
			}
		}

		return -1;
	}

	private static boolean func_179271_b(String parString1, int parInt1) {
		return parInt1 > 0 && parString1.charAt(parInt1 - 1) == 92 && !func_179271_b(parString1, parInt1 - 1);
	}

	abstract static class Any {
		protected String json;

		public abstract NBTBase parse() throws NBTException;
	}

	static class Compound extends JsonToNBT.Any {
		protected java.util.List<JsonToNBT.Any> field_150491_b = Lists.newArrayList();

		public Compound(String parString1) {
			this.json = parString1;
		}

		public NBTBase parse() throws NBTException {
			NBTTagCompound nbttagcompound = new NBTTagCompound();

			for (int i = 0, l = this.field_150491_b.size(); i < l; ++i) {
				JsonToNBT.Any jsontonbt$any = this.field_150491_b.get(i);
				nbttagcompound.setTag(jsontonbt$any.json, jsontonbt$any.parse());
			}

			return nbttagcompound;
		}
	}

	static class List extends JsonToNBT.Any {
		protected java.util.List<JsonToNBT.Any> field_150492_b = Lists.newArrayList();

		public List(String json) {
			this.json = json;
		}

		public NBTBase parse() throws NBTException {
			NBTTagList nbttaglist = new NBTTagList();

			for (int i = 0, l = this.field_150492_b.size(); i < l; ++i) {
				nbttaglist.appendTag(this.field_150492_b.get(i).parse());
			}

			return nbttaglist;
		}
	}

	static class Primitive extends JsonToNBT.Any {
		private static final Pattern DOUBLE = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+[d|D]");
		private static final Pattern FLOAT = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+[f|F]");
		private static final Pattern BYTE = Pattern.compile("[-+]?[0-9]+[b|B]");
		private static final Pattern LONG = Pattern.compile("[-+]?[0-9]+[l|L]");
		private static final Pattern SHORT = Pattern.compile("[-+]?[0-9]+[s|S]");
		private static final Pattern INTEGER = Pattern.compile("[-+]?[0-9]+");
		private static final Pattern DOUBLE_UNTYPED = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+");
		private static final Splitter SPLITTER = Splitter.on(',').omitEmptyStrings();
		protected String jsonValue;

		public Primitive(String parString1, String parString2) {
			this.json = parString1;
			this.jsonValue = parString2;
		}

		public NBTBase parse() throws NBTException {
			try {
				if (DOUBLE.matcher(this.jsonValue).matches()) {
					return new NBTTagDouble(
							Double.parseDouble(this.jsonValue.substring(0, this.jsonValue.length() - 1)));
				}

				if (FLOAT.matcher(this.jsonValue).matches()) {
					return new NBTTagFloat(Float.parseFloat(this.jsonValue.substring(0, this.jsonValue.length() - 1)));
				}

				if (BYTE.matcher(this.jsonValue).matches()) {
					return new NBTTagByte(Byte.parseByte(this.jsonValue.substring(0, this.jsonValue.length() - 1)));
				}

				if (LONG.matcher(this.jsonValue).matches()) {
					return new NBTTagLong(Long.parseLong(this.jsonValue.substring(0, this.jsonValue.length() - 1)));
				}

				if (SHORT.matcher(this.jsonValue).matches()) {
					return new NBTTagShort(Short.parseShort(this.jsonValue.substring(0, this.jsonValue.length() - 1)));
				}

				if (INTEGER.matcher(this.jsonValue).matches()) {
					return new NBTTagInt(Integer.parseInt(this.jsonValue));
				}

				if (DOUBLE_UNTYPED.matcher(this.jsonValue).matches()) {
					return new NBTTagDouble(Double.parseDouble(this.jsonValue));
				}

				if (this.jsonValue.equalsIgnoreCase("true") || this.jsonValue.equalsIgnoreCase("false")) {
					return new NBTTagByte((byte) (Boolean.parseBoolean(this.jsonValue) ? 1 : 0));
				}
			} catch (NumberFormatException var6) {
				this.jsonValue = this.jsonValue.replaceAll("\\\\\"", "\"");
				return new NBTTagString(this.jsonValue);
			}

			if (this.jsonValue.startsWith("[") && this.jsonValue.endsWith("]")) {
				String s = this.jsonValue.substring(1, this.jsonValue.length() - 1);
				String[] astring = (String[]) Iterables.toArray(SPLITTER.split(s), String.class);

				try {
					int[] aint = new int[astring.length];

					for (int j = 0; j < astring.length; ++j) {
						aint[j] = Integer.parseInt(astring[j].trim());
					}

					return new NBTTagIntArray(aint);
				} catch (NumberFormatException var5) {
					return new NBTTagString(this.jsonValue);
				}
			} else {
				if (this.jsonValue.startsWith("\"") && this.jsonValue.endsWith("\"")) {
					this.jsonValue = this.jsonValue.substring(1, this.jsonValue.length() - 1);
				}

				this.jsonValue = this.jsonValue.replaceAll("\\\\\"", "\"");
				StringBuilder stringbuilder = new StringBuilder();

				for (int i = 0; i < this.jsonValue.length(); ++i) {
					if (i < this.jsonValue.length() - 1 && this.jsonValue.charAt(i) == 92
							&& this.jsonValue.charAt(i + 1) == 92) {
						stringbuilder.append('\\');
						++i;
					} else {
						stringbuilder.append(this.jsonValue.charAt(i));
					}
				}

				return new NBTTagString(stringbuilder.toString());
			}
		}
	}
}