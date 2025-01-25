package net.minecraft.client.renderer.block.statemap;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;
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
public class StateMap extends StateMapperBase {
	private final IProperty<?> name;
	private final String suffix;
	private final List<IProperty<?>> ignored;

	private StateMap(IProperty<?> name, String suffix, List<IProperty<?>> ignored) {
		this.name = name;
		this.suffix = suffix;
		this.ignored = ignored;
	}

	protected ModelResourceLocation getModelResourceLocation(IBlockState iblockstate) {
		LinkedHashMap<IProperty, Comparable> linkedhashmap = Maps.newLinkedHashMap(iblockstate.getProperties());
		String s;
		if (this.name == null) {
			s = ((ResourceLocation) Block.blockRegistry.getNameForObject(iblockstate.getBlock())).toString();
		} else {
			s = this.name.getName(linkedhashmap.remove(this.name));
		}

		if (this.suffix != null) {
			s = s + this.suffix;
		}

		for (IProperty iproperty : this.ignored) {
			linkedhashmap.remove(iproperty);
		}

		return new ModelResourceLocation(s, getPropertyString(linkedhashmap));
	}

	public static class Builder {
		private IProperty<?> name;
		private String suffix;
		private final List<IProperty<?>> ignored = Lists.newArrayList();

		public StateMap.Builder withName(IProperty<?> builderPropertyIn) {
			this.name = builderPropertyIn;
			return this;
		}

		public StateMap.Builder withSuffix(String builderSuffixIn) {
			this.suffix = builderSuffixIn;
			return this;
		}

		public StateMap.Builder ignore(IProperty<?>... parArrayOfIProperty) {
			Collections.addAll(this.ignored, parArrayOfIProperty);
			return this;
		}

		public StateMap build() {
			return new StateMap(this.name, this.suffix, this.ignored);
		}
	}
}