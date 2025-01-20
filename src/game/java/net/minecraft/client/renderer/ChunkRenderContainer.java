package net.minecraft.client.renderer;

import java.util.List;

import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.dynamiclights.DynamicLightsStateManager;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;

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
public abstract class ChunkRenderContainer {
	private double viewEntityX;
	private double viewEntityY;
	private double viewEntityZ;
	protected List<RenderChunk> renderChunks = Lists.newArrayListWithCapacity(17424);
	protected boolean initialized;

	public void initialize(double viewEntityXIn, double viewEntityYIn, double viewEntityZIn) {
		this.initialized = true;
		this.renderChunks.clear();
		this.viewEntityX = viewEntityXIn;
		this.viewEntityY = viewEntityYIn;
		this.viewEntityZ = viewEntityZIn;
	}

	public void preRenderChunk(RenderChunk renderChunkIn, EnumWorldBlockLayer enumworldblocklayer) {
		BlockPos blockpos = renderChunkIn.getPosition();
		float posX = (float) ((double) blockpos.getX() - this.viewEntityX);
		float posY = (float) ((double) blockpos.getY() - this.viewEntityY);
		float posZ = (float) ((double) blockpos.getZ() - this.viewEntityZ);
		GlStateManager.translate(posX, posY, posZ);
		if (DeferredStateManager.isInForwardPass()) {
			posX = (float) (blockpos.getX() - (MathHelper.floor_double(this.viewEntityX / 16.0) << 4));
			posY = (float) (blockpos.getY() - (MathHelper.floor_double(this.viewEntityY / 16.0) << 4));
			posZ = (float) (blockpos.getZ() - (MathHelper.floor_double(this.viewEntityZ / 16.0) << 4));
			DeferredStateManager.reportForwardRenderObjectPosition((int) posX, (int) posY, (int) posZ);
		} else if (DynamicLightsStateManager.isInDynamicLightsPass()) {
			posX = (float) (blockpos.getX() - (MathHelper.floor_double(this.viewEntityX / 16.0) << 4));
			posY = (float) (blockpos.getY() - (MathHelper.floor_double(this.viewEntityY / 16.0) << 4));
			posZ = (float) (blockpos.getZ() - (MathHelper.floor_double(this.viewEntityZ / 16.0) << 4));
			DynamicLightsStateManager.reportForwardRenderObjectPosition((int) posX, (int) posY, (int) posZ);
		}
	}

	public void addRenderChunk(RenderChunk renderChunkIn, EnumWorldBlockLayer layer) {
		this.renderChunks.add(renderChunkIn);
	}

	public abstract void renderChunkLayer(EnumWorldBlockLayer var1);
}