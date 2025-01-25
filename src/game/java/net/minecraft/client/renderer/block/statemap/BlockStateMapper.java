package net.minecraft.client.renderer.block.statemap;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;

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
public class BlockStateMapper {
	public Map<Block, IStateMapper> blockStateMap = Maps.newIdentityHashMap();
	private Set<Block> setBuiltInBlocks = Sets.newIdentityHashSet();

	public void registerBlockStateMapper(Block parBlock, IStateMapper parIStateMapper) {
		this.blockStateMap.put(parBlock, parIStateMapper);
	}

	public void registerBuiltInBlocks(Block... parArrayOfBlock) {
		Collections.addAll(this.setBuiltInBlocks, parArrayOfBlock);
	}

	public Map<IBlockState, ModelResourceLocation> putAllStateModelLocations() {
		IdentityHashMap identityhashmap = Maps.newIdentityHashMap();

		for (Block block : Block.blockRegistry) {
			if (!this.setBuiltInBlocks.contains(block)) {
				identityhashmap.putAll(
						((IStateMapper) Objects.firstNonNull(this.blockStateMap.get(block), new DefaultStateMapper()))
								.putStateModelLocations(block));
			}
		}

		return identityhashmap;
	}
}