package net.minecraft.client.renderer.chunk;

import java.util.List;

import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.util.EnumWorldBlockLayer;

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
public class ChunkCompileTaskGenerator {
	private final RenderChunk renderChunk;
	private final List<Runnable> listFinishRunnables = Lists.newArrayList();
	private final ChunkCompileTaskGenerator.Type type;
	private RegionRenderCacheBuilder regionRenderCacheBuilder;
	private CompiledChunk compiledChunk;
	private ChunkCompileTaskGenerator.Status status = ChunkCompileTaskGenerator.Status.PENDING;
	private boolean finished;
	public long goddamnFuckingTimeout = 0l;
	public long time = 0;

	public ChunkCompileTaskGenerator(RenderChunk renderChunkIn, ChunkCompileTaskGenerator.Type typeIn) {
		this.renderChunk = renderChunkIn;
		this.type = typeIn;
	}

	public ChunkCompileTaskGenerator.Status getStatus() {
		return this.status;
	}

	public RenderChunk getRenderChunk() {
		return this.renderChunk;
	}

	public CompiledChunk getCompiledChunk() {
		return this.compiledChunk;
	}

	public void setCompiledChunk(CompiledChunk compiledChunkIn) {
		this.compiledChunk = compiledChunkIn;
	}

	public RegionRenderCacheBuilder getRegionRenderCacheBuilder() {
		return this.regionRenderCacheBuilder;
	}

	public void setRegionRenderCacheBuilder(RegionRenderCacheBuilder regionRenderCacheBuilderIn) {
		this.regionRenderCacheBuilder = regionRenderCacheBuilderIn;
	}

	public void setStatus(ChunkCompileTaskGenerator.Status statusIn) {
		this.status = statusIn;
	}

	public void finish() {
		if (this.type == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK
				&& this.status != ChunkCompileTaskGenerator.Status.DONE) {
			this.renderChunk.setNeedsUpdate(true);
		}

		this.finished = true;
		this.status = ChunkCompileTaskGenerator.Status.DONE;

		for (int i = 0, l = this.listFinishRunnables.size(); i < l; ++i) {
			this.listFinishRunnables.get(i).run();
		}
	}

	public void addFinishRunnable(Runnable parRunnable) {
		this.listFinishRunnables.add(parRunnable);
		if (this.finished) {
			parRunnable.run();
		}
	}

	public ChunkCompileTaskGenerator.Type getType() {
		return this.type;
	}

	public boolean isFinished() {
		return this.finished;
	}

	public boolean canExecuteYet() {
		if (this.type == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY) {
			CompiledChunk ch = this.renderChunk.getCompiledChunk();
			if (DeferredStateManager.isRenderingRealisticWater()) {
				return !ch.isLayerEmpty(EnumWorldBlockLayer.TRANSLUCENT)
						|| !ch.isLayerEmpty(EnumWorldBlockLayer.REALISTIC_WATER);
			} else {
				return !ch.isLayerEmpty(EnumWorldBlockLayer.TRANSLUCENT);
			}
		} else {
			return true;
		}
	}

	public static enum Status {
		PENDING, COMPILING, UPLOADING, DONE;
	}

	public static enum Type {
		REBUILD_CHUNK, RESORT_TRANSPARENCY;
	}
}