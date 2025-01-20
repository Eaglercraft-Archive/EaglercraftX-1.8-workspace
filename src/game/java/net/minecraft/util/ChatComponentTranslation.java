package net.minecraft.util;

import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

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
public class ChatComponentTranslation extends ChatComponentStyle {
	private final String key;
	private final Object[] formatArgs;
	private final Object syncLock = new Object();
	private long lastTranslationUpdateTimeInMilliseconds = -1L;
	List<IChatComponent> children = Lists.newArrayList();
	public static final Pattern stringVariablePattern = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");

	public ChatComponentTranslation(String translationKey, Object... args) {
		this.key = translationKey;
		this.formatArgs = args;

		for (int i = 0; i < args.length; ++i) {
			Object object = args[i];
			if (object instanceof IChatComponent) {
				((IChatComponent) object).getChatStyle().setParentStyle(this.getChatStyle());
			}
		}

	}

	/**+
	 * ensures that our children are initialized from the most
	 * recent string translation mapping.
	 */
	synchronized void ensureInitialized() {
		synchronized (this.syncLock) {
			long i = StatCollector.getLastTranslationUpdateTimeInMilliseconds();
			if (i == this.lastTranslationUpdateTimeInMilliseconds) {
				return;
			}

			this.lastTranslationUpdateTimeInMilliseconds = i;
			this.children.clear();
		}

		try {
			this.initializeFromFormat(StatCollector.translateToLocal(this.key));
		} catch (ChatComponentTranslationFormatException chatcomponenttranslationformatexception) {
			this.children.clear();

			try {
				this.initializeFromFormat(StatCollector.translateToFallback(this.key));
			} catch (ChatComponentTranslationFormatException var5) {
				throw chatcomponenttranslationformatexception;
			}
		}

	}

	/**+
	 * initializes our children from a format string, using the
	 * format args to fill in the placeholder variables.
	 */
	protected void initializeFromFormat(String format) {
		boolean flag = false;
		Matcher matcher = stringVariablePattern.matcher(format);
		int i = 0;
		int j = 0;

		try {
			int l;
			for (; matcher.find(j); j = l) {
				int k = matcher.start();
				l = matcher.end();
				if (k > j) {
					ChatComponentText chatcomponenttext = new ChatComponentText(
							HString.format(format.substring(j, k), new Object[0]));
					chatcomponenttext.getChatStyle().setParentStyle(this.getChatStyle());
					this.children.add(chatcomponenttext);
				}

				String s2 = matcher.group(2);
				String s = format.substring(k, l);
				if ("%".equals(s2) && "%%".equals(s)) {
					ChatComponentText chatcomponenttext2 = new ChatComponentText("%");
					chatcomponenttext2.getChatStyle().setParentStyle(this.getChatStyle());
					this.children.add(chatcomponenttext2);
				} else {
					if (!"s".equals(s2)) {
						throw new ChatComponentTranslationFormatException(this, "Unsupported format: \'" + s + "\'");
					}

					String s1 = matcher.group(1);
					int i1 = s1 != null ? Integer.parseInt(s1) - 1 : i++;
					if (i1 < this.formatArgs.length) {
						this.children.add(this.getFormatArgumentAsComponent(i1));
					}
				}
			}

			if (j < format.length()) {
				ChatComponentText chatcomponenttext1 = new ChatComponentText(
						HString.format(format.substring(j), new Object[0]));
				chatcomponenttext1.getChatStyle().setParentStyle(this.getChatStyle());
				this.children.add(chatcomponenttext1);
			}

		} catch (IllegalFormatException illegalformatexception) {
			throw new ChatComponentTranslationFormatException(this, illegalformatexception);
		}
	}

	private IChatComponent getFormatArgumentAsComponent(int index) {
		if (index >= this.formatArgs.length) {
			throw new ChatComponentTranslationFormatException(this, index);
		} else {
			Object object = this.formatArgs[index];
			Object object1;
			if (object instanceof IChatComponent) {
				object1 = (IChatComponent) object;
			} else {
				object1 = new ChatComponentText(object == null ? "null" : object.toString());
				((IChatComponent) object1).getChatStyle().setParentStyle(this.getChatStyle());
			}

			return (IChatComponent) object1;
		}
	}

	public IChatComponent setChatStyle(ChatStyle chatstyle) {
		super.setChatStyle(chatstyle);

		for (int i = 0; i < this.formatArgs.length; ++i) {
			Object object = this.formatArgs[i];
			if (object instanceof IChatComponent) {
				((IChatComponent) object).getChatStyle().setParentStyle(this.getChatStyle());
			}
		}

		if (this.lastTranslationUpdateTimeInMilliseconds > -1L) {
			for (int i = 0, l = this.children.size(); i < l; ++i) {
				this.children.get(i).getChatStyle().setParentStyle(chatstyle);
			}
		}

		return this;
	}

	public Iterator<IChatComponent> iterator() {
		this.ensureInitialized();
		return Iterators.concat(createDeepCopyIterator(this.children), createDeepCopyIterator(this.siblings));
	}

	/**+
	 * Gets the text of this component, without any special
	 * formatting codes added, for chat. TODO: why is this two
	 * different methods?
	 */
	public String getUnformattedTextForChat() {
		this.ensureInitialized();
		StringBuilder stringbuilder = new StringBuilder();

		for (int i = 0, l = this.children.size(); i < l; ++i) {
			stringbuilder.append(this.children.get(i).getUnformattedTextForChat());
		}

		return stringbuilder.toString();
	}

	/**+
	 * Creates a copy of this component. Almost a deep copy, except
	 * the style is shallow-copied.
	 */
	public ChatComponentTranslation createCopy() {
		Object[] aobject = new Object[this.formatArgs.length];

		for (int i = 0; i < this.formatArgs.length; ++i) {
			if (this.formatArgs[i] instanceof IChatComponent) {
				aobject[i] = ((IChatComponent) this.formatArgs[i]).createCopy();
			} else {
				aobject[i] = this.formatArgs[i];
			}
		}

		ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(this.key, aobject);
		chatcomponenttranslation.setChatStyle(this.getChatStyle().createShallowCopy());

		List<IChatComponent> lst = this.getSiblings();
		for (int i = 0, l = lst.size(); i < l; ++i) {
			chatcomponenttranslation.appendSibling(lst.get(i).createCopy());
		}

		return chatcomponenttranslation;
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (!(object instanceof ChatComponentTranslation)) {
			return false;
		} else {
			ChatComponentTranslation chatcomponenttranslation = (ChatComponentTranslation) object;
			return Arrays.equals(this.formatArgs, chatcomponenttranslation.formatArgs)
					&& this.key.equals(chatcomponenttranslation.key) && super.equals(object);
		}
	}

	public int hashCode() {
		int i = super.hashCode();
		i = 31 * i + this.key.hashCode();
		i = 31 * i + Arrays.hashCode(this.formatArgs);
		return i;
	}

	public String toString() {
		return "TranslatableComponent{key=\'" + this.key + '\'' + ", args=" + Arrays.toString(this.formatArgs)
				+ ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
	}

	public String getKey() {
		return this.key;
	}

	public Object[] getFormatArgs() {
		return this.formatArgs;
	}
}