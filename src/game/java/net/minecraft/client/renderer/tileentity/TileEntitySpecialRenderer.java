package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
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
public abstract class TileEntitySpecialRenderer<T extends TileEntity> {
	protected static final ResourceLocation[] DESTROY_STAGES = new ResourceLocation[] {
			new ResourceLocation("textures/blocks/destroy_stage_0.png"),
			new ResourceLocation("textures/blocks/destroy_stage_1.png"),
			new ResourceLocation("textures/blocks/destroy_stage_2.png"),
			new ResourceLocation("textures/blocks/destroy_stage_3.png"),
			new ResourceLocation("textures/blocks/destroy_stage_4.png"),
			new ResourceLocation("textures/blocks/destroy_stage_5.png"),
			new ResourceLocation("textures/blocks/destroy_stage_6.png"),
			new ResourceLocation("textures/blocks/destroy_stage_7.png"),
			new ResourceLocation("textures/blocks/destroy_stage_8.png"),
			new ResourceLocation("textures/blocks/destroy_stage_9.png") };
	protected TileEntityRendererDispatcher rendererDispatcher;

	public abstract void renderTileEntityAt(T var1, double var2, double var4, double var6, float var8, int var9);

	protected void bindTexture(ResourceLocation location) {
		TextureManager texturemanager = this.rendererDispatcher.renderEngine;
		if (texturemanager != null) {
			texturemanager.bindTexture(location);
		}

	}

	protected World getWorld() {
		return this.rendererDispatcher.worldObj;
	}

	public void setRendererDispatcher(TileEntityRendererDispatcher rendererDispatcherIn) {
		this.rendererDispatcher = rendererDispatcherIn;
	}

	public FontRenderer getFontRenderer() {
		return this.rendererDispatcher.getFontRenderer();
	}

	public boolean func_181055_a() {
		return false;
	}
}