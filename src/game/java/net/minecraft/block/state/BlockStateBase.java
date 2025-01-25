package net.minecraft.block.state;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
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
public abstract class BlockStateBase implements IBlockState {
	private static final Joiner COMMA_JOINER = Joiner.on(',');
	private static final Function<Entry<IProperty, Comparable>, String> MAP_ENTRY_TO_STRING = new Function<Entry<IProperty, Comparable>, String>() {
		public String apply(Entry<IProperty, Comparable> entry) {
			if (entry == null) {
				return "<NULL>";
			} else {
				IProperty iproperty = (IProperty) entry.getKey();
				return iproperty.getName() + "=" + iproperty.getName((Comparable) entry.getValue());
			}
		}
	};

	public <T extends Comparable<T>> IBlockState cycleProperty(IProperty<T> property) {
		return this.withProperty(property,
				(T) cyclePropertyValue(property.getAllowedValues(), this.getValue(property)));
	}

	protected static <T> T cyclePropertyValue(Collection<T> values, T currentValue) {
		Iterator iterator = values.iterator();

		while (iterator.hasNext()) {
			if (iterator.next().equals(currentValue)) {
				if (iterator.hasNext()) {
					return (T) iterator.next();
				}

				return (T) values.iterator().next();
			}
		}

		return (T) iterator.next();
	}

	public String toString() {
		StringBuilder stringbuilder = new StringBuilder();
		stringbuilder.append(Block.blockRegistry.getNameForObject(this.getBlock()));
		if (!this.getProperties().isEmpty()) {
			stringbuilder.append("[");
			COMMA_JOINER.appendTo(stringbuilder,
					Iterables.transform(this.getProperties().entrySet(), MAP_ENTRY_TO_STRING));
			stringbuilder.append("]");
		}

		return stringbuilder.toString();
	}

	private int blockId = -1;
	private int blockStateId = -1;
	private int metadata = -1;
	private ResourceLocation blockLocation = null;

	public int getBlockId() {
		if (this.blockId < 0) {
			this.blockId = Block.getIdFromBlock(this.getBlock());
		}

		return this.blockId;
	}

	public int getBlockStateId() {
		if (this.blockStateId < 0) {
			this.blockStateId = Block.getStateId(this);
		}

		return this.blockStateId;
	}

	public int getMetadata() {
		if (this.metadata < 0) {
			this.metadata = this.getBlock().getMetaFromState(this);
		}

		return this.metadata;
	}
}