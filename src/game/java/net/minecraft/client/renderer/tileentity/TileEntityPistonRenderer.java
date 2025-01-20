package net.minecraft.client.renderer.tileentity;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.VertexFormat;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.BlockPos;
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
public class TileEntityPistonRenderer extends TileEntitySpecialRenderer<TileEntityPiston> {
	private final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();

	public void renderTileEntityAt(TileEntityPiston tileentitypiston, double d0, double d1, double d2, float f,
			int var9) {
		BlockPos blockpos = tileentitypiston.getPos();
		IBlockState iblockstate = tileentitypiston.getPistonState();
		Block block = iblockstate.getBlock();
		if (block.getMaterial() != Material.air && tileentitypiston.getProgress(f) < 1.0F) {
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			this.bindTexture(TextureMap.locationBlocksTexture);
			RenderHelper.disableStandardItemLighting();
			GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.enableBlend();
			GlStateManager.disableCull();
			if (Minecraft.isAmbientOcclusionEnabled()) {
				GlStateManager.shadeModel(GL_SMOOTH);
			} else {
				GlStateManager.shadeModel(GL_FLAT);
			}

			worldrenderer.begin(7, DeferredStateManager.isDeferredRenderer() ? VertexFormat.BLOCK_SHADERS
					: DefaultVertexFormats.BLOCK);
			worldrenderer.setTranslation(
					(double) ((float) d0 - (float) blockpos.getX() + tileentitypiston.getOffsetX(f)),
					(double) ((float) d1 - (float) blockpos.getY() + tileentitypiston.getOffsetY(f)),
					(double) ((float) d2 - (float) blockpos.getZ() + tileentitypiston.getOffsetZ(f)));
			World world = this.getWorld();
			if (block == Blocks.piston_head && tileentitypiston.getProgress(f) < 0.5F) {
				iblockstate = iblockstate.withProperty(BlockPistonExtension.SHORT, Boolean.valueOf(true));
				this.blockRenderer.getBlockModelRenderer().renderModel(world,
						this.blockRenderer.getModelFromBlockState(iblockstate, world, blockpos), iblockstate, blockpos,
						worldrenderer, true);
			} else if (tileentitypiston.shouldPistonHeadBeRendered() && !tileentitypiston.isExtending()) {
				BlockPistonExtension.EnumPistonType blockpistonextension$enumpistontype = block == Blocks.sticky_piston
						? BlockPistonExtension.EnumPistonType.STICKY
						: BlockPistonExtension.EnumPistonType.DEFAULT;
				IBlockState iblockstate1 = Blocks.piston_head.getDefaultState()
						.withProperty(BlockPistonExtension.TYPE, blockpistonextension$enumpistontype)
						.withProperty(BlockPistonExtension.FACING, iblockstate.getValue(BlockPistonBase.FACING));
				iblockstate1 = iblockstate1.withProperty(BlockPistonExtension.SHORT,
						Boolean.valueOf(tileentitypiston.getProgress(f) >= 0.5F));
				this.blockRenderer.getBlockModelRenderer().renderModel(world,
						this.blockRenderer.getModelFromBlockState(iblockstate1, world, blockpos), iblockstate1,
						blockpos, worldrenderer, true);
				worldrenderer.setTranslation((double) ((float) d0 - (float) blockpos.getX()),
						(double) ((float) d1 - (float) blockpos.getY()),
						(double) ((float) d2 - (float) blockpos.getZ()));
				iblockstate.withProperty(BlockPistonBase.EXTENDED, Boolean.valueOf(true));
				this.blockRenderer.getBlockModelRenderer().renderModel(world,
						this.blockRenderer.getModelFromBlockState(iblockstate, world, blockpos), iblockstate, blockpos,
						worldrenderer, true);
			} else {
				this.blockRenderer.getBlockModelRenderer().renderModel(world,
						this.blockRenderer.getModelFromBlockState(iblockstate, world, blockpos), iblockstate, blockpos,
						worldrenderer, false);
			}

			worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
			tessellator.draw();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableCull();
			GlStateManager.disableBlend();
		}
	}
}