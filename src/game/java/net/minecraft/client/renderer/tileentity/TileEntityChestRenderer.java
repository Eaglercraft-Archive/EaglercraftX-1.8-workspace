package net.minecraft.client.renderer.tileentity;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.util.Calendar;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.tileentity.TileEntityChest;
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
public class TileEntityChestRenderer extends TileEntitySpecialRenderer<TileEntityChest> {
	private static final ResourceLocation textureTrappedDouble = new ResourceLocation(
			"textures/entity/chest/trapped_double.png");
	private static final ResourceLocation textureChristmasDouble = new ResourceLocation(
			"textures/entity/chest/christmas_double.png");
	private static final ResourceLocation textureNormalDouble = new ResourceLocation(
			"textures/entity/chest/normal_double.png");
	private static final ResourceLocation textureTrapped = new ResourceLocation("textures/entity/chest/trapped.png");
	private static final ResourceLocation textureChristmas = new ResourceLocation(
			"textures/entity/chest/christmas.png");
	private static final ResourceLocation textureNormal = new ResourceLocation("textures/entity/chest/normal.png");
	private ModelChest simpleChest = new ModelChest();
	private ModelChest largeChest = new ModelLargeChest();
	private boolean isChristams;

	public TileEntityChestRenderer() {
		Calendar calendar = Calendar.getInstance();
		if (calendar.get(2) + 1 == 12 && calendar.get(5) >= 24 && calendar.get(5) <= 26) {
			this.isChristams = true;
		}

	}

	public void renderTileEntityAt(TileEntityChest tileentitychest, double d0, double d1, double d2, float f, int i) {
		GlStateManager.enableDepth();
		GlStateManager.depthFunc(GL_LEQUAL);
		GlStateManager.depthMask(true);
		int j;
		if (!tileentitychest.hasWorldObj()) {
			j = 0;
		} else {
			Block block = tileentitychest.getBlockType();
			j = tileentitychest.getBlockMetadata();
			if (block instanceof BlockChest && j == 0) {
				((BlockChest) block).checkForSurroundingChests(tileentitychest.getWorld(), tileentitychest.getPos(),
						tileentitychest.getWorld().getBlockState(tileentitychest.getPos()));
				j = tileentitychest.getBlockMetadata();
			}

			tileentitychest.checkForAdjacentChests();
		}

		if (tileentitychest.adjacentChestZNeg == null && tileentitychest.adjacentChestXNeg == null) {
			ModelChest modelchest;
			if (tileentitychest.adjacentChestXPos == null && tileentitychest.adjacentChestZPos == null) {
				modelchest = this.simpleChest;
				if (i >= 0) {
					this.bindTexture(DESTROY_STAGES[i]);
					GlStateManager.matrixMode(GL_TEXTURE);
					GlStateManager.pushMatrix();
					GlStateManager.scale(4.0F, 4.0F, 1.0F);
					GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
					GlStateManager.matrixMode(GL_MODELVIEW);
				} else if (this.isChristams) {
					this.bindTexture(textureChristmas);
				} else if (tileentitychest.getChestType() == 1) {
					this.bindTexture(textureTrapped);
				} else {
					this.bindTexture(textureNormal);
				}
			} else {
				modelchest = this.largeChest;
				if (i >= 0) {
					this.bindTexture(DESTROY_STAGES[i]);
					GlStateManager.matrixMode(GL_TEXTURE);
					GlStateManager.pushMatrix();
					GlStateManager.scale(8.0F, 4.0F, 1.0F);
					GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
					GlStateManager.matrixMode(GL_MODELVIEW);
				} else if (this.isChristams) {
					this.bindTexture(textureChristmasDouble);
				} else if (tileentitychest.getChestType() == 1) {
					this.bindTexture(textureTrappedDouble);
				} else {
					this.bindTexture(textureNormalDouble);
				}
			}

			GlStateManager.pushMatrix();
			GlStateManager.enableRescaleNormal();
			if (i < 0) {
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			}

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

			if (j == 2 && tileentitychest.adjacentChestXPos != null) {
				GlStateManager.translate(1.0F, 0.0F, 0.0F);
			}

			if (j == 5 && tileentitychest.adjacentChestZPos != null) {
				GlStateManager.translate(0.0F, 0.0F, -1.0F);
			}

			GlStateManager.rotate((float) short1, 0.0F, 1.0F, 0.0F);
			GlStateManager.translate(-0.5F, -0.5F, -0.5F);
			float f1 = tileentitychest.prevLidAngle + (tileentitychest.lidAngle - tileentitychest.prevLidAngle) * f;
			if (tileentitychest.adjacentChestZNeg != null) {
				float f2 = tileentitychest.adjacentChestZNeg.prevLidAngle
						+ (tileentitychest.adjacentChestZNeg.lidAngle - tileentitychest.adjacentChestZNeg.prevLidAngle)
								* f;
				if (f2 > f1) {
					f1 = f2;
				}
			}

			if (tileentitychest.adjacentChestXNeg != null) {
				float f3 = tileentitychest.adjacentChestXNeg.prevLidAngle
						+ (tileentitychest.adjacentChestXNeg.lidAngle - tileentitychest.adjacentChestXNeg.prevLidAngle)
								* f;
				if (f3 > f1) {
					f1 = f3;
				}
			}

			f1 = 1.0F - f1;
			f1 = 1.0F - f1 * f1 * f1;
			modelchest.chestLid.rotateAngleX = -(f1 * 3.1415927F / 2.0F);
			modelchest.renderAll();
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
}