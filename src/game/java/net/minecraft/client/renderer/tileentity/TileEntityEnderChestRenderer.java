package net.minecraft.client.renderer.tileentity;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.model.ModelChest;
import net.minecraft.tileentity.TileEntityEnderChest;
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
public class TileEntityEnderChestRenderer extends TileEntitySpecialRenderer<TileEntityEnderChest> {
	private static final ResourceLocation ENDER_CHEST_TEXTURE = new ResourceLocation("textures/entity/chest/ender.png");
	private ModelChest field_147521_c = new ModelChest();

	public void renderTileEntityAt(TileEntityEnderChest tileentityenderchest, double d0, double d1, double d2, float f,
			int i) {
		int j = 0;
		if (tileentityenderchest.hasWorldObj()) {
			j = tileentityenderchest.getBlockMetadata();
		}

		if (i >= 0) {
			this.bindTexture(DESTROY_STAGES[i]);
			GlStateManager.matrixMode(GL_TEXTURE);
			GlStateManager.pushMatrix();
			GlStateManager.scale(4.0F, 4.0F, 1.0F);
			GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
			GlStateManager.matrixMode(GL_MODELVIEW);
		} else {
			this.bindTexture(ENDER_CHEST_TEXTURE);
		}

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.translate((float) d0, (float) d1 + 1.0F, (float) d2 + 1.0F);
		GlStateManager.scale(1.0F, -1.0F, -1.0F);
		GlStateManager.translate(0.5F, 0.5F, 0.5F);
		short short1 = 0;
		if (j == 2) {
			short1 = 180;
		}

		if (j == 3) {
			short1 = 0;
		}

		if (j == 4) {
			short1 = 90;
		}

		if (j == 5) {
			short1 = -90;
		}

		GlStateManager.rotate((float) short1, 0.0F, 1.0F, 0.0F);
		GlStateManager.translate(-0.5F, -0.5F, -0.5F);
		float f1 = tileentityenderchest.prevLidAngle
				+ (tileentityenderchest.lidAngle - tileentityenderchest.prevLidAngle) * f;
		f1 = 1.0F - f1;
		f1 = 1.0F - f1 * f1 * f1;
		this.field_147521_c.chestLid.rotateAngleX = -(f1 * 3.1415927F / 2.0F);
		this.field_147521_c.renderAll();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		if (i >= 0) {
			GlStateManager.matrixMode(GL_TEXTURE);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(GL_MODELVIEW);
		}

	}
}