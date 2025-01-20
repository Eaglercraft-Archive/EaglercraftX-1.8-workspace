package net.minecraft.client.renderer.chunk;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.minecraft.ChunkUpdateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.entity.Entity;
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
public class ChunkRenderWorker {
	private static final Logger LOGGER = LogManager.getLogger();
	private final ChunkUpdateManager chunkRenderDispatcher;
	private final RegionRenderCacheBuilder regionRenderCacheBuilder;

	public ChunkRenderWorker(ChunkUpdateManager parChunkRenderDispatcher) {
		this(parChunkRenderDispatcher, (RegionRenderCacheBuilder) null);
	}

	public ChunkRenderWorker(ChunkUpdateManager chunkRenderDispatcherIn,
			RegionRenderCacheBuilder regionRenderCacheBuilderIn) {
		this.chunkRenderDispatcher = chunkRenderDispatcherIn;
		this.regionRenderCacheBuilder = regionRenderCacheBuilderIn;
	}

	protected void processTask(final ChunkCompileTaskGenerator generator) throws InterruptedException {
		if (generator.getStatus() != ChunkCompileTaskGenerator.Status.PENDING) {
			if (!generator.isFinished()) {
				LOGGER.warn("Chunk render task was " + generator.getStatus()
						+ " when I expected it to be pending; ignoring task");
			}

			return;
		}

		generator.setStatus(ChunkCompileTaskGenerator.Status.COMPILING);

		Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
		if (entity == null) {
			generator.finish();
		} else {
			generator.setRegionRenderCacheBuilder(this.getRegionRenderCacheBuilder());
			float f = (float) entity.posX;
			float f1 = (float) entity.posY + entity.getEyeHeight();
			float f2 = (float) entity.posZ;
			ChunkCompileTaskGenerator.Type chunkcompiletaskgenerator$type = generator.getType();
			if (chunkcompiletaskgenerator$type == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK) {
				generator.getRenderChunk().rebuildChunk(f, f1, f2, generator);
			} else if (chunkcompiletaskgenerator$type == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY) {
				generator.getRenderChunk().resortTransparency(f, f1, f2, generator);
			}

			if (generator.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING) {
				if (!generator.isFinished()) {
					LOGGER.warn("Chunk render task was " + generator.getStatus()
							+ " when I expected it to be compiling; aborting task");
				}

				this.freeRenderBuilder(generator);
				return;
			}

			generator.setStatus(ChunkCompileTaskGenerator.Status.UPLOADING);

			final CompiledChunk compiledchunk = generator.getCompiledChunk();
			if (chunkcompiletaskgenerator$type == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK) {
				EnumWorldBlockLayer[] layers = EnumWorldBlockLayer._VALUES;
				for (int i = 0; i < layers.length; ++i) {
					EnumWorldBlockLayer enumworldblocklayer = layers[i];
					if (!compiledchunk.isLayerEmpty(enumworldblocklayer)) {
						this.chunkRenderDispatcher.uploadChunk(enumworldblocklayer,
								generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(enumworldblocklayer),
								generator.getRenderChunk(), compiledchunk);
						generator.setStatus(ChunkCompileTaskGenerator.Status.DONE);
					}
				}
			} else if (chunkcompiletaskgenerator$type == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY) {
				this.chunkRenderDispatcher.uploadChunk(
						EnumWorldBlockLayer.TRANSLUCENT, generator.getRegionRenderCacheBuilder()
								.getWorldRendererByLayer(EnumWorldBlockLayer.TRANSLUCENT),
						generator.getRenderChunk(), compiledchunk);
				if (DeferredStateManager.isRenderingRealisticWater()) {
					this.chunkRenderDispatcher.uploadChunk(
							EnumWorldBlockLayer.REALISTIC_WATER, generator.getRegionRenderCacheBuilder()
									.getWorldRendererByLayer(EnumWorldBlockLayer.REALISTIC_WATER),
							generator.getRenderChunk(), compiledchunk);
				}
				generator.setStatus(ChunkCompileTaskGenerator.Status.DONE);
			}

		}
	}

	private RegionRenderCacheBuilder getRegionRenderCacheBuilder() throws InterruptedException {
		return this.regionRenderCacheBuilder;
	}

	private void freeRenderBuilder(ChunkCompileTaskGenerator taskGenerator) {

	}
}