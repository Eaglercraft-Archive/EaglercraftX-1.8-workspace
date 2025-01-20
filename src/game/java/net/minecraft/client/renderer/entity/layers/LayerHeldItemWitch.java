package net.minecraft.client.renderer.entity.layers;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelWitch;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderWitch;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

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
public class LayerHeldItemWitch implements LayerRenderer<EntityWitch> {
	private final RenderWitch witchRenderer;

	public LayerHeldItemWitch(RenderWitch witchRendererIn) {
		this.witchRenderer = witchRendererIn;
	}

	public void doRenderLayer(EntityWitch entitywitch, float var2, float var3, float var4, float var5, float var6,
			float var7, float var8) {
		ItemStack itemstack = entitywitch.getHeldItem();
		if (itemstack != null) {
			GlStateManager.color(1.0F, 1.0F, 1.0F);
			GlStateManager.pushMatrix();
			if (this.witchRenderer.getMainModel().isChild) {
				GlStateManager.translate(0.0F, 0.625F, 0.0F);
				GlStateManager.rotate(-20.0F, -1.0F, 0.0F, 0.0F);
				float f = 0.5F;
				GlStateManager.scale(f, f, f);
			}

			((ModelWitch) this.witchRenderer.getMainModel()).villagerNose.postRender(0.0625F);
			GlStateManager.translate(-0.0625F, 0.53125F, 0.21875F);
			Item item = itemstack.getItem();
			Minecraft minecraft = Minecraft.getMinecraft();
			if (item instanceof ItemBlock && minecraft.getBlockRendererDispatcher()
					.isRenderTypeChest(Block.getBlockFromItem(item), itemstack.getMetadata())) {
				GlStateManager.translate(0.0F, 0.0625F, -0.25F);
				GlStateManager.rotate(30.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(-5.0F, 0.0F, 1.0F, 0.0F);
				float f4 = 0.375F;
				GlStateManager.scale(f4, -f4, f4);
			} else if (item == Items.bow) {
				GlStateManager.translate(0.0F, 0.125F, -0.125F);
				GlStateManager.rotate(-45.0F, 0.0F, 1.0F, 0.0F);
				float f1 = 0.625F;
				GlStateManager.scale(f1, -f1, f1);
				GlStateManager.rotate(-100.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(-20.0F, 0.0F, 1.0F, 0.0F);
			} else if (item.isFull3D()) {
				if (item.shouldRotateAroundWhenRendering()) {
					GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
					GlStateManager.translate(0.0F, -0.0625F, 0.0F);
				}

				this.witchRenderer.transformHeldFull3DItemLayer();
				GlStateManager.translate(0.0625F, -0.125F, 0.0F);
				float f2 = 0.625F;
				GlStateManager.scale(f2, -f2, f2);
				GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(0.0F, 0.0F, 1.0F, 0.0F);
			} else {
				GlStateManager.translate(0.1875F, 0.1875F, 0.0F);
				float f3 = 0.875F;
				GlStateManager.scale(f3, f3, f3);
				GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
				GlStateManager.rotate(-60.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(-30.0F, 0.0F, 0.0F, 1.0F);
			}

			GlStateManager.rotate(-15.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(40.0F, 0.0F, 0.0F, 1.0F);
			minecraft.getItemRenderer().renderItem(entitywitch, itemstack,
					ItemCameraTransforms.TransformType.THIRD_PERSON);
			GlStateManager.popMatrix();
		}
	}

	public boolean shouldCombineTextures() {
		return false;
	}
}