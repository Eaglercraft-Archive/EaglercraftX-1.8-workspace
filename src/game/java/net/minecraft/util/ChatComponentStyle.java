package net.minecraft.util;

import java.util.Iterator;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

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
public abstract class ChatComponentStyle implements IChatComponent {
	/**+
	 * The later siblings of this component. If this component turns
	 * the text bold, that will apply to all the siblings until a
	 * later sibling turns the text something else.
	 */
	protected List<IChatComponent> siblings = Lists.newArrayList();
	private ChatStyle style;

	/**+
	 * Appends the given component to the end of this one.
	 */
	public IChatComponent appendSibling(IChatComponent component) {
		component.getChatStyle().setParentStyle(this.getChatStyle());
		this.siblings.add(component);
		return this;
	}

	/**+
	 * Gets the sibling components of this one.
	 */
	public List<IChatComponent> getSiblings() {
		return this.siblings;
	}

	/**+
	 * Appends the given text to the end of this component.
	 */
	public IChatComponent appendText(String text) {
		return this.appendSibling(new ChatComponentText(text));
	}

	public IChatComponent setChatStyle(ChatStyle style) {
		this.style = style;

		for (int i = 0, l = this.siblings.size(); i < l; ++i) {
			this.siblings.get(i).getChatStyle().setParentStyle(this.getChatStyle());
		}

		return this;
	}

	public ChatStyle getChatStyle() {
		if (this.style == null) {
			this.style = new ChatStyle();

			for (int i = 0, l = this.siblings.size(); i < l; ++i) {
				this.siblings.get(i).getChatStyle().setParentStyle(this.style);
			}
		}

		return this.style;
	}

	public ChatStyle getChatStyleIfPresent() {
		return this.style;
	}

	public Iterator<IChatComponent> iterator() {
		return Iterators.concat(Iterators.forArray(new ChatComponentStyle[] { this }),
				createDeepCopyIterator(this.siblings));
	}

	/**+
	 * Get the text of this component, <em>and all child
	 * components</em>, with all special formatting codes removed.
	 */
	public final String getUnformattedText() {
		StringBuilder stringbuilder = new StringBuilder();

		for (IChatComponent ichatcomponent : this) {
			stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
		}

		return stringbuilder.toString();
	}

	/**+
	 * Gets the text of this component, with formatting codes added
	 * for rendering.
	 */
	public final String getFormattedText() {
		StringBuilder stringbuilder = new StringBuilder();

		for (IChatComponent ichatcomponent : this) {
			stringbuilder.append(ichatcomponent.getChatStyle().getFormattingCode());
			stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
			stringbuilder.append(EnumChatFormatting.RESET);
		}

		return stringbuilder.toString();
	}

	/**+
	 * Creates an iterator that iterates over the given components,
	 * returning deep copies of each component in turn so that the
	 * properties of the returned objects will remain externally
	 * consistent after being returned.
	 */
	public static Iterator<IChatComponent> createDeepCopyIterator(Iterable<IChatComponent> components) {
		Iterator iterator = Iterators.concat(
				Iterators.transform(components.iterator(), new Function<IChatComponent, Iterator<IChatComponent>>() {
					public Iterator<IChatComponent> apply(IChatComponent ichatcomponent) {
						return ichatcomponent.iterator();
					}
				}));
		iterator = Iterators.transform(iterator, new Function<IChatComponent, IChatComponent>() {
			public IChatComponent apply(IChatComponent ichatcomponent) {
				IChatComponent ichatcomponent1 = ichatcomponent.createCopy();
				ichatcomponent1.setChatStyle(ichatcomponent1.getChatStyle().createDeepCopy());
				return ichatcomponent1;
			}
		});
		return iterator;
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (!(object instanceof ChatComponentStyle)) {
			return false;
		} else {
			ChatComponentStyle chatcomponentstyle = (ChatComponentStyle) object;
			return this.siblings.equals(chatcomponentstyle.siblings)
					&& this.getChatStyle().equals(chatcomponentstyle.getChatStyle());
		}
	}

	public int hashCode() {
		return 31 * this.style.hashCode() + this.siblings.hashCode();
	}

	public String toString() {
		return "BaseComponent{style=" + this.style + ", siblings=" + this.siblings + '}';
	}
}