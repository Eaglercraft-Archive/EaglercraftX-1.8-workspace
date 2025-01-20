package net.minecraft.client.renderer.chunk;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
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
public class CompiledChunk {
	public static final CompiledChunk DUMMY = new CompiledChunk(null) {
		protected void setLayerUsed(EnumWorldBlockLayer layer) {
			throw new UnsupportedOperationException();
		}

		public void setLayerStarted(EnumWorldBlockLayer layer) {
			throw new UnsupportedOperationException();
		}

		public boolean isVisible(EnumFacing facing, EnumFacing facing2) {
			return true;
		}
	};
	private final RenderChunk chunk;
	private final boolean[] layersUsed = new boolean[EnumWorldBlockLayer._VALUES.length];
	private final boolean[] layersStarted = new boolean[EnumWorldBlockLayer._VALUES.length];
	private boolean empty = true;
	private final List<TileEntity> tileEntities = Lists.newArrayList();
	private SetVisibility setVisibility = new SetVisibility();
	private WorldRenderer.State state;
	private WorldRenderer.State stateWater;

	public CompiledChunk(RenderChunk chunk) {
		this.chunk = chunk;
	}

	public void reset() {
		Arrays.fill(layersUsed, false);
		Arrays.fill(layersStarted, false);
		empty = true;
		tileEntities.clear();
		setVisibility.setAllVisible(false);
		setState(null);
		setStateRealisticWater(null);
	}

	public boolean isEmpty() {
		return this.empty;
	}

	protected void setLayerUsed(EnumWorldBlockLayer enumworldblocklayer) {
		this.empty = false;
		this.layersUsed[enumworldblocklayer.ordinal()] = true;
	}

	public boolean isLayerEmpty(EnumWorldBlockLayer layer) {
		return !this.layersUsed[layer.ordinal()];
	}

	public void setLayerStarted(EnumWorldBlockLayer enumworldblocklayer) {
		this.layersStarted[enumworldblocklayer.ordinal()] = true;
	}

	public boolean isLayerStarted(EnumWorldBlockLayer layer) {
		return this.layersStarted[layer.ordinal()];
	}

	public List<TileEntity> getTileEntities() {
		return this.tileEntities;
	}

	public void addTileEntity(TileEntity tileEntityIn) {
		this.tileEntities.add(tileEntityIn);
	}

	public boolean isVisible(EnumFacing enumfacing, EnumFacing enumfacing1) {
		return this.setVisibility.isVisible(enumfacing, enumfacing1);
	}

	public void setVisibility(SetVisibility visibility) {
		this.setVisibility = visibility;
	}

	public WorldRenderer.State getState() {
		return this.state;
	}

	public void setState(WorldRenderer.State stateIn) {
		if (this.state != stateIn && this.state != null) {
			this.state.release();
		}
		this.state = stateIn;
	}

	public WorldRenderer.State getStateRealisticWater() {
		return this.stateWater;
	}

	public void setStateRealisticWater(WorldRenderer.State stateIn) {
		if (this.stateWater != stateIn && this.stateWater != null) {
			this.stateWater.release();
		}
		this.stateWater = stateIn;
	}
}