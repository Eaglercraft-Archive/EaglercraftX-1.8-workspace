package net.minecraft.block.state.pattern;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;

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
public class BlockStateHelper implements Predicate<IBlockState> {
	private final BlockState blockstate;
	private final Map<IProperty, Predicate> propertyPredicates = Maps.newHashMap();

	private BlockStateHelper(BlockState blockStateIn) {
		this.blockstate = blockStateIn;
	}

	public static BlockStateHelper forBlock(Block blockIn) {
		return new BlockStateHelper(blockIn.getBlockState());
	}

	public boolean apply(IBlockState iblockstate) {
		if (iblockstate != null && iblockstate.getBlock().equals(this.blockstate.getBlock())) {
			for (Entry entry : this.propertyPredicates.entrySet()) {
				Comparable comparable = iblockstate.getValue((IProperty) entry.getKey());
				if (!((Predicate) entry.getValue()).apply(comparable)) {
					return false;
				}
			}

			return true;
		} else {
			return false;
		}
	}

	public <V extends Comparable<V>> BlockStateHelper where(IProperty<V> property, Predicate<? extends V> is) {
		if (!this.blockstate.getProperties().contains(property)) {
			throw new IllegalArgumentException(this.blockstate + " cannot support property " + property);
		} else {
			this.propertyPredicates.put(property, is);
			return this;
		}
	}
}