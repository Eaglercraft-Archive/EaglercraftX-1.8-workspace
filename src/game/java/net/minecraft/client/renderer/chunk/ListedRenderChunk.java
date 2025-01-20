package net.minecraft.client.renderer.chunk;

import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;

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
public class ListedRenderChunk extends RenderChunk {
	private final int[] baseDisplayList;

	public ListedRenderChunk(World worldIn, RenderGlobal renderGlobalIn, BlockPos pos, int indexIn) {
		super(worldIn, renderGlobalIn, pos, indexIn);
		this.baseDisplayList = new int[EnumWorldBlockLayer._VALUES.length];
		for (int i = 0; i < this.baseDisplayList.length; ++i) {
			this.baseDisplayList[i] = GLAllocation.generateDisplayLists();
		}
	}

	public int getDisplayList(EnumWorldBlockLayer layer, CompiledChunk parCompiledChunk) {
		return !parCompiledChunk.isLayerEmpty(layer) ? this.baseDisplayList[layer.ordinal()] : -1;
	}

	public void deleteGlResources() {
		super.deleteGlResources();
		for (int i = 0; i < this.baseDisplayList.length; ++i) {
			GLAllocation.deleteDisplayLists(this.baseDisplayList[i]);
		}
	}

	public void rebuildChunk(float x, float y, float z, ChunkCompileTaskGenerator generator) {
		super.rebuildChunk(x, y, z, generator);
		EnumWorldBlockLayer[] layers = EnumWorldBlockLayer._VALUES;
		for (int i = 0; i < layers.length; ++i) {
			if (generator.getCompiledChunk().isLayerEmpty(layers[i])) {
				EaglercraftGPU.flushDisplayList(this.baseDisplayList[i]);
			}
		}
	}
}