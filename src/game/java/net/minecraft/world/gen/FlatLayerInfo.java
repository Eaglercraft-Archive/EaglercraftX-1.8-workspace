package net.minecraft.world.gen;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
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
public class FlatLayerInfo {
	private final int field_175902_a;
	private IBlockState field_175901_b;
	private int layerCount;
	private int layerMinimumY;

	public FlatLayerInfo(int parInt1, Block parBlock) {
		this(3, parInt1, parBlock);
	}

	public FlatLayerInfo(int parInt1, int parInt2, Block parBlock) {
		this.layerCount = 1;
		this.field_175902_a = parInt1;
		this.layerCount = parInt2;
		this.field_175901_b = parBlock.getDefaultState();
	}

	public FlatLayerInfo(int parInt1, int parInt2, Block parBlock, int parInt3) {
		this(parInt1, parInt2, parBlock);
		this.field_175901_b = parBlock.getStateFromMeta(parInt3);
	}

	/**+
	 * Return the amount of layers for this set of layers.
	 */
	public int getLayerCount() {
		return this.layerCount;
	}

	public IBlockState func_175900_c() {
		return this.field_175901_b;
	}

	private Block func_151536_b() {
		return this.field_175901_b.getBlock();
	}

	/**+
	 * Return the block metadata used on this set of layers.
	 */
	private int getFillBlockMeta() {
		return this.field_175901_b.getBlock().getMetaFromState(this.field_175901_b);
	}

	/**+
	 * Return the minimum Y coordinate for this layer, set during
	 * generation.
	 */
	public int getMinY() {
		return this.layerMinimumY;
	}

	/**+
	 * Set the minimum Y coordinate for this layer.
	 */
	public void setMinY(int parInt1) {
		this.layerMinimumY = parInt1;
	}

	public String toString() {
		String s;
		if (this.field_175902_a >= 3) {
			ResourceLocation resourcelocation = (ResourceLocation) Block.blockRegistry
					.getNameForObject(this.func_151536_b());
			s = resourcelocation == null ? "null" : resourcelocation.toString();
			if (this.layerCount > 1) {
				s = this.layerCount + "*" + s;
			}
		} else {
			s = Integer.toString(Block.getIdFromBlock(this.func_151536_b()));
			if (this.layerCount > 1) {
				s = this.layerCount + "x" + s;
			}
		}

		int i = this.getFillBlockMeta();
		if (i > 0) {
			s = s + ":" + i;
		}

		return s;
	}
}